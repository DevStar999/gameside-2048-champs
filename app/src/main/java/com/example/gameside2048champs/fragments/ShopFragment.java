package com.example.gameside2048champs.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.example.gameside2048champs.R;

public class ShopFragment extends Fragment {
    private Context context;
    private OnShopFragmentInteractionListener mListener;
    private SharedPreferences sharedPreferences;

    /* Views related to this fragment */
    private AppCompatTextView currentCoinsTextView;
    private AppCompatImageView backButton;
    private AppCompatButton addCoinsButton;

    /* Variables related to this fragment */
    private int currentCoins;

    public ShopFragment() {
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
                    mListener.onShopFragmentInteractionBackClicked();
                }
            }
        });
        addCoinsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentCoins += 1000;
                if (mListener != null) {
                    mListener.onShopFragmentInteractionUpdateCoins(currentCoins);
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

        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        currentCoinsTextView = view.findViewById(R.id.current_coins_shop_fragment_text_view);
        backButton = view.findViewById(R.id.title_back_shop_fragment_button);
        addCoinsButton = view.findViewById(R.id.add_coins_shop_fragment_button);

        currentCoins = sharedPreferences.getInt("currentCoins", 3000);
        currentCoinsTextView.setText(String.valueOf(currentCoins));

        settingOnClickListeners();

        return view;
    }

    public void updateCoinsShopFragment(int currentCoins) {
        if (mListener != null) {
            this.currentCoins = currentCoins;
            currentCoinsTextView.setText(String.valueOf(currentCoins));
        }
    }

    public interface OnShopFragmentInteractionListener {
        void onShopFragmentInteractionBackClicked();
        void onShopFragmentInteractionUpdateCoins(int currentCoins);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnShopFragmentInteractionListener) {
            mListener = (OnShopFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnShopFragmentInteractionListener");
        }
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}