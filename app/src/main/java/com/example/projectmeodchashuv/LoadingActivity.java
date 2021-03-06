package com.example.projectmeodchashuv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoadingActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference dbteruzRef;
    DatabaseReference dbuserRef;
    public static Boolean first = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        database = FirebaseDatabase.getInstance();
        dbteruzRef = database.getReference("teruzim");
        dbuserRef = database.getReference("users");
        dbteruzRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                GenericTypeIndicator<ArrayList<Teruzim>> t = new GenericTypeIndicator<ArrayList<Teruzim>>() {
                };
                ArrayList<Teruzim> fbTeruzim = dataSnapshot.getValue(t);
                //GenericTypeIndicator<String> t = new GenericTypeIndicator<String>() {};
                //String kaki = dataSnapshot.getValue(t);
                //String kaki2 = kaki;
                DataModel.teruzims.clear();

                DataModel.teruzims.addAll(fbTeruzim);


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
        dbuserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<User>> t = new GenericTypeIndicator<ArrayList<User>>() {
                };
                ArrayList<User> fbUser = dataSnapshot.getValue(t);
                DataModel.users.clear();
                DataModel.users.addAll(fbUser);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(intent, 0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}