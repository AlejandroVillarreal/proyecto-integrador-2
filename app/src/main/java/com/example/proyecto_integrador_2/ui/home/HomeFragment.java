package com.example.proyecto_integrador_2.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.FirebaseDatabase;

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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        //userID = getArguments().getString("uid");
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        navController = NavHostFragment.findNavController(this);
        home_recycler_view = root.findViewById(R.id.home_recycler_view);
        home_recycler_view.setLayoutManager(new LinearLayoutManager(root.getContext()));

        FirebaseRecyclerOptions<UserEntity> options =
                new FirebaseRecyclerOptions.Builder<UserEntity>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users"), UserEntity.class)
                        .build();
        adapter = new myAdapter(options, this);
        home_recycler_view.setAdapter(adapter);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();

    }


    @Override
    public void profileClicked(UserEntity userEntity) {
        Log.d(TAG, "profileClicked: working");
        String user_id = userEntity.user_id;
        navController.navigate(HomeFragmentDirections.actionNavHomeToProfileDetailFragment2(user_id));

    }

    @Override
    public void sendMessage(UserEntity userEntity) {

    }
}