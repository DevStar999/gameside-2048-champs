package com.example.gameside2048champs;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class NumericValueDisplay {
    private static final Map<Long, String> gameTileValueToFormattedString;
    private static final Map<String, Long> gameTileFormattedStringToValue;

    static {
        gameTileValueToFormattedString = new HashMap<>();
        gameTileValueToFormattedString.put(2L, "2"); // Formatted Value = 2
        gameTileValueToFormattedString.put(4L, "4"); // Formatted Value = 4
        gameTileValueToFormattedString.put(8L, "8"); // Formatted Value = 8
        gameTileValueToFormattedString.put(16L, "16"); // Formatted Value = 16
        gameTileValueToFormattedString.put(32L, "32"); // Formatted Value = 32
        gameTileValueToFormattedString.put(64L, "64"); // Formatted Value = 64
        gameTileValueToFormattedString.put(128L, "128"); // Formatted Value = 128
        gameTileValueToFormattedString.put(256L, "256"); // Formatted Value = 256
        gameTileValueToFormattedString.put(512L, "512"); // Formatted Value = 512
        gameTileValueToFormattedString.put(1024L, "1024"); // Formatted Value = 1,024
        gameTileValueToFormattedString.put(2048L, "2048"); // Formatted Value = 2,048
        gameTileValueToFormattedString.put(4096L, "4096"); // Formatted Value = 4,096
        gameTileValueToFormattedString.put(8192L, "8192"); // Formatted Value = 8,192
        gameTileValueToFormattedString.put(16384L, "16K"); // Formatted Value = 16,384
        gameTileValueToFormattedString.put(32768L, "32K"); // Formatted Value = 32,768
        gameTileValueToFormattedString.put(65536L, "65K"); // Formatted Value = 65,536
        gameTileValueToFormattedString.put(131072L, "131K"); // Formatted Value = 131,072
        gameTileValueToFormattedString.put(262144L, "262K"); // Formatted Value = 262,144
        gameTileValueToFormattedString.put(524288L, "524K"); // Formatted Value = 524,288
        gameTileValueToFormattedString.put(1048576L, "1M"); // Formatted Value = 1,048,576
        gameTileValueToFormattedString.put(2097152L, "2M"); // Formatted Value = 2,097,152
        gameTileValueToFormattedString.put(4194304L, "4M"); // Formatted Value = 4,194,304
        gameTileValueToFormattedString.put(8388608L, "8M"); // Formatted Value = 8,388,608
        gameTileValueToFormattedString.put(16777216L, "16M"); // Formatted Value = 16,777,216
        gameTileValueToFormattedString.put(33554432L, "33M"); // Formatted Value = 33,554,432
        gameTileValueToFormattedString.put(67108864L, "67M"); // Formatted Value = 67,108,864
        gameTileValueToFormattedString.put(134217728L, "134M"); // Formatted Value = 134,217,728
        gameTileValueToFormattedString.put(268435456L, "268M"); // Formatted Value = 268,435,456
        gameTileValueToFormattedString.put(536870912L, "536M"); // Formatted Value = 536,870,912
        gameTileValueToFormattedString.put(1073741824L, "1B"); // Formatted Value = 1,073,741,824
        gameTileValueToFormattedString.put(2147483648L, "2B"); // Formatted Value = 2,147,483,648
        gameTileValueToFormattedString.put(4294967296L, "4B"); // Formatted Value = 4,294,967,296
        gameTileValueToFormattedString.put(8589934592L, "8B"); // Formatted Value = 8,589,934,592
        gameTileValueToFormattedString.put(17179869184L, "17B"); // Formatted Value = 17,179,869,184
        gameTileValueToFormattedString.put(34359738368L, "34B"); // Formatted Value = 34,359,738,368
        gameTileValueToFormattedString.put(68719476736L, "68B"); // Formatted Value = 68,719,476,736
        gameTileValueToFormattedString.put(137438953472L, "137B"); // Formatted Value = 137,438,953,472
        gameTileValueToFormattedString.put(274877906944L, "274B"); // Formatted Value = 274,877,906,944
        gameTileValueToFormattedString.put(549755813888L, "549B"); // Formatted Value = 549,755,813,888
        gameTileValueToFormattedString.put(1099511627776L, "1T"); // Formatted Value = 1,099,511,627,776
        gameTileValueToFormattedString.put(2199023255552L, "2T"); // Formatted Value = 2,199,023,255,552
        gameTileValueToFormattedString.put(4398046511104L, "4T"); // Formatted Value = 4,398,046,511,104
        gameTileValueToFormattedString.put(8796093022208L, "8T"); // Formatted Value = 8,796,093,022,208
        gameTileValueToFormattedString.put(17592186044416L, "17T"); // Formatted Value = 17,592,186,044,416
        gameTileValueToFormattedString.put(35184372088832L, "35T"); // Formatted Value = 35,184,372,088,832
        gameTileValueToFormattedString.put(70368744177664L, "70T"); // Formatted Value = 70,368,744,177,664
        gameTileValueToFormattedString.put(140737488355328L, "140T"); // Formatted Value = 140,737,488,355,328
        gameTileValueToFormattedString.put(281474976710656L, "281T"); // Formatted Value = 281,474,976,710,656
        gameTileValueToFormattedString.put(562949953421312L, "562T"); // Formatted Value = 562,949,953,421,312
        gameTileValueToFormattedString.put(1125899906842624L, "1Q"); // Formatted Value = 1,125,899,906,842,624
        gameTileValueToFormattedString.put(2251799813685248L, "2Q"); // Formatted Value = 2,251,799,813,685,248
        gameTileValueToFormattedString.put(4503599627370496L, "4Q"); // Formatted Value = 4,503,599,627,370,496
        gameTileValueToFormattedString.put(9007199254740992L, "9Q"); // Formatted Value = 9,007,199,254,740,992
        gameTileValueToFormattedString.put(18014398509481984L, "18Q"); // Formatted Value = 18,014,398,509,481,984
        gameTileValueToFormattedString.put(36028797018963968L, "36Q"); // Formatted Value = 36,028,797,018,963,968
        gameTileValueToFormattedString.put(72057594037927936L, "72Q"); // Formatted Value = 72,057,594,037,927,936
        gameTileValueToFormattedString.put(144115188075855872L, "144Q"); // Formatted Value = 144,115,188,075,855,872
        gameTileValueToFormattedString.put(288230376151711744L, "288Q"); // Formatted Value = 288,230,376,151,711,744
        gameTileValueToFormattedString.put(576460752303423488L, "576Q"); // Formatted Value = 576,460,752,303,423,488
        gameTileValueToFormattedString.put(1152921504606846976L, "1152Q"); // Formatted Value = 1,152,921,504,606,846,976
        gameTileValueToFormattedString.put(2305843009213693952L, "2305Q"); // Formatted Value = 2,305,843,009,213,693,952
        gameTileValueToFormattedString.put(4611686018427387904L, "4611Q"); // Formatted Value = 4,611,686,018,427,387,904

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

    private static int getNumberOfDigits(long value) {
        return String.valueOf(value).length();
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
