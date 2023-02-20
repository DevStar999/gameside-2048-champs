package com.example.gameside2048champs.manager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.gameside2048champs.enums.ScoringAchievements;
import com.example.gameside2048champs.enums.TileUnlockAchievements;
import com.example.gameside2048champs.enums.UndoToolAchievements;
import com.google.android.gms.games.AchievementsClient;
import com.google.android.gms.games.PlayGames;
import com.google.android.gms.games.achievement.Achievement;

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

    /** Attributes related to -> Scoring Level Achievements **/
    /* A map to keep track of all Scoring Level Achievements
       [Element = Scoring Level Achievement enum, Boolean if the achievement has been unlocked or not]
    */
    private Map<ScoringAchievements, Boolean> scoringAchievements;

    /** Attributes related to -> Tile Unlock Achievements **/
    /* A map to keep track of all Tile Unlock Achievements
       [Element = Tile Unlock Achievement enum, Boolean if the achievement has been unlocked or not]
    */
    private Map<TileUnlockAchievements, Boolean> tileUnlockAchievements;

    /** Attributes related to -> 'Undo' Tool Use Achievements **/
    private int undoToolUseCountSubmitted;
    private int undoToolCurrentUseCount;
    /* A map to keep track of all 'Undo' Tool Use Achievements
       [Element = 'Undo' Tool Use Achievement enum, Current State of achievement (STATE_HIDDEN/STATE_REVEALED/STATE_UNLOCKED) ]
    */
    private Map<UndoToolAchievements, Integer> undoToolAchievements;

    AchievementsManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("com.nerdcoredevelopment.game2048champsfinal", Context.MODE_PRIVATE);
        achievementsClient = PlayGames.getAchievementsClient((Activity) this.context);

        // Scoring achievements are initialised as follows
        scoringAchievements = new HashMap<>();
        for (int index = 0; index < ScoringAchievements.values().length; index++) {
            ScoringAchievements currentScoringAchievement = ScoringAchievements.values()[index];
            boolean isCurrentAchievementUnlocked = sharedPreferences.getBoolean("scoringAchievement" + "_" +
                    context.getString(currentScoringAchievement.getAchievementStringResourceId()), false);
            scoringAchievements.put(currentScoringAchievement, isCurrentAchievementUnlocked);
        }

        // Tile Unlock achievements are initialised as follows
        tileUnlockAchievements = new HashMap<>();
        for (int index = 0; index < TileUnlockAchievements.values().length; index++) {
            TileUnlockAchievements currentTileUnlockAchievement = TileUnlockAchievements.values()[index];
            boolean isCurrentAchievementUnlocked = sharedPreferences.getBoolean("tileUnlockAchievement" + "_" +
                    context.getString(currentTileUnlockAchievement.getAchievementStringResourceId()), false);
            tileUnlockAchievements.put(currentTileUnlockAchievement, isCurrentAchievementUnlocked);
        }

        // 'Undo' Tool use achievements are initialised as follows
        undoToolUseCountSubmitted = sharedPreferences.getInt("undoToolUseCountSubmitted", 0);
        undoToolCurrentUseCount = sharedPreferences.getInt("undoToolCurrentUseCount", undoToolUseCountSubmitted);
        undoToolAchievements = new HashMap<>();
        for (int index = 0; index < UndoToolAchievements.values().length; index++) {
            UndoToolAchievements currentUndoToolAchievement = UndoToolAchievements.values()[index];
            int currentStateOfAchievement = sharedPreferences.getInt("undoToolAchievement" + "_" +
                    context.getString(currentUndoToolAchievement.getAchievementStringResourceId()),
                    currentUndoToolAchievement.getInitialAchievementState());
            undoToolAchievements.put(currentUndoToolAchievement, currentStateOfAchievement);
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

    // Here, we return 'true' if the score needs to be submitted to the 'Undo' tool use leaderboard & 'false' otherwise
    public boolean incrementUndoToolUseCount() {
        boolean isScoreToBeSubmittedToLeaderboard = false;
        undoToolCurrentUseCount += 25;
        if (undoToolCurrentUseCount >= undoToolUseCountSubmitted + 10) { // Here, we need to update GPGS data
            undoToolUseCountSubmitted = undoToolCurrentUseCount;

            // Here, we setSteps() for appropriate achievement
            if (undoToolCurrentUseCount >= 0 && undoToolCurrentUseCount <= 100) {
                UndoToolAchievements undoToolAchievementInRange = UndoToolAchievements.UNDO_TOOL_ACHIEVEMENT_LEVEL_1;
                achievementsClient.setSteps(context.getString(undoToolAchievementInRange.getAchievementStringResourceId()),
                        undoToolCurrentUseCount);
                if (undoToolCurrentUseCount == 100) {
                    sharedPreferences.edit().putInt("undoToolAchievement" + "_" + context.getString(undoToolAchievementInRange
                                    .getAchievementStringResourceId()), Achievement.STATE_UNLOCKED).apply();

                    UndoToolAchievements undoToolAchievementNextUp = UndoToolAchievements.UNDO_TOOL_ACHIEVEMENT_LEVEL_2;
                    achievementsClient.reveal(context.getString(undoToolAchievementNextUp.getAchievementStringResourceId()));
                    sharedPreferences.edit().putInt("undoToolAchievement" + "_" + context.getString(undoToolAchievementNextUp
                            .getAchievementStringResourceId()), Achievement.STATE_REVEALED).apply();

                    achievementsClient.setSteps(context.getString(undoToolAchievementNextUp.getAchievementStringResourceId()),
                            undoToolCurrentUseCount);
                }
            } else if (undoToolCurrentUseCount > 100 && undoToolCurrentUseCount <= 250) {
                UndoToolAchievements undoToolAchievementInRange = UndoToolAchievements.UNDO_TOOL_ACHIEVEMENT_LEVEL_2;
                achievementsClient.setSteps(context.getString(undoToolAchievementInRange.getAchievementStringResourceId()),
                        undoToolCurrentUseCount);
                if (undoToolCurrentUseCount == 250) {
                    sharedPreferences.edit().putInt("undoToolAchievement" + "_" + context.getString(undoToolAchievementInRange
                            .getAchievementStringResourceId()), Achievement.STATE_UNLOCKED).apply();

                    UndoToolAchievements undoToolAchievementNextUp = UndoToolAchievements.UNDO_TOOL_ACHIEVEMENT_LEVEL_3;
                    achievementsClient.reveal(context.getString(undoToolAchievementNextUp.getAchievementStringResourceId()));
                    sharedPreferences.edit().putInt("undoToolAchievement" + "_" + context.getString(undoToolAchievementNextUp
                            .getAchievementStringResourceId()), Achievement.STATE_REVEALED).apply();

                    achievementsClient.setSteps(context.getString(undoToolAchievementNextUp.getAchievementStringResourceId()),
                            undoToolCurrentUseCount);
                }
            } else if (undoToolCurrentUseCount > 250 && undoToolCurrentUseCount <= 500) {
                UndoToolAchievements undoToolAchievementInRange = UndoToolAchievements.UNDO_TOOL_ACHIEVEMENT_LEVEL_3;
                achievementsClient.setSteps(context.getString(undoToolAchievementInRange.getAchievementStringResourceId()),
                        undoToolCurrentUseCount);
                if (undoToolCurrentUseCount == 500) {
                    sharedPreferences.edit().putInt("undoToolAchievement" + "_" + context.getString(undoToolAchievementInRange
                            .getAchievementStringResourceId()), Achievement.STATE_UNLOCKED).apply();
                }
            }

            isScoreToBeSubmittedToLeaderboard = true;
        }

        sharedPreferences.edit().putInt("undoToolCurrentUseCount", undoToolCurrentUseCount).apply();
        sharedPreferences.edit().putInt("undoToolUseCountSubmitted", undoToolUseCountSubmitted).apply();
        return isScoreToBeSubmittedToLeaderboard;
    }
}
