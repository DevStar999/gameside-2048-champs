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
    private int currentScore; // Total score of the user in a game session
    private int moveScore; // Total score of the user in a single move
    private boolean hasMoveBeenCompleted;
    private boolean hasGoalBeenCompleted;
    private List<List<Integer>> gameMatrix;
    private UndoManager undoManager;
    private List<List<Boolean>> areAllAnimationsDone; // Boolean matrix to check if all animations are done
    private GameStates currentGameState;

    public GameManager(Activity parentActivity, GameModes currentGameMode) {
        this.parentActivity = parentActivity;
        this.currentGameMode = currentGameMode;

        moveScore = 0;

        hasMoveBeenCompleted = true;

        gameMatrix = new ArrayList<>();
        undoManager = new UndoManager();
        areAllAnimationsDone = new ArrayList<>();
        for (int row = 0; row < this.currentGameMode.getRows(); row++) {
            List<Integer> gameMatrixRow = new ArrayList<>();
            List<Boolean> areAllAnimationsDoneRow = new ArrayList<>();
            for (int column = 0; column < this.currentGameMode.getColumns(); column++) {
                if (this.currentGameMode.getBlockCells().get(row).get(column).equals(-1)) { // This is a block cell
                    gameMatrixRow.add(-1);
                } else { // This is a free cell
                    gameMatrixRow.add(0); // At first, we fill all cells with zero
                }
                areAllAnimationsDoneRow.add(false); // At first, we assign all values to false
            }
            gameMatrix.add(gameMatrixRow);
            areAllAnimationsDone.add(areAllAnimationsDoneRow);
        }
    }

    public void addNewValues(int numberOfCellsToAdd, List<List<Integer>> givenBoard) {
        // Assigning some random start values to play with at start
        Random random = new Random();
        for (int currentCell = 0; currentCell < numberOfCellsToAdd; currentCell++) {
            int randomStartValue = (random.nextInt(1000) < 900) ? 2 : 4;
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

        AppCompatTextView goalTileTextView = parentActivity.findViewById(R.id.goal_tile_text_view);
        AppCompatTextView tutorialTextView = parentActivity.findViewById(R.id.tutorial_text_view);
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
                int currentCellValue = gameMatrix.get(row).get(column);
                if (currentCellValue == 0 || currentCellValue == -1) {
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

    public void updateGameMatrix(List<List<Integer>> afterMoveGameMatrix) {
        List<List<Integer>> shallowCopyGameMatrix = new ArrayList<>();
        for (int row = 0; row < currentGameMode.getRows(); row++) {
            List<Integer> shallowCopyGameMatrixRow = new ArrayList<>();
            for (int column = 0; column < currentGameMode.getColumns(); column++) {
                shallowCopyGameMatrixRow.add(gameMatrix.get(row).get(column));
                gameMatrix.get(row).set(column, afterMoveGameMatrix.get(row).get(column));
            }
            shallowCopyGameMatrix.add(shallowCopyGameMatrixRow);
        }
        undoManager.saveStatePostMove(currentScore, shallowCopyGameMatrix);
    }

    public int findGameTilesCurrentlyOnBoard(List<List<Integer>> givenBoard) {
        int gameTilesCount = 0;
        for (int row = 0; row < givenBoard.size(); row++) {
            for (int column = 0; column < givenBoard.get(row).size(); column++) {
                if (givenBoard.get(row).get(column) != 0 && givenBoard.get(row).get(column) != -1) {
                    gameTilesCount++;
                }
            }
        }
        return gameTilesCount;
    }

    public List<Pair<Integer, Integer>> giveAllTilesPositionsOfGivenValue(List<List<Integer>> givenBoard, int targetValue) {
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

    public void updateGameMatrixPostUndo(List<List<Integer>> previousGameState) {
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
                if (ctiMatrix.get(row).get(column).getInitialValue() == 0
                        || ctiMatrix.get(row).get(column).getInitialValue() == -1) { // Initially un-filled
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
                                parentActivity.findViewById(R.id.game_frame_layout), swipeDirection,
                                textView, moveAnimationDuration, 0);
                    }
                }

                /* Now taking care of the endPartAnimation -> Duration = 75 ms */
                int endPartAnimationDuration = 75;
                if (gameMatrix.get(row).get(column) == 0 || gameMatrix.get(row).get(column) == -1) { // Finally un-filled
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
                if (gameMatrix.get(row).get(column) == 0) {
                    currentGameState = GameStates.GAME_ONGOING;
                    return;
                }
            }
        }

        // Still, GAME_ONGOING if we find that user can merge cells
        for (int row = 0; row < currentGameMode.getRows() - 1; row++) {
            for (int column = 0; column < currentGameMode.getColumns() - 1; column++) {
                if (gameMatrix.get(row).get(column) != -1) {
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
}