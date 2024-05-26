package SimpleReactionMachine;

public class Tester {
    private static IController controller;
    private static IGui gui;
    private static String displayText;
    private static int randomNumber;
    private static int passed = 0;
    private static int count = 0;

    public static void main(String[] args) {
        // Run simple test
        enhancedTest();
        System.out.println("\n=====================================\nSummary: " + passed + " tests passed out of 46");
    }

    private static void enhancedTest() {
        // Construct a ReactionController
        controller = new EnhancedReactionController();
        gui = new DummyGui();

        // Connect them to each other
        gui.connect(controller);
        controller.connect(gui, new RndGenerator());

        // Reset the components
        gui.init();

        // Test the EnhancedReactionController
        // InitialState
        doReset('A', controller, "Insert coin");
        doGoStop('B', controller, "Insert coin"); // goStop in InitialState - no effect
        doTicks('C', controller, 1, "Insert coin"); // ticks in InitialState - no effect
        doInsertCoin('D', controller, "Press GO!"); // insertCoin in InitialState - transition to CoinInsertedState

        // CoinInsertedState
        doInsertCoin('E', controller, "Press GO!"); // insertCoin in CoinInsertedState - no effect
        doTicks('F', controller, 999, "Press GO!"); // < 1000 ticks in CoinInsertedState - no effect
        doTicks('G', controller, 1000, "Insert coin"); // 1000 ticks in CoinInsertedState - transition to InitialState
        doInsertCoin('H', controller, "Press GO!"); // insertCoin in InitialState - transition back to CoinInsertedState
        randomNumber = 117;
        doGoStop('I', controller, "Wait..."); // press goStop in CoinInsertedState - transition to RandomDelayState

        // RandomDelayState - 1st round
        doTicks('J', controller, randomNumber - 1, "Wait..."); // ticks less than randomNumber
        doInsertCoin('K', controller, "Wait..."); // insertCoin in RandomDelayState - no effect
        doGoStop('L', controller, "Insert coin"); // press goStop in RandomDelayState - transition to InitialState
        doInsertCoin('M', controller, "Press GO!"); // insertCoin in InitialState - transition back to CoinInsertedState
        randomNumber = 122;
        doGoStop('N', controller, "Wait..."); // press goStop in CoinInserted State - transition back to RandomDelayState
        doTicks('O', controller, randomNumber, "0.00"); // ticks equal to randomNumber - transition to ReactionTestState

        // ReactionTestState - 1st round
        doTicks('P', controller, 1, "0.01"); // 1 tick
        doInsertCoin('Q', controller, "0.01"); // insertCoin in ReactionTestState - no effect
        doTicks('R', controller, 198, "1.99"); // less than 200 ticks - no effect
        doTicks('S', controller, 1, "2.00"); // 200 ticks - transition to DisplayResultState

        // DisplayResultState - 1st round
        doInsertCoin('T', controller, "2.00"); // insertCoin in DisplayResultState - no effect
        doTicks('U', controller, 299, "2.00"); // ticks less than 300 - no effect
        randomNumber = 166;
        doTicks('V', controller, 1, "Wait..."); // ticks equal to 300 and count less than 3 - transition to RandomDelayState

        // RandomDelayState - 2nd round
        doTicks('W', controller, randomNumber, "0.00"); // ticks equal to randomNumber - transition to ReactionTestState

        // ReactionTestState - 2nd round
        doTicks('X', controller, 11, "0.11"); // 11 ticks
        doGoStop('Y', controller, "0.11"); // press goStop in ReactionTestState - transition to DisplayResultState

        // DisplayResultState - 2nd round
        randomNumber = 200;
        doGoStop('Z', controller, "Wait..."); // press goStop in DisplayResultState and count less than 3 - transition to RandomDelayState

        // RandomDelayState - 3rd round
        doTicks('a', controller, randomNumber, "0.00"); // ticks equal to randomNumber - transition to ReactionTestState

        // ReactionTestState - 3rd round
        doTicks('b', controller, 111, "1.11"); // 111 ticks
        doGoStop('c', controller, "1.11"); // press goStop in ReactionTestState - transition to DisplayResultState

        // DisplayResultState - 3rd round
        doGoStop('d', controller, "Average = 1.07"); // press goStop in DisplayResultState when count == 3 - transition to AvgResultState

        // AvgResultState
        doInsertCoin('e', controller, "Average = 1.07"); // insertCoin in AvgResultState - no effect
        doGoStop('f', controller, "Insert coin"); // press goStop in AvgResultState - transition to InitialState

        // *********************************new game?
        doInsertCoin('g', controller, "Press GO!"); // insertCoin in InitialState - transition to CoinInsertedState
        randomNumber = 123;
        doGoStop('h', controller, "Wait..."); // press goStop in CoinInsertedState - transition to RandomDelayState
        doTicks('i', controller, randomNumber + 98, "0.98"); // ticks equal to randomNumber - transition to ReactionTestState, then reactionTest value equal to 0.98
        doGoStop('j', controller, "0.98"); // press goStop in ReactionTestState - transition to DisplayResultState
        randomNumber = 100;
        doGoStop('k', controller, "Wait..."); // press goStop in DisplayResultState and count less than 3 - transition to RandomDelayState
        doTicks('l', controller, randomNumber + 154, "1.54"); // ticks equal to randomNumber - transition to ReactionTestState, then reactionTest value equal to 1.54
        doGoStop('m', controller, "1.54"); // press goStop in ReactionTestState - transition to DisplayResultState
        randomNumber = 206;
        doTicks('n', controller, 300, "Wait..."); // ticks equal to 300 and count less than 3 - transition to RandomDelayState
        doTicks('o', controller, randomNumber + 198, "1.98"); // ticks equal to randomNumber - transition to ReactionTestState, then reactionTest value equal to 1.98
        doGoStop('p', controller, "1.98"); // press goStop in ReactionTestState - transition to DisplayResultState
        doGoStop('q', controller, "Average = 1.50"); // press goStop in DisplayResultState and count equal to 3 - transition to AvgResultState
        doTicks('r', controller, 499, "Average = 1.50"); // less than 500 ticks - no effect
        doInsertCoin('s', controller, "Average = 1.50"); // insertCoin in AvgResultState - no effect)
        doTicks('t', controller, 1, "Insert coin"); // 500 ticks - transition to initial state
    }

