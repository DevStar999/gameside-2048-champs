package com.example.gameside2048champs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.gameside2048champs.enums.BlockDesigns;
import com.example.gameside2048champs.enums.GameModes;

public class GameLayoutProvider {
    private static int dpToPx(int dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public static void provideGameFrameLayout(Context context, ConstraintLayout rootGameConstraintLayout,
                                              FrameLayout gameFrameLayout, GameModes gameMode) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "com.nerdcoredevelopment.game2048champsfinal", Context.MODE_PRIVATE);
        int padding = dpToPx(gameMode.getGameLayoutProperties().getSpacing(), context);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(rootGameConstraintLayout);
        constraintSet.setDimensionRatio(gameFrameLayout.getId(), gameMode.getGameLayoutProperties().getDimensionRatio());
        constraintSet.applyTo(rootGameConstraintLayout);

        // Adding a layer of background cells
        GridLayout gameBackgroundGridLayout = new GridLayout(context);
        gameBackgroundGridLayout.setId(R.id.game_background_grid_layout);
        gameBackgroundGridLayout.setRowCount(gameMode.getRows());
        gameBackgroundGridLayout.setColumnCount(gameMode.getRows());
        gameBackgroundGridLayout.setBackground(context.getDrawable(R.drawable.rounded_corners_board));
        for (int i = 0; i < gameMode.getRows(); i++) {
            for (int j = 0; j < gameMode.getColumns(); j++) {
                AppCompatImageView imageView = new AppCompatImageView(context);
                if (gameMode.getBlockCells().get(i).get(j).equals(-1L)) {
                    BlockDesigns selectedBlockDesign = BlockDesigns.valueOf(sharedPreferences
                            .getString("selectedBlockDrawableEnumName", BlockDesigns.BLOCK_PIRATE.name()));
                    imageView.setImageResource(selectedBlockDesign.getBlockDrawableResourceId());
                    imageView.setScaleX(selectedBlockDesign.getBlockDrawableScaleX());
                    imageView.setScaleY(selectedBlockDesign.getBlockDrawableScaleY());
                    imageView.setRotationX(selectedBlockDesign.getBlockDrawableRotationX());
                    imageView.setRotationY(selectedBlockDesign.getBlockDrawableRotationY());
                } else {
                    imageView.setImageResource(R.drawable.cell_empty);
                }

                imageView.setTag("emptyCell" + i + j);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.height = 1;
                params.width = 1;
                params.topMargin = params.bottomMargin = params.leftMargin = params.rightMargin = padding;
                params.rowSpec = GridLayout.spec(i,1f);
                params.columnSpec = GridLayout.spec(j,1f);
                params.setGravity(Gravity.FILL);
                imageView.setLayoutParams(params);
                gameBackgroundGridLayout.addView(imageView);
            }
        }
        gameBackgroundGridLayout.setPadding(padding, padding, padding, padding);

        // Adding a layer of actual game cells
        GridLayout gameGridLayout = new GridLayout(context);
        gameGridLayout.setId(R.id.game_grid_layout);
        gameGridLayout.setRowCount(gameMode.getRows());
        gameGridLayout.setColumnCount(gameMode.getColumns());
        for (int i = 0; i < gameMode.getRows(); i++) {
            for (int j = 0; j < gameMode.getColumns(); j++) {
                AppCompatTextView textView = new AppCompatTextView(context);
                textView.setGravity(Gravity.CENTER);
                textView.setTag("gameCell" + i + j);
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setTypeface(ResourcesCompat.getFont(context, R.font.dosis_extra_bold), Typeface.BOLD);
                textView.setBackground(context.getDrawable(R.drawable.cell_empty));
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.height = 1;
                params.width = 1;
                params.topMargin = params.bottomMargin = params.leftMargin = params.rightMargin = padding;
                params.rowSpec = GridLayout.spec(i,1f);
                params.columnSpec = GridLayout.spec(j,1f);
                params.setGravity(Gravity.FILL);
                textView.setLayoutParams(params);
                gameGridLayout.addView(textView);
            }
        }
        gameGridLayout.setPadding(padding, padding, padding, padding);

        // Adding a layer which consists of single full grid sized lottie animation view
        LottieAnimationView gridLottieView = new LottieAnimationView(context);
        gridLottieView.setId(R.id.grid_lottie_view);
        gridLottieView.setVisibility(View.GONE);

        gameFrameLayout.addView(gameBackgroundGridLayout);
        gameFrameLayout.addView(gameGridLayout);
        gameFrameLayout.addView(gridLottieView);
    }
}
