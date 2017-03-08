package com.mapps.seproject;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class UserActivityFragment extends Fragment  {

    Button bSignOut;
    TextView welcome;
    FirebaseAuth firebaseAuth;
    Button bComposeScreen;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_activity, container, false);

        firebaseAuth = FirebaseAuth.getInstance();


        if(firebaseAuth.getCurrentUser() == null)   {                               // Incase the user hasnt logged in

            getActivity().finish();
            startActivity(new Intent(getActivity().getApplicationContext(),LoginActivity.class));         // Go back to login activity

        }

        FirebaseUser user = firebaseAuth.getCurrentUser();


        welcome = (TextView) view.findViewById(R.id.tvWelcome);
        welcome.setText("Welcome "+user.getEmail());                                        // Get Email'

        bSignOut = (Button) view.findViewById(R.id.bSignOut);
        bSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Log.i("Singout","Clicked");
                    firebaseAuth.signOut();
                    getActivity().finish();
                    startActivity(new Intent(getActivity().getApplicationContext(),LoginActivity.class));

            }
        });



        return view;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        getActivity().setTitle("Menu1");


    }





}
