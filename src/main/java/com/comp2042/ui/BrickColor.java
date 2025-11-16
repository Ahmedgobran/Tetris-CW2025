package com.comp2042.ui;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/*
 handles mapping of brick color codes to JavaFX Paint objects
 Separates color logic from GUI controller responsibilities to make code shorter and more readable
 */

public class BrickColor {

    private static final double SHADOW_OPACITY = 0.3;



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