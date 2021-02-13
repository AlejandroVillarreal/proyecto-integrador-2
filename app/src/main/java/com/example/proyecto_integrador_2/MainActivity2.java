package com.example.proyecto_integrador_2;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity2 extends AppCompatActivity {
    private static final String TAG = "MyActivity2";
    private TextView textViewEmail;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        userID = getIntent().getStringExtra("uid");
        textViewEmail = findViewById(R.id.textViewEmail);
        showEmail();
    }

    public void showEmail() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        DatabaseReference getEmailFromDatabase = database.getReference("Users").child(userID).child("email");
        getEmailFromDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textViewEmail.setText(snapshot.getValue(String.class));
                Log.d(TAG, "Mostrando texto de usuario");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                textViewEmail.setText("User email");
            }
        });
    }

    public void showEmail2() {

    }
}