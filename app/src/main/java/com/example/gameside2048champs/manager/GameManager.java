package com.example.gameside2048champs.manager;

import static java.lang.Character.toChars;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.CountDownTimer;
import android.util.Pair;
import android.view.View;
import android.widget.GridLayout;

import androidx.appcompat.widget.AppCompatTextView;

import com.example.gameside2048champs.R;
import com.example.gameside2048champs.animations.AnimationsUtility;
import com.example.gameside2048champs.dataclasses.CellTransitionInfoMatrix;
import com.example.gameside2048champs.enums.CellValues;
import com.example.gameside2048champs.enums.Direction;
import com.example.gameside2048champs.enums.GameModes;
import com.example.gameside2048champs.enums.GameStates;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.games.PlayGames;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameManager {
    private final Activity parentActivity;
    private final GameModes currentGameMode;
    private long currentScore; // Total score of the user in a game session
    private long moveScore; // Total score of the user in a single move
    private boolean hasMoveBeenCompleted;
    private boolean hasGoalBeenCompleted;
    private List<List<Long>> gameMatrix;
    private GameProgressionManager gameProgressionManager;
    private UndoManager undoManager;
    private List<List<Boolean>> areAllAnimationsDone; // Boolean matrix to check if all animations are done
    private GameStates currentGameState;
    private AchievementsManager achievementsManager;
    private LeaderboardsClient leaderboardsClient;

    public GameManager(Activity parentActivity, GameModes currentGameMode) {
        this.parentActivity = parentActivity;
        this.currentGameMode = currentGameMode;

        moveScore = 0;

        hasMoveBeenCompleted = true;

        gameMatrix = new ArrayList<>();
        gameProgressionManager = new GameProgressionManager(parentActivity, currentGameMode);
        undoManager = new UndoManager();
        areAllAnimationsDone = new ArrayList<>();
        for (int row = 0; row < this.currentGameMode.getRows(); row++) {
            List<Long> gameMatrixRow = new ArrayList<>();
            List<Boolean> areAllAnimationsDoneRow = new ArrayList<>();
            for (int column = 0; column < this.currentGameMode.getColumns(); column++) {
                if (this.currentGameMode.getBlockCells().get(row).get(column).equals(-1L)) { // This is a block cell
                    gameMatrixRow.add(-1L);
                } else { // This is a free cell
                    gameMatrixRow.add(0L); // At first, we fill all cells with zero
                }
                areAllAnimationsDoneRow.add(false); // At first, we assign all values to false
            }
            gameMatrix.add(gameMatrixRow);
            areAllAnimationsDone.add(areAllAnimationsDoneRow);
        }

        achievementsManager = new AchievementsManager(this.parentActivity);
        leaderboardsClient = PlayGames.getLeaderboardsClient(this.parentActivity);
    }

    public void addNewValues(int numberOfCellsToAdd, List<List<Long>> givenBoard) {
        // Assigning some random start values to play with at start
        Random random = new Random();
        for (int currentCell = 0; currentCell < numberOfCellsToAdd; currentCell++) {
            long randomStartValue = (random.nextInt(1000) < 900) ?
                    gameProgressionManager.getLowestTileValue() : 2L * gameProgressionManager.getLowestTileValue();
            int randomRow, randomColumn;
            do {
                randomRow = random.nextInt(currentGameMode.getRows());
                randomColumn = random.nextInt(currentGameMode.getColumns());
            }
            while (givenBoard.get(randomRow).get(randomColumn) != 0);
            givenBoard.get(randomRow).set(randomColumn, randomStartValue);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public boolean startGameIfGameClosedCorrectly() {
        if (currentGameState == GameStates.GAME_OVER) {
            return false;
        }

        AppCompatTextView goalTileTextView = parentActivity.findViewById(R.id.goal_tile_title_game_activity_text_view);
        AppCompatTextView tutorialTextView = parentActivity.findViewById(R.id.tutorial_game_activity_text_view);
        if (hasGoalBeenCompleted) { // True, goal completed
            int greenTickEmojiUnicode = 0x2705;
            goalTileTextView.setText(String.format("GOAL TILE %s", String.valueOf(toChars(greenTickEmojiUnicode))));
            tutorialTextView.setText("Merge for higher tiles, SKY IS THE LIMIT");
        } else { // False, goal in progress
            goalTileTextView.setText("GOAL TILE");
            tutorialTextView.setText("Merge the tiles to form the GOAL TILE!");
        }

        if (currentGameState == GameStates.GAME_START) {
            // Adding 2 new random values to the gameMatrix, if game has just started
            addNewValues(2, gameMatrix);
        }

        // Updating the board in layout as per the values
        for (int row = 0; row < currentGameMode.getRows(); row++) {
            for (int column = 0; column < currentGameMode.getColumns(); column++) {
                GridLayout gameGridLayout = parentActivity.findViewById(R.id.game_grid_layout);
                AppCompatTextView textView = gameGridLayout.findViewWithTag("gameCell" + row + column);
                long currentCellValue = gameMatrix.get(row).get(column);
                if (currentCellValue == 0L || currentCellValue == -1L) {
                    textView.setVisibility(View.INVISIBLE);
                } else {
                    CellValues cellValueEnum = CellValues.getCellValueEnum(currentCellValue);

                    AnimationsUtility.executePopUpAnimation(textView, cellValueEnum.getCellValue(),
                            parentActivity.getColor(cellValueEnum.getNumberColorResourceId()),
                            parentActivity.getDrawable(cellValueEnum.getBackgroundDrawableResourceId()),
                            300, 200, currentGameMode.getGameLayoutProperties());
                }
            }
        }

        return true;
    }

    public void updateGameMatrix(List<List<Long>> afterMoveGameMatrix) {
        List<List<Long>> shallowCopyGameMatrix = new ArrayList<>();
        for (int row = 0; row < currentGameMode.getRows(); row++) {
            List<Long> shallowCopyGameMatrixRow = new ArrayList<>();
            for (int column = 0; column < currentGameMode.getColumns(); column++) {
                shallowCopyGameMatrixRow.add(gameMatrix.get(row).get(column));
                gameMatrix.get(row).set(column, afterMoveGameMatrix.get(row).get(column));
            }
            shallowCopyGameMatrix.add(shallowCopyGameMatrixRow);
        }

        // Making updates to the last saved game matrix state and score
        undoManager.saveStatePostMove(currentScore, shallowCopyGameMatrix);

        /* This is a great opportunity for us to make the checks to see if conditions for unlocking the many achievements
           offered in this game are met or not
        */
        for (int row = 0; row < currentGameMode.getRows(); row++) {
            for (int column = 0; column < currentGameMode.getColumns(); column++) {
                long tileValue = gameMatrix.get(row).get(column);
                achievementsManager.checkTileUnlockAchievements(tileValue);
            }
        }
    }

    public int findGameTilesCurrentlyOnBoard(List<List<Long>> givenBoard) {
        int gameTilesCount = 0;
        for (int row = 0; row < givenBoard.size(); row++) {
            for (int column = 0; column < givenBoard.get(row).size(); column++) {
                if (givenBoard.get(row).get(column) != 0L && givenBoard.get(row).get(column) != -1L) {
                    gameTilesCount++;
                }
            }
        }
        return gameTilesCount;
    }

    public List<Pair<Integer, Integer>> giveAllTilesPositionsOfGivenValue(List<List<Long>> givenBoard, long targetValue) {
        List<Pair<Integer, Integer>> targetValueTilesPositions = new ArrayList<>();
        for (int row = 0; row < givenBoard.size(); row++) {
            for (int column = 0; column < givenBoard.get(row).size(); column++) {
                if (givenBoard.get(row).get(column).equals(targetValue)) {
                    targetValueTilesPositions.add(new Pair<>(row, column));
                }
            }
        }
        return targetValueTilesPositions;
    }

    public long getHighestTileValueOnBoard() {
        long highestTileValue = 0;
        for (int row = 0; row < gameMatrix.size(); row++) {
            for (int column = 0; column < gameMatrix.get(row).size(); column++) {
                highestTileValue = Math.max(highestTileValue, gameMatrix.get(row).get(column));
            }
        }
        return highestTileValue;
    }

    public void updateGameMatrixPostUndo(List<List<Long>> previousGameState) {
        for (int row = 0; row < currentGameMode.getRows(); row++) {
            for (int column = 0; column < currentGameMode.getColumns(); column++) {
                gameMatrix.get(row).set(column, previousGameState.get(row).get(column));
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void addNewRandomValuePostMove(List<List<CellTransitionInfoMatrix>> ctiMatrix) {
        addNewValues(1, gameMatrix);

        currentScore += moveScore;
        moveScore = 0;
        hasMoveBeenCompleted = true;

        /* Duration to Add new Random Cell = 50 ms */
        int addNewRandomCellDuration = 50;
        for (int row = 0; row < currentGameMode.getRows(); row++) {
            for (int column = 0; column < currentGameMode.getColumns(); column++) {
                if (!gameMatrix.get(row).get(column).equals(ctiMatrix.get(row).get(column).getFinalValue())) {
                    GridLayout gameGridLayout = parentActivity.findViewById(R.id.game_grid_layout);
                    AppCompatTextView textView = gameGridLayout.findViewWithTag("gameCell" + row + column);
                    CellValues cellValueEnum = CellValues.getCellValueEnum(gameMatrix.get(row).get(column));
                    AnimationsUtility.executePopUpAnimation(textView, cellValueEnum.getCellValue(),
                            parentActivity.getColor(cellValueEnum.getNumberColorResourceId()),
                            parentActivity.getDrawable(cellValueEnum.getBackgroundDrawableResourceId()),
                            addNewRandomCellDuration, 0, currentGameMode.getGameLayoutProperties());
                }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void displayMoveAnimations(List<List<CellTransitionInfoMatrix>> ctiMatrix, Direction swipeDirection) {
        // Before starting an any animations setting them to false
        for (int row = 0; row < currentGameMode.getRows(); row++) {
            for (int column = 0; column < currentGameMode.getColumns(); column++) {
                areAllAnimationsDone.get(row).set(column, false);
            }
        }

        // Timer to timely check if all animations have been completed or not
        new CountDownTimer(10000/* Arbitrary long time for all animations to complete*/, 50) {
            @Override
            public void onTick(long millisUntilFinished) {
                boolean allDone = true;
                for (int row = 0; row < currentGameMode.getRows(); row++) {
                    for (int column = 0; column < currentGameMode.getColumns(); column++) {
                        if (areAllAnimationsDone.get(row).get(column).equals(false)) {
                            allDone = false; break;
                        }
                    }
                    if (!allDone) break;
                }
                if (allDone) {
                    addNewRandomValuePostMove(ctiMatrix);
                    cancel();
                }
            }
            @Override
            public void onFinish() {}
        }.start();

        // Setting up the animations for the game tiles
        List<List<AnimatorSet>> moveAnimationMatrix = new ArrayList<>();
        List<List<AnimatorSet>> endPartAnimationMatrix = new ArrayList<>();
        for (int row = 0; row < currentGameMode.getRows(); row++) {
            List<AnimatorSet> moveAnimationMatrixRow = new ArrayList<>();
            List<AnimatorSet> endPartAnimationMatrixRow = new ArrayList<>();
            for (int column = 0; column < currentGameMode.getColumns(); column++) {
                GridLayout gameGridLayout = parentActivity.findViewById(R.id.game_grid_layout);
                AppCompatTextView textView = gameGridLayout.findViewWithTag("gameCell" + row + column);
                CellValues cellValueEnum = CellValues.getCellValueEnum(gameMatrix.get(row).get(column));

                AnimatorSet moveAnimation; // Either Empty Animation OR Some Move Animation
                AnimatorSet endPartAnimation; // Either Empty Animation OR Simply Appear Animation OR Merge Animation

                /* Taking care of the moveAnimation first -> Duration = 125 ms  */
                int moveAnimationDuration = 125;
                if (ctiMatrix.get(row).get(column).getInitialValue() == 0L
                        || ctiMatrix.get(row).get(column).getInitialValue() == -1L) { // Initially un-filled
                    moveAnimation = AnimationsUtility.getEmptyAnimation(textView, moveAnimationDuration, 0, View.INVISIBLE);
                } else { // Initially filled
                    if (row == ctiMatrix.get(row).get(column).getFinalLocationRow()
                            && column == ctiMatrix.get(row).get(column).getFinalLocationColumn()) { // No move was made
                        moveAnimation = AnimationsUtility.getEmptyAnimation(textView, moveAnimationDuration, 0, View.VISIBLE);
                    } else { // Move was made
                        moveAnimation = AnimationsUtility.getSlideAnimation(row, column,
                                ctiMatrix.get(row).get(column).getFinalLocationRow(),
                                ctiMatrix.get(row).get(column).getFinalLocationColumn(),
                                currentGameMode.getRows(), currentGameMode.getColumns(),
                                parentActivity.findViewById(R.id.game_activity_game_frame_layout), swipeDirection,
                                textView, moveAnimationDuration, 0);
                    }
                }

                /* Now taking care of the endPartAnimation -> Duration = 75 ms */
                int endPartAnimationDuration = 75;
                if (gameMatrix.get(row).get(column) == 0L || gameMatrix.get(row).get(column) == -1L) { // Finally un-filled
                    endPartAnimation = AnimationsUtility.getEmptyAnimation(textView, endPartAnimationDuration, 0, View.INVISIBLE);
                } else { // Finally filled
                    if (ctiMatrix.get(row).get(column).isDidMerge()) { // Merge happened here
                        moveScore += gameMatrix.get(row).get(column);
                        endPartAnimation = AnimationsUtility.getMergeAnimation(textView, cellValueEnum.getCellValue(),
                                parentActivity.getColor(cellValueEnum.getNumberColorResourceId()),
                                parentActivity.getDrawable(cellValueEnum.getBackgroundDrawableResourceId()),
                                endPartAnimationDuration, 0, currentGameMode.getGameLayoutProperties());
                    } else { // Simply appear here
                        endPartAnimation = AnimationsUtility.getSimplyAppearAnimation(textView, cellValueEnum.getCellValue(),
                                parentActivity.getColor(cellValueEnum.getNumberColorResourceId()),
                                parentActivity.getDrawable(cellValueEnum.getBackgroundDrawableResourceId()),
                                endPartAnimationDuration, 0, currentGameMode.getGameLayoutProperties());
                    }
                }

                moveAnimationMatrixRow.add(moveAnimation);
                endPartAnimationMatrixRow.add(endPartAnimation);
            }
            moveAnimationMatrix.add(moveAnimationMatrixRow);
            endPartAnimationMatrix.add(endPartAnimationMatrixRow);
        }

        // Starting the animations as below
        for (int row = 0; row < currentGameMode.getRows(); row++) {
            for (int column = 0; column < currentGameMode.getColumns(); column++) {
                int finalRow = row;
                int finalColumn = column;
                moveAnimationMatrix.get(finalRow).get(finalColumn).addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {}
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        endPartAnimationMatrix.get(finalRow).get(finalColumn).start();
                    }
                    @Override
                    public void onAnimationCancel(Animator animation) {}
                    @Override
                    public void onAnimationRepeat(Animator animation) {}
                });
                endPartAnimationMatrix.get(finalRow).get(finalColumn).addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {}
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        areAllAnimationsDone.get(finalRow).set(finalColumn, true);
                    }
                    @Override
                    public void onAnimationCancel(Animator animation) {}
                    @Override
                    public void onAnimationRepeat(Animator animation) {}
                });
                moveAnimationMatrix.get(finalRow).get(finalColumn).start();
            }
        }
    }

    public void updateGameState() {
        if (!hasGoalBeenCompleted) {
            // The check goal completion condition is as follows
            for (int row = 0; row < currentGameMode.getRows(); row++) {
                for (int column = 0; column < currentGameMode.getColumns(); column++) {
                    if (gameMatrix.get(row).get(column) >= currentGameMode.getGoal()) {
                        currentGameState = GameStates.GAME_ONGOING;
                        hasGoalBeenCompleted = true;
                        return;
                    }
                }
            }
        }

        // GAME_ONGOING if we find a single 0 cell value
        for (int row = 0; row < currentGameMode.getRows(); row++) {
            for (int column = 0; column < currentGameMode.getColumns(); column++) {
                if (gameMatrix.get(row).get(column) == 0L) {
                    currentGameState = GameStates.GAME_ONGOING;
                    return;
                }
            }
        }

        // Still, GAME_ONGOING if we find that user can merge cells
        for (int row = 0; row < currentGameMode.getRows() - 1; row++) {
            for (int column = 0; column < currentGameMode.getColumns() - 1; column++) {
                if (gameMatrix.get(row).get(column) != -1L) {
                    if (gameMatrix.get(row).get(column).equals(gameMatrix.get(row + 1).get(column)) ||
                            gameMatrix.get(row).get(column).equals(gameMatrix.get(row).get(column + 1))) {
                        currentGameState = GameStates.GAME_ONGOING;
                        return;
                    }
                }
            }
        }
        for (int row = 0; row < currentGameMode.getRows() - 1; row++) {
            if (gameMatrix.get(row).get(currentGameMode.getColumns() - 1) != -1) {
                if (gameMatrix.get(row).get(currentGameMode.getColumns() - 1)
                        .equals(gameMatrix.get(row + 1).get(currentGameMode.getColumns() - 1))) {
                    currentGameState = GameStates.GAME_ONGOING;
                    return;
                }
            }
        }
        for (int column = 0; column < currentGameMode.getColumns() - 1; column++) {
            if (gameMatrix.get(currentGameMode.getRows() - 1).get(column) != -1) {
                if (gameMatrix.get(currentGameMode.getRows() - 1).get(column)
                        .equals(gameMatrix.get(currentGameMode.getRows() - 1).get(column + 1))) {
                    currentGameState = GameStates.GAME_ONGOING;
                    return;
                }
            }
        }

        // GAME_OVER otherwise
        currentGameState = GameStates.GAME_OVER;
    }

    public boolean checkGameLevelledUp() {
        boolean result;

        // First, we find the value of the highest value tile in the 'gameMatrix'
        long highestValueInGameMatrix = -100L; // Any value less than -1 is fine.
        for (int currentRow = 0; currentRow < gameMatrix.size(); currentRow++) {
            for (int currentColumn = 0; currentColumn < gameMatrix.get(currentRow).size(); currentColumn++) {
                highestValueInGameMatrix = Math.max(gameMatrix.get(currentRow).get(currentColumn), highestValueInGameMatrix);
            }
        }

        // Here, we now go to the gameProgressionManager to check if the game has levelled up or not
        result = (gameProgressionManager.hasGameLevelledUp(highestValueInGameMatrix, currentGameMode));

        return result;
    }

    // The following method is used to bring about the effects of Game Level Up
    public void removeLowerValueTiles() {
        for (int currentRow = 0; currentRow < gameMatrix.size(); currentRow++) {
            for (int currentColumn = 0; currentColumn < gameMatrix.get(currentRow).size(); currentColumn++) {
                long currentCellValue = gameMatrix.get(currentRow).get(currentColumn);
                if (currentCellValue > 0) { // So, the cell is neither a block cell (value = -1) nor an empty cell (value = 0)
                    if (currentCellValue < gameProgressionManager.getLowestTileValue()) {
                        gameMatrix.get(currentRow).set(currentColumn, 0L); // Removing the value out, so now we have an empty cell
                    }
                }
            }
        }
    }
}