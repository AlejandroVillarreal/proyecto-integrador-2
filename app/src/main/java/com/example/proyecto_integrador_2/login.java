package com.example.proyecto_integrador_2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText editTextEmail;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        // Write a message to the database
        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");*/
        // Initialize Firebase Auth

    }

    private boolean email_validation(){
        String emailInput = editTextEmail.getText().toString().trim();
        if (emailInput.isEmpty()){
            editTextEmail.setError("Field can't be empty");
            return false;
        } else {
            editTextEmail.setError(null);
            return true;
        }
    }

    private boolean password_validation(){
        String emailInput = editTextPassword.getText().toString().trim();
        if (emailInput.isEmpty()){
            editTextPassword.setError("Field can't be empty");
            return false;
        } else {
            editTextPassword.setError(null);
            return true;
        }
    }
    public void login(View v) {
        if (!email_validation() |   !password_validation() ){
            return;
        }
        mAuth = FirebaseAuth.getInstance();
        Log.d("mAuth",mAuth.toString());
        Log.d("email",editTextEmail.getText().toString());
        Log.d("password",editTextPassword.getText().toString());
        mAuth.signInWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Login", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            showHome(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            // ...
                        }

                        // ...
                    }
                });
    }

    public void register(View v){
        Intent intent = new Intent(login.this,Register.class);
        startActivity(intent);
    }

    private void showHome(FirebaseUser user) {
        Intent intent = new Intent(login.this, MainActivity.class);
        intent.putExtra("uid", user.getUid());
        startActivity(intent);
    }


}