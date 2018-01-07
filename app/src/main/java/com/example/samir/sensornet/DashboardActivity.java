package com.example.samir.sensornet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class DashboardActivity extends MenuActivity {

    private FirebaseDatabase fbDataBase;
    private DatabaseReference dbRef;

    Toolbar dbToolbar;
    String [] items = {};
    ArrayAdapter<String> listAdapter;
    private ArrayList<String> arrayList;
    private ListView deviceListView;


    @Override
    int getContentViewId() {
        return R.layout.activity_dashboard;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_dashboard;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fbDataBase = FirebaseDatabase.getInstance();
        dbRef = fbDataBase.getReference("DEV");

        dbToolbar = (Toolbar) findViewById(R.id.dashboardToolbar);
        dbToolbar.setTitle("Network Devices");

        arrayList = new ArrayList<>(Arrays.asList(items));
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);

        deviceListView = (ListView) findViewById(R.id.edListView);
        deviceListView.setAdapter(listAdapter);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                for(DataSnapshot dev : dataSnapshot.getChildren()) {
                    arrayList.add(dev.getKey());
                    listAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent deviceIntent = new Intent(DashboardActivity.this, DeviceActivity.class);
                deviceIntent.putExtra("Device", deviceListView.getItemAtPosition(i).toString());
                startActivity(deviceIntent);
            }
        });
    }

}
