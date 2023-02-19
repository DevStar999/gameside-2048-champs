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
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.gameside2048champs.NumericValueDisplay;
import com.example.gameside2048champs.R;

import java.util.HashMap;
import java.util.Map;

public class ToolsPageFragment extends Fragment {
    public static final String CURRENT_COINS = "currentCoins";
    private int currentCoins;
    private OnToolsPageFragmentInteractionListener mListener;
    private LinearLayout currentCoinsCountLinearLayout;
    private AppCompatTextView currentCoinsTextView;
    private AppCompatImageView currentCoinsAddCoinsImageView;
    private boolean isToolsChestOpen;
    private LottieAnimationView toolsChangeLottie;
    private AppCompatImageView toolsChangeClickAreaImageView;
    private Map<String, Integer> toolsCostMap;
    private LinearLayout standardToolsLinearLayout;
    private LinearLayout specialToolsLinearLayout;
    private LinearLayout toolsLottieLinearLayout;
    private AppCompatImageView standardToolsUndoImageView;
    private AppCompatImageView standardToolsSmashTileImageView;
    private AppCompatImageView standardToolsSwapTilesImageView;
    private AppCompatImageView specialToolsChangeValueImageView;
    private AppCompatImageView specialToolsEliminateValueImageView;
    private AppCompatImageView specialToolsDestroyAreaImageView;

    public ToolsPageFragment() {
        // Required empty public constructor
    }

