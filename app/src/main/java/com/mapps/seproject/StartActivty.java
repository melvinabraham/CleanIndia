package com.mapps.seproject;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivty extends AppCompatActivity implements View.OnClickListener {

    Button user;
    Button municipal;
    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_activty);
        checkAndroidVersion();
        user = (Button) findViewById(R.id.user_entry);
        municipal = (Button) findViewById(R.id.municipal_entry);
        user.setOnClickListener(this);
        municipal.setOnClickListener(this);
    }

    private void checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= 23) {

            checkPermission();
        } else {
            // write your logic here
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(this, android.Manifest.permission.CAMERA)   + ContextCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)  + ContextCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (this, android.Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (this, android.Manifest.permission.CAMERA) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (this, Manifest.permission.ACCESS_COARSE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                Snackbar.make(findViewById(android.R.id.content),
                        "Please Grant Permissions ",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestPermissions(
                                        new String[]{android.Manifest.permission
                                                .READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, Manifest.permission
                                                .ACCESS_FINE_LOCATION, Manifest.permission
                                                .ACCESS_COARSE_LOCATION},PERMISSIONS_MULTIPLE_REQUEST);
                            }
                        }).show();
            } else {
                requestPermissions(
                        new String[]{android.Manifest.permission
                                .READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, Manifest.permission
                                .ACCESS_FINE_LOCATION, Manifest.permission
                                .ACCESS_COARSE_LOCATION},PERMISSIONS_MULTIPLE_REQUEST);
            }
        } else {
            // write your logic code if permission already granted
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:
                if (grantResults.length > 0) {
                    boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;


                    if(cameraPermission && readExternalFile)
                    {
                        // write your logic here
                        Snackbar.make(findViewById(android.R.id.content),
                                "Please Grant Permissions to upload profile photo",
                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        requestPermissions(
                                                new String[]{android.Manifest.permission
                                                        .READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, Manifest.permission
                                                        .ACCESS_FINE_LOCATION, Manifest.permission
                                                        .ACCESS_COARSE_LOCATION},PERMISSIONS_MULTIPLE_REQUEST);
                                    }
                                }).show();
                    } else {

                    }
                }
                break;
        }
    }

    

    @Override
    public void onClick(View v) {
        if(v==user){
            startActivity(new Intent(StartActivty.this,LoginActivity.class));

        }
        if(v==municipal){
            startActivity(new Intent(StartActivty.this,MunicipalActivity.class));
        }
    }
}
