package SimpleReactionMachine;

public class EnhancedReactionController implements IController {
    private IGui gui;
    private IRandom rng;
    private State currentState;
    protected double randomNumber;
    protected double reactionTime;
    protected double delay;
    protected int count;
    protected double sumReactionTime;

    @Override
    public void connect(IGui gui, IRandom rng) {
        this.gui = gui;
        this.rng = rng;
    }

    @Override
    public void init() {
        currentState = new InitialState(this);
        gui.setDisplay("Insert coin");
    }

    @Override
    public void coinInserted() {
        currentState.coinInserted();
    }

    @Override
    public void goStopPressed() {
        currentState.goStopPressed();
    }

    @Override
    public void tick() {
        currentState.tick();
    }

    // State base class
    private abstract class State {
        protected EnhancedReactionController controller;

        public State(EnhancedReactionController controller) {
            this.controller = controller;
        }

        public void coinInserted() {}
        public void goStopPressed() {}
        public void tick() {}
    }

    // Initial state - coin not yet inserted
    private class InitialState extends State {
        public InitialState(EnhancedReactionController controller) {
            super(controller);
        }

        @Override
        public void coinInserted() {
            controller.gui.setDisplay("Press GO!");
            controller.delay = 0;
            controller.currentState = new CoinInsertedState(controller);
        }
    }

    // State when coin inserted and go/stop button not yet pressed
    private class CoinInsertedState extends State {
        public CoinInsertedState(EnhancedReactionController controller) {
            super(controller);
        }

        @Override
        public void goStopPressed() {
            controller.delay = 0;
            controller.count = 0;
            controller.sumReactionTime = 0;
            controller.gui.setDisplay("Wait...");
            controller.randomNumber = controller.rng.getRandom(100, 250);
            controller.currentState = new RandomDelayState(controller);
        }

        @Override
        public void tick() {
            controller.delay += 0.01;
            if (controller.delay >= 9.99) {
                controller.gui.setDisplay("Insert coin");
                controller.currentState = new InitialState(controller);
            }
        }
    }

    // State with random delay before reaction test
    private class RandomDelayState extends State {
        public RandomDelayState(EnhancedReactionController controller) {
            super(controller);
        }

        @Override
        public void goStopPressed() {
            controller.gui.setDisplay("Insert coin");
            controller.currentState = new InitialState(controller);
        }

        @Override
        public void tick() {
            controller.delay += 0.01;
            if (controller.delay * 100 >= controller.randomNumber) {
                controller.reactionTime = 0;
                controller.count += 1;
                controller.gui.setDisplay(String.format("%.2f", controller.reactionTime));
                controller.currentState = new ReactionTestState(controller);
            }
        }
    }

    // ReactionTest state
    private class ReactionTestState extends State {
        public ReactionTestState(EnhancedReactionController controller) {
            super(controller);
        }

        @Override
        public void goStopPressed() {
            controller.delay = 0;
            controller.sumReactionTime += controller.reactionTime;
            controller.currentState = new DisplayResultState(controller);
        }

        @Override
        public void tick() {
            controller.reactionTime += 0.01;
            controller.gui.setDisplay(String.format("%.2f", controller.reactionTime));

            if (controller.reactionTime >= 2) {
                controller.delay = 0;
                controller.sumReactionTime += controller.reactionTime;
                controller.currentState = new DisplayResultState(controller);
            }
        }
    }

    // State for displaying results after reaction test
    private class DisplayResultState extends State {
        public DisplayResultState(EnhancedReactionController controller) {
            super(controller);
        }

        @Override
        public void goStopPressed() {
            if (controller.count < 3) {
                controller.delay = 0;
                controller.gui.setDisplay("Wait...");
                controller.randomNumber = controller.rng.getRandom(100, 250);
                controller.currentState = new RandomDelayState(controller);
            } else {
                controller.delay = 0;
                controller.gui.setDisplay("Average = " + String.format("%.2f", controller.sumReactionTime / 3));
                controller.currentState = new AvgResultState(controller);
            }
        }

        @Override
        public void tick() {
            controller.delay += 0.01;
            if (controller.delay >= 2.99) {
                if (controller.count < 3) {
                    controller.delay = 0;
                    controller.gui.setDisplay("Wait...");
                    controller.randomNumber = controller.rng.getRandom(100, 250);
                    controller.currentState = new RandomDelayState(controller);
                } else {
                    controller.delay = 0;
                    controller.gui.setDisplay("Average = " + String.format("%.2f", controller.sumReactionTime / 3));
                    controller.currentState = new AvgResultState(controller);
                }
            }
        }
    }

    // State for displaying average results
    private class AvgResultState extends State {
        public AvgResultState(EnhancedReactionController controller) {
            super(controller);
        }

        @Override
        public void goStopPressed() {
            controller.gui.setDisplay("Insert coin");
            controller.currentState = new InitialState(controller);
        }

        @Override
        public void tick() {
            controller.delay += 0.01;
            if (controller.delay >= 4.99) {
                controller.gui.setDisplay("Insert coin");
                controller.currentState = new InitialState(controller);
            }
        }
    }
}
