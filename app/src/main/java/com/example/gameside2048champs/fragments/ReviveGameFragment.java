package com.example.gameside2048champs.fragments;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.gameside2048champs.NumericValueDisplay;
import com.example.gameside2048champs.R;

import java.util.HashMap;
import java.util.Map;

public class ReviveGameFragment extends Fragment {
    public static final String CURRENT_COINS = "currentCoins";
    private int currentCoins;
    private OnReviveGameFragmentInteractionListener mListener;
    private LinearLayout currentCoinsCountLinearLayout;
    private AppCompatTextView currentCoinsTextView;
    private AppCompatImageView currentCoinsAddCoinsImageView;
    private LottieAnimationView mysteryToolRevealLottie;
    private Map<String, Integer> toolsCostMap;
    private AppCompatImageView mysteryToolsReviveGameImageView;

    public ReviveGameFragment() {
        // Required empty public constructor
    }

    public static ReviveGameFragment newInstance(int currentCoins) {
        ReviveGameFragment fragment = new ReviveGameFragment();
        Bundle args = new Bundle();
        args.putInt(CURRENT_COINS, currentCoins);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.currentCoins = getArguments().getInt(CURRENT_COINS);
        }
    }

    private void setupMysteryToolLottie() {
        mysteryToolRevealLottie.setVisibility(View.VISIBLE);
        mysteryToolRevealLottie.setSpeed(0.5f);
        mysteryToolRevealLottie.setRepeatCount(0);
        mysteryToolRevealLottie.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}
            @Override
            public void onAnimationEnd(Animator animation) {
                mysteryToolRevealLottie.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        mysteryToolRevealLottie.playAnimation();
    }

    private void settingOnClickListeners() {
        currentCoinsCountLinearLayout.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onReviveGameFragmentInteractionShopCoinsClicked();
            }
        });
        currentCoinsAddCoinsImageView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onReviveGameFragmentInteractionShopCoinsClicked();
            }
        });
        mysteryToolsReviveGameImageView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onReviveGameFragmentInteractionMysteryToolsReviveGameClicked();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_revive_game, container, false);

        currentCoinsCountLinearLayout = view.findViewById(R.id.current_coins_count_revive_game_fragment_linear_layout);
        currentCoinsTextView = view.findViewById(R.id.current_coins_revive_game_fragment_text_view);
        currentCoinsTextView.setText(NumericValueDisplay.getGeneralValueDisplay(currentCoins));
        currentCoinsAddCoinsImageView = view.findViewById(R.id.current_coins_add_coins_revive_game_fragment_image_view);
        mysteryToolRevealLottie = view.findViewById(R.id.main_icon_overlay_revive_game_fragment_lottie);
        setupMysteryToolLottie();
        toolsCostMap = new HashMap<>() {{
            put("mysteryToolsReviveGameCost", 1000);
        }};
        AppCompatTextView mysteryToolsReviveGameCostTextView =
                view.findViewById(R.id.mystery_tools_revive_game_cost_revive_game_fragment_text_view);
        mysteryToolsReviveGameCostTextView.setText(NumericValueDisplay
                .getGeneralValueDisplay(toolsCostMap.get("mysteryToolsReviveGameCost")));
        mysteryToolsReviveGameImageView =
                view.findViewById(R.id.mystery_tools_revive_game_icon_revive_game_fragment_image_view);

        settingOnClickListeners();

        return view;
    }

    public interface OnReviveGameFragmentInteractionListener {
        void onReviveGameFragmentInteractionMysteryToolsReviveGameClicked();
        void onReviveGameFragmentInteractionShopCoinsClicked();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof OnReviveGameFragmentInteractionListener) {
            mListener = (OnReviveGameFragmentInteractionListener) getParentFragment();
        } else {
            throw new RuntimeException("The parent fragment of ReviveGameFragment must implement " +
                    "OnReviveGameFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
