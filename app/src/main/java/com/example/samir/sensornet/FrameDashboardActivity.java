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

public class FrameDashboardActivity extends MenuActivity {

    private FirebaseDatabase fbDataBase;
    private DatabaseReference dbRef;

    Toolbar dbToolbar;
    String [] items = {};
    ArrayAdapter<String> listAdapter;
    private ArrayList<String> arrayList;
    private ListView deviceListView;


    @Override
    int getContentViewId() {
        return R.layout.activity_dashboard_frame;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_frame;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fbDataBase = FirebaseDatabase.getInstance();
        dbRef = fbDataBase.getReference("FRAMES");

        dbToolbar = (Toolbar) findViewById(R.id.dashboardToolbar2);
        dbToolbar.setTitle("Last Frames");

        arrayList = new ArrayList<>(Arrays.asList(items));
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);

        deviceListView = (ListView) findViewById(R.id.edListView2);
        deviceListView.setAdapter(listAdapter);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
                Intent deviceIntent = new Intent(FrameDashboardActivity.this, FrameActivity.class);
                deviceIntent.putExtra("Frame", deviceListView.getItemAtPosition(i).toString());
                startActivity(deviceIntent);
            }
        });
    }

}
