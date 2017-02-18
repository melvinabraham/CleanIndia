package com.mapps.seproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener{

    EditText email;
    EditText password;
    Button b;
    TextView registerText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        email = (EditText) findViewById(R.id.etLoginEmail);
        password= (EditText) findViewById(R.id.etLoginPassword);
        b= (Button) findViewById(R.id.bLogin);
        registerText= (TextView) findViewById(R.id.textRegister);



        b.setOnClickListener(this);                             // Onclick
        registerText.setOnClickListener(this);






    }

    @Override
    public void onClick(View v) {



    }
}
