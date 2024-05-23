using System;

namespace SimpleReactionMachine
{
    class Tester
    {
        private static IController controller;
        private static IGui gui;
        private static string displayText;
        private static int randomNumber;
        private static int passed = 0;
        private static int count = 0;

        static void Main(string[] args)
        {
            // run simple test
            EnhancedTest();
            Console.WriteLine("\n=====================================\nSummary: {0} tests passed out of 46", passed);
            Console.ReadKey();
        }

        private static void EnhancedTest()
        {
            //Construct a ReactionController
            controller = new EnhancedReactionController();
            gui = new DummyGui();

            //Connect them to each other
            gui.Connect(controller);
            controller.Connect(gui, new RndGenerator());

            //Reset the components()
            gui.Init();

            //Test the EnhancedReactionController
            //InitialState
            DoReset('A', controller, "Insert coin");
            DoGoStop('B', controller, "Insert coin"); // goStop in InitialState - no effect
            DoTicks('C', controller, 1, "Insert coin"); // ticks in InitialState - no effect
            DoInsertCoin('D', controller, "Press GO!"); // insertCoin in InitialState - transition to CoinInsertedState

            //CoinInsertedState
            DoInsertCoin('E', controller, "Press GO!"); // insertCoin in CoinInsertedState - no effect
            DoTicks('F', controller, 999, "Press GO!"); // < 1000 ticks in CoinInsertedState - no effect
            DoTicks('G', controller, 1000, "Insert coin"); // 1000 ticks in CoinInsertedState - transition to InitialState
            DoInsertCoin('H', controller, "Press GO!"); // insertCoin in InitialState - transition back to CoinInsertedState
            randomNumber = 117;
            DoGoStop('I', controller, "Wait..."); // press goStop in CoinInsertedState - transition to RandomDelayState

            //RandomDelayState - 1st round
            DoTicks('J', controller, randomNumber - 1, "Wait..."); //ticks less than randomNumber
            DoInsertCoin('K', controller, "Wait..."); // insertCoin in RandomDelayState - no effect
            DoGoStop('L', controller,  "Insert coin"); // press goStop in RandomDelayState - transition to InitialState
            DoInsertCoin('M', controller, "Press GO!"); // insertCoin in InitialState - transition back to CoinInsertedState
            randomNumber = 122;
            DoGoStop('N', controller, "Wait..."); // press goStop in CoinInserted State -transition back to RandomDelayState
            DoTicks('O', controller, randomNumber, "0.00"); // ticks equal to randomNumber - transition to ReactionTestState

            //ReactionTestState - 1st round
            DoTicks('P', controller, 1, "0.01"); // 1 tick
            DoInsertCoin('Q', controller, "0.01"); // insertCoin in ReactionTestState - no effect
            DoTicks('R', controller, 198, "1.99"); // less than 200 ticks - no effect
            DoTicks('S', controller, 1, "2.00"); // 200 ticks - transition to DisplayResultState

            //DisplayResultState - 1st round
            DoInsertCoin('T', controller, "2.00"); // insertCoin in DisplayResultState - no effect
            DoTicks('U', controller, 299, "2.00"); // ticks less than 300 - no effect
            randomNumber = 166;
            DoTicks('V', controller, 1, "Wait..."); // ticks equal to 300 and count less than 3 - transition to RandomDelayState

            //RandomDelayState - 2nd round 
            DoTicks('W', controller, randomNumber, "0.00"); // ticks equal to randomNumber - transition to ReactionTestState

            //ReactionTestState - 2nd round
            DoTicks('X', controller, 11, "0.11"); // 11 ticks
            DoGoStop('Y', controller, "0.11"); // press goStop in ReactionTestState - transition to DisplayResultState

            //DisplayResultState - 2nd round
            randomNumber = 200;
            DoGoStop('Z', controller, "Wait..."); // press goStop in DisplayResultState and count less than 3 - transition to RandomDelayState

            //RandomDelayState - 3rd round
            DoTicks('a', controller, randomNumber, "0.00"); // ticks equal to randomNumber - transition to ReactionTestState

            //ReactionTestState - 3rd round
            DoTicks('b', controller, 111, "1.11"); // 111 ticks
            DoGoStop('c', controller, "1.11"); // press goStop in ReactionTestState - transition to DisplayResultState

            //DisplayResultState - 3rd round
            DoGoStop('d', controller, "Average = 1.07"); // press goStop in DisplayResultState when count == 3 - transition to AvgResultState

            //AvgResultState
            DoInsertCoin('e', controller, "Average = 1.07"); // insertCoin in AvgResultState - no effect
            DoGoStop('f', controller, "Insert coin"); // press goStop in AvgResultState - transition to InitialState

            // *********************************new game?
            DoInsertCoin('g', controller, "Press GO!"); // insertCoin in InitialState - transition to CoinInsertedState
            randomNumber = 123;
            DoGoStop('h', controller, "Wait..."); // press goStop in CoinInsertedState - transition to RandomDelayState
            DoTicks('i', controller, randomNumber + 98, "0.98"); // ticks equal to randomNumber - transition to ReactionTestState, then reactionTest value equal to 0.98
            DoGoStop('j', controller, "0.98"); // press goStop in ReactionTestState - transition to DisplayResultState
            randomNumber = 100;
            DoGoStop('k', controller, "Wait..."); // press goStop in DisplayResultState and count less than 3 - transition to RandomDelayState
            DoTicks('l', controller, randomNumber + 154, "1.54"); // ticks equal to randomNumber - transition to ReactionTestState, then reactionTest value equal to 1.54
            DoGoStop('m', controller, "1.54"); // press goStop in ReactionTestState - transition to DisplayResultState
            randomNumber = 206;
            DoTicks('n', controller, 300, "Wait..."); // ticks equal to 300 and count less than 3 - transition to RandomDelayState
            DoTicks('o', controller, randomNumber + 198, "1.98"); // ticks equal to randomNumber - transition to ReactionTestState, then reactionTest value equal to 1.98
            DoGoStop('p', controller, "1.98"); // press goStop in ReactionTestState - transition to DisplayResultState
            DoGoStop('q', controller, "Average = 1.50"); // press goStop in DisplayResultState and count equal to 3 - transition to AvgResultState
            DoTicks('r', controller, 499, "Average = 1.50"); // less than 500 ticks - no effect
            DoInsertCoin('s', controller, "Average = 1.50"); // insertCoin in AvgResultState - no effect)
            DoTicks('t', controller, 1, "Insert coin"); // 500 ticks - transition to initial state
        }

