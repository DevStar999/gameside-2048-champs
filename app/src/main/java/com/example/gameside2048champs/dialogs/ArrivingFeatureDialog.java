package com.example.gameside2048champs.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.gameside2048champs.R;

public class ArrivingFeatureDialog extends Dialog {
    private LottieAnimationView arrivingFeatureLottie;
    private AppCompatTextView arrivingFeatureText;
    private LinearLayout arrivingFeatureButtonsLinearLayout;
    private AppCompatButton arrivingFeatureContinue;

    private void initialise() {
        arrivingFeatureLottie = findViewById(R.id.arriving_feature_lottie);
        arrivingFeatureText = findViewById(R.id.arriving_feature_text);
        arrivingFeatureButtonsLinearLayout = findViewById(R.id.arriving_feature_buttons_linear_layout);
        arrivingFeatureContinue = findViewById(R.id.arriving_feature_continue);
    }

    private void setVisibilityOfViews(int visibility) {
        arrivingFeatureLottie.setVisibility(visibility);
        arrivingFeatureText.setVisibility(visibility);
        arrivingFeatureButtonsLinearLayout.setVisibility(visibility);
    }

    public ArrivingFeatureDialog(@NonNull Context context) {
        super(context, R.style.CustomDialogTheme);
        setContentView(R.layout.dialog_arriving_feature);

        initialise();

        arrivingFeatureContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // First, the views will disappear, then the dialog box will close
                setVisibilityOfViews(View.INVISIBLE);
                new CountDownTimer(100, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        dismiss();
                    }
                }.start();
            }
        });
    }

    @Override
    public void show() {
        // Set the dialog to not focusable.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        // Show the dialog!
        super.show();

        // Set the dialog to immersive sticky mode
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN);

        // Clear the not focusable flag from the window
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        // First, the dialog box will open, then the views will show
        new CountDownTimer(400, 400) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                setVisibilityOfViews(View.VISIBLE);
            }
        }.start();
    }
}