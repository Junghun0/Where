package com.example.parkjunghun.where.where.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.parkjunghun.where.R;

public class TEST extends AppCompatActivity {

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent smsIntent = getIntent();

        String originNumber = smsIntent.getStringExtra("originNum");
        String originSmsText = smsIntent.getStringExtra("originText");

        Log.d("intent","넘어옴: "+originNumber);
        Log.d("intent","넘어옴: "+originSmsText);

        mp = new MediaPlayer();
        mp = MediaPlayer.create(TEST.this, R.raw.test);
        mp.setLooping(true);

        if(originSmsText.equals("노래모드 ON")){
            mp.start();
            Toast.makeText(getApplicationContext(), "노래모드 실행", Toast.LENGTH_SHORT).show();
        }else{
            mp.pause();
            Toast.makeText(getApplicationContext(), "노래정지모드 실행", Toast.LENGTH_SHORT).show();
        }


    }
}
