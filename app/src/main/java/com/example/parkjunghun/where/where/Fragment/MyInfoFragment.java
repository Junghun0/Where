package com.example.parkjunghun.where.where.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.parkjunghun.where.R;

/**
 * Created by parkjunghun on 2017. 11. 2..
 */

public class MyInfoFragment extends Fragment {
    TextView username;
    TextView userphone;
    TextView usermail;
    TextView phonelocation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myinfo_fragment,container,false);
        username = (TextView)view.findViewById(R.id.username);
        userphone = (TextView)view.findViewById(R.id.userphone);
        usermail = (TextView)view.findViewById(R.id.phonelocation);




        return view;

    }
}
