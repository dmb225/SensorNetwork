package com.example.samir.sensornet;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
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
    static int NotificationID = 0;
    Toolbar mframeToolbar;
    String frameId = "";
    String [] items = {};
    ArrayAdapter<String> listAdapter;
    private ArrayList<String> arrayList;
    private ListView frameListView;

    protected void CheckBoundaries(Map<String,String> m){
        //Collecting the sensor's type and value
        String name = m.get("type");
        Float val = Float.parseFloat(m.get("value"));

        //Those constants should be moved two a @boundaries/bounds file
        Float pbound = 1.013f;
        Float tbound = 620.013f;

        //Checking boundaries
        if(name.equals("Pression") && Float.compare(val,pbound) > 0) {
                NotificationID++;
                Notify(name, Float.toString(val));
        }
        else if(name.equals("Temp") && Float.compare(val,tbound) > 0) {
                NotificationID++;
                Notify(name, Float.toString(val));
        }
    }

    protected void Notify(String type, String val){
        // The id of the channel.
        String CHANNEL_ID = "my_channel_01";
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_frame_black_24dp)
                        .setContentTitle("Boundary Notification")
                        .setContentText("sensor : " + type + ", " + val);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, HomeActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your app to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(HomeActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // mNotificationId is a unique integer your app uses to identify the
        // notification. For example, to cancel the notification, you can pass its ID
        // number to NotificationManager.cancel().
        mNotificationManager.notify(NotificationID, mBuilder.build());
    }
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
                        /*if(dev.getValue() instanceof ArrayList){
                            ArrayList<Map<String,String>> list = (ArrayList<Map<String,String>>) dev.getValue();
                            String PrettyList = new String();
                            for(Map<String,String> m : list){
                                Set<String> keys = m.keySet();
                                CheckBoundaries(m);
                                for(String key : keys){
                                    //PrettyList += "\n" + key + ": \t" + m.get(key);
                                }//Other way of printing the map content
                                PrettyList += '\n' + Arrays.toString(m.entrySet().toArray());
                            }
                            arrayList.add(dev.getKey() + ": " + PrettyList);
                            listAdapter.notifyDataSetChanged();
                        }*/
                        if(dev.getValue() instanceof Map){
                            Map<String, Float> list = (Map<String,Float>) dev.getValue();
                            Set<String> keys = list.keySet();
                            String PrettyList = new String();
                            for(String key : keys){
                                PrettyList += "\n" + key + ": \t" + String.valueOf(list.get(key)) + "\n";
                            }
                            arrayList.add(PrettyList);
                            listAdapter.notifyDataSetChanged();
                        }
                        else{
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
