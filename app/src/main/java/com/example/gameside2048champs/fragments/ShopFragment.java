package com.example.gameside2048champs.fragments;

import android.app.Activity;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.gameside2048champs.R;
import com.qonversion.android.sdk.Qonversion;
import com.qonversion.android.sdk.QonversionError;
import com.qonversion.android.sdk.QonversionOfferingsCallback;
import com.qonversion.android.sdk.QonversionPermissionsCallback;
import com.qonversion.android.sdk.dto.QPermission;
import com.qonversion.android.sdk.dto.offerings.QOffering;
import com.qonversion.android.sdk.dto.offerings.QOfferings;
import com.qonversion.android.sdk.dto.products.QProduct;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopFragment extends Fragment {
    private Context context;
    private OnShopFragmentInteractionListener mListener;
    private SharedPreferences sharedPreferences;

    /* Views related to this fragment */
    private AppCompatTextView currentCoinsTextView;
    private AppCompatImageView backButton;
    private AppCompatButton addCoinsButton;
    private List<ConstraintLayout> shopCoinsConstraintLayouts;
    private List<AppCompatButton> shopCoinsPurchaseButtons;

    /* Variables related to this fragment */
    private int currentCoins;
    private Map<String, Integer> coinsReward;

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

    private void handlePurchaseOfCoins(QProduct qProduct, String productIdPrefix) {
        Qonversion.purchase((Activity) context, qProduct, new QonversionPermissionsCallback() {
            @Override
            public void onSuccess(@NotNull Map<String, QPermission> permissions) {
                currentCoins += coinsReward.get(productIdPrefix);
                if (mListener != null) {
                    mListener.onShopFragmentInteractionUpdateCoins(currentCoins);
                }
            }
            @Override
            public void onError(@NotNull QonversionError error) {
                // TODO -> Create a purchase failed dialog
            }
        });
    }

    private void loadItemPrices() {
        Qonversion.offerings(new QonversionOfferingsCallback() {
            @Override
            public void onSuccess(@NotNull QOfferings offerings) {
                if (!offerings.getAvailableOfferings().isEmpty()) {
                    for (QOffering currentOffering: offerings.getAvailableOfferings()) {
                        QProduct qProduct = currentOffering.getProducts().get(0);
                        String storeId = qProduct.getStoreID();
                        String prefix = "coins_level";
                        if (storeId != null && storeId.startsWith(prefix)) {
                            int level = Integer.parseInt(String.valueOf(storeId.charAt(prefix.length())));
                            level--;
                            if (qProduct.getSkuDetail() != null && !qProduct.getSkuDetail().getPrice().isEmpty()) {
                                shopCoinsPurchaseButtons.get(level).setText(qProduct.getSkuDetail().getPrice());
                            }
                            String finalPrefix = prefix + (level+1);
                            shopCoinsConstraintLayouts.get(level)
                                    .setOnClickListener(view -> handlePurchaseOfCoins(qProduct, finalPrefix));
                            shopCoinsPurchaseButtons.get(level)
                                    .setOnClickListener(view -> handlePurchaseOfCoins(qProduct, finalPrefix));
                        }
                    }
                }
            }
            @Override
            public void onError(@NotNull QonversionError error) {
                loadItemPrices();
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
        addCoinsButton = view.findViewById(R.id.add_coins_button_shop_fragment);

        shopCoinsConstraintLayouts = new ArrayList<>();
        for (int level = 1; level <= 7; level++) {
            int layoutResId = context.getResources().getIdentifier("shop_coins_level" + level +
                    "_constraint_layout", "id", context.getPackageName());
            shopCoinsConstraintLayouts.add(view.findViewById(layoutResId));
        }

        shopCoinsPurchaseButtons = new ArrayList<>();
        for (int level = 1; level <= 7; level++) {
            int layoutResId = context.getResources().getIdentifier("shop_coins_level" + level +
                    "_purchase_button", "id", context.getPackageName());
            shopCoinsPurchaseButtons.add(view.findViewById(layoutResId));
        }

        currentCoins = sharedPreferences.getInt("currentCoins", 3000);
        currentCoinsTextView.setText(String.valueOf(currentCoins));
        coinsReward = new HashMap<>() {{
            put("coins_level1", 1000); put("coins_level2", 3000); put("coins_level3", 5000);
            put("coins_level4", 10000); put("coins_level5", 25000);
            put("coins_level6", 50000); put("coins_level7", 100000);
        }};

        settingOnClickListeners();

        loadItemPrices();

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