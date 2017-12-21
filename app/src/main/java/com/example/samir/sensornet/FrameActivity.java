package com.example.samir.sensornet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Map;
import java.util.Set;

public class FrameActivity extends AppCompatActivity {

    private FirebaseDatabase fbDataBase;
    private DatabaseReference FrameDBRef;

    Toolbar mframeToolbar;
    String frameId = "";
    String [] items = {};
    ArrayAdapter<String> listAdapter;
    private ArrayList<String> arrayList;
    private ListView frameListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);

        mframeToolbar = (Toolbar) findViewById(R.id.Toolbar2);

        Bundle deviceBundle = getIntent().getExtras();
        if(deviceBundle != null) {
            frameId = deviceBundle.getString("Frame");

            fbDataBase = FirebaseDatabase.getInstance();
            FrameDBRef = fbDataBase.getReference("FRAMES").child(frameId);

            mframeToolbar.setTitle(frameId);

            arrayList = new ArrayList<>(Arrays.asList(items));
            listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
            arrayList.add("Current frame description");
            frameListView = (ListView) findViewById(R.id.edAttributesListView2);
            frameListView.setAdapter(listAdapter);

            FrameDBRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    arrayList.clear();
                    for(DataSnapshot dev : dataSnapshot.getChildren()) {
                        if(dev.getValue() instanceof ArrayList){
                            ArrayList<Map<String,String>> list = (ArrayList<Map<String,String>>) dev.getValue();
                            String PrettyList = new String();
                            for(Map<String,String> m : list){
                                Set<String> keys = m.keySet();
                                for(String key : keys){
                                    //PrettyList += "\n" + key + ": \t" + m.get(key);
                                }//Other way of printing the map content
                                PrettyList += '\n' + Arrays.toString(m.entrySet().toArray());
                            }
                            arrayList.add(dev.getKey() + ": " + PrettyList);
                            listAdapter.notifyDataSetChanged();
                        } else{
                            arrayList.add(dev.getKey() + ": " + dev.getValue(String.class));
                            listAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
