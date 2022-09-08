package com.example.gameside2048champs.enums;

import lombok.Getter;

@Getter
public enum GameStates {
    /* Game has just started, starting random values have been added, but no move has been made yet */
    GAME_START,

    /* User cannot make any more moves */
    GAME_OVER,

    /* User has made one or more moves */
    GAME_ONGOING
}
