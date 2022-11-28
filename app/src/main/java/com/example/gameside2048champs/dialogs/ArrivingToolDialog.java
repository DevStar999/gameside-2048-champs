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

public class ArrivingToolDialog extends Dialog {
    private LottieAnimationView arrivingToolLottie;
    private AppCompatTextView arrivingToolText;
    private LinearLayout arrivingToolButtonsLinearLayout;
    private AppCompatButton arrivingToolContinue;

    private void initialise() {
        arrivingToolLottie = findViewById(R.id.arriving_tool_lottie);
        arrivingToolText = findViewById(R.id.arriving_tool_text);
        arrivingToolButtonsLinearLayout = findViewById(R.id.arriving_tool_buttons_linear_layout);
        arrivingToolContinue = findViewById(R.id.arriving_tool_continue);
    }

    private void setVisibilityOfViews(int visibility) {
        arrivingToolLottie.setVisibility(visibility);
        arrivingToolText.setVisibility(visibility);
        arrivingToolButtonsLinearLayout.setVisibility(visibility);
    }

    public ArrivingToolDialog(@NonNull Context context) {
        super(context, R.style.CustomDialogTheme);
        setContentView(R.layout.dialog_arriving_tool);

        initialise();

        arrivingToolContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // First, the views will disappear, then the dialog box will close
                setVisibilityOfViews(View.INVISIBLE);
                new CountDownTimer(100, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {}
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
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        // Clear the not focusable flag from the window
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        // First, the dialog box will open, then the views will show
        new CountDownTimer(400, 400) {
            @Override
            public void onTick(long millisUntilFinished) {}
            @Override
            public void onFinish() {
                setVisibilityOfViews(View.VISIBLE);
            }
        }.start();
    }
}