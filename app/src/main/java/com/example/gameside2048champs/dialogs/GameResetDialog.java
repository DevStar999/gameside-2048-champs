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

public class GameResetDialog extends Dialog {
    private LottieAnimationView gameResetLottie;
    private AppCompatTextView gameResetText;
    private LinearLayout gameResetButtonsLinearLayout;
    private AppCompatButton gameResetReset;
    private AppCompatButton gameResetCancel;
    private GameResetDialogListener gameResetDialogListener;

    private void initialise() {
        gameResetLottie = findViewById(R.id.game_reset_lottie);
        gameResetText = findViewById(R.id.game_reset_text);
        gameResetButtonsLinearLayout = findViewById(R.id.game_reset_buttons_linear_layout);
        gameResetReset = findViewById(R.id.game_reset_reset);
        gameResetCancel = findViewById(R.id.game_reset_cancel);
    }

    private void setVisibilityOfViews(int visibility) {
        gameResetLottie.setVisibility(visibility);
        gameResetText.setVisibility(visibility);
        gameResetButtonsLinearLayout.setVisibility(visibility);
    }

    public GameResetDialog(@NonNull Context context) {
        super(context, R.style.CustomDialogTheme);
        setContentView(R.layout.dialog_game_reset);

        initialise();

        gameResetReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // First, the views will disappear, then the dialog box will close
                setVisibilityOfViews(View.INVISIBLE);
                new CountDownTimer(100, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {}
                    @Override
                    public void onFinish() {
                        gameResetDialogListener.getResponseOfResetDialog(true);
                        dismiss();
                    }
                }.start();
            }
        });
        gameResetCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // First, the views will disappear, then the dialog box will close
                setVisibilityOfViews(View.INVISIBLE);
                new CountDownTimer(100, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {}
                    @Override
                    public void onFinish() {
                        gameResetDialogListener.getResponseOfResetDialog(false);
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
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
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

    public void setGameResetDialogListener(GameResetDialogListener gameResetDialogListener) {
        this.gameResetDialogListener = gameResetDialogListener;
    }

    public interface GameResetDialogListener {
        void getResponseOfResetDialog(boolean response);
    }
}