    public static ToolsPageFragment newInstance(int currentCoins) {
        ToolsPageFragment fragment = new ToolsPageFragment();
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

    private void handleToolsChangeTransition() {
        if (!isToolsChestOpen) { // False i.e. the tools chest is NOT open. So, now we will open it.
            toolsChangeLottie.setSpeed(0.7f);
            isToolsChestOpen = true;
        } else { // True i.e. the tools chest is open. So, now we will close it.
            toolsChangeLottie.setSpeed(-0.7f); // Negative speed will make the animation play from end in reverse
            isToolsChestOpen = false;
        }
        toolsChangeLottie.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                toolsChangeClickAreaImageView.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isToolsChestOpen) {
                    standardToolsLinearLayout.setVisibility(View.VISIBLE);
                    specialToolsLinearLayout.setVisibility(View.GONE);
                    toolsChangeClickAreaImageView.setClickable(true);
                } else {
                    specialToolsLinearLayout.setVisibility(View.VISIBLE);
                    standardToolsLinearLayout.setVisibility(View.GONE);

                    LottieAnimationView leftView = toolsLottieLinearLayout.findViewById(R.id.tools_left_tools_page_fragment_lottie);
                    LottieAnimationView midView = toolsLottieLinearLayout.findViewById(R.id.tools_middle_tools_page_fragment_lottie);
                    LottieAnimationView rightView = toolsLottieLinearLayout.findViewById(R.id.tools_right_tools_page_fragment_lottie);
                    toolsLottieLinearLayout.setVisibility(View.VISIBLE);
                    midView.addAnimatorListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {}
                        @Override
                        public void onAnimationEnd(Animator animator) {
                            toolsChangeClickAreaImageView.setClickable(true);
                            toolsLottieLinearLayout.setVisibility(View.GONE);
                        }
                        @Override
                        public void onAnimationCancel(Animator animator) {}
                        @Override
                        public void onAnimationRepeat(Animator animator) {}
                    });
                    midView.playAnimation();
                    leftView.playAnimation();
                    rightView.playAnimation();
                }
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
        toolsChangeLottie.playAnimation();
    }

    private void settingOnClickListeners() {
        currentCoinsCountLinearLayout.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onToolsPageFragmentInteractionShopCoinsClicked();
            }
        });
        currentCoinsAddCoinsImageView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onToolsPageFragmentInteractionShopCoinsClicked();
            }
        });
        toolsChangeClickAreaImageView.setOnClickListener(v -> {
            handleToolsChangeTransition();
        });
        standardToolsUndoImageView.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.onToolsPageFragmentInteractionStandardToolsUndoClicked();
            }
        });
        standardToolsSmashTileImageView.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.onToolsPageFragmentInteractionStandardToolsSmashTileClicked();
            }
        });
        standardToolsSwapTilesImageView.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.onToolsPageFragmentInteractionStandardToolsSwapTilesClicked();
            }
        });
        specialToolsChangeValueImageView.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.onToolsPageFragmentInteractionSpecialToolsChangeValueClicked();
            }
        });
        specialToolsEliminateValueImageView.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.onToolsPageFragmentInteractionSpecialToolsEliminateValueClicked();
            }
        });
        specialToolsDestroyAreaImageView.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.onToolsPageFragmentInteractionSpecialToolsDestroyAreaClicked();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tools_page, container, false);

        currentCoinsCountLinearLayout = view.findViewById(R.id.current_coins_count_tools_page_fragment_linear_layout);
        currentCoinsTextView = view.findViewById(R.id.current_coins_tools_page_fragment_text_view);
        currentCoinsTextView.setText(NumericValueDisplay.getGeneralValueDisplay(currentCoins));
        currentCoinsAddCoinsImageView = view.findViewById(R.id.current_coins_add_coins_tools_page_fragment_image_view);
        isToolsChestOpen = false;
        toolsChangeLottie = view.findViewById(R.id.tools_change_tools_page_fragment_lottie);
        toolsChangeClickAreaImageView = view.findViewById(R.id.tools_change_click_area_tools_page_fragment_image_view);
        toolsCostMap = new HashMap<>() {{
            put("standardToolsUndoCost", 125);
            put("standardToolsSmashTileCost", 150);
            put("standardToolsSwapTilesCost", 200);
            put("specialToolsChangeValueCost", 400);
            put("specialToolsEliminateValueCost", 450);
            put("specialToolsDestroyAreaCost", 500);
        }};
        standardToolsLinearLayout = view.findViewById(R.id.standard_tools_tools_page_fragment_linear_layout);
        specialToolsLinearLayout = view.findViewById(R.id.special_tools_tools_page_fragment_linear_layout);
        toolsLottieLinearLayout = view.findViewById(R.id.tools_lottie_tools_page_fragment_linear_layout);
        standardToolsUndoImageView = view.findViewById(R.id.standard_tools_undo_icon_tools_page_fragment_image_view);
        standardToolsSmashTileImageView = view.findViewById(R.id.standard_tools_smash_icon_tools_page_fragment_image_view);
        standardToolsSwapTilesImageView = view.findViewById(R.id.standard_tools_swap_tiles_icon_tools_page_fragment_image_view);
        specialToolsChangeValueImageView = view.findViewById(R.id.special_tools_change_value_icon_tools_page_fragment_image_view);
        specialToolsEliminateValueImageView = view.findViewById(R.id.special_tools_eliminate_value_icon_tools_page_fragment_image_view);
        specialToolsDestroyAreaImageView = view.findViewById(R.id.special_tools_destroy_area_icon_tools_page_fragment_image_view);
        AppCompatTextView standardToolsUndoCostTextView =
                view.findViewById(R.id.standard_tools_undo_cost_tools_page_fragment_text_view);
        standardToolsUndoCostTextView.setText(NumericValueDisplay
                .getGeneralValueDisplay(toolsCostMap.get("standardToolsUndoCost")));
        AppCompatTextView standardToolsSmashTileCostTextView =
                view.findViewById(R.id.standard_tools_smash_cost_tools_page_fragment_text_view);
        standardToolsSmashTileCostTextView.setText(NumericValueDisplay
                .getGeneralValueDisplay(toolsCostMap.get("standardToolsSmashTileCost")));
        AppCompatTextView standardToolsSwapTilesCostTextView =
                view.findViewById(R.id.standard_tools_swap_tiles_cost_tools_page_fragment_text_view);
        standardToolsSwapTilesCostTextView.setText(NumericValueDisplay
                .getGeneralValueDisplay(toolsCostMap.get("standardToolsSwapTilesCost")));
        AppCompatTextView specialToolsChangeValueCostTextView =
                view.findViewById(R.id.special_tools_change_value_cost_tools_page_fragment_text_view);
        specialToolsChangeValueCostTextView.setText(NumericValueDisplay
                .getGeneralValueDisplay(toolsCostMap.get("specialToolsChangeValueCost")));
        AppCompatTextView specialToolsEliminateValueCostTextView =
                view.findViewById(R.id.special_tools_eliminate_value_cost_tools_page_fragment_text_view);
        specialToolsEliminateValueCostTextView.setText(NumericValueDisplay
                .getGeneralValueDisplay(toolsCostMap.get("specialToolsEliminateValueCost")));
        AppCompatTextView specialToolsDestroyAreaCostTextView =
                view.findViewById(R.id.special_tools_destroy_area_cost_tools_page_fragment_text_view);
        specialToolsDestroyAreaCostTextView.setText(NumericValueDisplay
                .getGeneralValueDisplay(toolsCostMap.get("specialToolsDestroyAreaCost")));

        settingOnClickListeners();

        return view;
    }

    public interface OnToolsPageFragmentInteractionListener {
        void onToolsPageFragmentInteractionStandardToolsUndoClicked();
        void onToolsPageFragmentInteractionStandardToolsSmashTileClicked();
        void onToolsPageFragmentInteractionStandardToolsSwapTilesClicked();
        void onToolsPageFragmentInteractionSpecialToolsChangeValueClicked();
        void onToolsPageFragmentInteractionSpecialToolsEliminateValueClicked();
        void onToolsPageFragmentInteractionSpecialToolsDestroyAreaClicked();
        void onToolsPageFragmentInteractionShopCoinsClicked();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof OnToolsPageFragmentInteractionListener) {
            mListener = (OnToolsPageFragmentInteractionListener) getParentFragment();
        } else {
            throw new RuntimeException("The parent fragment of ToolsPageFragment must implement " +
                    "OnToolsPageFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
