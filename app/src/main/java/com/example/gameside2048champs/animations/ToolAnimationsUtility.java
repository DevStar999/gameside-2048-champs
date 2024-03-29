package com.example.gameside2048champs.animations;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.example.gameside2048champs.NumericValueDisplay;
import com.example.gameside2048champs.R;
import com.example.gameside2048champs.enums.GameLayoutProperties;

import java.util.List;

public class ToolAnimationsUtility {
    private static int dpToPx(int dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public static void toolsBackgroundAppearAnimation(AppCompatImageView backgroundFilmImageView, int duration) {
        ObjectAnimator simpleAppearAnimator = ObjectAnimator.ofFloat(backgroundFilmImageView, View.ALPHA, 0f, 1f)
                .setDuration(duration);
        simpleAppearAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                backgroundFilmImageView.setImageResource(R.color.black_translucent_film_game_activity_background);
            }
            @Override
            public void onAnimationEnd(Animator animator) {}
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
        AnimatorSet simpleAppearAnimatorSet = new AnimatorSet();
        simpleAppearAnimatorSet.play(simpleAppearAnimator);
        simpleAppearAnimatorSet.start();
    }

    public static void toolLottieEmergeAnimation(LottieAnimationView toolLottieView, int duration) {
        ObjectAnimator toolEmergeXAnimator =
                ObjectAnimator.ofFloat(toolLottieView, View.SCALE_X, 0.4f, 1f).setDuration(duration);
        ObjectAnimator toolEmergeYAnimator =
                ObjectAnimator.ofFloat(toolLottieView, View.SCALE_Y, 0.4f, 1f).setDuration(duration);

        AnimatorSet toolEmergeAnimatorSet = new AnimatorSet();
        toolEmergeAnimatorSet.playTogether(toolEmergeXAnimator, toolEmergeYAnimator);
        toolEmergeAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {
                toolLottieView.setScaleX(1f);
                toolLottieView.setScaleY(1f);
                toolLottieView.playAnimation();
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
        toolEmergeAnimatorSet.start();
    }

    public static void standardToolsUndo(LottieAnimationView gridLottieView, ConstraintLayout rootGameConstraintLayout) {
        gridLottieView.setVisibility(View.VISIBLE);
        gridLottieView.setRotationY(180f);
        gridLottieView.setBackgroundResource(R.drawable.rounded_corners_grid_lottie);
        gridLottieView.setAnimation(R.raw.standard_tools_undo_grid);
        gridLottieView.setSpeed(1.5f);
        gridLottieView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                rootGameConstraintLayout.setEnabled(false);
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                gridLottieView.setRotationY(0f);
                gridLottieView.setBackgroundResource(0); // To remove background drawable
                gridLottieView.setSpeed(1);
                gridLottieView.setVisibility(View.GONE);
                gridLottieView.removeAllAnimatorListeners();
                rootGameConstraintLayout.setEnabled(true);
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
        gridLottieView.playAnimation();
    }

    public static void standardToolsUndoResetState(AppCompatTextView textView, long cellValue, int textColor,
                                                   Drawable backgroundDrawable, GameLayoutProperties gameLayoutProperties) {
        textView.setVisibility(View.VISIBLE);
        textView.setText(NumericValueDisplay.getGameTileValueDisplay(cellValue));
        textView.setTextColor(textColor);
        textView.setTextSize(gameLayoutProperties.getTextSizeForValue(cellValue));
        textView.setBackground(backgroundDrawable);
    }

    public static void standardToolsSmashTileTargetTileSelectionSetup(LottieAnimationView targetTileLottie) {
        targetTileLottie.setPadding(16,0,0,0);
        targetTileLottie.setScaleX(1.05f);
        targetTileLottie.setVisibility(View.VISIBLE);
        targetTileLottie.setMaxFrame(20);
        targetTileLottie.setAnimation(R.raw.tile_selection);
        targetTileLottie.setSpeed(0.7f);
    }

    public static void standardToolsSmashTileGridSetup(LottieAnimationView gridLottieView) {
        gridLottieView.setVisibility(View.VISIBLE);
        gridLottieView.setRotationY(180f);
        gridLottieView.setBackgroundResource(R.drawable.rounded_corners_grid_lottie);
        gridLottieView.setAnimation(R.raw.standard_tools_smash_tile_grid_tile);
        gridLottieView.setSpeed(0.7f);
    }

    public static void standardToolsSmashTileTargetTileSetup(LottieAnimationView targetTileLottie) {
        targetTileLottie.setVisibility(View.VISIBLE);
        targetTileLottie.setRotationY(180f);
        targetTileLottie.setMaxFrame(15);
        targetTileLottie.setBackgroundResource(R.drawable.rounded_corners_tile_lottie);
        targetTileLottie.setAnimation(R.raw.standard_tools_smash_tile_grid_tile);
        targetTileLottie.setSpeed(0.7f);
    }

    public static void standardToolsSwapTilesFirstClickSelectionSetup(LottieAnimationView swapTileLottie) {
        swapTileLottie.setPadding(2,2,2,2);
        swapTileLottie.setVisibility(View.VISIBLE);
        swapTileLottie.setRepeatMode(LottieDrawable.RESTART);
        swapTileLottie.setRepeatCount(LottieDrawable.INFINITE);
        swapTileLottie.setScaleType(ImageView.ScaleType.FIT_XY);
        swapTileLottie.setAnimation(R.raw.standard_tools_swap_tiles_tile_selection);
    }

    public static void standardToolsSwapTilesSecondClickFirstSelectionSetup(LottieAnimationView swapTileLottie) {
        swapTileLottie.setPadding(2,2,2,2);
        swapTileLottie.setVisibility(View.VISIBLE);
        swapTileLottie.setRepeatCount(0);
        swapTileLottie.setScaleType(ImageView.ScaleType.FIT_XY);
        swapTileLottie.setAnimation(R.raw.standard_tools_swap_tiles_tile_selection);
        swapTileLottie.setSpeed(2);
    }

    public static void standardToolsSwapTilesSecondClickSecondSelectionSetup(LottieAnimationView swapTileLottie) {
        swapTileLottie.setPadding(16,0,0,0);
        swapTileLottie.setScaleX(1.05f);
        swapTileLottie.setVisibility(View.VISIBLE);
        swapTileLottie.setMaxFrame(20);
        swapTileLottie.setAnimation(R.raw.tile_selection);
        swapTileLottie.setSpeed(1f);
    }

    public static void standardToolsSwapTilesGridSetup(LottieAnimationView gridLottieView) {
        gridLottieView.setVisibility(View.VISIBLE);
        gridLottieView.setBackgroundResource(R.drawable.rounded_corners_grid_lottie);
        gridLottieView.setAnimation(R.raw.standard_tools_swap_tiles_grid);
        gridLottieView.setSpeed(1.25f);
    }

    public static void standardToolsSwapTilesSwapTileSetup(LottieAnimationView swapTileLottie) {
        swapTileLottie.setVisibility(View.VISIBLE);
        swapTileLottie.setBackgroundResource(R.drawable.rounded_corners_tile_lottie);
        swapTileLottie.setAnimation(R.raw.standard_tools_swap_tiles_tile);
        swapTileLottie.setSpeed(1.5f);
    }

    public static void specialToolsChangeValueFirstClickSelectionSetup(LottieAnimationView changeValueTileLottie) {
        changeValueTileLottie.setPadding(2,2,2,2);
        changeValueTileLottie.setVisibility(View.VISIBLE);
        changeValueTileLottie.setRepeatMode(LottieDrawable.RESTART);
        changeValueTileLottie.setRepeatCount(LottieDrawable.INFINITE);
        changeValueTileLottie.setScaleType(ImageView.ScaleType.FIT_XY);
        changeValueTileLottie.setAnimation(R.raw.tile_selection_continuous);
    }

    public static void specialToolsChangeValueSecondClickSelectionSetup(LottieAnimationView changeValueTileLottie) {
        // Removing old properties
        changeValueTileLottie.setPadding(0,0,0,0);
        changeValueTileLottie.setRepeatCount(0);
        changeValueTileLottie.setScaleType(ImageView.ScaleType.FIT_CENTER); // Setting back to the default ScaleType

        // Setting new properties
        changeValueTileLottie.setPadding(16,0,0,0);
        changeValueTileLottie.setScaleX(1.05f);
        changeValueTileLottie.setVisibility(View.VISIBLE);
        changeValueTileLottie.setMaxFrame(20);
        changeValueTileLottie.setAnimation(R.raw.tile_selection);
        changeValueTileLottie.setSpeed(1f);
    }

    public static void specialToolsChangeValueGridSetup(LottieAnimationView gridLottieView) {
        gridLottieView.setVisibility(View.VISIBLE);
        gridLottieView.setBackgroundResource(R.drawable.rounded_corners_grid_lottie);
        gridLottieView.setAnimation(R.raw.special_tools_change_value_grid);
        gridLottieView.setSpeed(2f);
    }

    public static void specialToolsChangeValueTargetTileSetup(LottieAnimationView changeValueTileLottie) {
        changeValueTileLottie.setVisibility(View.VISIBLE);
        changeValueTileLottie.setBackgroundResource(R.drawable.rounded_corners_tile_lottie);
        changeValueTileLottie.setAnimation(R.raw.special_tools_change_value_tile);
        changeValueTileLottie.setSpeed(1.5f);
    }

    public static void specialToolsEliminateValueTargetTilesSelectionSetup(List<LottieAnimationView> targetTilesLottie) {
        for (int index = 0; index < targetTilesLottie.size(); index++) {
            targetTilesLottie.get(index).setPadding(16,0,0,0);
            targetTilesLottie.get(index).setScaleX(1.05f);
            targetTilesLottie.get(index).setVisibility(View.VISIBLE);
            targetTilesLottie.get(index).setMaxFrame(20);
            targetTilesLottie.get(index).setAnimation(R.raw.tile_selection);
            targetTilesLottie.get(index).setSpeed(0.7f);
        }
    }

    public static void specialToolsEliminateValueGridSetup(LottieAnimationView gridLottieView) {
        gridLottieView.setVisibility(View.VISIBLE);
        gridLottieView.setBackgroundResource(R.drawable.rounded_corners_grid_lottie);
        gridLottieView.setAnimation(R.raw.special_tools_eliminate_value_grid);
        gridLottieView.setSpeed(3f);
    }

    public static void specialToolsEliminateValueTargetTilesSetup(List<LottieAnimationView> targetTilesLottie) {
        for (int index = 0; index < targetTilesLottie.size(); index++) {
            targetTilesLottie.get(index).setVisibility(View.VISIBLE);
            targetTilesLottie.get(index).setMaxFrame(100);
            targetTilesLottie.get(index).setBackgroundResource(R.drawable.rounded_corners_tile_lottie);
            targetTilesLottie.get(index).setAnimation(R.raw.special_tools_eliminate_value_tile);
            targetTilesLottie.get(index).setSpeed(1.25f);
        }
    }

    public static void specialToolsDestroyAreaFirstClickSelectionSetup(List<LottieAnimationView> destroyAreaTilesLotties) {
        for (int index = 0; index < destroyAreaTilesLotties.size(); index++) {
            destroyAreaTilesLotties.get(index).setPadding(2,2,2,2);
            destroyAreaTilesLotties.get(index).setVisibility(View.VISIBLE);
            destroyAreaTilesLotties.get(index).setRepeatMode(LottieDrawable.RESTART);
            destroyAreaTilesLotties.get(index).setRepeatCount(LottieDrawable.INFINITE);
            destroyAreaTilesLotties.get(index).setScaleType(ImageView.ScaleType.FIT_XY);
            destroyAreaTilesLotties.get(index).setAnimation(R.raw.tile_selection_continuous);
        }
    }

    public static void specialToolsDestroyAreaSecondClickSelectionSetup(LottieAnimationView destroyAreaTileLottie) {
        // Removing old properties
        destroyAreaTileLottie.setPadding(0,0,0,0);
        destroyAreaTileLottie.setRepeatCount(0);
        destroyAreaTileLottie.setScaleType(ImageView.ScaleType.FIT_CENTER); // Setting back to the default ScaleType

        // Setting new properties
        destroyAreaTileLottie.setPadding(16,0,0,0);
        destroyAreaTileLottie.setScaleX(1.05f);
        destroyAreaTileLottie.setVisibility(View.VISIBLE);
        destroyAreaTileLottie.setMaxFrame(20);
        destroyAreaTileLottie.setAnimation(R.raw.tile_selection);
        destroyAreaTileLottie.setSpeed(1f);
    }

    public static void specialToolsDestroyAreaGridSetup(LottieAnimationView gridLottieView) {
        gridLottieView.setVisibility(View.VISIBLE);
        gridLottieView.setBackgroundResource(R.drawable.rounded_corners_grid_lottie);
        gridLottieView.setAnimation(R.raw.special_tools_destroy_area_grid);
        gridLottieView.setSpeed(2f);
    }

    public static void specialToolsDestroyAreaTargetTilesSetup(List<LottieAnimationView> destroyAreaTilesLotties) {
        for (int index = 0; index < destroyAreaTilesLotties.size(); index++) {
            destroyAreaTilesLotties.get(index).setVisibility(View.VISIBLE);
            destroyAreaTilesLotties.get(index).setBackgroundResource(R.drawable.rounded_corners_tile_lottie);
            destroyAreaTilesLotties.get(index).setAnimation(R.raw.special_tools_destroy_area_tile);
            destroyAreaTilesLotties.get(index).setSpeed(1.5f);
        }
    }

    public static void mysteryToolsReviveGame(Context context, LottieAnimationView gridLottieView,
                                              ConstraintLayout rootGameConstraintLayout) {
        gridLottieView.setVisibility(View.VISIBLE);
        gridLottieView.setBackgroundResource(R.drawable.mystery_tools_revive_game_background);
        gridLottieView.setPadding(dpToPx(64, context), dpToPx(64, context),
                dpToPx(64, context), dpToPx(64, context));
        gridLottieView.setAnimation(R.raw.mystery_tools_revive_game_grid); // Duration ~ 1400 ms
        gridLottieView.setSpeed(1.25f); // Total Duration = 1400/1.15 = 1120 ms
        gridLottieView.setRepeatCount(1); // Total Duration = 1120 * 2 = 2240 ms
        gridLottieView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                rootGameConstraintLayout.setEnabled(false);
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                gridLottieView.setBackgroundResource(0); // To remove background drawable
                gridLottieView.setVisibility(View.GONE);
                gridLottieView.setPadding(dpToPx(0, context), dpToPx(0, context),
                        dpToPx(0, context), dpToPx(0, context));
                gridLottieView.setSpeed(1f);
                gridLottieView.setRepeatCount(0);
                gridLottieView.removeAllAnimatorListeners();
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
        gridLottieView.playAnimation();
    }

    public static void mysteryToolsReviveGameReviveTileSetup(LottieAnimationView reviveTileLottie) {
        reviveTileLottie.setVisibility(View.VISIBLE);
        reviveTileLottie.setBackgroundResource(R.drawable.rounded_corners_tile_lottie);
        reviveTileLottie.setAnimation(R.raw.mystery_tools_revive_game_tile);
        reviveTileLottie.setSpeed(1.5f);
    }
}

