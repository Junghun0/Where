package com.example.parkjunghun.where.where.Fragment;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.parkjunghun.where.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by parkjunghun on 2017. 11. 2..
 */

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap gMap;
    protected Location lastLocation;
    private com.google.android.gms.maps.MapFragment mapFragment;
    private LatLng location;

    private FusedLocationProviderClient gFusedLocationClient;
    private static final int RC_LOCATION = 1;

    private Button findPhone;
    private android.widget.Button currentLocation;
    private Button navigation;
    private LatLng myphonelocation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, null);
        myphonelocation = new LatLng(37.5882784 ,127.0036808);


        findPhone = (Button) view.findViewById(R.id.findmyphpne);
        findPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gMap.addMarker(new MarkerOptions().position(myphonelocation).title("내폰위치"));
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myphonelocation,14));
                gMap.animateCamera(CameraUpdateFactory.zoomTo(14),2000,null);
                Log.e("Location", String.valueOf(lastLocation.getLatitude()));
                Log.e("Location1", String.valueOf(lastLocation.getLongitude()));
            }
        });

        currentLocation = (Button) view.findViewById(R.id.currentlocation);
        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gMap.clear();
                location = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());

                gMap.addMarker(new MarkerOptions().position(location).title("현재위치"));
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,14));
                gMap.animateCamera(CameraUpdateFactory.zoomTo(14),2000,null);
            }
        });

        navigation = (Button) view.findViewById(R.id.navigation);
        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://www.google.co.kr/maps/dir/"+lastLocation.getLatitude()+","+lastLocation.getLongitude()+"/"+37.5882784+","+127.0036808));
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstance){
        super.onViewCreated(view, savedInstance);

        mapFragment = (com.google.android.gms.maps.MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        gFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mapFragment.getMapAsync(this);
    }


    @SuppressWarnings("MissingPermission")
    @AfterPermissionGranted(RC_LOCATION) // automatically re-invoke this method after getting the required permission
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(getActivity(), perms)) {
            gFusedLocationClient.getLastLocation().addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        lastLocation = task.getResult();

                        LatLng location = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                        gMap.addMarker(new MarkerOptions().position(location).title("현재위치"));
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,14));
                        gMap.animateCamera(CameraUpdateFactory.zoomTo(14),2000,null);
                    }
                }
            });
        } else {
            EasyPermissions.requestPermissions(this, "This app needs access to your location to know where you are.", RC_LOCATION, perms);
        }
    }
}
