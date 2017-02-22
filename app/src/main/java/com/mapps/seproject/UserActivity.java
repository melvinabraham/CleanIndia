package com.mapps.seproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    Button bSignOut;
    TextView welcome;
    FirebaseAuth firebaseAuth;
    Button bComposeScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        firebaseAuth = FirebaseAuth.getInstance();


        if(firebaseAuth.getCurrentUser() == null)   {                               // Incase the user hasnt logged in

            finish();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));         // Go back to login activity

        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        welcome = (TextView) findViewById(R.id.tvWelcome);
        welcome.setText("Welcome "+user.getEmail());                                        // Get Email'

        bSignOut = (Button) findViewById(R.id.bSignOut);
        bSignOut.setOnClickListener(this);

        bComposeScreen = (Button) findViewById(R.id.bGotoCompose);
        bComposeScreen.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        if(v == bSignOut)   {


            firebaseAuth.signOut();                                                         // sign out
            finish();
            startActivity(new Intent(this,LoginActivity.class));                        // Go back to login activitys


        }

        if(v == bComposeScreen) {

            finish();
            startActivity(new Intent(this,ComposeMail.class));

        }
    }
}
