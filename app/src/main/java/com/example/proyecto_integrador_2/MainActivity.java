package com.example.proyecto_integrador_2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    private TextView nav_header_username;
    private TextView nav_header_email;
    private TextView home_fragment_userID;
    //private ListView users_list;

    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Tomar ID del usuario getExtra de la info proporcionada en el login
        userID = getIntent().getStringExtra("uid");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View rootView = navigationView.getHeaderView(0);
        // asignarle una vista al textview para el username y correo(email)
        nav_header_username = (TextView) rootView.findViewById(R.id.nav_header_username);
        nav_header_email = (TextView) rootView.findViewById(R.id.nav_header_email);
        // Home fragment asignar vista a la lista de usuarios
        //home_fragment_userID = (TextView) rootView.findViewById(R.id.home_fragment_userID);
        //users_list = (ListView) rootView.findViewById(R.id.users_list);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        

        //Asignando el username y correo al usuario segun el userID tomado al principio
        showUsername();
        showEmail();

    }

    public void showUsername() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        DatabaseReference getEmailFromDatabase = database.getReference("Users").child(userID).child("name");
        getEmailFromDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nav_header_username.setText(snapshot.getValue(String.class));
                //Log.d(TAG, "Mostrando texto de usuario");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                nav_header_username.setText("User name");
            }
        });
    }

    public void showEmail() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        DatabaseReference getEmailFromDatabase = database.getReference("Users").child(userID).child("email");
        getEmailFromDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nav_header_email.setText(snapshot.getValue(String.class));
                //Log.d(TAG, "Mostrando texto de usuario");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                nav_header_email.setText("User name");
            }
        });
    }

    public void showUserID(){
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        DatabaseReference getEmailFromDatabase = database.getReference("Users").child(userID);
        String id = mAuth.getCurrentUser().toString();
        home_fragment_userID.setText(id);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}