package com.example.weatherproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weatherproject.chatpackage.MessageActivity;
import com.example.weatherproject.R;
import com.example.weatherproject.model.Chat;
import com.example.weatherproject.model.MyCallback;
import com.example.weatherproject.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private Context context;
    private List<Users> mUsers;
    private String myID;

    public UsersAdapter(Context context, List<Users> users, String myID) {
        this.context = context;
        this.mUsers = users;
        this.myID = myID;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);

        return new UsersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Users users = mUsers.get(position);
        holder.userName.setText(users.getUsername());

        findLastMessage(myID, users.getId(), new MyCallback() {
            @Override
            public void onCallback(String value, boolean isSeen, String idSender) {

                if (value.equals("")){
                    holder.textViewLastMessage.setVisibility(View.GONE);
                }else {
                    holder.textViewLastMessage.setVisibility(View.VISIBLE);
                    if (idSender.equals(myID)){
                        holder.textViewLastMessage.setText("Bạn: " + value);
                        holder.textViewLastMessage.setTextColor(ContextCompat.getColor(context, R.color.seenMessageColor));
                        holder.textViewLastMessage.setTypeface(null, Typeface.NORMAL);
                    }else {
                        holder.textViewLastMessage.setText(value);
                        if (isSeen) {
                            holder.textViewLastMessage.setTextColor(ContextCompat.getColor(context, R.color.seenMessageColor));
                            holder.textViewLastMessage.setTypeface(null, Typeface.NORMAL);
                        }else {
                            holder.textViewLastMessage.setTextColor(ContextCompat.getColor(context, R.color.unseenMessageColor));
                            holder.textViewLastMessage.setTypeface(null, Typeface.BOLD);
                        }

                    }
                }
            }
        });

        if (users.getImageURL().equals("default")) {
            holder.imageViewUser.setImageResource(R.drawable.ic_profile);
        } else {
            Glide.with(context)
                    .load(users.getImageURL())
                    .into(holder.imageViewUser);
        }


        //Status check
        if (users.getStatus().equals("online")) {
            holder.imageViewOn.setVisibility(View.VISIBLE);
            holder.imageViewOff.setVisibility(View.GONE);
        } else {
            holder.imageViewOn.setVisibility(View.GONE);
            holder.imageViewOff.setVisibility(View.VISIBLE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid", users.getId());
                context.startActivity(intent);
            }
        });

    }

    private void findLastMessage(final String myID, final String userID, final MyCallback myCallback) {
//        Log.i("USERINFO", myID + " " + userID);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String lastMessage2 = "";
                boolean isSeen2 = false;
                String idSender2 = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Chat chat = snapshot.getValue(Chat.class);

                    if ((chat.getReciver().equals(myID) && chat.getSender().equals(userID)) ||
                            (chat.getReciver().equals(userID) && chat.getSender().equals(myID))) {
                        lastMessage2 = chat.getMessager();
                        isSeen2 = chat.getIsseenReceiver();
                        idSender2 = chat.getSender();
                    }
                }
                myCallback.onCallback(lastMessage2, isSeen2, idSender2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public ImageView imageViewUser;
        public ImageView imageViewOn;
        public ImageView imageViewOff;
        public TextView textViewLastMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.tv_item_user);
            imageViewUser = itemView.findViewById(R.id.img_item_user);
            imageViewOn = itemView.findViewById(R.id.img_status_onl);
            imageViewOff = itemView.findViewById(R.id.img_status_off);
            textViewLastMessage = itemView.findViewById(R.id.tv_last_message);

        }
    }

}
