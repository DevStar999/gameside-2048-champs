package com.example.gameside2048champs;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.gameside2048champs.fragments.GameSummaryFragment;
import com.example.gameside2048champs.fragments.ReviveGameFragment;
import com.example.gameside2048champs.fragments.ToolsPageFragment;

public class GameOverDialogFragmentsPagerAdapter extends FragmentStateAdapter {
    public GameOverDialogFragmentsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1: {
                return new ReviveGameFragment();
            }
            case 2: {
                return new GameSummaryFragment();
            }
            default: {
                return new ToolsPageFragment();
            }
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
