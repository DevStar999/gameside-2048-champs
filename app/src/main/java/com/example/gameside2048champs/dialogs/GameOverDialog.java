package com.example.gameside2048champs.dialogs;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.example.gameside2048champs.NumericValueDisplay;
import com.example.gameside2048champs.R;
import com.example.gameside2048champs.enums.GameOverDialogActivePage;
import com.example.gameside2048champs.enums.GameOverDialogOptions;

import java.util.HashMap;
import java.util.Map;

public class GameOverDialog extends Dialog {
    private boolean didUserGiveResponse;
    private GameOverDialogActivePage activePage;
    private GameOverDialogOptions optionSelected;
    private GameOverDialogListener gameOverDialogListener;
    private LinearLayout rootLinearLayoutFirstPage; // REVIVE_TOOLS_PAGE
    private LinearLayout rootLinearLayoutSecondPage; // GAME_SUMMARY_PAGE

    /* Attributes of the GameOverDialog related to the REVIVE_TOOLS_PAGE */
    private boolean isToolsChestOpen;
    private LottieAnimationView toolsChangeLottie;
    private Map<String, Integer> toolsCostMap;
    private LinearLayout standardToolsLinearLayout;
    private LinearLayout specialToolsLinearLayout;
    private LinearLayout toolsLottieLinearLayout;
    private AppCompatImageView standardToolsUndoImageView;
    private AppCompatImageView standardToolsSmashTileImageView;
    private AppCompatImageView standardToolsSwapTilesImageView;
    private AppCompatImageView specialToolsChangeValueImageView;
    private AppCompatImageView specialToolsEliminateValueImageView;
    private AppCompatImageView specialToolsDestroyAreaImageView;
    private AppCompatButton shopCoinsButton;
    private AppCompatButton continueButton;

    /* Attributes of the GameOverDialog related to the GAME_SUMMARY_PAGE */
    private long currentScore;
    private long bestScore;
    private LottieAnimationView gameOverLottie;
    private LinearLayout newHighScoreSummaryLinearLayout;
    private AppCompatTextView newHighScoreTextView;
    private ConstraintLayout scoresSummaryConstraintLayout;
    private AppCompatTextView currentScoreTextView;
    private AppCompatTextView bestScoreTextView;
    private AppCompatButton mainMenuButton;
    private AppCompatButton playAgainButton;
    private AppCompatButton backButton;

