package com.reactionmachine.core;

import com.reactionmachine.config.GameConfiguration;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnhancedReactionControllerTest {
    private EnhancedReactionController controller;
    private String displayText;
    private int randomNumber;

    @Before
    public void setUp() {
        GameConfiguration config = new GameConfiguration.Builder()
                .maxAttempts(3)
                .timeoutSeconds(9.99)
                .delayRange(100, 250)
                .maxReactionTime(2.0)
                .build();

        controller = new EnhancedReactionController(config);
        TestGui gui = new TestGui();
        TestRandomGenerator randomGenerator = new TestRandomGenerator();

        gui.connect(controller);
        controller.connect((IGui) gui, (IRandom) randomGenerator);
        gui.init();
    }

    @Test
    public void testCompleteGameFlow() {
        // InitialState
        assertEquals("Insert coin", doReset());
        assertEquals("Insert coin", doGoStop()); // goStop in InitialState - no effect
        assertEquals("Insert coin", doTicks(1)); // ticks in InitialState - no effect
        assertEquals("Press GO!", doInsertCoin()); // insertCoin in InitialState - transition to CoinInsertedState

        // CoinInsertedState
        assertEquals("Press GO!", doInsertCoin()); // insertCoin in CoinInsertedState - no effect
        assertEquals("Press GO!", doTicks(999)); // < 999 ticks in CoinInsertedState - no effect
        assertEquals("Insert coin", doTicks(1)); // 1000 ticks total (9.99 seconds) - transition to InitialState

        assertEquals("Press GO!", doInsertCoin()); // insertCoin in InitialState - transition back to CoinInsertedState
        randomNumber = 117;
        assertEquals("Wait...", doGoStop()); // press goStop in CoinInsertedState - transition to RandomDelayState

        // RandomDelayState - 1st round
        assertEquals("Wait...", doTicks(randomNumber - 1)); // ticks less than randomNumber
        assertEquals("Wait...", doInsertCoin()); // insertCoin in RandomDelayState - no effect
        assertEquals("Too early! Insert coin", doGoStop()); // press goStop in RandomDelayState - transition to InitialState

        assertEquals("Press GO!", doInsertCoin()); // insertCoin in InitialState - transition back to CoinInsertedState
        randomNumber = 122;
        assertEquals("Wait...", doGoStop()); // press goStop in CoinInserted State - transition back to RandomDelayState
        assertEquals("0.00", doTicks(randomNumber)); // ticks equal to randomNumber - transition to ReactionTestState

        // ReactionTestState - 1st round
        assertEquals("0.01", doTicks(1)); // 1 tick
        assertEquals("0.01", doInsertCoin()); // insertCoin in ReactionTestState - no effect
        assertEquals("1.99", doTicks(198)); // less than 200 ticks - no effect
        assertEquals("2.00 seconds", doTicks(1)); // 200 ticks - transition to DisplayResultState (timeout)

        // DisplayResultState - 1st round
        assertEquals("2.00 seconds", doInsertCoin()); // insertCoin in DisplayResultState - no effect
        assertEquals("2.00 seconds", doTicks(299)); // ticks less than 299 - no effect
        randomNumber = 166;
        assertEquals("Wait...", doTicks(1)); // ticks equal to 300 and count less than 3 - transition to RandomDelayState

        // RandomDelayState - 2nd round
        assertEquals("0.00", doTicks(randomNumber)); // ticks equal to randomNumber - transition to ReactionTestState

        // ReactionTestState - 2nd round
        assertEquals("0.11", doTicks(11)); // 11 ticks
        assertEquals("0.11 seconds", doGoStop()); // press goStop in ReactionTestState - transition to DisplayResultState

        // DisplayResultState - 2nd round
        randomNumber = 200;
        assertEquals("Wait...", doGoStop()); // press goStop in DisplayResultState and count less than 3 - transition to RandomDelayState

        // RandomDelayState - 3rd round
        assertEquals("0.00", doTicks(randomNumber)); // ticks equal to randomNumber - transition to ReactionTestState

        // ReactionTestState - 3rd round
        assertEquals("1.11", doTicks(111)); // 111 ticks
        assertEquals("1.11 seconds", doGoStop()); // press goStop in ReactionTestState - transition to DisplayResultState

        // DisplayResultState - 3rd round
        assertEquals("Average: 1.07", doGoStop()); // press goStop in DisplayResultState when count == 3 - transition to AvgResultState

        // AvgResultState
        assertEquals("Average: 1.07", doInsertCoin()); // insertCoin in AvgResultState - no effect
        assertEquals("Insert coin", doGoStop()); // press goStop in AvgResultState - transition to InitialState

        // New game
        assertEquals("Press GO!", doInsertCoin()); // insertCoin in InitialState - transition to CoinInsertedState
        randomNumber = 123;
        assertEquals("Wait...", doGoStop()); // press goStop in CoinInsertedState - transition to RandomDelayState
        assertEquals("0.98", doTicks(randomNumber + 98)); // ticks equal to randomNumber + 98 - transition to ReactionTestState
        assertEquals("0.98 seconds", doGoStop()); // press goStop in ReactionTestState - transition to DisplayResultState

        randomNumber = 100;
        assertEquals("Wait...", doGoStop()); // press goStop in DisplayResultState and count less than 3 - transition to RandomDelayState
        assertEquals("1.54", doTicks(randomNumber + 154)); // ticks equal to randomNumber + 154 - transition to ReactionTestState
        assertEquals("1.54 seconds", doGoStop()); // press goStop in ReactionTestState - transition to DisplayResultState

        randomNumber = 206;
        assertEquals("Wait...", doTicks(299)); // ticks less than 300 - no effect
        assertEquals("Wait...", doTicks(1)); // ticks equal to 300 and count less than 3 - transition to RandomDelayState
        assertEquals("1.98", doTicks(randomNumber + 198)); // ticks equal to randomNumber + 198 - transition to ReactionTestState
        assertEquals("1.98 seconds", doGoStop()); // press goStop in ReactionTestState - transition to DisplayResultState
        assertEquals("Average: 1.50", doGoStop()); // press goStop in DisplayResultState and count equal to 3 - transition to AvgResultState

        assertEquals("Average: 1.50", doTicks(499)); // less than 500 ticks - no effect
        assertEquals("Average: 1.50", doInsertCoin()); // insertCoin in AvgResultState - no effect
        assertEquals("Insert coin", doTicks(1)); // 500 ticks (4.99 seconds) - transition to initial state
    }

    @Test
    public void testStatistics() {
        // Complete one game
        doReset();
        doInsertCoin();
        randomNumber = 100;
        doGoStop();

        // Complete 3 rounds
        doTicks(randomNumber);
        doTicks(50);
        doGoStop(); // 0.50 seconds

        doGoStop();
        doTicks(randomNumber);
        doTicks(75);
        doGoStop(); // 0.75 seconds

        doGoStop();
        doTicks(randomNumber);
        doTicks(100);
        doGoStop(); // 1.00 seconds

        doGoStop(); // Should show average

        // Verify statistics are available
        assertTrue(controller.getStatistics().isPresent());
        assertEquals(1, controller.getStatistics().get().getTotalGamesPlayed());
    }

    @Test
    public void testConfigurationBuilder() {
        GameConfiguration config = new GameConfiguration.Builder()
                .maxAttempts(5)
                .timeoutSeconds(15.0)
                .delayRange(200, 400)
                .maxReactionTime(3.0)
                .build();

        assertEquals(5, config.getMaxAttempts());
        assertEquals(15.0, config.getTimeoutSeconds(), 0.01);
        assertEquals(200, config.getMinDelayMs());
        assertEquals(400, config.getMaxDelayMs());
        assertEquals(3.0, config.getMaxReactionTime(), 0.01);
    }

    @Test
    public void testAsyncReportGeneration() {
        assertNotNull(controller.generateReportAsync());
        // In a real test, you might want to wait for completion and verify content
    }

    private String doReset() {
        controller.init();
        return displayText;
    }

    private String doGoStop() {
        controller.goStopPressed();
        return displayText;
    }

    private String doInsertCoin() {
        controller.coinInserted();
        return displayText;
    }

    private String doTicks(int n) {
        for (int t = 0; t < n; t++) {
            controller.tick();
        }
        return displayText;
    }

    // Test GUI implementation
    private class TestGui implements IGui {
        private IController controller;

        public void connect(EnhancedReactionController controller) {
            this.controller = controller;
        }

        @Override
        public void connect(IController controller) {

        }

        @Override
        public void init() {
            displayText = "?reset?";
        }

        @Override
        public void setDisplay(String msg) {
            displayText = msg;
        }
    }

    // Test Random Generator implementation
    private class TestRandomGenerator implements IRandom {
        @Override
        public int getRandom(int from, int to) {
            return randomNumber;
        }
    }
}