package com.comp2042.model.bricks;

import java.util.Arrays;

/**
 * Enumeration representing the different types of Tetromino shapes.
 * <p>
 * This replaces the "Magic Numbers" (1-7) used throughout the codebase.
 * It provides a lookup mechanism to find a type based on its integer ID.
 * </p>
 */
public enum BrickType {
    EMPTY(0),
    I(1),
    J(2),
    L(3),
    O(4),
    S(5),
    T(6),
    Z(7);

    private final int id;

    BrickType(int id) {
        this.id = id;
    }

    /**
     * Retrieves the integer ID used in the board matrix.
     * @return The ID (e.g., 1 for I-Brick).
     */
    public int getID() {
        return id;
    }

    /**
     * Lookup method to convert an integer ID back to a BrickType.
     * @param id The integer ID from the board matrix.
     * @return The corresponding BrickType, or EMPTY if not found.
     */
    public static BrickType fromID(int id) {
        return Arrays.stream(values())
                .filter(type -> type.id == id)
                .findFirst()
                .orElse(EMPTY);
    }
}