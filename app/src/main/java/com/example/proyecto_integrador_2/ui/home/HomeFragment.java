package com.example.proyecto_integrador_2.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_integrador_2.R;
import com.example.proyecto_integrador_2.data.database.entities.UserEntity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class HomeFragment extends Fragment implements ProfileInterface {

    private static final String TAG = "HomeFragment";
    private HomeViewModel homeViewModel;
    private TextView home_username;
    private String userID;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private RecyclerView home_recycler_view;
    private myAdapter adapter;
    private NavController navController;
    private SearchView searchView;
    private DatabaseReference databaseReference;
    private Query firebaseQuery;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        //userID = getArguments().getString("uid");
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        navController = NavHostFragment.findNavController(this);
        home_recycler_view = root.findViewById(R.id.home_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        home_recycler_view.setLayoutManager(layoutManager);
        //home_recycler_view.setLayoutManager(new LinearLayoutManager(root.getContext()));

        searchView = root.findViewById(R.id.home_search_view);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        //firebaseQuery = FirebaseDatabase.getInstance().getReference().child("Users").child("name").equalTo(searchText);
/*
        FirebaseRecyclerOptions<UserEntity> options =
                new FirebaseRecyclerOptions.Builder<UserEntity>()
                        .setQuery(firebaseQuery, UserEntity.class)
                        .build();

        adapter = new myAdapter(options, this);
        home_recycler_view.setAdapter(adapter);*/


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStartListening");
        //adapter.startListening();
        if (databaseReference != null) {
            Log.d(TAG, "Showing unfiltered data");
            firebaseQuery = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("prom");
            //firebaseQuery = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("name").equalTo("Alejandro Villarreal");
            FirebaseRecyclerOptions<UserEntity> options =
                    new FirebaseRecyclerOptions.Builder<UserEntity>()
                            .setQuery(firebaseQuery, UserEntity.class)
                            .build();

            adapter = new myAdapter(options, this);
            home_recycler_view.setAdapter(adapter);
            adapter.startListening();
        }
        if (searchView != null) {
            Log.d(TAG, "Showing query results");
            searchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlertAreas();
                    searchView.clearFocus();
                }
            });
//            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    return false;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String newText) {
//                    firebaseSearch(newText);
//                    return true;
//                }
//            });
        }


    }

    @Override
    public void onStop() {
        super.onStop();

        adapter.stopListening();
        Log.d(TAG, "onStopListening");

    }


    @Override
    public void profileClicked(UserEntity userEntity) {
        Log.d(TAG, "profileClicked: working");
        String user_id = userEntity.user_id;
        navController.navigate(HomeFragmentDirections.actionNavHomeToNavProfile(user_id));
    }

    @Override
    public void sendMessage(UserEntity userEntity) {

    }

    private void firebaseSearch(String searchText) {
        Log.d(TAG, searchText);
        Query firebaseSearchQuery = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("area_of_service").equalTo(searchText);
        FirebaseRecyclerOptions<UserEntity> options =
                new FirebaseRecyclerOptions.Builder<UserEntity>()
                        .setQuery(firebaseSearchQuery, UserEntity.class)
                        .build();
        adapter = new myAdapter(options, this);
        home_recycler_view.setAdapter(adapter);
        adapter.startListening();
        Log.d(TAG, "Showing filtered data");
    }

    private void searchViewQueryListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);
                return false;
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
                        //editTextArea_of_service.setText(items[0]);
                        firebaseSearch(items[0]);
                        dialog.dismiss();
                        break;
                    case 1:
                        Toast.makeText(getActivity(), "San Nicolás", Toast.LENGTH_LONG).show();
                        //editTextArea_of_service.setText(items[1]);
                        firebaseSearch(items[1]);
                        dialog.dismiss();
                        break;
                    case 2:
                        Toast.makeText(getActivity(), "General Escobedo", Toast.LENGTH_LONG).show();
                        //editTextArea_of_service.setText(items[2]);
                        firebaseSearch(items[2]);
                        dialog.dismiss();
                        break;
                    case 3:
                        Toast.makeText(getActivity(), "San Pedro", Toast.LENGTH_LONG).show();
                        //editTextArea_of_service.setText(items[3]);
                        firebaseSearch(items[3]);
                        dialog.dismiss();
                        break;
                    case 4:
                        Toast.makeText(getActivity(), "Guadalupe", Toast.LENGTH_LONG).show();
                        //editTextArea_of_service.setText(items[4]);
                        firebaseSearch(items[4]);
                        dialog.dismiss();
                        break;
                    case 5:
                        Toast.makeText(getActivity(), "Juárez", Toast.LENGTH_LONG).show();
                        //editTextArea_of_service.setText(items[5]);
                        firebaseSearch(items[5]);
                        dialog.dismiss();
                        break;
                    case 6:
                        Toast.makeText(getActivity(), "Apodaca", Toast.LENGTH_LONG).show();
                        //editTextArea_of_service.setText(items[6]);
                        firebaseSearch(items[6]);
                        dialog.dismiss();
                        break;
                    case 7:
                        Toast.makeText(getActivity(), "Santiago", Toast.LENGTH_LONG).show();
                        firebaseSearch(items[7]);
                        //editTextArea_of_service.setText(items[7]);
                        dialog.dismiss();
                        break;
                    case 8:
                        Toast.makeText(getActivity(), "Clicked on java", Toast.LENGTH_LONG).show();
                        //editTextArea_of_service.setText(items[8]);
                        firebaseSearch(items[8]);
                        dialog.dismiss();
                        break;
                    case 9:
                        Toast.makeText(getActivity(), "Santa Catarina", Toast.LENGTH_LONG).show();
                        firebaseSearch(items[9]);
                        dialog.dismiss();
                        //editTextArea_of_service.setText(items[9]);
                        break;
                    case 10:
                        Toast.makeText(getActivity(), "García", Toast.LENGTH_LONG).show();
                        firebaseSearch(items[10]);
                        dialog.dismiss();
                        //editTextArea_of_service.setText(items[10]);
                        break;
                }
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

}