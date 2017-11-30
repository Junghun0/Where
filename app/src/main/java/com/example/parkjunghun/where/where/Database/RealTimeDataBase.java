package com.example.parkjunghun.where.where.Database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.DatabaseMetaData;

/**
 * Created by lenovo on 2017-11-17.
 */

public class RealTimeDataBase {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public RealTimeDataBase(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }


}