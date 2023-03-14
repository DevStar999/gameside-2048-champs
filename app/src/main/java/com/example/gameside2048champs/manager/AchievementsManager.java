package com.example.gameside2048champs.manager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.gameside2048champs.enums.ChangeValueToolAchievements;
import com.example.gameside2048champs.enums.DestroyAreaToolAchievements;
import com.example.gameside2048champs.enums.EarlyOutAchievementType;
import com.example.gameside2048champs.enums.EarlyOutAchievements;
import com.example.gameside2048champs.enums.EliminateValueToolAchievements;
import com.example.gameside2048champs.enums.ReviveGameToolAchievements;
import com.example.gameside2048champs.enums.ScoringAchievements;
import com.example.gameside2048champs.enums.SmashTileToolAchievements;
import com.example.gameside2048champs.enums.SwapTilesToolAchievements;
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


    /** Attributes related to -> Early Out Achievements **/
    /* A map to keep track of all Early Out Achievements
       [Element = Early Out Achievement enum, Boolean if the achievement has been unlocked or not]
    */
    private Map<EarlyOutAchievements, Boolean> earlyOutAchievements;

    /** Attributes related to -> 'Undo' Tool Use Achievements **/
    private int undoToolUseCountSubmitted;
    private int undoToolCurrentUseCount;
    /* A map to keep track of all 'Undo' Tool Use Achievements
       [Element = 'Undo' Tool Use Achievement enum, Current State of achievement (STATE_HIDDEN/STATE_REVEALED/STATE_UNLOCKED) ]
    */
    private Map<UndoToolAchievements, Integer> undoToolAchievements;

    /** Attributes related to -> 'Smash Tile' Tool Use Achievements **/
    private int smashTileToolUseCountSubmitted;
    private int smashTileToolCurrentUseCount;
    /* A map to keep track of all 'Smash Tile' Tool Use Achievements
       [Element = 'Smash Tile' Tool Use Achievement enum, Current State of achievement
       (STATE_HIDDEN/STATE_REVEALED/STATE_UNLOCKED) ]
    */
    private Map<SmashTileToolAchievements, Integer> smashTileToolAchievements;

    /** Attributes related to -> 'Swap Tiles' Tool Use Achievements **/
    private int swapTilesToolUseCountSubmitted;
    private int swapTilesToolCurrentUseCount;
    /* A map to keep track of all 'Swap Tiles' Tool Use Achievements
       [Element = 'Swap Tiles' Tool Use Achievement enum, Current State of achievement
       (STATE_HIDDEN/STATE_REVEALED/STATE_UNLOCKED) ]
    */
    private Map<SwapTilesToolAchievements, Integer> swapTilesToolAchievements;

    /** Attributes related to -> 'Change Value' Tool Use Achievements **/
    private int changeValueToolUseCountSubmitted;
    private int changeValueToolCurrentUseCount;
    /* A map to keep track of all 'Change Value' Tool Use Achievements
       [Element = 'Change Value' Tool Use Achievement enum, Current State of achievement
       (STATE_HIDDEN/STATE_REVEALED/STATE_UNLOCKED) ]
    */
    private Map<ChangeValueToolAchievements, Integer> changeValueToolAchievements;

    /** Attributes related to -> 'Eliminate Value' Tool Use Achievements **/
    private int eliminateValueToolUseCountSubmitted;
    private int eliminateValueToolCurrentUseCount;
    /* A map to keep track of all 'Eliminate Value' Tool Use Achievements
       [Element = 'Eliminate Value' Tool Use Achievement enum, Current State of achievement
       (STATE_HIDDEN/STATE_REVEALED/STATE_UNLOCKED) ]
    */
    private Map<EliminateValueToolAchievements, Integer> eliminateValueToolAchievements;

    /** Attributes related to -> 'Destroy Area' Tool Use Achievements **/
    private int destroyAreaToolUseCountSubmitted;
    private int destroyAreaToolCurrentUseCount;
    /* A map to keep track of all 'Destroy Area' Tool Use Achievements
       [Element = 'Destroy Area' Tool Use Achievement enum, Current State of achievement
       (STATE_HIDDEN/STATE_REVEALED/STATE_UNLOCKED) ]
    */
    private Map<DestroyAreaToolAchievements, Integer> destroyAreaToolAchievements;

    /** Attributes related to -> 'Revive Game' Tool Use Achievements **/
    private int reviveGameToolUseCountSubmitted;
    private int reviveGameToolCurrentUseCount;
    /* A map to keep track of all 'Revive Game' Tool Use Achievements
       [Element = 'Revive Game' Tool Use Achievement enum, Current State of achievement
       (STATE_HIDDEN/STATE_REVEALED/STATE_UNLOCKED) ]
    */
    private Map<ReviveGameToolAchievements, Integer> reviveGameToolAchievements;

    public AchievementsManager(Context context) {
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

        // Early out achievements are initialised as follows
        earlyOutAchievements = new HashMap<>();
        for (int index = 0; index < EarlyOutAchievements.values().length; index++) {
            EarlyOutAchievements currentEarlyOutAchievement = EarlyOutAchievements.values()[index];
            boolean isCurrentAchievementUnlocked = sharedPreferences.getBoolean("earlyOutAchievement" + "_" +
                    context.getString(currentEarlyOutAchievement.getAchievementStringResourceId()), false);
            earlyOutAchievements.put(currentEarlyOutAchievement, isCurrentAchievementUnlocked);
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

        // 'Smash Tile' Tool use achievements are initialised as follows
        smashTileToolUseCountSubmitted = sharedPreferences.getInt("smashTileToolUseCountSubmitted", 0);
        smashTileToolCurrentUseCount = sharedPreferences.getInt("smashTileToolCurrentUseCount", smashTileToolUseCountSubmitted);
        smashTileToolAchievements = new HashMap<>();
        for (int index = 0; index < SmashTileToolAchievements.values().length; index++) {
            SmashTileToolAchievements currentSmashTileToolAchievement = SmashTileToolAchievements.values()[index];
            int currentStateOfAchievement = sharedPreferences.getInt("smashTileToolAchievement" + "_" +
                    context.getString(currentSmashTileToolAchievement.getAchievementStringResourceId()),
                    currentSmashTileToolAchievement.getInitialAchievementState());
            smashTileToolAchievements.put(currentSmashTileToolAchievement, currentStateOfAchievement);
        }

        // 'Swap Tiles' Tool use achievements are initialised as follows
        swapTilesToolUseCountSubmitted = sharedPreferences.getInt("swapTilesToolUseCountSubmitted", 0);
        swapTilesToolCurrentUseCount = sharedPreferences.getInt("swapTilesToolCurrentUseCount", swapTilesToolUseCountSubmitted);
        swapTilesToolAchievements = new HashMap<>();
        for (int index = 0; index < SwapTilesToolAchievements.values().length; index++) {
            SwapTilesToolAchievements currentSwapTilesToolAchievement = SwapTilesToolAchievements.values()[index];
            int currentStateOfAchievement = sharedPreferences.getInt("swapTilesToolAchievement" + "_" +
                    context.getString(currentSwapTilesToolAchievement.getAchievementStringResourceId()),
                    currentSwapTilesToolAchievement.getInitialAchievementState());
            swapTilesToolAchievements.put(currentSwapTilesToolAchievement, currentStateOfAchievement);
        }

        // 'Change Value' Tool use achievements are initialised as follows
        changeValueToolUseCountSubmitted = sharedPreferences.getInt("changeValueToolUseCountSubmitted", 0);
        changeValueToolCurrentUseCount = sharedPreferences.getInt("changeValueToolCurrentUseCount", changeValueToolUseCountSubmitted);
        changeValueToolAchievements = new HashMap<>();
        for (int index = 0; index < ChangeValueToolAchievements.values().length; index++) {
            ChangeValueToolAchievements currentChangeValueToolAchievement = ChangeValueToolAchievements.values()[index];
            int currentStateOfAchievement = sharedPreferences.getInt("changeValueToolAchievement" + "_" +
                    context.getString(currentChangeValueToolAchievement.getAchievementStringResourceId()),
                    currentChangeValueToolAchievement.getInitialAchievementState());
            changeValueToolAchievements.put(currentChangeValueToolAchievement, currentStateOfAchievement);
        }

        // 'Eliminate Value' Tool use achievements are initialised as follows
        eliminateValueToolUseCountSubmitted = sharedPreferences.getInt("eliminateValueToolUseCountSubmitted", 0);
        eliminateValueToolCurrentUseCount = sharedPreferences.getInt("eliminateValueToolCurrentUseCount", eliminateValueToolUseCountSubmitted);
        eliminateValueToolAchievements = new HashMap<>();
        for (int index = 0; index < EliminateValueToolAchievements.values().length; index++) {
            EliminateValueToolAchievements currentEliminateValueToolAchievement = EliminateValueToolAchievements.values()[index];
            int currentStateOfAchievement = sharedPreferences.getInt("eliminateValueToolAchievement" + "_" +
                    context.getString(currentEliminateValueToolAchievement.getAchievementStringResourceId()),
                    currentEliminateValueToolAchievement.getInitialAchievementState());
            eliminateValueToolAchievements.put(currentEliminateValueToolAchievement, currentStateOfAchievement);
        }

        // 'Destroy Area' Tool use achievements are initialised as follows
        destroyAreaToolUseCountSubmitted = sharedPreferences.getInt("destroyAreaToolUseCountSubmitted", 0);
        destroyAreaToolCurrentUseCount = sharedPreferences.getInt("destroyAreaToolCurrentUseCount", destroyAreaToolUseCountSubmitted);
        destroyAreaToolAchievements = new HashMap<>();
        for (int index = 0; index < DestroyAreaToolAchievements.values().length; index++) {
            DestroyAreaToolAchievements currentDestroyAreaToolAchievement = DestroyAreaToolAchievements.values()[index];
            int currentStateOfAchievement = sharedPreferences.getInt("destroyAreaToolAchievement" + "_" +
                    context.getString(currentDestroyAreaToolAchievement.getAchievementStringResourceId()),
                    currentDestroyAreaToolAchievement.getInitialAchievementState());
            destroyAreaToolAchievements.put(currentDestroyAreaToolAchievement, currentStateOfAchievement);
        }

        // 'Revive Game' Tool use achievements are initialised as follows
        reviveGameToolUseCountSubmitted = sharedPreferences.getInt("reviveGameToolUseCountSubmitted", 0);
        reviveGameToolCurrentUseCount = sharedPreferences.getInt("reviveGameToolCurrentUseCount", reviveGameToolUseCountSubmitted);
        reviveGameToolAchievements = new HashMap<>();
        for (int index = 0; index < ReviveGameToolAchievements.values().length; index++) {
            ReviveGameToolAchievements currentReviveGameToolAchievement = ReviveGameToolAchievements.values()[index];
            int currentStateOfAchievement = sharedPreferences.getInt("reviveGameToolAchievement" + "_" +
                            context.getString(currentReviveGameToolAchievement.getAchievementStringResourceId()),
                    currentReviveGameToolAchievement.getInitialAchievementState());
            reviveGameToolAchievements.put(currentReviveGameToolAchievement, currentStateOfAchievement);
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

    public void checkEarlyOutAchievements(long givenHighestTileValue, long givenCurrentScore) {
        for (Map.Entry<EarlyOutAchievements, Boolean> element: earlyOutAchievements.entrySet()) {
            EarlyOutAchievements currentEarlyOutAchievement = element.getKey();
            boolean isAchievementUnlocked = element.getValue();
            if (currentEarlyOutAchievement.getEarlyOutAchievementType() == EarlyOutAchievementType.BEFORE_SCORE) {
                long achievementScore = currentEarlyOutAchievement.getAchievementThreshold();
                if (givenCurrentScore < achievementScore && !isAchievementUnlocked) {
                    earlyOutAchievements.put(currentEarlyOutAchievement, true);
                    achievementsClient.unlock(context.getString(currentEarlyOutAchievement.getAchievementStringResourceId()));
                    sharedPreferences.edit().putBoolean("earlyOutAchievement" + "_" +
                            context.getString(currentEarlyOutAchievement.getAchievementStringResourceId()), true).apply();
                }
            } else if (currentEarlyOutAchievement.getEarlyOutAchievementType() == EarlyOutAchievementType.BEFORE_TILE) {
                long achievementTileValue = currentEarlyOutAchievement.getAchievementThreshold();
                if (givenHighestTileValue < achievementTileValue && !isAchievementUnlocked) {
                    earlyOutAchievements.put(currentEarlyOutAchievement, true);
                    achievementsClient.unlock(context.getString(currentEarlyOutAchievement.getAchievementStringResourceId()));
                    sharedPreferences.edit().putBoolean("earlyOutAchievement" + "_" +
                            context.getString(currentEarlyOutAchievement.getAchievementStringResourceId()), true).apply();
                }
            }
        }
    }

    // Here, we return 'true' if the score needs to be submitted to the 'Undo' tool use leaderboard & 'false' otherwise
    public boolean incrementUndoToolUseCount() {
        boolean isScoreToBeSubmittedToLeaderboard = false;
        undoToolCurrentUseCount += 1;
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

    // Here, we return 'true' if the score needs to be submitted to the 'Smash Tile' tool use leaderboard & 'false' otherwise
    public boolean incrementSmashTileToolUseCount() {
        boolean isScoreToBeSubmittedToLeaderboard = false;
        smashTileToolCurrentUseCount += 1;
        if (smashTileToolCurrentUseCount >= smashTileToolUseCountSubmitted + 10) { // Here, we need to update GPGS data
            smashTileToolUseCountSubmitted = smashTileToolCurrentUseCount;

            // Here, we setSteps() for appropriate achievement
            if (smashTileToolCurrentUseCount >= 0 && smashTileToolCurrentUseCount <= 100) {
                SmashTileToolAchievements smashTileToolAchievementInRange = SmashTileToolAchievements.SMASH_TILE_TOOL_ACHIEVEMENT_LEVEL_1;
                achievementsClient.setSteps(context.getString(smashTileToolAchievementInRange.getAchievementStringResourceId()),
                        smashTileToolCurrentUseCount);
                if (smashTileToolCurrentUseCount == 100) {
                    sharedPreferences.edit().putInt("smashTileToolAchievement" + "_" + context.getString(smashTileToolAchievementInRange
                            .getAchievementStringResourceId()), Achievement.STATE_UNLOCKED).apply();

                    SmashTileToolAchievements smashTileToolAchievementNextUp = SmashTileToolAchievements.SMASH_TILE_TOOL_ACHIEVEMENT_LEVEL_2;
                    achievementsClient.reveal(context.getString(smashTileToolAchievementNextUp.getAchievementStringResourceId()));
                    sharedPreferences.edit().putInt("smashTileToolAchievement" + "_" + context.getString(smashTileToolAchievementNextUp
                            .getAchievementStringResourceId()), Achievement.STATE_REVEALED).apply();

                    achievementsClient.setSteps(context.getString(smashTileToolAchievementNextUp.getAchievementStringResourceId()),
                            smashTileToolCurrentUseCount);
                }
            } else if (smashTileToolCurrentUseCount > 100 && smashTileToolCurrentUseCount <= 250) {
                SmashTileToolAchievements smashTileToolAchievementInRange = SmashTileToolAchievements.SMASH_TILE_TOOL_ACHIEVEMENT_LEVEL_2;
                achievementsClient.setSteps(context.getString(smashTileToolAchievementInRange.getAchievementStringResourceId()),
                        smashTileToolCurrentUseCount);
                if (smashTileToolCurrentUseCount == 250) {
                    sharedPreferences.edit().putInt("smashTileToolAchievement" + "_" + context.getString(smashTileToolAchievementInRange
                            .getAchievementStringResourceId()), Achievement.STATE_UNLOCKED).apply();

                    SmashTileToolAchievements smashTileToolAchievementNextUp = SmashTileToolAchievements.SMASH_TILE_TOOL_ACHIEVEMENT_LEVEL_3;
                    achievementsClient.reveal(context.getString(smashTileToolAchievementNextUp.getAchievementStringResourceId()));
                    sharedPreferences.edit().putInt("smashTileToolAchievement" + "_" + context.getString(smashTileToolAchievementNextUp
                            .getAchievementStringResourceId()), Achievement.STATE_REVEALED).apply();

                    achievementsClient.setSteps(context.getString(smashTileToolAchievementNextUp.getAchievementStringResourceId()),
                            smashTileToolCurrentUseCount);
                }
            } else if (smashTileToolCurrentUseCount > 250 && smashTileToolCurrentUseCount <= 500) {
                SmashTileToolAchievements smashTileToolAchievementInRange = SmashTileToolAchievements.SMASH_TILE_TOOL_ACHIEVEMENT_LEVEL_3;
                achievementsClient.setSteps(context.getString(smashTileToolAchievementInRange.getAchievementStringResourceId()),
                        smashTileToolCurrentUseCount);
                if (smashTileToolCurrentUseCount == 500) {
                    sharedPreferences.edit().putInt("smashTileToolAchievement" + "_" + context.getString(smashTileToolAchievementInRange
                            .getAchievementStringResourceId()), Achievement.STATE_UNLOCKED).apply();
                }
            }

            isScoreToBeSubmittedToLeaderboard = true;
        }

        sharedPreferences.edit().putInt("smashTileToolCurrentUseCount", smashTileToolCurrentUseCount).apply();
        sharedPreferences.edit().putInt("smashTileToolUseCountSubmitted", smashTileToolUseCountSubmitted).apply();
        return isScoreToBeSubmittedToLeaderboard;
    }

    // Here, we return 'true' if the score needs to be submitted to the 'Swap Tiles' tool use leaderboard & 'false' otherwise
    public boolean incrementSwapTilesToolUseCount() {
        boolean isScoreToBeSubmittedToLeaderboard = false;
        swapTilesToolCurrentUseCount += 1;
        if (swapTilesToolCurrentUseCount >= swapTilesToolUseCountSubmitted + 10) { // Here, we need to update GPGS data
            swapTilesToolUseCountSubmitted = swapTilesToolCurrentUseCount;

            // Here, we setSteps() for appropriate achievement
            if (swapTilesToolCurrentUseCount >= 0 && swapTilesToolCurrentUseCount <= 100) {
                SwapTilesToolAchievements swapTilesToolAchievementInRange = SwapTilesToolAchievements.SWAP_TILES_TOOL_ACHIEVEMENT_LEVEL_1;
                achievementsClient.setSteps(context.getString(swapTilesToolAchievementInRange.getAchievementStringResourceId()),
                        swapTilesToolCurrentUseCount);
                if (swapTilesToolCurrentUseCount == 100) {
                    sharedPreferences.edit().putInt("swapTilesToolAchievement" + "_" + context.getString(swapTilesToolAchievementInRange
                            .getAchievementStringResourceId()), Achievement.STATE_UNLOCKED).apply();

                    SwapTilesToolAchievements swapTilesToolAchievementNextUp = SwapTilesToolAchievements.SWAP_TILES_TOOL_ACHIEVEMENT_LEVEL_2;
                    achievementsClient.reveal(context.getString(swapTilesToolAchievementNextUp.getAchievementStringResourceId()));
                    sharedPreferences.edit().putInt("swapTilesToolAchievement" + "_" + context.getString(swapTilesToolAchievementNextUp
                            .getAchievementStringResourceId()), Achievement.STATE_REVEALED).apply();

                    achievementsClient.setSteps(context.getString(swapTilesToolAchievementNextUp.getAchievementStringResourceId()),
                            swapTilesToolCurrentUseCount);
                }
            } else if (swapTilesToolCurrentUseCount > 100 && swapTilesToolCurrentUseCount <= 250) {
                SwapTilesToolAchievements swapTilesToolAchievementInRange = SwapTilesToolAchievements.SWAP_TILES_TOOL_ACHIEVEMENT_LEVEL_2;
                achievementsClient.setSteps(context.getString(swapTilesToolAchievementInRange.getAchievementStringResourceId()),
                        swapTilesToolCurrentUseCount);
                if (swapTilesToolCurrentUseCount == 250) {
                    sharedPreferences.edit().putInt("swapTilesToolAchievement" + "_" + context.getString(swapTilesToolAchievementInRange
                            .getAchievementStringResourceId()), Achievement.STATE_UNLOCKED).apply();

                    SwapTilesToolAchievements swapTilesToolAchievementNextUp = SwapTilesToolAchievements.SWAP_TILES_TOOL_ACHIEVEMENT_LEVEL_3;
                    achievementsClient.reveal(context.getString(swapTilesToolAchievementNextUp.getAchievementStringResourceId()));
                    sharedPreferences.edit().putInt("swapTilesToolAchievement" + "_" + context.getString(swapTilesToolAchievementNextUp
                            .getAchievementStringResourceId()), Achievement.STATE_REVEALED).apply();

                    achievementsClient.setSteps(context.getString(swapTilesToolAchievementNextUp.getAchievementStringResourceId()),
                            swapTilesToolCurrentUseCount);
                }
            } else if (swapTilesToolCurrentUseCount > 250 && swapTilesToolCurrentUseCount <= 500) {
                SwapTilesToolAchievements swapTilesToolAchievementInRange = SwapTilesToolAchievements.SWAP_TILES_TOOL_ACHIEVEMENT_LEVEL_3;
                achievementsClient.setSteps(context.getString(swapTilesToolAchievementInRange.getAchievementStringResourceId()),
                        swapTilesToolCurrentUseCount);
                if (swapTilesToolCurrentUseCount == 500) {
                    sharedPreferences.edit().putInt("swapTilesToolAchievement" + "_" + context.getString(swapTilesToolAchievementInRange
                            .getAchievementStringResourceId()), Achievement.STATE_UNLOCKED).apply();
                }
            }

            isScoreToBeSubmittedToLeaderboard = true;
        }

        sharedPreferences.edit().putInt("swapTilesToolCurrentUseCount", swapTilesToolCurrentUseCount).apply();
        sharedPreferences.edit().putInt("swapTilesToolUseCountSubmitted", swapTilesToolUseCountSubmitted).apply();
        return isScoreToBeSubmittedToLeaderboard;
    }

    // Here, we return 'true' if the score needs to be submitted to the 'Change Value' tool use leaderboard & 'false' otherwise
    public boolean incrementChangeValueToolUseCount() {
        boolean isScoreToBeSubmittedToLeaderboard = false;
        changeValueToolCurrentUseCount += 1;
        if (changeValueToolCurrentUseCount >= changeValueToolUseCountSubmitted + 10) { // Here, we need to update GPGS data
            changeValueToolUseCountSubmitted = changeValueToolCurrentUseCount;

            // Here, we setSteps() for appropriate achievement
            if (changeValueToolCurrentUseCount >= 0 && changeValueToolCurrentUseCount <= 100) {
                ChangeValueToolAchievements changeValueToolAchievementInRange = ChangeValueToolAchievements.CHANGE_VALUE_TOOL_ACHIEVEMENT_LEVEL_1;
                achievementsClient.setSteps(context.getString(changeValueToolAchievementInRange.getAchievementStringResourceId()),
                        changeValueToolCurrentUseCount);
                if (changeValueToolCurrentUseCount == 100) {
                    sharedPreferences.edit().putInt("changeValueToolAchievement" + "_" + context.getString(changeValueToolAchievementInRange
                            .getAchievementStringResourceId()), Achievement.STATE_UNLOCKED).apply();

                    ChangeValueToolAchievements changeValueToolAchievementNextUp = ChangeValueToolAchievements.CHANGE_VALUE_TOOL_ACHIEVEMENT_LEVEL_2;
                    achievementsClient.reveal(context.getString(changeValueToolAchievementNextUp.getAchievementStringResourceId()));
                    sharedPreferences.edit().putInt("changeValueToolAchievement" + "_" + context.getString(changeValueToolAchievementNextUp
                            .getAchievementStringResourceId()), Achievement.STATE_REVEALED).apply();

                    achievementsClient.setSteps(context.getString(changeValueToolAchievementNextUp.getAchievementStringResourceId()),
                            changeValueToolCurrentUseCount);
                }
            } else if (changeValueToolCurrentUseCount > 100 && changeValueToolCurrentUseCount <= 250) {
                ChangeValueToolAchievements changeValueToolAchievementInRange = ChangeValueToolAchievements.CHANGE_VALUE_TOOL_ACHIEVEMENT_LEVEL_2;
                achievementsClient.setSteps(context.getString(changeValueToolAchievementInRange.getAchievementStringResourceId()),
                        changeValueToolCurrentUseCount);
                if (changeValueToolCurrentUseCount == 250) {
                    sharedPreferences.edit().putInt("changeValueToolAchievement" + "_" + context.getString(changeValueToolAchievementInRange
                            .getAchievementStringResourceId()), Achievement.STATE_UNLOCKED).apply();

                    ChangeValueToolAchievements changeValueToolAchievementNextUp = ChangeValueToolAchievements.CHANGE_VALUE_TOOL_ACHIEVEMENT_LEVEL_3;
                    achievementsClient.reveal(context.getString(changeValueToolAchievementNextUp.getAchievementStringResourceId()));
                    sharedPreferences.edit().putInt("changeValueToolAchievement" + "_" + context.getString(changeValueToolAchievementNextUp
                            .getAchievementStringResourceId()), Achievement.STATE_REVEALED).apply();

                    achievementsClient.setSteps(context.getString(changeValueToolAchievementNextUp.getAchievementStringResourceId()),
                            changeValueToolCurrentUseCount);
                }
            } else if (changeValueToolCurrentUseCount > 250 && changeValueToolCurrentUseCount <= 500) {
                ChangeValueToolAchievements changeValueToolAchievementInRange = ChangeValueToolAchievements.CHANGE_VALUE_TOOL_ACHIEVEMENT_LEVEL_3;
                achievementsClient.setSteps(context.getString(changeValueToolAchievementInRange.getAchievementStringResourceId()),
                        changeValueToolCurrentUseCount);
                if (changeValueToolCurrentUseCount == 500) {
                    sharedPreferences.edit().putInt("changeValueToolAchievement" + "_" + context.getString(changeValueToolAchievementInRange
                            .getAchievementStringResourceId()), Achievement.STATE_UNLOCKED).apply();
                }
            }

            isScoreToBeSubmittedToLeaderboard = true;
        }

        sharedPreferences.edit().putInt("changeValueToolCurrentUseCount", changeValueToolCurrentUseCount).apply();
        sharedPreferences.edit().putInt("changeValueToolUseCountSubmitted", changeValueToolUseCountSubmitted).apply();
        return isScoreToBeSubmittedToLeaderboard;
    }

    // Here, we return 'true' if the score needs to be submitted to the 'Eliminate Value' tool use leaderboard & 'false' otherwise
    public boolean incrementEliminateValueToolUseCount() {
        boolean isScoreToBeSubmittedToLeaderboard = false;
        eliminateValueToolCurrentUseCount += 1;
        if (eliminateValueToolCurrentUseCount >= eliminateValueToolUseCountSubmitted + 10) { // Here, we need to update GPGS data
            eliminateValueToolUseCountSubmitted = eliminateValueToolCurrentUseCount;

            // Here, we setSteps() for appropriate achievement
            if (eliminateValueToolCurrentUseCount >= 0 && eliminateValueToolCurrentUseCount <= 100) {
                EliminateValueToolAchievements eliminateValueToolAchievementInRange = EliminateValueToolAchievements.ELIMINATE_VALUE_TOOL_ACHIEVEMENT_LEVEL_1;
                achievementsClient.setSteps(context.getString(eliminateValueToolAchievementInRange.getAchievementStringResourceId()),
                        eliminateValueToolCurrentUseCount);
                if (eliminateValueToolCurrentUseCount == 100) {
                    sharedPreferences.edit().putInt("eliminateValueToolAchievement" + "_" + context.getString(eliminateValueToolAchievementInRange
                            .getAchievementStringResourceId()), Achievement.STATE_UNLOCKED).apply();

                    EliminateValueToolAchievements eliminateValueToolAchievementNextUp = EliminateValueToolAchievements.ELIMINATE_VALUE_TOOL_ACHIEVEMENT_LEVEL_2;
                    achievementsClient.reveal(context.getString(eliminateValueToolAchievementNextUp.getAchievementStringResourceId()));
                    sharedPreferences.edit().putInt("eliminateValueToolAchievement" + "_" + context.getString(eliminateValueToolAchievementNextUp
                            .getAchievementStringResourceId()), Achievement.STATE_REVEALED).apply();

                    achievementsClient.setSteps(context.getString(eliminateValueToolAchievementNextUp.getAchievementStringResourceId()),
                            eliminateValueToolCurrentUseCount);
                }
            } else if (eliminateValueToolCurrentUseCount > 100 && eliminateValueToolCurrentUseCount <= 250) {
                EliminateValueToolAchievements eliminateValueToolAchievementInRange = EliminateValueToolAchievements.ELIMINATE_VALUE_TOOL_ACHIEVEMENT_LEVEL_2;
                achievementsClient.setSteps(context.getString(eliminateValueToolAchievementInRange.getAchievementStringResourceId()),
                        eliminateValueToolCurrentUseCount);
                if (eliminateValueToolCurrentUseCount == 250) {
                    sharedPreferences.edit().putInt("eliminateValueToolAchievement" + "_" + context.getString(eliminateValueToolAchievementInRange
                            .getAchievementStringResourceId()), Achievement.STATE_UNLOCKED).apply();

                    EliminateValueToolAchievements eliminateValueToolAchievementNextUp = EliminateValueToolAchievements.ELIMINATE_VALUE_TOOL_ACHIEVEMENT_LEVEL_3;
                    achievementsClient.reveal(context.getString(eliminateValueToolAchievementNextUp.getAchievementStringResourceId()));
                    sharedPreferences.edit().putInt("eliminateValueToolAchievement" + "_" + context.getString(eliminateValueToolAchievementNextUp
                            .getAchievementStringResourceId()), Achievement.STATE_REVEALED).apply();

                    achievementsClient.setSteps(context.getString(eliminateValueToolAchievementNextUp.getAchievementStringResourceId()),
                            eliminateValueToolCurrentUseCount);
                }
            } else if (eliminateValueToolCurrentUseCount > 250 && eliminateValueToolCurrentUseCount <= 500) {
                EliminateValueToolAchievements eliminateValueToolAchievementInRange = EliminateValueToolAchievements.ELIMINATE_VALUE_TOOL_ACHIEVEMENT_LEVEL_3;
                achievementsClient.setSteps(context.getString(eliminateValueToolAchievementInRange.getAchievementStringResourceId()),
                        eliminateValueToolCurrentUseCount);
                if (eliminateValueToolCurrentUseCount == 500) {
                    sharedPreferences.edit().putInt("eliminateValueToolAchievement" + "_" + context.getString(eliminateValueToolAchievementInRange
                            .getAchievementStringResourceId()), Achievement.STATE_UNLOCKED).apply();
                }
            }

            isScoreToBeSubmittedToLeaderboard = true;
        }

        sharedPreferences.edit().putInt("eliminateValueToolCurrentUseCount", eliminateValueToolCurrentUseCount).apply();
        sharedPreferences.edit().putInt("eliminateValueToolUseCountSubmitted", eliminateValueToolUseCountSubmitted).apply();
        return isScoreToBeSubmittedToLeaderboard;
    }

    // Here, we return 'true' if the score needs to be submitted to the 'Destroy Area' tool use leaderboard & 'false' otherwise
    public boolean incrementDestroyAreaToolUseCount() {
        boolean isScoreToBeSubmittedToLeaderboard = false;
        destroyAreaToolCurrentUseCount += 1;
        if (destroyAreaToolCurrentUseCount >= destroyAreaToolUseCountSubmitted + 10) { // Here, we need to update GPGS data
            destroyAreaToolUseCountSubmitted = destroyAreaToolCurrentUseCount;

            // Here, we setSteps() for appropriate achievement
            if (destroyAreaToolCurrentUseCount >= 0 && destroyAreaToolCurrentUseCount <= 100) {
                DestroyAreaToolAchievements destroyAreaToolAchievementInRange = DestroyAreaToolAchievements.DESTROY_AREA_TOOL_ACHIEVEMENT_LEVEL_1;
                achievementsClient.setSteps(context.getString(destroyAreaToolAchievementInRange.getAchievementStringResourceId()),
                        destroyAreaToolCurrentUseCount);
                if (destroyAreaToolCurrentUseCount == 100) {
                    sharedPreferences.edit().putInt("destroyAreaToolAchievement" + "_" + context.getString(destroyAreaToolAchievementInRange
                            .getAchievementStringResourceId()), Achievement.STATE_UNLOCKED).apply();

                    DestroyAreaToolAchievements destroyAreaToolAchievementNextUp = DestroyAreaToolAchievements.DESTROY_AREA_TOOL_ACHIEVEMENT_LEVEL_2;
                    achievementsClient.reveal(context.getString(destroyAreaToolAchievementNextUp.getAchievementStringResourceId()));
                    sharedPreferences.edit().putInt("destroyAreaToolAchievement" + "_" + context.getString(destroyAreaToolAchievementNextUp
                            .getAchievementStringResourceId()), Achievement.STATE_REVEALED).apply();

                    achievementsClient.setSteps(context.getString(destroyAreaToolAchievementNextUp.getAchievementStringResourceId()),
                            destroyAreaToolCurrentUseCount);
                }
            } else if (destroyAreaToolCurrentUseCount > 100 && destroyAreaToolCurrentUseCount <= 250) {
                DestroyAreaToolAchievements destroyAreaToolAchievementInRange = DestroyAreaToolAchievements.DESTROY_AREA_TOOL_ACHIEVEMENT_LEVEL_2;
                achievementsClient.setSteps(context.getString(destroyAreaToolAchievementInRange.getAchievementStringResourceId()),
                        destroyAreaToolCurrentUseCount);
                if (destroyAreaToolCurrentUseCount == 250) {
                    sharedPreferences.edit().putInt("destroyAreaToolAchievement" + "_" + context.getString(destroyAreaToolAchievementInRange
                            .getAchievementStringResourceId()), Achievement.STATE_UNLOCKED).apply();

                    DestroyAreaToolAchievements destroyAreaToolAchievementNextUp = DestroyAreaToolAchievements.DESTROY_AREA_TOOL_ACHIEVEMENT_LEVEL_3;
                    achievementsClient.reveal(context.getString(destroyAreaToolAchievementNextUp.getAchievementStringResourceId()));
                    sharedPreferences.edit().putInt("destroyAreaToolAchievement" + "_" + context.getString(destroyAreaToolAchievementNextUp
                            .getAchievementStringResourceId()), Achievement.STATE_REVEALED).apply();

                    achievementsClient.setSteps(context.getString(destroyAreaToolAchievementNextUp.getAchievementStringResourceId()),
                            destroyAreaToolCurrentUseCount);
                }
            } else if (destroyAreaToolCurrentUseCount > 250 && destroyAreaToolCurrentUseCount <= 500) {
                DestroyAreaToolAchievements destroyAreaToolAchievementInRange = DestroyAreaToolAchievements.DESTROY_AREA_TOOL_ACHIEVEMENT_LEVEL_3;
                achievementsClient.setSteps(context.getString(destroyAreaToolAchievementInRange.getAchievementStringResourceId()),
                        destroyAreaToolCurrentUseCount);
                if (destroyAreaToolCurrentUseCount == 500) {
                    sharedPreferences.edit().putInt("destroyAreaToolAchievement" + "_" + context.getString(destroyAreaToolAchievementInRange
                            .getAchievementStringResourceId()), Achievement.STATE_UNLOCKED).apply();
                }
            }

            isScoreToBeSubmittedToLeaderboard = true;
        }

        sharedPreferences.edit().putInt("destroyAreaToolCurrentUseCount", destroyAreaToolCurrentUseCount).apply();
        sharedPreferences.edit().putInt("destroyAreaToolUseCountSubmitted", destroyAreaToolUseCountSubmitted).apply();
        return isScoreToBeSubmittedToLeaderboard;
    }

    // Here, we return 'true' if the score needs to be submitted to the 'Revive Game' tool use leaderboard & 'false' otherwise
    public boolean incrementReviveGameToolUseCount() {
        boolean isScoreToBeSubmittedToLeaderboard = false;
        reviveGameToolCurrentUseCount += 1;
        if (reviveGameToolCurrentUseCount >= reviveGameToolUseCountSubmitted + 10) { // Here, we need to update GPGS data
            reviveGameToolUseCountSubmitted = reviveGameToolCurrentUseCount;

            // Here, we setSteps() for appropriate achievement
            if (reviveGameToolCurrentUseCount >= 0 && reviveGameToolCurrentUseCount <= 50) {
                ReviveGameToolAchievements reviveGameToolAchievementInRange = ReviveGameToolAchievements.REVIVE_GAME_TOOL_ACHIEVEMENT_LEVEL_1;
                achievementsClient.setSteps(context.getString(reviveGameToolAchievementInRange.getAchievementStringResourceId()),
                        reviveGameToolCurrentUseCount);
                if (reviveGameToolCurrentUseCount == 50) {
                    sharedPreferences.edit().putInt("reviveGameToolAchievement" + "_" + context.getString(reviveGameToolAchievementInRange
                            .getAchievementStringResourceId()), Achievement.STATE_UNLOCKED).apply();

                    ReviveGameToolAchievements reviveGameToolAchievementNextUp = ReviveGameToolAchievements.REVIVE_GAME_TOOL_ACHIEVEMENT_LEVEL_2;
                    achievementsClient.reveal(context.getString(reviveGameToolAchievementNextUp.getAchievementStringResourceId()));
                    sharedPreferences.edit().putInt("reviveGameToolAchievement" + "_" + context.getString(reviveGameToolAchievementNextUp
                            .getAchievementStringResourceId()), Achievement.STATE_REVEALED).apply();

                    achievementsClient.setSteps(context.getString(reviveGameToolAchievementNextUp.getAchievementStringResourceId()),
                            reviveGameToolCurrentUseCount);
                }
            } else if (reviveGameToolCurrentUseCount > 50 && reviveGameToolCurrentUseCount <= 100) {
                ReviveGameToolAchievements reviveGameToolAchievementInRange = ReviveGameToolAchievements.REVIVE_GAME_TOOL_ACHIEVEMENT_LEVEL_2;
                achievementsClient.setSteps(context.getString(reviveGameToolAchievementInRange.getAchievementStringResourceId()),
                        reviveGameToolCurrentUseCount);
                if (reviveGameToolCurrentUseCount == 100) {
                    sharedPreferences.edit().putInt("reviveGameToolAchievement" + "_" + context.getString(reviveGameToolAchievementInRange
                            .getAchievementStringResourceId()), Achievement.STATE_UNLOCKED).apply();

                    ReviveGameToolAchievements reviveGameToolAchievementNextUp = ReviveGameToolAchievements.REVIVE_GAME_TOOL_ACHIEVEMENT_LEVEL_3;
                    achievementsClient.reveal(context.getString(reviveGameToolAchievementNextUp.getAchievementStringResourceId()));
                    sharedPreferences.edit().putInt("reviveGameToolAchievement" + "_" + context.getString(reviveGameToolAchievementNextUp
                            .getAchievementStringResourceId()), Achievement.STATE_REVEALED).apply();

                    achievementsClient.setSteps(context.getString(reviveGameToolAchievementNextUp.getAchievementStringResourceId()),
                            reviveGameToolCurrentUseCount);
                }
            } else if (reviveGameToolCurrentUseCount > 100 && reviveGameToolCurrentUseCount <= 200) {
                ReviveGameToolAchievements reviveGameToolAchievementInRange = ReviveGameToolAchievements.REVIVE_GAME_TOOL_ACHIEVEMENT_LEVEL_3;
                achievementsClient.setSteps(context.getString(reviveGameToolAchievementInRange.getAchievementStringResourceId()),
                        reviveGameToolCurrentUseCount);
                if (reviveGameToolCurrentUseCount == 200) {
                    sharedPreferences.edit().putInt("reviveGameToolAchievement" + "_" + context.getString(reviveGameToolAchievementInRange
                            .getAchievementStringResourceId()), Achievement.STATE_UNLOCKED).apply();
                }
            }

            isScoreToBeSubmittedToLeaderboard = true;
        }

        sharedPreferences.edit().putInt("reviveGameToolCurrentUseCount", reviveGameToolCurrentUseCount).apply();
        sharedPreferences.edit().putInt("reviveGameToolUseCountSubmitted", reviveGameToolUseCountSubmitted).apply();
        return isScoreToBeSubmittedToLeaderboard;
    }
}
