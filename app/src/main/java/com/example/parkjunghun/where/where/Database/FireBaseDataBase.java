package com.example.parkjunghun.where.where.Database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by lenovo on 2017-11-17.
 */

public class FireBaseDataBase {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public FireBaseDataBase(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }



}