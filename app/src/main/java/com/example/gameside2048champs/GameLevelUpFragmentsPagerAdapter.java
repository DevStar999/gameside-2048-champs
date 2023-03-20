package com.example.gameside2048champs;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class GameLevelUpFragmentsPagerAdapter extends FragmentStateAdapter {
    private final List<Fragment> gameLevelUpFragments = new ArrayList<>();

    public GameLevelUpFragmentsPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public void addFragment(Fragment fragment) {
        gameLevelUpFragments.add(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return gameLevelUpFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return gameLevelUpFragments.size();
    }
}
