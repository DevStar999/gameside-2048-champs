package com.example.gameside2048champs.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.example.gameside2048champs.NumericValueDisplay;
import com.example.gameside2048champs.R;

public class GameSummaryFragment extends Fragment {
    private static final String CURRENT_SCORE = "currentScore";
    private static final String BEST_SCORE = "bestScore";
    private long currentScore;
    private long bestScore;
    private OnGameSummaryFragmentInteractionListener mListener;
    private LottieAnimationView gameOverLottie;
    private LinearLayout newHighScoreSummaryLinearLayout;
    private AppCompatTextView newHighScoreTextView;
    private ConstraintLayout scoresSummaryConstraintLayout;
    private AppCompatTextView currentScoreTextView;
    private AppCompatTextView bestScoreTextView;
    private AppCompatButton mainMenuButton;
    private AppCompatButton playAgainButton;

    public GameSummaryFragment() {
        // Required empty public constructor
    }

    public static GameSummaryFragment newInstance(long currentScore, long bestScore) {
        GameSummaryFragment fragment = new GameSummaryFragment();
        Bundle args = new Bundle();
        args.putLong(CURRENT_SCORE, currentScore);
        args.putLong(BEST_SCORE, bestScore);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.currentScore = getArguments().getLong(CURRENT_SCORE);
            this.bestScore = getArguments().getLong(BEST_SCORE);
        }
    }

    private void settingOnClickListeners() {
        mainMenuButton.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onGameSummaryFragmentInteractionMainMenuClicked();
            }
        });
        playAgainButton.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onGameSummaryFragmentInteractionPlayAgainClicked();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_summary, container, false);

        gameOverLottie = view.findViewById(R.id.game_over_game_summary_fragment_lottie);
        newHighScoreSummaryLinearLayout = view.findViewById(R.id.new_high_score_summary_game_summary_fragment_linear_layout);
        newHighScoreTextView = view.findViewById(R.id.new_high_score_game_summary_fragment_text_view);
        scoresSummaryConstraintLayout = view.findViewById(R.id.scores_summary_game_summary_fragment_constraint_layout);
        currentScoreTextView = view.findViewById(R.id.score_game_summary_fragment_text_view);
        bestScoreTextView = view.findViewById(R.id.high_score_game_summary_fragment_text_view);
        if (currentScore == bestScore) {
            newHighScoreSummaryLinearLayout.setVisibility(View.VISIBLE);
            scoresSummaryConstraintLayout.setVisibility(View.GONE);
            newHighScoreTextView.setText(NumericValueDisplay.getScoreValueDisplay(this.bestScore));
        } else {
            newHighScoreSummaryLinearLayout.setVisibility(View.GONE);
            scoresSummaryConstraintLayout.setVisibility(View.VISIBLE);
            currentScoreTextView.setText(NumericValueDisplay.getScoreValueDisplay(this.currentScore));
            bestScoreTextView.setText(NumericValueDisplay.getScoreValueDisplay(this.bestScore));
        }
        mainMenuButton = view.findViewById(R.id.main_menu_game_summary_fragment_button);
        playAgainButton = view.findViewById(R.id.play_again_game_summary_fragment_button);

        settingOnClickListeners();

        return view;
    }

    public interface OnGameSummaryFragmentInteractionListener {
        void onGameSummaryFragmentInteractionMainMenuClicked();
        void onGameSummaryFragmentInteractionPlayAgainClicked();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof OnGameSummaryFragmentInteractionListener) {
            mListener = (OnGameSummaryFragmentInteractionListener) getParentFragment();
        } else {
            throw new RuntimeException("The parent fragment of GameSummaryFragment must implement " +
                    "OnGameSummaryFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
