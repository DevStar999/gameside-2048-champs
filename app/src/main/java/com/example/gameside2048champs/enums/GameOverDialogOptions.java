package com.example.gameside2048champs.enums;

import lombok.Getter;

@Getter
public enum GameOverDialogOptions {
    /* If the user wants to shop coins from the ShopFragment (First Page) */
    SHOP_COINS,
    /* If the user chose to use the Standard Tool Undo (First Page) */
    STANDARD_TOOL_UNDO,
    /* If the user chose to use the Standard Tool Smash Tile (First Page) */
    STANDARD_TOOL_SMASH_TILE,
    /* If the user chose to use the Standard Tool Change Value (First Page) */
    STANDARD_TOOL_CHANGE_VALUE,
    /* If the user chose to use the Special Tool Swap Tiles (First Page) */
    SPECIAL_TOOL_SWAP_TILES,
    /* If the user chose to use the Special Tool Eliminate Value (First Page) */
    SPECIAL_TOOL_ELIMINATE_VALUE,
    /* If the user chose to use the Special Tool Destroy Area (First Page) */
    SPECIAL_TOOL_DESTROY_AREA,

    /* If the user wants to return to the main menu (Second Page) */
    MAIN_MENU,
    /* If the user wants to play the game again (Second Page) */
    PLAY_AGAIN,
}
