package com.example.proyecto_integrador_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextName;
    private EditText editTextPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
    }


    /*-----------------------Validaciones de campos------------------*/

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
            editTextPassword.setError("El campo no puede estar vacio");
            return false;
        } else {
            editTextPassword.setError(null);
            return true;
        }
    }
    private boolean name_validation(){
        String nameInput = editTextName.getText().toString();
        if (nameInput.isEmpty()){
            editTextName.setError("El campo no puede estar vacio");
            return false;
        }
        else if (nameInput.length() > 50){
            editTextName.setError("Nombre demasiado largo");
            return false;
        } else {
            editTextName.setError(null);
            return true;
        }
    }
    private boolean phone_validation(){
        String phoneInput = editTextPhone.getText().toString();
        if(phoneInput.isEmpty()){
            editTextPhone.setError("El campo no puede estar vacio");
            return false;
        }
        else if(phoneInput.length() != 10){
            editTextPhone.setError("Ingrese un numero valido");
            return false;
        } else {
            editTextPhone.setError(null);
            return true;
        }
    }

    public void register(View v){
        if (!email_validation() |   !password_validation() | !name_validation() |   !phone_validation() ){
            return;
        }
        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(editTextEmail.getText().toString(),editTextPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user =mAuth.getCurrentUser();
                    String email = user.getEmail();
                    String user_id =user.getUid();
                    String name = editTextName.getText().toString();
                    String phone = editTextPhone.getText().toString();
                    HashMap<Object,String> hashMap = new HashMap<>();
                    hashMap.put("email",email);
                    hashMap.put("user_id",user_id);
                    hashMap.put("name",name);
                    hashMap.put("phone",phone);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = database.getReference("Users");
                    databaseReference.child(user_id).setValue(hashMap);
                }
                Intent intent = new Intent(Register.this,login.class);
                startActivity(intent);
            }
        });
    }
}