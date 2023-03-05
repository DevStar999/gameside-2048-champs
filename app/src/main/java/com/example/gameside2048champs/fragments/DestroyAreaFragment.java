package com.example.gameside2048champs.fragments;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.gameside2048champs.NumericValueDisplay;
import com.example.gameside2048champs.animations.ToolAnimationsUtility;
import com.example.gameside2048champs.R;
import com.example.gameside2048champs.enums.CellValues;

import java.util.List;

public class DestroyAreaFragment extends Fragment {
    private OnDestroyAreaFragmentInteractionListener mListener;
    private AppCompatImageView backButton;
    private LottieAnimationView rotatingLightLottie;
    private LottieAnimationView destroyAreaPreviewLottie;
    private AppCompatImageView toolUseCompletedImageView;
    private AppCompatCheckBox firstClickCheckBox;
    private AppCompatCheckBox secondClickCheckBox;
    private AppCompatButton goBoomButton;
    private boolean isFirstClickDone;
    private boolean isSecondClickDone;
    private List<Pair<Integer, Integer>> destroyAreaTilesPositions;
    private List<LottieAnimationView> destroyAreaTilesLotties;
    private GridLayout gameCellLottieLayout;
    private LottieAnimationView gridLottieView;

    public DestroyAreaFragment() {
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
                    mListener.onDestroyAreaFragmentInteractionBackClicked();
                }
            }
        });
        firstClickCheckBox.setEnabled(false);
        secondClickCheckBox.setEnabled(false);
        goBoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDestroyAreaToolSecondClick();
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

        View view = inflater.inflate(R.layout.fragment_destroy_area, container, false);

        backButton = view.findViewById(R.id.back_destroy_area_fragment_button);
        rotatingLightLottie = view.findViewById(R.id.rotating_light_destroy_area_fragment_lottie);
        destroyAreaPreviewLottie = view.findViewById(R.id.destroy_area_preview_destroy_area_fragment_lottie);
        destroyAreaPreviewLottie.setMaxFrame(50);
        toolUseCompletedImageView = view.findViewById(R.id.tool_title_completed_destroy_area_fragment_image_view);
        firstClickCheckBox = view.findViewById(R.id.first_click_destroy_area_fragment_check_box);
        secondClickCheckBox = view.findViewById(R.id.second_click_destroy_area_fragment_check_box);
        goBoomButton = view.findViewById(R.id.go_boom_destroy_area_fragment_button);
        isFirstClickDone = true;
        isSecondClickDone = true;

        // Making tool lottie view emerge so that it grabs attention during the tool fragment transition
        CountDownTimer postFragmentSetupTimer = new CountDownTimer(225, 10000) {
            @Override
            public void onTick(long l) {}
            @Override
            public void onFinish() {
                isFirstClickDone = false; isSecondClickDone = false;
            }
        };
        new CountDownTimer(150, 10000) {
            @Override
            public void onTick(long l) {}
            @Override
            public void onFinish() {
                ToolAnimationsUtility.toolLottieEmergeAnimation(destroyAreaPreviewLottie, 200);
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

    public void handleDestroyAreaToolFirstClick(List<LottieAnimationView> destroyAreaTilesLotties,
                                                GridLayout gameCellLottieLayout, LottieAnimationView gridLottieView,
                                                List<Pair<Integer, Integer>> destroyAreaTilesPositions) {
        // 1st set of events is as follows
        isFirstClickDone = true;
        firstClickCheckBox.setChecked(true);
        rotatingLightLottie.pauseAnimation();
        destroyAreaPreviewLottie.setProgress(0f);
        destroyAreaPreviewLottie.pauseAnimation();

        // 2nd set of events is as follows
        this.destroyAreaTilesPositions = destroyAreaTilesPositions;
        this.destroyAreaTilesLotties = destroyAreaTilesLotties;
        this.gameCellLottieLayout = gameCellLottieLayout;
        this.gridLottieView = gridLottieView;
        secondClickCheckBox.setText("Confirm the selection of area to destroy with the button below");
        goBoomButton.setVisibility(View.VISIBLE);

        // 3rd set of events is as follows - Setting the selection animation for the destroy area tiles
        ToolAnimationsUtility.specialToolsDestroyAreaFirstClickSelectionSetup(destroyAreaTilesLotties);
        for (int destroyAreaTilesLottiesIndex = 0; destroyAreaTilesLottiesIndex < destroyAreaTilesLotties.size();
             destroyAreaTilesLottiesIndex++) {
            destroyAreaTilesLotties.get(destroyAreaTilesLottiesIndex).playAnimation();
        }
    }

    private Animator.AnimatorListener getIndividualTileLottieDestroyAnimatorListener(boolean isLast) {
        // Pre-processing for the 4th set of events is as follows
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}
            @Override
            public void onAnimationEnd(Animator animation) {
                if (isLast) {
                    gameCellLottieLayout.setVisibility(View.GONE);
                    isFirstClickDone = false; isSecondClickDone = false;
                    if (mListener != null) {
                        mListener.onDestroyAreaFragmentInteractionProcessToolUse(destroyAreaTilesPositions);
                    }
                }
            }
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        };
    }

    private Animator.AnimatorListener getGridLottieAnimatorListener() {
        // Pre-processing for the 3rd set of events is as follows
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}
            @Override
            public void onAnimationEnd(Animator animation) {
                DestroyAreaFragment.this.gridLottieView.setBackgroundResource(0);
                DestroyAreaFragment.this.gridLottieView.setSpeed(1f);
                DestroyAreaFragment.this.gridLottieView.setVisibility(View.GONE);
                DestroyAreaFragment.this.gridLottieView.removeAllAnimatorListeners();
                DestroyAreaFragment.this.gameCellLottieLayout.setVisibility(View.VISIBLE);

                // 4th set of events is as follows
                ToolAnimationsUtility.specialToolsDestroyAreaTargetTilesSetup(DestroyAreaFragment.this.destroyAreaTilesLotties);
                for (int index = 0; index < DestroyAreaFragment.this.destroyAreaTilesLotties.size(); index++) {
                    DestroyAreaFragment.this.destroyAreaTilesLotties.get(index).removeAllAnimatorListeners();
                    boolean isLast = (index == DestroyAreaFragment.this.destroyAreaTilesLotties.size() -1);
                    DestroyAreaFragment.this.destroyAreaTilesLotties.get(index).removeAllAnimatorListeners();
                    DestroyAreaFragment.this.destroyAreaTilesLotties.get(index)
                            .addAnimatorListener(getIndividualTileLottieDestroyAnimatorListener(isLast));
                    DestroyAreaFragment.this.destroyAreaTilesLotties.get(index).playAnimation();
                }
            }
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        };
    }

    private Animator.AnimatorListener getDestroyAreaTileLottieSelectionAnimatorListener(int indexOfDestroyAreaTilesLotties,
                                                                                        boolean isLast) {
        // Pre-processing for the 2nd set of events is as follows
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {
                DestroyAreaFragment.this.destroyAreaTilesLotties.get(indexOfDestroyAreaTilesLotties).setPadding(0,0,0,0);
                DestroyAreaFragment.this.destroyAreaTilesLotties.get(indexOfDestroyAreaTilesLotties).setScaleX(1f);
                DestroyAreaFragment.this.destroyAreaTilesLotties.get(indexOfDestroyAreaTilesLotties).pauseAnimation();

                // 3rd set of events is as follows
                if (isLast) {
                    DestroyAreaFragment.this.gameCellLottieLayout.setVisibility(View.GONE);
                    ToolAnimationsUtility.specialToolsDestroyAreaGridSetup(DestroyAreaFragment.this.gridLottieView);
                    DestroyAreaFragment.this.gridLottieView.addAnimatorListener(getGridLottieAnimatorListener());
                    DestroyAreaFragment.this.gridLottieView.playAnimation();
                }
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        };
    }

    private void handleDestroyAreaToolSecondClick() {
        // 1st set of events is as follows
        isSecondClickDone = true;
        backButton.setVisibility(View.GONE);
        toolUseCompletedImageView.setImageResource(R.drawable.completed_icon);
        secondClickCheckBox.setChecked(true);
        secondClickCheckBox.setText("Confirm the selection of area to destroy");
        goBoomButton.setVisibility(View.GONE);

        // 2nd set of events is as follows
        for (int index = 0; index < destroyAreaTilesLotties.size(); index++) {
            destroyAreaTilesLotties.get(index).pauseAnimation();
            ToolAnimationsUtility.specialToolsDestroyAreaSecondClickSelectionSetup(destroyAreaTilesLotties.get(index));
            boolean isLast = (index == destroyAreaTilesLotties.size() - 1);
            destroyAreaTilesLotties.get(index)
                    .addAnimatorListener(getDestroyAreaTileLottieSelectionAnimatorListener(index, isLast));
            destroyAreaTilesLotties.get(index).playAnimation();
        }
    }

    public interface OnDestroyAreaFragmentInteractionListener {
        void onDestroyAreaFragmentInteractionBackClicked();
        void onDestroyAreaFragmentInteractionProcessToolUse(List<Pair<Integer, Integer>> destroyAreaTilesPositions);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnDestroyAreaFragmentInteractionListener) {
            mListener = (OnDestroyAreaFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnDestroyAreaFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
