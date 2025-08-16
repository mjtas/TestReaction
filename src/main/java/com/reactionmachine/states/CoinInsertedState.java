package com.reactionmachine.states;

import com.reactionmachine.core.EnhancedReactionController;
import com.reactionmachine.model.GameSession;

public class CoinInsertedState extends State {
    private double elapsedTime = 0;

    public CoinInsertedState(EnhancedReactionController controller) {
        super(controller);
    }

    @Override
    public void goStopPressed() {
        controller.setCurrentSession(new GameSession());
        safeGuiUpdate("Wait...");
        controller.transitionToState(new RandomDelayState(controller));
    }

    @Override
    public void tick() {
        elapsedTime += 0.01;
        if (elapsedTime >= controller.getConfig().getTimeoutSeconds()) {
            safeGuiUpdate("Insert coin");
            controller.transitionToState(new InitialState(controller));
        }
    }
}