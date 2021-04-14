package com.example.proyecto_integrador_2.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.proyecto_integrador_2.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ProfileDetailFragment extends Fragment {
    private DatabaseReference mDatabase;
// ...

    public ProfileDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.updateUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_detail, container, false);
    }

    public void updateUser(){
        //FirebaseAuth fa =
        /*val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        if (uid.length > 0){
            val database = FirebaseDatabase.getInstance()
            val ref = database.getReference("Users")
            ref.child(uid).get().addOnSuccessListener {
                val map = it.getValue() as? HashMap<String, Any>
                if (map != null){
                    this.updateData(map)
                }
            }.addOnFailureListener {

            }
        }*/
    }
}