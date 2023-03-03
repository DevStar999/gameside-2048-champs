package com.example.gameside2048champs.enums;

import lombok.Getter;

@Getter
public enum EarlyOutAchievementType {
    /* Achievement for ending game below a certain score */
    BEFORE_SCORE,

    /* Achievement for ending game below a certain tile */
    BEFORE_TILE
}
