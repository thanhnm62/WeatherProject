package com.example.weatherproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weatherproject.chatpackage.MessageActivity;
import com.example.weatherproject.R;
import com.example.weatherproject.model.Chat;
import com.example.weatherproject.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessagerAdapter extends RecyclerView.Adapter<MessagerAdapter.ViewHolder> {
    private Context context;
    private List<Chat> mChats;
    private String imgMessURL;

    //firebase
    FirebaseUser firebaseUser;

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    public String sViewType = "";

    public MessagerAdapter(Context context, List<com.example.weatherproject.model.Chat> mChats, String imgMessURL) {
        this.context = context;
        this.mChats = mChats;
        this.imgMessURL = imgMessURL;
    }


    @NonNull
    @Override
    public MessagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            sViewType = "right";
            return new MessagerAdapter.ViewHolder(view);

        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            sViewType = "left";
            return new MessagerAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessagerAdapter.ViewHolder holder, int position) {

        Chat chat = mChats.get(position);
        holder.textViewMessage.setText(chat.getEdtsent());

        if (imgMessURL.equals("default")) {
            holder.imageProfile.setImageResource(R.drawable.ic_profile);
        } else {
            Glide.with(context).load(imgMessURL).into(holder.imageProfile);
        }
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//        public TextView showMessageRight;
        public ImageView imageProfile;
        public TextView textViewMessage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.profile_image);
            textViewMessage = itemView.findViewById(R.id.show_message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChats.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
