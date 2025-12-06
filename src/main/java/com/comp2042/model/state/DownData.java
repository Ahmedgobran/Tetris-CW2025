package com.comp2042.model.state;

/**
 * A container record used to pass the result of a downward movement event.
 * <p>
 * This aggregates the result of any potential line clears and the updated visual
 * state of the board into a single return object.
 * </p>
 *
 * @param clearRow The result of checking for cleared lines after the move.
 * @param viewData The updated view state (positions, active brick) after the move.
 */
public record DownData(ClearRow clearRow, ViewData viewData) {
}