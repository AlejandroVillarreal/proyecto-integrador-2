package com.example.proyecto_integrador_2.ui.profile;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kotlin.jvm.internal.CollectionToArray;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileDetailFragment";
    private FirebaseAuth mAuth;
    private ProfileViewModel profileViewModel;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPhone;
    private EditText editTextBuisness;
    private TextView lblCalif;
    private ImageView imageProfile;
    private ImageView star1, star2, star3, star4, star5;
    //MODAL
    private View modalView;
    private TextView textCalif;
    private ImageView estrella6, estrella7, estrella8, estrella9, estrella10;
    // USER VAR
    private String user_id;
    private ArrayList califications;
    private Boolean isGrading;
    private Map<String,Object> userData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        isGrading = false;
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        if (getArguments().get("user_id") != null){
            user_id = ProfileFragmentArgs.fromBundle(getArguments()).getUserId().toString();
        }
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
        lblCalif = (TextView) root.findViewById(R.id.lblCalif);
        // MODAL
        modalView = (View) root.findViewById(R.id.viewModal);
        textCalif = (TextView) root.findViewById(R.id.textViewCalif);
        estrella6 = (ImageView) root.findViewById(R.id.estrella6);
        estrella7 = (ImageView) root.findViewById(R.id.estrella7);
        estrella8 = (ImageView) root.findViewById(R.id.estrella8);
        estrella9 = (ImageView) root.findViewById(R.id.estrella9);
        estrella10 = (ImageView) root.findViewById(R.id.estrella10);
        estrella6.setClickable(true);
        estrella7.setClickable(true);
        estrella8.setClickable(true);
        estrella9.setClickable(true);
        estrella10.setClickable(true);
        ImageView whats = root.findViewById(R.id.imageViewWhatsApp);
        whats.setClickable(true);

        Button btnCalif = (Button) root.findViewById(R.id.btnCalif);
        btnCalif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCalifPopUp();
            }
        });
        estrella6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starCalifAction(1);
            }
        });
        estrella7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starCalifAction(2);
            }
        });
        estrella8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starCalifAction(3);
            }
        });
        estrella9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starCalifAction(4);
            }
        });
        estrella10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starCalifAction(5);
            }
        });

        whats.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 wppLogoClicked();
             }
         });

        if (this.user_id == null){
            TextView req = root.findViewById(R.id.textViewRequestService);

            req.setVisibility(View.INVISIBLE);
            whats.setVisibility(View.INVISIBLE);
            btnCalif.setVisibility(View.INVISIBLE);
        }

        this.updateUser();
        return root;
    }

    public void updateUser(){
        String uid = "";
        if(this.user_id != null && this.user_id.length() > 0){
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
                    userData = map;
                    editTextName.setText(map.get("name").toString());
                    editTextEmail.setText(map.get("email").toString());
                    editTextPhone.setText(map.get("phone").toString());
                    Glide.with(getContext()).load(map.get("profile_pic").toString()).into(imageProfile);
                    Object aux = map.get("calif");
                    if (aux instanceof ArrayList){
                        ArrayList<Long> array = (ArrayList<Long>) aux;
                        califications = array;
                        calculateCalif(null);
                    }else{
                        califications = new ArrayList<Integer>();
                    }
                }
            }
        });
    }

    private void toggleCalifPopUp(){
        this.isGrading = !this.isGrading;
        this.modalView.setVisibility((this.isGrading)?View.VISIBLE:View.INVISIBLE);
        this.textCalif.setVisibility((this.isGrading)?View.VISIBLE:View.INVISIBLE);
        this.estrella6.setVisibility((this.isGrading)?View.VISIBLE:View.INVISIBLE);
        this.estrella7.setVisibility((this.isGrading)?View.VISIBLE:View.INVISIBLE);
        this.estrella8.setVisibility((this.isGrading)?View.VISIBLE:View.INVISIBLE);
        this.estrella9.setVisibility((this.isGrading)?View.VISIBLE:View.INVISIBLE);
        this.estrella10.setVisibility((this.isGrading)?View.VISIBLE:View.INVISIBLE);
    }

    private void starCalifAction(Integer calif){
        Drawable yellow = getResources().getDrawable(R.drawable.estrella_amarilla);
        Drawable gray = getResources().getDrawable(R.drawable.estrella_gris);
        this.estrella6.setImageDrawable((calif > 0) ? yellow : gray );
        this.estrella7.setImageDrawable((calif > 1) ? yellow : gray );
        this.estrella8.setImageDrawable((calif > 2) ? yellow : gray );
        this.estrella9.setImageDrawable((calif > 3) ? yellow : gray );
        this.estrella10.setImageDrawable((calif > 4) ? yellow : gray );
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                calculateCalif(calif);
                toggleCalifPopUp();
            }
        }, 500);

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
        DecimalFormat df2 = new DecimalFormat("#.##");
        this.lblCalif.setText(df2.format(prom));
        double aux = Math.round(prom);
        Drawable yellow = getResources().getDrawable(R.drawable.estrella_amarilla);
        Drawable gray = getResources().getDrawable(R.drawable.estrella_gris);
        this.star1.setImageDrawable((aux > 0) ? yellow : gray );
        this.star2.setImageDrawable((aux > 1) ? yellow : gray );
        this.star3.setImageDrawable((aux > 2) ? yellow : gray );
        this.star4.setImageDrawable((aux > 3) ? yellow : gray );
        this.star5.setImageDrawable((aux > 4) ? yellow : gray );
        if (calif != null){
            this.uploadPromUser(prom);
        }
    }

    private void uploadPromUser(Double prom){
        if (this.user_id != null && this.userData != null && this.califications != null){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("Users");
            this.userData.put("prom", prom);
            this.userData.put("calif", this.califications);
            ref.child(this.user_id).setValue(this.userData);
        }
    }

    void wppLogoClicked() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users");
        ref.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    Map<String,Object> map = new HashMap<>();
                    for (DataSnapshot messageSnapshot: task.getResult().getChildren()) {
                        String name = (String) messageSnapshot.getKey().toString();
                        Object message = messageSnapshot.getValue();
                        map.put(name, message);
                    }
                    String name = map.get("name").toString();
                    String phone = map.get("phone").toString();

                    if (name != null && name.length() > 0 && phone != null && phone.length() > 0){
                        PackageManager packageManager = getContext().getPackageManager();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        String message = "Hola soy " + name + ", vi su anuncio en Service Finder, deseo cotizar uno de sus servicios.";
                        try {
                            String url = "https://api.whatsapp.com/send?phone=" + "52" + phone + "&text=" + URLEncoder.encode(message, "UTF-8");
                            i.setPackage("com.whatsapp");
                            i.setData(Uri.parse(url));
                            if (i.resolveActivity(packageManager) != null) {
                                getContext().startActivity(i);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

}