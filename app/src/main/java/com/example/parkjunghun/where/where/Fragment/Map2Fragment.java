package com.example.parkjunghun.where.where.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.example.parkjunghun.where.where.Activity.TEST;

/**
 * Created by parkjunghun on 2017. 11. 3..
 */

public class Map2Fragment extends Fragment {

    Context mContext;
    Switch playSing;
    Switch lockScreen;

    String smsNum = "010-2010-8329";
    String playSingOn = "노래모드 ON";
    String playSingOff = "노래모드 OFF";
    String lockScreenOn = "화면 잠금";
    String lockScreenOff ="화면 잠금 해제";

    MediaPlayer mp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map2_fragment,container,false);

        mContext = getActivity().getApplicationContext();


        mp = new MediaPlayer();
        mp = MediaPlayer.create(getActivity(), R.raw.test);
        mp.setLooping(true);


        playSing = (Switch)view.findViewById(R.id.play_sing);
        playSing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    sendSMS(smsNum,playSingOn);
                }else{
                    sendSMS(smsNum,playSingOff);
                }
            }
        });

        lockScreen = (Switch)view.findViewById(R.id.lock_screen);
        lockScreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    sendSMS(smsNum,lockScreenOn);
                }else{
                    sendSMS(smsNum,lockScreenOff);
                }
            }
        });


        Bundle extra = getArguments();
        int originSmsText = extra.getInt("originText");
        Log.d("intent","넘어옴: "+originSmsText);

       /* if(originSmsText.equals("노래모드 ON")){
            mp.start();
            Toast.makeText(getActivity().getApplicationContext(), "노래모드 실행", Toast.LENGTH_SHORT).show();
        }else{
            mp.pause();
            Toast.makeText(getActivity().getApplicationContext(), "노래정지모드 실행", Toast.LENGTH_SHORT).show();
        }*/




        return view;
    }

    public void sendSMS(String smsNumber, String smsText){
        PendingIntent sentIntent = PendingIntent.getBroadcast(mContext, 0, new Intent("SMS_SENT_ACTION"), 0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(mContext, 0, new Intent("SMS_DELIVERED_ACTION"), 0);

        /**
         * SMS가 발송될때 실행
         * When the SMS massage has been sent
         */
        getActivity().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch(getResultCode()){
                    case Activity.RESULT_OK:
                        // 전송 성공
                        Toast.makeText(mContext, "전송 완료", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        // 전송 실패
                        Toast.makeText(mContext, "전송 실패", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        // 서비스 지역 아님
                        Toast.makeText(mContext, "서비스 지역이 아닙니다", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        // 무선 꺼짐
                        Toast.makeText(mContext, "무선(Radio)가 꺼져있습니다", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        // PDU 실패
                        Toast.makeText(mContext, "PDU Null", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_SENT_ACTION"));

        /**
         * SMS가 도착했을때 실행
         * When the SMS massage has been delivered
         */
        getActivity().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        // 도착 완료
                        Toast.makeText(mContext, "SMS 도착 완료", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        // 도착 안됨
                        Toast.makeText(mContext, "SMS 도착 실패", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_DELIVERED_ACTION"));

        SmsManager mSmsManager = SmsManager.getDefault();
        mSmsManager.sendTextMessage(smsNumber, null, smsText, sentIntent, deliveredIntent);
    }


}
