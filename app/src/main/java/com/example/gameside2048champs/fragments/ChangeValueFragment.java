package com.example.gameside2048champs.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.gameside2048champs.AnimationUtility;
import com.example.gameside2048champs.R;

public class ChangeValueFragment extends Fragment {
    private OnChangeValueFragmentInteractionListener mListener;
    private AppCompatImageView backButton;
    private LottieAnimationView rotatingLightLottie;
    private LottieAnimationView changeValuePreviewLottie;
    private AppCompatImageView toolUseCompletedImageView;
    private AppCompatTextView toolDescriptionTextView;
    private AppCompatCheckBox firstClickCheckBox;
    private AppCompatCheckBox secondClickCheckBox;
    private boolean isFirstClickDone;
    private boolean isSecondClickDone;
    private Pair<Integer, Integer> changeValueTilePosition;
    private LottieAnimationView changeValueTileLottie;

    public ChangeValueFragment() {
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
                    mListener.onChangeValueFragmentInteractionBackClicked();
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

        View view = inflater.inflate(R.layout.fragment_change_value, container, false);

        backButton = view.findViewById(R.id.back_button_change_value_fragment);
        rotatingLightLottie = view.findViewById(R.id.rotating_light_change_value_fragment);
        changeValuePreviewLottie = view.findViewById(R.id.change_value_preview_lottie);
        toolUseCompletedImageView = view.findViewById(R.id.tool_title_completed_image_view_change_value_fragment);
        toolDescriptionTextView = view.findViewById(R.id.tool_description_text_view_change_value_fragment);
        firstClickCheckBox = view.findViewById(R.id.first_click_change_value_fragment_check_box);
        secondClickCheckBox = view.findViewById(R.id.second_click_change_value_fragment_check_box);
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
                AnimationUtility.toolLottieEmergeAnimation(changeValuePreviewLottie, 575);
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

    public void handleChangeValueToolFirstClick(LottieAnimationView changeValueTileLottie,
                                                Pair<Integer, Integer> changeValueTilePosition) {
        // 1st set of events is as follows
        isFirstClickDone = true;
        firstClickCheckBox.setChecked(true);
        rotatingLightLottie.pauseAnimation();
        changeValuePreviewLottie.setProgress(0f);
        changeValuePreviewLottie.pauseAnimation();

        // 2nd set of events is as follows - Setting the selection animation for the change value tile
        this.changeValueTilePosition = new Pair<>(changeValueTilePosition.first, changeValueTilePosition.second);
        this.changeValueTileLottie = changeValueTileLottie;
        AnimationUtility.normalToolsChangeValueFirstClickSelectionSetup(this.changeValueTileLottie);
        this.changeValueTileLottie.playAnimation();
    }

    public void handleChangeValueToolSecondClick() {

    }

    public interface OnChangeValueFragmentInteractionListener {
        void onChangeValueFragmentInteractionBackClicked();
        void onChangeValueFragmentInteractionProcessToolUse(Pair<Integer, Integer> changeValueTilePosition, int newValue);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnChangeValueFragmentInteractionListener) {
            mListener = (OnChangeValueFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChangeValueFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
