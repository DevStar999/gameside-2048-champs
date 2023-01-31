package com.example.gameside2048champs.enums;

import com.example.gameside2048champs.R;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public enum TileUnlockAchievements {
    TILE_UNLOCK_ACHIEVEMENT_LEVEL_1("Masterful", R.string.achievement_masterful, 1,
            2048L),
    TILE_UNLOCK_ACHIEVEMENT_LEVEL_2("Brainiac", R.string.achievement_brainiac, 1,
            4096L);

    private String nameOfAchievement; // Name as per the name given in the Google Play Games project
    private int achievementStringResourceId; // Achievement Id as per the Id generated in the Google Play Games project
    private int levelOfAchievement; // Custom information for us to keep track of the level of achievement completed
    private long achievementTileValue;

    TileUnlockAchievements(String nameOfAchievement, int achievementStringResourceId, int levelOfAchievement,
                           long achievementTileValue) {
        this.nameOfAchievement = nameOfAchievement;
        this.achievementStringResourceId = achievementStringResourceId;
        this.levelOfAchievement = levelOfAchievement;
        this.achievementTileValue = achievementTileValue;
    }
}
