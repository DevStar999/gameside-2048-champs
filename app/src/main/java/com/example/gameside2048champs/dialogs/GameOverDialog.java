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
import com.example.gameside2048champs.enums.GameOverDialogOptions;

public class GameOverDialog extends Dialog {
    private boolean didUserGiveResponse;
    private GameOverDialogOptions optionSelected;
    private LottieAnimationView gameOverLottie;
    private AppCompatTextView gameOverText;
    private LinearLayout gameOverAllButtonsLinearLayout;
    private AppCompatButton gameOverMainMenu;
    private AppCompatButton gameOverPlayAgain;
    private AppCompatButton gameOverUndoLastMove;
    private GameOverDialogListener gameOverDialogListener;

    private void initialise() {
        didUserGiveResponse = false;
        optionSelected = GameOverDialogOptions.UNDO_LAST_MOVE;
        gameOverLottie = findViewById(R.id.game_over_lottie);
        gameOverText = findViewById(R.id.game_over_text);
        gameOverAllButtonsLinearLayout = findViewById(R.id.game_over_all_buttons_linear_layout);
        gameOverMainMenu = findViewById(R.id.game_over_main_menu);
        gameOverPlayAgain = findViewById(R.id.game_over_play_again);
        gameOverUndoLastMove = findViewById(R.id.game_over_undo_last_move);
    }

    private void setVisibilityOfViews(int visibility) {
        gameOverLottie.setVisibility(visibility);
        gameOverText.setVisibility(visibility);
        gameOverAllButtonsLinearLayout.setVisibility(visibility);
    }

    public GameOverDialog(@NonNull Context context) {
        super(context, R.style.CustomDialogTheme);
        setContentView(R.layout.dialog_game_over);

        initialise();

        gameOverMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // First, the views will disappear, then the dialog box will close
                setVisibilityOfViews(View.INVISIBLE);
                new CountDownTimer(100, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {}
                    @Override
                    public void onFinish() {
                        optionSelected = GameOverDialogOptions.MAIN_MENU;
                        didUserGiveResponse = true;
                        dismiss();
                    }
                }.start();
            }
        });
        gameOverPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // First, the views will disappear, then the dialog box will close
                setVisibilityOfViews(View.INVISIBLE);
                new CountDownTimer(100, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {}
                    @Override
                    public void onFinish() {
                        optionSelected = GameOverDialogOptions.PLAY_AGAIN;
                        didUserGiveResponse = true;
                        dismiss();
                    }
                }.start();
            }
        });
        gameOverUndoLastMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // First, the views will disappear, then the dialog box will close
                setVisibilityOfViews(View.INVISIBLE);
                new CountDownTimer(100, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {}
                    @Override
                    public void onFinish() {
                        didUserGiveResponse = true;
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

    @Override
    public void dismiss() {
        super.dismiss();
        gameOverDialogListener.getResponseOfOverDialog(optionSelected, didUserGiveResponse);
    }

    public void setGameOverDialogListener(GameOverDialogListener gameOverDialogListener) {
        this.gameOverDialogListener = gameOverDialogListener;
    }

    public interface GameOverDialogListener {
        void getResponseOfOverDialog(GameOverDialogOptions optionSelected, boolean didUserRespond);
    }
}
