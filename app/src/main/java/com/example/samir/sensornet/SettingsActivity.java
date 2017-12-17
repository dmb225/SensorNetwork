package com.example.samir.sensornet;

import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends MenuActivity {

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

    //sign out method
    public void signOut() {
        fbAuth.signOut();
        Intent intentLg = new Intent(SettingsActivity.this, LoginActivity.class);
        startActivity(intentLg);
    }
}
