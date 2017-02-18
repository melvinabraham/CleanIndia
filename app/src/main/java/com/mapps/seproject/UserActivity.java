package com.mapps.seproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth firebaseAuth;
    Button button;
    TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        welcome = (TextView) findViewById(R.id.tvWelcome);
        button = (Button) findViewById(R.id.bSignOut);
        firebaseAuth = FirebaseAuth.getInstance();


        if(firebaseAuth.getCurrentUser() == null)   {                               // Incase the user hasnt logged in

            finish();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));         // Go back to login activity

        }

        FirebaseUser user = firebaseAuth.getCurrentUser();                                  // Get user
        welcome.setText("Welcome "+user.getEmail());                                        // Get Email


        button.setOnClickListener(this);                                                    // Start listener on Button







    }

    @Override
    public void onClick(View v) {

        if(v == button) {

            firebaseAuth.signOut();                                                         // sign out
            finish();
            startActivity(new Intent(this,LoginActivity.class));                        // Go back to login activity

        }





    }
}
