package com.example.gameside2048champs;

import static java.lang.Character.toChars;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.gameside2048champs.dialogs.GameOverDialog;
import com.example.gameside2048champs.dialogs.GamePausedDialog;
import com.example.gameside2048champs.dialogs.GameResetDialog;
import com.example.gameside2048champs.dialogs.GameUndoDialog;
import com.example.gameside2048champs.dialogs.GameWinDialog;
import com.example.gameside2048champs.enums.CellValues;
import com.example.gameside2048champs.enums.Direction;
import com.example.gameside2048champs.enums.GameModes;
import com.example.gameside2048champs.enums.GameOverDialogOptions;
import com.example.gameside2048champs.enums.GameStates;
import com.example.gameside2048champs.manager.GameManager;
import com.example.gameside2048champs.manager.UndoManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class GameActivity extends AppCompatActivity {
    // Variable Attributes
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private GameModes currentGameMode;
    private GameManager gameManager;
    private SwipeUtility swipeUtility;
    private Queue<Direction> movesQueue;
    private boolean goalDone;
    private boolean isCurrentScoreTheBest; // Flag to check if best score and current score displays have been merged
    private boolean isSoundOn;

    // UI Elements
    /* Layouts */
    private LinearLayout currentScoreLinearLayout;
    private LinearLayout bestScoreLinearLayout;
    private ConstraintLayout rootGameConstraintLayout;
    /* Views */
    private AppCompatTextView currentScoreTextView;
    private AppCompatTextView bestScoreTextView;
    private AppCompatTextView goalTileTextView;
    private AppCompatTextView tutorialTextView;
    private LottieAnimationView gridLottieView;

    private void initialiseVariableAttributes() {
        sharedPreferences = getSharedPreferences("com.nerdcoredevelopment.game2048champsfinal", Context.MODE_PRIVATE);
        gson = new Gson();
        currentGameMode = GameModes.getGameModeEnum(
                getIntent().getIntExtra("gameMatrixColumns", 4),
                getIntent().getIntExtra("gameMatrixRows", 4),
                getIntent().getStringExtra("gameMode"));
        gameManager = new GameManager(GameActivity.this, currentGameMode);
        gameManager.setCurrentGameState(GameStates.values()[
                sharedPreferences.getInt("GameStateEnumIndex" + " " + currentGameMode.getMode()
                        + " " + currentGameMode.getDimensions(), 0)]);
        swipeUtility = new SwipeUtility(currentGameMode.getRows(), currentGameMode.getColumns());
        movesQueue = new ArrayDeque<>();
        goalDone = sharedPreferences.getBoolean("GoalDone" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), false); // Keep default as 'false'
        isCurrentScoreTheBest = false;
        isSoundOn = true;
    }

    private void initialiseLayouts() {
        currentScoreLinearLayout = findViewById(R.id.current_score_linear_layout);
        bestScoreLinearLayout = findViewById(R.id.best_score_linear_layout);
        rootGameConstraintLayout = findViewById(R.id.root_game_constraint_layout);
    }

    private void initialiseViews() {
        currentScoreTextView = findViewById(R.id.current_score_value_text_view);
        currentScoreTextView.setText(sharedPreferences.getString("CurrentScore" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), "0"));
        gameManager.setCurrentScore(Integer.parseInt(currentScoreTextView.getText().toString()));
        gameManager.setHasGoalBeenCompleted(goalDone);

        String jsonRetrieveBoard = sharedPreferences.getString("CurrentBoard" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), gson.toJson(gameManager.getGameMatrix()));
        Type typeBoard = new TypeToken<List<List<Integer>>>(){}.getType();
        gameManager.setGameMatrix(gson.fromJson(jsonRetrieveBoard, typeBoard));

        String jsonRetrieveUndoManager = sharedPreferences.getString("UndoManager" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), gson.toJson(gameManager.getUndoManager()));
        Type typeUndoManager = new TypeToken<UndoManager>(){}.getType();
        gameManager.setUndoManager(gson.fromJson(jsonRetrieveUndoManager, typeUndoManager));

        bestScoreTextView = findViewById(R.id.best_score_value_text_view);
        bestScoreTextView.setText(sharedPreferences.getString("BestScore" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), "0"));
        goalTileTextView = findViewById(R.id.goal_tile_text_view);
        if (gameManager.getCurrentGameState() == GameStates.GAME_OVER) {
            updateScore("0");
        } else {
            updateScore(currentScoreTextView.getText().toString());
        }

        tutorialTextView = findViewById(R.id.tutorial_text_view);
        if (goalDone) {
            int greenTickEmojiUnicode = 0x2705;
            goalTileTextView.setText(String.format("GOAL TILE %s",
                    String.valueOf(toChars(greenTickEmojiUnicode))));
            tutorialTextView.setText("Merge for higher tiles, SKY IS THE LIMIT");
        } else {
            goalTileTextView.setText("GOAL TILE");
            tutorialTextView.setText("Merge the tiles to form the GOAL TILE!");
        }
    }

    private void initialiseGoalText() {
        CellValues goalCellValue = CellValues.getCellValueEnum(currentGameMode.getGoal());
        goalCellValue.setCellValue(currentGameMode.getGoal());
        AppCompatTextView goalTextView = findViewById(R.id.goal_value_text_view);
        goalTextView.setText(String.valueOf(goalCellValue.getCellValue()));
        goalTextView.setTextColor(getResources().getColor(goalCellValue.getNumberColorResourceId()));
        GradientDrawable goalScoreGradientDrawable = new GradientDrawable();
        goalScoreGradientDrawable.setColor(getResources().getColor(goalCellValue.getBackgroundColorResourceId()));
        float density = getResources().getDisplayMetrics().density;
        int radius = (int) (5 * density); // The corner radius is 5 dp
        goalScoreGradientDrawable.setCornerRadii(new float[]{0, 0, 0, 0, radius, radius, radius, radius});
        goalTextView.setBackground(goalScoreGradientDrawable);
    }

    private void initialiseViewsPostGameLayout() {
        gridLottieView = findViewById(R.id.grid_lottie_view);
    }

    private void initialise() {
        initialiseVariableAttributes();
        initialiseLayouts();
        initialiseViews();
        initialiseGoalText();
        GameLayoutProvider.provideGameFrameLayout(GameActivity.this, rootGameConstraintLayout,
                findViewById(R.id.game_frame_layout), currentGameMode); // initialise tiles
        if (!gameManager.startGameIfGameClosedCorrectly()) { // Means game was not closed correctly
            resetGameAndStartIfFlagTrue(true);
        }
        initialiseViewsPostGameLayout();
    }

    private void executeMove() {
        if (!gameManager.isHasMoveBeenCompleted()) {
            return;
        }

        Direction currentMoveDirection = movesQueue.poll();
        if (currentMoveDirection == Direction.RIGHT) {
            swipeUtility.madeLeftToRightSwipe(gameManager.getGameMatrix());
        } else if (currentMoveDirection == Direction.LEFT) {
            swipeUtility.madeRightToLeftSwipe(gameManager.getGameMatrix());
        } else if (currentMoveDirection == Direction.DOWN) {
            swipeUtility.madeTopToBottomSwipe(gameManager.getGameMatrix());
        } else if (currentMoveDirection == Direction.UP) {
            swipeUtility.madeBottomToTopSwipe(gameManager.getGameMatrix());
        }

        /* Can't directly use setter method for updating gameMatrix to avoid deep copy with
           afterMoveMatrix. So created a custom update method for updating gameMatrix.
           The following if state is so that, if nothing moved, it counts as no move at all and no
           new cell value should be added to the gameMatrix
        */
        if (!swipeUtility.getAfterMoveMatrix().equals(gameManager.getGameMatrix())) {
            gameManager.updateGameMatrix(swipeUtility.getAfterMoveMatrix());
            gameManager.setHasMoveBeenCompleted(false);
            gameManager.displayMoveAnimations(swipeUtility.getCtiMatrix(), currentMoveDirection);

            // Timer to timely check if the move has been completed or not
            new CountDownTimer(10000/* Arbitrary long time for all animations to complete*/, 50) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (gameManager.isHasMoveBeenCompleted()) {
                        gameManager.updateGameState();
                        updateScore(String.valueOf(gameManager.getCurrentScore()));
                        if (gameManager.isHasGoalBeenCompleted() && !goalDone) {
                            goalDone = true;
                            int greenTickEmojiUnicode = 0x2705;
                            goalTileTextView.setText(String.format("GOAL TILE %s",
                                    String.valueOf(toChars(greenTickEmojiUnicode))));
                            tutorialTextView.setText("Merge for higher tiles, SKY IS THE LIMIT");
                            sharedPreferences.edit().putBoolean("GoalDone" + " " + currentGameMode.getMode()
                                    + " " + currentGameMode.getDimensions(), goalDone).apply();
                            movesQueue.clear();
                            new GameWinDialog(GameActivity.this).show();
                        } else if (gameManager.getCurrentGameState() == GameStates.GAME_OVER) {
                            movesQueue.clear();
                            saveGameState();
                            GameOverDialog gameOverDialog = new GameOverDialog(GameActivity.this);
                            gameOverDialog.show();
                            gameOverDialog.setGameOverDialogListener(new GameOverDialog.GameOverDialogListener() {
                                @Override
                                public void getResponseOfOverDialog(GameOverDialogOptions optionSelected,
                                                                    boolean didUserRespond) {
                                    if (didUserRespond) {
                                        if (optionSelected == GameOverDialogOptions.MAIN_MENU) {
                                            resetGameAndStartIfFlagTrue(false);

                                            Intent intent = new Intent(GameActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else if (optionSelected == GameOverDialogOptions.PLAY_AGAIN) {
                                            resetGameAndStartIfFlagTrue(true);
                                        } else if (optionSelected == GameOverDialogOptions.UNDO_LAST_MOVE) {
                                            undoProcess();
                                        }
                                    } else {
                                        undoProcess();
                                        // TODO -> Do something more neat and clean instead of a Toast message
                                        Toast.makeText(GameActivity.this,
                                                "Last move has been undone", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        cancel();
                        // If there are still moves to execute then go on and execute them
                        if (movesQueue.size() > 0) {
                            executeMove();
                        }
                    }
                }

                @Override
                public void onFinish() {
                }
            }.start();
        }
    }

    @SuppressLint({"ClickableViewAccessibility", "SourceLockedOrientationActivity"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_GameSide2048Champs);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game);

        initialise();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        rootGameConstraintLayout.setOnTouchListener(new OnSwipeTouchListener(GameActivity.this) {
            public void onSwipeLeftToRight() {
                if (gameManager.getCurrentGameState() == GameStates.GAME_START ||
                        gameManager.getCurrentGameState() == GameStates.GAME_ONGOING) {
                    movesQueue.add(Direction.RIGHT);
                    executeMove();
                }
            }

            public void onSwipeRightToLeft() {
                if (gameManager.getCurrentGameState() == GameStates.GAME_START ||
                        gameManager.getCurrentGameState() == GameStates.GAME_ONGOING) {
                    movesQueue.add(Direction.LEFT);
                    executeMove();
                }
            }

            public void onSwipeTopToBottom() {
                if (gameManager.getCurrentGameState() == GameStates.GAME_START ||
                        gameManager.getCurrentGameState() == GameStates.GAME_ONGOING) {
                    movesQueue.add(Direction.DOWN);
                    executeMove();
                }
            }

            public void onSwipeBottomToTop() {
                if (gameManager.getCurrentGameState() == GameStates.GAME_START ||
                        gameManager.getCurrentGameState() == GameStates.GAME_ONGOING) {
                    movesQueue.add(Direction.UP);
                    executeMove();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void updateScore(String currentScore) {
        currentScoreTextView.setText(currentScore);

        if ((Integer.parseInt(currentScoreTextView.getText().toString())
                >= Integer.parseInt(bestScoreTextView.getText().toString()))
                && Integer.parseInt(currentScoreTextView.getText().toString()) > 0) {
            bestScoreTextView.setText(currentScoreTextView.getText().toString());
            sharedPreferences.edit().putString("BestScore" + " " + currentGameMode.getMode()
                    + " " + currentGameMode.getDimensions(), bestScoreTextView.getText().toString()).apply();
            if (!isCurrentScoreTheBest) {
                isCurrentScoreTheBest = true;
                AnimationUtility.mergeScoreDisplays(currentScoreLinearLayout, bestScoreLinearLayout,
                        findViewById(R.id.scores_layout_lottie),
                        getResources().getDisplayMetrics().density, 750);
            }
        } else {
            if (isCurrentScoreTheBest) {
                isCurrentScoreTheBest = false;
                AnimationUtility.splitScoreDisplays(currentScoreLinearLayout, bestScoreLinearLayout, 750);
            }
        }
    }

    private ArrayList<ArrayList<Integer>> getCopyOfGivenBoard(ArrayList<ArrayList<Integer>> givenBoard) {
        ArrayList<ArrayList<Integer>> copyOfGivenBoard = new ArrayList<>();
        for (int i = 0; i < givenBoard.size(); i++) {
            ArrayList<Integer> row = new ArrayList<>();
            for (int j = 0; j < givenBoard.get(i).size(); j++) {
                row.add(givenBoard.get(i).get(j));
            }
            copyOfGivenBoard.add(row);
        }
        return copyOfGivenBoard;
    }

    private void saveGameState() {
        // Saving the current state of the game to play later
        sharedPreferences.edit().putInt("GameStateEnumIndex" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), gameManager.getCurrentGameState().ordinal()).apply();
        sharedPreferences.edit().putString("CurrentScore" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), currentScoreTextView.getText().toString()).apply();
        sharedPreferences.edit().putString("UndoManager" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), gson.toJson(gameManager.getUndoManager())).apply();
        sharedPreferences.edit().putBoolean("GoalDone" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), goalDone).apply();
        if (gameManager.getCurrentGameState() == GameStates.GAME_START) {
            sharedPreferences.edit().putString("CurrentBoard" + " " + currentGameMode.getMode()
                    + " " + currentGameMode.getDimensions(), gson.toJson(getCopyOfGivenBoard(currentGameMode.getBlockCells()))).apply();
        } else {
            sharedPreferences.edit().putString("CurrentBoard" + " " + currentGameMode.getMode()
                    + " " + currentGameMode.getDimensions(), gson.toJson(gameManager.getGameMatrix())).apply();
        }
    }

    public void resetGameAndStartIfFlagTrue(boolean flag) {
        updateScore("0");
        gameManager = new GameManager(GameActivity.this, currentGameMode);
        goalDone = false;
        gameManager.setHasGoalBeenCompleted(false);
        gameManager.setCurrentGameState(GameStates.GAME_START);
        saveGameState();

        if (flag) {
            swipeUtility = new SwipeUtility(currentGameMode.getRows(), currentGameMode.getColumns());
            movesQueue.clear();
            gameManager.startGameIfGameClosedCorrectly();
        }
    }

    private void setupGamePausedDialog() {
        movesQueue.clear();
        saveGameState();
        GamePausedDialog gamePausedDialog = new GamePausedDialog(this);
        gamePausedDialog.show();
        gamePausedDialog.setGamePausedDialogListener(new GamePausedDialog.GamePausedDialogListener() {
            @Override
            public void getResponseOfPausedDialog(boolean response) {
                if (response) {
                    // Switching to MainActivity
                    Intent intent = new Intent(GameActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    // For when the 'Back' button on the device is pressed
    @Override
    public void onBackPressed() {
        setupGamePausedDialog();
    }

    // For when the 'Home' button on the device is pressed
    @Override
    protected void onPause() {
        super.onPause();
        saveGameState();
    }

    // For when the 'Recent Apps' button on the device is pressed
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        saveGameState();
    }

    /**
     * onClick listeners for the icons are as follows
     */
    public void pauseClicked(View view) {
        setupGamePausedDialog();
    }

    public void resetClicked(View view) {
        movesQueue.clear();
        GameResetDialog gameResetDialog = new GameResetDialog(this);
        gameResetDialog.show();
        gameResetDialog.setGameResetDialogListener(new GameResetDialog.GameResetDialogListener() {
            @Override
            public void getResponseOfResetDialog(boolean response) {
                if (response) {
                    resetGameAndStartIfFlagTrue(true);
                }
            }
        });
    }

    private void updateScoreOnUndo(String currentScore) {
        currentScoreTextView.setText(currentScore);

        // Making a check if the current score and the best scores need to be split or not
        if ((Integer.parseInt(currentScoreTextView.getText().toString())
                < Integer.parseInt(bestScoreTextView.getText().toString()))) {
            if (isCurrentScoreTheBest) {
                isCurrentScoreTheBest = false;
                AnimationUtility.splitScoreDisplays(currentScoreLinearLayout, bestScoreLinearLayout, 750);
            }
        }

        // Making a check if the goal completion is still intact or not
        for (int row = 0; row < currentGameMode.getRows(); row++) {
            for (int column = 0; column < currentGameMode.getColumns(); column++) {
                int value = gameManager.getGameMatrix().get(row).get(column);
                if (value >= gameManager.getCurrentGameMode().getGoal()) {
                    return;
                }
            }
        }
        goalDone = false;
        goalTileTextView.setText("GOAL TILE");
        tutorialTextView.setText("Merge the tiles to form the GOAL TILE!");
        sharedPreferences.edit().putBoolean("GoalDone" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), goalDone).apply();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void updateBoardOnUndo() {
        for (int row = 0; row < currentGameMode.getRows(); row++) {
            for (int column = 0; column < currentGameMode.getColumns(); column++) {
                GridLayout gameGridLayout = findViewById(R.id.game_grid_layout);
                AppCompatTextView textView = gameGridLayout.findViewWithTag("gameCell" + row + column);
                int value = gameManager.getGameMatrix().get(row).get(column);
                if (value == 0 || value == -1) {
                    textView.setVisibility(View.INVISIBLE);
                } else {
                    CellValues cellValueEnum = CellValues.getCellValueEnum(value);
                    cellValueEnum.setCellValue(value);

                    AnimationUtility.undoResetState(textView, cellValueEnum.getCellValue(),
                            getResources().getColor(cellValueEnum.getNumberColorResourceId()),
                            getDrawable(cellValueEnum.getBackgroundDrawableResourceId()),
                            currentGameMode.getGameLayoutProperties());
                }
            }
        }
    }

    public void normalToolsUndo(View view) {
        undoProcess();
    }

    private void undoProcess() {
        if (!gameManager.getUndoManager().isUndoUsed()) { // Undo was not used, so using it now
            AnimationUtility.normalToolsUndo(gridLottieView, rootGameConstraintLayout);
            new CountDownTimer(1000, 10000) {
                @Override
                public void onTick(long l) {
                }

                @Override
                public void onFinish() {
                    gameManager.setCurrentGameState(GameStates.GAME_ONGOING);
                    movesQueue.clear();
                    Pair<Integer, List<List<Integer>>> previousStateInfo = gameManager.getUndoManager().undoToPreviousState();
                    // Revert the state of the board to the previous state
                    gameManager.updateGameMatrixPostUndo(previousStateInfo.second);
                    updateBoardOnUndo();
                    // Revert score to previous state score
                    gameManager.setCurrentScore(previousStateInfo.first);
                    updateScoreOnUndo(String.valueOf(gameManager.getCurrentScore()));
                }
            }.start();
        } else { // Undo was used, so we need to show a message here
            String undoMessageText = (gameManager.getCurrentGameState() == GameStates.GAME_ONGOING) ?
                    "UNDO WAS USED ALREADY" : "NO MOVE HAS BEEN MADE YET";
            new GameUndoDialog(this, undoMessageText).show();
        }
    }

    public void changeSoundState(View view) {
        /*
        if (isSoundOn) {
            isSoundOn = false;
            ((AppCompatImageView) view).setImageDrawable(getDrawable(R.drawable.sound_off));
            Toast.makeText(this, "Sound Turned OFF Clicked", Toast.LENGTH_SHORT).show();
        } else {
            isSoundOn = true;
            ((AppCompatImageView) view).setImageDrawable(getDrawable(R.drawable.sound_on));
            Toast.makeText(this, "Sound Turned ON Clicked", Toast.LENGTH_SHORT).show();
        }
        */
    }
}