    /* TODO -> Later, use SharedPreferences to decide which tools to keep open based on the state of the tools
               chest in the game.
    */
    private void initialise(long currentScore, long bestScore) {
        didUserGiveResponse = false;
        activePage = GameOverDialogActivePage.REVIVE_TOOLS_PAGE; // Since by default we open this page
        optionSelected = GameOverDialogOptions.PLAY_AGAIN; // As we want to start the game again if the user does not choose
        rootLinearLayoutFirstPage = findViewById(R.id.root_first_page_game_over_dialog_linear_layout);
        rootLinearLayoutSecondPage = findViewById(R.id.root_second_page_game_over_dialog_linear_layout);

        isToolsChestOpen = false;
        toolsChangeLottie = findViewById(R.id.tools_change_game_over_dialog_lottie);
        toolsCostMap = new HashMap<>() {{
            put("standardToolsUndoCost", 125);
            put("standardToolsSmashTileCost", 150);
            put("standardToolsSwapTilesCost", 200);
            put("specialToolsChangeValueCost", 400);
            put("specialToolsEliminateValueCost", 450);
            put("specialToolsDestroyAreaCost", 500);
        }};
        standardToolsLinearLayout = findViewById(R.id.standard_tools_game_over_dialog_linear_layout);
        specialToolsLinearLayout = findViewById(R.id.special_tools_game_over_dialog_linear_layout);
        toolsLottieLinearLayout = findViewById(R.id.tools_lottie_game_over_dialog_linear_layout);
        standardToolsUndoImageView = findViewById(R.id.standard_tools_undo_icon_game_over_dialog_image_view);
        standardToolsSmashTileImageView = findViewById(R.id.standard_tools_smash_icon_game_over_dialog_image_view);
        standardToolsSwapTilesImageView = findViewById(R.id.standard_tools_swap_tiles_icon_game_over_dialog_image_view);
        specialToolsChangeValueImageView = findViewById(R.id.special_tools_change_value_icon_game_over_dialog_image_view);
        specialToolsEliminateValueImageView = findViewById(R.id.special_tools_eliminate_value_icon_game_over_dialog_image_view);
        specialToolsDestroyAreaImageView = findViewById(R.id.special_tools_destroy_area_icon_game_over_dialog_image_view);
        AppCompatTextView standardToolsUndoCostTextView =
                findViewById(R.id.standard_tools_undo_cost_game_over_dialog_text_view);
        standardToolsUndoCostTextView.setText(String.valueOf(toolsCostMap.get("standardToolsUndoCost")));
        AppCompatTextView standardToolsSmashTileCostTextView =
                findViewById(R.id.standard_tools_smash_cost_game_over_dialog_text_view);
        standardToolsSmashTileCostTextView.setText(String.valueOf(toolsCostMap.get("standardToolsSmashTileCost")));
        AppCompatTextView standardToolsSwapTilesCostTextView =
                findViewById(R.id.standard_tools_swap_tiles_cost_game_over_dialog_text_view);
        standardToolsSwapTilesCostTextView.setText(String.valueOf(toolsCostMap.get("standardToolsSwapTilesCost")));
        AppCompatTextView specialToolsChangeValueCostTextView =
                findViewById(R.id.special_tools_change_value_cost_game_over_dialog_text_view);
        specialToolsChangeValueCostTextView.setText(String.valueOf(toolsCostMap.get("specialToolsChangeValueCost")));
        AppCompatTextView specialToolsEliminateValueCostTextView =
                findViewById(R.id.special_tools_eliminate_value_cost_game_over_dialog_text_view);
        specialToolsEliminateValueCostTextView.setText(String.valueOf(toolsCostMap.get("specialToolsEliminateValueCost")));
        AppCompatTextView specialToolsDestroyAreaCostTextView =
                findViewById(R.id.special_tools_destroy_area_cost_game_over_dialog_text_view);
        specialToolsDestroyAreaCostTextView.setText(String.valueOf(toolsCostMap.get("specialToolsDestroyAreaCost")));
        shopCoinsButton = findViewById(R.id.first_page_shop_coins_game_over_dialog_button);
        continueButton = findViewById(R.id.first_page_continue_game_over_dialog_button);

        this.currentScore = currentScore;
        this.bestScore = bestScore;
        gameOverLottie = findViewById(R.id.game_over_dialog_lottie);
        newHighScoreSummaryLinearLayout = findViewById(R.id.new_high_score_summary_game_over_dialog_linear_layout);
        newHighScoreTextView = findViewById(R.id.new_high_score_game_over_dialog_text_view);
        scoresSummaryConstraintLayout = findViewById(R.id.scores_summary_game_over_dialog_constraint_layout);
        currentScoreTextView = findViewById(R.id.score_game_over_dialog_text_view);
        bestScoreTextView = findViewById(R.id.high_score_game_over_dialog_text_view);
        if (currentScore == bestScore) {
            newHighScoreSummaryLinearLayout.setVisibility(View.VISIBLE);
            scoresSummaryConstraintLayout.setVisibility(View.GONE);
            newHighScoreTextView.setText(NumericValueDisplay.getScoreValueDisplay(this.bestScore));
        } else {
            newHighScoreSummaryLinearLayout.setVisibility(View.GONE);
            scoresSummaryConstraintLayout.setVisibility(View.VISIBLE);
            currentScoreTextView.setText(NumericValueDisplay.getScoreValueDisplay(this.currentScore));
            bestScoreTextView.setText(NumericValueDisplay.getScoreValueDisplay(this.bestScore));
        }
        mainMenuButton = findViewById(R.id.second_page_main_menu_game_over_dialog_button);
        playAgainButton = findViewById(R.id.second_page_play_again_game_over_dialog_button);
        backButton = findViewById(R.id.second_page_back_game_over_dialog_button);
    }

