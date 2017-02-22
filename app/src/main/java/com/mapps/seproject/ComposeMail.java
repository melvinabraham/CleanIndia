package com.mapps.seproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ComposeMail extends AppCompatActivity implements View.OnClickListener{


    int flag =0;
    Button bComposeMail;
    Spinner dropdown;
    TextView emailText;

    Button bAddImage;
    Uri imageUri;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_MAIL = 2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_mail);




        bComposeMail = (Button) findViewById(R.id.bComposeMail);
        emailText = (TextView) findViewById(R.id.tvEmailMessage);
        bAddImage = (Button) findViewById(R.id.bAddImage);

                           // Get user
        dropdown = (Spinner) findViewById(R.id.spSelectCitiy);
        String [] items = new String[]{"Coimbatore","Chennai"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,items);
        dropdown.setAdapter(arrayAdapter);





        bAddImage.setOnClickListener(this);
        bComposeMail.setOnClickListener(this);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position)  {

                    case  0:
                        // setCityCoimbatore();
                        Log.i("City: ","Coimbatore");
                        flag =0;
                        break;

                    case 1:
                        // setCityChennai();
                        Log.i("City:","Chennai");
                        flag = 1;
                        break;

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






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

            Toast.makeText(getApplicationContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();

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




    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data!=null)   {

            imageUri = data.getData();
            Toast.makeText(this, "Image Added. Press Compose to transfer to Mail Window", Toast.LENGTH_SHORT).show();

        }

        if(requestCode == RESULT_MAIL)  {

            new AlertDialog.Builder(this)
                    .setTitle("Select action")
                    .setMessage("Do you want to go back?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            startActivity(new Intent(getApplicationContext(),UserActivity.class));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    }).show();

        }


    }












}
