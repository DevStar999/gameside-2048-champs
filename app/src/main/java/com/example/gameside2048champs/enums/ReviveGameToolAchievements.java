package com.example.gameside2048champs.enums;

import static com.google.android.gms.games.achievement.Achievement.STATE_HIDDEN;
import static com.google.android.gms.games.achievement.Achievement.STATE_REVEALED;

import com.example.gameside2048champs.R;

import lombok.Getter;

@Getter
public enum ReviveGameToolAchievements {
    REVIVE_GAME_TOOL_ACHIEVEMENT_LEVEL_1("Revival Expert", R.string.achievement_revival_expert,
            1, 50, STATE_REVEALED),
    REVIVE_GAME_TOOL_ACHIEVEMENT_LEVEL_2("Cure All", R.string.achievement_cure_all,
            2, 100, STATE_HIDDEN),
    REVIVE_GAME_TOOL_ACHIEVEMENT_LEVEL_3("Fountain of Life", R.string.achievement_fountain_of_life,
            3, 200, STATE_HIDDEN);

    private String nameOfAchievement; // Name as per the name given in the Google Play Games project
    private int achievementStringResourceId; // Achievement Id as per the Id generated in the Google Play Games project
    private int levelOfAchievement; // Custom information for us to keep track of the level of achievement completed
    private int achievementThresholdUseCount; // The assigned achievement threshold use count
    private int initialAchievementState; // The initial state of achievement as per the Google Play Games project

    ReviveGameToolAchievements(String nameOfAchievement, int achievementStringResourceId, int levelOfAchievement,
                               int achievementThresholdUseCount, int initialAchievementState) {
        this.nameOfAchievement = nameOfAchievement;
        this.achievementStringResourceId = achievementStringResourceId;
        this.levelOfAchievement = levelOfAchievement;
        this.achievementThresholdUseCount = achievementThresholdUseCount;
        this.initialAchievementState = initialAchievementState;
    }
}
