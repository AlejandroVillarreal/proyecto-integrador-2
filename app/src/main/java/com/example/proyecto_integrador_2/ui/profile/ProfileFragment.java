package com.example.proyecto_integrador_2.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        return root;
    }

    public void UpdateUser(){
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
                    for (DataSnapshot messageSnapshot: task.getResult().getChildren()) {
                        String name = (String) messageSnapshot.child("name").getValue();
                        String message = (String) messageSnapshot.child("message").getValue();
                    }
                    //Map<String, Object> map = task.getResult().getValue().getClass(HashMap<String, Object>);
                }else{

                }
            }
        });
//        if (task.isSuccessful()){
//            FirebaseUser user =mAuth.getCurrentUser();
//            String email = user.getEmail();
//            String user_id =user.getUid();
//            String name = editTextName.getText().toString();
//            String phone = editTextPhone.getText().toString();
//            HashMap<Object,String> hashMap = new HashMap<>();
//            hashMap.put("email",email);
//            hashMap.put("user_id",user_id);
//            hashMap.put("name",name);
//            hashMap.put("phone",phone);
//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            DatabaseReference databaseReference = database.getReference("Users");
//            databaseReference.child(user_id).setValue(hashMap);
//        }
//        Intent intent = new Intent(Register.this, login.class);
//        startActivity(intent);
    }
}