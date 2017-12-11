package com.example.parkjunghun.where.where.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.example.parkjunghun.where.R;
import com.example.parkjunghun.where.where.Activity.LockActivity;
import com.example.parkjunghun.where.where.Fragment.Map2Fragment;
import com.example.parkjunghun.where.where.Model.ReceiveEvent;

import org.greenrobot.eventbus.EventBus;


public class SmsbroadCast extends BroadcastReceiver {
    private int code;
    private Context mContext;
    static MediaPlayer mp;

    @Override
    public void onReceive(Context mContext, Intent intent) {
        String action = intent.getAction();
        if ("android.provider.Telephony.SMS_RECEIVED".equals(action)) {

            Map2Fragment map2Fragment = new Map2Fragment();

            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[])bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[messages.length];

            for(int i = 0; i < messages.length; i++) {
                smsMessage[i] = SmsMessage.createFromPdu((byte[])messages[i]);
            }
            String Message = smsMessage[0].getMessageBody().toString();

            if(Message.equals("노래모드 ON")){
                mp = new MediaPlayer();
                mp = MediaPlayer.create(mContext, R.raw.test);
                mp.setLooping(true);
                mp.start();
                /*code =1;
                EventBus.getDefault().post(new ReceiveEvent(code));*/
                Toast.makeText(mContext.getApplicationContext(), "노래모드 실행", Toast.LENGTH_SHORT).show();
            } else if(Message.equals("노래모드 OFF")){
                mp.stop();
                /*code =0;
                EventBus.getDefault().post(new ReceiveEvent(code));*/
                Toast.makeText(mContext.getApplicationContext(), "노래정지모드 실행", Toast.LENGTH_SHORT).show();
            } else if(Message.equals("화면 잠금")){
                Intent i = new Intent(mContext , LockActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(i);
            } else if(Message.equals("화면 잠금 해제")){
                code =3;
                EventBus.getDefault().post(new ReceiveEvent(code));
            }

        }
    }

}
