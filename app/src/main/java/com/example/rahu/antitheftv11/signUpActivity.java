package com.example.rahu.antitheftv11;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signUpActivity extends AppCompatActivity {


    private EditText emailField;
    private EditText passwordField;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // sign up
        emailField = (EditText)findViewById(R.id.emailField);
        passwordField = (EditText)findViewById(R.id.passwordField);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void signUpUser(View view){


        final ProgressDialog progressDialog = ProgressDialog.show(signUpActivity.this, "Hang in there...", "We are Signing you up...", true);
            (firebaseAuth.createUserWithEmailAndPassword(emailField.getText().toString(), passwordField.getText().toString())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();

                    if (task.isSuccessful()) {
                        Toast.makeText(signUpActivity.this, "Sign Up sucessful...", Toast.LENGTH_LONG).show();
                        Intent in = new Intent(signUpActivity.this, HomeScreen.class);
                        in.putExtra("Email",firebaseAuth.getCurrentUser().getEmail());
                        startActivity(in);
                    } else {
                        Toast.makeText(signUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
            });


    }
}
