package SimpleReactionMachine;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TesterTest {
    private static IController controller;
    private static IGui gui;
    private static String displayText;
    private static int randomNumber;
    private static int passed = 0;
    private static int count = 0;

    @Before
    public void setUp() {
        controller = new EnhancedReactionController(); // Replace with your actual controller implementation
        gui = new DummyGui();
        gui.connect(controller);
        controller.connect(gui, new RndGenerator());
        gui.init();
    }

    @Test
    public void testEnhancedTest() {
        enhancedTest();
    }

    private static void enhancedTest() {
        // InitialState
        assertEquals("Insert coin", doReset(controller));
        assertEquals("Insert coin", doGoStop(controller)); // goStop in InitialState - no effect
        assertEquals("Insert coin", doTicks(controller, 1)); // ticks in InitialState - no effect
        assertEquals("Press GO!", doInsertCoin(controller)); // insertCoin in InitialState - transition to CoinInsertedState

        // CoinInsertedState
        assertEquals("Press GO!", doInsertCoin(controller)); // insertCoin in CoinInsertedState - no effect
        assertEquals("Press GO!", doTicks(controller, 999)); // < 1000 ticks in CoinInsertedState - no effect
        assertEquals("Insert coin", doTicks(controller, 1000)); // 1000 ticks in CoinInsertedState - transition to InitialState
        assertEquals("Press GO!", doInsertCoin(controller)); // insertCoin in InitialState - transition back to CoinInsertedState
        randomNumber = 117;
        assertEquals("Wait...", doGoStop(controller)); // press goStop in CoinInsertedState - transition to RandomDelayState

        // RandomDelayState - 1st round
        assertEquals("Wait...", doTicks(controller, randomNumber - 1)); // ticks less than randomNumber
        assertEquals("Wait...", doInsertCoin(controller)); // insertCoin in RandomDelayState - no effect
        assertEquals("Insert coin", doGoStop(controller)); // press goStop in RandomDelayState - transition to InitialState
        assertEquals("Press GO!", doInsertCoin(controller)); // insertCoin in InitialState - transition back to CoinInsertedState
        randomNumber = 122;
        assertEquals("Wait...", doGoStop(controller)); // press goStop in CoinInserted State - transition back to RandomDelayState
        assertEquals("0.00", doTicks(controller, randomNumber)); // ticks equal to randomNumber - transition to ReactionTestState

        // ReactionTestState - 1st round
        assertEquals("0.01", doTicks(controller, 1)); // 1 tick
        assertEquals("0.01", doInsertCoin(controller)); // insertCoin in ReactionTestState - no effect
        assertEquals("1.99", doTicks(controller, 198)); // less than 200 ticks - no effect
        assertEquals("2.00", doTicks(controller, 1)); // 200 ticks - transition to DisplayResultState

        // DisplayResultState - 1st round
        assertEquals("2.00", doInsertCoin(controller)); // insertCoin in DisplayResultState - no effect
        assertEquals("2.00", doTicks(controller, 299)); // ticks less than 300 - no effect
        randomNumber = 166;
        assertEquals("Wait...", doTicks(controller, 1)); // ticks equal to 300 and count less than 3 - transition to RandomDelayState

        // RandomDelayState - 2nd round
        assertEquals("0.00", doTicks(controller, randomNumber)); // ticks equal to randomNumber - transition to ReactionTestState

        // ReactionTestState - 2nd round
        assertEquals("0.11", doTicks(controller, 11)); // 11 ticks
        assertEquals("0.11", doGoStop(controller));// press goStop in ReactionTestState - transition to DisplayResultState

        // DisplayResultState - 2nd round
        randomNumber = 200;
        assertEquals("Wait...", doGoStop(controller)); // press goStop in DisplayResultState and count less than 3 - transition to RandomDelayState

        // RandomDelayState - 3rd round
        assertEquals("0.00", doTicks(controller, randomNumber)); // ticks equal to randomNumber - transition to ReactionTestState

        // ReactionTestState - 3rd round
        assertEquals("1.11", doTicks(controller, 111)); // 111 ticks
        assertEquals("1.11", doGoStop(controller)); // press goStop in ReactionTestState - transition to DisplayResultState

        // DisplayResultState - 3rd round
        assertEquals("Average = 1.07", doGoStop(controller)); // press goStop in DisplayResultState when count == 3 - transition to AvgResultState

        // AvgResultState
        assertEquals("Average = 1.07", doInsertCoin(controller)); // insertCoin in AvgResultState - no effect
        assertEquals("Insert coin", doGoStop(controller)); // press goStop in AvgResultState - transition to InitialState

        // *********************************new game?
        assertEquals("Press GO!", doInsertCoin(controller)); // insertCoin in InitialState - transition to CoinInsertedState
        randomNumber = 123;
        assertEquals("Wait...", doGoStop(controller)); // press goStop in CoinInsertedState - transition to RandomDelayState
        assertEquals("0.98", doTicks(controller, randomNumber + 98)); // ticks equal to randomNumber - transition to ReactionTestState, then reactionTest value equal to 0.98
        assertEquals("0.98", doGoStop(controller)); // press goStop in ReactionTestState - transition to DisplayResultState
        randomNumber = 100;
        assertEquals("Wait...", doGoStop(controller)); // press goStop in DisplayResultState and count less than 3 - transition to RandomDelayState
        assertEquals("1.54", doTicks(controller, randomNumber + 154)); // ticks equal to randomNumber - transition to ReactionTestState, then reactionTest value equal to 1.54
        assertEquals("1.54", doGoStop(controller)); // press goStop in ReactionTestState - transition to DisplayResultState
        randomNumber = 206;
        assertEquals("Wait...", doTicks(controller, 300)); // ticks equal to 300 and count less than 3 - transition to RandomDelayState
        assertEquals("1.98", doTicks(controller, randomNumber + 198)); // ticks equal to randomNumber - transition to ReactionTestState, then reactionTest value equal to 1.98
        assertEquals("1.98", doGoStop(controller)); // press goStop in ReactionTestState - transition to DisplayResultState
        assertEquals("Average = 1.50", doGoStop(controller)); // press goStop in DisplayResultState and count equal to 3 - transition to AvgResultState
        assertEquals("Average = 1.50", doTicks(controller, 499)); // less than 500 ticks - no effect
        assertEquals("Average = 1.50", doInsertCoin(controller)); // insertCoin in AvgResultState - no effect)
        assertEquals("Insert coin", doTicks(controller, 1)); // 500 ticks - transition to initial state
    }

    private static String doReset(IController controller) {
        controller.init();
        return displayText;
    }

    private static String doGoStop(IController controller) {
        controller.goStopPressed();
        return displayText;
    }

    private static String doInsertCoin(IController controller) {
        controller.coinInserted();
        return displayText;
    }

    private static String doTicks(IController controller, int n) {
        for (int t = 0; t < n; t++) {
            controller.tick();
        }
        return displayText;
    }

    private static class DummyGui implements IGui {
        private IController controller;

        @Override
        public void connect(IController controller) {
            this.controller = controller;
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

    private static class RndGenerator implements IRandom {
        @Override
        public int getRandom(int from, int to) {
            return randomNumber;
        }
    }
}
