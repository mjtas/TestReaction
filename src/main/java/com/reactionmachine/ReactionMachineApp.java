package com.reactionmachine;

import com.reactionmachine.core.EnhancedReactionController;
import com.reactionmachine.core.IController;
import com.reactionmachine.core.IGui;
import com.reactionmachine.ui.ConsoleGui;
import com.reactionmachine.util.RandomGenerator;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class ReactionMachineApp {
    private static IController controller;
    private static IGui gui;

    public static void main(String[] args) {
        try {
            Terminal terminal = TerminalBuilder.builder().build();
            LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();

            System.out.println("Welcome to the Enhanced Reaction Speed Tester!");
            System.out.println("     - For Insert Coin press SPACE then ENTER");
            System.out.println("     - For Go/Stop action press ENTER");
            System.out.println("     - For Exit press ESC then ENTER");

            Timer timer = new Timer(true);
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    controller.tick();
                }
            };

            controller = new EnhancedReactionController();
            gui = new ConsoleGui();
            gui.connect(controller);
            controller.connect(gui, new RandomGenerator(100));

            gui.init();
            controller.init();
            timer.scheduleAtFixedRate(task, 0, 10);

            boolean quitPressed = false;
            while (!quitPressed) {
                int key = terminal.reader().read();
                switch (key) {
                    case '\n':
                        controller.goStopPressed();
                        break;
                    case ' ':
                        controller.coinInserted();
                        break;
                    case 27:
                        quitPressed = true;
                        break;
                }
            }

            terminal.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}