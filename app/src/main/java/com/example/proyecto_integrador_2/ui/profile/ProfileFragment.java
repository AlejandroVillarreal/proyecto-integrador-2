package com.example.proyecto_integrador_2.ui.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.proyecto_integrador_2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileDetailFragment";
    private FirebaseAuth mAuth;
    private ProfileViewModel profileViewModel;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPhone;
    private EditText editTextArea_of_service;
    private EditText editTextServices;
    private TextView lblCalif;
    private ImageView imageProfile;
    private ImageView imageEditIcon;
    private ImageView star1, star2, star3, star4, star5;
    private Button btnUpdate;
    private ChipGroup chipAreas;
    //MODAL
    private View modalView;
    private TextView textCalif;
    private ImageView estrella6, estrella7, estrella8, estrella9, estrella10;
    // USER VAR
    private String user_id;
    private ArrayList califications;
    private Boolean isGrading;
    private Map<String, Object> userData;
    // IMAGE UPLOAD
    private Uri imageUri;
    final String randomKey = UUID.randomUUID().toString();
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String imageDownloadURL;
    private DatabaseReference databaseReference;
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        isGrading = false;
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        if (getArguments().get("user_id") != null) {
            user_id = ProfileFragmentArgs.fromBundle(getArguments()).getUserId().toString();
        }
        editTextName = (EditText) root.findViewById(R.id.editTextName);
        editTextEmail = (EditText) root.findViewById(R.id.editTextEmail);
        editTextPhone = (EditText) root.findViewById(R.id.editTextPhone);
        editTextArea_of_service = (EditText) root.findViewById(R.id.editTextBuisness);
        editTextServices = (EditText) root.findViewById(R.id.editTextService);
        imageProfile = (ImageView) root.findViewById(R.id.imageProfile);
        imageEditIcon = (ImageView) root.findViewById(R.id.edit_image);
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
        imageEditIcon.setVisibility(View.INVISIBLE);
        Button btnCalif = (Button) root.findViewById(R.id.btnCalif);
        btnUpdate = (Button) root.findViewById(R.id.btnUpdate);
        btnUpdate.setVisibility(View.INVISIBLE);
        chipAreas = root.findViewById(R.id.chipGroup);
        chipAreas.setVisibility(View.INVISIBLE);

        editTextName.setFocusable(false);
        editTextServices.setFocusable(false);
        editTextArea_of_service.setFocusable(false);
        editTextEmail.setFocusable(false);
        editTextPhone.setFocusable(false);
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

        if (this.user_id == null) {
            TextView req = root.findViewById(R.id.textViewRequestService);

            req.setVisibility(View.INVISIBLE);
            whats.setVisibility(View.INVISIBLE);
            btnCalif.setVisibility(View.INVISIBLE);
            imageProfile.setOnClickListener(this::uploadImage);
            imageEditIcon.setVisibility(View.VISIBLE);
            btnUpdate.setVisibility(View.VISIBLE);
            editTextName.setFocusableInTouchMode(true);
            editTextEmail.setFocusableInTouchMode(true);
            editTextPhone.setFocusableInTouchMode(true);
            editTextArea_of_service.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlertAreas();
                }
            });
            editTextServices.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlertServicios();
                }
            });
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateProfile();
                }
            });
        }

        this.updateUser();
        // IMAGE UPLOAD

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
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
                    Map<String, Object> map = new HashMap<>();
                    for (DataSnapshot messageSnapshot : task.getResult().getChildren()) {
                        String name = (String) messageSnapshot.getKey().toString();
                        Object message = messageSnapshot.getValue();
                        map.put(name, message);
                    }
                    userData = map;
                    editTextName.setText(map.get("name").toString());
                    editTextEmail.setText(map.get("email").toString());
                    editTextPhone.setText(map.get("phone").toString());
                    if (map.get("area_of_service") == null && map.get("services") == null) {
                        editTextArea_of_service.setText("");
                        editTextServices.setText("");
                    } else {
                        editTextArea_of_service.setText(map.get("area_of_service").toString());
                        editTextServices.setText(map.get("services").toString());
                    }

                    Glide.with(getContext()).load(map.get("profile_pic").toString()).into(imageProfile);
                    Object aux = map.get("calif");
                    if (aux instanceof ArrayList) {
                        ArrayList<Long> array = (ArrayList<Long>) aux;
                        califications = array;
                        calculateCalif(null);
                    } else {
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


    //IMAGE UPLOAD
    public void uploadImage(View v) {
        choosePicture();
    }

    public void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data.getData() != null) {
            imageUri = data.getData();
            Log.d("UploadImage", "test");
            uploadPicture();
        }
    }

    public void uploadPicture() {
        String uid = "";
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid();
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Cargando imagen...");
        progressDialog.show();
        //final String randomKey = UUID.randomUUID().toString();
        UploadTask uploadTask;
        //Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        StorageReference riversRef = storageReference.child("profile_pictures/" + randomKey);
        uploadTask = riversRef.putFile(imageUri);

// Register observers to listen for when the download is done or if it fails
        String finalUid = uid;
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                progressDialog.dismiss();
                Snackbar.make(getActivity().findViewById(android.R.id.content), "No se pudo cargar la imagen", Snackbar.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                progressDialog.dismiss();
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Imagen Cargada", Snackbar.LENGTH_LONG).show();
                storageReference.child("profile_pictures/"+randomKey).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageDownloadURL = uri.toString();
                        Log.d(TAG,imageDownloadURL);
                        Log.d(TAG, finalUid);
                        databaseReference.child("Users").child(finalUid).child("profile_pic").setValue(imageDownloadURL).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG,"SUCCESSFUL");
                                    //Toast.makeText(context, "Imagen Actualizada", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.d(TAG,"FAILED");
                                    //Toast.makeText(context, "Error al subir imagen", Toast.LENGTH_SHORT).show();
                                }
                                }
                            });
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.setMessage("Cargando: " + (int) progressPercent + "%");
            }
        });
    }

    public void updateProfile() {
        String uid = "";
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid();

        String nameInput, phoneInput, service_areaInput, serviceInput;
        nameInput = editTextName.getText().toString().trim();
        phoneInput = editTextPhone.getText().toString().trim();
        service_areaInput = editTextArea_of_service.getText().toString().trim();
        serviceInput = editTextServices.getText().toString().trim();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("area_of_service", service_areaInput);
        hashMap.put("service", serviceInput);
        hashMap.put("name", nameInput);
        hashMap.put("phone", phoneInput);
        databaseReference.child("Users").child(uid).child("name").setValue(nameInput);

        databaseReference.child("Users").child(uid).child("phone").setValue(phoneInput);

        databaseReference.child("Users").child(uid).child("area_of_service").setValue(service_areaInput);

        databaseReference.child("Users").child(uid).child("services").setValue(serviceInput).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Perfil Actualizado", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Error al Actualizar Perfil", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void showAlertAreas() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Selecciona tu Municipio");
        String[] items = {"Monterrey", "San Nicolás", "General Escobedo", "San Pedro", "Guadalupe", "Juárez", "Apodaca", "Santiago", "Santa Catarina", "García"};
        int checkedItem = 1;
        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Toast.makeText(getActivity(), "Monterrey", Toast.LENGTH_LONG).show();
                        editTextArea_of_service.setText(items[0]);
                        break;
                    case 1:
                        Toast.makeText(getActivity(), "San Nicolás", Toast.LENGTH_LONG).show();
                        editTextArea_of_service.setText(items[1]);
                        break;
                    case 2:
                        Toast.makeText(getActivity(), "General Escobedo", Toast.LENGTH_LONG).show();
                        editTextArea_of_service.setText(items[2]);
                        break;
                    case 3:
                        Toast.makeText(getActivity(), "San Pedro", Toast.LENGTH_LONG).show();
                        editTextArea_of_service.setText(items[3]);
                        break;
                    case 4:
                        Toast.makeText(getActivity(), "Guadalupe", Toast.LENGTH_LONG).show();
                        editTextArea_of_service.setText(items[4]);
                        break;
                    case 5:
                        Toast.makeText(getActivity(), "Juárez", Toast.LENGTH_LONG).show();
                        editTextArea_of_service.setText(items[5]);
                        break;
                    case 6:
                        Toast.makeText(getActivity(), "Apodaca", Toast.LENGTH_LONG).show();
                        editTextArea_of_service.setText(items[6]);
                        break;
                    case 7:
                        Toast.makeText(getActivity(), "Santiago", Toast.LENGTH_LONG).show();
                        editTextArea_of_service.setText(items[7]);
                        break;
                    case 8:
                        Toast.makeText(getActivity(), "Clicked on java", Toast.LENGTH_LONG).show();
                        editTextArea_of_service.setText(items[8]);
                        break;
                    case 9:
                        Toast.makeText(getActivity(), "Santa Catarina", Toast.LENGTH_LONG).show();
                        editTextArea_of_service.setText(items[9]);
                        break;
                    case 10:
                        Toast.makeText(getActivity(), "García", Toast.LENGTH_LONG).show();
                        editTextArea_of_service.setText(items[10]);
                        break;
                }
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    private void showAlertServicios() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Selecciona tu Servicio");
        String[] items = {"Electricista", "Plomería", "Mecánico", "Albañil", "Pintor", "Refrigeración", "Carpintero", "Fumigación", "Impermeabilización", "Limpieza"};
        int checkedItem = 1;
        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Toast.makeText(getActivity(), "Electricista", Toast.LENGTH_LONG).show();
                        editTextServices.setText(items[0]);
                        break;
                    case 1:
                        Toast.makeText(getActivity(), "Plomería", Toast.LENGTH_LONG).show();
                        editTextServices.setText(items[1]);
                        break;
                    case 2:
                        Toast.makeText(getActivity(), "Mecánico", Toast.LENGTH_LONG).show();
                        editTextServices.setText(items[2]);
                        break;
                    case 3:
                        Toast.makeText(getActivity(), "Albañil", Toast.LENGTH_LONG).show();
                        editTextServices.setText(items[3]);
                        break;
                    case 4:
                        Toast.makeText(getActivity(), "Pintor", Toast.LENGTH_LONG).show();
                        editTextServices.setText(items[4]);
                        break;
                    case 5:
                        Toast.makeText(getActivity(), "Refrigeración", Toast.LENGTH_LONG).show();
                        editTextServices.setText(items[5]);
                        break;
                    case 6:
                        Toast.makeText(getActivity(), "Carpintero", Toast.LENGTH_LONG).show();
                        editTextServices.setText(items[6]);
                        break;
                    case 7:
                        Toast.makeText(getActivity(), "Fumigación", Toast.LENGTH_LONG).show();
                        editTextServices.setText(items[7]);
                        break;
                    case 8:
                        Toast.makeText(getActivity(), "Impermeabilización", Toast.LENGTH_LONG).show();
                        editTextServices.setText(items[8]);
                        break;
                    case 9:
                        Toast.makeText(getActivity(), "Limpieza", Toast.LENGTH_LONG).show();
                        editTextServices.setText(items[9]);
                        break;
                }
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

}