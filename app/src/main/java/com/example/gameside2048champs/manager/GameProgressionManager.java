package com.example.gameside2048champs.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.gameside2048champs.enums.GameModes;

import lombok.Getter;
import lombok.Setter;

/*  The summary of the Game Progression Logic is as follows ->
    [1] (a) Lowest value available = 2 ^ a
    (b) If we start fill up increasing unique values on the board, starting from {2 ^ a} until {2 ^ [(n - 1) + (a + op - 1)]},
    then all the tiles on the board will be filled up, but we need to relax this so that the game can progress, so let's
    give a relaxation of 3 tiles (1 is a must, but we are giving 3 tiles relaxation to the user)
    (c) Thus, the highest value available (after relaxation) = 2 ^ [(n - 1) + (a + op - 1) - 3]
    [2] Thus (a) When user unlocks = 2 ^ [(n - 1) + (a + op - 1) - 3 + 1] (b) Then, remove the tile = 2 ^ a
*/
@Getter
@Setter
public class GameProgressionManager {
    private Context context;
    private SharedPreferences sharedPreferences;
    private int totalPlayableTiles; // Total number of playable tiles in the current Game Mode [Short notation = n]
    private final int optionsCountChangeValueTool; // Total number of options given in 'Change Value' Tool [Short notation = op]
    private int lowestTilePowerOfTwo; // The Index or Power of 2 for the lowest available value tile [Short notation = a]
    private long lowestTileValue; // Value of the lowest available tile [Short notation = mnt]
    private int highestTilePowerOfTwo; // The Index or Power of 2 for the lowest available value tile [Short notation = (n - 1) + (a + op - 1) - 3]
    private long highestTileValue; // Value of the highest available tile [Shot notation = mxt]
    private final long highestPossibleScore; // The max. possible score permitted in the game

    public GameProgressionManager(Context context, GameModes currentGameMode) {
        this.context = context;
        this.sharedPreferences = this.context.getSharedPreferences("com.nerdcoredevelopment.game2048champsfinal", Context.MODE_PRIVATE);
        this.totalPlayableTiles = currentGameMode.getTotalPlayableTiles();
        this.optionsCountChangeValueTool = 5; // Making this final as we always only give 5 options in the 'Change Value' tool
        this.lowestTilePowerOfTwo = sharedPreferences.getInt("gameProgressionManagerLowestTilePowerOfTwo" + " " +
                currentGameMode.getMode() + " " + currentGameMode.getDimensions(), 1);
        this.lowestTileValue = powerOfTwo(this.lowestTilePowerOfTwo);
        this.highestTilePowerOfTwo = (this.totalPlayableTiles - 1) + (this.lowestTilePowerOfTwo + this.optionsCountChangeValueTool - 1) - 3;
        this.highestTileValue = powerOfTwo(this.highestTilePowerOfTwo);
        this.highestPossibleScore = 9200000000000000000L; // Value = 9200 X (10 ^ 15) = 9200Q (Q -> Quadrillion)
    }

    private long powerOfTwo(long index) {
        if (index == 0) {
            return 1L;
        }

        long result = 1L;
        for (int indexCounter = 1; indexCounter <= index; indexCounter++) {
            result = result * 2L;
        }
        return result;
    }

    private int getPowerOfTwo(long value) {
        int result = 0;
        while (value >= 2) {
            value = value / 2L;
            result++;
        }
        return result;
    }

    public void resetGameProgress(GameModes currentGameMode) {
        lowestTilePowerOfTwo = 1;
        lowestTileValue = powerOfTwo(lowestTilePowerOfTwo);
        highestTilePowerOfTwo = (totalPlayableTiles - 1) + (lowestTilePowerOfTwo + optionsCountChangeValueTool - 1) - 3;
        highestTileValue = powerOfTwo(highestTilePowerOfTwo);

        // Saving 'lowestTilePowerOfTwo' in sharedPreferences
        sharedPreferences.edit().putInt("gameProgressionManagerLowestTilePowerOfTwo" + " " +
                currentGameMode.getMode() + " " + currentGameMode.getDimensions(), lowestTilePowerOfTwo).apply();
    }

    public boolean hasGameLevelledUp(long valueToBeChecked, GameModes currentGameMode) {
        if (valueToBeChecked <= highestTileValue) {
            return false;
        }

        // The level up process is executed as follows -
        highestTileValue = valueToBeChecked;
        highestTilePowerOfTwo = getPowerOfTwo(highestTileValue);
        lowestTilePowerOfTwo = highestTilePowerOfTwo - optionsCountChangeValueTool + 1 - totalPlayableTiles + 1 + 3;
        lowestTileValue = powerOfTwo(lowestTilePowerOfTwo);

        // Saving 'lowestTilePowerOfTwo' in sharedPreferences
        sharedPreferences.edit().putInt("gameProgressionManagerLowestTilePowerOfTwo" + " " +
                        currentGameMode.getMode() + " " + currentGameMode.getDimensions(), lowestTilePowerOfTwo).apply();

        return true;
    }
}
