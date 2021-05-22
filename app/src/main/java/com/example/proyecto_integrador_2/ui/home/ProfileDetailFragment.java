package com.example.proyecto_integrador_2.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.proyecto_integrador_2.R;
import com.example.proyecto_integrador_2.data.database.entities.UserEntity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class ProfileDetailFragment extends Fragment implements ProfileInterface {
    private static final String TAG = "ProfileDetailFragment";
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String user_id;
    private Context context;
    private ImageView profile_pic;
    private ImageView wpp_logo;
    private TextView profile_name;
    private FirebaseUser user;
    private ProfileInterface profileInterface;
    private String phone;
    private String currentUserName;
    private ChipGroup chipAreas;
// ...

    public ProfileDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user_id = "";//ProfileDetailFragmentArgs.fromBundle(getArguments()).getUserId();
        this.updateUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile_detail, container, false);
        context = getContext();
        profile_pic = root.findViewById(R.id.imgProfile);
        wpp_logo = root.findViewById(R.id.wpp_logo);
        profile_name = root.findViewById(R.id.nombre_tecnico);
        wpp_logo.setOnClickListener(this::wppLogoClicked);
        getCurrentUser();
        return root;
    }

    public void updateUser() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users");
        ref.child(user_id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, String> map = new HashMap<>();
                    for (DataSnapshot messageSnapshot : task.getResult().getChildren()) {
                        String name = (String) messageSnapshot.getKey().toString();
                        String message = (String) messageSnapshot.getValue().toString();
                        map.put(name, message);

                    }
                    profile_name.setText(map.get("name"));
//                    editTextEmail.setText(map.get("email"));
//                    editTextPhone.setText(map.get("phone"));
                    phone = map.get("phone");
                    Glide.with(context).load(map.get("profile_pic")).into(profile_pic);
                    //editTextBuisness.setText(map.get(""));
                }
            }
        });
    }

    void wppLogoClicked(View v) {
        Log.d(TAG, "WhatsappClicked: working");
        PackageManager packageManager = context.getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);
        String message = "Hola soy " + currentUserName + ", vi su anuncio en Service Finder, deseo cotizar uno de sus servicios.";
        try {
            String url = "https://api.whatsapp.com/send?phone=" + "52" + phone + "&text=" + URLEncoder.encode(message, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                context.startActivity(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void getCurrentUser() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users");
        ref.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, String> map = new HashMap<>();
                    for (DataSnapshot messageSnapshot : task.getResult().getChildren()) {
                        String name = (String) messageSnapshot.getKey().toString();
                        String message = (String) messageSnapshot.getValue().toString();
                        map.put(name, message);

                    }
                    //editTextName.setText(map.get("name"));
                    currentUserName = map.get("name");
                }
            }
        });
    }

    @Override
    public void profileClicked(UserEntity userEntity) {

    }

    @Override
    public void sendMessage(UserEntity userEntity) {

    }
}