    private void setVisibilityOfDialogPage(GameOverDialogActivePage currentActivePage, int visibility) {
        if (visibility == View.VISIBLE) {
            if (currentActivePage == GameOverDialogActivePage.REVIVE_TOOLS_PAGE) {
                rootLinearLayoutFirstPage.setVisibility(View.VISIBLE);
                rootLinearLayoutSecondPage.setVisibility(View.GONE);
            } else if (currentActivePage == GameOverDialogActivePage.GAME_SUMMARY_PAGE) {
                rootLinearLayoutFirstPage.setVisibility(View.GONE);
                rootLinearLayoutSecondPage.setVisibility(View.VISIBLE);
            }
            activePage = currentActivePage;
        } else if (visibility == View.INVISIBLE) {
            rootLinearLayoutFirstPage.setVisibility(visibility);
            rootLinearLayoutSecondPage.setVisibility(visibility);
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
                    standardToolsLinearLayout.setVisibility(View.VISIBLE);
                    specialToolsLinearLayout.setVisibility(View.GONE);
                    toolsChangeLottie.setClickable(true);
                } else {
                    specialToolsLinearLayout.setVisibility(View.VISIBLE);
                    standardToolsLinearLayout.setVisibility(View.GONE);

                    LottieAnimationView leftView = toolsLottieLinearLayout.findViewById(R.id.tools_left_game_over_dialog_lottie);
                    LottieAnimationView midView = toolsLottieLinearLayout.findViewById(R.id.tools_middle_game_over_dialog_lottie);
                    LottieAnimationView rightView = toolsLottieLinearLayout.findViewById(R.id.tools_right_game_over_dialog_lottie);
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
    }

    private void handlePageTransitionFromFirstToSecond() {
        rootLinearLayoutFirstPage.animate().scaleY(0f).setDuration(200).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                rootLinearLayoutFirstPage.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                rootLinearLayoutFirstPage.setScaleY(0f);
                rootLinearLayoutSecondPage.setScaleY(0f);
                rootLinearLayoutSecondPage.setVisibility(View.VISIBLE);
                rootLinearLayoutSecondPage.animate().scaleY(1f).setDuration(200).setListener(new Animator.AnimatorListener() {
                    @Override public void onAnimationStart(Animator animator) {}
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        rootLinearLayoutFirstPage.setVisibility(View.GONE);
                        rootLinearLayoutFirstPage.setScaleY(1f);
                        rootLinearLayoutSecondPage.setVisibility(View.VISIBLE);
                        rootLinearLayoutSecondPage.setScaleY(1f);

                        // Initialise the game over lottie animation
                        gameOverLottie.setRepeatMode(LottieDrawable.RESTART);
                        gameOverLottie.setRepeatCount(4);
                        gameOverLottie.playAnimation();
                    }
                    @Override public void onAnimationCancel(Animator animator) {}
                    @Override public void onAnimationRepeat(Animator animator) {}
                }).start();
            }
            @Override public void onAnimationCancel(Animator animator) {}
            @Override public void onAnimationRepeat(Animator animator) {}
        }).start();
    }

    private void settingFirstPageClickListeners() {
        toolsChangeLottie.setOnClickListener(view -> {
            handleToolsChangeTransition();
        });
        standardToolsUndoImageView.setOnClickListener(view -> {
            // First, the views will disappear, then the dialog box will close
            setVisibilityOfDialogPage(activePage, View.INVISIBLE);
            new CountDownTimer(100, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {}
                @Override
                public void onFinish() {
                    didUserGiveResponse = true;
                    activePage = GameOverDialogActivePage.REVIVE_TOOLS_PAGE;
                    optionSelected = GameOverDialogOptions.STANDARD_TOOL_UNDO;
                    dismiss();
                }
            }.start();
        });
        standardToolsSmashTileImageView.setOnClickListener(view -> {
            // First, the views will disappear, then the dialog box will close
            setVisibilityOfDialogPage(activePage, View.INVISIBLE);
            new CountDownTimer(100, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {}
                @Override
                public void onFinish() {
                    didUserGiveResponse = true;
                    activePage = GameOverDialogActivePage.REVIVE_TOOLS_PAGE;
                    optionSelected = GameOverDialogOptions.STANDARD_TOOL_SMASH_TILE;
                    dismiss();
                }
            }.start();
        });
        standardToolsSwapTilesImageView.setOnClickListener(view -> {
            // First, the views will disappear, then the dialog box will close
            setVisibilityOfDialogPage(activePage, View.INVISIBLE);
            new CountDownTimer(100, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {}
                @Override
                public void onFinish() {
                    didUserGiveResponse = true;
                    activePage = GameOverDialogActivePage.REVIVE_TOOLS_PAGE;
                    optionSelected = GameOverDialogOptions.STANDARD_TOOL_SWAP_TILES;
                    dismiss();
                }
            }.start();
        });
        specialToolsChangeValueImageView.setOnClickListener(view -> {
            // First, the views will disappear, then the dialog box will close
            setVisibilityOfDialogPage(activePage, View.INVISIBLE);
            new CountDownTimer(100, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {}
                @Override
                public void onFinish() {
                    didUserGiveResponse = true;
                    activePage = GameOverDialogActivePage.REVIVE_TOOLS_PAGE;
                    optionSelected = GameOverDialogOptions.SPECIAL_TOOL_CHANGE_VALUE;
                    dismiss();
                }
            }.start();
        });
        specialToolsEliminateValueImageView.setOnClickListener(view -> {
            // First, the views will disappear, then the dialog box will close
            setVisibilityOfDialogPage(activePage, View.INVISIBLE);
            new CountDownTimer(100, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {}
                @Override
                public void onFinish() {
                    didUserGiveResponse = true;
                    activePage = GameOverDialogActivePage.REVIVE_TOOLS_PAGE;
                    optionSelected = GameOverDialogOptions.SPECIAL_TOOL_ELIMINATE_VALUE;
                    dismiss();
                }
            }.start();
        });
        specialToolsDestroyAreaImageView.setOnClickListener(view -> {
            // First, the views will disappear, then the dialog box will close
            setVisibilityOfDialogPage(activePage, View.INVISIBLE);
            new CountDownTimer(100, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {}
                @Override
                public void onFinish() {
                    didUserGiveResponse = true;
                    activePage = GameOverDialogActivePage.REVIVE_TOOLS_PAGE;
                    optionSelected = GameOverDialogOptions.SPECIAL_TOOL_DESTROY_AREA;
                    dismiss();
                }
            }.start();
        });
        shopCoinsButton.setOnClickListener(view -> {
            // First, the views will disappear, then the dialog box will close
            setVisibilityOfDialogPage(activePage, View.INVISIBLE);
            new CountDownTimer(100, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {}
                @Override
                public void onFinish() {
                    didUserGiveResponse = true;
                    activePage = GameOverDialogActivePage.REVIVE_TOOLS_PAGE;
                    optionSelected = GameOverDialogOptions.SHOP_COINS;
                    dismiss();
                }
            }.start();
        });
        continueButton.setOnClickListener(view -> {
            handlePageTransitionFromFirstToSecond();
        });
    }

    private void handlePageTransitionFromSecondToFirst() {
        gameOverLottie.pauseAnimation();
        rootLinearLayoutSecondPage.animate().scaleY(0f).setDuration(200).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                rootLinearLayoutSecondPage.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                rootLinearLayoutSecondPage.setScaleY(0f);
                rootLinearLayoutFirstPage.setScaleY(0f);
                rootLinearLayoutFirstPage.setVisibility(View.VISIBLE);
                rootLinearLayoutFirstPage.animate().scaleY(1f).setDuration(200).setListener(new Animator.AnimatorListener() {
                    @Override public void onAnimationStart(Animator animator) {}
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        rootLinearLayoutSecondPage.setVisibility(View.GONE);
                        rootLinearLayoutSecondPage.setScaleY(1f);
                        rootLinearLayoutFirstPage.setVisibility(View.VISIBLE);
                        rootLinearLayoutFirstPage.setScaleY(1f);
                    }
                    @Override public void onAnimationCancel(Animator animator) {}
                    @Override public void onAnimationRepeat(Animator animator) {}
                }).start();
            }
            @Override public void onAnimationCancel(Animator animator) {}
            @Override public void onAnimationRepeat(Animator animator) {}
        }).start();
    }

    private void settingSecondPageClickListeners() {
        mainMenuButton.setOnClickListener(view -> {
            // First, the views will disappear, then the dialog box will close
            setVisibilityOfDialogPage(activePage, View.INVISIBLE);
            new CountDownTimer(100, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {}
                @Override
                public void onFinish() {
                    didUserGiveResponse = true;
                    activePage = GameOverDialogActivePage.GAME_SUMMARY_PAGE;
                    optionSelected = GameOverDialogOptions.MAIN_MENU;
                    dismiss();
                }
            }.start();
        });
        playAgainButton.setOnClickListener(view -> {
            // First, the views will disappear, then the dialog box will close
            setVisibilityOfDialogPage(activePage, View.INVISIBLE);
            new CountDownTimer(100, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {}
                @Override
                public void onFinish() {
                    didUserGiveResponse = true;
                    activePage = GameOverDialogActivePage.GAME_SUMMARY_PAGE;
                    optionSelected = GameOverDialogOptions.PLAY_AGAIN;
                    dismiss();
                }
            }.start();
        });
        backButton.setOnClickListener(view -> {
            handlePageTransitionFromSecondToFirst();
        });
    }

    public GameOverDialog(@NonNull Context context, long currentScore, long bestScore) {
        super(context, R.style.CustomDialogTheme);
        setContentView(R.layout.dialog_game_over);

        initialise(currentScore, bestScore);

        settingFirstPageClickListeners();

        settingSecondPageClickListeners();
    }

    @Override
    public void show() {
        // Set the dialog to not focusable.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        // Show the dialog!
        super.show();

        // Set the dialog to immersive sticky mode
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        // Clear the not focusable flag from the window
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        // First, the dialog box will open, then the views will show
        new CountDownTimer(400, 400) {
            @Override
            public void onTick(long millisUntilFinished) {}
            @Override
            public void onFinish() {
                setVisibilityOfDialogPage(GameOverDialogActivePage.REVIVE_TOOLS_PAGE, View.VISIBLE);
            }
        }.start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        gameOverDialogListener.getResponseOfOverDialog(didUserGiveResponse, activePage, optionSelected);
    }

    public void setGameOverDialogListener(GameOverDialogListener gameOverDialogListener) {
        this.gameOverDialogListener = gameOverDialogListener;
    }

    public interface GameOverDialogListener {
        void getResponseOfOverDialog(boolean didUserRespond, GameOverDialogActivePage activePage,
                                     GameOverDialogOptions optionSelected);
    }
}
