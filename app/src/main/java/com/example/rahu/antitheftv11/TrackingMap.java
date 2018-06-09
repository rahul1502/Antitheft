package com.example.rahu.antitheftv11;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

public class TrackingMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    // retrieve from database
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private String uid =firebaseAuth.getCurrentUser().getUid();
    Firebase mRootRef;

    private double measureLat;
    private double measureLong;
    SharedPreferences mPref;
    SharedPreferences.Editor mEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // close PhoneTracking service if it still exists
        if(isMyServiceRunning(PhoneTracking.class) == true){
            Intent stopIntent = new Intent(TrackingMap.this ,PhoneTracking.class );
            stopService(stopIntent);
        }




        // retireve latitude and longitude
        Firebase.setAndroidContext(this);
        mRootRef = new Firebase("https://mantitheft.firebaseio.com/users/" + uid);

        Firebase latRef = mRootRef.child("latitude");
        latRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                measureLat = dataSnapshot.getValue(Double.class);
                Log.v("LAT" , String.valueOf(measureLat));
                mPref = getSharedPreferences("latitude" , Context.MODE_PRIVATE);
                mEdit = mPref.edit();
                mEdit.putString("latitude", String.valueOf(measureLat));
                mEdit.apply();
                
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        Firebase longRef = mRootRef.child("longitude");
        longRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                measureLong = dataSnapshot.getValue(Double.class);
                Log.v("LONG" , String.valueOf(measureLong));
                mPref = getSharedPreferences("longitude" , Context.MODE_PRIVATE);
                mEdit = mPref.edit();
                mEdit.putString("longitude", String.valueOf(measureLong));
                mEdit.apply();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    // check
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private Double inputLat;
    private Double inputLong;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mPref = getSharedPreferences("latitude" , Context.MODE_PRIVATE);
        inputLat = Double.valueOf(mPref.getString("latitude","0"));
        Log.v("SP Lat" , String.valueOf(inputLat));


        mPref = getSharedPreferences("longitude" , Context.MODE_PRIVATE);
        inputLong = Double.valueOf(mPref.getString("longitude","0"));
        Log.v("SP Long" , String.valueOf(inputLong));


        LatLng location = new LatLng(inputLat, inputLong);
        mMap.addMarker(new MarkerOptions().position(location).title("Your Phone"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));


    }

}
