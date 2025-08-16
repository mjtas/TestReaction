package com.reactionmachine.states;

import com.reactionmachine.core.EnhancedReactionController;

public class RandomDelayState extends State {
    private double elapsedTime = 0;
    private final double targetDelay;

    public RandomDelayState(EnhancedReactionController controller) {
        super(controller);
        this.targetDelay = controller.getRng().getRandom(
                controller.getConfig().getMinDelayMs(),
                controller.getConfig().getMaxDelayMs()
        ) / 100.0;
    }

    @Override
    public void goStopPressed() {
        safeGuiUpdate("Too early! Insert coin");
        controller.transitionToState(new InitialState(controller));
    }

    @Override
    public void tick() {
        elapsedTime += 0.01;
        if (elapsedTime >= targetDelay) {
            safeGuiUpdate("0.00");
            controller.transitionToState(new ReactionTestState(controller));
        }
    }
}