package com.reactionmachine.core;

import com.reactionmachine.config.GameConfiguration;
import com.reactionmachine.model.GameSession;
import com.reactionmachine.model.Statistics;
import com.reactionmachine.states.State;
import com.reactionmachine.states.InitialState;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

public class EnhancedReactionController implements IController {
    private static final Logger LOGGER = Logger.getLogger(EnhancedReactionController.class.getName());

    private static Integer addIntegers(Integer a, Integer b) {
        return (Integer) (a + b);
    }

    private IGui gui;
    private IRandom rng;
    private State currentState;

    private final Queue<Double> reactionTimes = new ConcurrentLinkedQueue<>();
    private final List<GameSession> gameSessions = new ArrayList<>();
    private final GameConfiguration config;
    private GameSession currentSession;

    private final Map<String, Integer> stateTransitions = new HashMap<>();

    public EnhancedReactionController() {
        this.config = GameConfiguration.defaultConfig();
        LOGGER.info("Enhanced Reaction Controller initialized");
    }

    public EnhancedReactionController(GameConfiguration config) {
        this.config = config;
        LOGGER.info("Enhanced Reaction Controller initialized with custom config");
    }

    @Override
    public void connect(IGui gui, IRandom rng) {
        this.gui = Objects.requireNonNull(gui, "GUI cannot be null");
        this.rng = Objects.requireNonNull(rng, "Random generator cannot be null");
        LOGGER.info("Connected to GUI and RNG");
    }

    @Override
    public void init() {
        transitionToState(new InitialState(this));
        gui.setDisplay("Insert coin");
        currentSession = new GameSession();
        LOGGER.info("Controller initialized");
    }

    @Override
    public void coinInserted() {
        currentState.coinInserted();
    }

    @Override
    public void goStopPressed() {
        currentState.goStopPressed();
    }

    @Override
    public void tick() {
        currentState.tick();
    }

    public void transitionToState(State newState) {
        if (currentState != null) {
            String transition = currentState.getClass().getSimpleName() + " -> " +
                    newState.getClass().getSimpleName();
            stateTransitions.merge(transition, Integer.valueOf(1), EnhancedReactionController::addIntegers);
        }
        currentState = newState;
        LOGGER.fine("State transition: " + newState.getClass().getSimpleName());
    }

    // Getters for state classes
    public IGui getGui() { return gui; }
    public IRandom getRng() { return rng; }
    public GameConfiguration getConfig() { return config; }
    public GameSession getCurrentSession() { return currentSession; }
    public void setCurrentSession(GameSession session) { this.currentSession = session; }
    public List<GameSession> getGameSessions() { return gameSessions; }

    public Optional<Statistics> getStatistics() {
        return Optional.of(new Statistics(gameSessions));
    }

    public CompletableFuture<String> generateReportAsync() {
        return CompletableFuture.supplyAsync(() -> {
            return new Statistics(gameSessions).generateReport();
        });
    }
}