package com.example.parkjunghun.where.where.Activity;

import android.app.KeyguardManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.parkjunghun.where.R;
import com.example.parkjunghun.where.where.Fragment.MapFragment;

public class LockActivity extends AppCompatActivity implements View.OnClickListener{

    Button unlock = (Button) findViewById(R.id.unlock);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_lock);

    }


    @Override
    public void onClick(View v) {
        if(v == unlock){

            KeyguardManager km = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);

            KeyguardManager.KeyguardLock keyLock = km.newKeyguardLock(KEYGUARD_SERVICE);

            keyLock.disableKeyguard();

            finish();
            startActivity(new Intent(this, LoginActivity.class));


        }
    }

    public boolean onKeyDown(int KeyCode, KeyEvent event){
        if(KeyCode == KeyEvent.KEYCODE_BACK){
            Toast toast = Toast.makeText(getApplicationContext(),"못나감",Toast.LENGTH_LONG);
            toast.show();
        }
        return true;
    }
}
