package com.mapps.seproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    EditText email;
    EditText password;
    Button b;
    TextView loginText;

    ProgressDialog progress;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        firebaseAuth = FirebaseAuth.getInstance();


        if(firebaseAuth.getCurrentUser() != null)   {

            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));          // If user is already logged in


        }



        progress = new ProgressDialog(this);
        email = (EditText) findViewById(R.id.etRegEmail);
        password= (EditText) findViewById(R.id.etRegPassword);
        b= (Button) findViewById(R.id.bRegister);
        loginText = (TextView) findViewById(R.id.textLogin);



        b.setOnClickListener(this);
        loginText.setOnClickListener(this);







    }





    public void registerUser()  {

        String mail = email.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if(TextUtils.isEmpty(mail)) {


            Toast.makeText(this, "Enter E-Mail", Toast.LENGTH_SHORT).show();             // Incase Any of the fields are empty
            return;
        }
        if(TextUtils.isEmpty(pass)) {

            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;

        }



        progress.setMessage("Registering");                                                      // Progress Bar
        progress.show();


        try {

            firebaseAuth.createUserWithEmailAndPassword(mail, pass)                                    // Create New Entry
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {                                                 // On Successful Registration


                                Toast.makeText(RegisterActivity.this, "Registered Successfuly", Toast.LENGTH_SHORT).show();
                                progress.hide();


                            } else {


                                Toast.makeText(RegisterActivity.this, "Failed to Register", Toast.LENGTH_SHORT).show();
                                progress.hide();
                                //task.getException().getMessage();

                            }


                        }
                    });

        }

        catch (Exception e) {

                e.printStackTrace();
                Log.i("Registration Fail: ",e.toString());


        }



    }




    @Override
    public void onClick(View v) {


        if(v == b)                      // Register the current user
        {

            registerUser();

        }

        if(v == loginText){                 // Go to login activity

            finish();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));

        }



    }
}
