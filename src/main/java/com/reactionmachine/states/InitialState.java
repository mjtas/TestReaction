package com.reactionmachine.states;

import com.reactionmachine.core.EnhancedReactionController;

public class InitialState extends State {
    public InitialState(EnhancedReactionController controller) {
        super(controller);
    }

    @Override
    public void coinInserted() {
        safeGuiUpdate("Press GO!");
        controller.transitionToState(new CoinInsertedState(controller));
    }
}