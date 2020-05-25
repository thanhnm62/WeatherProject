package com.example.weatherproject.chatpackage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.weatherproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText edtLoginUser,edtLoginPass;
    Button btnLogin,btnBackRegister;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    @Override

    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null){
            Intent intent = new Intent(LoginActivity.this,ChatActivity.class);
            startActivity(intent);
            finish();
        }
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AnhXa();
        auth = FirebaseAuth.getInstance();



        //BackRegister
        btnBackRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = edtLoginUser.getText().toString();
                String passText = edtLoginPass.getText().toString();

                if (TextUtils.isEmpty(emailText)|| TextUtils.isEmpty(passText)){
                    Toast.makeText(LoginActivity.this,"Nhập đủ các trường",Toast.LENGTH_SHORT).show();
                }
                else {
                    auth.signInWithEmailAndPassword(emailText,passText)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(LoginActivity.this,ChatActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                                        startActivity(intent);
                                        finish();

                                    }
                                    else {
                                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại!",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });


    }


    private void AnhXa() {
        edtLoginUser = (EditText) findViewById(R.id.edt_login_user);
        edtLoginPass = (EditText) findViewById(R.id.edt_login_pass);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnBackRegister = (Button) findViewById(R.id.btn_back_register);
    }
}
