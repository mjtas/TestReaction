package com.reactionmachine.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;

public class GameSession {private final LocalDateTime startTime;
    private final List<Double> reactionTimes;
    private LocalDateTime endTime;
    private boolean completed;

    public GameSession() {
        this.startTime = LocalDateTime.now();
        this.reactionTimes = new ArrayList<>();
        this.completed = false;
    }

    public void addReactionTime(Double time) {
        reactionTimes.add(time);
    }

    public void complete() {
        this.endTime = LocalDateTime.now();
        this.completed = true;
    }

    public double getAverageReactionTime() {
        return reactionTimes.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    public OptionalDouble getBestTime() {
        return reactionTimes.stream()
                .mapToDouble(Double::doubleValue)
                .min();
    }

    // Getters
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public List<Double> getReactionTimes() { return Collections.unmodifiableList(reactionTimes); }
    public boolean isCompleted() { return completed; }
}

