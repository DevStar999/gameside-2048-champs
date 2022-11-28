package com.example.gameside2048champs.fragments;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.gameside2048champs.AnimationUtility;
import com.example.gameside2048champs.R;
import com.example.gameside2048champs.enums.CellValues;

import java.util.ArrayList;
import java.util.List;

/* TODO -> Implement code to adjust layout of this fragment for showing game tile options which could be different from the
           current fixed 5 options of 2, 4, 8, 16 & 32 (After implementing a progression system in the game)
           (Noted in Main Project)
*/
public class ChangeValueFragment extends Fragment {
    private Context context;
    private OnChangeValueFragmentInteractionListener mListener;
    private AppCompatImageView backButton;
    private LottieAnimationView rotatingLightLottie;
    private LottieAnimationView changeValuePreviewLottie;
    private AppCompatImageView toolUseCompletedImageView;
    private AppCompatCheckBox firstClickCheckBox;
    private AppCompatCheckBox secondClickCheckBox;
    private LinearLayout selectedOptionLinearLayout;
    private AppCompatTextView selectedOptionValueTextView;
    private LinearLayout changeValueOptionsLinearLayout;
    private LinearLayout changeValueOptionsFirstRow;
    private LinearLayout changeValueOptionsSecondRow;
    private List<Integer> optionValues;
    private List<AppCompatTextView> valueOptionTextViews;
    private List<AppCompatImageView> valueOptionSelectionImageViews;
    private boolean isFirstClickDone;
    private boolean isSecondClickDone;
    private Pair<Integer, Integer> changeValueTilePosition;
    private LottieAnimationView changeValueTileLottie;
    private GridLayout gameCellLottieLayout;
    private LottieAnimationView gridLottieView;

