package com.example.proyecto_integrador_2.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyecto_integrador_2.R;
import com.example.proyecto_integrador_2.data.database.entities.UserEntity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class myAdapter extends FirebaseRecyclerAdapter<UserEntity, myAdapter.myViewholder> {

    private ProfileInterface profileInterface;

    public myAdapter(@NonNull FirebaseRecyclerOptions options, ProfileInterface profileInterface) {
        super(options);
        this.profileInterface = profileInterface;
    }

    @NonNull
    @Override
    public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow, parent, false);

        return new myViewholder(view);

    }

    @Override
    protected void onBindViewHolder(@NonNull myViewholder myViewholder, int i, @NonNull UserEntity userEntity) {
        myViewholder.name.setText(userEntity.name);
        myViewholder.email.setText(userEntity.email);
        myViewholder.phone.setText(userEntity.phone);
        myViewholder.area_of_service.setText(userEntity.area_of_service);
        myViewholder.service.setText(userEntity.services);
        Glide.with(myViewholder.image.getContext()).load(userEntity.profile_pic).into(myViewholder.image);

        myViewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profileInterface.profileClicked(userEntity);
            }
        });
    }

    class myViewholder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, email, phone, area_of_service, service;

        public myViewholder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imgProfile);
            name = itemView.findViewById(R.id.textViewName);
            email = itemView.findViewById(R.id.textViewEmail);
            phone = itemView.findViewById(R.id.textViewPhone);
            area_of_service = itemView.findViewById(R.id.textViewServiceArea);
            service = itemView.findViewById(R.id.textViewServices);
        }
    }



}
