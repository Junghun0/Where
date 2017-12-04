package com.example.parkjunghun.where.where.Fragment;

import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.parkjunghun.where.R;
import com.example.parkjunghun.where.where.Model.ReceiveEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by parkjunghun on 2017. 11. 3..
 */

public class Map2Fragment extends Fragment {

    Context mContext;
    Switch playMusic;
    Switch lockScreen;

    String smsNum = "010-2010-8329";
    String playMusicOn = "노래모드 ON";
    String playMusicOff = "노래모드 OFF";
    String lockScreenOn = "화면 잠금";
    String lockScreenOff = "화면 잠금 해제";

    private int playMusicCode;

    MediaPlayer mp = new MediaPlayer();;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map2_fragment, container, false);

        mContext = getActivity().getApplicationContext();

        playMusic = (Switch) view.findViewById(R.id.play_sing);
        playMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    sendSMS(smsNum, playMusicOn);
                } else {
                    sendSMS(smsNum, playMusicOff);
                }
            }
        });

        lockScreen = (Switch) view.findViewById(R.id.lock_screen);
        lockScreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    sendSMS(smsNum, lockScreenOn);
                } else {
                    sendSMS(smsNum, lockScreenOff);
                }
            }
        });
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        getPlayMusicCode();
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

    @Override
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void sendSMS(String smsNumber, String smsText) {
        PendingIntent sentIntent = PendingIntent.getBroadcast(mContext, 0, new Intent("SMS_SENT_ACTION"), 0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(mContext, 0, new Intent("SMS_DELIVERED_ACTION"), 0);

        SmsManager mSmsManager = SmsManager.getDefault();
        mSmsManager.sendTextMessage(smsNumber, null, smsText, sentIntent, deliveredIntent);
    }

    public void setPlayMusic(int playMusicCode) {
        this.playMusicCode = playMusicCode;
    }

    public int getPlayMusicCode() {
        return playMusicCode;
    }

    @Subscribe
    public void playMusic(ReceiveEvent event){
        playMusicCode = event.getPlayMusic();
        Log.d("test", playMusicCode + "");

        if (playMusicCode == 1) {
            mp = MediaPlayer.create(getActivity(), R.raw.test);
            mp.setLooping(true);
            mp.start();
            Toast.makeText(getActivity().getApplicationContext(), "노래모드 실행", Toast.LENGTH_SHORT).show();
        }else{
            mp.stop();
            Toast.makeText(getActivity().getApplicationContext(), "노래정지모드 실행", Toast.LENGTH_SHORT).show();
        }

    }

}
