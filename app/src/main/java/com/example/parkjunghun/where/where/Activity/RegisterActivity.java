package com.example.parkjunghun.where.where.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkjunghun.where.R;
import com.example.parkjunghun.where.where.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    //define view objects
    private  EditText editTextEmail;
    private  EditText editTextPassword;
    private  EditText editTextPhone;
    private  EditText editTextName;
    private  Button buttonSignup;
    private  TextView textviewSingin;
    private  TextView textviewMessage;
    private  ProgressDialog progressDialog;
    //define firebase object
    private  FirebaseAuth firebaseAuth;


    private FirebaseUser firebaseUser;
    private  FirebaseDatabase firebaseDatabase;
    private  DatabaseReference userRef;
    private  String pass;
    String phonenum;

    long pressedTime = System.currentTimeMillis();

    @SuppressWarnings("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
        try {
            phonenum = telephonyManager.getLine1Number();
            phonenum = phonenum.replace("+82", "0");
            Log.d("editText", phonenum + "");
        }
        catch (Exception e){

        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userRef = firebaseDatabase.getReference("users");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        textviewSingin = (TextView) findViewById(R.id.textViewSignin);
        textviewMessage = (TextView) findViewById(R.id.textviewMessage);
        buttonSignup = (Button) findViewById(R.id.buttonSignup);
        progressDialog = new ProgressDialog(this);

        buttonSignup.setOnClickListener(this);
        textviewSingin.setOnClickListener(this);

    }

    private void registerUser() {

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String name = editTextName.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Phone을 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Name을 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }

        progressDialog.setMessage("등록중입니다. 기다려 주세요...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User();
                            user.setName(editTextName.getText().toString());
                            user.setEmail(editTextEmail.getText().toString());
                            user.setPhonenum(editTextPhone.getText().toString());
                            user.setPassword(editTextPassword.getText().toString());
                            user.setUid(task.getResult().getUser().getUid());

                            FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).setValue(user);
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        } else {

                            textviewMessage.setText("에러유형\n - 이미 등록된 이메일  \n -암호 최소 6자리 이상 \n - 서버에러");
                            Toast.makeText(RegisterActivity.this, "등록 에러!", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if (view == buttonSignup) {
            Log.d("editTextPhone",editTextPhone.getText().toString().trim()+" ");
            if(phonenum.equals(editTextPhone.getText().toString().trim())){
                registerUser();
            }else{
                Toast.makeText(getApplicationContext(),"회원가입 전화번호랑 현재 번호가 서로 다릅니다.",Toast.LENGTH_SHORT).show();
            }
        }

        if (view == textviewSingin) {
            startActivity(new Intent(this, LoginActivity.class)); //추가해 줄 로그인 액티비티
        }
    }

    @Override
    public void onBackPressed() {

        if (pressedTime == 0) {
            Toast.makeText(RegisterActivity.this, " 한 번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show();
            pressedTime = System.currentTimeMillis();
        } else {
            int seconds = (int) (System.currentTimeMillis() - pressedTime);

            if (seconds > 2000) {
                Toast.makeText(RegisterActivity.this, " 한 번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show();
                pressedTime = 0;
            } else {
                super.onBackPressed();
                finish();
            }
        }
    }
}





