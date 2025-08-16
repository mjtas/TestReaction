package com.reactionmachine.ui;

import com.reactionmachine.core.IController;
import com.reactionmachine.core.IGui;

public class ConsoleGui implements IGui {
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