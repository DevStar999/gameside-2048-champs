package com.example.gameside2048champs.enums;

import static com.google.android.gms.games.achievement.Achievement.STATE_HIDDEN;
import static com.google.android.gms.games.achievement.Achievement.STATE_REVEALED;

import com.example.gameside2048champs.R;

import lombok.Getter;

@Getter
public enum SmashTileToolAchievements {
    SMASH_TILE_TOOL_ACHIEVEMENT_LEVEL_1("Smasher", R.string.achievement_smasher,
            1, 100, STATE_REVEALED),
    SMASH_TILE_TOOL_ACHIEVEMENT_LEVEL_2("Destroyer", R.string.achievement_destroyer,
            2, 250, STATE_HIDDEN),
    SMASH_TILE_TOOL_ACHIEVEMENT_LEVEL_3("Hammer of Thor", R.string.achievement_hammer_of_thor,
            3, 500, STATE_HIDDEN);

    private String nameOfAchievement; // Name as per the name given in the Google Play Games project
    private int achievementStringResourceId; // Achievement Id as per the Id generated in the Google Play Games project
    private int levelOfAchievement; // Custom information for us to keep track of the level of achievement completed
    private int achievementThresholdUseCount; // The assigned achievement threshold use count
    private int initialAchievementState; // The initial state of achievement as per the Google Play Games project

    SmashTileToolAchievements(String nameOfAchievement, int achievementStringResourceId, int levelOfAchievement,
                         int achievementThresholdUseCount, int initialAchievementState) {
        this.nameOfAchievement = nameOfAchievement;
        this.achievementStringResourceId = achievementStringResourceId;
        this.levelOfAchievement = levelOfAchievement;
        this.achievementThresholdUseCount = achievementThresholdUseCount;
        this.initialAchievementState = initialAchievementState;
    }
}
