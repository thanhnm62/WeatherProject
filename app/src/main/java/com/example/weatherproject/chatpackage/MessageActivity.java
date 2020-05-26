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
import com.example.weatherproject.model.Chat;
import com.example.weatherproject.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    TextView tvUserNameProfile;
    ImageView imgUserProfile;

    EditText edtSentMessager;
    ImageButton ibtnSentMassager;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    Intent intent;

    MessagerAdapter messagerAdapter;
    List<Chat> lvChat;

    RecyclerView recyclerViewSent;

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

        //Intent tu UserAdapter gui qua
        intent = getIntent();
        final String userID = intent.getStringExtra("userid");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("MyUsers").child(userID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                tvUserNameProfile.setText(user.getUsername());

                if (user.getImageURL().equals("default")) {
                    imgUserProfile.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(MessageActivity.this)
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
                String edtsent = edtSentMessager.getText().toString();
                if (!edtsent.equals("")) {
                    SentMessage(firebaseUser.getUid(), userID, edtsent);
                } else {
                    Toast.makeText(MessageActivity.this, "Vui lòng nhập tin nhắn", Toast.LENGTH_SHORT).show();
                }
                edtSentMessager.setText("");
            }

        });

    }

    private void SentMessage(String sender, String reciver, String edtsent) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("reciver", reciver);
        hashMap.put("edtsent", edtsent);

        reference.child("Chats").push().setValue(hashMap);

    }

    private void readMessage(final String myID, final String userID, final String imageURL) {
        lvChat = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lvChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Chat chat = snapshot.getValue(Chat.class);

                    if ((chat.getReciver().equals(myID) && chat.getSender().equals(userID)) ||
                            (chat.getReciver().equals(userID) && chat.getSender().equals(myID))) {

                        lvChat.add(chat);
                        Log.i("CHATTTTT", chat + "");
                    }

                    messagerAdapter = new MessagerAdapter(MessageActivity.this, lvChat, imageURL);
                    recyclerViewSent.setAdapter(messagerAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
