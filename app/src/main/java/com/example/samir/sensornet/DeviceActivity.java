package com.example.samir.sensornet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class DeviceActivity extends AppCompatActivity {

    private FirebaseDatabase fbDataBase;
    private DatabaseReference DeviceDBRef;

    Toolbar mdevToolbar;
    String deviceId = "";
    String [] items = {};
    ArrayAdapter<String> listAdapter;
    private ArrayList<String> arrayList;
    private ListView deviceListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        mdevToolbar = (Toolbar) findViewById(R.id.deviceToolbar);

        Bundle deviceBundle = getIntent().getExtras();
        if(deviceBundle != null) {
            deviceId = deviceBundle.getString("Device");

            fbDataBase = FirebaseDatabase.getInstance();
            DeviceDBRef = fbDataBase.getReference("DEV").child(deviceId);

            mdevToolbar.setTitle("Device " + deviceId);

            arrayList = new ArrayList<>(Arrays.asList(items));
            listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);

            deviceListView = (ListView) findViewById(R.id.edAttributesListView);
            deviceListView.setAdapter(listAdapter);

            DeviceDBRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot dev : dataSnapshot.getChildren()) {
                        arrayList.add(dev.getKey() + ": " + dev.getValue(String.class));
                        listAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }



    }
}
