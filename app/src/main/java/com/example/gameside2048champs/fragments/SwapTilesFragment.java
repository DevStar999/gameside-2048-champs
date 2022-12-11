package com.example.gameside2048champs.fragments;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.gameside2048champs.animations.ToolAnimationsUtility;
import com.example.gameside2048champs.R;

public class SwapTilesFragment extends Fragment {
    private OnSwapTilesFragmentInteractionListener mListener;
    private AppCompatImageView backButton;
    private LottieAnimationView rotatingLightLottie;
    private LottieAnimationView swapTilesPreviewLottie;
    private AppCompatImageView toolUseCompletedImageView;
    private AppCompatCheckBox firstClickCheckBox;
    private AppCompatCheckBox secondClickCheckBox;
    private boolean isFirstClickDone;
    private boolean isSecondClickDone;
    private Pair<Integer, Integer> firstSwapTilePosition;
    private Pair<Integer, Integer> secondSwapTilePosition;
    private LottieAnimationView firstSwapTileLottie;
    private LottieAnimationView secondSwapTileLottie;

    public SwapTilesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void settingOnClickListeners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onSwapTilesFragmentInteractionBackClicked();
                }
            }
        });
        firstClickCheckBox.setEnabled(false);
        secondClickCheckBox.setEnabled(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        requireActivity().getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        View view = inflater.inflate(R.layout.fragment_swap_tiles, container, false);

        backButton = view.findViewById(R.id.back_button_swap_tiles_fragment);
        rotatingLightLottie = view.findViewById(R.id.rotating_light_swap_tiles_fragment);
        swapTilesPreviewLottie = view.findViewById(R.id.swap_tiles_preview_lottie);
        toolUseCompletedImageView = view.findViewById(R.id.tool_title_completed_image_view_swap_tiles_fragment);
        firstClickCheckBox = view.findViewById(R.id.first_click_swap_tiles_fragment_check_box);
        secondClickCheckBox = view.findViewById(R.id.second_click_swap_tiles_fragment_check_box);
        isFirstClickDone = true;
        isSecondClickDone = true;

        // Making tool lottie view emerge so that it grabs attention during the tool fragment transition
        CountDownTimer postFragmentSetupTimer = new CountDownTimer(650, 10000) {
            @Override
            public void onTick(long l) {}
            @Override
            public void onFinish() { isFirstClickDone = false; isSecondClickDone = false; }
        };
        new CountDownTimer(300, 10000) {
            @Override
            public void onTick(long l) {}
            @Override
            public void onFinish() {
                ToolAnimationsUtility.toolLottieEmergeAnimation(swapTilesPreviewLottie, 575);
                postFragmentSetupTimer.start();
            }
        }.start();

        settingOnClickListeners();

        return view;
    }

    public boolean checkFirstClickStatus() {
        return isFirstClickDone;
    }

    public boolean checkSecondClickStatus() {
        return isSecondClickDone;
    }

    public void handleSwapTilesToolFirstClick(LottieAnimationView firstSwapTileLottie,
                                              Pair<Integer, Integer> firstSwapTilePosition) {
        // 1st set of events is as follows
        isFirstClickDone = true;
        firstClickCheckBox.setChecked(true);
        rotatingLightLottie.pauseAnimation();
        swapTilesPreviewLottie.setProgress(0.1077f); // Jump to 7th frame out of 65 frames
        swapTilesPreviewLottie.pauseAnimation();

        // 2nd set of events is as follows - Setting the selection animation for the first swap tile
        this.firstSwapTilePosition = new Pair<>(firstSwapTilePosition.first, firstSwapTilePosition.second);
        this.firstSwapTileLottie = firstSwapTileLottie;
        ToolAnimationsUtility.specialToolsSwapTilesFirstClickSelectionSetup(this.firstSwapTileLottie);
        this.firstSwapTileLottie.playAnimation();
    }

    public void handleSwapTilesToolSecondClick(GridLayout gameCellLottieLayout, LottieAnimationView secondSwapTileLottie,
                                               LottieAnimationView gridLottieView, Pair<Integer, Integer> secondSwapTilePosition) {
        // 1st set of events is as follows
        isSecondClickDone = true;
        backButton.setVisibility(View.GONE);
        toolUseCompletedImageView.setImageResource(R.drawable.completed_icon);
        secondClickCheckBox.setChecked(true);

        // Pre-processing for the 5th set of events is as follows
        Animator.AnimatorListener swapTilesLottieSwapAnimatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {
                gameCellLottieLayout.setVisibility(View.GONE);
                isFirstClickDone = false; isSecondClickDone = false;
                if (mListener != null) {
                    mListener.onSwapTilesFragmentInteractionProcessToolUse(SwapTilesFragment.this.firstSwapTilePosition,
                            SwapTilesFragment.this.secondSwapTilePosition);
                }
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        };

        // Pre-processing for the 4th set of events is as follows
        Animator.AnimatorListener gridLottieAnimatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {
                gridLottieView.setBackgroundResource(0); // To remove background drawable
                gridLottieView.setSpeed(1f);
                gridLottieView.setVisibility(View.GONE);
                gridLottieView.removeAllAnimatorListeners();
                gameCellLottieLayout.setVisibility(View.VISIBLE);

                // 5th set of events is as follows
                ToolAnimationsUtility.specialToolsSwapTilesSwapTileSetup(SwapTilesFragment.this.firstSwapTileLottie);
                ToolAnimationsUtility.specialToolsSwapTilesSwapTileSetup(SwapTilesFragment.this.secondSwapTileLottie);
                SwapTilesFragment.this.secondSwapTileLottie.removeAllAnimatorListeners();
                SwapTilesFragment.this.secondSwapTileLottie.addAnimatorListener(swapTilesLottieSwapAnimatorListener);
                SwapTilesFragment.this.firstSwapTileLottie.playAnimation();
                SwapTilesFragment.this.secondSwapTileLottie.playAnimation();
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        };

        // Pre-processing for the 3rd set of events is as follows
        Animator.AnimatorListener swapTilesLottieSecondSelectionAnimatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {
                gameCellLottieLayout.setVisibility(View.GONE);

                // 4th set of events is as follows
                ToolAnimationsUtility.specialToolsSwapTilesGridSetup(gridLottieView);
                gridLottieView.addAnimatorListener(gridLottieAnimatorListener);
                gridLottieView.playAnimation();

                //  Taking care of some trivial work after starting the animation
                SwapTilesFragment.this.firstSwapTileLottie.setPadding(0,0,0,0);
                SwapTilesFragment.this.firstSwapTileLottie.setScaleX(1f);
                SwapTilesFragment.this.firstSwapTileLottie.setSpeed(1f);
                SwapTilesFragment.this.firstSwapTileLottie.setScaleType(ImageView.ScaleType.FIT_CENTER);
                SwapTilesFragment.this.firstSwapTileLottie.pauseAnimation();
                SwapTilesFragment.this.secondSwapTileLottie.setPadding(0,0,0,0);
                SwapTilesFragment.this.secondSwapTileLottie.setScaleX(1f);
                SwapTilesFragment.this.secondSwapTileLottie.setSpeed(1f);
                SwapTilesFragment.this.secondSwapTileLottie.setScaleType(ImageView.ScaleType.FIT_CENTER);
                SwapTilesFragment.this.secondSwapTileLottie.pauseAnimation();
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        };

        // Pre-processing for the 2nd set of events is as follows
        Animator.AnimatorListener swapTilesLottieFirstSelectionAnimatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {
                SwapTilesFragment.this.firstSwapTileLottie.setProgress(0f);
                SwapTilesFragment.this.firstSwapTileLottie.setPadding(0,0,0,0);
                SwapTilesFragment.this.firstSwapTileLottie.setSpeed(1f);
                SwapTilesFragment.this.firstSwapTileLottie.setScaleType(ImageView.ScaleType.FIT_CENTER);
                SwapTilesFragment.this.firstSwapTileLottie.pauseAnimation();
                SwapTilesFragment.this.secondSwapTileLottie.setProgress(0f);
                SwapTilesFragment.this.secondSwapTileLottie.setPadding(0,0,0,0);
                SwapTilesFragment.this.secondSwapTileLottie.setSpeed(1f);
                SwapTilesFragment.this.secondSwapTileLottie.setScaleType(ImageView.ScaleType.FIT_CENTER);
                SwapTilesFragment.this.secondSwapTileLottie.pauseAnimation();

                // 3rd set of events is as follows
                ToolAnimationsUtility.specialToolsSwapTilesSecondClickSecondSelectionSetup(SwapTilesFragment.this.firstSwapTileLottie);
                ToolAnimationsUtility.specialToolsSwapTilesSecondClickSecondSelectionSetup(SwapTilesFragment.this.secondSwapTileLottie);
                SwapTilesFragment.this.secondSwapTileLottie.removeAllAnimatorListeners();
                SwapTilesFragment.this.secondSwapTileLottie.addAnimatorListener(swapTilesLottieSecondSelectionAnimatorListener);
                SwapTilesFragment.this.firstSwapTileLottie.playAnimation();
                SwapTilesFragment.this.secondSwapTileLottie.playAnimation();
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        };

        // 2nd set of events is as follows
        this.firstSwapTileLottie.setProgress(0);
        this.firstSwapTileLottie.pauseAnimation();
        this.secondSwapTilePosition = new Pair<>(secondSwapTilePosition.first, secondSwapTilePosition.second);
        this.secondSwapTileLottie = secondSwapTileLottie;
        ToolAnimationsUtility.specialToolsSwapTilesSecondClickFirstSelectionSetup(this.firstSwapTileLottie);
        ToolAnimationsUtility.specialToolsSwapTilesSecondClickFirstSelectionSetup(this.secondSwapTileLottie);
        this.secondSwapTileLottie.addAnimatorListener(swapTilesLottieFirstSelectionAnimatorListener);
        this.firstSwapTileLottie.playAnimation(); this.secondSwapTileLottie.playAnimation();
    }

    public interface OnSwapTilesFragmentInteractionListener {
        void onSwapTilesFragmentInteractionBackClicked();
        void onSwapTilesFragmentInteractionProcessToolUse(Pair<Integer, Integer> firstSwapTilePosition,
                                                          Pair<Integer, Integer> secondSwapTilePosition);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnSwapTilesFragmentInteractionListener) {
            mListener = (OnSwapTilesFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnSwapTilesFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
