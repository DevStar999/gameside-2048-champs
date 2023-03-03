package com.example.gameside2048champs.enums;

import com.example.gameside2048champs.R;

import lombok.Getter;

@Getter
public enum EarlyOutAchievements {
    EARLY_OUT_ACHIEVEMENT_NEWBIE("Newbie", R.string.achievement_newbie,
            1000L, EarlyOutAchievementType.BEFORE_SCORE),
    EARLY_OUT_ACHIEVEMENT_ROOKIE("Rookie", R.string.achievement_rookie,
            256L, EarlyOutAchievementType.BEFORE_TILE),
    EARLY_OUT_ACHIEVEMENT_TRICKSTER("Trickster", R.string.achievement_trickster,
            500L, EarlyOutAchievementType.BEFORE_SCORE),
    EARLY_OUT_ACHIEVEMENT_SCAMMER("Scammer", R.string.achievement_scammer,
            128L, EarlyOutAchievementType.BEFORE_TILE);

    private String nameOfAchievement; // Name as per the name given in the Google Play Games project
    private int achievementStringResourceId; // Achievement Id as per the Id generated in the Google Play Games project
    private long achievementThreshold; // The assigned achievement threshold (Either score or tile value)
    private EarlyOutAchievementType earlyOutAchievementType; // The type of early out achievement

    EarlyOutAchievements(String nameOfAchievement, int achievementStringResourceId, long achievementThreshold,
                         EarlyOutAchievementType earlyOutAchievementType) {
        this.nameOfAchievement = nameOfAchievement;
        this.achievementStringResourceId = achievementStringResourceId;
        this.achievementThreshold = achievementThreshold;
        this.earlyOutAchievementType = earlyOutAchievementType;
    }
}
