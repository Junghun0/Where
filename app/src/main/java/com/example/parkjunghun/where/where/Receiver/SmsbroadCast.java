package com.example.parkjunghun.where.where.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.example.parkjunghun.where.where.Fragment.Map2Fragment;
import com.example.parkjunghun.where.where.Model.ReceiveEvent;

import org.greenrobot.eventbus.EventBus;


public class SmsbroadCast extends BroadcastReceiver {
    private int code;

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
                code =1;
                map2Fragment.setPlayMusic(code);
                EventBus.getDefault().post(new ReceiveEvent(code));
            } else if(Message.equals("노래모드 OFF")){
                code =0;
                map2Fragment.setPlayMusic(code);
                EventBus.getDefault().post(new ReceiveEvent(code));
            } else if(Message.equals("화면 잠금")){
                code =2;
                map2Fragment.setPlayMusic(code);
                EventBus.getDefault().post(new ReceiveEvent(code));
            } else if(Message.equals("화면 잠금 해제")){
                code =3;
                EventBus.getDefault().post(new ReceiveEvent(code));
            }

        }
    }


}
