package com.example.rahu.antitheftv11;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TunePlayer extends Service {

    // find Phone
    private boolean flag = false;

    // retrieve from database
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private String uid =firebaseAuth.getCurrentUser().getUid();
    Firebase mRootRef;


    public TunePlayer() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final MediaPlayer alarmTone = MediaPlayer.create(this , R.raw.alarm);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                // play the tune
                mRootRef = new Firebase("https://mantitheft.firebaseio.com/users/" + uid);
                Firebase flagRef = mRootRef.child("flag");
                flagRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        flag = dataSnapshot.getValue(Boolean.class);
                        Log.v("flag" , String.valueOf(flag));

                        if(flag == true){
                            //play the tune
                            alarmTone.start();
                            alarmTone.setLooping(false);
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });


            }
        };

        Thread pTune = new Thread(r);
        pTune.start();


        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
