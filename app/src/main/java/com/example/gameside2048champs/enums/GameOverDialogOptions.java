package com.example.gameside2048champs.enums;

import lombok.Getter;

@Getter
public enum GameOverDialogOptions {
    /* If the user wants to shop coins from the ShopFragment (First Page) */
    SHOP_COINS,

    /* If the user wants to continue to the second page of the game over dialog (First Page) */
    CONTINUE,

    /* If the user wants to return to the main menu (Second Page) */
    MAIN_MENU,

    /* If the user wants to play the game again (Second Page) */
    PLAY_AGAIN,

    /* If the user wants to go back to the first page of the game over dialog (Second Page) */
    BACK
}
