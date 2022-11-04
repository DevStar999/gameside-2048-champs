package com.example.gameside2048champs.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.gameside2048champs.AnimationUtility;
import com.example.gameside2048champs.R;

public class SmashTileFragment extends Fragment {
    private Context context;
    private OnSmashTileFragmentInteractionListener mListener;
    private SharedPreferences sharedPreferences;
    private AppCompatImageView backButton;
    private LottieAnimationView smashTilePreviewLottie;

    public SmashTileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void settingOnClickListeners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onSmashTileFragmentInteractionBackClicked();
                }
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

        sharedPreferences = context.getSharedPreferences("com.nerdcoredevelopment.game2048champsfinal", Context.MODE_PRIVATE);

        View view = inflater.inflate(R.layout.fragment_smash_tile, container, false);

        backButton = view.findViewById(R.id.back_button_smash_tile_fragment);
        smashTilePreviewLottie = view.findViewById(R.id.smash_tile_preview_lottie);

        // Making tool lottie view emerge so that it grabs attention during the tool fragment transition
        new CountDownTimer(300, 10000) {
            @Override
            public void onTick(long l) {}
            @Override
            public void onFinish() {
                AnimationUtility.toolLottieEmergeAnimation(smashTilePreviewLottie, 700);
            }
        }.start();

        settingOnClickListeners();

        return view;
    }

    public interface OnSmashTileFragmentInteractionListener {
        void onSmashTileFragmentInteractionBackClicked();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnSmashTileFragmentInteractionListener) {
            mListener = (OnSmashTileFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSmashTileFragmentInteractionListener");
        }
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
