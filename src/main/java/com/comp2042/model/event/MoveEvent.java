package com.comp2042.model.event;

/**
 * An immutable record representing a single movement command in the game.
 * <p>
 * This event object encapsulates <i>what</i> is happening ({@link EventType}) and
 * <i>who</i> caused it ({@link EventSource}), allowing the AbstractGameController, NormalModeController, & ChallengeModeController to apply
 * the correct logic (e.g., only awarding points for USER-driven moves).
 * </p>
 *
 * @param eventType   The type of movement requested (e.g., DOWN, LEFT).
 * @param eventSource The origin of the event (USER or THREAD).
 */
public record MoveEvent(EventType eventType, EventSource eventSource) {
}