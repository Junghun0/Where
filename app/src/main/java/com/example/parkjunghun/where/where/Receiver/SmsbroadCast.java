package com.example.parkjunghun.where.where.Receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.parkjunghun.where.R;
import com.example.parkjunghun.where.where.Activity.TEST;
import com.example.parkjunghun.where.where.Fragment.Map2Fragment;

/**
 * Created by lenovo on 2017-12-02.
 */

public class SmsbroadCast extends BroadcastReceiver{

    @Override
    public void onReceive(Context mContext, Intent intent) {
        String action =  intent.getAction();

        if("android.provider.Telephony.SMS_RECEIVED".equals(action)) {
            Log.d("receive", "OnReceive");

            Map2Fragment map2Fragment = new Map2Fragment();
            Bundle bundle = new Bundle();

            /*Object messages[] = (Object[])bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[messages.length];

            for(int i = 0; i < messages.length; i++) {
                *//**
                 * PDU포멧의 SMS를 변환합니다
                 *//*
                smsMessage[i] = SmsMessage.createFromPdu((byte[])messages[i]);
            }

            String Message = smsMessage[0].getMessageBody().toString();*/

            bundle.putSerializable("originText",1);
            map2Fragment.setArguments(bundle);

            /**
             * 날짜 형식을 우리나라에 맞도록 변환합니다
             */

            /*String origNumber = smsMessage[0].getOriginatingAddress();
            String Message = smsMessage[0].getMessageBody().toString();

            Intent showSMSIntent = new Intent(mContext, TEST.class);

            showSMSIntent.putExtra("originNum", origNumber);
            showSMSIntent.putExtra("originText", Message);

            showSMSIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/

        }
    }
}
