package com.example.gameside2048champs.enums;

import lombok.Getter;

@Getter
public enum GameLayoutProperties {
    /* Text Size Fine Tuning Notes -
    a) 1, 2 and 3 digit numbers should have the same size and it should look like value is
    filling the cell, but not to extreme (While testing put randomStartValue = 8)
    b) 4 digit numbers should leave some space such that they don't look like they
    are touching the border horizontally
    c) The higher order numbers should be such size that the number '262144' has the last digit '4' on the next line
    */
    SQUARE_3X3_LAYOUT_PROPERTIES("SQUARE_3X3", "1:1", 4,
            45, 45, 45, 38, 32),
    SQUARE_4X4_LAYOUT_PROPERTIES("SQUARE_4X4", "1:1", 4,
            36, 36, 36, 28, 24),
    SQUARE_5X5_LAYOUT_PROPERTIES("SQUARE_5X5", "1:1", 3,
            30, 30, 30, 22, 19),
    SQUARE_6X6_LAYOUT_PROPERTIES("SQUARE_6X6", "1:1", 2,
            27, 27, 27, 20, 16),
    RECTANGLE_3X4_LAYOUT_PROPERTIES("RECTANGLE_3X4", "1:1", 4,
            42, 42, 42, 36, 32),
    RECTANGLE_3X5_LAYOUT_PROPERTIES("RECTANGLE_3X5", "1:1.15", 4,
            42, 42, 42, 36, 32),
    RECTANGLE_4X5_LAYOUT_PROPERTIES("RECTANGLE_4X5", "1:1", 3,
            38, 38, 38, 28, 24),
    RECTANGLE_4X6_LAYOUT_PROPERTIES("RECTANGLE_4X6", "1:1.15", 3,
            38, 38, 38, 28, 24),
    RECTANGLE_5X6_LAYOUT_PROPERTIES("RECTANGLE_5X6", "1:1", 2,
            32, 32, 32, 23, 20),
    BLOCK_MIDDLE_SQUARE_5X5_LAYOUT_PROPERTIES("BLOCK_MIDDLE_SQUARE_5X5", "1:1", 3,
            30, 30, 30, 22, 19),
    BLOCK_MIDDLE_SQUARE_6X6_LAYOUT_PROPERTIES("BLOCK_MIDDLE_SQUARE_6X6", "1:1", 3,
            27, 27, 27, 20, 16),
    BLOCK_MIDDLE_RECTANGLE_3X4_LAYOUT_PROPERTIES("BLOCK_MIDDLE_RECTANGLE_3X4", "1:1", 4,
            42, 42, 42, 36, 32),
    BLOCK_MIDDLE_RECTANGLE_3X5_LAYOUT_PROPERTIES("BLOCK_MIDDLE_RECTANGLE_3X5", "1:1.15", 4,
            42, 42, 42, 36, 32),
    BLOCK_2_CORNERS_SQUARE_4X4_LAYOUT_PROPERTIES("BLOCK_2_CORNERS_SQUARE_4X4", "1:1", 4,
            36, 36, 36, 28, 24),
    BLOCK_2_CORNERS_SQUARE_5X5_LAYOUT_PROPERTIES("BLOCK_2_CORNERS_SQUARE_5X5", "1:1", 4,
            30, 30, 30, 22, 19),
    BLOCK_2_CORNERS_RECTANGLE_3X4_LAYOUT_PROPERTIES("BLOCK_2_CORNERS_RECTANGLE_3X4", "1:1", 4,
            42, 42, 42, 36, 32),
    BLOCK_2_CORNERS_RECTANGLE_3X5_LAYOUT_PROPERTIES("BLOCK_2_CORNERS_RECTANGLE_3X5", "1:1.15", 4,
            42, 42, 42, 36, 32);

    private final String gameModeName;
    private final String dimensionRatio;
    private final int spacing; // The spacing that we want in dp [Note: Screen Display Density is taken care of elsewhere]
    // Here, Nts -> Number Text Size
    private final float oneDigitNts;
    private final float twoDigitNts;
    private final float threeDigitNts;
    private final float fourDigitNts;
    private final float highOrderNts;

    GameLayoutProperties(String gameModeName, String dimensionRatio, int spacing,
                         float oneDigitNts, float twoDigitNts, float threeDigitNts,
                         float fourDigitNts, float highOrderNts) {
        this.gameModeName = gameModeName;
        this.dimensionRatio = dimensionRatio;
        this.spacing = spacing;
        this.oneDigitNts = oneDigitNts;
        this.twoDigitNts = twoDigitNts;
        this.threeDigitNts = threeDigitNts;
        this.fourDigitNts = fourDigitNts;
        this.highOrderNts = highOrderNts;
    }

    public float getTextSizeForValue(int value) {
        int numberOfDigits = 0;
        while (value != 0) {
            value /= 10;
            numberOfDigits++;
        }
        switch (numberOfDigits) {
            case 1: {
                return oneDigitNts;
            } case 2: {
                return twoDigitNts;
            } case 3: {
                return threeDigitNts;
            } case 4: {
                return fourDigitNts;
            } default: {
                return highOrderNts;
            }
        }
    }
}