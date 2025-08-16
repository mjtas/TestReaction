package com.reactionmachine.states;

import com.reactionmachine.core.EnhancedReactionController;
import java.util.List;

public class DisplayResultState extends State {
    private double elapsedTime = 0;

    public DisplayResultState(EnhancedReactionController controller) {
        super(controller);
        displayCurrentResult();
    }

    private void displayCurrentResult() {
        List<Double> times = controller.getCurrentSession().getReactionTimes();
        if (!times.isEmpty()) {
            Double lastTime = times.get(times.size() - 1);
            safeGuiUpdate(String.format("%.2f seconds", lastTime));
        }
    }

    @Override
    public void goStopPressed() {
        proceedToNextState();
    }

    @Override
    public void tick() {
        elapsedTime += 0.01;
        if (elapsedTime >= 2.99) {
            proceedToNextState();
        }
    }

    private void proceedToNextState() {
        if (controller.getCurrentSession().getReactionTimes().size() < controller.getConfig().getMaxAttempts()) {
            safeGuiUpdate("Wait...");
            controller.transitionToState(new RandomDelayState(controller));
        } else {
            Double average = (Double) controller.getCurrentSession().getAverageReactionTime();
            safeGuiUpdate(String.format("Average: %.2f", average));
            controller.getCurrentSession().complete();
            controller.getGameSessions().add(controller.getCurrentSession());
            controller.transitionToState(new AvgResultState(controller));
        }
    }
}