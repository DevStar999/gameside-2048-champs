package com.example.gameside2048champs.enums;

import static com.google.android.gms.games.achievement.Achievement.STATE_HIDDEN;
import static com.google.android.gms.games.achievement.Achievement.STATE_REVEALED;

import com.example.gameside2048champs.R;

import lombok.Getter;

@Getter
public enum ChangeValueToolAchievements {
    CHANGE_VALUE_TOOL_ACHIEVEMENT_LEVEL_1("Charlatan", R.string.achievement_charlatan,
            1, 100, STATE_REVEALED),
    CHANGE_VALUE_TOOL_ACHIEVEMENT_LEVEL_2("Wizard", R.string.achievement_wizard,
            2, 250, STATE_HIDDEN),
    CHANGE_VALUE_TOOL_ACHIEVEMENT_LEVEL_3("Sorcerer Supreme", R.string.achievement_sorcerer_supreme,
            3, 500, STATE_HIDDEN);
    
    private String nameOfAchievement; // Name as per the name given in the Google Play Games project
    private int achievementStringResourceId; // Achievement Id as per the Id generated in the Google Play Games project
    private int levelOfAchievement; // Custom information for us to keep track of the level of achievement completed
    private int achievementThresholdUseCount; // The assigned achievement threshold use count
    private int initialAchievementState; // The initial state of achievement as per the Google Play Games project

    ChangeValueToolAchievements(String nameOfAchievement, int achievementStringResourceId, int levelOfAchievement,
                              int achievementThresholdUseCount, int initialAchievementState) {
        this.nameOfAchievement = nameOfAchievement;
        this.achievementStringResourceId = achievementStringResourceId;
        this.levelOfAchievement = levelOfAchievement;
        this.achievementThresholdUseCount = achievementThresholdUseCount;
        this.initialAchievementState = initialAchievementState;
    }
}
