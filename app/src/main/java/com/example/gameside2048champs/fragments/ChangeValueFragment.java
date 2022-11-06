package com.example.gameside2048champs.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.gameside2048champs.AnimationUtility;
import com.example.gameside2048champs.R;

public class ChangeValueFragment extends Fragment {
    private OnChangeValueFragmentInteractionListener mListener;
    private ConstraintLayout rootLayoutOfFragment;
    private AppCompatImageView backButton;
    private LottieAnimationView changeValuePreviewLottie;
    private AppCompatImageView toolUseCompletedImageView;
    private AppCompatTextView toolDescriptionTextView;
    private boolean isToolUseComplete;

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

        rootLayoutOfFragment = view.findViewById(R.id.change_value_fragment_root_layout);
        backButton = view.findViewById(R.id.back_button_change_value_fragment);
        changeValuePreviewLottie = view.findViewById(R.id.change_value_preview_lottie);
        toolUseCompletedImageView = view.findViewById(R.id.tool_title_completed_image_view_change_value_fragment);
        toolDescriptionTextView = view.findViewById(R.id.tool_description_text_view_change_value_fragment);
        isToolUseComplete = false;

        // Making tool lottie view emerge so that it grabs attention during the tool fragment transition
        new CountDownTimer(300, 10000) {
            @Override
            public void onTick(long l) {}
            @Override
            public void onFinish() {
                AnimationUtility.toolLottieEmergeAnimation(changeValuePreviewLottie, 700);
            }
        }.start();

        settingOnClickListeners();

        return view;
    }

    public boolean checkToolUseState() {
        return isToolUseComplete;
    }

    public interface OnChangeValueFragmentInteractionListener {
        void onChangeValueFragmentInteractionBackClicked();
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
