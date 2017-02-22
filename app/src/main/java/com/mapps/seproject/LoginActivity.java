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

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener{

    EditText email;
    EditText password;
    Button b;
    TextView registerText;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        email = (EditText) findViewById(R.id.etLoginEmail);
        password= (EditText) findViewById(R.id.etLoginPassword);
        b= (Button) findViewById(R.id.bLogin);
        registerText= (TextView) findViewById(R.id.textRegister);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null)   {

            finish();
            startActivity(new Intent(getApplicationContext(),UserActivity.class));              // If already logged in



        }

        b.setOnClickListener(this);                             // Onclick
        registerText.setOnClickListener(this);






    }


    public void LoginUser() {                                               // Check the login credentials


        String mail = email.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if(TextUtils.isEmpty(mail)) {
            // email empty

            Toast.makeText(this, "Enter E-Mail", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass)) {
            // password empty
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;

        }

        progressDialog.setMessage("Logging IN");
        progressDialog.show();

        try {

            firebaseAuth.signInWithEmailAndPassword(mail, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            if (task.isSuccessful()) {

                                // Successful Log In
                                finish();
                                startActivity(new Intent(getApplicationContext(), UserActivity.class));
                                progressDialog.hide();


                            } else {


                                progressDialog.hide();                                      // Unsuccessful Log In
                                Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                return;


                            }


                        }
                    });
        }

        catch (Exception e) {

            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();


        }


    }





    @Override
    public void onClick(View v) {


        if(v == b)                          // Verify Login Credentials
        {

            LoginUser();

        }

        if(v == registerText){              // Register New User

            finish();
            startActivity(new Intent(this,RegisterActivity.class));

        }





    }
}
