package com.example.weatherproject.chatpackage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

                if (emailText.isEmpty() || passText.isEmpty() || reenterPassword.isEmpty() || userNameText.isEmpty())
                    Toast.makeText(RegisterActivity.this, "Nhập đầy đủ trường", Toast.LENGTH_SHORT).show();
                else if (!passText.equals(reenterPassword)) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                } else if (passText.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu không được ít hơn 6 kí tự", Toast.LENGTH_SHORT).show();
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
                            hashMap.put("status", "offline");


                            myref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(RegisterActivity.this, ChatActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        Toast.makeText(RegisterActivity.this, "Đăng kí tài khoản thành công", Toast.LENGTH_SHORT).show();
                                        startActivity(intent);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Tài khoản đã tồn tại, vui lòng đăng kí tài khoản khác", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void AnhXa() {
        edtUser = findViewById(R.id.edt_user);
        edtPass = findViewById(R.id.edt_password);
        edtEmail = findViewById(R.id.edt_email);
        btnRegister = findViewById(R.id.btn_register);
        edtRepassword = findViewById(R.id.edt_repassword);
    }
}
