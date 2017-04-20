package com.mapps.seproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivty extends AppCompatActivity implements View.OnClickListener {

    Button user;
    Button municipal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_activty);
        user = (Button) findViewById(R.id.user_entry);
        municipal = (Button) findViewById(R.id.municipal_entry);
        user.setOnClickListener(this);
        municipal.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==user){
            startActivity(new Intent(StartActivty.this,LoginActivity.class));

        }
        if(v==municipal){
            startActivity(new Intent(StartActivty.this,MunicipalActivity.class));
        }
    }
}