    private static void doReset(char ch, IController controller, String msg) {
        try {
            controller.init();
            getMessage(ch, msg);
        } catch (Exception e) {
            System.out.println("test " + ch + ": failed with exception " + e.getMessage());
        }
    }

    private static void doGoStop(char ch, IController controller, String msg) {
        try {
            controller.goStopPressed();
            getMessage(ch, msg);
        } catch (Exception e) {
            System.out.println("test " + ch + ": failed with exception " + e.getMessage());
        }
    }

    private static void doInsertCoin(char ch, IController controller, String msg) {
        try {
            controller.coinInserted();
            getMessage(ch, msg);
        } catch (Exception e) {
            System.out.println("test " + ch + ": failed with exception " + e.getMessage());
        }
    }

    private static void doTicks(char ch, IController controller, int n, String msg) {
        try {
            for (int t = 0; t < n; t++) {
                controller.tick();
            }
            getMessage(ch, msg);
        } catch (Exception e) {
            System.out.println("test " + ch + ": failed with exception " + e.getMessage());
        }
    }

    private static void getMessage(char ch, String msg) {
        if (msg.equalsIgnoreCase(displayText)) {
            System.out.println("test " + ch + ": passed successfully");
            passed++;
        } else {
            System.out.println("test " + ch + ": failed with message (expected " + msg + " | received " + displayText + ")");
        }
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
