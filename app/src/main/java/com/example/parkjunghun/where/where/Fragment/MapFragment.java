package com.example.parkjunghun.where.where.Fragment;

import android.Manifest;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;

import com.example.parkjunghun.where.R;
import com.example.parkjunghun.where.where.Model.Locationinfo;
import com.example.parkjunghun.where.where.Model.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private Button lock;
    private ToggleButton toggleButton;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference LocationRef;
    private FirebaseUser user;
    private FirebaseAuth auth;
    double longitude;
    double latitude;
    String phonenum;
    User users;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(getContext().TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
        }
        phonenum = telephonyManager.getLine1Number();

        final View view = inflater.inflate(R.layout.map_fragment, null);
        final NotificationManager nm = (NotificationManager) getContext().getSystemService(getContext().NOTIFICATION_SERVICE);
        toggleButton = (ToggleButton) view.findViewById(R.id.map_toggle);
        final LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        final LocationListener mLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                //여기서 위치값이 갱신되면 이벤트가 발생한다.
                //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.
                Log.d("test", "onLocationChanged, location:" + location);
                longitude = location.getLongitude(); //경도
                latitude= location.getLatitude();   //위도
                float accuracy = location.getAccuracy();    //정확도
                String provider = location.getProvider();   //위치제공자
                    Log.e("test", "위치정보 : " + provider + "\n위도 : " + longitude + "\n경도 : " + latitude + "\n고도 : " + accuracy);
                    //tv.setText("위치정보 : " + provider + "\n위도 : " + longitude + "\n경도 : " + latitude
//                        + "\n고도 : " + altitude + "\n정확도 : "  + accuracy);
                    Log.e("test","번호같음");

                users = new User();
                users.setLongitude(String.valueOf(longitude));
                users.setLatitude(String.valueOf(latitude));

                FirebaseDatabase.getInstance().getReference().child("Location").child(user.getUid()).setValue(users);

                final NotificationCompat.Builder mCompatBuilder = new NotificationCompat.Builder(getContext());
                mCompatBuilder.setSmallIcon(R.drawable.noti_icon);
                mCompatBuilder.setContentTitle("Where?");
                mCompatBuilder.setContentText("내폰위치 : " + "(" + latitude + "," + longitude + ")");
                mCompatBuilder.setWhen(System.currentTimeMillis());
                mCompatBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
                mCompatBuilder.setAutoCancel(true);
                nm.notify(0, mCompatBuilder.build());
            }
            public void onProviderDisabled(String provider) {
                // Disabled시
                Log.d("test2", "onProviderDisabled, provider:" + provider);
            }
            public void onProviderEnabled(String provider) {
                // Enabled시
                Log.d("test", "onProviderEnabled, provider:" + provider);
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // 변경시
                Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
            }
        };

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(toggleButton.isChecked()){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,100,1, (android.location.LocationListener) mLocationListener);
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,100,1, (android.location.LocationListener) mLocationListener);
                        Log.e("test2= ","togglebuttononclick1");
                    }
                    else{
                        locationManager.removeUpdates((android.location.LocationListener) mLocationListener);
                        Log.e("test2= ","togglebuttononclick2");
                    }
                }
                catch(SecurityException e){
                }
            }
        });

        myphonelocation = new LatLng(37.5882784, 127.0036808);

        firebaseDatabase = FirebaseDatabase.getInstance();
        LocationRef = firebaseDatabase.getReference("Location");
        user = FirebaseAuth.getInstance().getCurrentUser();

        auth = FirebaseAuth.getInstance();

        findPhone = (Button) view.findViewById(R.id.findmyphpne);
        findPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gMap.addMarker(new MarkerOptions().position(myphonelocation).title("내폰위치"));
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myphonelocation, 14));
                gMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                Log.e("location", String.valueOf(location));

                final NotificationCompat.Builder mCompatBuilder = new NotificationCompat.Builder(getContext());
                mCompatBuilder.setSmallIcon(R.drawable.noti_icon);
                mCompatBuilder.setContentTitle("Where?");
                mCompatBuilder.setContentText("내폰위치 : " + "(" + lastLocation.getLatitude() + "," + lastLocation.getLongitude() + ")");
                mCompatBuilder.setWhen(System.currentTimeMillis());
                mCompatBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
                mCompatBuilder.setAutoCancel(true);
                nm.notify(0, mCompatBuilder.build());
                WriteData();
            }
        });

        currentLocation = (Button) view.findViewById(R.id.currentlocation);
        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gMap.clear();
                location = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                gMap.addMarker(new MarkerOptions().position(location).title("현재위치"));
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14));
                gMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
            }
        });

        navigation = (Button) view.findViewById(R.id.navigation);
        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.google.co.kr/maps/dir/" + lastLocation.getLatitude() + "," + lastLocation.getLongitude() + "/" + 37.5882784 + "," + 127.0036808));
                startActivity(intent);
            }
        });
        return view;
    }

    public void WriteData() {
        Locationinfo locationinfo = new Locationinfo();
        locationinfo.setLongitude(String.valueOf(latitude));
        locationinfo.setLatitude(String.valueOf(longitude));
        locationinfo.setEmail(user.getEmail());

        FirebaseDatabase.getInstance().getReference().child("Location").child(user.getUid()).setValue(locationinfo);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);

        mapFragment = (com.google.android.gms.maps.MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        gFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mapFragment.getMapAsync(this);
    }


    @SuppressWarnings("MissingPermission")
    @AfterPermissionGranted(RC_LOCATION)
// automatically re-invoke this method after getting the required permission
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
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14));
                        gMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                    }
                }
            });
        } else {
        }
    }
}