    public ChangeValueFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private int dpToPx(int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    private void addValueOptionInLayout(int optionNumber, LinearLayout changeValueOptionsRow) {
        FrameLayout optionFrameLayout = new FrameLayout(context);
        optionFrameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        AppCompatTextView valueOptionTextView = new AppCompatTextView(context);
        valueOptionTextView.setTag("value_option_" + optionNumber + "_text_view");
        valueOptionTextView.setLayoutParams(new ViewGroup.LayoutParams(dpToPx(48), dpToPx(48)));
        valueOptionTextView.setGravity(Gravity.CENTER);
        valueOptionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        valueOptionTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        valueOptionTextView.setTypeface(valueOptionTextView.getTypeface(), Typeface.BOLD);
        valueOptionTextView.setVisibility(View.VISIBLE);

        int currentOptionValue = optionValues.get(optionNumber);
        CellValues cellValueCurrentOption = CellValues.getCellValueEnum(currentOptionValue);
        cellValueCurrentOption.setCellValue(currentOptionValue);
        valueOptionTextView.setText(String.valueOf(currentOptionValue));
        valueOptionTextView.setTextColor(ContextCompat.getColor(context, cellValueCurrentOption.getNumberColorResourceId()));
        valueOptionTextView.setBackgroundResource(cellValueCurrentOption.getBackgroundDrawableResourceId());

        AppCompatImageView valueOptionSelectionImageView = new AppCompatImageView(context);
        valueOptionSelectionImageView.setTag("value_option_" + optionNumber + "_selection_image_view");
        valueOptionSelectionImageView.setLayoutParams(new ViewGroup.LayoutParams(dpToPx(48), dpToPx(48)));
        valueOptionSelectionImageView.setPadding(4,6,4,6);
        valueOptionSelectionImageView.setVisibility(View.VISIBLE);
        valueOptionSelectionImageView.setBackgroundResource(R.drawable.change_value_fragment_selection_background);
        valueOptionSelectionImageView.setImageResource(R.drawable.change_value_options_locked);

        optionFrameLayout.addView(valueOptionTextView);
        valueOptionTextViews.add(valueOptionTextView);
        optionFrameLayout.addView(valueOptionSelectionImageView);
        valueOptionSelectionImageViews.add(valueOptionSelectionImageView);
        changeValueOptionsRow.addView(optionFrameLayout);
    }

    private void setValueOptionsInLayout() {
        // Setting the options in the layout
        for (int optionNumber = 0; optionNumber < 5; optionNumber++) {
            if (optionNumber <= 2) { // For 1st row of options
                addValueOptionInLayout(optionNumber, changeValueOptionsFirstRow);
            } else { // For 2nd row of options
                addValueOptionInLayout(optionNumber, changeValueOptionsSecondRow);
            }
        }
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
        for (int index = 0; index < valueOptionTextViews.size(); index++) {
            int finalIndex = index;
            valueOptionTextViews.get(index).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleChangeValueToolSecondClick(optionValues.get(finalIndex), finalIndex);
                }
            });
        }
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
        firstClickCheckBox = view.findViewById(R.id.first_click_change_value_fragment_check_box);
        secondClickCheckBox = view.findViewById(R.id.second_click_change_value_fragment_check_box);
        selectedOptionLinearLayout = view.findViewById(R.id.selected_option_linear_layout);
        selectedOptionValueTextView = view.findViewById(R.id.selected_option_value_text_view);
        changeValueOptionsLinearLayout = view.findViewById(R.id.change_value_options_linear_layout);
        changeValueOptionsFirstRow = view.findViewById(R.id.change_value_options_first_row);
        changeValueOptionsSecondRow = view.findViewById(R.id.change_value_options_second_row);
        optionValues = new ArrayList<>() {{ add(2); add(4); add(8); add(16); add(32); }};
        valueOptionTextViews = new ArrayList<>();
        valueOptionSelectionImageViews = new ArrayList<>();
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

        setValueOptionsInLayout();

        settingOnClickListeners();

        return view;
    }

    public boolean checkFirstClickStatus() {
        return isFirstClickDone;
    }

    public boolean checkSecondClickStatus() {
        return isSecondClickDone;
    }

    public void handleChangeValueToolFirstClick(LottieAnimationView changeValueTileLottie, GridLayout gameCellLottieLayout,
        LottieAnimationView gridLottieView, Pair<Integer, Integer> changeValueTilePosition) {
        // 1st set of events is as follows
        isFirstClickDone = true;
        firstClickCheckBox.setChecked(true);
        rotatingLightLottie.pauseAnimation();
        changeValuePreviewLottie.setProgress(0f);
        changeValuePreviewLottie.pauseAnimation();
        for (int index = 0; index < valueOptionSelectionImageViews.size(); index++) {
            valueOptionSelectionImageViews.get(index).setVisibility(View.GONE);
        }

        // 2nd set of events is as follows
        this.changeValueTilePosition = new Pair<>(changeValueTilePosition.first, changeValueTilePosition.second);
        this.changeValueTileLottie = changeValueTileLottie;
        this.gameCellLottieLayout = gameCellLottieLayout;
        this.gridLottieView = gridLottieView;

        // 3rd set of events is as follows - Setting the selection animation for the change value tile
        AnimationUtility.standardToolsChangeValueFirstClickSelectionSetup(this.changeValueTileLottie);
        this.changeValueTileLottie.playAnimation();
    }

    private void handleChangeValueToolSecondClick(int newValue, int optionNumber) {
        // 1st set of events is as follows
        isSecondClickDone = true;
        toolUseCompletedImageView.setImageResource(R.drawable.completed_icon);
        secondClickCheckBox.setChecked(true);
        CellValues cellValuesSelectedOption = CellValues.getCellValueEnum(optionValues.get(optionNumber));
        cellValuesSelectedOption.setCellValue(optionValues.get(optionNumber));
        selectedOptionValueTextView.setText(String.valueOf(cellValuesSelectedOption.getCellValue()));
        selectedOptionValueTextView.setTextColor(ContextCompat.getColor(context,
                cellValuesSelectedOption.getNumberColorResourceId()));
        selectedOptionValueTextView.setBackgroundResource(cellValuesSelectedOption.getBackgroundDrawableResourceId());
        changeValueOptionsLinearLayout.setVisibility(View.GONE);
        selectedOptionLinearLayout.setVisibility(View.VISIBLE);

        // Pre-processing for the 4th set of events is as follows
        Animator.AnimatorListener individualTileLottieSmashAnimatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {
                gameCellLottieLayout.setVisibility(View.GONE);
                isFirstClickDone = false; isSecondClickDone = false;
                if (mListener != null) {
                    mListener.onChangeValueFragmentInteractionProcessToolUse(changeValueTilePosition, newValue);
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
                ChangeValueFragment.this.gridLottieView.setBackgroundResource(0);
                ChangeValueFragment.this.gridLottieView.setSpeed(1f);
                ChangeValueFragment.this.gridLottieView.setVisibility(View.GONE);
                ChangeValueFragment.this.gridLottieView.removeAllAnimatorListeners();
                ChangeValueFragment.this.gameCellLottieLayout.setVisibility(View.VISIBLE);

                // 4th set of events is as follows
                AnimationUtility.standardToolsChangeValueTargetTileSetup(ChangeValueFragment.this.changeValueTileLottie);
                ChangeValueFragment.this.changeValueTileLottie.removeAllAnimatorListeners();
                ChangeValueFragment.this.changeValueTileLottie.addAnimatorListener(individualTileLottieSmashAnimatorListener);
                ChangeValueFragment.this.changeValueTileLottie.playAnimation();
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        };

        // Pre-processing for the 2nd set of events is as follows
        Animator.AnimatorListener changeValueTileLottieSelectionAnimatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {
                ChangeValueFragment.this.changeValueTileLottie.setPadding(0,0,0,0);
                ChangeValueFragment.this.changeValueTileLottie.setScaleX(1f);
                ChangeValueFragment.this.changeValueTileLottie.pauseAnimation();
                ChangeValueFragment.this.gameCellLottieLayout.setVisibility(View.GONE);

                // 3rd set of events is as follows
                AnimationUtility.standardToolsChangeValueGridSetup(ChangeValueFragment.this.gridLottieView);
                ChangeValueFragment.this.gridLottieView.addAnimatorListener(gridLottieAnimatorListener);
                ChangeValueFragment.this.gridLottieView.playAnimation();
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        };

        // 2nd set of events is as follows
        this.changeValueTileLottie.pauseAnimation();
        AnimationUtility.standardToolsChangeValueSecondClickSelectionSetup(this.changeValueTileLottie);
        this.changeValueTileLottie.addAnimatorListener(changeValueTileLottieSelectionAnimatorListener);
        this.changeValueTileLottie.playAnimation();
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
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
