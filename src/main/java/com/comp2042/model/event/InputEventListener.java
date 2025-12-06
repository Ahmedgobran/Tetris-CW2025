package com.comp2042.model.event;

import com.comp2042.model.state.DownData;
import com.comp2042.model.state.ViewData;

/**
 * Interface defining the contract for handling user input events.
 * <p>
 * This interface acts as a bridge between the Controller (which receives inputs)
 * and the Model (which executes game logic). It decouples the UI from the game rules.
 * </p>
 */
public interface InputEventListener {

    /**
     * Handles the event when the brick moves down (either by gravity or user input).
     *
     * @param event The move event containing the event source.
     * @return A {@link DownData} object containing the result of the move (e.g., if lines were cleared).
     */
    DownData onDownEvent(MoveEvent event);

    /**
     * Handles the event when the user requests to move the brick left.
     *
     * @param event The move event details.
     * @return A {@link ViewData} object representing the updated state of the board.
     */
    ViewData onLeftEvent(MoveEvent event);

    /**
     * Handles the event when the user requests to move the brick right.
     *
     * @param event The move event details.
     * @return A {@link ViewData} object representing the updated state of the board.
     */
    ViewData onRightEvent(MoveEvent event);

    /**
     * Handles the event when the user requests to rotate the brick.
     *
     * @param event The move event details.
     * @return A {@link ViewData} object representing the updated state of the board.
     */
    ViewData onRotateEvent(MoveEvent event);

    /**
     * Resets the game state to start a new session.
     */
    void createNewGame();

    /**
     * Handles the event when the user requests a hard drop (instant fall).
     *
     * @return A {@link DownData} object containing the result of the drop and any line clears.
     */
    DownData onHardDropEvent();

    /**
     * Retrieves the current configuration of the game board.
     *
     * @return A 2D integer array representing the grid of blocks.
     */
    int[][] getBoard();

    /**
     * Handles the event when the user requests to hold the current brick.
     *
     * @return A {@link ViewData} object containing the updated board state and held brick info.
     */
    ViewData onHoldEvent();
}