        private static void DoReset(char ch, IController controller, string msg)
        {
            try
            {
                controller.Init();
                GetMessage(ch, msg);
            }
            catch (Exception exception)
            {
                Console.WriteLine("test {0}: failed with exception {1})", ch, msg, exception.Message);
            }
        }

        private static void DoGoStop(char ch, IController controller, string msg)
        {
            try
            {
                controller.GoStopPressed();
                GetMessage(ch, msg);
            }
            catch (Exception exception)
            {
                Console.WriteLine("test {0}: failed with exception {1})", ch, msg, exception.Message);
            }
        }

        private static void DoInsertCoin(char ch, IController controller, string msg)
        {
            try
            {
                controller.CoinInserted();
                GetMessage(ch, msg);
            }
            catch (Exception exception)
            {
                Console.WriteLine("test {0}: failed with exception {1})", ch, msg, exception.Message);
            }
        }

        private static void DoTicks(char ch, IController controller, int n, string msg)
        {
            try
            {
                for (int t = 0; t < n; t++) controller.Tick();
                GetMessage(ch, msg);
            }
            catch (Exception exception)
            {
                Console.WriteLine("test {0}: failed with exception {1})", ch, msg, exception.Message);
            }
        }

        private static void GetMessage(char ch, string msg)
        {
            if (msg.ToLower() == displayText.ToLower())
            {
                Console.WriteLine("test {0}: passed successfully", ch);
                passed++;
            }
            else
                Console.WriteLine("test {0}: failed with message ( expected {1} | received {2})", ch, msg, displayText);
        }

        private class DummyGui : IGui
        {

            private IController controller;

            public void Connect(IController controller)
            {
                this.controller = controller;
            }

            public void Init()
            {
                displayText = "?reset?";
            }

            public void SetDisplay(string msg)
            {
                displayText = msg;
            }
        }

        private class RndGenerator : IRandom
        {
            public int GetRandom(int from, int to)
            {
                return randomNumber;
            }
        }

    }

}
