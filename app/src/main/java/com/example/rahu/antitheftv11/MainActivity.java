package com.example.rahu.antitheftv11;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailField = (EditText)findViewById(R.id.emailField);
        passwordField = (EditText)findViewById(R.id.passwordField);
        firebaseAuth = FirebaseAuth.getInstance();


    }
    public void btnLogin(View view){
        final ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this , "Hang in there..." , "Logging you in..." , true);

        (firebaseAuth.signInWithEmailAndPassword(emailField.getText().toString() , passwordField.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressDialog.dismiss();
                        if(task.isSuccessful()){

                            Toast.makeText(MainActivity.this , "Login Sucessful" , Toast.LENGTH_LONG).show();
                            Intent in = new Intent(MainActivity.this , HomeScreen.class);
                            in.putExtra("Email",firebaseAuth.getCurrentUser().getEmail());
                            startActivity(in);
                        }
                        else{
                            Toast.makeText(MainActivity.this , task.getException().getMessage() , Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }

    public void btnSignUp(View view){
        Intent intent = new Intent(this , signUpActivity.class);
        startActivity(intent);
    }




}
