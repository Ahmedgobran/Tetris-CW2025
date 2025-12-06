package com.comp2042.model.event;

/**
 * Enumeration representing the specific type of movement or action requested.
 * <p>
 * Used to categorize {@link MoveEvent}s so the game logic knows which
 * operation (move, rotate, drop) to perform.
 * </p>
 */
public enum EventType {
    /** Request to move the active brick downwards. */
    DOWN,

    /** Request to move the active brick to the left. */
    LEFT,

    /** Request to move the active brick to the right. */
    RIGHT,

    /** Request to rotate the active brick. */
    ROTATE
}