using System;
using System.Timers;

namespace SimpleReactionMachine
{
    public class EnhancedReactionController : IController
    {
        private IGui gui;
        private IRandom rng;
        private State currentState;
        protected double randomNumber;
        protected double reactionTime;
        protected double delay;
        protected int count;
        protected double sumReactionTime;

        public void Connect(IGui gui, IRandom rng)
        {
            this.gui = gui;
            this.rng = rng;
        }

        //Initialise the controller
        public void Init()
        {
            currentState = new InitialState(this);
            gui.SetDisplay("Insert coin");
        }

        public void CoinInserted()
        {
            currentState.CoinInserted();
        }

        public void GoStopPressed()
        {
            currentState.GoStopPressed();
        }
        public void Tick()
        {
            currentState.Tick();
        }

        // State base class
        private abstract class State
        {
            protected EnhancedReactionController controller;

            public State(EnhancedReactionController controller)
            {
                this.controller = controller;
            }

            public virtual void CoinInserted() { }
            public virtual void GoStopPressed() { }
            public virtual void Tick() { }
        }
        //Initial state - coin not yet inserted
        private class InitialState : State
        {
            public InitialState(EnhancedReactionController controller) : base(controller) { }

            public override void CoinInserted()
            {
                controller.gui.SetDisplay("Press GO!");
                controller.delay = 0;
                controller.currentState = new CoinInsertedState(controller);
            }
        }
        //State when coin inserted and go/stop button not yet pressed
        private class CoinInsertedState : State
        {
            public CoinInsertedState(EnhancedReactionController controller) : base(controller) { }

            public override void GoStopPressed()
            {
                controller.delay = 0;
                controller.count = 0;
                controller.sumReactionTime = 0;
                controller.gui.SetDisplay("Wait...");
                controller.randomNumber = controller.rng.GetRandom(100, 250);
                controller.currentState = new RandomDelayState(controller);
            }

            public override void Tick()
            {
                controller.delay += 0.01;
                if (controller.delay >= 9.99)
                {
                    controller.gui.SetDisplay("Insert coin");
                    controller.currentState = new InitialState(controller);
                }
            }
        }
    
        // State with random delay before reaction test
        private class RandomDelayState : State
        {
            public RandomDelayState(EnhancedReactionController controller) : base(controller) { }

            public override void GoStopPressed()
            {
                controller.gui.SetDisplay("Insert coin");
                controller.currentState = new InitialState(controller);
            }

            public override void Tick()
            {
                controller.delay += 0.01;
                if (controller.delay*100 >= controller.randomNumber)
                {
                    controller.reactionTime = 0;
                    controller.count += 1;
                    controller.gui.SetDisplay(controller.reactionTime.ToString("F2"));
                    controller.currentState = new ReactionTestState(controller);
                }
            }
        }

        // ReactionTest state
        private class ReactionTestState : State
        {
            public ReactionTestState(EnhancedReactionController controller) : base(controller) { }

            public override void GoStopPressed()
            {
                controller.delay = 0;
                controller.sumReactionTime += controller.reactionTime;
                controller.currentState = new DisplayResultState(controller);
            }

            public override void Tick()
            {
                controller.reactionTime += 0.01;
                controller.gui.SetDisplay(controller.reactionTime.ToString("F2"));

                if (controller.reactionTime >= 2)
                {
                    controller.delay = 0;
                    controller.sumReactionTime += controller.reactionTime;
                    controller.currentState = new DisplayResultState(controller);
                }
            }
        }

        //State for displaying results after reaction test
        private class DisplayResultState : State
        {
            public DisplayResultState(EnhancedReactionController controller) : base(controller) { }

            public override void GoStopPressed()
            {
                if (controller.count < 3)
                {
                    controller.delay = 0;
                    controller.gui.SetDisplay("Wait...");
                    controller.randomNumber = controller.rng.GetRandom(100, 250);
                    controller.currentState = new RandomDelayState(controller);
                }

                else
                {
                    controller.delay = 0;
                    controller.gui.SetDisplay("Average = " + (controller.sumReactionTime / 3).ToString("F2"));
                    controller.currentState = new AvgResultState(controller);
                }
            }

            public override void Tick()
            {
                controller.delay += 0.01;
                if (controller.delay >= 2.99)
                {
                    if (controller.count < 3)
                    {
                        controller.delay = 0;
                        controller.gui.SetDisplay("Wait...");
                        controller.randomNumber = controller.rng.GetRandom(100, 250);
                        controller.currentState = new RandomDelayState(controller);
                    }

                    else
                    {
                        controller.delay = 0;
                        controller.gui.SetDisplay("Average = " + (controller.sumReactionTime / 3).ToString("F2"));
                        controller.currentState = new AvgResultState(controller);
                    }
                }
            }
        }
        //State for displaying average results
        private class AvgResultState : State
        {
            public AvgResultState(EnhancedReactionController controller) : base(controller) { }

            public override void GoStopPressed()
            {
                controller.gui.SetDisplay("Insert coin");
                controller.currentState = new InitialState(controller);
            }

            public override void Tick()
            {
                controller.delay += 0.01;
                if (controller.delay >= 4.99)
                {
                    controller.gui.SetDisplay("Insert coin");
                    controller.currentState = new InitialState(controller);
                }
            }
        }
    }
}
