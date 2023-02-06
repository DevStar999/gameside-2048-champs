package com.example.gameside2048champs.enums;

import com.example.gameside2048champs.R;

import lombok.Getter;

@Getter
public enum CellValues {
    CELL_VALUE_EMPTY(0L, R.color.game_tile_number_color_light, R.color.empty_cell, R.drawable.cell_empty),
    CELL_VALUE_1_2(2L, R.color.game_tile_number_color_light, R.color.background_color_1_2, R.drawable.cell_value_1_2),
    CELL_VALUE_2_4(4L, R.color.game_tile_number_color_light, R.color.background_color_2_4, R.drawable.cell_value_2_4),
    CELL_VALUE_3_8(8L, R.color.game_tile_number_color_light, R.color.background_color_3_8, R.drawable.cell_value_3_8),
    CELL_VALUE_4_16(16L, R.color.game_tile_number_color_light, R.color.background_color_4_16, R.drawable.cell_value_4_16),
    CELL_VALUE_5_32(32L, R.color.game_tile_number_color_light, R.color.background_color_5_32, R.drawable.cell_value_5_32),
    CELL_VALUE_6_64(64L, R.color.game_tile_number_color_light, R.color.background_color_6_64, R.drawable.cell_value_6_64),
    CELL_VALUE_7_128(128L, R.color.game_tile_number_color_light, R.color.background_color_7_128, R.drawable.cell_value_7_128),
    CELL_VALUE_8_256(256L, R.color.game_tile_number_color_dark, R.color.background_color_8_256, R.drawable.cell_value_8_256),
    CELL_VALUE_9_512(512L, R.color.game_tile_number_color_dark, R.color.background_color_9_512, R.drawable.cell_value_9_512),
    CELL_VALUE_10_1024(1024L, R.color.game_tile_number_color_dark, R.color.background_color_10_1024, R.drawable.cell_value_10_1024),
    CELL_VALUE_11_2048(2048L, R.color.game_tile_number_color_light, R.color.background_color_11_2048, R.drawable.cell_value_11_2048),
    CELL_VALUE_12_4096(4096L, R.color.game_tile_number_color_light, R.color.background_color_12_4096, R.drawable.cell_value_12_4096),
    CELL_VALUE_13_8192(8192L, R.color.game_tile_number_color_dark, R.color.background_color_13_8192, R.drawable.cell_value_13_8192),
    CELL_VALUE_14_16384(16384L, R.color.game_tile_number_color_dark, R.color.background_color_14_16384, R.drawable.cell_value_14_16384),
    CELL_VALUE_15_32768(32768L, R.color.game_tile_number_color_light, R.color.background_color_15_32768, R.drawable.cell_value_15_32768),
    CELL_VALUE_16_65536(65536L, R.color.game_tile_number_color_light, R.color.background_color_16_65536, R.drawable.cell_value_16_65536),
    CELL_VALUE_17_131072(131072L, R.color.game_tile_number_color_light, R.color.background_color_17_131072, R.drawable.cell_value_17_131072),
    CELL_VALUE_18_262144(262144L, R.color.game_tile_number_color_light, R.color.background_color_18_262144, R.drawable.cell_value_18_262144),
    CELL_VALUE_19_524288(524288L, R.color.game_tile_number_color_light, R.color.background_color_19_524288, R.drawable.cell_value_19_524288),
    CELL_VALUE_20_1048576(1048576L, R.color.game_tile_number_color_dark, R.color.background_color_20_1048576, R.drawable.cell_value_20_1048576),
    CELL_VALUE_21_2097152(2097152L, R.color.game_tile_number_color_light, R.color.background_color_21_2097152, R.drawable.cell_value_21_2097152),
    CELL_VALUE_22_4194304(4194304L, R.color.game_tile_number_color_dark, R.color.background_color_22_4194304, R.drawable.cell_value_22_4194304),
    CELL_VALUE_23_8388608(8388608L, R.color.game_tile_number_color_light, R.color.background_color_23_8388608, R.drawable.cell_value_23_8388608),
    CELL_VALUE_24_16777216(16777216L, R.color.game_tile_number_color_light, R.color.background_color_24_16777216, R.drawable.cell_value_24_16777216),
    CELL_VALUE_25_33554432(33554432L, R.color.game_tile_number_color_light, R.color.background_color_25_33554432, R.drawable.cell_value_25_33554432),
    CELL_VALUE_26_67108864(67108864L, R.color.game_tile_number_color_dark, R.color.background_color_26_67108864, R.drawable.cell_value_26_67108864),
    CELL_VALUE_27_134217728(134217728L, R.color.game_tile_number_color_light, R.color.background_color_27_134217728, R.drawable.cell_value_27_134217728),
    CELL_VALUE_28_268435456(268435456L, R.color.game_tile_number_color_dark, R.color.background_color_28_268435456, R.drawable.cell_value_28_268435456),
    CELL_VALUE_29_536870912(536870912L, R.color.game_tile_number_color_light, R.color.background_color_29_536870912, R.drawable.cell_value_29_536870912),
    CELL_VALUE_30_1073741824(1073741824L, R.color.game_tile_number_color_dark, R.color.background_color_30_1073741824, R.drawable.cell_value_30_1073741824),
    CELL_VALUE_31_2147483648(2147483648L, R.color.game_tile_number_color_light, R.color.background_color_31_2147483648, R.drawable.cell_value_31_2147483648),
    CELL_VALUE_32_4294967296(4294967296L, R.color.game_tile_number_color_dark, R.color.background_color_32_4294967296, R.drawable.cell_value_32_4294967296),
    CELL_VALUE_33_8589934592(8589934592L, R.color.game_tile_number_color_light, R.color.background_color_33_8589934592, R.drawable.cell_value_33_8589934592),
    CELL_VALUE_34_17179869184(17179869184L, R.color.game_tile_number_color_light, R.color.background_color_34_17179869184, R.drawable.cell_value_34_17179869184),
    CELL_VALUE_35_34359738368(34359738368L, R.color.game_tile_number_color_light, R.color.background_color_35_34359738368, R.drawable.cell_value_35_34359738368),
    CELL_VALUE_36_68719476736(68719476736L, R.color.game_tile_number_color_light, R.color.background_color_36_68719476736, R.drawable.cell_value_36_68719476736),
    CELL_VALUE_37_137438953472(137438953472L, R.color.game_tile_number_color_light, R.color.background_color_37_137438953472, R.drawable.cell_value_37_137438953472),
    CELL_VALUE_38_274877906944(274877906944L, R.color.game_tile_number_color_light, R.color.background_color_38_274877906944, R.drawable.cell_value_38_274877906944),
    CELL_VALUE_39_549755813888(549755813888L, R.color.game_tile_number_color_dark, R.color.background_color_39_549755813888, R.drawable.cell_value_39_549755813888),
    CELL_VALUE_40_1099511627776(1099511627776L, R.color.game_tile_number_color_dark, R.color.background_color_40_1099511627776, R.drawable.cell_value_40_1099511627776),
    CELL_VALUE_41_2199023255552(2199023255552L, R.color.game_tile_number_color_light, R.color.background_color_41_2199023255552, R.drawable.cell_value_41_2199023255552),
    CELL_VALUE_42_4398046511104(4398046511104L, R.color.game_tile_number_color_dark, R.color.background_color_42_4398046511104, R.drawable.cell_value_42_4398046511104),
    CELL_VALUE_43_8796093022208(8796093022208L, R.color.game_tile_number_color_light, R.color.background_color_43_8796093022208, R.drawable.cell_value_43_8796093022208),
    CELL_VALUE_44_17592186044416(17592186044416L, R.color.game_tile_number_color_dark, R.color.background_color_44_17592186044416, R.drawable.cell_value_44_17592186044416),
    CELL_VALUE_45_35184372088832(35184372088832L, R.color.game_tile_number_color_light, R.color.background_color_45_35184372088832, R.drawable.cell_value_45_35184372088832),
    CELL_VALUE_46_70368744177664(70368744177664L, R.color.game_tile_number_color_dark, R.color.background_color_46_70368744177664, R.drawable.cell_value_46_70368744177664),
    CELL_VALUE_47_140737488355328(140737488355328L, R.color.game_tile_number_color_light, R.color.background_color_47_140737488355328, R.drawable.cell_value_47_140737488355328),
    CELL_VALUE_48_281474976710656(281474976710656L, R.color.game_tile_number_color_light, R.color.background_color_48_281474976710656, R.drawable.cell_value_48_281474976710656),
    CELL_VALUE_49_562949953421312(562949953421312L, R.color.game_tile_number_color_light, R.color.background_color_49_562949953421312, R.drawable.cell_value_49_562949953421312),
    CELL_VALUE_50_1125899906842624(1125899906842624L, R.color.game_tile_number_color_light, R.color.background_color_50_1125899906842624, R.drawable.cell_value_50_1125899906842624),
    CELL_VALUE_51_2251799813685248(2251799813685248L, R.color.game_tile_number_color_dark, R.color.background_color_51_2251799813685248, R.drawable.cell_value_51_2251799813685248),
    CELL_VALUE_52_4503599627370496(4503599627370496L, R.color.game_tile_number_color_light, R.color.background_color_52_4503599627370496, R.drawable.cell_value_52_4503599627370496),
    CELL_VALUE_53_9007199254740992(9007199254740992L, R.color.game_tile_number_color_light, R.color.background_color_53_9007199254740992, R.drawable.cell_value_53_9007199254740992),
    CELL_VALUE_54_18014398509481984(18014398509481984L, R.color.game_tile_number_color_dark, R.color.background_color_54_18014398509481984, R.drawable.cell_value_54_18014398509481984),
    CELL_VALUE_55_36028797018963968(36028797018963968L, R.color.game_tile_number_color_light, R.color.background_color_55_36028797018963968, R.drawable.cell_value_55_36028797018963968),
    CELL_VALUE_56_72057594037927936(72057594037927936L, R.color.game_tile_number_color_light, R.color.background_color_56_72057594037927936, R.drawable.cell_value_56_72057594037927936),
    CELL_VALUE_57_144115188075855872(144115188075855872L, R.color.game_tile_number_color_dark, R.color.background_color_57_144115188075855872, R.drawable.cell_value_57_144115188075855872),
    CELL_VALUE_58_288230376151711744(288230376151711744L, R.color.game_tile_number_color_light, R.color.background_color_58_288230376151711744, R.drawable.cell_value_58_288230376151711744),
    CELL_VALUE_59_576460752303423488(576460752303423488L, R.color.game_tile_number_color_light, R.color.background_color_59_576460752303423488, R.drawable.cell_value_59_576460752303423488),
    CELL_VALUE_60_1152921504606846976(1152921504606846976L, R.color.game_tile_number_color_dark, R.color.background_color_60_1152921504606846976, R.drawable.cell_value_60_1152921504606846976),
    CELL_VALUE_61_2305843009213693952(2305843009213693952L, R.color.game_tile_number_color_light, R.color.background_color_61_2305843009213693952, R.drawable.cell_value_61_2305843009213693952),
    CELL_VALUE_62_4611686018427387904(4611686018427387904L, R.color.game_tile_number_color_light, R.color.background_color_62_4611686018427387904, R.drawable.cell_value_62_4611686018427387904),
    CELL_VALUE_HIGHER_ORDER(Integer.MAX_VALUE, R.color.game_tile_number_color_light, R.color.background_color_higher_order, R.drawable.cell_value_higher_order);

