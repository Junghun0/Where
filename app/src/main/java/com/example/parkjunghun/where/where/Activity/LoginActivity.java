package com.example.parkjunghun.where.where.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkjunghun.where.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignin;
    private TextView textviewSingin;
    private TextView textviewMessage;
    private TextView textviewFindPassword;
    private ProgressDialog progressDialog;
    //define firebase object
    private FirebaseAuth firebaseAuth;
    String phonenum;
    CheckBox checkBox;
    SharedPreferences autologin;
    SharedPreferences.Editor editor;
    String ID;
    String PW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textviewSingin = (TextView) findViewById(R.id.textViewSignin);
        textviewMessage = (TextView) findViewById(R.id.textviewMessage);
        textviewFindPassword = (TextView) findViewById(R.id.textViewFindpassword);
        buttonSignin = (Button) findViewById(R.id.buttonSignup);
        progressDialog = new ProgressDialog(this);
        checkBox = (CheckBox)findViewById(R.id.autologin_checkbox);

        buttonSignin.setOnClickListener(this);
        textviewSingin.setOnClickListener(this);
        textviewFindPassword.setOnClickListener(this);

        autologin = getSharedPreferences("setting", 0);
        editor= autologin.edit();

        if(autologin.getBoolean("autologin", false)){
            editTextEmail.setText(autologin.getString("ID", ""));
            editTextPassword.setText(autologin.getString("PW", ""));
            checkBox.setChecked(true);
        }


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    ID = editTextEmail.getText().toString();
                    PW = editTextPassword.getText().toString();

                    editor.putString("ID", ID);
                    editor.putString("PW", PW);
                    editor.putBoolean("autologin", true);

                    editor.commit();
                }else{
                    editor.clear();
                    editor.commit();
                }
            }
        });
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("로그인중입니다. 잠시 기다려 주세요...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            //finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "로그인 실패!", Toast.LENGTH_LONG).show();
                            textviewMessage.setText("로그인 실패 유형\n - password가 맞지 않습니다.\n -서버에러");
                        }
                    }
                });
    }
    @Override
    public void onClick(View view) {
        if (view == buttonSignin) {
            userLogin();
        }
        if (view == textviewSingin) {
            startActivity(new Intent(this, RegisterActivity.class));
        }
        if (view == textviewFindPassword) {
            startActivity(new Intent(this, FindActivity.class));
        }
    }
}