package com.comp2042.model.bricks;

import com.comp2042.model.state.NextShapeInfo;

/**
 * Manages the state and rotation logic for the active brick.
 * <p>
 * This class tracks the current rotation index of the active brick and calculates
 * the next shape configuration when a rotation is requested.
 * </p>
 */
public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    /**
     * Calculates the next rotation state of the current brick.
     * Does not modify the current state; it only predicts the next one.
     *
     * @return A {@link NextShapeInfo} record containing the matrix of the next rotation and its index.
     */
    public NextShapeInfo getNextShape() {
        int nextShape = currentShape;
        nextShape = (++nextShape) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    /**
     * Retrieves the matrix representation of the brick in its current rotation.
     *
     * @return A 2D integer array representing the brick's shape.
     */
    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    /**
     * Updates the current rotation index.
     *
     * @param currentShape The new index representing the rotation state.
     */
    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    /**
     * Assigns a new brick to be managed by this rotator.
     * Resets the rotation index to 0 (default position).
     *
     * @param brick The new Brick object to control.
     */
    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    }

    /**
     * Retrieves the underlying Brick object currently being managed.
     *
     * @return The current Brick instance.
     */
    public Brick getBrick() {
        return brick;
    }
}