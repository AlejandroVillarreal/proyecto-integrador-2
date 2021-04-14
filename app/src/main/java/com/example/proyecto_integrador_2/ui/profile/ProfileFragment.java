package com.example.proyecto_integrador_2.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyecto_integrador_2.R;
import com.example.proyecto_integrador_2.Register;
import com.example.proyecto_integrador_2.login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;
    private ProfileViewModel profileViewModel;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPhone;
    private EditText editTextBuisness;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        editTextName = (EditText) root.findViewById(R.id.editTextName);
        editTextEmail = (EditText) root.findViewById(R.id.editTextEmail);
        editTextPhone = (EditText) root.findViewById(R.id.editTextPhone);
        editTextBuisness = (EditText) root.findViewById(R.id.editTextBuisness);
        this.updateUser();
        return root;
    }

    public void updateUser(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail();
        String uid = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users");
        ref.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    Map<String,String> map = new HashMap<>();
                    for (DataSnapshot messageSnapshot: task.getResult().getChildren()) {
                        String name = (String) messageSnapshot.getKey().toString();
                        String message = (String) messageSnapshot.getValue().toString();
                        map.put(name, message);

                    }
                    editTextName.setText(map.get("name"));
                    editTextEmail.setText(map.get("email"));
                    editTextPhone.setText(map.get("phone"));
                    //editTextBuisness.setText(map.get(""));
                }
            }
        });
    }
}