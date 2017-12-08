package com.example.parkjunghun.where.where.Activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.parkjunghun.where.R;
import com.example.parkjunghun.where.where.Model.ReceiveEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class LockActivity extends AppCompatActivity{

    private int code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lock);

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    Toast.makeText(getApplicationContext(),"LOCK",Toast.LENGTH_LONG).show();
                    return true;
                case KeyEvent.KEYCODE_MENU:
                    Toast.makeText(getApplicationContext(),"LOCK",Toast.LENGTH_LONG).show();
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onPause(){
        super.onPause();
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(),0);
    }

    @Override
    public void onResume(){
        super.onResume();
        getUnLockCode();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public int getUnLockCode() {
        return code;
    }

    @Subscribe
    public void unLock(ReceiveEvent event){
        code = event.getCode();
        Log.d("test", code + "");

        if(code == 3){
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
