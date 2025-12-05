package com.comp2042.util;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.function.Consumer;

public class GameLoop {

    private Timeline timeline;
    private final Consumer<Void> tickAction; // What runs every tick
    private double currentDelay;

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

    public void start() {
        if (timeline != null) timeline.play();
    }

    public void stop() {
        if (timeline != null) timeline.stop();
    }

    public void pause() {
        if (timeline != null) timeline.pause();
    }

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