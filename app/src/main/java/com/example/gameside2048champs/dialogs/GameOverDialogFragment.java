package com.example.gameside2048champs.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.gameside2048champs.GameOverDialogFragmentsPagerAdapter;
import com.example.gameside2048champs.R;
import com.example.gameside2048champs.ZoomOutPageTransformer;
import com.example.gameside2048champs.fragments.GameSummaryFragment;
import com.example.gameside2048champs.fragments.ReviveGameFragment;
import com.example.gameside2048champs.fragments.ToolsPageFragment;

public class GameOverDialogFragment extends Fragment implements
        ToolsPageFragment.OnToolsPageFragmentInteractionListener,
        ReviveGameFragment.OnReviveGameFragmentInteractionListener,
        GameSummaryFragment.OnGameSummaryFragmentInteractionListener {
    private static final String CURRENT_SCORE = "currentScore";
    private static final String BEST_SCORE = "bestScore";
    private long currentScore;
    private long bestScore;
    private Context context;
    private OnGameOverDialogFragmentInteractionListener mListener;
    private LinearLayout pageSelectedDotIndicatorLinearLayout;
    private AppCompatImageView firstPageDotIndicator;
    private AppCompatImageView secondPageDotIndicator;
    private AppCompatImageView thirdPageDotIndicator;
    private AppCompatImageView viewPagerNavigationLeft;
    private AppCompatImageView viewPagerNavigationRight;
    private ViewPager2 viewPager2;
    private GameOverDialogFragmentsPagerAdapter pagerAdapter;

    public GameOverDialogFragment() {
        // Required empty public constructor
    }

    public static GameOverDialogFragment newInstance(long currentScore, long bestScore) {
        GameOverDialogFragment fragment = new GameOverDialogFragment();
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

    private void setVisibilityOfViews(int visibility) {
        viewPager2.setVisibility(visibility);
        pageSelectedDotIndicatorLinearLayout.setVisibility(visibility);
    }

    private void handleNavigationArrowsChange(int pageIndex) {
        if (pageIndex == 0) {
            viewPagerNavigationLeft.setVisibility(View.INVISIBLE);
            viewPagerNavigationRight.setVisibility(View.VISIBLE);
        } else if (pageIndex == 1) {
            viewPagerNavigationLeft.setVisibility(View.VISIBLE);
            viewPagerNavigationRight.setVisibility(View.VISIBLE);
        } else if (pageIndex == 2) {
            viewPagerNavigationLeft.setVisibility(View.VISIBLE);
            viewPagerNavigationRight.setVisibility(View.INVISIBLE);
        }
    }

    private void handleDotIndicatorsChange(int pageIndex) {
        if (pageIndex == 0) {
            firstPageDotIndicator.setImageResource(R.drawable.dialog_page_selected);
            secondPageDotIndicator.setImageResource(R.drawable.dialog_page_not_selected);
            thirdPageDotIndicator.setImageResource(R.drawable.dialog_page_not_selected);
        } else if (pageIndex == 1) {
            firstPageDotIndicator.setImageResource(R.drawable.dialog_page_not_selected);
            secondPageDotIndicator.setImageResource(R.drawable.dialog_page_selected);
            thirdPageDotIndicator.setImageResource(R.drawable.dialog_page_not_selected);
        } else if (pageIndex == 2) {
            firstPageDotIndicator.setImageResource(R.drawable.dialog_page_not_selected);
            secondPageDotIndicator.setImageResource(R.drawable.dialog_page_not_selected);
            thirdPageDotIndicator.setImageResource(R.drawable.dialog_page_selected);
        }
    }

    private void setupViewPager() {
        viewPager2.setAdapter(pagerAdapter);
        viewPager2.setPageTransformer(new ZoomOutPageTransformer());
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                handleNavigationArrowsChange(position);
                handleDotIndicatorsChange(position);
            }
        });
    }

    private void settingOnClickListeners() {
        viewPagerNavigationLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pageIndex = viewPager2.getCurrentItem();
                if (pageIndex == 0) { viewPager2.setCurrentItem(2, true); }
                else if (pageIndex == 1) { viewPager2.setCurrentItem(0, true); }
                else if (pageIndex == 2) { viewPager2.setCurrentItem(1, true); }
            }
        });
        viewPagerNavigationRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pageIndex = viewPager2.getCurrentItem();
                if (pageIndex == 0) { viewPager2.setCurrentItem(1, true); }
                else if (pageIndex == 1) { viewPager2.setCurrentItem(2, true); }
                else if (pageIndex == 2) { viewPager2.setCurrentItem(0, true); }
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

        View view = inflater.inflate(R.layout.fragment_game_over_dialog, container, false);

        pageSelectedDotIndicatorLinearLayout = view.findViewById(R.id.
                page_selected_dot_indicator_game_over_dialog_fragment_linear_layout);
        firstPageDotIndicator = view.findViewById(R.id.first_page_dot_game_over_dialog_fragment_image_view);
        secondPageDotIndicator = view.findViewById(R.id.second_page_dot_game_over_dialog_fragment_image_view);
        thirdPageDotIndicator = view.findViewById(R.id.third_page_dot_game_over_dialog_fragment_image_view);
        viewPagerNavigationLeft = view.findViewById(R.id.navigation_arrow_left_game_over_dialog_fragment_image_view);
        viewPagerNavigationRight = view.findViewById(R.id.navigation_arrow_right_game_over_dialog_fragment_image_view);
        viewPager2 = view.findViewById(R.id.game_over_dialog_fragment_view_pager);
        pagerAdapter = new GameOverDialogFragmentsPagerAdapter(getChildFragmentManager(), getLifecycle());

        setupViewPager();

        settingOnClickListeners();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new CountDownTimer(350, 10000) {
            @Override
            public void onTick(long millisUntilFinished) {}
            @Override
            public void onFinish() {
                setVisibilityOfViews(View.VISIBLE);
            }
        }.start();
    }

    @Override
    public void onStop() {
        setVisibilityOfViews(View.INVISIBLE);
        super.onStop();
    }

    public interface OnGameOverDialogFragmentInteractionListener {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnGameOverDialogFragmentInteractionListener) {
            mListener = (OnGameOverDialogFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnGameOverDialogFragmentInteractionListener");
        }
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
