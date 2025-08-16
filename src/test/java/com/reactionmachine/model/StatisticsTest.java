package com.reactionmachine.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StatisticsTest {
    private List<GameSession> sessions;
    private Statistics stats;

    @Before
    public void setUp() {
        sessions = new ArrayList<>();

        // Create completed sessions with reaction times
        GameSession session1 = new GameSession();
        session1.addReactionTime(Double.valueOf(0.5));
        session1.addReactionTime(Double.valueOf(0.8));
        session1.addReactionTime(Double.valueOf(0.3));
        session1.complete();

        GameSession session2 = new GameSession();
        session2.addReactionTime(Double.valueOf(0.6));
        session2.addReactionTime(Double.valueOf(0.7));
        session2.addReactionTime(Double.valueOf(0.4));
        session2.complete();

        sessions.add(session1);
        sessions.add(session2);

        stats = new Statistics(sessions);
    }

    @Test
    public void testOverallAverage() {
        assertEquals(0.55, stats.getOverallAverage(), 0.01);
    }

    @Test
    public void testBestTime() {
        assertTrue(stats.getBestTime().isPresent());
        assertEquals(0.3, stats.getBestTime().getAsDouble(), 0.01);
    }

    @Test
    public void testTotalGamesPlayed() {
        assertEquals(2, stats.getTotalGamesPlayed());
    }

    @Test
    public void testDetailedStats() {
        Map<String, Object> detailedStats = stats.getDetailedStats();

        assertNotNull(detailedStats);
        assertEquals(Optional.of(6L), detailedStats.get("count"));
        assertEquals(0.55, (Double) detailedStats.get("average"), 0.01);
        assertEquals(0.3, (Double) detailedStats.get("min"), 0.01);
        assertEquals(0.8, (Double) detailedStats.get("max"), 0.01);
        assertTrue(detailedStats.containsKey("standardDeviation"));
    }

    @Test
    public void testGenerateReport() {
        String report = stats.generateReport();

        assertNotNull(report);
        assertTrue(report.contains("REACTION TIME STATISTICS"));
        assertTrue(report.contains("Games Played: 2"));
        assertTrue(report.contains("Total Reactions: 6"));
    }

    @Test
    public void testEmptyStatistics() {
        Statistics emptyStats = new Statistics(new ArrayList<>());

        assertEquals(0.0, emptyStats.getOverallAverage(), 0.01);
        assertFalse(emptyStats.getBestTime().isPresent());
        assertEquals(0, emptyStats.getTotalGamesPlayed());
        assertTrue(emptyStats.getDetailedStats().isEmpty());
    }
}