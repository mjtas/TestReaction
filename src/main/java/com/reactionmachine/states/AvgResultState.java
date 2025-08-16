package com.reactionmachine.states;

import com.reactionmachine.core.EnhancedReactionController;

public class AvgResultState extends State {
    private double elapsedTime = 0;

    public AvgResultState(EnhancedReactionController controller) {
        super(controller);
    }

    @Override
    public void goStopPressed() {
        safeGuiUpdate("Insert coin");
        controller.transitionToState(new InitialState(controller));
    }

    @Override
    public void tick() {
        elapsedTime += 0.01;
        if (elapsedTime >= 4.99) {
            safeGuiUpdate("Insert coin");
            controller.transitionToState(new InitialState(controller));
        }
    }
}