package com.example.gameside2048champs;

import static java.lang.Character.toChars;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.example.gameside2048champs.animations.AnimationsUtility;
import com.example.gameside2048champs.animations.ToolAnimationsUtility;
import com.example.gameside2048champs.dialogs.ArrivingToolDialog;
import com.example.gameside2048champs.dialogs.GameOverDialogFragment;
import com.example.gameside2048champs.dialogs.GamePausedDialog;
import com.example.gameside2048champs.dialogs.GameResetDialog;
import com.example.gameside2048champs.dialogs.GoalCompletionDialog;
import com.example.gameside2048champs.dialogs.ToolUseProhibitedDialog;
import com.example.gameside2048champs.enums.CellValues;
import com.example.gameside2048champs.enums.Direction;
import com.example.gameside2048champs.enums.GameModes;
import com.example.gameside2048champs.enums.GameStates;
import com.example.gameside2048champs.fragments.ChangeValueFragment;
import com.example.gameside2048champs.fragments.DestroyAreaFragment;
import com.example.gameside2048champs.fragments.EliminateValueFragment;
import com.example.gameside2048champs.fragments.ShopFragment;
import com.example.gameside2048champs.fragments.SmashTileFragment;
import com.example.gameside2048champs.fragments.SwapTilesFragment;
import com.example.gameside2048champs.manager.GameManager;
import com.example.gameside2048champs.manager.ReviveGameManager;
import com.example.gameside2048champs.manager.UndoManager;
import com.google.android.gms.games.leaderboard.ScoreSubmissionData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class GameActivity extends AppCompatActivity implements
        ShopFragment.OnShopFragmentInteractionListener,
        GameOverDialogFragment.OnGameOverDialogFragmentInteractionListener,
        SmashTileFragment.OnSmashTileFragmentInteractionListener,
        SwapTilesFragment.OnSwapTilesFragmentInteractionListener,
        ChangeValueFragment.OnChangeValueFragmentInteractionListener,
        EliminateValueFragment.OnEliminateValueFragmentInteractionListener,
        DestroyAreaFragment.OnDestroyAreaFragmentInteractionListener {
    // Variable Attributes
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private GameModes currentGameMode;
    private GameManager gameManager;
    private SwipeUtility swipeUtility;
    private Queue<Direction> movesQueue;
    private boolean goalDone;
    private int currentCoins;
    private Map<String, Integer> toolsCostMap;
    private long currentScore;
    private long bestScore;
    private boolean isCurrentScoreTheBest; // Flag to check if best score and current score displays have been merged
    private int multiMergeComboBarProgress;
    private boolean isToolsChestOpen;

    // UI Elements
    /* Layouts */
    private ConstraintLayout rootGameConstraintLayout;
    private FrameLayout gameFrameLayout;
    private LinearLayout standardToolsLinearLayout;
    private LinearLayout specialToolsLinearLayout;
    private LinearLayout toolsLottieLinearLayout;
    /* Views */
    private AppCompatImageView backgroundFilmImageView;
    private AppCompatTextView currentCoinsTextView;
    private AppCompatTextView currentScoreTextView;
    private AppCompatTextView bestScoreTextView;
    private ProgressBar multiMergeComboBar;
    private AppCompatTextView goalTileTextView;
    private LottieAnimationView toolsChangeLottie;
    private AppCompatImageView toolsChangeClickAreaImageView;
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
                sharedPreferences.getInt("gameStateEnumIndex" + " " + currentGameMode.getMode()
                        + " " + currentGameMode.getDimensions(), 0)]);
        swipeUtility = new SwipeUtility(currentGameMode.getRows(), currentGameMode.getColumns());
        movesQueue = new ArrayDeque<>();
        goalDone = sharedPreferences.getBoolean("goalDone" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), false); // Keep default as 'false'
        currentCoins = sharedPreferences.getInt("currentCoins", 5000);
        toolsCostMap = new HashMap<>() {{
            put("standardToolsUndoCost", 125);
            put("standardToolsSmashTileCost", 150);
            put("standardToolsSwapTilesCost", 200);
            put("specialToolsChangeValueCost", 400);
            put("specialToolsEliminateValueCost", 450);
            put("specialToolsDestroyAreaCost", 500);
            put("mysteryToolsReviveGameCost", 1000);
        }};
        currentScore = sharedPreferences.getLong("currentScore" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), 0L);
        bestScore = sharedPreferences.getLong("bestScore" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), 0L);
        isCurrentScoreTheBest = false;
        multiMergeComboBarProgress = sharedPreferences.getInt("multiMergeComboBarProgress"  + " " +
                currentGameMode.getMode() + " " + currentGameMode.getDimensions(), 1);
        isToolsChestOpen = false; // Always start with the Tools' Chest closed
    }

    private void initialiseLayouts() {
        rootGameConstraintLayout = findViewById(R.id.root_game_constraint_layout);
        gameFrameLayout = findViewById(R.id.game_activity_game_frame_layout);
        standardToolsLinearLayout = findViewById(R.id.standard_tools_game_activity_linear_layout);
        specialToolsLinearLayout = findViewById(R.id.special_tools_game_activity_linear_layout);
        toolsLottieLinearLayout = findViewById(R.id.tools_lottie_game_activity_linear_layout);

        if (!isToolsChestOpen) { // Tools chest is NOT open (This is the default condition as well)
            standardToolsLinearLayout.setVisibility(View.VISIBLE);
            specialToolsLinearLayout.setVisibility(View.GONE);
        } else { // Tools chest is open
            specialToolsLinearLayout.setVisibility(View.VISIBLE);
            standardToolsLinearLayout.setVisibility(View.GONE);
        }
    }

    private void initialiseViews() {
        backgroundFilmImageView = findViewById(R.id.background_film_game_activity_image_view);
        currentCoinsTextView = findViewById(R.id.current_coins_game_activity_text_view);
        currentCoinsTextView.setText(NumericValueDisplay.getGeneralValueDisplay(currentCoins));

        currentScoreTextView = findViewById(R.id.current_score_value_game_activity_text_view);
        currentScoreTextView.setText(NumericValueDisplay.getScoreValueDisplay(currentScore));
        gameManager.setCurrentScore(currentScore);
        gameManager.setHasGoalBeenCompleted(goalDone);

        String jsonRetrieveBoard = sharedPreferences.getString("currentBoard" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), gson.toJson(gameManager.getGameMatrix()));
        Type typeBoard = new TypeToken<List<List<Long>>>(){}.getType();
        gameManager.setGameMatrix(gson.fromJson(jsonRetrieveBoard, typeBoard));

        String jsonRetrieveUndoManager = sharedPreferences.getString("undoManager" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), gson.toJson(gameManager.getUndoManager()));
        Type typeUndoManager = new TypeToken<UndoManager>(){}.getType();
        gameManager.setUndoManager(gson.fromJson(jsonRetrieveUndoManager, typeUndoManager));

        bestScoreTextView = findViewById(R.id.best_score_value_game_activity_text_view);
        bestScoreTextView.setText(NumericValueDisplay.getScoreValueDisplay(bestScore));
        multiMergeComboBar = findViewById(R.id.multi_merge_combo_bar_game_activity_progress_bar);
        multiMergeComboBar.setMax(49);
        setMultiMergeComboBarProgress(multiMergeComboBarProgress);
        goalTileTextView = findViewById(R.id.goal_tile_title_game_activity_text_view);
        if (gameManager.getCurrentGameState() == GameStates.GAME_OVER) {
            updateScore(0L);
        } else {
            updateScore(currentScore);
        }

        toolsChangeLottie = findViewById(R.id.tools_change_game_activity_lottie);
        toolsChangeLottie.setProgress((!isToolsChestOpen) ? 0f : 1f);
        toolsChangeClickAreaImageView = findViewById(R.id.tools_change_click_area_game_activity_image_view);
        toolsChangeClickAreaImageView.setOnClickListener(view -> handleToolsChangeTransition());

        tutorialTextView = findViewById(R.id.tutorial_game_activity_text_view);
        if (goalDone) {
            int greenTickEmojiUnicode = 0x2705;
            goalTileTextView.setText(String.format("GOAL TILE %s",
                    String.valueOf(toChars(greenTickEmojiUnicode))));
            tutorialTextView.setText("Merge for higher tiles, SKY IS THE LIMIT");
        } else {
            goalTileTextView.setText("GOAL TILE");
            tutorialTextView.setText("Merge the tiles to form the GOAL TILE!");
        }

        AppCompatTextView standardToolsUndoCostTextView = findViewById(R.id.standard_tools_undo_cost_game_activity_text_view);
        standardToolsUndoCostTextView.setText(NumericValueDisplay.getGeneralValueDisplay(toolsCostMap.get("standardToolsUndoCost")));
        AppCompatTextView standardToolsSmashTileCostTextView = findViewById(R.id.standard_tools_smash_cost_game_activity_text_view);
        standardToolsSmashTileCostTextView.setText(NumericValueDisplay.getGeneralValueDisplay(toolsCostMap.get("standardToolsSmashTileCost")));
        AppCompatTextView standardToolsSwapTilesCostTextView = findViewById(R.id.standard_tools_swap_tiles_cost_game_activity_text_view);
        standardToolsSwapTilesCostTextView.setText(NumericValueDisplay.getGeneralValueDisplay(toolsCostMap.get("standardToolsSwapTilesCost")));
        AppCompatTextView specialToolsChangeValueCostTextView = findViewById(R.id.special_tools_change_value_cost_game_activity_text_view);
        specialToolsChangeValueCostTextView.setText(NumericValueDisplay.getGeneralValueDisplay(toolsCostMap.get("specialToolsChangeValueCost")));
        AppCompatTextView specialToolsEliminateValueCostTextView = findViewById(R.id.special_tools_eliminate_value_cost_game_activity_text_view);
        specialToolsEliminateValueCostTextView.setText(NumericValueDisplay.getGeneralValueDisplay(toolsCostMap.get("specialToolsEliminateValueCost")));
        AppCompatTextView specialToolsDestroyAreaCostTextView = findViewById(R.id.special_tools_destroy_area_cost_game_activity_text_view);
        specialToolsDestroyAreaCostTextView.setText(NumericValueDisplay.getGeneralValueDisplay(toolsCostMap.get("specialToolsDestroyAreaCost")));
    }

    private void initialiseGoalText() {
        CellValues goalCellValue = CellValues.getCellValueEnum(currentGameMode.getGoal());
        AppCompatTextView goalTextView = findViewById(R.id.goal_value_game_activity_text_view);
        goalTextView.setText(NumericValueDisplay.getGameTileValueDisplay(goalCellValue.getCellValue()));
        goalTextView.setTextColor(getColor(goalCellValue.getNumberColorResourceId()));
        GradientDrawable goalScoreGradientDrawable = new GradientDrawable();
        goalScoreGradientDrawable.setColor(getColor(goalCellValue.getBackgroundColorResourceId()));
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
                gameFrameLayout, currentGameMode); // initialise tiles
        if (!gameManager.startGameIfGameClosedCorrectly()) { // Means game was not closed correctly
            resetGameAndStartIfFlagTrue(true);
        }
        initialiseViewsPostGameLayout();
    }

    private void handleGameOverProcess() {
        if (movesQueue.size() > 0) {
            movesQueue.clear();
        }

        tutorialTextView.setText(String.format("GAME OVER %s",
                String.valueOf(toChars(Integer.parseInt("1F613", 16)))));
        saveGameState(true);

        // If GameOverDialogFragment has been opened even once, then return
        int countOfFragments = getSupportFragmentManager().getFragments().size();
        if (countOfFragments > 0) {
            List<Fragment> allFragments = new ArrayList<>(getSupportFragmentManager().getFragments());
            for (int index = 0; index < allFragments.size(); index++) {
                Fragment currentFragment = allFragments.get(index);
                if (currentFragment != null && currentFragment.getTag() != null && !currentFragment.getTag().isEmpty()
                        && currentFragment.getTag().equals("GAME_OVER_DIALOG_FRAGMENT")) {
                    return;
                }
            }
        }

        // Check the early out achievements here
        gameManager.getAchievementsManager().checkEarlyOutAchievements(gameManager.getHighestTileValueOnBoard(), currentScore);

        // Initiate the tool entry transition
        GameOverDialogFragment fragment = GameOverDialogFragment.newInstance(currentScore, bestScore, currentCoins);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.open_dialog, R.anim.close_dialog,
                R.anim.open_dialog, R.anim.close_dialog);
        transaction.addToBackStack(null);
        transaction.replace(R.id.game_activity_full_screen_fragment_container,
                fragment, "GAME_OVER_DIALOG_FRAGMENT").commit();
    }

    private void executeMove() {
        // Return if some previous move was not completed
        if (!gameManager.isHasMoveBeenCompleted()) {
            return;
        }

        // Return if currently the user is using a tool
        if (getSupportFragmentManager().findFragmentByTag("SMASH_TILE_FRAGMENT") != null
                || getSupportFragmentManager().findFragmentByTag("SWAP_TILES_FRAGMENT") != null
                || getSupportFragmentManager().findFragmentByTag("CHANGE_VALUE_FRAGMENT") != null
                || getSupportFragmentManager().findFragmentByTag("ELIMINATE_VALUE_FRAGMENT") != null
                || getSupportFragmentManager().findFragmentByTag("DESTROY_AREA_FRAGMENT") != null) {
            movesQueue.clear();
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
                        updateScore(gameManager.getCurrentScore());
                        updateMultiMergeComboBar(swipeUtility.getMergePositionsCount());
                        if (gameManager.isHasGoalBeenCompleted() && !goalDone) {
                            goalDone = true;
                            int greenTickEmojiUnicode = 0x2705;
                            goalTileTextView.setText(String.format("GOAL TILE %s",
                                    String.valueOf(toChars(greenTickEmojiUnicode))));
                            tutorialTextView.setText("Merge for higher tiles, SKY IS THE LIMIT");
                            sharedPreferences.edit().putBoolean("goalDone" + " " + currentGameMode.getMode()
                                    + " " + currentGameMode.getDimensions(), goalDone).apply();
                            movesQueue.clear();
                            new GoalCompletionDialog(GameActivity.this).show();
                        } else if (gameManager.getCurrentGameState() == GameStates.GAME_OVER) {
                            handleGameOverProcess();
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game);

        initialise();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
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
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void updateCoins(int currentCoins) {
        // For GameActivity
        this.currentCoins = currentCoins;
        sharedPreferences.edit().putInt("currentCoins", this.currentCoins).apply();
        currentCoinsTextView.setText(NumericValueDisplay.getGeneralValueDisplay(this.currentCoins));

        // For fragments which display coins
        sharedPreferences.edit().putInt("currentCoins", currentCoins).apply();
        List<Fragment> fragments = new ArrayList<>(getSupportFragmentManager().getFragments());
        for (int index = 0; index < fragments.size(); index++) {
            Fragment currentFragment = fragments.get(index);
            if (currentFragment != null && currentFragment.getTag() != null
                    && !currentFragment.getTag().isEmpty()) {
                if (currentFragment.getTag().equals("SHOP_FRAGMENT")) {
                    ((ShopFragment) currentFragment).updateCoinsShopFragment(currentCoins);
                }
            }
        }

        // Check if current coins count is greater than the highest most coins count
        int mostCoins = sharedPreferences.getInt("mostCoins", 5000);
        if (this.currentCoins >= mostCoins + 1000) {
            sharedPreferences.edit().putInt("mostCoins", this.currentCoins).apply();
            gameManager.getLeaderboardsClient().submitScoreImmediate(getString(R.string.leaderboard_coins_leaderboard),
                    this.currentCoins).addOnCompleteListener(new OnCompleteListener<ScoreSubmissionData>() {
                @Override
                public void onComplete(@NonNull Task<ScoreSubmissionData> task) {
                    if (task.isSuccessful()) {
                        // Score was submitted to the leaderboard
                    } else {
                        // Error submitting the score, so we use submitScore() method
                        gameManager.getLeaderboardsClient().submitScore(getString(R.string.leaderboard_coins_leaderboard),
                                GameActivity.this.currentCoins);
                    }
                }
            });
        }
    }

    private void updateScore(long updatedCurrentScore) {
        currentScore = updatedCurrentScore;
        currentScoreTextView.setText(NumericValueDisplay.getScoreValueDisplay(currentScore));

        if ((currentScore >= bestScore) && currentScore > 0L) {
            bestScore = currentScore;
            bestScoreTextView.setText(NumericValueDisplay.getScoreValueDisplay(bestScore));
            sharedPreferences.edit().putLong("bestScore" + " " + currentGameMode.getMode()
                    + " " + currentGameMode.getDimensions(), bestScore).apply();
            if (!isCurrentScoreTheBest) {
                isCurrentScoreTheBest = true;
            }
        } else {
            if (isCurrentScoreTheBest) {
                isCurrentScoreTheBest = false;
            }
        }

        /* This is a great opportunity for us to make the checks to see if conditions for unlocking the many achievements
           offered in this game are met or not
        */
        gameManager.getAchievementsManager().checkScoringAchievements(currentScore);
    }

    // TODO -> In the later stages of the game, make different ranges for smaller & larger dimension game boards
    @SuppressLint("UseCompatLoadingForDrawables")
    private void setMultiMergeComboBarProgress(int givenMultiMergeComboBarProgress) {
        multiMergeComboBarProgress = givenMultiMergeComboBarProgress;
        multiMergeComboBar.setProgress(multiMergeComboBarProgress);
        if (givenMultiMergeComboBarProgress >= 0 && givenMultiMergeComboBarProgress <= 6) {
            multiMergeComboBar.setProgressDrawable(getDrawable(R.drawable.combo_bar_progress_segment1));
        } else if (givenMultiMergeComboBarProgress >= 7 && givenMultiMergeComboBarProgress <= 12) {
            multiMergeComboBar.setProgressDrawable(getDrawable(R.drawable.combo_bar_progress_segment2));
        } else if (givenMultiMergeComboBarProgress >= 13 && givenMultiMergeComboBarProgress <= 18) {
            multiMergeComboBar.setProgressDrawable(getDrawable(R.drawable.combo_bar_progress_segment3));
        } else if (givenMultiMergeComboBarProgress >= 19 && givenMultiMergeComboBarProgress <= 24) {
            multiMergeComboBar.setProgressDrawable(getDrawable(R.drawable.combo_bar_progress_segment4));
        } else if (givenMultiMergeComboBarProgress >= 25 && givenMultiMergeComboBarProgress <= 30) {
            multiMergeComboBar.setProgressDrawable(getDrawable(R.drawable.combo_bar_progress_segment5));
        } else if (givenMultiMergeComboBarProgress >= 31 && givenMultiMergeComboBarProgress <= 36) {
            multiMergeComboBar.setProgressDrawable(getDrawable(R.drawable.combo_bar_progress_segment6));
        } else if (givenMultiMergeComboBarProgress >= 37 && givenMultiMergeComboBarProgress <= 42) {
            multiMergeComboBar.setProgressDrawable(getDrawable(R.drawable.combo_bar_progress_segment7));
        } else if (givenMultiMergeComboBarProgress >= 43 && givenMultiMergeComboBarProgress <= 48) {
            multiMergeComboBar.setProgressDrawable(getDrawable(R.drawable.combo_bar_progress_segment8));
        } else { // Over 48 i.e. 49 which is the limit, so the bar is completely filled now and it is time to reset it
            /* TODO -> Add the following animations/sounds at this stage of the game -
                       (1) Reset the progress of the Multi-Merge Progress Bar in an animated way
                       (2) Add the +10 coins to the total coins tally in an animated way (With Sound)
                       (3) Remove the old toast message 
            */
            multiMergeComboBarProgress = 1;
            multiMergeComboBar.setProgress(multiMergeComboBarProgress);
            multiMergeComboBar.setProgressDrawable(getDrawable(R.drawable.combo_bar_progress_segment1));

            // Added 20 coins to the total
            int rewardAmountCoins = 20;
            updateCoins(this.currentCoins + rewardAmountCoins);
            Toast.makeText(GameActivity.this, "Cheers \uD83E\uDD17 Rewarded +" + rewardAmountCoins
                    + " Coins", Toast.LENGTH_LONG).show();
        }
    }

    private void updateMultiMergeComboBar(int mergePositionsCount) {
        if (mergePositionsCount > 1) {
            setMultiMergeComboBarProgress(multiMergeComboBarProgress + 1);
        }
    }

    private ArrayList<ArrayList<Long>> getCopyOfGivenBoard(ArrayList<ArrayList<Long>> givenBoard) {
        ArrayList<ArrayList<Long>> copyOfGivenBoard = new ArrayList<>();
        for (int i = 0; i < givenBoard.size(); i++) {
            ArrayList<Long> row = new ArrayList<>();
            for (int j = 0; j < givenBoard.get(i).size(); j++) {
                row.add(givenBoard.get(i).get(j));
            }
            copyOfGivenBoard.add(row);
        }
        return copyOfGivenBoard;
    }

    private void saveGameState(boolean submitScore) {
        // Saving the current state of the game to play later
        sharedPreferences.edit().putInt("gameStateEnumIndex" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), gameManager.getCurrentGameState().ordinal()).apply();
        sharedPreferences.edit().putInt("multiMergeComboBarProgress"  + " " + currentGameMode.getMode() + " " +
                currentGameMode.getDimensions(), multiMergeComboBarProgress).apply();
        sharedPreferences.edit().putLong("currentScore" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), currentScore).apply();
        sharedPreferences.edit().putString("undoManager" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), gson.toJson(gameManager.getUndoManager())).apply();
        sharedPreferences.edit().putBoolean("goalDone" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), goalDone).apply();
        if (gameManager.getCurrentGameState() == GameStates.GAME_START) {
            sharedPreferences.edit().putString("currentBoard" + " " + currentGameMode.getMode()
                    + " " + currentGameMode.getDimensions(), gson.toJson(getCopyOfGivenBoard(currentGameMode.getBlockCells()))).apply();
        } else {
            sharedPreferences.edit().putString("currentBoard" + " " + currentGameMode.getMode()
                    + " " + currentGameMode.getDimensions(), gson.toJson(gameManager.getGameMatrix())).apply();
        }

        // Submitting the score to leaderboards if the current score is the high score
        if (isCurrentScoreTheBest && submitScore) {
            gameManager.getLeaderboardsClient().submitScoreImmediate(getString(currentGameMode.getLeaderboardStringResourceId()),
                    bestScore).addOnCompleteListener(new OnCompleteListener<ScoreSubmissionData>() {
                @Override
                public void onComplete(@NonNull Task<ScoreSubmissionData> task) {
                    if (task.isSuccessful()) {
                        // Score was submitted to the leaderboard
                    } else {
                        // Error submitting the score, so we use submitScore() method
                        gameManager.getLeaderboardsClient().submitScore(getString(currentGameMode.getLeaderboardStringResourceId()),
                                bestScore);
                    }
                }
            });
        }
    }

    public void resetGameAndStartIfFlagTrue(boolean flag) {
        updateScore(0L);
        gameManager = new GameManager(GameActivity.this, currentGameMode);
        goalDone = false;
        gameManager.setHasGoalBeenCompleted(false);
        gameManager.setCurrentGameState(GameStates.GAME_START);
        saveGameState(false);

        if (flag) {
            swipeUtility = new SwipeUtility(currentGameMode.getRows(), currentGameMode.getColumns());
            movesQueue.clear();
            gameManager.startGameIfGameClosedCorrectly();
        }
    }

    private void setupGamePausedDialog() {
        movesQueue.clear();
        GamePausedDialog gamePausedDialog = new GamePausedDialog(this);
        gamePausedDialog.show();
        gamePausedDialog.setGamePausedDialogListener(new GamePausedDialog.GamePausedDialogListener() {
            @Override
            public void getResponseOfPausedDialog(boolean response) {
                if (response) {
                    saveGameState(true);
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
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) { // Back button was pressed from activity
            setupGamePausedDialog();
        } else { // Back button was pressed from fragment
            int countOfFragments = getSupportFragmentManager().getFragments().size();
            Fragment topMostFragment = getSupportFragmentManager().getFragments().get(countOfFragments - 1);
            if (topMostFragment != null && topMostFragment.getTag() != null && !topMostFragment.getTag().isEmpty()) {
                String fragmentTag = topMostFragment.getTag();
                if (fragmentTag.equals("SMASH_TILE_FRAGMENT") || fragmentTag.equals("SWAP_TILES_FRAGMENT")
                        || fragmentTag.equals("CHANGE_VALUE_FRAGMENT") || fragmentTag.equals("ELIMINATE_VALUE_FRAGMENT")
                        || fragmentTag.equals("DESTROY_AREA_FRAGMENT")) {
                    boolean forToolFragmentsIsToolInProgress = false;
                    if (fragmentTag.equals("SMASH_TILE_FRAGMENT")) {
                        forToolFragmentsIsToolInProgress = ((SmashTileFragment) topMostFragment).checkToolUseState();
                        handleToolFragmentBackClicked(forToolFragmentsIsToolInProgress);
                    } else if (fragmentTag.equals("SWAP_TILES_FRAGMENT")) {
                        forToolFragmentsIsToolInProgress = ((SwapTilesFragment) topMostFragment).checkSecondClickStatus();
                        handleToolFragmentBackClicked(forToolFragmentsIsToolInProgress);
                    } else if (fragmentTag.equals("CHANGE_VALUE_FRAGMENT")) {
                        forToolFragmentsIsToolInProgress = ((ChangeValueFragment) topMostFragment).checkSecondClickStatus();
                        handleToolFragmentBackClicked(forToolFragmentsIsToolInProgress);
                    } else if (fragmentTag.equals("ELIMINATE_VALUE_FRAGMENT")) {
                        forToolFragmentsIsToolInProgress = ((EliminateValueFragment) topMostFragment).checkToolUseState();
                        handleToolFragmentBackClicked(forToolFragmentsIsToolInProgress);
                    } else { // For DestroyAreaFragment
                        forToolFragmentsIsToolInProgress = ((DestroyAreaFragment) topMostFragment).checkSecondClickStatus();
                        handleToolFragmentBackClicked(forToolFragmentsIsToolInProgress);
                    }

                    if (!forToolFragmentsIsToolInProgress) { // If tool is not in use, we can safely close the fragment
                        getSupportFragmentManager().popBackStack();
                    } else {
                        // If tool use is in progress we want to ignore the back clicked
                    }

                    // We will perform a game state check after every tool use as follows
                    gameManager.updateGameState();
                    if (gameManager.getCurrentGameState() == GameStates.GAME_OVER) {
                        handleGameOverProcess();
                    } else {
                        restoreTutorialTextViewMessage();
                    }
                    return;
                }
            }

            getSupportFragmentManager().popBackStack();
        }
    }

    // For when the 'Home' button on the device is pressed
    @Override
    protected void onPause() {
        super.onPause();
        saveGameState(false);
    }

    // For when the 'Recent Apps' button on the device is pressed
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        saveGameState(false);
    }

    /**
     * onClick listeners for purchasing coins are as follows
     */
    public void currentCoinsAddCoinsLayout(View view) {
        openShopFragment();
    }

    public void currentCoinsAddCoinsButton(View view) {
        openShopFragment();
    }

    private void openShopFragment() {
        // If ShopFragment was opened and is currently on top, then return
        int countOfFragments = getSupportFragmentManager().getFragments().size();
        if (countOfFragments > 0) {
            Fragment topMostFragment = getSupportFragmentManager().getFragments().get(countOfFragments - 1);
            if (topMostFragment != null && topMostFragment.getTag() != null && !topMostFragment.getTag().isEmpty()
                    && topMostFragment.getTag().equals("SHOP_FRAGMENT")) {
                return;
            }
        }

        ShopFragment fragment = new ShopFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right,
                R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.addToBackStack(null);
        transaction.add(R.id.game_activity_full_screen_fragment_container,
                fragment, "SHOP_FRAGMENT").commit();
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

    private boolean checkIfGoalCompletionIsIntact() {
        for (int row = 0; row < currentGameMode.getRows(); row++) {
            for (int column = 0; column < currentGameMode.getColumns(); column++) {
                long value = gameManager.getGameMatrix().get(row).get(column);
                if (value >= gameManager.getCurrentGameMode().getGoal()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void restoreTutorialTextViewMessage() {
        if (goalDone) {
            tutorialTextView.setText("Merge for higher tiles, SKY IS THE LIMIT");
        } else {
            tutorialTextView.setText("Merge the tiles to form the GOAL TILE!");
        }
    }

    private void handleGoalCompletionStatus() {
        // Making a check if the goal completion is still intact or not
        if (checkIfGoalCompletionIsIntact()) {
            int greenTickEmojiUnicode = 0x2705;
            goalTileTextView.setText(String.format("GOAL TILE %s", String.valueOf(toChars(greenTickEmojiUnicode))));
        } else {
            goalDone = false;
            gameManager.setHasGoalBeenCompleted(false);
            goalTileTextView.setText("GOAL TILE");
            sharedPreferences.edit().putBoolean("goalDone" + " " + currentGameMode.getMode()
                    + " " + currentGameMode.getDimensions(), goalDone).apply();
        }
    }

    private void updateScoreOnUndo(long updatedCurrentScore) {
        currentScore = updatedCurrentScore;
        currentScoreTextView.setText(NumericValueDisplay.getScoreValueDisplay(currentScore));

        // Making a check if the current score and the best scores need to be split or not
        if (currentScore < bestScore) {
            if (isCurrentScoreTheBest) {
                isCurrentScoreTheBest = false;
            }
        }

        handleGoalCompletionStatus();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void updateBoardOnUndo() {
        for (int row = 0; row < currentGameMode.getRows(); row++) {
            for (int column = 0; column < currentGameMode.getColumns(); column++) {
                GridLayout gameGridLayout = findViewById(R.id.game_grid_layout);
                AppCompatTextView textView = gameGridLayout.findViewWithTag("gameCell" + row + column);
                long value = gameManager.getGameMatrix().get(row).get(column);
                if (value == 0L || value == -1L) {
                    textView.setVisibility(View.INVISIBLE);
                } else {
                    CellValues cellValueEnum = CellValues.getCellValueEnum(value);
                    ToolAnimationsUtility.standardToolsUndoResetState(textView, cellValueEnum.getCellValue(),
                            getColor(cellValueEnum.getNumberColorResourceId()),
                            getDrawable(cellValueEnum.getBackgroundDrawableResourceId()),
                            currentGameMode.getGameLayoutProperties());
                }
            }
        }
    }

    private void addTempIndividualCellLottieLayerForReviveGame() {
        float density = getResources().getDisplayMetrics().density;
        int dp = currentGameMode.getGameLayoutProperties().getSpacing();
        int padding = Math.round((float) dp * density);

        // Adding a layer of lottie animation views for each individual game cell
        GridLayout gameCellLottieLayout = new GridLayout(this);
        gameCellLottieLayout.setId(R.id.game_cell_lottie_layout);
        gameCellLottieLayout.setRowCount(currentGameMode.getRows());
        gameCellLottieLayout.setColumnCount(currentGameMode.getColumns());
        for (int i = 0; i < currentGameMode.getRows(); i++) {
            for (int j = 0; j < currentGameMode.getColumns(); j++) {
                LottieAnimationView lottieView = new LottieAnimationView(this);
                lottieView.setTag("gameCellLottie" + i + j);
                lottieView.setVisibility(View.VISIBLE);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.height = 1;
                params.width = 1;
                params.topMargin = params.bottomMargin = params.leftMargin = params.rightMargin = padding;
                params.rowSpec = GridLayout.spec(i, 1f);
                params.columnSpec = GridLayout.spec(j, 1f);
                params.setGravity(Gravity.FILL);
                lottieView.setLayoutParams(params);
                gameCellLottieLayout.addView(lottieView);
            }
        }
        gameCellLottieLayout.setPadding(padding, padding, padding, padding);

        // Adding layer to gameFrameLayout
        gameFrameLayout.addView(gameCellLottieLayout);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void updateBoardOnReviveGame() {
        // Adding a layer of individual lottie cells
        addTempIndividualCellLottieLayerForReviveGame();

        // Hiding all the tiles just before the grid lottie animation ends
        for (int row = 0; row < currentGameMode.getRows(); row++) {
            for (int column = 0; column < currentGameMode.getColumns(); column++) {
                AppCompatTextView textView = gameFrameLayout.findViewWithTag("gameCell" + row + column);
                textView.setVisibility(View.INVISIBLE);
            }
        }

        // Setting up all the individual tile lottie animations
        int finalGameTileRow = 0;
        int finalGameTileColumn = 0;
        for (int row = 0; row < currentGameMode.getRows(); row++) {
            for (int column = 0; column < currentGameMode.getColumns(); column++) {
                LottieAnimationView cellLottie = gameFrameLayout.findViewWithTag("gameCellLottie" + row + column);
                long value = gameManager.getGameMatrix().get(row).get(column);
                if (value == 0L || value == -1L) {
                    cellLottie.setVisibility(View.INVISIBLE);
                } else {
                    finalGameTileRow = row;
                    finalGameTileColumn = column;
                    ToolAnimationsUtility.mysteryToolsReviveGameReviveTileSetup(cellLottie);
                }
            }
        }
        LottieAnimationView finalGameTileLottie = gameFrameLayout.findViewWithTag("gameCellLottie" +
                finalGameTileRow + finalGameTileColumn);
        finalGameTileLottie.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {
                gameFrameLayout.removeView(findViewById(R.id.game_cell_lottie_layout));
                for (int row = 0; row < currentGameMode.getRows(); row++) {
                    for (int column = 0; column < currentGameMode.getColumns(); column++) {
                        GridLayout gameGridLayout = findViewById(R.id.game_grid_layout);
                        AppCompatTextView textView = gameGridLayout.findViewWithTag("gameCell" + row + column);
                        long value = gameManager.getGameMatrix().get(row).get(column);
                        if (value == 0L || value == -1L) {
                            textView.setVisibility(View.INVISIBLE);
                        } else {
                            CellValues cellValueEnum = CellValues.getCellValueEnum(value);
                            AnimationsUtility.executePopUpAnimation(textView, cellValueEnum.getCellValue(),
                                    getColor(cellValueEnum.getNumberColorResourceId()),
                                    getDrawable(cellValueEnum.getBackgroundDrawableResourceId()), 300, 0,
                                    currentGameMode.getGameLayoutProperties());
                        }
                    }
                }

                rootGameConstraintLayout.setEnabled(true);
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        });

        // Playing all the individual tile lottie animations
        for (int row = 0; row < currentGameMode.getRows(); row++) {
            for (int column = 0; column < currentGameMode.getColumns(); column++) {
                LottieAnimationView cellLottie = gameFrameLayout.findViewWithTag("gameCellLottie" + row + column);
                long value = gameManager.getGameMatrix().get(row).get(column);
                if (value == 0L || value == -1L) {
                    cellLottie.setVisibility(View.INVISIBLE);
                } else {
                    cellLottie.playAnimation();
                }
            }
        }
    }

    private void handleToolsChangeTransition() {
        if (!isToolsChestOpen) { // False i.e. the tools chest is NOT open. So, now we will open it.
            toolsChangeLottie.setSpeed(0.7f);
            isToolsChestOpen = true;
        } else { // True i.e. the tools chest is open. So, now we will close it.
            toolsChangeLottie.setSpeed(-0.7f); // Negative speed will make the animation play from end in reverse
            isToolsChestOpen = false;
        }
        toolsChangeLottie.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                toolsChangeClickAreaImageView.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isToolsChestOpen) {
                    standardToolsLinearLayout.setVisibility(View.VISIBLE);
                    specialToolsLinearLayout.setVisibility(View.GONE);
                    toolsChangeClickAreaImageView.setClickable(true);
                } else {
                    specialToolsLinearLayout.setVisibility(View.VISIBLE);
                    standardToolsLinearLayout.setVisibility(View.GONE);

                    LottieAnimationView leftView = toolsLottieLinearLayout.findViewById(R.id.tools_lottie_left_game_activity_lottie);
                    LottieAnimationView midView = toolsLottieLinearLayout.findViewById(R.id.tools_lottie_middle_game_activity_lottie);
                    LottieAnimationView rightView = toolsLottieLinearLayout.findViewById(R.id.tools_lottie_right_game_activity_lottie);
                    toolsLottieLinearLayout.setVisibility(View.VISIBLE);
                    midView.addAnimatorListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {}
                        @Override
                        public void onAnimationEnd(Animator animator) {
                            toolsChangeClickAreaImageView.setClickable(true);
                            toolsLottieLinearLayout.setVisibility(View.GONE);
                        }
                        @Override
                        public void onAnimationCancel(Animator animator) {}
                        @Override
                        public void onAnimationRepeat(Animator animator) {}
                    });
                    midView.playAnimation();
                    leftView.playAnimation();
                    rightView.playAnimation();
                }
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
        toolsChangeLottie.playAnimation();
    }

    public void standardToolsUndo(View view) {
        undoProcess();
    }

    public void standardToolsSmashTile(View view) {
        smashTileProcess();
    }

    public void standardToolsSwapTiles(View view) {
        swapTilesProcess();
    }

    public void specialToolsChangeValue(View view) {
        changeValueProcess();
    }

    public void specialToolsEliminateValue(View view) {
        eliminateValueProcess();
    }

    public void specialToolsDestroyArea(View view) {
        // new ArrivingToolDialog(this).show();
        /* TODO -> Implement the Destroy Area tool and uncomment the following line */
        destroyAreaProcess();
    }

    private void undoProcess() {
        movesQueue.clear();
        if (!gameManager.getUndoManager().isUndoUsed()) { // Undo was not used, so using it now
            if (currentCoins >= toolsCostMap.get("standardToolsUndoCost")) {
                ToolAnimationsUtility.standardToolsUndo(gridLottieView, rootGameConstraintLayout);
                new CountDownTimer(1000, 10000) {
                    @Override
                    public void onTick(long l) {
                    }

                    @Override
                    public void onFinish() {
                        gameManager.setCurrentGameState(GameStates.GAME_ONGOING);
                        Pair<Long, List<List<Long>>> previousStateInfo = gameManager.getUndoManager().undoToPreviousState();
                        // Revert the state of the board to the previous state
                        gameManager.updateGameMatrixPostUndo(previousStateInfo.second);
                        updateBoardOnUndo();
                        // Revert score to previous state score
                        gameManager.setCurrentScore(previousStateInfo.first);
                        updateScoreOnUndo(gameManager.getCurrentScore());
                        // Restore the tutorial text view message
                        restoreTutorialTextViewMessage();
                        // Update the reduced number of coins
                        currentCoins -= toolsCostMap.get("standardToolsUndoCost");
                        updateCoins(currentCoins);
                        // Update the count of tool use
                        if (gameManager.getAchievementsManager().incrementUndoToolUseCount()) {
                            gameManager.getLeaderboardsClient().submitScoreImmediate(getString(R.string.leaderboard_undo_tool_masters),
                                    gameManager.getAchievementsManager().getUndoToolCurrentUseCount())
                                .addOnCompleteListener(new OnCompleteListener<ScoreSubmissionData>() {
                                    @Override
                                    public void onComplete(@NonNull Task<ScoreSubmissionData> task) {
                                        if (task.isSuccessful()) {
                                            // Score was submitted to the leaderboard
                                        } else {
                                            // Error submitting the score, so we use submitScore() method
                                            gameManager.getLeaderboardsClient().submitScore(getString(R.string.leaderboard_undo_tool_masters),
                                                    gameManager.getAchievementsManager().getUndoToolCurrentUseCount());
                                        }
                                    }
                                });
                        }
                    }
                }.start();
            } else {
                openShopFragment();
            }
        } else { // Undo was used, so we need to show a message here
            String undoMessageText = (gameManager.getCurrentGameState() == GameStates.GAME_ONGOING) ?
                    "'UNDO' tool was already used" : "No move has been made yet";
            new ToolUseProhibitedDialog(this, undoMessageText).show();
        }
    }

    private void addTempIndividualCellLottieLayer() {
        float density = getResources().getDisplayMetrics().density;
        int dp = currentGameMode.getGameLayoutProperties().getSpacing();
        int padding = Math.round((float) dp * density);

        // Adding a layer of lottie animation views for each individual game cell
        GridLayout gameCellLottieLayout = new GridLayout(this);
        gameCellLottieLayout.setId(R.id.game_cell_lottie_layout);
        gameCellLottieLayout.setRowCount(currentGameMode.getRows());
        gameCellLottieLayout.setColumnCount(currentGameMode.getColumns());
        for (int i = 0; i < currentGameMode.getRows(); i++) {
            for (int j = 0; j < currentGameMode.getColumns(); j++) {
                LottieAnimationView lottieView = new LottieAnimationView(this);
                lottieView.setTag("gameCellLottie" + i + j);
                lottieView.setVisibility(View.VISIBLE);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.height = 1;
                params.width = 1;
                params.topMargin = params.bottomMargin = params.leftMargin = params.rightMargin = padding;
                params.rowSpec = GridLayout.spec(i, 1f);
                params.columnSpec = GridLayout.spec(j, 1f);
                params.setGravity(Gravity.FILL);
                lottieView.setLayoutParams(params);
                gameCellLottieLayout.addView(lottieView);
            }
        }
        gameCellLottieLayout.setPadding(padding, padding, padding, padding);

        // Adding onClick listeners
        for (int i = 0; i < currentGameMode.getRows(); i++) {
            for (int j = 0; j < currentGameMode.getColumns(); j++) {
                LottieAnimationView lottieAnimationView = gameCellLottieLayout.findViewWithTag("gameCellLottie" + i + j);
                int row = i, column = j;
                long cellValue = gameManager.getGameMatrix().get(row).get(column);
                lottieAnimationView.setOnClickListener(view -> {
                    int countOfFragments = getSupportFragmentManager().getFragments().size();
                    if (countOfFragments > 0) {
                        Fragment topMostFragment = getSupportFragmentManager().getFragments().get(countOfFragments - 1);
                        if (topMostFragment != null && topMostFragment.getTag() != null &&
                                !topMostFragment.getTag().isEmpty()) {
                            if (topMostFragment.getTag().equals("SMASH_TILE_FRAGMENT")) {
                                if (cellValue != 0L && cellValue != -1L) {
                                    SmashTileFragment smashTileFragment = ((SmashTileFragment) topMostFragment);
                                    if (!smashTileFragment.checkToolUseState()) { // Tool use is not complete
                                        rootGameConstraintLayout.setEnabled(false);
                                        smashTileFragment.handleTileToBeSmashed(findViewById(R.id.game_cell_lottie_layout),
                                                lottieAnimationView, gridLottieView, new Pair<>(row, column));
                                    } // else, user has clicked after choosing tile to smash, so we ignore the click
                                }
                            } else if (topMostFragment.getTag().equals("SWAP_TILES_FRAGMENT")) {
                                if (cellValue != 0L && cellValue != -1L) {
                                    SwapTilesFragment swapTilesFragment = ((SwapTilesFragment) topMostFragment);
                                    if (swapTilesFragment.checkFirstClickStatus()
                                            && swapTilesFragment.checkSecondClickStatus()) {
                                        // User has clicked at some time else which is not valid for this tool use
                                    } else { // Tool use is not complete
                                        if (!swapTilesFragment.checkFirstClickStatus()) { // First click is yet to be done
                                            rootGameConstraintLayout.setEnabled(false);
                                            swapTilesFragment.handleSwapTilesToolFirstClick(lottieAnimationView,
                                                    new Pair<>(row, column));
                                        } else { // Time to execute the 2nd click as follows
                                            swapTilesFragment.handleSwapTilesToolSecondClick(
                                                    findViewById(R.id.game_cell_lottie_layout), lottieAnimationView,
                                                    gridLottieView, new Pair<>(row, column));
                                        }
                                    }
                                }
                            } else if (topMostFragment.getTag().equals("CHANGE_VALUE_FRAGMENT")) {
                                if (cellValue != 0L && cellValue != -1L) {
                                    ChangeValueFragment changeValueFragment = ((ChangeValueFragment) topMostFragment);
                                    if (changeValueFragment.checkFirstClickStatus()
                                            && changeValueFragment.checkSecondClickStatus()) {
                                        // User has clicked at some time else which is not valid for this tool use
                                    } else { // Tool use is not complete
                                        if (!changeValueFragment.checkFirstClickStatus()) { // First click is yet to be done
                                            rootGameConstraintLayout.setEnabled(false);
                                            changeValueFragment.handleChangeValueToolFirstClick(lottieAnimationView,
                                                    findViewById(R.id.game_cell_lottie_layout), gridLottieView,
                                                    new Pair<>(row, column));
                                        }
                                    }
                                }
                            } else if (topMostFragment.getTag().equals("ELIMINATE_VALUE_FRAGMENT")) {
                                if (cellValue != 0L && cellValue != -1L) {
                                    EliminateValueFragment eliminateValueFragment = ((EliminateValueFragment) topMostFragment);
                                    if (!eliminateValueFragment.checkToolUseState()) { // Tool use is not complete
                                        rootGameConstraintLayout.setEnabled(false);
                                        List<Pair<Integer, Integer>> targetValueTilesPositions =
                                                gameManager.giveAllTilesPositionsOfGivenValue(gameManager.getGameMatrix(), cellValue);
                                        List<LottieAnimationView> targetTilesLottie = new ArrayList<>();
                                        for (Pair<Integer, Integer> tilePosition: targetValueTilesPositions) {
                                            targetTilesLottie.add(gameCellLottieLayout.findViewWithTag("gameCellLottie"
                                                    + tilePosition.first + tilePosition.second));
                                        }
                                        eliminateValueFragment.handleValueToBeEliminated(findViewById(R.id.game_cell_lottie_layout),
                                                targetTilesLottie, gridLottieView, targetValueTilesPositions);
                                    } // else, user has clicked after choosing tile value to eliminate, so we ignore the click
                                }
                            } else if (topMostFragment.getTag().equals("DESTROY_AREA_FRAGMENT")) {
                                DestroyAreaFragment destroyAreaFragment = ((DestroyAreaFragment) topMostFragment);
                                if (destroyAreaFragment.checkFirstClickStatus()
                                        && destroyAreaFragment.checkSecondClickStatus()) {
                                    // User has clicked at some time else which is not valid for this tool use
                                } else { // Tool use is not complete
                                    // First we check if the selected area has atleast a single game tile or not
                                    int rowDown = (row == currentGameMode.getRows() - 1) ? row - 1 : row + 1;
                                    int columnRight = (column == currentGameMode.getColumns() - 1) ? column - 1 : column + 1;
                                    long cellValueTopLeft = cellValue;
                                    long cellValueTopRight = gameManager.getGameMatrix().get(row).get(columnRight);
                                    long cellValueBottomLeft = gameManager.getGameMatrix().get(rowDown).get(column);
                                    long cellValueBottomRight = gameManager.getGameMatrix().get(rowDown).get(columnRight);
                                    if ((cellValueTopLeft == 0 || cellValueTopLeft == -1)
                                            && (cellValueTopRight == 0 || cellValueTopRight == -1)
                                            && (cellValueBottomLeft == 0 || cellValueBottomLeft == -1)
                                            && (cellValueBottomRight == 0 || cellValueBottomRight == -1)
                                            && !destroyAreaFragment.checkFirstClickStatus()) {
                                        // The selected area does NOT have a single game tile
                                        Toast.makeText(GameActivity.this, "The selected area should " +
                                                "contain atleast 1 game tile", Toast.LENGTH_SHORT).show();
                                    } else { // The selected area has atleast
                                        if (!destroyAreaFragment.checkFirstClickStatus()) { // First click is yet to be done
                                            rootGameConstraintLayout.setEnabled(false);
                                            List<LottieAnimationView> destroyAreaTilesLotties = new ArrayList<>();
                                            List<Pair<Integer, Integer>> destroyAreaTilesPositions = new ArrayList<>();
                                            if (cellValueTopLeft > 0) { destroyAreaTilesLotties.add(lottieAnimationView);
                                                destroyAreaTilesPositions.add(new Pair<>(row, column));
                                            }
                                            if (cellValueTopRight > 0) { destroyAreaTilesLotties.add(gameCellLottieLayout
                                                .findViewWithTag("gameCellLottie" + row + columnRight));
                                                destroyAreaTilesPositions.add(new Pair<>(row, columnRight));
                                            }
                                            if (cellValueBottomLeft > 0) { destroyAreaTilesLotties.add(gameCellLottieLayout
                                                    .findViewWithTag("gameCellLottie" + rowDown + column));
                                                destroyAreaTilesPositions.add(new Pair<>(rowDown, column));
                                            }
                                            if (cellValueBottomRight > 0) { destroyAreaTilesLotties.add(gameCellLottieLayout
                                                    .findViewWithTag("gameCellLottie" + rowDown + columnRight));
                                                destroyAreaTilesPositions.add(new Pair<>(rowDown, columnRight));
                                            }
                                            destroyAreaFragment.handleDestroyAreaToolFirstClick(destroyAreaTilesLotties,
                                                    findViewById(R.id.game_cell_lottie_layout), gridLottieView,
                                                    destroyAreaTilesPositions);
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }

        // Adding layer to gameFrameLayout
        gameFrameLayout.addView(gameCellLottieLayout);
    }

    private void smashTileProcess() {
        movesQueue.clear();
        if (gameManager.findGameTilesCurrentlyOnBoard(gameManager.getGameMatrix()) < 1) {
            String smashTileMessage = "Atleast 1 game tile is required to use the \"SMASH TILE\" tool";
            new ToolUseProhibitedDialog(this, smashTileMessage).show();
            return;
        }

        if (currentCoins >= toolsCostMap.get("standardToolsSmashTileCost")) {
            // If SmashTileFragment was opened and is currently on top, then return
            int countOfFragments = getSupportFragmentManager().getFragments().size();
            if (countOfFragments > 0) {
                Fragment topMostFragment = getSupportFragmentManager().getFragments().get(countOfFragments - 1);
                if (topMostFragment != null && topMostFragment.getTag() != null && !topMostFragment.getTag().isEmpty()
                        && topMostFragment.getTag().equals("SMASH_TILE_FRAGMENT")) {
                    return;
                }
            }

            // Add a layer of individual lottie cells to the game board
            addTempIndividualCellLottieLayer();

            // Initiate the tool entry transition
            ToolAnimationsUtility.toolsBackgroundAppearAnimation(backgroundFilmImageView, 300);
            SmashTileFragment fragment = new SmashTileFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.tool_fragment_entry, R.anim.tool_fragment_exit,
                    R.anim.tool_fragment_entry, R.anim.tool_fragment_exit);
            transaction.addToBackStack(null);
            transaction.replace(R.id.tool_use_game_activity_fragment_container,
                    fragment, "SMASH_TILE_FRAGMENT").commit();
        } else {
            openShopFragment();
        }
    }

    private void swapTilesProcess() {
        movesQueue.clear();
        if (gameManager.findGameTilesCurrentlyOnBoard(gameManager.getGameMatrix()) < 2) {
            String swapTilesMessage = "Atleast 2 game tiles are required to use the \"SWAP TILES\" tool";
            new ToolUseProhibitedDialog(this, swapTilesMessage).show();
            return;
        }

        if (currentCoins >= toolsCostMap.get("standardToolsSwapTilesCost")) {
            // If SwapTilesFragment was opened and is currently on top, then return
            int countOfFragments = getSupportFragmentManager().getFragments().size();
            if (countOfFragments > 0) {
                Fragment topMostFragment = getSupportFragmentManager().getFragments().get(countOfFragments - 1);
                if (topMostFragment != null && topMostFragment.getTag() != null && !topMostFragment.getTag().isEmpty()
                        && topMostFragment.getTag().equals("SWAP_TILES_FRAGMENT")) {
                    return;
                }
            }

            // Add a layer of individual lottie cells to the game board
            addTempIndividualCellLottieLayer();

            // Initiate the tool entry transition
            ToolAnimationsUtility.toolsBackgroundAppearAnimation(backgroundFilmImageView, 300);
            SwapTilesFragment fragment = new SwapTilesFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.tool_fragment_entry, R.anim.tool_fragment_exit,
                    R.anim.tool_fragment_entry, R.anim.tool_fragment_exit);
            transaction.addToBackStack(null);
            transaction.replace(R.id.tool_use_game_activity_fragment_container,
                    fragment, "SWAP_TILES_FRAGMENT").commit();
        } else {
            openShopFragment();
        }
    }

    private void changeValueProcess() {
        movesQueue.clear();
        if (gameManager.findGameTilesCurrentlyOnBoard(gameManager.getGameMatrix()) < 1) {
            String changeValueMessage = "Atleast 1 game tile is required to use the \"CHANGE VALUE\" tool";
            new ToolUseProhibitedDialog(this, changeValueMessage).show();
            return;
        }

        if (currentCoins >= toolsCostMap.get("specialToolsChangeValueCost")) {
            // If ChangeValueFragment was opened and is currently on top, then return
            int countOfFragments = getSupportFragmentManager().getFragments().size();
            if (countOfFragments > 0) {
                Fragment topMostFragment = getSupportFragmentManager().getFragments().get(countOfFragments - 1);
                if (topMostFragment != null && topMostFragment.getTag() != null && !topMostFragment.getTag().isEmpty()
                        && topMostFragment.getTag().equals("CHANGE_VALUE_FRAGMENT")) {
                    return;
                }
            }

            // Add a layer of individual lottie cells to the game board
            addTempIndividualCellLottieLayer();

            // Initiate the tool entry transition
            ToolAnimationsUtility.toolsBackgroundAppearAnimation(backgroundFilmImageView, 300);
            ChangeValueFragment fragment = new ChangeValueFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.tool_fragment_entry, R.anim.tool_fragment_exit,
                    R.anim.tool_fragment_entry, R.anim.tool_fragment_exit);
            transaction.addToBackStack(null);
            transaction.replace(R.id.tool_use_game_activity_fragment_container,
                    fragment, "CHANGE_VALUE_FRAGMENT").commit();
        } else {
            openShopFragment();
        }
    }

    private void eliminateValueProcess() {
        movesQueue.clear();
        if (gameManager.findGameTilesCurrentlyOnBoard(gameManager.getGameMatrix()) < 1) {
            String eliminateValueMessage = "Atleast 1 game tile is required to use the \"ELIMINATE VALUE\" tool";
            new ToolUseProhibitedDialog(this, eliminateValueMessage).show();
            return;
        }

        if (currentCoins >= toolsCostMap.get("specialToolsEliminateValueCost")) {
            // If EliminateValueFragment was opened and is currently on top, then return
            int countOfFragments = getSupportFragmentManager().getFragments().size();
            if (countOfFragments > 0) {
                Fragment topMostFragment = getSupportFragmentManager().getFragments().get(countOfFragments - 1);
                if (topMostFragment != null && topMostFragment.getTag() != null && !topMostFragment.getTag().isEmpty()
                        && topMostFragment.getTag().equals("ELIMINATE_VALUE_FRAGMENT")) {
                    return;
                }
            }

            // Add a layer of individual lottie cells to the game board
            addTempIndividualCellLottieLayer();

            // Initiate the tool entry transition
            ToolAnimationsUtility.toolsBackgroundAppearAnimation(backgroundFilmImageView, 300);
            EliminateValueFragment fragment = new EliminateValueFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.tool_fragment_entry, R.anim.tool_fragment_exit,
                    R.anim.tool_fragment_entry, R.anim.tool_fragment_exit);
            transaction.addToBackStack(null);
            transaction.replace(R.id.tool_use_game_activity_fragment_container,
                    fragment, "ELIMINATE_VALUE_FRAGMENT").commit();
        } else {
            openShopFragment();
        }
    }

    private void destroyAreaProcess() {
        /* TODO -> After the implementation of the this tool is done at the very end of the process of this tool, remember to
                   add code to increment the tool use count (Refer to other tools if required)
        */
        movesQueue.clear();
        if (gameManager.findGameTilesCurrentlyOnBoard(gameManager.getGameMatrix()) < 1) {
            String destroyAreaMessage = "Atleast 1 game tile is required to use the \"DESTROY AREA\" tool";
            new ToolUseProhibitedDialog(this, destroyAreaMessage).show();
            return;
        }

        if (currentCoins >= toolsCostMap.get("specialToolsDestroyAreaCost")) {
            // If DestroyAreaFragment was opened and is currently on top, then return
            int countOfFragments = getSupportFragmentManager().getFragments().size();
            if (countOfFragments > 0) {
                Fragment topMostFragment = getSupportFragmentManager().getFragments().get(countOfFragments - 1);
                if (topMostFragment != null && topMostFragment.getTag() != null && !topMostFragment.getTag().isEmpty()
                        && topMostFragment.getTag().equals("DESTROY_AREA_FRAGMENT")) {
                    return;
                }
            }

            // Add a layer of individual lottie cells to the game board
            addTempIndividualCellLottieLayer();

            // Initiate the tool entry transition
            ToolAnimationsUtility.toolsBackgroundAppearAnimation(backgroundFilmImageView, 300);
            DestroyAreaFragment fragment = new DestroyAreaFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.tool_fragment_entry, R.anim.tool_fragment_exit,
                    R.anim.tool_fragment_entry, R.anim.tool_fragment_exit);
            transaction.addToBackStack(null);
            transaction.replace(R.id.tool_use_game_activity_fragment_container,
                    fragment, "DESTROY_AREA_FRAGMENT").commit();
        } else {
            openShopFragment();
        }
    }

    private void reviveGameProcess() {
        movesQueue.clear();
        /*  There is no requirement to check any condition to use this tool, as this is already done because dialog is only
            shown when the game is over
        */

        if (currentCoins >= toolsCostMap.get("mysteryToolsReviveGameCost")) {
            ToolAnimationsUtility.mysteryToolsReviveGame(this, gridLottieView, rootGameConstraintLayout);
            new CountDownTimer(2200, 10000) {
                @Override
                public void onTick(long l) {
                }

                @Override
                public void onFinish() {
                    gameManager.setCurrentGameState(GameStates.GAME_ONGOING);
                    // Storing the result of revive game tool before hand with us
                    ReviveGameManager reviveGameManager = new ReviveGameManager(gameManager.getGameMatrix());
                    List<List<Long>> postReviveGameProcessGameMatrix = reviveGameManager.gameMatrixPostReviveGame();
                    /*  Taking steps for undo, so as to have the game state which was just before the game got over,
                        available for undo if the user chooses to do undo just after the revive game process
                    */
                    Pair<Long, List<List<Long>>> previousStateInfo = gameManager.getUndoManager().undoToPreviousState();
                    gameManager.updateGameMatrixPostUndo(previousStateInfo.second);
                    gameManager.setCurrentScore(previousStateInfo.first);
                    currentScore = gameManager.getCurrentScore();
                    // Taking steps for revive game process as follows
                    gameManager.updateGameMatrix(postReviveGameProcessGameMatrix);
                    updateBoardOnReviveGame();
                    // Restore the tutorial text view message
                    restoreTutorialTextViewMessage();
                    // Update the reduced number of coins
                    currentCoins -= toolsCostMap.get("mysteryToolsReviveGameCost");
                    updateCoins(currentCoins);
                    // Update the count of tool use
                    if (gameManager.getAchievementsManager().incrementReviveGameToolUseCount()) {
                        gameManager.getLeaderboardsClient().submitScoreImmediate(getString(R.string.leaderboard_revive_game_tool_masters),
                                gameManager.getAchievementsManager().getReviveGameToolCurrentUseCount())
                            .addOnCompleteListener(new OnCompleteListener<ScoreSubmissionData>() {
                                @Override
                                public void onComplete(@NonNull Task<ScoreSubmissionData> task) {
                                    if (task.isSuccessful()) {
                                        // Score was submitted to the leaderboard
                                    } else {
                                        // Error submitting the score, so we use submitScore() method
                                        gameManager.getLeaderboardsClient().submitScore(getString(R.string.leaderboard_revive_game_tool_masters),
                                                gameManager.getAchievementsManager().getReviveGameToolCurrentUseCount());
                                    }
                                }
                            });
                    }
                }
            }.start();
        } else {
            openShopFragment();
        }
    }

    @Override
    public void onShopFragmentInteractionBackClicked() {
        onBackPressed();
        if (gameManager.getCurrentGameState() == GameStates.GAME_OVER) {
            handleGameOverProcess();
        }
    }

    @Override
    public void onShopFragmentInteractionUpdateCoins(int currentCoins) {
        updateCoins(currentCoins);
    }

    @Override
    public void onGameOverDialogFragmentInteractionUserDidNotRespond() {
        // If the user does not respond we will start a new game from our side
        resetGameAndStartIfFlagTrue(true);
    }

    @Override
    public void onGameOverDialogFragmentInteractionStandardToolsUndoClicked() {
        onBackPressed();
        undoProcess();
    }

    @Override
    public void onGameOverDialogFragmentInteractionStandardToolsSmashTileClicked() {
        onBackPressed();
        smashTileProcess();
    }

    @Override
    public void onGameOverDialogFragmentInteractionStandardToolsSwapTilesClicked() {
        onBackPressed();
        swapTilesProcess();
    }

    @Override
    public void onGameOverDialogFragmentInteractionSpecialToolsChangeValueClicked() {
        onBackPressed();
        changeValueProcess();
    }

    @Override
    public void onGameOverDialogFragmentInteractionSpecialToolsEliminateValueClicked() {
        onBackPressed();
        eliminateValueProcess();
    }

    @Override
    public void onGameOverDialogFragmentInteractionSpecialToolsDestroyAreaClicked() {
        onBackPressed();
        // new ArrivingToolDialog(GameActivity.this).show();
        /* TODO -> Implement the Destroy Area tool and uncomment the following line */
        destroyAreaProcess();
    }

    @Override
    public void onGameOverDialogFragmentInteractionMysteryToolsReviveGameClicked() {
        onBackPressed();
        reviveGameProcess();
    }

    @Override
    public void onGameOverDialogFragmentInteractionShopCoinsClicked() {
        onBackPressed();
        openShopFragment();
    }

    @Override
    public void onGameOverDialogFragmentInteractionMainMenuClicked() {
        resetGameAndStartIfFlagTrue(false);
        onBackPressed();

        Intent intent = new Intent(GameActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onGameOverDialogFragmentInteractionPlayAgainClicked() {
        onBackPressed();
        resetGameAndStartIfFlagTrue(true);
    }

    @Override
    public void onSmashTileFragmentInteractionBackClicked() {
        onBackPressed();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void onSmashTileFragmentInteractionProcessToolUse(Pair<Integer, Integer> targetTilePosition) {
        // Making a copy of the board
        List<List<Long>> copyOfCurrentBoard = new ArrayList<>();
        for (int i = 0; i < gameManager.getGameMatrix().size(); i++) {
            List<Long> boardRow = new ArrayList<>(gameManager.getGameMatrix().get(i));
            copyOfCurrentBoard.add(boardRow);
        }

        // Removing the chosen tile from the board
        copyOfCurrentBoard.get(targetTilePosition.first).set(targetTilePosition.second, 0L);
        AppCompatTextView targetTextView = rootGameConstraintLayout.findViewWithTag("gameCell" +
                targetTilePosition.first + targetTilePosition.second);
        targetTextView.setVisibility(View.INVISIBLE);
        gameManager.updateGameMatrix(copyOfCurrentBoard);

        // Checking if the board has game tiles, or do we need to insert some random values
        if (gameManager.findGameTilesCurrentlyOnBoard(copyOfCurrentBoard) == 0) {
            gameManager.addNewValues(2, copyOfCurrentBoard);
            int addNewRandomCellDuration = 50;
            for (int row = 0; row < currentGameMode.getRows(); row++) {
                for (int column = 0; column < currentGameMode.getColumns(); column++) {
                    if (!copyOfCurrentBoard.get(row).get(column).equals(gameManager.getGameMatrix().get(row).get(column))) {
                        AppCompatTextView textView = rootGameConstraintLayout.findViewWithTag("gameCell" + row + column);
                        CellValues cellValueEnum = CellValues.getCellValueEnum(copyOfCurrentBoard.get(row).get(column));
                        AnimationsUtility.executePopUpAnimation(textView, cellValueEnum.getCellValue(),
                                getColor(cellValueEnum.getNumberColorResourceId()),
                                getDrawable(cellValueEnum.getBackgroundDrawableResourceId()),
                                addNewRandomCellDuration, 500, currentGameMode.getGameLayoutProperties());
                    }
                }
            }
            gameManager.updateGameMatrix(copyOfCurrentBoard);
        }

        // Update the reduced number of coins
        currentCoins -= toolsCostMap.get("standardToolsSmashTileCost");
        updateCoins(currentCoins);

        // Final set of actions
            // Update the count of tool use
        if (gameManager.getAchievementsManager().incrementSmashTileToolUseCount()) {
            gameManager.getLeaderboardsClient().submitScoreImmediate(getString(R.string.leaderboard_smash_tile_tool_masters),
                    gameManager.getAchievementsManager().getSmashTileToolCurrentUseCount())
                .addOnCompleteListener(new OnCompleteListener<ScoreSubmissionData>() {
                    @Override
                    public void onComplete(@NonNull Task<ScoreSubmissionData> task) {
                        if (task.isSuccessful()) {
                            // Score was submitted to the leaderboard
                        } else {
                            // Error submitting the score, so we use submitScore() method
                            gameManager.getLeaderboardsClient().submitScore(getString(R.string.leaderboard_smash_tile_tool_masters),
                                    gameManager.getAchievementsManager().getSmashTileToolCurrentUseCount());
                        }
                    }
                });
        }
        saveGameState(false);
        handleGoalCompletionStatus();
        onBackPressed();
    }

    @Override
    public void onSwapTilesFragmentInteractionBackClicked() {
        onBackPressed();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onSwapTilesFragmentInteractionProcessToolUse(Pair<Integer, Integer> firstSwapTilePosition,
                                                             Pair<Integer, Integer> secondSwapTilePosition) {
        // Making a copy of the board
        List<List<Long>> copyOfCurrentBoard = new ArrayList<>();
        for (int i = 0; i < gameManager.getGameMatrix().size(); i++) {
            List<Long> boardRow = new ArrayList<>(gameManager.getGameMatrix().get(i));
            copyOfCurrentBoard.add(boardRow);
        }

        /* Swapping the chosen tiles on the board */
        // Settings values in copyOfCurrentBoard
        long firstValue = gameManager.getGameMatrix().get(firstSwapTilePosition.first).get(firstSwapTilePosition.second);
        long secondValue = gameManager.getGameMatrix().get(secondSwapTilePosition.first).get(secondSwapTilePosition.second);
        copyOfCurrentBoard.get(firstSwapTilePosition.first).set(firstSwapTilePosition.second, secondValue);
        copyOfCurrentBoard.get(secondSwapTilePosition.first).set(secondSwapTilePosition.second, firstValue);
        int popUpAnimationDuration = 250; // In Milli-seconds
        // Giving new look to first swap position
        AppCompatTextView firstPositionTextView = rootGameConstraintLayout.findViewWithTag("gameCell" +
                firstSwapTilePosition.first + firstSwapTilePosition.second);
        CellValues cellValueEnumFirstPosition = CellValues.getCellValueEnum(secondValue);
        // Giving new look to second swap position
        AppCompatTextView secondPositionTextView = rootGameConstraintLayout.findViewWithTag("gameCell" +
                secondSwapTilePosition.first + secondSwapTilePosition.second);
        CellValues cellValueEnumSecondPosition = CellValues.getCellValueEnum(firstValue);
        // Executing the pop up animation for both cell one after the other
        AnimationsUtility.executePopUpAnimation(firstPositionTextView, cellValueEnumFirstPosition.getCellValue(),
                getColor(cellValueEnumFirstPosition.getNumberColorResourceId()),
                getDrawable(cellValueEnumFirstPosition.getBackgroundDrawableResourceId()),
                popUpAnimationDuration, 0, currentGameMode.getGameLayoutProperties());
        AnimationsUtility.executePopUpAnimation(secondPositionTextView, cellValueEnumSecondPosition.getCellValue(),
                getColor(cellValueEnumSecondPosition.getNumberColorResourceId()),
                getDrawable(cellValueEnumSecondPosition.getBackgroundDrawableResourceId()),
                popUpAnimationDuration, 0, currentGameMode.getGameLayoutProperties());
        // Updating the game matrix
        gameManager.updateGameMatrix(copyOfCurrentBoard);

        // Update the reduced number of coins
        currentCoins -= toolsCostMap.get("standardToolsSwapTilesCost");
        updateCoins(currentCoins);

        // Final set of actions
            // Update the count of tool use
        if (gameManager.getAchievementsManager().incrementSwapTilesToolUseCount()) {
            gameManager.getLeaderboardsClient().submitScoreImmediate(getString(R.string.leaderboard_swap_tiles_tool_masters),
                    gameManager.getAchievementsManager().getSwapTilesToolCurrentUseCount())
                .addOnCompleteListener(new OnCompleteListener<ScoreSubmissionData>() {
                    @Override
                    public void onComplete(@NonNull Task<ScoreSubmissionData> task) {
                        if (task.isSuccessful()) {
                            // Score was submitted to the leaderboard
                        } else {
                            // Error submitting the score, so we use submitScore() method
                            gameManager.getLeaderboardsClient().submitScore(getString(R.string.leaderboard_swap_tiles_tool_masters),
                                    gameManager.getAchievementsManager().getSwapTilesToolCurrentUseCount());
                        }
                    }
                });
        }
        saveGameState(false);
        onBackPressed();
    }

    @Override
    public void onChangeValueFragmentInteractionBackClicked() {
        onBackPressed();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onChangeValueFragmentInteractionProcessToolUse(Pair<Integer, Integer> changeValueTilePosition, long newValue) {
        // Making a copy of the board
        List<List<Long>> copyOfCurrentBoard = new ArrayList<>();
        for (int i = 0; i < gameManager.getGameMatrix().size(); i++) {
            List<Long> boardRow = new ArrayList<>(gameManager.getGameMatrix().get(i));
            copyOfCurrentBoard.add(boardRow);
        }

        /* Changing the value of the chosen tile */
        // Settings new value for chosen tile in copyOfCurrentBoard
        copyOfCurrentBoard.get(changeValueTilePosition.first).set(changeValueTilePosition.second, newValue);
        int popUpAnimationDuration = 250; // In Milli-seconds
        // Giving new look to first swap position
        AppCompatTextView changeValueTilePositionTextView = rootGameConstraintLayout.findViewWithTag("gameCell" +
                changeValueTilePosition.first + changeValueTilePosition.second);
        CellValues cellValueEnumFirstPosition = CellValues.getCellValueEnum(newValue);
        // Executing the pop up animation for the chosen tile to change value
        AnimationsUtility.executePopUpAnimation(changeValueTilePositionTextView, cellValueEnumFirstPosition.getCellValue(),
                getColor(cellValueEnumFirstPosition.getNumberColorResourceId()),
                getDrawable(cellValueEnumFirstPosition.getBackgroundDrawableResourceId()),
                popUpAnimationDuration, 0, currentGameMode.getGameLayoutProperties());
        // Updating the game matrix
        gameManager.updateGameMatrix(copyOfCurrentBoard);

        // Update the reduced number of coins
        currentCoins -= toolsCostMap.get("specialToolsChangeValueCost");
        updateCoins(currentCoins);

        // Final set of actions
            // Update the count of tool use
        if (gameManager.getAchievementsManager().incrementChangeValueToolUseCount()) {
            gameManager.getLeaderboardsClient().submitScoreImmediate(getString(R.string.leaderboard_change_value_tool_masters),
                    gameManager.getAchievementsManager().getChangeValueToolCurrentUseCount())
                .addOnCompleteListener(new OnCompleteListener<ScoreSubmissionData>() {
                    @Override
                    public void onComplete(@NonNull Task<ScoreSubmissionData> task) {
                        if (task.isSuccessful()) {
                            // Score was submitted to the leaderboard
                        } else {
                            // Error submitting the score, so we use submitScore() method
                            gameManager.getLeaderboardsClient().submitScore(getString(R.string.leaderboard_change_value_tool_masters),
                                    gameManager.getAchievementsManager().getChangeValueToolCurrentUseCount());
                        }
                    }
                });
        }
        saveGameState(false);
        handleGoalCompletionStatus();
        onBackPressed();
    }

    @Override
    public void onEliminateValueFragmentInteractionBackClicked() {
        onBackPressed();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onEliminateValueFragmentInteractionProcessToolUse(List<Pair<Integer, Integer>> targetTilesPositions) {
        // Making a copy of the board
        List<List<Long>> copyOfCurrentBoard = new ArrayList<>();
        for (int i = 0; i < gameManager.getGameMatrix().size(); i++) {
            List<Long> boardRow = new ArrayList<>(gameManager.getGameMatrix().get(i));
            copyOfCurrentBoard.add(boardRow);
        }

        // Removing the chosen tile from the board
        for (int index = 0; index < targetTilesPositions.size(); index++) {
            copyOfCurrentBoard.get(targetTilesPositions.get(index).first)
                    .set(targetTilesPositions.get(index).second, 0L);
            AppCompatTextView textView = rootGameConstraintLayout.findViewWithTag("gameCell" +
                    targetTilesPositions.get(index).first + targetTilesPositions.get(index).second);
            textView.setVisibility(View.INVISIBLE);
        }
        gameManager.updateGameMatrix(copyOfCurrentBoard);

        // Checking if the board has game tiles, or do we need to insert some random values
        if (gameManager.findGameTilesCurrentlyOnBoard(copyOfCurrentBoard) == 0) {
            gameManager.addNewValues(2, copyOfCurrentBoard);
            int addNewRandomCellDuration = 50;
            for (int row = 0; row < currentGameMode.getRows(); row++) {
                for (int column = 0; column < currentGameMode.getColumns(); column++) {
                    if (!copyOfCurrentBoard.get(row).get(column).equals(gameManager.getGameMatrix().get(row).get(column))) {
                        GridLayout gameGridLayout = findViewById(R.id.game_grid_layout);
                        AppCompatTextView textView = gameGridLayout.findViewWithTag("gameCell" + row + column);
                        CellValues cellValueEnum = CellValues.getCellValueEnum(copyOfCurrentBoard.get(row).get(column));
                        AnimationsUtility.executePopUpAnimation(textView, cellValueEnum.getCellValue(),
                                getColor(cellValueEnum.getNumberColorResourceId()),
                                getDrawable(cellValueEnum.getBackgroundDrawableResourceId()),
                                addNewRandomCellDuration, 500, currentGameMode.getGameLayoutProperties());
                    }
                }
            }
            gameManager.updateGameMatrix(copyOfCurrentBoard);
        }

        // Update the reduced number of coins
        currentCoins -= toolsCostMap.get("specialToolsEliminateValueCost");
        updateCoins(currentCoins);

        // Final set of actions
            // Update the count of tool use
        if (gameManager.getAchievementsManager().incrementEliminateValueToolUseCount()) {
            gameManager.getLeaderboardsClient().submitScoreImmediate(getString(R.string.leaderboard_eliminate_value_tool_masters),
                    gameManager.getAchievementsManager().getEliminateValueToolCurrentUseCount())
                .addOnCompleteListener(new OnCompleteListener<ScoreSubmissionData>() {
                    @Override
                    public void onComplete(@NonNull Task<ScoreSubmissionData> task) {
                        if (task.isSuccessful()) {
                            // Score was submitted to the leaderboard
                        } else {
                            // Error submitting the score, so we use submitScore() method
                            gameManager.getLeaderboardsClient().submitScore(getString(R.string.leaderboard_eliminate_value_tool_masters),
                                    gameManager.getAchievementsManager().getEliminateValueToolCurrentUseCount());
                        }
                    }
                });
        }
        saveGameState(false);
        handleGoalCompletionStatus();
        onBackPressed();
    }

    @Override
    public void onDestroyAreaFragmentInteractionBackClicked() {
        onBackPressed();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onDestroyAreaFragmentInteractionProcessToolUse(List<Pair<Integer, Integer>> destroyAreaTilesPositions) {
        // Making a copy of the board
        List<List<Long>> copyOfCurrentBoard = new ArrayList<>();
        for (int i = 0; i < gameManager.getGameMatrix().size(); i++) {
            List<Long> boardRow = new ArrayList<>(gameManager.getGameMatrix().get(i));
            copyOfCurrentBoard.add(boardRow);
        }

        // Removing the chosen tile from the board
        for (int index = 0; index < destroyAreaTilesPositions.size(); index++) {
            copyOfCurrentBoard.get(destroyAreaTilesPositions.get(index).first)
                    .set(destroyAreaTilesPositions.get(index).second, 0L);
            AppCompatTextView textView = rootGameConstraintLayout.findViewWithTag("gameCell" +
                    destroyAreaTilesPositions.get(index).first + destroyAreaTilesPositions.get(index).second);
            textView.setVisibility(View.INVISIBLE);
        }
        gameManager.updateGameMatrix(copyOfCurrentBoard);

        // Checking if the board has game tiles, or do we need to insert some random values
        if (gameManager.findGameTilesCurrentlyOnBoard(copyOfCurrentBoard) == 0) {
            gameManager.addNewValues(2, copyOfCurrentBoard);
            int addNewRandomCellDuration = 50;
            for (int row = 0; row < currentGameMode.getRows(); row++) {
                for (int column = 0; column < currentGameMode.getColumns(); column++) {
                    if (!copyOfCurrentBoard.get(row).get(column).equals(gameManager.getGameMatrix().get(row).get(column))) {
                        GridLayout gameGridLayout = findViewById(R.id.game_grid_layout);
                        AppCompatTextView textView = gameGridLayout.findViewWithTag("gameCell" + row + column);
                        CellValues cellValueEnum = CellValues.getCellValueEnum(copyOfCurrentBoard.get(row).get(column));
                        AnimationsUtility.executePopUpAnimation(textView, cellValueEnum.getCellValue(),
                                getColor(cellValueEnum.getNumberColorResourceId()),
                                getDrawable(cellValueEnum.getBackgroundDrawableResourceId()),
                                addNewRandomCellDuration, 500, currentGameMode.getGameLayoutProperties());
                    }
                }
            }
            gameManager.updateGameMatrix(copyOfCurrentBoard);
        }

        // Update the reduced number of coins
        currentCoins -= toolsCostMap.get("specialToolsDestroyAreaCost");
        updateCoins(currentCoins);

        // Final set of actions
            // Update the count of tool use
        if (gameManager.getAchievementsManager().incrementDestroyAreaToolUseCount()) {
            gameManager.getLeaderboardsClient().submitScoreImmediate(getString(R.string.leaderboard_destroy_area_tool_masters),
                    gameManager.getAchievementsManager().getDestroyAreaToolCurrentUseCount())
                .addOnCompleteListener(new OnCompleteListener<ScoreSubmissionData>() {
                    @Override
                    public void onComplete(@NonNull Task<ScoreSubmissionData> task) {
                        if (task.isSuccessful()) {
                            // Score was submitted to the leaderboard
                        } else {
                            // Error submitting the score, so we use submitScore() method
                            gameManager.getLeaderboardsClient().submitScore(getString(R.string.leaderboard_destroy_area_tool_masters),
                                    gameManager.getAchievementsManager().getDestroyAreaToolCurrentUseCount());
                        }
                    }
                });
        }
        saveGameState(false);
        handleGoalCompletionStatus();
        onBackPressed();
    }

    private void handleToolFragmentBackClicked(boolean isToolUseInProgress) {
        if (isToolUseInProgress) { // If tool use is in progress then we want to ignore the back button click
            return;
        }

        backgroundFilmImageView.setImageResource(0); // Setting image resource to blank
        rootGameConstraintLayout.setEnabled(true);
        gameFrameLayout.removeView(findViewById(R.id.game_cell_lottie_layout));
    }
}