package com.example.gameside2048champs;

import static java.lang.Character.toChars;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.example.gameside2048champs.dialogs.ArrivingFeatureDialog;
import com.example.gameside2048champs.dialogs.ArrivingToolDialog;
import com.example.gameside2048champs.dialogs.GameOverDialog;
import com.example.gameside2048champs.dialogs.GamePausedDialog;
import com.example.gameside2048champs.dialogs.GameResetDialog;
import com.example.gameside2048champs.dialogs.ToolUseProhibitedDialog;
import com.example.gameside2048champs.dialogs.GameWinDialog;
import com.example.gameside2048champs.enums.CellValues;
import com.example.gameside2048champs.enums.Direction;
import com.example.gameside2048champs.enums.GameModes;
import com.example.gameside2048champs.enums.GameOverDialogOptions;
import com.example.gameside2048champs.enums.GameStates;
import com.example.gameside2048champs.fragments.BombFragment;
import com.example.gameside2048champs.fragments.ChangeValueFragment;
import com.example.gameside2048champs.fragments.EliminateValueFragment;
import com.example.gameside2048champs.fragments.ShopFragment;
import com.example.gameside2048champs.fragments.SmashTileFragment;
import com.example.gameside2048champs.fragments.SwapTilesFragment;
import com.example.gameside2048champs.manager.GameManager;
import com.example.gameside2048champs.manager.UndoManager;
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
        SmashTileFragment.OnSmashTileFragmentInteractionListener,
        ChangeValueFragment.OnChangeValueFragmentInteractionListener,
        SwapTilesFragment.OnSwapTilesFragmentInteractionListener,
        EliminateValueFragment.OnEliminateValueFragmentInteractionListener,
        BombFragment.OnBombFragmentInteractionListener {
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
    private int currentScore;
    private int bestScore;
    private boolean isCurrentScoreTheBest; // Flag to check if best score and current score displays have been merged
    private boolean isToolsChestOpen;

    // UI Elements
    /* Layouts */
    private ConstraintLayout rootGameConstraintLayout;
    private FrameLayout gameFrameLayout;
    private LinearLayout normalToolsLinearLayout;
    private LinearLayout specialToolsLinearLayout;
    private LinearLayout toolsLottieLinearLayout;
    /* Views */
    private AppCompatImageView backgroundFilmImageView;
    private AppCompatTextView currentCoinsTextView;
    private AppCompatTextView currentScoreTextView;
    private AppCompatTextView bestScoreTextView;
    private AppCompatTextView goalTileTextView;
    private LottieAnimationView toolsChangeLottie;
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
        currentCoins = sharedPreferences.getInt("currentCoins", 2000);
        toolsCostMap = new HashMap<>() {{
            put("normalToolsUndoCost", 125);
            put("normalToolsSmashTileCost", 150);
            put("normalToolsChangeValueCost", 200);
            put("specialToolsSwapTilesCost", 400);
            put("specialToolsEliminateValueCost", 450);
            put("specialToolsBombCost", 500);
        }};
        currentScore = sharedPreferences.getInt("currentScore" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), 0);
        bestScore = sharedPreferences.getInt("bestScore" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), 0);
        isCurrentScoreTheBest = false;
        isToolsChestOpen = sharedPreferences.getBoolean("isToolsChestOpen" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), false);
    }

    private void initialiseLayouts() {
        rootGameConstraintLayout = findViewById(R.id.root_game_constraint_layout);
        gameFrameLayout = findViewById(R.id.game_frame_layout);
        normalToolsLinearLayout = findViewById(R.id.normal_tools_linear_layout);
        specialToolsLinearLayout = findViewById(R.id.special_tools_linear_layout);
        toolsLottieLinearLayout = findViewById(R.id.tools_lottie_linear_layout);

        if (!isToolsChestOpen) { // Tools chest is NOT open (This is the default condition as well)
            normalToolsLinearLayout.setVisibility(View.VISIBLE);
            specialToolsLinearLayout.setVisibility(View.GONE);
        } else { // Tools chest is open
            specialToolsLinearLayout.setVisibility(View.VISIBLE);
            normalToolsLinearLayout.setVisibility(View.GONE);
        }
    }

    private void initialiseViews() {
        backgroundFilmImageView = findViewById(R.id.background_film_game_activity_image_view);
        currentCoinsTextView = findViewById(R.id.current_coins_game_activity_text_view);
        currentCoinsTextView.setText(String.valueOf(currentCoins));

        currentScoreTextView = findViewById(R.id.current_score_value_text_view);
        currentScoreTextView.setText(String.valueOf(currentScore));
        gameManager.setCurrentScore(currentScore);
        gameManager.setHasGoalBeenCompleted(goalDone);

        String jsonRetrieveBoard = sharedPreferences.getString("currentBoard" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), gson.toJson(gameManager.getGameMatrix()));
        Type typeBoard = new TypeToken<List<List<Integer>>>(){}.getType();
        gameManager.setGameMatrix(gson.fromJson(jsonRetrieveBoard, typeBoard));

        String jsonRetrieveUndoManager = sharedPreferences.getString("undoManager" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), gson.toJson(gameManager.getUndoManager()));
        Type typeUndoManager = new TypeToken<UndoManager>(){}.getType();
        gameManager.setUndoManager(gson.fromJson(jsonRetrieveUndoManager, typeUndoManager));

        bestScoreTextView = findViewById(R.id.best_score_value_text_view);
        bestScoreTextView.setText(String.valueOf(bestScore));
        goalTileTextView = findViewById(R.id.goal_tile_text_view);
        if (gameManager.getCurrentGameState() == GameStates.GAME_OVER) {
            updateScore(0);
        } else {
            updateScore(currentScore);
        }

        toolsChangeLottie = findViewById(R.id.tools_change_lottie);
        toolsChangeLottie.setProgress((!isToolsChestOpen) ? 0f : 1f);
        toolsChangeLottie.setOnClickListener(view -> handleToolsChangeTransition());

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

        AppCompatTextView normalToolsUndoCostTextView =
                findViewById(R.id.normal_tools_undo_cost_text_view);
        normalToolsUndoCostTextView.setText(String.valueOf(toolsCostMap.get("normalToolsUndoCost")));
        AppCompatTextView normalToolsSmashTileCostTextView =
                findViewById(R.id.normal_tools_smash_cost_text_view);
        normalToolsSmashTileCostTextView.setText(String.valueOf(toolsCostMap.get("normalToolsSmashTileCost")));
        AppCompatTextView normalToolsChangeValueCostTextView =
                findViewById(R.id.normal_tools_change_value_cost_text_view);
        normalToolsChangeValueCostTextView.setText(String.valueOf(toolsCostMap.get("normalToolsChangeValueCost")));
        AppCompatTextView specialToolsSwapTilesCostTextView =
                findViewById(R.id.special_tools_swap_tiles_cost_text_view);
        specialToolsSwapTilesCostTextView.setText(String.valueOf(toolsCostMap.get("specialToolsSwapTilesCost")));
        AppCompatTextView specialToolsEliminateValueCostTextView =
                findViewById(R.id.special_tools_eliminate_value_cost_text_view);
        specialToolsEliminateValueCostTextView.setText(String.valueOf(toolsCostMap.get("specialToolsEliminateValueCost")));
        AppCompatTextView specialToolsBombCostTextView =
                findViewById(R.id.special_tools_bomb_cost_text_view);
        specialToolsBombCostTextView.setText(String.valueOf(toolsCostMap.get("specialToolsBombCost")));
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
                    // If the user does not respond we will start a new game
                    // from our side
                    resetGameAndStartIfFlagTrue(true);
                }
            }
        });
    }

    private void executeMove() {
        // Return if some previous move was not completed
        if (!gameManager.isHasMoveBeenCompleted()) {
            return;
        }

        // Return if currently the user is using a tool
        if (getSupportFragmentManager().findFragmentByTag("SMASH_TILE_FRAGMENT") != null
                || getSupportFragmentManager().findFragmentByTag("CHANGE_VALUE_FRAGMENT") != null
                || getSupportFragmentManager().findFragmentByTag("SWAP_TILES_FRAGMENT") != null
                || getSupportFragmentManager().findFragmentByTag("ELIMINATE_VALUE_FRAGMENT") != null
                || getSupportFragmentManager().findFragmentByTag("BOMB_FRAGMENT") != null) {
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
                        if (gameManager.isHasGoalBeenCompleted() && !goalDone) {
                            goalDone = true;
                            int greenTickEmojiUnicode = 0x2705;
                            goalTileTextView.setText(String.format("GOAL TILE %s",
                                    String.valueOf(toChars(greenTickEmojiUnicode))));
                            tutorialTextView.setText("Merge for higher tiles, SKY IS THE LIMIT");
                            sharedPreferences.edit().putBoolean("goalDone" + " " + currentGameMode.getMode()
                                    + " " + currentGameMode.getDimensions(), goalDone).apply();
                            movesQueue.clear();
                            new GameWinDialog(GameActivity.this).show();
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

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onResume() {
        super.onResume();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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
        currentCoinsTextView.setText(String.valueOf(this.currentCoins));

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
    }

    private void updateScore(int updatedCurrentScore) {
        currentScore = updatedCurrentScore;
        currentScoreTextView.setText(String.valueOf(currentScore));

        if ((currentScore >= bestScore) && currentScore > 0) {
            bestScore = currentScore;
            bestScoreTextView.setText(String.valueOf(bestScore));
            sharedPreferences.edit().putInt("bestScore" + " " + currentGameMode.getMode()
                    + " " + currentGameMode.getDimensions(), bestScore).apply();
            if (!isCurrentScoreTheBest) {
                isCurrentScoreTheBest = true;
            }
        } else {
            if (isCurrentScoreTheBest) {
                isCurrentScoreTheBest = false;
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
        sharedPreferences.edit().putInt("gameStateEnumIndex" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), gameManager.getCurrentGameState().ordinal()).apply();
        sharedPreferences.edit().putInt("currentScore" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), currentScore).apply();
        sharedPreferences.edit().putString("undoManager" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), gson.toJson(gameManager.getUndoManager())).apply();
        sharedPreferences.edit().putBoolean("goalDone" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), goalDone).apply();
        sharedPreferences.edit().putBoolean("isToolsChestOpen" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), isToolsChestOpen).apply();
        if (gameManager.getCurrentGameState() == GameStates.GAME_START) {
            sharedPreferences.edit().putString("currentBoard" + " " + currentGameMode.getMode()
                    + " " + currentGameMode.getDimensions(), gson.toJson(getCopyOfGivenBoard(currentGameMode.getBlockCells()))).apply();
        } else {
            sharedPreferences.edit().putString("currentBoard" + " " + currentGameMode.getMode()
                    + " " + currentGameMode.getDimensions(), gson.toJson(gameManager.getGameMatrix())).apply();
        }
    }

    public void resetGameAndStartIfFlagTrue(boolean flag) {
        updateScore(0);
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
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) { // Back button was pressed from activity
            setupGamePausedDialog();
        } else { // Back button was pressed from fragment
            boolean isGameOverCheckRequired = false;
            int countOfFragments = getSupportFragmentManager().getFragments().size();
            if (countOfFragments > 0) {
                Fragment topMostFragment = getSupportFragmentManager().getFragments().get(countOfFragments - 1);
                if (topMostFragment != null && topMostFragment.getTag() != null && !topMostFragment.getTag().isEmpty()) {
                    if (topMostFragment.getTag().equals("SMASH_TILE_FRAGMENT")
                            || topMostFragment.getTag().equals("CHANGE_VALUE_FRAGMENT")
                            || topMostFragment.getTag().equals("SWAP_TILES_FRAGMENT")
                            || topMostFragment.getTag().equals("ELIMINATE_VALUE_FRAGMENT")
                            || topMostFragment.getTag().equals("BOMB_FRAGMENT")) {
                        handleToolFragmentBackClicked();
                        // For some of the tool fragments some more processing maybe required, which is as follows
                        if (topMostFragment.getTag().equals("CHANGE_VALUE_FRAGMENT")
                                || topMostFragment.getTag().equals("SMASH_TILE_FRAGMENT")) {
                            // Need to check if after the swap move is game going on OR is it game over
                            isGameOverCheckRequired = true; // Game over check will be performed later using this flag
                        }
                    }
                }
            }
            getSupportFragmentManager().popBackStack();

            if (isGameOverCheckRequired) {
                gameManager.updateGameState();
                if (gameManager.getCurrentGameState() == GameStates.GAME_OVER) {
                    handleGameOverProcess();
                }
            }
        }
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

    private void updateScoreOnUndo(int updatedCurrentScore) {
        currentScore = updatedCurrentScore;
        currentScoreTextView.setText(String.valueOf(currentScore));

        // Making a check if the current score and the best scores need to be split or not
        if (currentScore < bestScore) {
            if (isCurrentScoreTheBest) {
                isCurrentScoreTheBest = false;
            }
        }

        // Making a check if the goal completion is still intact or not
        for (int row = 0; row < currentGameMode.getRows(); row++) {
            for (int column = 0; column < currentGameMode.getColumns(); column++) {
                int value = gameManager.getGameMatrix().get(row).get(column);
                if (value >= gameManager.getCurrentGameMode().getGoal()) {
                    tutorialTextView.setText("Merge for higher tiles, SKY IS THE LIMIT");
                    return;
                }
            }
        }
        goalDone = false;
        goalTileTextView.setText("GOAL TILE");
        tutorialTextView.setText("Merge the tiles to form the GOAL TILE!");
        sharedPreferences.edit().putBoolean("goalDone" + " " + currentGameMode.getMode()
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
                toolsChangeLottie.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isToolsChestOpen) {
                    normalToolsLinearLayout.setVisibility(View.VISIBLE);
                    specialToolsLinearLayout.setVisibility(View.GONE);
                    toolsChangeLottie.setClickable(true);
                } else {
                    specialToolsLinearLayout.setVisibility(View.VISIBLE);
                    normalToolsLinearLayout.setVisibility(View.GONE);

                    LottieAnimationView leftView = toolsLottieLinearLayout.findViewById(R.id.tools_lottie_left);
                    LottieAnimationView midView = toolsLottieLinearLayout.findViewById(R.id.tools_lottie_middle);
                    LottieAnimationView rightView = toolsLottieLinearLayout.findViewById(R.id.tools_lottie_right);
                    toolsLottieLinearLayout.setVisibility(View.VISIBLE);
                    midView.addAnimatorListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {}
                        @Override
                        public void onAnimationEnd(Animator animator) {
                            toolsChangeLottie.setClickable(true);
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
        sharedPreferences.edit().putBoolean("isToolsChestOpen" + " " + currentGameMode.getMode()
                + " " + currentGameMode.getDimensions(), isToolsChestOpen).apply();
    }

    public void normalToolsUndo(View view) {
        undoProcess();
    }

    public void normalToolsSmashTile(View view) {
        smashTileProcess();
    }

    public void normalToolsChangeValue(View view) {
        changeValueProcess();
    }

    public void specialToolsSwapTiles(View view) {
        swapTilesProcess();
    }

    public void specialToolsEliminateValue(View view) {
        eliminateValueProcess();
    }

    public void specialToolsBomb(View view) {
        new ArrivingToolDialog(this).show();
        /* TODO -> Implement the Bomb tool and uncomment the following line */
        //bombProcess();
    }

    private void undoProcess() {
        movesQueue.clear();
        if (!gameManager.getUndoManager().isUndoUsed()) { // Undo was not used, so using it now
            if (currentCoins >= toolsCostMap.get("normalToolsUndoCost")) {
                AnimationUtility.normalToolsUndo(gridLottieView, rootGameConstraintLayout);
                new CountDownTimer(1000, 10000) {
                    @Override
                    public void onTick(long l) {
                    }

                    @Override
                    public void onFinish() {
                        gameManager.setCurrentGameState(GameStates.GAME_ONGOING);
                        Pair<Integer, List<List<Integer>>> previousStateInfo = gameManager.getUndoManager().undoToPreviousState();
                        // Revert the state of the board to the previous state
                        gameManager.updateGameMatrixPostUndo(previousStateInfo.second);
                        updateBoardOnUndo();
                        // Revert score to previous state score
                        gameManager.setCurrentScore(previousStateInfo.first);
                        updateScoreOnUndo(gameManager.getCurrentScore());
                        // Update the reduced number of coins
                        currentCoins -= toolsCostMap.get("normalToolsUndoCost");
                        sharedPreferences.edit().putInt("currentCoins", currentCoins).apply();
                        currentCoinsTextView.setText(String.valueOf(currentCoins));
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
                int row = i, column = j, cellValue = gameManager.getGameMatrix().get(row).get(column);
                lottieAnimationView.setOnClickListener(view -> {
                    int countOfFragments = getSupportFragmentManager().getFragments().size();
                    if (countOfFragments > 0) {
                        Fragment topMostFragment = getSupportFragmentManager().getFragments().get(countOfFragments - 1);
                        if (topMostFragment != null && topMostFragment.getTag() != null &&
                                !topMostFragment.getTag().isEmpty()) {
                            if (topMostFragment.getTag().equals("SMASH_TILE_FRAGMENT")) {
                                if (cellValue != 0 && cellValue != -1) {
                                    SmashTileFragment smashTileFragment = ((SmashTileFragment) topMostFragment);
                                    if (!smashTileFragment.checkToolUseState()) { // Tool use is not complete
                                        rootGameConstraintLayout.setEnabled(false);
                                        smashTileFragment.handleTileToBeSmashed(findViewById(R.id.game_cell_lottie_layout),
                                                lottieAnimationView, gridLottieView, new Pair<>(row, column));
                                    } // else, user has clicked after choosing tile to smash, so we ignore the click
                                }
                            } else if (topMostFragment.getTag().equals("CHANGE_VALUE_FRAGMENT")) {
                                if (cellValue != 0 && cellValue != -1) {
                                    ChangeValueFragment changeValueFragment = ((ChangeValueFragment) topMostFragment);
                                    if (changeValueFragment.checkFirstClickStatus()
                                            && changeValueFragment.checkSecondClickStatus()) {
                                        // User has clicked at some time else which is not valid for this tool use
                                    } else { // Tool use is not complete
                                        if (!changeValueFragment.checkFirstClickStatus()) { // First click is yet to be done
                                            rootGameConstraintLayout.setEnabled(false);
                                            changeValueFragment.handleChangeValueToolFirstClick(lottieAnimationView,
                                                    new Pair<>(row, column));
                                        } else { // Time to execute the 2nd click as follows
                                            changeValueFragment.handleChangeValueToolSecondClick();
                                        }
                                    }
                                }
                            } else if (topMostFragment.getTag().equals("SWAP_TILES_FRAGMENT")) {
                                if (cellValue != 0 && cellValue != -1) {
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
                            } else if (topMostFragment.getTag().equals("ELIMINATE_VALUE_FRAGMENT")) {
                                if (cellValue != 0 && cellValue != -1) {
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
                            } else if (topMostFragment.getTag().equals("BOMB_FRAGMENT")) {

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

        if (currentCoins >= toolsCostMap.get("normalToolsSmashTileCost")) {
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
            AnimationUtility.toolsBackgroundAppearAnimation(backgroundFilmImageView, 300);
            SmashTileFragment fragment = new SmashTileFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.tools_fragment_entry, R.anim.tools_fragment_exit,
                    R.anim.tools_fragment_entry, R.anim.tools_fragment_exit);
            transaction.addToBackStack(null);
            transaction.replace(R.id.tool_use_game_activity_fragment_container,
                    fragment, "SMASH_TILE_FRAGMENT").commit();
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

        if (currentCoins >= toolsCostMap.get("normalToolsChangeValueCost")) {
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
            AnimationUtility.toolsBackgroundAppearAnimation(backgroundFilmImageView, 300);
            ChangeValueFragment fragment = new ChangeValueFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.tools_fragment_entry, R.anim.tools_fragment_exit,
                    R.anim.tools_fragment_entry, R.anim.tools_fragment_exit);
            transaction.addToBackStack(null);
            transaction.replace(R.id.tool_use_game_activity_fragment_container,
                    fragment, "CHANGE_VALUE_FRAGMENT").commit();
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

        if (currentCoins >= toolsCostMap.get("specialToolsSwapTilesCost")) {
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
            AnimationUtility.toolsBackgroundAppearAnimation(backgroundFilmImageView, 300);
            SwapTilesFragment fragment = new SwapTilesFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.tools_fragment_entry, R.anim.tools_fragment_exit,
                    R.anim.tools_fragment_entry, R.anim.tools_fragment_exit);
            transaction.addToBackStack(null);
            transaction.replace(R.id.tool_use_game_activity_fragment_container,
                    fragment, "SWAP_TILES_FRAGMENT").commit();
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
            AnimationUtility.toolsBackgroundAppearAnimation(backgroundFilmImageView, 300);
            EliminateValueFragment fragment = new EliminateValueFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.tools_fragment_entry, R.anim.tools_fragment_exit,
                    R.anim.tools_fragment_entry, R.anim.tools_fragment_exit);
            transaction.addToBackStack(null);
            transaction.replace(R.id.tool_use_game_activity_fragment_container,
                    fragment, "ELIMINATE_VALUE_FRAGMENT").commit();
        } else {
            openShopFragment();
        }
    }

    private void bombProcess() {
        movesQueue.clear();
        if (gameManager.findGameTilesCurrentlyOnBoard(gameManager.getGameMatrix()) < 1) {
            String bombMessage = "Atleast 1 game tile is required to use the \"DESTROY AREA\" tool";
            new ToolUseProhibitedDialog(this, bombMessage).show();
            return;
        }

        if (currentCoins >= toolsCostMap.get("specialToolsBombCost")) {
            // If BombFragment was opened and is currently on top, then return
            int countOfFragments = getSupportFragmentManager().getFragments().size();
            if (countOfFragments > 0) {
                Fragment topMostFragment = getSupportFragmentManager().getFragments().get(countOfFragments - 1);
                if (topMostFragment != null && topMostFragment.getTag() != null && !topMostFragment.getTag().isEmpty()
                        && topMostFragment.getTag().equals("BOMB_FRAGMENT")) {
                    return;
                }
            }

            // Add a layer of individual lottie cells to the game board
            addTempIndividualCellLottieLayer();

            // Initiate the tool entry transition
            AnimationUtility.toolsBackgroundAppearAnimation(backgroundFilmImageView, 300);
            BombFragment fragment = new BombFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.tools_fragment_entry, R.anim.tools_fragment_exit,
                    R.anim.tools_fragment_entry, R.anim.tools_fragment_exit);
            transaction.addToBackStack(null);
            transaction.replace(R.id.tool_use_game_activity_fragment_container,
                    fragment, "BOMB_FRAGMENT").commit();
        } else {
            openShopFragment();
        }
    }

    @Override
    public void onShopFragmentInteractionBackClicked() {
        onBackPressed();
        if (gameManager.getCurrentGameState() == GameStates.GAME_OVER) {
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
                        // If the user does not respond we will start a new game from our side
                        resetGameAndStartIfFlagTrue(true);
                    }
                }
            });
        }
    }

    @Override
    public void onShopFragmentInteractionRestorePurchaseClicked() {
        Toast.makeText(this, "Restore Purchase Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShopFragmentInteractionUpdateCoins(int currentCoins) {
        updateCoins(currentCoins);
    }

    @Override
    public void onSmashTileFragmentInteractionBackClicked() {
        onBackPressed();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void onSmashTileFragmentInteractionProcessToolUse(Pair<Integer, Integer> targetTilePosition) {
        // Making a copy of the board
        List<List<Integer>> copyOfCurrentBoard = new ArrayList<>();
        for (int i = 0; i < gameManager.getGameMatrix().size(); i++) {
            List<Integer> boardRow = new ArrayList<>(gameManager.getGameMatrix().get(i));
            copyOfCurrentBoard.add(boardRow);
        }

        // Removing the chosen tile from the board
        copyOfCurrentBoard.get(targetTilePosition.first).set(targetTilePosition.second, 0);
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
                        cellValueEnum.setCellValue(copyOfCurrentBoard.get(row).get(column));
                        AnimationUtility.executePopUpAnimation(textView, cellValueEnum.getCellValue(),
                                getResources().getColor(cellValueEnum.getNumberColorResourceId()),
                                getDrawable(cellValueEnum.getBackgroundDrawableResourceId()),
                                addNewRandomCellDuration, 500, currentGameMode.getGameLayoutProperties());
                    }
                }
            }
            gameManager.updateGameMatrix(copyOfCurrentBoard);
        }

        // Update the reduced number of coins
        currentCoins -= toolsCostMap.get("normalToolsSmashTileCost");
        sharedPreferences.edit().putInt("currentCoins", currentCoins).apply();
        currentCoinsTextView.setText(String.valueOf(currentCoins));

        // Final set of actions
        saveGameState();
        onBackPressed();
    }

    @Override
    public void onChangeValueFragmentInteractionBackClicked() {
        onBackPressed();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onChangeValueFragmentInteractionProcessToolUse(Pair<Integer, Integer> changeValueTilePosition,
        int newValue) {
        // Making a copy of the board
        List<List<Integer>> copyOfCurrentBoard = new ArrayList<>();
        for (int i = 0; i < gameManager.getGameMatrix().size(); i++) {
            List<Integer> boardRow = new ArrayList<>(gameManager.getGameMatrix().get(i));
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
        cellValueEnumFirstPosition.setCellValue(newValue);
            // Executing the pop up animation for the chosen tile to change value
        AnimationUtility.executePopUpAnimation(changeValueTilePositionTextView, cellValueEnumFirstPosition.getCellValue(),
                getResources().getColor(cellValueEnumFirstPosition.getNumberColorResourceId()),
                getDrawable(cellValueEnumFirstPosition.getBackgroundDrawableResourceId()),
                popUpAnimationDuration, 0, currentGameMode.getGameLayoutProperties());
            // Updating the game matrix
        gameManager.updateGameMatrix(copyOfCurrentBoard);

        // Update the reduced number of coins
        currentCoins -= toolsCostMap.get("specialToolsSwapTilesCost");
        sharedPreferences.edit().putInt("currentCoins", currentCoins).apply();
        currentCoinsTextView.setText(String.valueOf(currentCoins));

        // Final set of actions
        saveGameState();
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
        List<List<Integer>> copyOfCurrentBoard = new ArrayList<>();
        for (int i = 0; i < gameManager.getGameMatrix().size(); i++) {
            List<Integer> boardRow = new ArrayList<>(gameManager.getGameMatrix().get(i));
            copyOfCurrentBoard.add(boardRow);
        }

        /* Swapping the chosen tiles on the board */
            // Settings values in copyOfCurrentBoard
        int firstValue = gameManager.getGameMatrix().get(firstSwapTilePosition.first).get(firstSwapTilePosition.second);
        int secondValue = gameManager.getGameMatrix().get(secondSwapTilePosition.first).get(secondSwapTilePosition.second);
        copyOfCurrentBoard.get(firstSwapTilePosition.first).set(firstSwapTilePosition.second, secondValue);
        copyOfCurrentBoard.get(secondSwapTilePosition.first).set(secondSwapTilePosition.second, firstValue);
        int popUpAnimationDuration = 250; // In Milli-seconds
            // Giving new look to first swap position
        AppCompatTextView firstPositionTextView = rootGameConstraintLayout.findViewWithTag("gameCell" +
                firstSwapTilePosition.first + firstSwapTilePosition.second);
        CellValues cellValueEnumFirstPosition = CellValues.getCellValueEnum(secondValue);
        cellValueEnumFirstPosition.setCellValue(secondValue);
            // Giving new look to second swap position
        AppCompatTextView secondPositionTextView = rootGameConstraintLayout.findViewWithTag("gameCell" +
                secondSwapTilePosition.first + secondSwapTilePosition.second);
        CellValues cellValueEnumSecondPosition = CellValues.getCellValueEnum(firstValue);
        cellValueEnumSecondPosition.setCellValue(firstValue);
            // Executing the pop up animation for both cell one after the other
        AnimationUtility.executePopUpAnimation(firstPositionTextView, cellValueEnumFirstPosition.getCellValue(),
                getResources().getColor(cellValueEnumFirstPosition.getNumberColorResourceId()),
                getDrawable(cellValueEnumFirstPosition.getBackgroundDrawableResourceId()),
                popUpAnimationDuration, 0, currentGameMode.getGameLayoutProperties());
        AnimationUtility.executePopUpAnimation(secondPositionTextView, cellValueEnumSecondPosition.getCellValue(),
                getResources().getColor(cellValueEnumSecondPosition.getNumberColorResourceId()),
                getDrawable(cellValueEnumSecondPosition.getBackgroundDrawableResourceId()),
                popUpAnimationDuration, 0, currentGameMode.getGameLayoutProperties());
            // Updating the game matrix
        gameManager.updateGameMatrix(copyOfCurrentBoard);

        // Update the reduced number of coins
        currentCoins -= toolsCostMap.get("specialToolsSwapTilesCost");
        sharedPreferences.edit().putInt("currentCoins", currentCoins).apply();
        currentCoinsTextView.setText(String.valueOf(currentCoins));

        // Final set of actions
        saveGameState();
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
        List<List<Integer>> copyOfCurrentBoard = new ArrayList<>();
        for (int i = 0; i < gameManager.getGameMatrix().size(); i++) {
            List<Integer> boardRow = new ArrayList<>(gameManager.getGameMatrix().get(i));
            copyOfCurrentBoard.add(boardRow);
        }

        // Removing the chosen tile from the board
        for (int index = 0; index < targetTilesPositions.size(); index++) {
            copyOfCurrentBoard.get(targetTilesPositions.get(index).first)
                    .set(targetTilesPositions.get(index).second, 0);
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
                        cellValueEnum.setCellValue(copyOfCurrentBoard.get(row).get(column));
                        AnimationUtility.executePopUpAnimation(textView, cellValueEnum.getCellValue(),
                                getResources().getColor(cellValueEnum.getNumberColorResourceId()),
                                getDrawable(cellValueEnum.getBackgroundDrawableResourceId()),
                                addNewRandomCellDuration, 500, currentGameMode.getGameLayoutProperties());
                    }
                }
            }
            gameManager.updateGameMatrix(copyOfCurrentBoard);
        }

        // Update the reduced number of coins
        currentCoins -= toolsCostMap.get("specialToolsEliminateValueCost");
        sharedPreferences.edit().putInt("currentCoins", currentCoins).apply();
        currentCoinsTextView.setText(String.valueOf(currentCoins));

        // Final set of actions
        saveGameState();
        onBackPressed();
    }

    @Override
    public void onBombFragmentInteractionBackClicked() {
        onBackPressed();
    }

    private void handleToolFragmentBackClicked() {
        backgroundFilmImageView.setImageResource(0); // Setting image resource to blank
        rootGameConstraintLayout.setEnabled(true);
        gameFrameLayout.removeView(findViewById(R.id.game_cell_lottie_layout));
    }
}