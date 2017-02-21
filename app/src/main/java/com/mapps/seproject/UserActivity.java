package com.mapps.seproject;

import android.content.Intent;
import android.net.Uri;
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

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth firebaseAuth;
    Button bSignOut;
    Button bComposeMail;
    TextView welcome;
    Spinner dropdown;
    TextView emailText;
    int flag =0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        welcome = (TextView) findViewById(R.id.tvWelcome);
        bSignOut = (Button) findViewById(R.id.bSignOut);
        bComposeMail = (Button) findViewById(R.id.bComposeMail);
        emailText = (TextView) findViewById(R.id.tvEmailMessage);


        firebaseAuth = FirebaseAuth.getInstance();


        if(firebaseAuth.getCurrentUser() == null)   {                               // Incase the user hasnt logged in

            finish();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));         // Go back to login activity

        }

        FirebaseUser user = firebaseAuth.getCurrentUser();                                  // Get user
        welcome.setText("Welcome "+user.getEmail());                                        // Get Email

        dropdown = (Spinner) findViewById(R.id.spSelectCitiy);
        String [] items = new String[]{"Coimbatore","Chennai"};
        ArrayAdapter <String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,items);
        dropdown.setAdapter(arrayAdapter);






        bSignOut.setOnClickListener(this);                                                    // Start listener on Button
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

        try {

            startActivity(Intent.createChooser(emailIntent,"Send Mail..."));
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

        if(v == bSignOut) {

            firebaseAuth.signOut();                                                         // sign out
            finish();
            startActivity(new Intent(this,LoginActivity.class));                        // Go back to login activity

        }

        if(v == bComposeMail)   {


            Log.i("Clicked","Compose");
            composeEmail();


        }




    }


}
