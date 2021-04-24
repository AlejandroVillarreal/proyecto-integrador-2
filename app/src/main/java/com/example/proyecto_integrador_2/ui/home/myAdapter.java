package com.example.proyecto_integrador_2.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyecto_integrador_2.R;
import com.example.proyecto_integrador_2.data.database.entities.UserEntity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

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
        //String user_id = userEntity.user_id;
        Glide.with(myViewholder.image.getContext()).load(userEntity.profile_pic).into(myViewholder.image);

        myViewholder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profileInterface.profileClicked(userEntity);
                //navController.navigate(HomeFragmentDirections.actionNavHomeToProfileDetailFragment2(userEntity.firebaseId));
                //String user_id = userEntity.user_id;
                //navController.navigate(HomeFragmentDirections.actionNavHomeToProfileDetailFragment2(userEntity.user_id));

            }
        });
    }

    class myViewholder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView name, email, phone;
        RelativeLayout layout;

        public myViewholder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img1);
            name = itemView.findViewById(R.id.nametext);
            email = itemView.findViewById(R.id.emailtext);
            phone = itemView.findViewById(R.id.phonetext);
            layout = itemView.findViewById(R.id.profile_view);
        }
    }
}
