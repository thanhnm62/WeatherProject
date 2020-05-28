package com.example.weatherproject.chatpackage;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherproject.MainActivity;
import com.example.weatherproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText edtUser, edtPass, edtEmail, edtRepassword;
    Button btnRegister;
    //FireBase
    FirebaseAuth auth;
    DatabaseReference myref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AnhXa();
        auth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameText = edtUser.getText().toString();
                String emailText = edtEmail.getText().toString();
                String passText = edtPass.getText().toString();
                String reenterPassword = edtRepassword.getText().toString();

                if (TextUtils.isEmpty(userNameText) || TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passText) || TextUtils.isEmpty(reenterPassword))
                    Toast.makeText(RegisterActivity.this, "Nhập đầy đủ trường", Toast.LENGTH_SHORT).show();
                else if (!passText.equals(reenterPassword)) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                } else {
                    Register(userNameText, emailText, passText);
                }
            }
        });
    }

    private void Register(final String userName, final String email, final String pass) {
        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userId = firebaseUser.getUid();

                            myref = FirebaseDatabase.getInstance()
                                    .getReference("MyUsers")
                                    .child(userId);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userId);
                            hashMap.put("username", userName);
                            hashMap.put("imageURL", "default");
                            hashMap.put("status","offline");

                            Intent intent = new Intent(RegisterActivity.this, ChatActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            Toast.makeText(RegisterActivity.this, "Đăng kí tài khoản thành công", Toast.LENGTH_SHORT).show();
                            finish();
                            //Chuyen ssang man hinh chinh
                            myref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {


                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Chưa nhập Email hoặc PassWord", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void AnhXa() {
        edtUser = (EditText) findViewById(R.id.edt_user);
        edtPass = (EditText) findViewById(R.id.edt_password);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        btnRegister = (Button) findViewById(R.id.btn_register);
        edtRepassword = findViewById(R.id.edt_repassword);
    }
}
