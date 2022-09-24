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

public class GameUndoDialog extends Dialog {
    private LottieAnimationView gameUndoLottie;
    private AppCompatTextView gameUndoText;
    private LinearLayout gameUndoButtonsLinearLayout;
    private AppCompatButton gameUndoContinue;

    private void initialise() {
        gameUndoLottie = findViewById(R.id.game_undo_lottie);
        gameUndoText = findViewById(R.id.game_undo_text);
        gameUndoButtonsLinearLayout = findViewById(R.id.game_undo_buttons_linear_layout);
        gameUndoContinue = findViewById(R.id.game_undo_continue);
    }

    private void setVisibilityOfViews(int visibility) {
        gameUndoLottie.setVisibility(visibility);
        gameUndoText.setVisibility(visibility);
        gameUndoButtonsLinearLayout.setVisibility(visibility);
    }

    public GameUndoDialog(@NonNull Context context, String undoMessageText) {
        super(context, R.style.CustomDialogTheme);
        setContentView(R.layout.dialog_game_undo);

        initialise();

        gameUndoText.setText(undoMessageText);
        gameUndoContinue.setOnClickListener(new View.OnClickListener() {
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
