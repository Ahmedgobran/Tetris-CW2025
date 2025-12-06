package com.comp2042.model.event;

/**
 * Enumeration representing the origin of a game event.
 * <p>
 * This distinguishes between actions initiated by the human player (e.g., key presses)
 * and actions initiated by the system (e.g., automatic gravity ticks).
 * </p>
 */
public enum EventSource {
    /**
     * Event triggered by user input (keyboard/mouse).
     * Usually results in score accumulation.
     */
    USER,

    /**
     * Event triggered by the game loop or background thread.
     * Usually related to gravity/time-based movement.
     */
    THREAD
}