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

public class GameWinDialog extends Dialog {
    private LottieAnimationView gameWinLottie;
    private AppCompatTextView gameWinText;
    private LinearLayout gameWinButtonsLinearLayout;
    private AppCompatButton gameWinContinue;

    private void initialise() {
        gameWinLottie = findViewById(R.id.game_win_lottie);
        gameWinText = findViewById(R.id.game_win_text);
        gameWinButtonsLinearLayout = findViewById(R.id.game_win_buttons_linear_layout);
        gameWinContinue = findViewById(R.id.game_win_continue);
    }

    private void setVisibilityOfViews(int visibility) {
        gameWinLottie.setVisibility(visibility);
        gameWinText.setVisibility(visibility);
        gameWinButtonsLinearLayout.setVisibility(visibility);
    }

    public GameWinDialog(@NonNull Context context) {
        super(context, R.style.CustomDialogTheme);
        setContentView(R.layout.dialog_game_win);

        initialise();

        gameWinContinue.setOnClickListener(new View.OnClickListener() {
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
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN);

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
