package com.example.rahu.antitheftv11;

import android.app.Application;

import com.firebase.client.Firebase;

public class RetrieveData extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
