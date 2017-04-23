package com.mapps.seproject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.IdRes;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    int flag = 0;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference databaseReference;

    String userId;
    String UserEmail = null;
    static String location = "Not Updated";
    static String complaint = "Not Updated";

    String UID="";

    int flags = 0;

    public static int ids;
    String image="";
    //   String TimeStamp = "";
    String URL = "";
    String profilePic="http://api.androidhive.info/feed/img/ar.jpg";
    String status = "";
    String UserName = "";
    FirebaseUser firebaseUser;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.sign_out:
                firebaseAuth.signOut();
                startActivity(new Intent(this,StartActivty.class));

        }

        return true;
        }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        final String data = firebaseAuth.getCurrentUser().getEmail();
        firebaseUser = firebaseAuth.getCurrentUser();

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");

        databaseReference = FirebaseDatabase.getInstance().getReference("feed");
        /*
        userId = mDatabase.push().getKey();
        User user = new User(data);
        mDatabase.child(userId).setValue(user);

*/

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
                    User user = new User(data,RegisterActivity.name.getText().toString(),location,complaint);
                    mDatabase.child(userId).setValue(user);
                    Log.e("E","new");
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The re  ad failed" + databaseError.getMessage());

            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                //       UserName = getname.getText().toString();

                if(flags == 0)   {
                    ids = (int) dataSnapshot.child("feed").getChildrenCount();

                    Long tsLong = System.currentTimeMillis()/1000;
                    UID = firebaseUser.getUid();
                    final String TimeStamp = tsLong.toString();




                    Feed feed = new Feed(ids,UserName,image,status,profilePic,TimeStamp,URL);
                    databaseReference.child(UID).child("feed").child(String.valueOf(ids)).setValue(feed);
                    flags = 1;

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
