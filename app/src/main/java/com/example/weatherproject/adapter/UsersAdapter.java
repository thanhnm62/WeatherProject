package com.example.weatherproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weatherproject.R;
import com.example.weatherproject.model.Users;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private Context context;
    private List<Users> Users;

    public UsersAdapter(Context context, List<com.example.weatherproject.model.Users> users) {
        this.context = context;
        Users = users;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);

        return new UsersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users users = Users.get(position);
        holder.userName.setText(users.getUsername());
        if (users.getImageURL().equals("default")){
            holder.imageViewUser.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(context)
                    .load(users.getImageURL())
                    .into(holder.imageViewUser);
        }

    }

    @Override
    public int getItemCount() {
        return Users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView userName;
        public ImageView imageViewUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.tv_item_user);
            imageViewUser = itemView.findViewById(R.id.img_item_user);
        }
    }
}
