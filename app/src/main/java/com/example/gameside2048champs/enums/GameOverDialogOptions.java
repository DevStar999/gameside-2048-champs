package com.example.gameside2048champs.enums;

import lombok.Getter;

@Getter
public enum GameOverDialogOptions {
    /* If the user wants to return to the main menu */
    MAIN_MENU,

    /* If the user wants to play the game again */
    PLAY_AGAIN,

    /* User has made one or more moves and he/she wants to undo the last move */
    UNDO_LAST_MOVE
}
