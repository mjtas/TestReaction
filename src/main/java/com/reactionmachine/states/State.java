package com.reactionmachine.states;

import com.reactionmachine.core.EnhancedReactionController;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class State {
    private static final Logger LOGGER = Logger.getLogger(State.class.getName());
    protected final EnhancedReactionController controller;

    public State(EnhancedReactionController controller) {
        this.controller = Objects.requireNonNull(controller);
    }

    public void coinInserted() {
        LOGGER.warning("Coin inserted in invalid state: " + getClass().getSimpleName());
    }

    public void goStopPressed() {
        LOGGER.warning("Go/Stop pressed in invalid state: " + getClass().getSimpleName());
    }

    public void tick() {}

    protected void safeGuiUpdate(String message) {
        try {
            controller.getGui().setDisplay(message);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "GUI update failed", e);
        }
    }
}
