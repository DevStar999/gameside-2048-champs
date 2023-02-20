package com.example.gameside2048champs.enums;

import com.example.gameside2048champs.R;

import lombok.Getter;

@Getter
public enum ScoringAchievements {
    SCORING_ACHIEVEMENT_LEVEL_1("Novice", R.string.achievement_novice, 1,
            10000L),
    SCORING_ACHIEVEMENT_LEVEL_2("Skilled", R.string.achievement_skilled, 2,
            100000L),
    SCORING_ACHIEVEMENT_LEVEL_3("Genius \uD83E\uDD13", R.string.achievement_genius, 3,
            1000000L),
    SCORING_ACHIEVEMENT_LEVEL_4("Champion \uD83D\uDC51", R.string.achievement_champion, 4,
            10000000L),
    SCORING_ACHIEVEMENT_LEVEL_5("God Tier \uD83D\uDCAB", R.string.achievement_god_tier, 5,
            100000000L);

    private String nameOfAchievement; // Name as per the name given in the Google Play Games project
    private int achievementStringResourceId; // Achievement Id as per the Id generated in the Google Play Games project
    private int levelOfAchievement; // Custom information for us to keep track of the level of achievement completed
    private long achievementThresholdScore; // The assigned achievement threshold score


    ScoringAchievements(String nameOfAchievement, int achievementStringResourceId, int levelOfAchievement,
                        long achievementThresholdScore) {
        this.nameOfAchievement = nameOfAchievement;
        this.levelOfAchievement = levelOfAchievement;
        this.achievementStringResourceId = achievementStringResourceId;
        this.achievementThresholdScore = achievementThresholdScore;
    }
}
