package com.example.gameside2048champs;

import java.text.DecimalFormat;

public class NumericValueDisplay {
    public static String getGameTileValueDisplay(long cellValue) {
        if (cellValue == 2L) { // Base = 2, Index (Power) = 1, Expression = 2 ^ 1
            // Value = 2
            return "2";
        } else if (cellValue == 4L) { // Base = 2, Index (Power) = 2, Expression = 2 ^ 2
            // Value = 4
            return "4";
        } else if (cellValue == 8L) { // Base = 2, Index (Power) = 3, Expression = 2 ^ 3
            // Value = 8
            return "8";
        } else if (cellValue == 16L) { // Base = 2, Index (Power) = 4, Expression = 2 ^ 4
            // Value = 16
            return "16";
        } else if (cellValue == 32L) { // Base = 2, Index (Power) = 5, Expression = 2 ^ 5
            // Value = 32
            return "32";
        } else if (cellValue == 64L) { // Base = 2, Index (Power) = 6, Expression = 2 ^ 6
            // Value = 64
            return "64";
        } else if (cellValue == 128L) { // Base = 2, Index (Power) = 7, Expression = 2 ^ 7
            // Value = 128
            return "128";
        } else if (cellValue == 256L) { // Base = 2, Index (Power) = 8, Expression = 2 ^ 8
            // Value = 256
            return "256";
        } else if (cellValue == 512L) { // Base = 2, Index (Power) = 9, Expression = 2 ^ 9
            // Value = 512
            return "512";
        } else if (cellValue == 1024L) { // Base = 2, Index (Power) = 10, Expression = 2 ^ 10
            // Value = 1,024
            return "1024";
        } else if (cellValue == 2048L) { // Base = 2, Index (Power) = 11, Expression = 2 ^ 11
            // Value = 2,048
            return "2048";
        } else if (cellValue == 4096L) { // Base = 2, Index (Power) = 12, Expression = 2 ^ 12
            // Value = 4,096
            return "4096";
        } else if (cellValue == 8192L) { // Base = 2, Index (Power) = 13, Expression = 2 ^ 13
            // Value = 8,192
            return "8192";
        } else if (cellValue == 16384L) { // Base = 2, Index (Power) = 14, Expression = 2 ^ 14
            // Value = 16,384
            return "16K";
        } else if (cellValue == 32768L) { // Base = 2, Index (Power) = 15, Expression = 2 ^ 15
            // Value = 32,768
            return "32K";
        } else if (cellValue == 65536L) { // Base = 2, Index (Power) = 16, Expression = 2 ^ 16
            // Value = 65,536
            return "65K";
        } else if (cellValue == 131072L) { // Base = 2, Index (Power) = 17, Expression = 2 ^ 17
            // Value = 131,072
            return "131K";
        } else if (cellValue == 262144L) { // Base = 2, Index (Power) = 18, Expression = 2 ^ 18
            // Value = 262,144
            return "262K";
        } else if (cellValue == 524288L) { // Base = 2, Index (Power) = 19, Expression = 2 ^ 19
            // Value = 524,288
            return "524K";
        } else if (cellValue == 1048576L) { // Base = 2, Index (Power) = 20, Expression = 2 ^ 20
            // Value = 1,048,576
            return "1M";
        } else if (cellValue == 2097152L) { // Base = 2, Index (Power) = 21, Expression = 2 ^ 21
            // Value = 2,097,152
            return "2M";
        } else if (cellValue == 4194304L) { // Base = 2, Index (Power) = 22, Expression = 2 ^ 22
            // Value = 4,194,304
            return "4M";
        } else if (cellValue == 8388608L) { // Base = 2, Index (Power) = 23, Expression = 2 ^ 23
            // Value = 8,388,608
            return "8M";
        } else if (cellValue == 16777216L) { // Base = 2, Index (Power) = 24, Expression = 2 ^ 24
            // Value = 16,777,216
            return "16M";
        } else if (cellValue == 33554432L) { // Base = 2, Index (Power) = 25, Expression = 2 ^ 25
            // Value = 33,554,432
            return "33M";
        } else if (cellValue == 67108864L) { // Base = 2, Index (Power) = 26, Expression = 2 ^ 26
            // Value = 67,108,864
            return "67M";
        } else if (cellValue == 134217728L) { // Base = 2, Index (Power) = 27, Expression = 2 ^ 27
            // Value = 134,217,728
            return "134M";
        } else if (cellValue == 268435456L) { // Base = 2, Index (Power) = 28, Expression = 2 ^ 28
            // Value = 268,435,456
            return "268M";
        } else if (cellValue == 536870912L) { // Base = 2, Index (Power) = 29, Expression = 2 ^ 29
            // Value = 536,870,912
            return "536M";
        } else if (cellValue == 1073741824L) { // Base = 2, Index (Power) = 30, Expression = 2 ^ 30
            // Value = 1,073,741,824
            return "1B";
        } else if (cellValue == 2147483648L) { // Base = 2, Index (Power) = 31, Expression = 2 ^ 31
            // Value = 2,147,483,648
            return "2B";
        } else if (cellValue == 4294967296L) { // Base = 2, Index (Power) = 32, Expression = 2 ^ 32
            // Value = 4,294,967,296
            return "4B";
        } else if (cellValue == 8589934592L) { // Base = 2, Index (Power) = 33, Expression = 2 ^ 33
            // Value = 8,589,934,592
            return "8B";
        } else if (cellValue == 17179869184L) { // Base = 2, Index (Power) = 34, Expression = 2 ^ 34
            // Value = 17,179,869,184
            return "17B";
        } else if (cellValue == 34359738368L) { // Base = 2, Index (Power) = 35, Expression = 2 ^ 35
            // Value = 34,359,738,368
            return "34B";
        } else if (cellValue == 68719476736L) { // Base = 2, Index (Power) = 36, Expression = 2 ^ 36
            // Value = 68,719,476,736
            return "68B";
        } else if (cellValue == 137438953472L) { // Base = 2, Index (Power) = 37, Expression = 2 ^ 37
            // Value = 137,438,953,472
            return "137B";
        } else if (cellValue == 274877906944L) { // Base = 2, Index (Power) = 38, Expression = 2 ^ 38
            // Value = 274,877,906,944
            return "274B";
        } else if (cellValue == 549755813888L) { // Base = 2, Index (Power) = 39, Expression = 2 ^ 39
            // Value = 549,755,813,888
            return "549B";
        } else if (cellValue == 1099511627776L) { // Base = 2, Index (Power) = 40, Expression = 2 ^ 40
            // Value = 1,099,511,627,776
            return "1T";
        } else if (cellValue == 2199023255552L) { // Base = 2, Index (Power) = 41, Expression = 2 ^ 41
            // Value = 2,199,023,255,552
            return "2T";
        } else if (cellValue == 4398046511104L) { // Base = 2, Index (Power) = 42, Expression = 2 ^ 42
            // Value = 4,398,046,511,104
            return "4T";
        } else if (cellValue == 8796093022208L) { // Base = 2, Index (Power) = 43, Expression = 2 ^ 43
            // Value = 8,796,093,022,208
            return "8T";
        } else if (cellValue == 17592186044416L) { // Base = 2, Index (Power) = 44, Expression = 2 ^ 44
            // Value = 17,592,186,044,416
            return "17T";
        } else if (cellValue == 35184372088832L) { // Base = 2, Index (Power) = 45, Expression = 2 ^ 45
            // Value = 35,184,372,088,832
            return "35T";
        } else if (cellValue == 70368744177664L) { // Base = 2, Index (Power) = 46, Expression = 2 ^ 46
            // Value = 70,368,744,177,664
            return "70T";
        } else if (cellValue == 140737488355328L) { // Base = 2, Index (Power) = 47, Expression = 2 ^ 47
            // Value = 140,737,488,355,328
            return "140T";
        } else if (cellValue == 281474976710656L) { // Base = 2, Index (Power) = 48, Expression = 2 ^ 48
            // Value = 281,474,976,710,656
            return "281T";
        } else if (cellValue == 562949953421312L) { // Base = 2, Index (Power) = 49, Expression = 2 ^ 49
            // Value = 562,949,953,421,312
            return "562T";
        } else if (cellValue == 1125899906842624L) { // Base = 2, Index (Power) = 50, Expression = 2 ^ 50
            // Value = 1,125,899,906,842,624
            return "1Q";
        } else if (cellValue == 2251799813685248L) { // Base = 2, Index (Power) = 51, Expression = 2 ^ 51
            // Value = 2,251,799,813,685,248
            return "2Q";
        } else if (cellValue == 4503599627370496L) { // Base = 2, Index (Power) = 52, Expression = 2 ^ 52
            // Value = 4,503,599,627,370,496
            return "4Q";
        } else if (cellValue == 9007199254740992L) { // Base = 2, Index (Power) = 53, Expression = 2 ^ 53
            // Value = 9,007,199,254,740,992
            return "9Q";
        } else if (cellValue == 18014398509481984L) { // Base = 2, Index (Power) = 54, Expression = 2 ^ 54
            // Value = 18,014,398,509,481,984
            return "18Q";
        } else if (cellValue == 36028797018963968L) { // Base = 2, Index (Power) = 55, Expression = 2 ^ 55
            // Value = 36,028,797,018,963,968
            return "36Q";
        } else if (cellValue == 72057594037927936L) { // Base = 2, Index (Power) = 56, Expression = 2 ^ 56
            // Value = 72,057,594,037,927,936
            return "72Q";
        } else if (cellValue == 144115188075855872L) { // Base = 2, Index (Power) = 57, Expression = 2 ^ 57
            // Value = 144,115,188,075,855,872
            return "144Q";
        } else if (cellValue == 288230376151711744L) { // Base = 2, Index (Power) = 58, Expression = 2 ^ 58
            // Value = 288,230,376,151,711,744
            return "288Q";
        } else if (cellValue == 576460752303423488L) { // Base = 2, Index (Power) = 59, Expression = 2 ^ 59
            // Value = 576,460,752,303,423,488
            return "576Q";
        } else if (cellValue == 1152921504606846976L) { // Base = 2, Index (Power) = 60, Expression = 2 ^ 60
            // Value = 1,152,921,504,606,846,976
            return "1152Q";
        } else if (cellValue == 2305843009213693952L) { // Base = 2, Index (Power) = 61, Expression = 2 ^ 61
            // Value = 2,305,843,009,213,693,952
            return "2305Q";
        } else if (cellValue == 4611686018427387904L) { // Base = 2, Index (Power) = 62, Expression = 2 ^ 62
            // Value = 4,611,686,018,427,387,904
            return "4611Q";
        }

        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(cellValue);
    }

    public static long getCellValueFromFormattedGameTileString(String formattedGameTileString) {
        if (formattedGameTileString.equals("2")) { // FormattedString = 2, Base = 2, Index (Power) = 1
            // Expression = 2 ^ 1, Value = 2
            return 2L;
        } else if (formattedGameTileString.equals("4")) {
            return 4L;
        }
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
