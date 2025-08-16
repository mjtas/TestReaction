package com.reactionmachine.core;

public interface IController {
    void connect(IGui gui, IRandom rng);
    void init();
    void coinInserted();
    void goStopPressed();
    void tick();
}
