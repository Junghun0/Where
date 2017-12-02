package com.example.parkjunghun.where.where.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.parkjunghun.where.R;
import com.example.parkjunghun.where.where.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Objects;


/**
 * Created by parkjunghun on 2017. 11. 2..
 */

public class MyInfoFragment extends Fragment {
    TextView username;
    TextView userphone;
    TextView usermail;
    TextView phonelocation;

    FirebaseUser user;
    User dbuser;
    Map<String, Objects> usrmap;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myinfo_fragment, container, false);
        username = (TextView) view.findViewById(R.id.username);
        userphone = (TextView) view.findViewById(R.id.userphone);
        usermail = (TextView) view.findViewById(R.id.usermail);
        phonelocation = (TextView) view.findViewById(R.id.phonelocation);


        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            String phone = user.getPhoneNumber();
            userphone.setText(phone);
            usermail.setText(email);
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        userRef = firebaseDatabase.getReference("users");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usrmap = (Map<String, Objects>) dataSnapshot.getValue();
                for (String mapkey : usrmap.keySet()){
                    System.out.println("key:"+mapkey+",value:"+usrmap.get(mapkey));
                    username.setText(mapkey);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("error = ", "Failed to read value.", databaseError.toException());
            }
        });


        return view;
    }

}
