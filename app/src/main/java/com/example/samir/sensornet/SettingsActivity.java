package com.example.samir.sensornet;
import android.view.Menu;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsActivity extends MenuActivity {

    Toolbar settingsToolbar;
    String [] items = {"Pression", "Temperature"};
    ArrayAdapter<String> listAdapter;
    private ArrayList<String> arrayList;
    private GridView boundariesGridView;

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth fbAuth;
    private Button signOut;

    @Override
    int getContentViewId() {
        return R.layout.activity_settings;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_settings;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settingsToolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(settingsToolbar);

        arrayList = new ArrayList<>(Arrays.asList(items));
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);

        boundariesGridView = (GridView) findViewById(R.id.sensorBoundariesGrid);
        boundariesGridView.setAdapter(listAdapter);

        listAdapter.notifyDataSetChanged();

        //Getting Firebase auth instance
        fbAuth = FirebaseAuth.getInstance();
        signOut = (Button) findViewById(R.id.sign_out);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_log_out){
            signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    //sign out method
    public void signOut() {
        fbAuth.signOut();
        Intent intentLg = new Intent(SettingsActivity.this, LoginActivity.class);
        startActivity(intentLg);
    }
}
