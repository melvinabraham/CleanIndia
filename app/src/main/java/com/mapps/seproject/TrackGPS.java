package com.mapps.seproject;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;


import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;

/**
 * Created by Kishore on 8/8/2016.
 */

public class TrackGPS extends Service implements LocationListener {

    private final Context mContext;


    boolean checkGPS = false;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    final String data = firebaseAuth.getCurrentUser().getEmail();

    boolean checkNetwork = false;

    boolean canGetLocation = false;

    Location loc;


    String UserEmail = null;
    final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");

    private com.mapps.seproject.TrackGPS gps;
    double longitude;
    double latitude;
    String city;
    String postalCode;




    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;


    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    protected LocationManager locationManager;

    public TrackGPS(Context mContext) {
        this.mContext = mContext;
        getLocation();
    }

        private Location getLocation() {

            try {
                locationManager = (LocationManager) mContext
                        .getSystemService(LOCATION_SERVICE);

                // getting GPS status
                checkGPS = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);



                // getting network status
                checkNetwork = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!checkGPS && !checkNetwork) {
                    Toast.makeText(mContext, "No Service Provider Available", Toast.LENGTH_SHORT).show();
                } else {
                    this.canGetLocation = true;
                    // First get location from Network Provider
                    if (checkNetwork) {
                        //Toast.makeText(mContext, "Network", Toast.LENGTH_SHORT).show();

                        try {
                            locationManager.requestLocationUpdates(
                                    LocationManager.NETWORK_PROVIDER,1000,0, this);
                            Log.d("Network", "Network");
                            if (locationManager != null) {
                                loc = locationManager
                                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                            }

                            if (loc != null) {
                                Toast.makeText(mContext,"Gps enabled",Toast.LENGTH_SHORT).show();

                                latitude = loc.getLatitude();
                                longitude = loc.getLongitude();
                                Geocoder geocoder= new Geocoder(mContext,Locale.getDefault());
                                List<Address> addresses;

                                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                              //  String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                city = addresses.get(0).getLocality();
                                //String state = addresses.get(0).getAdminArea();
                                //String country = addresses.get(0).getCountryName();
                                postalCode = addresses.get(0).getPostalCode();
                                //String knownName = addresses.get(0).getFeatureName();
                            }
                        }
                        catch(SecurityException e){

                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (checkGPS) {
                    if (loc == null) {
                        try {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,1000,0, this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                loc = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (loc != null) {
                                    latitude = loc.getLatitude();
                                    longitude = loc.getLongitude();
                                    Geocoder geocoder= new Geocoder(mContext,Locale.getDefault());
                                    List<Address> addresses;

                                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                    //  String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                    city = addresses.get(0).getLocality();
                                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                                                UserEmail = snapshot.child("email").toString().split("value =")[1].split(" ")[1].replaceAll("\\s+","");
                                                if(UserEmail.equals(data))  {

                                                    snapshot.getRef().child("location").setValue(city);
                                                    break;
                                                }

                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    //String state = addresses.get(0).getAdminArea();
                                    //String country = addresses.get(0).getCountryName();
                                    postalCode = addresses.get(0).getPostalCode();
                                    //String knownName = addresses.get(0).getFeatureName();
                                }
                            }

                        } catch (SecurityException e) {

                        }
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return loc;
        }

    public double getLongitude() {
        if (loc != null) {
            longitude = loc.getLongitude();
        }
        return longitude;
    }

    public double getLatitude() {
        if (loc != null) {
            latitude = loc.getLatitude();
        }
        return latitude;
    }

    public String getCity(){
        return this.city;
    }

    public String getPostalCode(){
        return this.postalCode;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);


        alertDialog.setTitle("GPS Not Enabled");

        alertDialog.setMessage("Do you wants to turn On GPS");


        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });


        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        alertDialog.show();
    }


    public void stopUsingGPS() {
        if (locationManager != null) {

            locationManager.removeUpdates(TrackGPS.this);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}