package com.example.rahu.antitheftv11;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileOutputStream;

public class HomeScreen extends AppCompatActivity {

    private TextView userEmail;


    // find Phone
    private boolean flag = false;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private String uid =firebaseAuth.getCurrentUser().getUid();

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference userRef = rootRef.child("users");
    DatabaseReference subUserRef = userRef.child(uid);
    DatabaseReference flagRef = subUserRef.child("flag");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        userEmail = (TextView)findViewById(R.id.userEmail);
        userEmail.setText(getIntent().getExtras().getString("Email"));
        // start phone tracking service
        Intent intentTrack = new Intent(this , PhoneTracking.class);
        startService(intentTrack);


        // start tune playing service
        Intent findPhone = new Intent(this ,TunePlayer.class);
        startService(findPhone);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // stop phone tracking service
        Intent intentStop = new Intent(this , PhoneTracking.class);
        stopService(intentStop);

        // stop tune palying service
        Intent stopTune = new Intent(this , TunePlayer.class);
        stopService(stopTune);


        // set the find phone flag false
        flag = false;
        rootRef.child("users").child(uid).push();
        flagRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rootRef.child("users").child(uid).child("flag").setValue(flag);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }

    public void goToMap(View view){
        Intent intentM = new Intent(this,TrackingMap.class);
        startActivity(intentM);
    }

    public void btnSignOut(View view){
        firebaseAuth.signOut();
        //closing activity
        finish();
        //starting login activity
        // close the phonetracking service
        Intent stopIntent = new Intent(this ,PhoneTracking.class);
        stopService(stopIntent);

        startActivity(new Intent(this, MainActivity.class));
    }

    public void findPhone(View view){

        // stop tune playing service
        Intent stopTune = new Intent(HomeScreen.this , TunePlayer.class);
        stopService(stopTune);

        flag = true;
        rootRef.child("users").child(uid).push();
        flagRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rootRef.child("users").child(uid).child("flag").setValue(flag);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }    private boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
}
