package com.example.gameside2048champs.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.gameside2048champs.R;

public class GameLevelUpFragment extends Fragment {
    private static final String NEW_HIGHER_TILE_UNLOCKED_VALUE = "newHigherTileUnlockedValue";
    private static final String OLD_LOWER_TILE_REMOVED_VALUE = "oldLowerTileRemovedValue";
    private long newHigherTileUnlockedValue;
    private long oldLowerTileRemovedValue;
    private boolean isHigherTileRevealed;
    private OnGameLevelUpFragmentInteractionListener mListener;
    private AppCompatImageView viewPagerNavigationLeft;
    private AppCompatImageView viewPagerNavigationRight;
    private AppCompatImageView firstPageDotIndicator;
    private AppCompatImageView secondPageDotIndicator;
    private ViewPager2 viewPager2;

    public GameLevelUpFragment() {
        // Required empty public constructor
    }

    public static GameLevelUpFragment newInstance(long newHigherTileUnlockedValue, long oldLowerTileRemovedValue) {
        GameLevelUpFragment fragment = new GameLevelUpFragment();
        Bundle args = new Bundle();
        args.putLong(NEW_HIGHER_TILE_UNLOCKED_VALUE, newHigherTileUnlockedValue);
        args.putLong(OLD_LOWER_TILE_REMOVED_VALUE, oldLowerTileRemovedValue);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.newHigherTileUnlockedValue = getArguments().getLong(NEW_HIGHER_TILE_UNLOCKED_VALUE);
            this.oldLowerTileRemovedValue = getArguments().getLong(OLD_LOWER_TILE_REMOVED_VALUE);
        }
    }

    private void initialise(View layoutView) {
        isHigherTileRevealed = false;
        viewPagerNavigationLeft = layoutView.findViewById(R.id.navigation_arrow_left_game_level_up_dialog_fragment_image_view);
        viewPagerNavigationRight = layoutView.findViewById(R.id.navigation_arrow_right_game_level_up_dialog_fragment_image_view);
        firstPageDotIndicator = layoutView.findViewById(R.id.first_page_dot_game_level_up_dialog_fragment_image_view);
        secondPageDotIndicator = layoutView.findViewById(R.id.second_page_dot_game_level_up_dialog_fragment_image_view);
        viewPager2 = layoutView.findViewById(R.id.game_level_up_dialog_fragment_view_pager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requireActivity().getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        View view = inflater.inflate(R.layout.fragment_game_level_up, container, false);

        initialise(view);

        return view;
    }

    public interface OnGameLevelUpFragmentInteractionListener {
        // void onGameLevelUpFragmentInteractionBackClicked();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnGameLevelUpFragmentInteractionListener) {
            mListener = (OnGameLevelUpFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnGameLevelUpFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
