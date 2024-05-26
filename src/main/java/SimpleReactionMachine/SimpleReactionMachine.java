package SimpleReactionMachine;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SimpleReactionMachine {

    private static IController controller;
    private static IGui gui;

    public static void main(String[] args) {
        try {
            // Create a terminal
            Terminal terminal = TerminalBuilder.builder().build();

            // Create a line reader
            LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();

            System.out.println("Welcome to the Reaction Speed Tester!");
            System.out.println("     - For Insert Coin press SPACE then ENTER");
            System.out.println("     - For Go/Stop action press ENTER");
            System.out.println("     - For Exit press ESC then ENTER");

            // Create a timer for Tick event
            Timer timer = new Timer(true);
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    controller.tick();
                }
            };

            // Connect GUI with the Controller and vice versa
            controller = new EnhancedReactionController();
            gui = new Gui();
            gui.connect(controller);
            controller.connect(gui, new RandomGenerator());

            // Reset the GUI
            gui.init();
            // Reset the controller
            controller.init();
            // Start the timer
            timer.scheduleAtFixedRate(task, 0, 10);

            // Loop until Escape key is pressed
            boolean quitePressed = false;
            while (!quitePressed) {
                // Read a single character
                int key = terminal.reader().read();

                // Process the key
                switch (key) {
                    case '\n': // Enter key pressed
                        controller.goStopPressed();
                        break;
                    case ' ': // Space key pressed
                        controller.coinInserted();
                        break;
                    case 27: // Escape key pressed
                        quitePressed = true;
                        break;
                }
            }

            // Close the terminal
            terminal.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Internal implementation of Random Generator
    private static class RandomGenerator implements IRandom {
        private final Random rnd = new Random(100);

        @Override
        public int getRandom(int from, int to) {
            return rnd.nextInt(to - from) + from;
        }
    }

    // Internal implementation of GUI
    private static class Gui implements IGui {
        private IController controller;

        @Override
        public void connect(IController controller) {
            this.controller = controller;
        }

        @Override
        public void init() {
            setDisplay("Start your game!");
        }

        @Override
        public void setDisplay(String text) {
            printUserInterface(text);
        }

        private void printUserInterface(String text) {
            System.out.println(text);
        }
    }
}
