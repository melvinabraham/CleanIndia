package com.mapps.seproject;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * Created by kishore on 3/7/2017.
 */

public class ComposeFragment extends Fragment implements View.OnClickListener{

    int flag =0;
    Button bComposeMail;
    Spinner dropdown;
    TextView emailText;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    final String data = firebaseAuth.getCurrentUser().getEmail();

    String UserEmail = null;
    private Uri imageUri = CameraFragment.images;


    //Button bAddImage;
    View view;
    Button upload;
    //private static int RESULT_LOAD_IMAGE = 1;



    private com.mapps.seproject.TrackGPS gps;
    double longitude;
    double latitude;
    String city;
    String postalCode;
    final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_compose, container, false);

        bComposeMail = (Button) view.findViewById(R.id.bComposeMail);
        emailText = (TextView) view.findViewById(R.id.tvEmailMessage);
        //b_get = (Button) view.findViewById(R.id.button2);



        bComposeMail.setOnClickListener(this);
        //b_get.setOnClickListener(this);
        return view;
    }


    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        getActivity().setTitle("Menu1");

    }

    public void composeEmail()  {


        Log.i("Sending Email","");
        String[] TO;
        if (flag == 0) {

            TO = new String[]{"commr.coimbatore@tn.gov.in"};

        }
        else {
            TO = new String[]{"mayor@chennaicorporation.gov.in"};
        }
        String [] CC = {""};

        final String mailText = emailText.getText().toString();


        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        if ( flag == 0) {
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Regarding cleanliness in Coimbatore");
        }
        else {
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Regarding cleanliness in Chennai");

        }
        emailIntent.putExtra(Intent.EXTRA_TEXT,mailText);
        emailIntent.setType("application/image");
        emailIntent.putExtra(Intent.EXTRA_STREAM,imageUri);

        try {

            startActivityForResult(Intent.createChooser(emailIntent,"Send Mail..."),2);

            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                        UserEmail = snapshot.child("email").toString().split("value =")[1].split(" ")[1].replaceAll("\\s+","");
                        if(UserEmail.equals(data))  {

                            snapshot.getRef().child("complaint").setValue(mailText);
                            break;
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });





            Log.i("Finished","");
        }

        catch (android.content.ActivityNotFoundException e)    {

            Toast.makeText(getActivity().getBaseContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();

        }
        catch (Exception e) {

            e.printStackTrace();
        }




    }
    @Override
    public void onClick(View v) {


        if(v == bComposeMail)   {


            Log.i("Clicked","Compose");
            composeEmail();


        }








    }








    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        try {
            dropdown = (Spinner) view.findViewById(R.id.spSelectCitiy);
            String[] items = new String[]{"Coimbatore", "Chennai"};
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, items);

            dropdown.setAdapter(arrayAdapter);
            System.out.println("Spinner Initialised");



            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    switch (position) {

                        case 0:
                            // setCityCoimbatore();
                            Log.i("City: ", "Coimbatore");
                            flag = 0;
                            break;

                        case 1:
                            // setCityChennai();
                            Log.i("City:", "Chennai");
                            flag = 1;
                            break;

                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        }

        catch (Exception e) {
            Log.i("Exception",e.toString());
        }


    }




}