    private long cellValue;
    private final int numberColorResourceId;
    private final int backgroundColorResourceId;
    private final int backgroundDrawableResourceId;

    CellValues(long cellValue, int numberColorResourceId, int backgroundColorResourceId, int backgroundDrawableResourceId) {
        this.cellValue = cellValue;
        this.numberColorResourceId = numberColorResourceId;
        this.backgroundColorResourceId = backgroundColorResourceId;
        this.backgroundDrawableResourceId = backgroundDrawableResourceId;
    }

    private void setCellValue(long givenCellValue) {
        this.cellValue = givenCellValue;
    }

    public static CellValues getCellValueEnum(long cellValue) {
        if (cellValue == 0L) { return valueOf("CELL_VALUE_EMPTY"); }
        else if (cellValue == 2L) { return valueOf("CELL_VALUE_1_2"); }
        else if (cellValue == 4L) { return valueOf("CELL_VALUE_2_4"); }
        else if (cellValue == 8L) { return valueOf("CELL_VALUE_3_8"); }
        else if (cellValue == 16L) { return valueOf("CELL_VALUE_4_16"); }
        else if (cellValue == 32L) { return valueOf("CELL_VALUE_5_32"); }
        else if (cellValue == 64L) { return valueOf("CELL_VALUE_6_64"); }
        else if (cellValue == 128L) { return valueOf("CELL_VALUE_7_128"); }
        else if (cellValue == 256L) { return valueOf("CELL_VALUE_8_256"); }
        else if (cellValue == 512L) { return valueOf("CELL_VALUE_9_512"); }
        else if (cellValue == 1024L) { return valueOf("CELL_VALUE_10_1024"); }
        else if (cellValue == 2048L) { return valueOf("CELL_VALUE_11_2048"); }
        else if (cellValue == 4096L) { return valueOf("CELL_VALUE_12_4096"); }
        else if (cellValue == 8192L) { return valueOf("CELL_VALUE_13_8192"); }
        else if (cellValue == 16384L) { return valueOf("CELL_VALUE_14_16384"); }
        else if (cellValue == 32768L) { return valueOf("CELL_VALUE_15_32768"); }
        else if (cellValue == 65536L) { return valueOf("CELL_VALUE_16_65536"); }
        else if (cellValue == 131072L) { return valueOf("CELL_VALUE_17_131072"); }
        else if (cellValue == 262144L) { return valueOf("CELL_VALUE_18_262144"); }
        else if (cellValue == 524288L) { return valueOf("CELL_VALUE_19_524288"); }
        else if (cellValue == 1048576L) { return valueOf("CELL_VALUE_20_1048576"); }
        else if (cellValue == 2097152L) { return valueOf("CELL_VALUE_21_2097152"); }
        else if (cellValue == 4194304L) { return valueOf("CELL_VALUE_22_4194304"); }
        else if (cellValue == 8388608L) { return valueOf("CELL_VALUE_23_8388608"); }
        else if (cellValue == 16777216L) { return valueOf("CELL_VALUE_24_16777216"); }
        else if (cellValue == 33554432L) { return valueOf("CELL_VALUE_25_33554432"); }
        else if (cellValue == 67108864L) { return valueOf("CELL_VALUE_26_67108864"); }
        else if (cellValue == 134217728L) { return valueOf("CELL_VALUE_27_134217728"); }
        else if (cellValue == 268435456L) { return valueOf("CELL_VALUE_28_268435456"); }
        else if (cellValue == 536870912L) { return valueOf("CELL_VALUE_29_536870912"); }
        else if (cellValue == 1073741824L) { return valueOf("CELL_VALUE_30_1073741824"); }
        else if (cellValue == 2147483648L) { return valueOf("CELL_VALUE_31_2147483648"); }
        else if (cellValue == 4294967296L) { return valueOf("CELL_VALUE_32_4294967296"); }
        else if (cellValue == 8589934592L) { return valueOf("CELL_VALUE_33_8589934592"); }
        else if (cellValue == 17179869184L) { return valueOf("CELL_VALUE_34_17179869184"); }
        else if (cellValue == 34359738368L) { return valueOf("CELL_VALUE_35_34359738368"); }
        else if (cellValue == 68719476736L) { return valueOf("CELL_VALUE_36_68719476736"); }
        else if (cellValue == 137438953472L) { return valueOf("CELL_VALUE_37_137438953472"); }
        else if (cellValue == 274877906944L) { return valueOf("CELL_VALUE_38_274877906944"); }
        else if (cellValue == 549755813888L) { return valueOf("CELL_VALUE_39_549755813888"); }
        else if (cellValue == 1099511627776L) { return valueOf("CELL_VALUE_40_1099511627776"); }
        else if (cellValue == 2199023255552L) { return valueOf("CELL_VALUE_41_2199023255552"); }
        else if (cellValue == 4398046511104L) { return valueOf("CELL_VALUE_42_4398046511104"); }
        else if (cellValue == 8796093022208L) { return valueOf("CELL_VALUE_43_8796093022208"); }
        else if (cellValue == 17592186044416L) { return valueOf("CELL_VALUE_44_17592186044416"); }
        else if (cellValue == 35184372088832L) { return valueOf("CELL_VALUE_45_35184372088832"); }
        else if (cellValue == 70368744177664L) { return valueOf("CELL_VALUE_46_70368744177664"); }
        else if (cellValue == 140737488355328L) { return valueOf("CELL_VALUE_47_140737488355328"); }
        else if (cellValue == 281474976710656L) { return valueOf("CELL_VALUE_48_281474976710656"); }
        else if (cellValue == 562949953421312L) { return valueOf("CELL_VALUE_49_562949953421312"); }
        else if (cellValue == 1125899906842624L) { return valueOf("CELL_VALUE_50_1125899906842624"); }
        else if (cellValue == 2251799813685248L) { return valueOf("CELL_VALUE_51_2251799813685248"); }
        else if (cellValue == 4503599627370496L) { return valueOf("CELL_VALUE_52_4503599627370496"); }
        else if (cellValue == 9007199254740992L) { return valueOf("CELL_VALUE_53_9007199254740992"); }
        else if (cellValue == 18014398509481984L) { return valueOf("CELL_VALUE_54_18014398509481984"); }
        else if (cellValue == 36028797018963968L) { return valueOf("CELL_VALUE_55_36028797018963968"); }
        else if (cellValue == 72057594037927936L) { return valueOf("CELL_VALUE_56_72057594037927936"); }
        else if (cellValue == 144115188075855872L) { return valueOf("CELL_VALUE_57_144115188075855872"); }
        else if (cellValue == 288230376151711744L) { return valueOf("CELL_VALUE_58_288230376151711744"); }
        else if (cellValue == 576460752303423488L) { return valueOf("CELL_VALUE_59_576460752303423488"); }
        else if (cellValue == 1152921504606846976L) { return valueOf("CELL_VALUE_60_1152921504606846976"); }
        else if (cellValue == 2305843009213693952L) { return valueOf("CELL_VALUE_61_2305843009213693952"); }
        else if (cellValue == 4611686018427387904L) { return valueOf("CELL_VALUE_62_4611686018427387904"); }
        else {
            CellValues cellValueEnum = valueOf("CELL_VALUE_HIGHER_ORDER");
            cellValueEnum.setCellValue(cellValue);
            return cellValueEnum;
        }
    }
}
