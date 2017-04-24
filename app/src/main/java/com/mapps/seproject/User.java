package com.mapps.seproject;

/**
 * Created by melvin on 21/4/17.
 */
public class User   {


    public String name;
    public String email;
    public String location;
    public String complaint;


    public User()   {


    }

    public User(String email,String name,String location,String complaint)   {

        this.email = email;
        this.name = name;
        this.location = location;
        this.complaint = complaint;

    }

}

