package com.reactionmachine.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Statistics {private final List<GameSession> gameSessions;

    public Statistics(List<GameSession> gameSessions) {
        this.gameSessions = gameSessions;
    }

    public double getOverallAverage() {
        return gameSessions.stream()
                .filter(GameSession::isCompleted)
                .flatMap(session -> session.getReactionTimes().stream())
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    public OptionalDouble getBestTime() {
        return gameSessions.stream()
                .filter(GameSession::isCompleted)
                .flatMap(session -> session.getReactionTimes().stream())
                .mapToDouble(Double::doubleValue)
                .min();
    }

    public long getTotalGamesPlayed() {
        return gameSessions.stream()
                .filter(GameSession::isCompleted)
                .count();
    }

    public Map<String, Object> getDetailedStats() {
        Map<String, Object> stats = new HashMap<>();

        List<Double> allTimes = gameSessions.stream()
                .filter(GameSession::isCompleted)
                .flatMap(session -> session.getReactionTimes().stream())
                .collect(Collectors.toList());

        if (!allTimes.isEmpty()) {
            DoubleSummaryStatistics summary = allTimes.stream()
                    .mapToDouble(Double::doubleValue)
                    .summaryStatistics();

            stats.put("count", summary.getCount());
            stats.put("average", summary.getAverage());
            stats.put("min", summary.getMin());
            stats.put("max", summary.getMax());
            stats.put("standardDeviation", calculateStandardDeviation(allTimes));
        }

        return stats;
    }

    private double calculateStandardDeviation(List<Double> values) {
        double mean = values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double variance = values.stream()
                .mapToDouble(Double::doubleValue)
                .map(x -> Math.pow(x - mean, 2))
                .average()
                .orElse(0.0);
        return Math.sqrt(variance);
    }

    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== REACTION TIME STATISTICS ===\n");
        report.append("Generated: ").append(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).append("\n\n");

        Map<String, Object> stats = getDetailedStats();
        if (!stats.isEmpty()) {
            report.append(String.format("Games Played: %d\n", getTotalGamesPlayed()));
            report.append(String.format("Total Reactions: %d\n", stats.get("count")));
            report.append(String.format("Average Time: %.3f seconds\n", stats.get("average")));
            report.append(String.format("Best Time: %.3f seconds\n", stats.get("min")));
            report.append(String.format("Worst Time: %.3f seconds\n", stats.get("max")));
            report.append(String.format("Standard Deviation: %.3f\n", stats.get("standardDeviation")));
        } else {
            report.append("No completed games found.\n");
        }

        return report.toString();
    }
}
