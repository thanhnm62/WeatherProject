package com.example.weatherproject.chatpackage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weatherproject.R;
import com.example.weatherproject.adapter.MessagerAdapter;
import com.example.weatherproject.chatpackage.Notification.Client;
import com.example.weatherproject.chatpackage.Notification.Data;
import com.example.weatherproject.chatpackage.Notification.MyResponse;
import com.example.weatherproject.chatpackage.Notification.Sender;
import com.example.weatherproject.chatpackage.Notification.Token;
import com.example.weatherproject.chatpackage.fragments.APIService;
import com.example.weatherproject.model.Chat;
import com.example.weatherproject.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {

    TextView tvUserNameProfile;
    CircleImageView imgUserProfile;

    EditText edtSentMessager;
    ImageButton ibtnSentMassager;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    Intent intent;

    MessagerAdapter messagerAdapter;
    List<Chat> lvChat;

    RecyclerView recyclerViewSent;

    ValueEventListener seenListener;

    String userID;
    APIService apiService = null;

    boolean notify = false;
    String status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        tvUserNameProfile = findViewById(R.id.tv_username_profile);
        imgUserProfile = findViewById(R.id.img_user_profile);

        ibtnSentMassager = findViewById(R.id.ibtn_sent_messager);
        edtSentMessager = findViewById(R.id.edt_sent_messager);


        //Recycleview
        recyclerViewSent = findViewById(R.id.recycle_view_messager);
        recyclerViewSent.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewSent.setLayoutManager(linearLayoutManager);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        //Intent tu UserAdapter gui qua
        intent = getIntent();
         userID = intent.getStringExtra("userid");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("MyUsers").child(userID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                tvUserNameProfile.setText(user.getUsername());
//                Toast.makeText(MessageActivity.this, "On processing", Toast.LENGTH_SHORT).show();
                status = user.getStatus();
                if (user.getImageURL().equals("default")) {
                    imgUserProfile.setImageResource(R.drawable.ic_profile);
                } else {
                    Glide.with(getApplicationContext())
                            .load(user.getImageURL())
                            .into(imgUserProfile);
                }
                readMessage(firebaseUser.getUid(), userID, user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //sent mess
        ibtnSentMassager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals("offline")){
                    notify = true;
                }
                String edtsent = edtSentMessager.getText().toString();
                if (!edtsent.equals("")) {
                    SentMessage(firebaseUser.getUid(), userID, edtsent);
                } else {
                    Toast.makeText(MessageActivity.this, "Vui lòng nhập tin nhắn", Toast.LENGTH_SHORT).show();
                }
                edtSentMessager.setText("");
            }

        });
        SeenMessage(userID);
    }


    private void SeenMessage(final String userID){
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot :dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
//                    Log.i("CHATTTT", chat.getReciver() + "");
                    if (chat.getReciver().equals(firebaseUser.getUid()) && chat.getSender().equals(userID)){
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("isseenSender",true);
                        hashMap.put("isseenReceiver", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void SentMessage(String sender, final String reciver, String messager) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("reciver", reciver);
        hashMap.put("messager", messager);
        hashMap.put("isseenSender",true);
        hashMap.put("isseenReceiver", false);

        reference.child("Chats").push().setValue(hashMap);


        final String msg = messager;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("MyUsers").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                if (notify) {
                    sendNotifiaction(reciver, user.getUsername(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendNotifiaction(String reciver, final String username, final String msg) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Tokens");
        Query query = reference.orderByKey().equalTo(reciver);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(firebaseUser.getUid(), R.mipmap.ic_launcher, username+": "+msg, "New Message",
                            userID);


                    Sender sender = new Sender(data, token.getToken().toString());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            Toast.makeText(MessageActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessage(final String myID, final String userID, final String imageURL) {
        lvChat = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lvChat.clear();
                String a = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Chat chat = snapshot.getValue(Chat.class);
                    if ((chat.getReciver().equals(myID) && chat.getSender().equals(userID)) ||
                            (chat.getReciver().equals(userID) && chat.getSender().equals(myID))) {

                        lvChat.add(chat);
//                        Log.i("CHATTTTT", chat + "");
//                        a = snapshot.getKey();
                    }
                    messagerAdapter = new MessagerAdapter(MessageActivity.this, lvChat, imageURL);
                    recyclerViewSent.setAdapter(messagerAdapter);

                }
                Log.i("IdLastMESSAGE", a);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void CheckStatus(String status){
        databaseReference = FirebaseDatabase.getInstance().getReference("MyUsers").child(firebaseUser.getUid());
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status",status);
        databaseReference.updateChildren(hashMap);
    }


    @Override
    protected void onResume() {
        super.onResume();
        CheckStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        databaseReference.removeEventListener(seenListener);
        CheckStatus("offline");
    }
}
