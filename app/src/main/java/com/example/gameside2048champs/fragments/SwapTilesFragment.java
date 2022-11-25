package com.example.gameside2048champs.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.gameside2048champs.AnimationUtility;
import com.example.gameside2048champs.R;

public class SwapTilesFragment extends Fragment {
    private OnSwapTilesFragmentInteractionListener mListener;
    private AppCompatImageView backButton;
    private LottieAnimationView rotatingLightLottie;
    private LottieAnimationView swapTilesPreviewLottie;
    private AppCompatImageView toolUseCompletedImageView;
    private AppCompatTextView toolDescriptionTextView;
    private AppCompatCheckBox firstClickCheckBox;
    private AppCompatCheckBox secondClickCheckBox;
    private boolean isFirstClickDone;
    private boolean isSecondClickDone;
    private Pair<Integer, Integer> firstSwapTilePosition;
    private Pair<Integer, Integer> secondSwapTilePosition;

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
        toolDescriptionTextView = view.findViewById(R.id.tool_description_text_view_swap_tiles_fragment);
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
                AnimationUtility.toolLottieEmergeAnimation(swapTilesPreviewLottie, 575);
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
        AnimationUtility.specialToolsSwapTilesFirstTileSelectionSetup(firstSwapTileLottie);
        firstSwapTileLottie.playAnimation();
    }

    public void handleSwapTilesToolSecondClick() {
        /*
        // 1st set of events is as follows
        isSecondClickDone = true;
        toolUseCompletedImageView.setImageResource(R.drawable.completed_icon);
        toolDescriptionTextView.setVisibility(View.GONE);
        secondClickCheckBox.setChecked(true);
        */
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
            throw new RuntimeException(context.toString()
                    + " must implement OnSwapTilesFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
