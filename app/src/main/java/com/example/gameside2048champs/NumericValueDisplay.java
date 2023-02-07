package com.example.gameside2048champs;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class NumericValueDisplay {
    private static final Map<Long, String> gameTileValueToFormattedString;
    private static final Map<String, Long> gameTileFormattedStringToValue;

    private static int getNumberOfDigits(long value) {
        return String.valueOf(value).length();
    }

    private static String getFormattedString(long cellValue) {
        String stringValue = String.valueOf(cellValue);
        int numberOfDigits = getNumberOfDigits(cellValue);
        if (numberOfDigits <= 4) { // For (1 <= numberOfDigits <= 4)
            return stringValue;
        } else if (numberOfDigits <= 6) { // For (5 <= numberOfDigits <= 6)
            return stringValue.substring(0, stringValue.length() - 3) + "K";
        } else if (numberOfDigits <= 9) { // For (7 <= numberOfDigits <= 9)
            return stringValue.substring(0, stringValue.length() - 6) + "M";
        } else if (numberOfDigits <= 12) { // For (10 <= numberOfDigits <= 12)
            return stringValue.substring(0, stringValue.length() - 9) + "B";
        } else if (numberOfDigits <= 15) { // For (13 <= numberOfDigits <= 15)
            return stringValue.substring(0, stringValue.length() - 12) + "T";
        } else { // For (16 <= numberOfDigits <= 19)
            return stringValue.substring(0, stringValue.length() - 15) + "Q";
        }
    }

    static {
        gameTileValueToFormattedString = new HashMap<>();
        gameTileValueToFormattedString.put(0L, "0");
        long cellValue = 1;
        int powerOfTwo = 0;
        while (powerOfTwo <= 61) {
            cellValue = cellValue * 2;
            powerOfTwo++;
            gameTileValueToFormattedString.put(cellValue, getFormattedString(cellValue));
        }

        gameTileFormattedStringToValue = new HashMap<>();
        for (Map.Entry<Long, String> element: gameTileValueToFormattedString.entrySet()) {
            gameTileFormattedStringToValue.put(element.getValue(), element.getKey());
        }
    }

    public static String getGameTileValueDisplay(long cellValue) {
        return gameTileValueToFormattedString.get(cellValue);
    }

    public static long getValueFromFormattedString(String formattedString) {
        return gameTileFormattedStringToValue.get(formattedString);
    }

    private static long powerOf(long base, long index) {
        if (index == 0) {
            return 1L;
        }

        long result = 1L;
        for (int indexCounter = 1; indexCounter <= index; indexCounter++) {
            result = result * base;
        }
        return result;
    }

    private static String computeResultForGetScoreValueDisplay(DecimalFormat decimalFormat, long dividend, long score,
                                                               String numberAbbreviation) {
        long quotient = score / dividend, remainder = score % dividend;
        String quotientPartString = String.valueOf(quotient);
        String remainderPartString = String.valueOf(remainder);
        while (quotientPartString.length() + remainderPartString.length() > 6) {
            remainderPartString = remainderPartString.substring(0, remainderPartString.length() - 1);
        }
        if (remainderPartString.length() > 0) {
            return decimalFormat.format(quotient) + "." + remainderPartString + numberAbbreviation;
        } else {
            return decimalFormat.format(quotient) + numberAbbreviation;
        }
    }

    public static String getScoreValueDisplay(long score) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        int numberOfDigits = getNumberOfDigits(score);
        if (numberOfDigits <= 7) {
            return decimalFormat.format(score);
        } else if (numberOfDigits <= 9) { // Digits = 8 to 9, 1K = 1,000
            long oneThousand = powerOf(10, 3);
            return computeResultForGetScoreValueDisplay(decimalFormat, oneThousand, score, "K");
        } else if (numberOfDigits <= 12) { // Digits = 10 to 12, 1M = 1,000,000
            long oneMillion = powerOf(10, 6);
            return computeResultForGetScoreValueDisplay(decimalFormat, oneMillion, score, "M");
        } else if (numberOfDigits <= 15) { // Digits = 13 to 15, 1B = 1,000,000,000
            long oneBillion = powerOf(10, 9);
            return computeResultForGetScoreValueDisplay(decimalFormat, oneBillion, score, "B");
        } else if (numberOfDigits <= 18) { // Digits = 16 to 18, 1T = 1,000,000,000,000
            long oneTrillion = powerOf(10, 12);
            return computeResultForGetScoreValueDisplay(decimalFormat, oneTrillion, score, "T");
        } else { // Digits = 19, 1Q = 1,000,000,000,000,000
            long oneQuadrillion = powerOf(10, 15);
            return computeResultForGetScoreValueDisplay(decimalFormat, oneQuadrillion, score, "Q");
        }
    }

    public static String getGeneralValueDisplay(int value) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(value);
    }
}
