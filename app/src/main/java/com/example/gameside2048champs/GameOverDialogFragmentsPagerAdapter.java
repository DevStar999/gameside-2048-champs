package com.example.gameside2048champs;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class GameOverDialogFragmentsPagerAdapter extends FragmentStateAdapter {
    private final List<Fragment> gameOverDialogFragments = new ArrayList<>();

    public GameOverDialogFragmentsPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public void addFragment(Fragment fragment) {
        gameOverDialogFragments.add(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return gameOverDialogFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return gameOverDialogFragments.size();
    }
}
