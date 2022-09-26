package com.example.gameside2048champs;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.gameside2048champs.enums.Direction;
import com.example.gameside2048champs.enums.GameLayoutProperties;

public class AnimationUtility {
    private static void setTextViewAttributes(AppCompatTextView textView, int cellValue,
                                              int textColor, Drawable backgroundDrawable,
                                              GameLayoutProperties gameLayoutProperties) {
        textView.setVisibility(View.VISIBLE);
        textView.setText(String.valueOf(cellValue));
        textView.setTextColor(textColor);
        textView.setTextSize(gameLayoutProperties.getTextSizeForValue(cellValue));
        textView.setBackground(backgroundDrawable);
    }

    /* Note: ViewGroup is the parent class for all the Layout classes and View is the parent class
    for all types of views
    */
    public static void executePopUpAnimation(AppCompatTextView textView, int cellValue, int textColor,
                                             Drawable backgroundDrawable, long duration, long delay,
                                             GameLayoutProperties gameLayoutProperties) {
        ObjectAnimator popUpXAnimator = ObjectAnimator.ofFloat(textView, View.SCALE_X, 0.5f, 1f).setDuration(duration);
        ObjectAnimator popUpYAnimator = ObjectAnimator.ofFloat(textView, View.SCALE_Y, 0.5f, 1f).setDuration(duration);
        AnimatorSet popUpAnimatorSet = new AnimatorSet();
        popUpAnimatorSet.playTogether(popUpXAnimator, popUpYAnimator);
        popUpAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setTextViewAttributes(textView, cellValue, textColor, backgroundDrawable, gameLayoutProperties);
                textView.setScaleX(0f);
                textView.setScaleY(0f);
            }
            @Override
            public void onAnimationEnd(Animator animation) {}
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        popUpAnimatorSet.setStartDelay(delay);
        popUpAnimatorSet.start();
    }

    public static void undoResetState(AppCompatTextView textView, int cellValue, int textColor,
                                      Drawable backgroundDrawable, GameLayoutProperties gameLayoutProperties) {
        textView.setVisibility(View.VISIBLE);
        textView.setText(String.valueOf(cellValue));
        textView.setTextColor(textColor);
        textView.setTextSize(gameLayoutProperties.getTextSizeForValue(cellValue));
        textView.setBackground(backgroundDrawable);
    }

    /**
     * The following is a substitute animation
     */
    public static AnimatorSet getEmptyAnimation(AppCompatTextView textView, long duration, long delay, int visibility) {
        AnimatorSet emptyAnimatorSet = new AnimatorSet();
        ObjectAnimator keepSameRotationAnimator = ObjectAnimator.ofFloat(textView, View.ROTATION, 0f, 0f).setDuration(duration);
        keepSameRotationAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                textView.setVisibility(visibility);
            }
            @Override
            public void onAnimationEnd(Animator animation) {}
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        emptyAnimatorSet.play(keepSameRotationAnimator);
        emptyAnimatorSet.setStartDelay(delay);
        return emptyAnimatorSet;
    }

    /**
     * The Following is the animation for showing a slide animation
     */
    public static AnimatorSet getSlideAnimation(int initRowPos, int initColumnPos, int finalRowPos, int finalColumnPos,
                                                int totalRows, int totalColumns, ViewGroup rootLayout, Direction direction,
                                                AppCompatTextView textView, long duration, long delay) {
        // Setting up slide animation in horizontal direction
        float pixelsToTravelX = textView.getWidth() +
                (float) (rootLayout.getWidth() - (totalColumns * textView.getWidth())) / (float) (totalColumns + 1);
        pixelsToTravelX *= Math.abs(initColumnPos - finalColumnPos);
        pixelsToTravelX = (direction == Direction.RIGHT) ? pixelsToTravelX : (-1 * pixelsToTravelX);
        ObjectAnimator slideXAnimator = ObjectAnimator.ofFloat(textView, View.TRANSLATION_X, pixelsToTravelX);
        float returnXPosition = textView.getTranslationX();
        slideXAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                textView.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                textView.setVisibility(View.INVISIBLE);
                textView.setTranslationX(returnXPosition);
            }
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        slideXAnimator.setDuration(duration);

        // Setting up slide animation in vertical direction
        float pixelsToTravelY = textView.getHeight() +
                (float) (rootLayout.getHeight() - (totalRows * textView.getHeight())) / (float) (totalRows + 1);
        pixelsToTravelY *= Math.abs(initRowPos - finalRowPos);
        pixelsToTravelY = (direction == Direction.DOWN) ? pixelsToTravelY : (-1 * pixelsToTravelY);
        ObjectAnimator slideYAnimator = ObjectAnimator.ofFloat(textView, View.TRANSLATION_Y, pixelsToTravelY);
        float returnYPosition = textView.getTranslationY();
        slideYAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                textView.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                textView.setVisibility(View.INVISIBLE);
                textView.setTranslationY(returnYPosition);
            }
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        slideYAnimator.setDuration(duration);

        // Finally, choosing which animation to play
        AnimatorSet slideAnimatorSet = new AnimatorSet();
        slideAnimatorSet.play((direction == Direction.LEFT || direction == Direction.RIGHT) ?
                slideXAnimator : slideYAnimator);
        slideAnimatorSet.setStartDelay(delay);
        return slideAnimatorSet;
    }

    /**
     * The Following two are end part animations
     */
    public static AnimatorSet getSimplyAppearAnimation(AppCompatTextView textView, int cellValue, int textColor,
                                                       Drawable backgroundDrawable, long duration, long delay,
                                                       GameLayoutProperties gameLayoutProperties) {
        AnimatorSet simplyAppearAnimatorSet = new AnimatorSet();
        ObjectAnimator keepSameRotationAnimator = ObjectAnimator.ofFloat(textView, View.ROTATION, 0f, 0f).setDuration(duration);
        keepSameRotationAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setTextViewAttributes(textView, cellValue, textColor, backgroundDrawable, gameLayoutProperties);
            }
            @Override
            public void onAnimationEnd(Animator animation) {}
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        simplyAppearAnimatorSet.play(keepSameRotationAnimator);
        simplyAppearAnimatorSet.setStartDelay(delay);
        return simplyAppearAnimatorSet;
    }

    public static AnimatorSet getMergeAnimation(AppCompatTextView textView, int cellValue, int textColor,
                                                Drawable backgroundDrawable, long duration, long delay,
                                                GameLayoutProperties gameLayoutProperties) {
        AnimatorSet mergeAnimatorSet = new AnimatorSet();
        ObjectAnimator keepSameRotationAnimator = ObjectAnimator.ofFloat(textView, View.ROTATION, 0f, 0f).setDuration(0);
        keepSameRotationAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setTextViewAttributes(textView, cellValue, textColor, backgroundDrawable, gameLayoutProperties);
            }
            @Override
            public void onAnimationEnd(Animator animation) {}
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        ObjectAnimator mergeXAnimator = ObjectAnimator.ofFloat(textView, View.SCALE_X, 0.5f, 1.15f).setDuration(duration);
        ObjectAnimator mergeYAnimator = ObjectAnimator.ofFloat(textView, View.SCALE_Y, 0.5f, 1.15f).setDuration(duration);
        ObjectAnimator settleXAnimator = ObjectAnimator.ofFloat(textView, View.SCALE_X, 1.15f, 1f).setDuration(0);
        ObjectAnimator settleYAnimator = ObjectAnimator.ofFloat(textView, View.SCALE_Y, 1.15f, 1f).setDuration(0);
        mergeAnimatorSet.play(keepSameRotationAnimator).before(mergeXAnimator);
        mergeAnimatorSet.play(mergeXAnimator).with(mergeYAnimator);
        mergeAnimatorSet.play(settleXAnimator).with(settleYAnimator).after(mergeXAnimator);
        mergeAnimatorSet.setStartDelay(delay);
        return mergeAnimatorSet;
    }

    /**
     * The following animations are for when there is a use of a tool
     */
    public static void normalToolsUndo(LottieAnimationView gridLottieView, ConstraintLayout rootGameConstraintLayout) {
        gridLottieView.setVisibility(View.VISIBLE);
        gridLottieView.setRotationY(180f);
        gridLottieView.setBackgroundResource(R.drawable.rounded_corner_grid_lottie);
        gridLottieView.setAnimation(R.raw.normal_tools_undo);
        gridLottieView.setSpeed(1.5f);
        gridLottieView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                rootGameConstraintLayout.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                gridLottieView.setVisibility(View.GONE);
                gridLottieView.setRotationY(0f);
                gridLottieView.setBackgroundResource(0); // To remove background drawable
                rootGameConstraintLayout.setEnabled(true);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        gridLottieView.playAnimation();
    }
}
