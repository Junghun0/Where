package com.example.parkjunghun.where.where.Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.parkjunghun.where.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by parkjunghun on 2017. 11. 2..
 */

public class SettingFragment extends Fragment {
    private String gpsEnabled;

    @BindView(R.id.gps_switch)
    private Switch aSwitch;

    @BindView(R.id.noti_switch)
    Switch nSwitch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final NotificationManager nm = (NotificationManager) getContext().getSystemService(getContext().NOTIFICATION_SERVICE);
        View view = inflater.inflate(R.layout.setting_fragment, container, false);
        ButterKnife.bind(this, view);
        aSwitch = (Switch) view.findViewById(R.id.gps_switch);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    checkGPS1();
                } else {
                    checkGPS2();
                }
            }
        });
        nSwitch = (Switch) view.findViewById(R.id.noti_switch);
        nSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    Toast.makeText(getActivity(), "알림보내기", Toast.LENGTH_SHORT).show();
                    final NotificationCompat.Builder mCompatBuilder = new NotificationCompat.Builder(getContext());
                    mCompatBuilder.setSmallIcon(R.drawable.noti_icon);
                    mCompatBuilder.setContentTitle("Where?");
                    mCompatBuilder.setContentText("알림 : " + "(" + "," + ")");
                    mCompatBuilder.setWhen(System.currentTimeMillis());
                    mCompatBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
                    mCompatBuilder.setAutoCancel(true);
                    nm.notify(0, mCompatBuilder.build());
                } else {
                    Toast.makeText(getActivity(), "꺼짐", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
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
