package com.example.gameside2048champs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.gameside2048champs.dialogs.ArrivingFeatureDialog;

public class MainActivity extends AppCompatActivity implements PreGameFragment.OnPreGameFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatButton startPreGameFragmentButton = findViewById(R.id.start_game_pregame_fragment_button);
        startPreGameFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreGameFragment fragment = new PreGameFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right,
                        R.anim.enter_from_right, R.anim.exit_to_right);
                transaction.addToBackStack(null);
                transaction.replace(R.id.full_screen_fragment_container, fragment, "PREGAME_FRAGMENT")
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