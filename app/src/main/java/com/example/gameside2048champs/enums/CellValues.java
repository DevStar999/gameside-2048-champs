package com.example.gameside2048champs.enums;

import com.example.gameside2048champs.R;

import lombok.Getter;

@Getter
public enum CellValues {
    CELL_VALUE_EMPTY(0, R.color.transparent_color, R.color.empty_cell, R.drawable.cell_empty),
    CELL_VALUE_0002(2, R.color.number_color_0002, R.color.background_color_0002, R.drawable.cell_value_0002),
    CELL_VALUE_0004(4, R.color.number_color_0004, R.color.background_color_0004, R.drawable.cell_value_0004),
    CELL_VALUE_0008(8, R.color.number_color_0008, R.color.background_color_0008, R.drawable.cell_value_0008),
    CELL_VALUE_0016(16, R.color.number_color_0016, R.color.background_color_0016, R.drawable.cell_value_0016),
    CELL_VALUE_0032(32, R.color.number_color_0032, R.color.background_color_0032, R.drawable.cell_value_0032),
    CELL_VALUE_0064(64, R.color.number_color_0064, R.color.background_color_0064, R.drawable.cell_value_0064),
    CELL_VALUE_0128(128, R.color.number_color_0128, R.color.background_color_0128, R.drawable.cell_value_0128),
    CELL_VALUE_0256(256, R.color.number_color_0256, R.color.background_color_0256, R.drawable.cell_value_0256),
    CELL_VALUE_0512(512, R.color.number_color_0512, R.color.background_color_0512, R.drawable.cell_value_0512),
    CELL_VALUE_1024(1024, R.color.number_color_1024, R.color.background_color_1024, R.drawable.cell_value_1024),
    CELL_VALUE_2048(2048, R.color.number_color_2048, R.color.background_color_2048, R.drawable.cell_value_2048),
    CELL_VALUE_4096(4096, R.color.number_color_4096, R.color.background_color_4096, R.drawable.cell_value_4096),
    CELL_VALUE_8192(8192, R.color.number_color_8192, R.color.background_color_8192, R.drawable.cell_value_8192),
    CELL_VALUE_HIGHER_ORDER(Integer.MAX_VALUE, R.color.number_color_higher_order,
            R.color.background_color_higher_order, R.drawable.cell_value_higher_order);

    private int cellValue;
    private final int numberColorResourceId;
    private final int backgroundColorResourceId;
    private final int backgroundDrawableResourceId;

    CellValues(int cellValue, int numberColorResourceId,
               int backgroundColorResourceId, int backgroundDrawableResourceId) {
        this.cellValue = cellValue;
        this.numberColorResourceId = numberColorResourceId;
        this.backgroundColorResourceId = backgroundColorResourceId;
        this.backgroundDrawableResourceId = backgroundDrawableResourceId;
    }

    public static CellValues getCellValueEnum(int cellValue) {
        switch (cellValue) {
            case 0: return valueOf("CELL_VALUE_EMPTY");
            case 2: return valueOf("CELL_VALUE_0002");
            case 4: return valueOf("CELL_VALUE_0004");
            case 8: return valueOf("CELL_VALUE_0008");
            case 16: return valueOf("CELL_VALUE_0016");
            case 32: return valueOf("CELL_VALUE_0032");
            case 64: return valueOf("CELL_VALUE_0064");
            case 128: return valueOf("CELL_VALUE_0128");
            case 256: return valueOf("CELL_VALUE_0256");
            case 512: return valueOf("CELL_VALUE_0512");
            case 1024: return valueOf("CELL_VALUE_1024");
            case 2048: return valueOf("CELL_VALUE_2048");
            case 4096: return valueOf("CELL_VALUE_4096");
            case 8192: return valueOf("CELL_VALUE_8192");
            default: return valueOf("CELL_VALUE_HIGHER_ORDER");
        }
    }
}
