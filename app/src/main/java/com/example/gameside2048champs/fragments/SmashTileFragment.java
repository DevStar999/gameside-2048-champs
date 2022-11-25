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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.gameside2048champs.AnimationUtility;
import com.example.gameside2048champs.R;

public class SmashTileFragment extends Fragment {
    private OnSmashTileFragmentInteractionListener mListener;
    private AppCompatImageView backButton;
    private LottieAnimationView smashTilePreviewLottie;
    private AppCompatImageView toolUseCompletedImageView;
    private AppCompatTextView toolDescriptionTextView;
    private boolean isToolUseComplete;

    public SmashTileFragment() {
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
                    mListener.onSmashTileFragmentInteractionBackClicked();
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

        View view = inflater.inflate(R.layout.fragment_smash_tile, container, false);

        backButton = view.findViewById(R.id.back_button_smash_tile_fragment);
        smashTilePreviewLottie = view.findViewById(R.id.smash_tile_preview_lottie);
        toolUseCompletedImageView = view.findViewById(R.id.tool_title_completed_image_view_smash_tile_fragment);
        toolDescriptionTextView = view.findViewById(R.id.tool_description_text_view_smash_tile_fragment);
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
                AnimationUtility.toolLottieEmergeAnimation(smashTilePreviewLottie, 575);
                postFragmentSetupTimer.start();
            }
        }.start();

        settingOnClickListeners();

        return view;
    }

    public boolean checkToolUseState() {
        return isToolUseComplete;
    }

    public void handleTileToBeSmashed(GridLayout gameCellLottieLayout, LottieAnimationView individualTileLottie,
                                      LottieAnimationView gridLottieView, Pair<Integer, Integer> targetTilePosition) {
        // 1st set of events is as follows
        isToolUseComplete = true;
        toolUseCompletedImageView.setImageResource(R.drawable.completed_icon);
        toolDescriptionTextView.setVisibility(View.GONE);
        smashTilePreviewLottie.setProgress(0f);
        smashTilePreviewLottie.pauseAnimation();

        // Pre-processing for the 4th set of events is as follows
        Animator.AnimatorListener individualTileLottieSmashAnimatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {
                gameCellLottieLayout.setVisibility(View.GONE);
                isToolUseComplete = false;
                if (mListener != null) {
                    mListener.onSmashTileFragmentInteractionProcessToolUse(targetTilePosition);
                }
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        };

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
                AnimationUtility.normalToolsSmashTileTargetTileSetup(individualTileLottie);
                individualTileLottie.removeAllAnimatorListeners();
                individualTileLottie.addAnimatorListener(individualTileLottieSmashAnimatorListener);
                individualTileLottie.playAnimation();
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        };

        // Pre-processing for the 2nd set of events is as follows
        Animator.AnimatorListener individualTileLottieSelectionAnimatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {
                individualTileLottie.setPadding(0,0,0,0); // Removing the paddings
                individualTileLottie.setScaleX(1f);
                individualTileLottie.pauseAnimation();
                gameCellLottieLayout.setVisibility(View.GONE);

                // 3rd set of events is as follows
                AnimationUtility.normalToolsSmashTileGridSetup(gridLottieView);
                gridLottieView.addAnimatorListener(gridLottieAnimatorListener);
                gridLottieView.playAnimation();
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        };

        // 2nd set of events is as follows
        AnimationUtility.normalToolsSmashTileTargetTileSelectionSetup(individualTileLottie);
        individualTileLottie.addAnimatorListener(individualTileLottieSelectionAnimatorListener);
        individualTileLottie.playAnimation();
    }

    public interface OnSmashTileFragmentInteractionListener {
        void onSmashTileFragmentInteractionBackClicked();
        void onSmashTileFragmentInteractionProcessToolUse(Pair<Integer, Integer> targetTilePosition);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnSmashTileFragmentInteractionListener) {
            mListener = (OnSmashTileFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSmashTileFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
