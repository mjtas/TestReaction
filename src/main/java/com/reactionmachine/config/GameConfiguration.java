package com.reactionmachine.config;

public class GameConfiguration {
    private final int maxAttempts;
    private final double timeoutSeconds;
    private final int minDelayMs;
    private final int maxDelayMs;
    private final double maxReactionTime;

    private GameConfiguration(Builder builder) {
        this.maxAttempts = builder.maxAttempts;
        this.timeoutSeconds = builder.timeoutSeconds;
        this.minDelayMs = builder.minDelayMs;
        this.maxDelayMs = builder.maxDelayMs;
        this.maxReactionTime = builder.maxReactionTime;
    }

    public static GameConfiguration defaultConfig() {
        return new Builder().build();
    }

    public static class Builder {
        private int maxAttempts = 3;
        private double timeoutSeconds = 9.99;
        private int minDelayMs = 100;
        private int maxDelayMs = 250;
        private double maxReactionTime = 2.0;

        public Builder maxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
            return this;
        }

        public Builder timeoutSeconds(double timeout) {
            this.timeoutSeconds = timeout;
            return this;
        }

        public Builder delayRange(int minMs, int maxMs) {
            this.minDelayMs = minMs;
            this.maxDelayMs = maxMs;
            return this;
        }

        public Builder maxReactionTime(double seconds) {
            this.maxReactionTime = seconds;
            return this;
        }

        public GameConfiguration build() {
            return new GameConfiguration(this);
        }
    }

    // Getters
    public int getMaxAttempts() { return maxAttempts; }
    public double getTimeoutSeconds() { return timeoutSeconds; }
    public int getMinDelayMs() { return minDelayMs; }
    public int getMaxDelayMs() { return maxDelayMs; }
    public double getMaxReactionTime() { return maxReactionTime; }
}
