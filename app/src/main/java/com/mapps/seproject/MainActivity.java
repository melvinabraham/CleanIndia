package com.mapps.seproject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.IdRes;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {

    Fragment user_activity;
    Fragment camera_activity;
    Fragment compose_activity;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FirebaseAuth firebaseAuth;
    EditText getname;
    int flag = 0;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");
    String userId;
    String UserEmail = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        final String data = firebaseAuth.getCurrentUser().getEmail();

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");

        /*
        userId = mDatabase.push().getKey();
        User user = new User(data);
        mDatabase.child(userId).setValue(user);

*/
            getname = RegisterActivity.name;
           mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.e("Count" , ""+dataSnapshot.getChildrenCount());

                for(DataSnapshot idSnapshot : dataSnapshot.getChildren())   {

                    //Log.e("Email:",""+idSnapshot.child("email").toString().split("value =")[1].split(" ")[1]);
                    UserEmail = idSnapshot.child("email").toString().split("value =")[1].split(" ")[1].replaceAll("\\s+","");

                    Log.e("Email",UserEmail);
                    Log.e("Data",data);


                    if(data.equals(UserEmail))
                    {

                     Log.e("E","Already Exists");
                     flag =1;

                    }


                }

                if(flag == 0)   {

                    userId = mDatabase.push().getKey();
                    User user = new User(data,getname.getText().toString()); 
                    mDatabase.child(userId).setValue(user);
                    Log.e("E","new");
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed" + databaseError.getMessage());

            }
        });




        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.user_activity) {

                    user_activity = new UserActivityFragment();
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container,user_activity);
                    fragmentTransaction.commit();



                }
                if(tabId == R.id.camera_activity){
                    camera_activity = new CameraFragment();
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container,camera_activity);
                    fragmentTransaction.commit();

                }

                if(tabId == R.id.compose_mail){
                    compose_activity = new ComposeFragment();
                     fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container,compose_activity);
                    fragmentTransaction.commit();

                }
            }
        });



    }





}
