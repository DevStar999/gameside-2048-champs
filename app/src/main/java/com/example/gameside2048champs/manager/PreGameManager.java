package com.example.gameside2048champs.manager;

import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.gameside2048champs.R;

import java.util.List;

public class PreGameManager {
    // Attributes to browse through the various game modes
    private final AppCompatImageView modeLeft;
    private final AppCompatImageView modeRight;
    private final AppCompatImageView sizeLeft;
    private final AppCompatImageView sizeRight;


    public PreGameManager(View currentFragmentLayoutView) {
        modeLeft = currentFragmentLayoutView.findViewById(R.id.game_mode_left_arrow_pregame_fragment_image_view);
        modeRight = currentFragmentLayoutView.findViewById(R.id.game_mode_right_arrow_pregame_fragment_image_view);
        sizeLeft = currentFragmentLayoutView.findViewById(R.id.game_size_left_arrow_pregame_fragment_image_view);
        sizeRight = currentFragmentLayoutView.findViewById(R.id.game_size_right_arrow_pregame_fragment_image_view);
    }

    public void updateModeBrowseIcons(String currentMode, List<String> allModes) {
        if (currentMode.equals(allModes.get(0)) &&
                !currentMode.equals(allModes.get(allModes.size() - 1))) {
            modeLeft.setVisibility(View.INVISIBLE); modeRight.setVisibility(View.VISIBLE);
        } else if (!currentMode.equals(allModes.get(0)) &&
                currentMode.equals(allModes.get(allModes.size() - 1))) {
            modeLeft.setVisibility(View.VISIBLE); modeRight.setVisibility(View.INVISIBLE);
        } else if (currentMode.equals(allModes.get(0)) &&
                currentMode.equals(allModes.get(allModes.size() - 1))) {
            modeLeft.setVisibility(View.INVISIBLE); modeRight.setVisibility(View.INVISIBLE);
        } else {
            modeLeft.setVisibility(View.VISIBLE); modeRight.setVisibility(View.VISIBLE);
        }
    }

    public void updateSizeBrowseIcons(String currentSize, List<String> allCurrentSizes) {
        if (currentSize.equals(allCurrentSizes.get(0)) &&
                !currentSize.equals(allCurrentSizes.get(allCurrentSizes.size() - 1))) {
            sizeLeft.setVisibility(View.INVISIBLE); sizeRight.setVisibility(View.VISIBLE);
        } else if (!currentSize.equals(allCurrentSizes.get(0)) &&
                currentSize.equals(allCurrentSizes.get(allCurrentSizes.size() - 1))) {
            sizeLeft.setVisibility(View.VISIBLE); sizeRight.setVisibility(View.INVISIBLE);
        } else if (currentSize.equals(allCurrentSizes.get(0)) &&
                currentSize.equals(allCurrentSizes.get(allCurrentSizes.size() - 1))) {
            sizeLeft.setVisibility(View.INVISIBLE); sizeRight.setVisibility(View.INVISIBLE);
        } else {
            sizeLeft.setVisibility(View.VISIBLE); sizeRight.setVisibility(View.VISIBLE);
        }
    }
}