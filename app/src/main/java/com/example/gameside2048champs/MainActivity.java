package com.example.gameside2048champs;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.gameside2048champs.dialogs.ArrivingFeatureDialog;
import com.example.gameside2048champs.fragments.PreGameFragment;
import com.google.android.gms.games.AuthenticationResult;
import com.google.android.gms.games.GamesSignInClient;
import com.google.android.gms.games.PlayGames;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

// TODO -> Add option in ShopFragment to earn coins for free by watching Ad when ad has loaded
public class MainActivity extends AppCompatActivity implements PreGameFragment.OnPreGameFragmentInteractionListener {
    private GamesSignInClient gamesSignInClient;
    private boolean isUserSignedIn;
    private AppCompatTextView gpgsSignInStatusTextView;
    private AppCompatImageView gpgsSignInImageView;
    private AppCompatButton startPreGameFragmentButton;

    private void initialise() {
        gamesSignInClient = PlayGames.getGamesSignInClient(MainActivity.this);
        isUserSignedIn = true;
        gpgsSignInStatusTextView = findViewById(R.id.gpgs_sign_in_status_text_view);
        gpgsSignInImageView = findViewById(R.id.gpgs_sign_in_image_view);
        startPreGameFragmentButton = findViewById(R.id.start_pregame_fragment_button);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_GameSide2048Champs);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        initialise();

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
                transaction.add(R.id.main_activity_full_screen_fragment_container, fragment, "PREGAME_FRAGMENT")
                        .commit();
            }
        });

        verifyPlayGamesSignIn(false);
    }

    private boolean isInternetConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) { return false; }
        /* NetworkInfo is deprecated in API 29 so we have to check separately for higher API Levels */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Network network = cm.getActiveNetwork();
            if (network == null) { return false; }
            NetworkCapabilities networkCapabilities = cm.getNetworkCapabilities(network);
            if (networkCapabilities == null) { return false; }
            boolean isInternetSuspended = !networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_SUSPENDED);
            return (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                    && !isInternetSuspended);
        } else {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnected());
        }
    }

    private void verifyPlayGamesSignIn(boolean isSignInAttemptManual) {
        gamesSignInClient.isAuthenticated().addOnCompleteListener(new OnCompleteListener<AuthenticationResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthenticationResult> isAuthenticatedTask) {
                boolean isAuthenticated = (isAuthenticatedTask.isSuccessful()
                        && isAuthenticatedTask.getResult().isAuthenticated());
                if (isAuthenticated) {
                    isUserSignedIn = true;
                    gpgsSignInStatusTextView.setText("Google Play Games Sign In Status : Signed In ✅");
                    gpgsSignInImageView.setVisibility(View.GONE);
                    /* TODO -> Un-comment the following code when we want to make use of playerId of the GPGS signed in user
                    PlayGames.getPlayersClient(MainActivity.this).getCurrentPlayer()
                            .addOnCompleteListener(new OnCompleteListener<Player>() {
                        @Override
                        public void onComplete(@NonNull Task<Player> task) {
                            String playerId = task.getResult().getPlayerId();
                        }
                    });
                    */
                } else {
                    isUserSignedIn = false;
                    gpgsSignInStatusTextView.setText("Google Play Games Sign In Status : NOT Signed In");
                    gpgsSignInImageView.setVisibility(View.VISIBLE);
                    if (isSignInAttemptManual) {
                        if (isInternetConnected()) {
                            // TODO -> Show a dialog for GPGS Sign In Troubleshooting
                        } else { // Internet is NOT connected
                            Toast.makeText(MainActivity.this, "Network connection failed. Please check " +
                                    "Internet connectivity", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }

    public void gpgsSignInClicked(View view) {
        gamesSignInClient.signIn();
        new CountDownTimer(1000, 10000) {
            @Override
            public void onTick(long l) {}
            @Override
            public void onFinish() { verifyPlayGamesSignIn(true); }
        }.start();
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