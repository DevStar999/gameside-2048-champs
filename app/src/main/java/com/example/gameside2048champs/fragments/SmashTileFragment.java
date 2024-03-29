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
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.gameside2048champs.animations.ToolAnimationsUtility;
import com.example.gameside2048champs.R;

public class SmashTileFragment extends Fragment {
    private OnSmashTileFragmentInteractionListener mListener;
    private AppCompatImageView backButton;
    private LottieAnimationView rotatingLightLottie;
    private LottieAnimationView smashTilePreviewLottie;
    private AppCompatImageView toolUseCompletedImageView;
    private AppCompatCheckBox userActionCheckBox;
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

        backButton = view.findViewById(R.id.back_smash_tile_fragment_button);
        rotatingLightLottie = view.findViewById(R.id.rotating_light_smash_tile_fragment_lottie);
        smashTilePreviewLottie = view.findViewById(R.id.smash_tile_preview_smash_tile_fragment_lottie);
        toolUseCompletedImageView = view.findViewById(R.id.tool_title_completed_smash_tile_fragment_image_view);
        userActionCheckBox = view.findViewById(R.id.user_action_smash_tile_fragment_check_box);
        isToolUseComplete = true;

        // Making tool lottie view emerge so that it grabs attention during the tool fragment transition
        CountDownTimer postFragmentSetupTimer = new CountDownTimer(225, 10000) {
            @Override
            public void onTick(long l) {}
            @Override
            public void onFinish() {
                isToolUseComplete = false;
            }
        };
        new CountDownTimer(150, 10000) {
            @Override
            public void onTick(long l) {}
            @Override
            public void onFinish() {
                ToolAnimationsUtility.toolLottieEmergeAnimation(smashTilePreviewLottie, 200);
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
        backButton.setVisibility(View.GONE);
        toolUseCompletedImageView.setImageResource(R.drawable.completed_icon);
        userActionCheckBox.setChecked(true);
        rotatingLightLottie.pauseAnimation();
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
                ToolAnimationsUtility.standardToolsSmashTileTargetTileSetup(individualTileLottie);
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
                ToolAnimationsUtility.standardToolsSmashTileGridSetup(gridLottieView);
                gridLottieView.addAnimatorListener(gridLottieAnimatorListener);
                gridLottieView.playAnimation();
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        };

        // 2nd set of events is as follows
        ToolAnimationsUtility.standardToolsSmashTileTargetTileSelectionSetup(individualTileLottie);
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
            throw new RuntimeException(context + " must implement OnSmashTileFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
