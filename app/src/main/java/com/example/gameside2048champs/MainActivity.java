package com.example.gameside2048champs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.gameside2048champs.dialogs.ArrivingFeatureDialog;
import com.example.gameside2048champs.fragments.PreGameFragment;

/* TODO -> !! Bug !! -> If we do undo exactly after the move in which we completed the goal, then goal completion is done.
           But now if the user makes any move even a move which does not lead to goal completion the code block for goal
           completion is being played. This happens also when Change Value tool reverses the goal completion status
*/
/* TODO -> Set the scaleX and scaleY properties for the block drawables dynamically through code so as to have a good fit
           of the block drawables in the cell space
*/
/* TODO -> Dialog text should have same case i.e. it should be uppercase or lowercase etc. uniform in all the dialogs
           throughout the whole project
*/
/* TODO -> Implement the Game Over Dialog is such a way that it accommodates the use of the newly implemented tools to
           revive the game, if game state is game over
*/
public class MainActivity extends AppCompatActivity implements PreGameFragment.OnPreGameFragmentInteractionListener {

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_GameSide2048Champs);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        AppCompatButton startPreGameFragmentButton = findViewById(R.id.start_pregame_fragment_button);
        startPreGameFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If PreGameFragment was opened and is currently on top, then return
                int countOfFragments = getSupportFragmentManager().getFragments().size();
                if (countOfFragments > 0) {
                    Fragment topMostFragment = getSupportFragmentManager().getFragments().get(countOfFragments-1);
                    if (topMostFragment != null && topMostFragment.getTag() != null && !topMostFragment.getTag().isEmpty()
                            && topMostFragment.getTag().equals("PREGAME_FRAGMENT")) {
                        return;
                    }
                }

                PreGameFragment fragment = new PreGameFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right,
                        R.anim.enter_from_right, R.anim.exit_to_right);
                transaction.addToBackStack(null);
                transaction.add(R.id.full_screen_fragment_container, fragment, "PREGAME_FRAGMENT")
                        .commit();
            }
        });
    }

    @Override
    public void onPreGameFragmentInteractionBackClicked() {
        onBackPressed();
    }

    @Override
    public void onPreGameFragmentInteractionStartGame(String gameMode, int gameMatrixColumns, int gameMatrixRows) {
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intent.putExtra("gameMode", gameMode);
        intent.putExtra("gameMatrixColumns", gameMatrixColumns);
        intent.putExtra("gameMatrixRows", gameMatrixRows);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPreGameFragmentInteractionShowArrivingFeatureDialog() {
        new ArrivingFeatureDialog(this).show();
    }
}