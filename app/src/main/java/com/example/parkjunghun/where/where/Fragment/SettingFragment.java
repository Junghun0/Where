package com.example.parkjunghun.where.where.Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment,container,false);
        ButterKnife.bind(this,view);
        aSwitch = (Switch)view.findViewById(R.id.gps_switch);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    checkGPS();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private boolean checkGPS() {

        gpsEnabled = android.provider.Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!(gpsEnabled.matches(".*gps.*") && gpsEnabled.matches(".*network.*"))) {
            new AlertDialog.Builder(getActivity()).setTitle("GPS 설정").setMessage("GPS를 활성화 하시겠습니까?").setPositiveButton("GPS 켜기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent,0);
                }
            }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).create().show();

        }
        return true;
    }
}
