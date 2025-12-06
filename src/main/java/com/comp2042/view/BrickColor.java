package com.comp2042.view;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Utility class responsible for mapping integer color codes to JavaFX Paint objects.
 * <p>
 * This class centralizes the color definitions for the game bricks, decoupling the
 * logic model (which uses simple integers) from the UI rendering.
 * </p>
 */
public class BrickColor {

    private static final double SHADOW_OPACITY = 0.3;

    /**
     * Retrieves the main fill color associated with a specific brick ID.
     *
     * @param colorCode The integer ID representing the brick type (0-7).
     * @return The JavaFX Paint object (Color) for the brick.
     */
    public Paint getFillColor(int colorCode) {
        return switch (colorCode) {
            case 0 -> Color.TRANSPARENT;
            case 1 -> Color.AQUA;
            case 2 -> Color.BLUEVIOLET;
            case 3 -> Color.DARKGREEN;
            case 4 -> Color.YELLOW;
            case 5 -> Color.RED;
            case 6 -> Color.BEIGE;
            case 7 -> Color.BURLYWOOD;
            default -> Color.WHITE;
        };
    }

    /**
     * Generates a semi-transparent version of the brick's color for the Ghost Piece.
     *
     * @param colorCode The integer ID representing the brick type.
     * @return A translucent Paint object matching the brick's base color.
     */
    public Paint getShadowColor(int colorCode) {
        Paint basePaint = getFillColor(colorCode);
        if (basePaint instanceof Color baseColor) {
            return new Color(
                    baseColor.getRed(),
                    baseColor.getGreen(),
                    baseColor.getBlue(),
                    SHADOW_OPACITY
            );
        }
        return basePaint;
    }
}