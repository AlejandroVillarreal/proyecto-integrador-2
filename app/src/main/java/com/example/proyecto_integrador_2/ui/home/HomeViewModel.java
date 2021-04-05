package com.example.proyecto_integrador_2.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    public HomeViewModel() {

    }


}