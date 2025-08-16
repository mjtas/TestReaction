package com.reactionmachine.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDateTime;

public class GameSessionTest {
    private GameSession session;

    @Before
    public void setUp() {
        session = new GameSession();
    }

    @Test
    public void testNewSession() {
        assertNotNull(session.getStartTime());
        assertNull(session.getEndTime());
        assertFalse(session.isCompleted());
        assertTrue(session.getReactionTimes().isEmpty());
        assertEquals(0.0, session.getAverageReactionTime(), 0.01);
        assertFalse(session.getBestTime().isPresent());
    }

    @Test
    public void testAddReactionTimes() {
        session.addReactionTime(Double.valueOf(0.5));
        session.addReactionTime(Double.valueOf(0.8));
        session.addReactionTime(Double.valueOf(0.3));

        assertEquals(3, session.getReactionTimes().size());
        assertEquals(0.533, session.getAverageReactionTime(), 0.01);
        assertEquals(0.3, session.getBestTime().getAsDouble(), 0.01);
    }

    @Test
    public void testCompleteSession() {
        LocalDateTime before = LocalDateTime.now();
        session.complete();
        LocalDateTime after = LocalDateTime.now();

        assertTrue(session.isCompleted());
        assertNotNull(session.getEndTime());
        assertTrue(session.getEndTime().isAfter(before) || session.getEndTime().isEqual(before));
        assertTrue(session.getEndTime().isBefore(after) || session.getEndTime().isEqual(after));
    }
}