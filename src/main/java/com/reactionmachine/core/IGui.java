package com.reactionmachine.core;

public interface IGui {
    void connect(IController controller);
    void init();
    void setDisplay(String s);
}