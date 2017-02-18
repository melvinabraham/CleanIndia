package com.mapps.seproject;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth firebaseAuth;
    Button bSignOut;
    Button bComposeMail;
    TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        welcome = (TextView) findViewById(R.id.tvWelcome);
        bSignOut = (Button) findViewById(R.id.bSignOut);
        bComposeMail = (Button) findViewById(R.id.bComposeMail);

        firebaseAuth = FirebaseAuth.getInstance();


        if(firebaseAuth.getCurrentUser() == null)   {                               // Incase the user hasnt logged in

            finish();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));         // Go back to login activity

        }

        FirebaseUser user = firebaseAuth.getCurrentUser();                                  // Get user
        welcome.setText("Welcome "+user.getEmail());                                        // Get Email


        bSignOut.setOnClickListener(this);                                                    // Start listener on Button
        bComposeMail.setOnClickListener(this);


    }


    public void composeEmail()  {

        Log.i("Sending Email","");
        String [] TO = {""};
        String [] CC = {""};

        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL,TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Your Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT,"Type your text here");

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
