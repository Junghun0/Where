package com.example.parkjunghun.where.where.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.parkjunghun.where.R;

/**
 * Created by parkjunghun on 2017. 11. 2..
 */

public class MapFragment extends Fragment {

    public MapFragment(){
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment,container,false);
        return view;
    }
}
