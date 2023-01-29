package com.example.gameside2048champs;

import android.app.Application;

import com.google.android.gms.games.PlayGamesSdk;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PlayGamesSdk.initialize(this);
    }
}