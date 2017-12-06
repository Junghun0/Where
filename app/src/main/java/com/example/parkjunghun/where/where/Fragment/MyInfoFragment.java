package com.example.parkjunghun.where.where.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.parkjunghun.where.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by parkjunghun on 2017. 11. 2..
 */

public class MyInfoFragment extends Fragment {
    private TextView username;
    private TextView userphone;
    private TextView usermail;
    private TextView phonelocation;

    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myinfo_fragment, container, false);
        username = (TextView) view.findViewById(R.id.username);
        userphone = (TextView) view.findViewById(R.id.userphone);
        usermail = (TextView) view.findViewById(R.id.usermail);
        phonelocation = (TextView) view.findViewById(R.id.phonelocation);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userRef = firebaseDatabase.getReference("users");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String mail = dataSnapshot.child(firebaseUser.getUid()).child("email").getValue(String.class);
                String name = dataSnapshot.child(firebaseUser.getUid()).child("name").getValue(String.class);
                String pnum = dataSnapshot.child(firebaseUser.getUid()).child("phonenum").getValue(String.class);

                usermail.setText(mail);
                username.setText(name);
                userphone.setText(pnum);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

}
