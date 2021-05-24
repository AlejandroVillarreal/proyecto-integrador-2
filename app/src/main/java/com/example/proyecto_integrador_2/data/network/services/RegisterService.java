package com.example.proyecto_integrador_2.data.network.services;

import androidx.annotation.NonNull;

import com.example.proyecto_integrador_2.data.database.entities.UserEntity;
import com.example.proyecto_integrador_2.data.mappers.UserMapper;
import com.example.proyecto_integrador_2.data.network.FirebaseTables;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import javax.inject.Inject;

public class RegisterService {

    private static final String TAG = "RegisterService";
    private FirebaseAuth mAuth;
    private DatabaseReference database;
    private UserMapper userMapper;

    @Inject
    public RegisterService(UserMapper userMapper) {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        this.userMapper = userMapper;
    }

    public void createUser(UserEntity userEntity, String password, RegisterResult registerResult) {
        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(userEntity.email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    userEntity.user_id = mAuth.getCurrentUser().getUid();
                    HashMap<Object, String> hashMap = new HashMap<>();
                    hashMap.put("email", userEntity.email);
                    hashMap.put("user_id", userEntity.user_id);
                    hashMap.put("name", userEntity.name);
                    hashMap.put("phone", userEntity.phone);
                    hashMap.put("profile_pic", " ");
                    hashMap.put("services", " ");
                    hashMap.put("area_of_service", " ");
                    database.child(FirebaseTables.USERS.name).child(userEntity.user_id).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                registerResult.onRegisterSuccessful(userEntity);
                            } else {
                                // Error guardando el user en Firebase
                                registerResult.onRegisterFailure(task.getException());
                            }
                        }
                    });
                } else {
                    // Error creando el user de Firebase
                    registerResult.onRegisterFailure(task.getException());
                }
            }
        });
    }
}
