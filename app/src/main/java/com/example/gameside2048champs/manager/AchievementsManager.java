package com.example.gameside2048champs.manager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.gameside2048champs.enums.ScoringAchievements;
import com.example.gameside2048champs.enums.TileUnlockAchievements;
import com.google.android.gms.games.AchievementsClient;
import com.google.android.gms.games.PlayGames;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AchievementsManager {
    private Context context;
    private SharedPreferences sharedPreferences;
    private AchievementsClient achievementsClient;

    /* A map to keep track of all scoring level achievements
       [Element = String having name of Scoring Level Achievement, Boolean if the achievement has been unlocked or not]
    */
    private Map<ScoringAchievements, Boolean> scoringAchievements;
    /* A map to keep track of all tile unlock achievements
       [Element = String having name of Tile Unlock Achievement, Boolean if the achievement has been unlocked or not]
    */
    private Map<TileUnlockAchievements, Boolean> tileUnlockAchievements;

    AchievementsManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("com.nerdcoredevelopment.game2048champsfinal", Context.MODE_PRIVATE);
        achievementsClient = PlayGames.getAchievementsClient((Activity) this.context);
        scoringAchievements = new HashMap<>();
        for (int index = 0; index < ScoringAchievements.values().length; index++) {
            ScoringAchievements currentScoringAchievement = ScoringAchievements.values()[index];
            boolean isCurrentAchievementUnlocked = sharedPreferences.getBoolean("scoringAchievement" + "_" +
                    context.getString(currentScoringAchievement.getAchievementStringResourceId()), false);
            scoringAchievements.put(currentScoringAchievement, isCurrentAchievementUnlocked);
        }
        tileUnlockAchievements = new HashMap<>();
        for (int index = 0; index < TileUnlockAchievements.values().length; index++) {
            TileUnlockAchievements currentTileUnlockAchievement = TileUnlockAchievements.values()[index];
            boolean isCurrentAchievementUnlocked = sharedPreferences.getBoolean("tileUnlockAchievement" + "_" +
                    context.getString(currentTileUnlockAchievement.getAchievementStringResourceId()), false);
            tileUnlockAchievements.put(currentTileUnlockAchievement, isCurrentAchievementUnlocked);
        }
    }

    public void checkScoringAchievements(long givenScore) {
        for (Map.Entry<ScoringAchievements, Boolean> element: scoringAchievements.entrySet()) {
            ScoringAchievements currentScoringAchievement = element.getKey();
            boolean isAchievementUnlocked = element.getValue();
            long achievementThresholdScore = currentScoringAchievement.getAchievementThresholdScore();
            if (givenScore >= achievementThresholdScore && !isAchievementUnlocked) {
                scoringAchievements.put(currentScoringAchievement, true);
                achievementsClient.unlock(context.getString(currentScoringAchievement.getAchievementStringResourceId()));
                sharedPreferences.edit().putBoolean("scoringAchievement" + "_" +
                        context.getString(currentScoringAchievement.getAchievementStringResourceId()), true).apply();
            }
        }
    }

    public void checkTileUnlockAchievements(long givenTileValue) {
        for (Map.Entry<TileUnlockAchievements, Boolean> element: tileUnlockAchievements.entrySet()) {
            TileUnlockAchievements currentTileUnlockAchievement = element.getKey();
            boolean isAchievementUnlocked = element.getValue();
            long achievementTileValue = currentTileUnlockAchievement.getAchievementTileValue();
            if (givenTileValue == achievementTileValue && !isAchievementUnlocked) {
                tileUnlockAchievements.put(currentTileUnlockAchievement, true);
                achievementsClient.unlock(context.getString(currentTileUnlockAchievement.getAchievementStringResourceId()));
                sharedPreferences.edit().putBoolean("tileUnlockAchievement" + "_" +
                        context.getString(currentTileUnlockAchievement.getAchievementStringResourceId()), true).apply();
            }
        }
    }
}
