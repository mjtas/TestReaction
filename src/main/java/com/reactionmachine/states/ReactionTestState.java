package com.reactionmachine.states;

import com.reactionmachine.core.EnhancedReactionController;

public class ReactionTestState extends State {
    private double reactionTime = 0;

    public ReactionTestState(EnhancedReactionController controller) {
        super(controller);
    }

    @Override
    public void goStopPressed() {
        controller.getCurrentSession().addReactionTime(Double.valueOf(reactionTime));
        controller.transitionToState(new DisplayResultState(controller));
    }

    @Override
    public void tick() {
        reactionTime += 0.01;
        safeGuiUpdate(String.format(String.valueOf(reactionTime)));

        if (reactionTime >= controller.getConfig().getMaxReactionTime()) {
            controller.getCurrentSession().addReactionTime(Double.valueOf(reactionTime));
            controller.transitionToState(new DisplayResultState(controller));
        }
    }
}
