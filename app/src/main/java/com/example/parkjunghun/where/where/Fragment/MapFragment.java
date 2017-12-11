package com.example.parkjunghun.where.where.Fragment;

import android.Manifest;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parkjunghun.where.R;
import com.example.parkjunghun.where.where.Model.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by parkjunghun on 2017. 11. 2..
 */

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap gMap;
    private LatLng myPhoneLocation;
    private LatLng rentPhoneLocation;
    private com.google.android.gms.maps.MapFragment mapFragment;
    private Location location;

    private FusedLocationProviderClient gFusedLocationClient;
    private static final int RC_LOCATION = 1;

    private Button findPhone;
    private android.widget.Button currentLocation;
    private Button navigation;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userRef;
    private DatabaseReference locationRef;
    private FirebaseUser firebaseUser;

    double longitude;
    double latitude;
    String phonenum;
    private User users;

    EditText findEditText;
    EditText currentEditText;

    @SuppressWarnings("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.map_fragment, null);

        final NotificationManager nm = (NotificationManager) getContext().getSystemService(getContext().NOTIFICATION_SERVICE);

        findEditText = (EditText) view.findViewById(R.id.findEditText);
        findEditText.setFocusable(false);
        findEditText.setClickable(false);

        currentEditText = (EditText) view.findViewById(R.id.currentEditText);
        currentEditText.setFocusable(false);
        currentEditText.setClickable(false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userRef = firebaseDatabase.getReference("users");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String pnum = dataSnapshot.child(firebaseUser.getUid()).child("phonenum").getValue(String.class);

                TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(getContext().TELEPHONY_SERVICE);
                phonenum = telephonyManager.getLine1Number();

                if (phonenum.equals(pnum)) {
                    final LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                    final LocationListener mLocationListener = new LocationListener() {
                        public void onLocationChanged(Location location) {
                            longitude = location.getLongitude();
                            latitude = location.getLatitude();

                            float accuracy = location.getAccuracy();
                            String provider = location.getProvider();
                            Log.e("test", "위치정보 : " + provider + "\n위도 : " + longitude + "\n경도 : " + latitude + "\n고도 : " + accuracy);
                            Log.e("test", "번호같음");
                            users = new User();
                            users.setLongitude(longitude);
                            users.setLatitude(latitude);
                            FirebaseDatabase.getInstance().getReference().child("Location").child(firebaseUser.getUid()).setValue(users);

                            findPhone = (Button) view.findViewById(R.id.findmyphpne);
                            findPhone.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    gMap.clear();
                                    myPhoneLocation = new LatLng(latitude, longitude);
                                    gMap.addMarker(new MarkerOptions().position(myPhoneLocation).title("내폰위치"));
                                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPhoneLocation, 14));
                                    gMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                                    Log.e("location", String.valueOf(myPhoneLocation));

                                    try {
                                        Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.KOREA);
                                        List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
                                        if (addresses.size() >0) {
                                            Address address = addresses.get(0);
                                            String text = address.getLocality() + " " + address.getThoroughfare() + " " + address.getFeatureName() + " " + address.getPostalCode();
                                            findEditText.setText(text);
                                        }
                                    } catch (IOException e) {}

                                }
                            });

                            currentLocation = (Button) view.findViewById(R.id.currentlocation);
                            currentLocation.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    gMap.clear();
                                    rentPhoneLocation = new LatLng(latitude, longitude);
                                    gMap.addMarker(new MarkerOptions().position(myPhoneLocation).title("내폰위치"));
                                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rentPhoneLocation, 14));
                                    gMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);

                                    try {
                                        Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.KOREA);
                                        List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
                                        if (addresses.size() >0) {
                                            Address address = addresses.get(0);
                                            String text = address.getLocality() + " " + address.getThoroughfare() + " " + address.getFeatureName() + " " + address.getPostalCode();
                                            currentEditText.setText(text);
                                        }
                                    } catch (IOException e) {}

                                }
                            });

                            navigation = (Button) view.findViewById(R.id.navigation);
                            navigation.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(getActivity().getApplicationContext(),"당신의 핸드폰 입니다.",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                        public void onProviderDisabled(String provider) {}
                        public void onProviderEnabled(String provider) {}
                        public void onStatusChanged(String provider, int status, Bundle extras) {}
                    };
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, (android.location.LocationListener) mLocationListener);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, (android.location.LocationListener) mLocationListener);
                } else {
                    locationRef = firebaseDatabase.getReference("users");
                    locationRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            latitude = dataSnapshot.child(firebaseUser.getUid()).child("latitude").getValue(Double.class);
                            longitude = dataSnapshot.child(firebaseUser.getUid()).child("longitude").getValue(Double.class);

                            findPhone = (Button) view.findViewById(R.id.findmyphpne);
                            findPhone.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myPhoneLocation = new LatLng(latitude, longitude);

                                    gMap.addMarker(new MarkerOptions().position(myPhoneLocation).title("내폰위치"));
                                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPhoneLocation, 14));
                                    gMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                                    Log.e("location", String.valueOf(myPhoneLocation));

                                    final NotificationCompat.Builder mCompatBuilder = new NotificationCompat.Builder(getContext());
                                    mCompatBuilder.setSmallIcon(R.drawable.noti_icon);
                                    mCompatBuilder.setContentTitle("Where?");
                                    mCompatBuilder.setContentText("내폰위치 : " + "(" + latitude + "," + longitude + ")");
                                    mCompatBuilder.setWhen(System.currentTimeMillis());
                                    mCompatBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
                                    mCompatBuilder.setAutoCancel(true);
                                    nm.notify(0, mCompatBuilder.build());

                                    try {
                                        Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.KOREA);
                                        List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
                                        if (addresses.size() >0) {
                                            Address address = addresses.get(0);
                                            String text = address.getLocality() + " " + address.getThoroughfare() + " " + address.getFeatureName() + " " + address.getPostalCode();
                                            findEditText.setText(text);
                                        }
                                    } catch (IOException e) {}
                                }
                            });

                            currentLocation = (Button) view.findViewById(R.id.currentlocation);
                            currentLocation.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    gMap.clear();
                                    rentPhoneLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                    gMap.addMarker(new MarkerOptions().position(rentPhoneLocation).title("현재위치").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rentPhoneLocation, 14));
                                    gMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);

                                    try {
                                        Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.KOREA);
                                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                        if (addresses.size() >0) {
                                            Address address = addresses.get(0);
                                            String text = address.getLocality() + " " + address.getThoroughfare() + " " + address.getFeatureName() + " " + address.getPostalCode();
                                            currentEditText.setText(text);
                                        }
                                    } catch (IOException e) {}
                                }
                            });

                            navigation = (Button) view.findViewById(R.id.navigation);
                            navigation.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse("https://www.google.co.kr/maps/dir/" + location.getLatitude() + "," + location.getLongitude() + "/" + latitude + "," + longitude));
                                    startActivity(intent);
                                }
                            });

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        return view;
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
                        location = task.getResult();

                        rentPhoneLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        gMap.addMarker(new MarkerOptions().position(rentPhoneLocation).title("현재위치").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rentPhoneLocation, 14));
                        gMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                    }
                }
            });
        } else {
        }
    }
}
