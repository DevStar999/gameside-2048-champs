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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.gameside2048champs.animations.ToolAnimationsUtility;
import com.example.gameside2048champs.R;

import java.util.List;

public class EliminateValueFragment extends Fragment {
    private OnEliminateValueFragmentInteractionListener mListener;
    private AppCompatImageView backButton;
    private LottieAnimationView rotatingLightLottie;
    private LottieAnimationView eliminateValuePreviewLottie;
    private AppCompatImageView toolUseCompletedImageView;
    private AppCompatTextView toolDescriptionTextView;
    private boolean isToolUseComplete;

    public EliminateValueFragment() {
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
                    mListener.onEliminateValueFragmentInteractionBackClicked();
                }
            }
        });
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

        View view = inflater.inflate(R.layout.fragment_eliminate_value, container, false);

        backButton = view.findViewById(R.id.back_button_eliminate_value_fragment);
        rotatingLightLottie = view.findViewById(R.id.rotating_light_eliminate_value_fragment);
        eliminateValuePreviewLottie = view.findViewById(R.id.eliminate_value_preview_lottie);
        toolUseCompletedImageView = view.findViewById(R.id.tool_title_completed_image_view_eliminate_value_fragment);
        toolDescriptionTextView = view.findViewById(R.id.tool_description_text_view_eliminate_value_fragment);
        isToolUseComplete = true;

        // Making tool lottie view emerge so that it grabs attention during the tool fragment transition
        CountDownTimer postFragmentSetupTimer = new CountDownTimer(650, 10000) {
            @Override
            public void onTick(long l) {}
            @Override
            public void onFinish() {
                isToolUseComplete = false;
            }
        };
        new CountDownTimer(300, 10000) {
            @Override
            public void onTick(long l) {}
            @Override
            public void onFinish() {
                ToolAnimationsUtility.toolLottieEmergeAnimation(eliminateValuePreviewLottie, 575);
                postFragmentSetupTimer.start();
            }
        }.start();

        settingOnClickListeners();

        return view;
    }

    public boolean checkToolUseState() {
        return isToolUseComplete;
    }

    private Animator.AnimatorListener getIndividualTargetTileSelectionAnimatorListener(boolean isCurrentTargetTileFinal,
                                                                                       LottieAnimationView targetTileLottie, GridLayout gameCellLottieLayout, LottieAnimationView gridLottieView,
                                                                                       Animator.AnimatorListener gridLottieAnimatorListener) {
        // Pre-processing for the 2nd set of events is as follows
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {
                targetTileLottie.setPadding(0,0,0,0); // Removing the paddings
                targetTileLottie.setScaleX(1f);
                targetTileLottie.pauseAnimation();

                // 3rd set of events is as follows
                if (isCurrentTargetTileFinal) {
                    gameCellLottieLayout.setVisibility(View.GONE);
                    ToolAnimationsUtility.specialToolsEliminateValueGridSetup(gridLottieView);
                    gridLottieView.addAnimatorListener(gridLottieAnimatorListener);
                    gridLottieView.playAnimation();
                }
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        };
    }

    private Animator.AnimatorListener getIndividualTargetTileEliminateAnimatorListener(boolean isCurrentTargetTileFinal,
                                                                                       GridLayout gameCellLottieLayout, List<Pair<Integer, Integer>> targetTilesPositions) {
        // Pre-processing for the 4th set of events is as follows
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {
                if (isCurrentTargetTileFinal) {
                    gameCellLottieLayout.setVisibility(View.GONE);
                    isToolUseComplete = false;
                    if (mListener != null) {
                        mListener.onEliminateValueFragmentInteractionProcessToolUse(targetTilesPositions);
                    }
                }
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        };
    }

    public void handleValueToBeEliminated(GridLayout gameCellLottieLayout, List<LottieAnimationView> targetTilesLottie,
                                          LottieAnimationView gridLottieView, List<Pair<Integer, Integer>> targetTilesPositions) {
        // 1st set of events is as follows
        isToolUseComplete = true;
        toolUseCompletedImageView.setImageResource(R.drawable.completed_icon);
        toolDescriptionTextView.setVisibility(View.GONE);
        rotatingLightLottie.pauseAnimation();
        eliminateValuePreviewLottie.setProgress(0f);
        eliminateValuePreviewLottie.pauseAnimation();

        // Pre-processing for the 3rd set of events is as follows
        Animator.AnimatorListener gridLottieAnimatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {
                gridLottieView.setRotationY(0f);
                gridLottieView.setBackgroundResource(0); // To remove background drawable
                gridLottieView.setSpeed(1f);
                gridLottieView.setVisibility(View.GONE);
                gridLottieView.removeAllAnimatorListeners();
                gameCellLottieLayout.setVisibility(View.VISIBLE);

                // 4th set of events is as follows
                ToolAnimationsUtility.specialToolsEliminateValueTargetTilesSetup(targetTilesLottie);
                for (int index = 0; index < targetTilesLottie.size(); index++) {
                    boolean isCurrentTargetTileFinal = (index == targetTilesLottie.size() - 1);
                    targetTilesLottie.get(index).removeAllAnimatorListeners();
                    targetTilesLottie.get(index).addAnimatorListener(getIndividualTargetTileEliminateAnimatorListener(
                            isCurrentTargetTileFinal, gameCellLottieLayout, targetTilesPositions));
                }
                for (int index = 0; index < targetTilesLottie.size(); index++) {
                    targetTilesLottie.get(index).playAnimation();
                }
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        };

        // 2nd set of events is as follows
        ToolAnimationsUtility.specialToolsEliminateValueTargetTilesSelectionSetup(targetTilesLottie);
        for (int index = 0; index < targetTilesLottie.size(); index++) {
            boolean isCurrentTargetTileFinal = (index == targetTilesLottie.size() - 1);
            targetTilesLottie.get(index).addAnimatorListener(getIndividualTargetTileSelectionAnimatorListener(
                    isCurrentTargetTileFinal, targetTilesLottie.get(index), gameCellLottieLayout,
                    gridLottieView, gridLottieAnimatorListener));
        }
        for (int index = 0; index < targetTilesLottie.size(); index++) {
            targetTilesLottie.get(index).playAnimation();
        }
    }

    public interface OnEliminateValueFragmentInteractionListener {
        void onEliminateValueFragmentInteractionBackClicked();
        void onEliminateValueFragmentInteractionProcessToolUse(List<Pair<Integer, Integer>> targetTilesPositions);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnEliminateValueFragmentInteractionListener) {
            mListener = (OnEliminateValueFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnEliminateValueFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
