package com.comp2042.view;

import com.comp2042.model.BrickType;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.EnumMap;
import java.util.Map;

/**
 * Utility class responsible for mapping BrickTypes to JavaFX Paint objects.
 * <p>
 * Refactored to use an EnumMap instead of a switch statement, improving
 * maintainability and performance.
 * </p>
 */
public class BrickColor {

    private static final double SHADOW_OPACITY = 0.3;
    private final Map<BrickType, Color> colorMap;

    public BrickColor() {
        colorMap = new EnumMap<>(BrickType.class);
        initializeColors();
    }

    private void initializeColors() {
        colorMap.put(BrickType.EMPTY, Color.TRANSPARENT);
        colorMap.put(BrickType.I, Color.AQUA);
        colorMap.put(BrickType.J, Color.BLUEVIOLET);
        colorMap.put(BrickType.L, Color.DARKGREEN);
        colorMap.put(BrickType.O, Color.YELLOW);
        colorMap.put(BrickType.S, Color.RED);
        colorMap.put(BrickType.T, Color.BEIGE);
        colorMap.put(BrickType.Z, Color.BURLYWOOD);
    }

    /**
     * Retrieves the main fill color associated with a specific brick ID.
     *
     * @param colorCode The integer ID representing the brick type.
     * @return The JavaFX Paint object (Color) for the brick.
     */
    public Paint getFillColor(int colorCode) {
        BrickType type = BrickType.fromID(colorCode);
        return colorMap.getOrDefault(type, Color.WHITE);
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