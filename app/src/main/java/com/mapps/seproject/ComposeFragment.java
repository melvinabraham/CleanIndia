package com.mapps.seproject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by kishore on 3/7/2017.
 */

public class ComposeFragment extends Fragment implements View.OnClickListener{

    int flag =0;
    Button bComposeMail;
    Spinner dropdown;
    TextView emailText;

    private StorageReference mStorageRef;


    Button bAddImage;
    View view;
    private Uri imageUri;
    Button upload;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_MAIL = 2;

    private Button b_get;
    private com.mapps.seproject.TrackGPS gps;
    double longitude;
    double latitude;
    String city;
    String postalCode;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_compose, container, false);

        bComposeMail = (Button) view.findViewById(R.id.bComposeMail);
        emailText = (TextView) view.findViewById(R.id.tvEmailMessage);
        bAddImage = (Button) view.findViewById(R.id.bAddImage);
        b_get = (Button) view.findViewById(R.id.button2);
        upload = (Button) view.findViewById(R.id.upload_image);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        bAddImage.setOnClickListener(this);
        bComposeMail.setOnClickListener(this);
        b_get.setOnClickListener(this);
        upload.setOnClickListener(this);
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

        String mailText = emailText.getText().toString();


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

            Log.i("Finished","");
        }

        catch (android.content.ActivityNotFoundException ex)    {

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

        if(v == bAddImage)  {

            Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(cameraIntent,RESULT_LOAD_IMAGE);

        }

        //Upload Image
        if(v == upload){
            uploadFile();
        }

        //Location
	    if(v == b_get){
            gps = new TrackGPS(getActivity());


            if(gps.canGetLocation()){


                longitude = gps.getLongitude();
                latitude = gps .getLatitude();
                city = gps.getCity();
                postalCode = gps.getPostalCode();
                Toast.makeText(getActivity(),"Longitude:"+Double.toString(longitude)+"\nLatitude:"+Double.toString(latitude)+"\nCity:"+city+"\nPostal:"+postalCode,Toast.LENGTH_SHORT).show();
            }
            else
            {

                gps.showSettingsAlert();
            }
        }




    }






    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {

            imageUri = data.getData();
            Toast.makeText(getActivity().getBaseContext(), "Image Added. Press Compose to transfer to Mail Window", Toast.LENGTH_SHORT).show();

        }




    }


    //Upload image
    private void uploadFile() {
        //if there is a file to upload
        if (imageUri != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference riversRef = mStorageRef.child("images/pic.jpg");
            riversRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getActivity().getBaseContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getActivity().getBaseContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("VisibleForTests")
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage

                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
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
