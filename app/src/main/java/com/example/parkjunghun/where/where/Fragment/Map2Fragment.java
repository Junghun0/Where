package com.example.parkjunghun.where.where.Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.parkjunghun.where.R;
import com.example.parkjunghun.where.where.Activity.LockActivity;
import com.example.parkjunghun.where.where.Model.ReceiveEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by parkjunghun on 2017. 11. 3..
 */

public class Map2Fragment extends Fragment {

    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userRef;

    private Context mContext;
    private Switch playMusic;
    private Switch lockScreen;

    private static String smsNum;
    private String phonenum;
    private String playMusicOn = "노래모드 ON";
    private String playMusicOff = "노래모드 OFF";
    private String lockScreenOn = "화면 잠금";
    private String lockScreenOff = "화면 잠금 해제";

    private int code;

    private MediaPlayer mp = new MediaPlayer();

    private String gpsEnabled;
    private Switch gpsSwitch;

    @SuppressWarnings("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map2_fragment, container, false);

        mContext = getActivity().getApplicationContext();
        playMusic = (Switch) view.findViewById(R.id.play_sing);
        lockScreen = (Switch) view.findViewById(R.id.lock_screen);

        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(getContext().TELEPHONY_SERVICE);
        phonenum = telephonyManager.getLine1Number();

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userRef = firebaseDatabase.getReference("users");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                smsNum = dataSnapshot.child(firebaseUser.getUid()).child("phonenum").getValue(String.class);

                if(smsNum.equals(phonenum)){
                    playMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            Toast.makeText(getActivity().getApplicationContext(),"당신의 핸드폰 입니다.",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    playMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked == true) {
                                Log.d("smsNum",smsNum+"");
                                sendSMS(smsNum, playMusicOn);
                            } else {
                                Log.d("smsNum",smsNum+"");
                                sendSMS(smsNum, playMusicOff);
                            }
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                smsNum = dataSnapshot.child(firebaseUser.getUid()).child("phonenum").getValue(String.class);

                if(smsNum.equals(phonenum)){
                    lockScreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            Toast.makeText(getActivity().getApplicationContext(),"당신의 핸드폰 입니다.",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
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
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        gpsSwitch = (Switch) view.findViewById(R.id.gps_switch);
        gpsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    checkGPS1();
                } else {
                    checkGPS2();
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

    public void sendSMS(String smsNumber, String smsText) {
        PendingIntent sentIntent = PendingIntent.getBroadcast(mContext, 0, new Intent("SMS_SENT_ACTION"), 0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(mContext, 0, new Intent("SMS_DELIVERED_ACTION"), 0);

        SmsManager mSmsManager = SmsManager.getDefault();
        mSmsManager.sendTextMessage(smsNumber, null, smsText, sentIntent, deliveredIntent);
    }

    public void setPlayMusic(int playMusicCode) {
        this.code = playMusicCode;
    }

    public int getPlayMusicCode() {
        return code;
    }

    @Subscribe
    public void playMusic(ReceiveEvent event){
        code = event.getCode();
        Log.d("test", code + "");

        if (code == 1) {
            mp = MediaPlayer.create(getActivity(), R.raw.test);
            mp.setLooping(true);
            mp.start();
            Toast.makeText(getActivity().getApplicationContext(), "노래모드 실행", Toast.LENGTH_SHORT).show();
        }
        if(code == 0){
            mp.stop();
            Toast.makeText(getActivity().getApplicationContext(), "노래정지모드 실행", Toast.LENGTH_SHORT).show();
        }
        if(code == 2){
            Intent intent = new Intent(getActivity(), LockActivity.class);
            startActivity(intent);
        }
    }



    private boolean checkGPS1() {
        gpsEnabled = android.provider.Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!(gpsEnabled.matches(".*gps.*") && gpsEnabled.matches(".*network.*"))) {
            new AlertDialog.Builder(getActivity()).setTitle("GPS").setMessage("GPS를 활성화 하시겠습니까?").setPositiveButton("하기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, 0);
                }
            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {}
            }).create().show();
        }
        return true;
    }

    private boolean checkGPS2(){
        gpsEnabled = android.provider.Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if ((gpsEnabled.matches(".*gps.*") && gpsEnabled.matches(".*network.*"))) {
            new AlertDialog.Builder(getActivity()).setTitle("GPS").setMessage("GPS를 활성화하지 않겠습니까?").setPositiveButton("하기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, 0);
                }
            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {}
            }).create().show();
        }
        return true;
    }

}
