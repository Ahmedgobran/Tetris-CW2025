package com.comp2042.util;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.function.Consumer;

/**
 * A wrapper class for the JavaFX Timeline to manage the game loop.
 * <p>
 * Encapsulates the logic for starting, stopping, pausing, and adjusting the speed
 * of the game tick cycle.
 * </p>
 */
public class GameLoop {

    private Timeline timeline;
    private final Consumer<Void> tickAction; // What runs every tick
    private double currentDelay;

    /**
     * Creates a new GameLoop.
     *
     * @param initialDelay The starting duration (in ms) between each tick.
     * @param tickAction   The action (Consumer) to execute on every tick.
     */
    public GameLoop(double initialDelay, Consumer<Void> tickAction) {
        this.currentDelay = initialDelay;
        this.tickAction = tickAction;
        createTimeline();
    }

    private void createTimeline() {
        if (timeline != null) {
            timeline.stop();
        }
        timeline = new Timeline(new KeyFrame(
                Duration.millis(currentDelay),
                ae -> tickAction.accept(null)
        ));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Starts or resumes the game loop.
     */
    public void start() {
        if (timeline != null) timeline.play();
    }

    /**
     * Stops the game loop completely.
     */
    public void stop() {
        if (timeline != null) timeline.stop();
    }

    /**
     * Pauses the game loop. Identical to stop() but semantically different.
     */
    public void pause() {
        if (timeline != null) timeline.pause();
    }

    /**
     * Updates the loop speed (tick duration).
     * If the loop is currently running, it restarts with the new speed immediately.
     *
     * @param delayMillis The new delay between ticks in milliseconds.
     */
    public void setSpeed(double delayMillis) {
        this.currentDelay = delayMillis;
        // Restart with new speed if running
        boolean wasRunning = (timeline.getStatus() == javafx.animation.Animation.Status.RUNNING);
        createTimeline();
        if (wasRunning) {
            start();
        }
    }
}