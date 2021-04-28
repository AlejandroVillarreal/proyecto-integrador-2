package com.example.proyecto_integrador_2.ui.profile;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.proyecto_integrador_2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;
    private ProfileViewModel profileViewModel;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPhone;
    private EditText editTextBuisness;
    private ImageView imageProfile;
    private ImageView star1, star2, star3, star4, star5;
    //MODAL
    private View modalView;
    private TextView textCalif;
    private ImageView calif1, calif2, calif3, calif4, calif5;
    // USER VAR
    private String user_id;
    private ArrayList califications;
    private Boolean isGrading;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        isGrading = false;
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        user_id = ProfileFragmentArgs.fromBundle(getArguments()).getUserId();
        editTextName = (EditText) root.findViewById(R.id.editTextName);
        editTextEmail = (EditText) root.findViewById(R.id.editTextEmail);
        editTextPhone = (EditText) root.findViewById(R.id.editTextPhone);
        editTextBuisness = (EditText) root.findViewById(R.id.editTextBuisness);
        imageProfile = (ImageView) root.findViewById(R.id.imageProfile);
        star1 = (ImageView) root.findViewById(R.id.estrella1);
        star2 = (ImageView) root.findViewById(R.id.estrella2);
        star3 = (ImageView) root.findViewById(R.id.estrella3);
        star4 = (ImageView) root.findViewById(R.id.estrella4);
        star5 = (ImageView) root.findViewById(R.id.estrella5);
        // MODAL
        modalView = (View) root.findViewById(R.id.viewModal);
        textCalif = (TextView) root.findViewById(R.id.textViewCalif);
        calif1 = (ImageView) root.findViewById(R.id.estrella6);
        calif2 = (ImageView) root.findViewById(R.id.estrella7);
        calif3 = (ImageView) root.findViewById(R.id.estrella8);
        calif4 = (ImageView) root.findViewById(R.id.estrella9);
        calif5 = (ImageView) root.findViewById(R.id.estrella10);

        Button btnCalif = (Button) root.findViewById(R.id.btnCalif);
        btnCalif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCalifPopUp();
            }
        });


        this.updateUser();
        return root;
    }

    public void updateUser(){
        String uid = "";
        if(this.user_id.length() > 0){
            uid = this.user_id;
        }else{
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            uid = user.getUid();
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users");
        ref.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    Map<String,Object> map = new HashMap<>();
                    for (DataSnapshot messageSnapshot: task.getResult().getChildren()) {
                        String name = (String) messageSnapshot.getKey().toString();
                        Object message = messageSnapshot.getValue();
                        map.put(name, message);

                    }
                    editTextName.setText(map.get("name").toString());
                    editTextEmail.setText(map.get("email").toString());
                    editTextPhone.setText(map.get("phone").toString());
                    Glide.with(getContext()).load(map.get("profile_pic").toString()).into(imageProfile);
                    Object aux = map.get("calif");
                    if (aux instanceof ArrayList){
                        ArrayList<Long> array = (ArrayList<Long>) aux;
                        califications = array;
                        calculateCalif(null);
                    }
                }
            }
        });
    }

    private void toggleCalifPopUp(){
        this.isGrading = !this.isGrading;
        this.modalView.setVisibility((this.isGrading)?View.VISIBLE:View.INVISIBLE);
        this.textCalif.setVisibility((this.isGrading)?View.VISIBLE:View.INVISIBLE);
        this.calif1.setVisibility((this.isGrading)?View.VISIBLE:View.INVISIBLE);
        this.calif2.setVisibility((this.isGrading)?View.VISIBLE:View.INVISIBLE);
        this.calif3.setVisibility((this.isGrading)?View.VISIBLE:View.INVISIBLE);
        this.calif4.setVisibility((this.isGrading)?View.VISIBLE:View.INVISIBLE);
        this.calif5.setVisibility((this.isGrading)?View.VISIBLE:View.INVISIBLE);
    }

    private void starCalifAction(ImageView star){
        star.setClickable(true);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer calif = (v == calif1) ? 1 : (v == calif2) ? 2 : (v == calif3) ? 3 : (v == calif4) ? 4 : (v == calif5) ? 5 : 0;
                calculateCalif(calif);
                toggleCalifPopUp();
            }
        });
    }

    private void calculateCalif(Integer calif){
        if (this.califications == null){
            this.califications = new ArrayList<Long>();
        }
        ArrayList<Long> arrayCalif = this.califications;
        if (calif != null){
            arrayCalif.add(Long.valueOf(calif));
        }
        Double total = 0.0;
        for (int i = 0; i < arrayCalif.size(); i ++){
            Long value = arrayCalif.get(i);
            total = total + value;
        }
        Double prom = total / arrayCalif.size();
        double aux = Math.round(prom);
        //star1
        Drawable yellow = getResources().getDrawable(R.drawable.estrella_amarilla);
        Drawable gray = getResources().getDrawable(R.drawable.estrella_gris);
        star1.setImageDrawable((aux > 0) ? yellow : gray );
        star2.setImageDrawable((aux > 1) ? yellow : gray );
        star3.setImageDrawable((aux > 2) ? yellow : gray );
        star4.setImageDrawable((aux > 3) ? yellow : gray );
        star5.setImageDrawable((aux > 4) ? yellow : gray );
    }

}