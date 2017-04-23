package com.mapps.seproject;

/**
 * Created by kishore on 4/24/2017.
 */

public class Feed {

    int id;
    String name,image,status,profilePic,timeStamp,url;

    public Feed(){}

    public Feed(int id,String name,String image,String status,String profilePic,String timeStamp,String url){
        this.id = id;
        this.name = name;
        this.image = image;
        this.status = status;
        this.profilePic = profilePic;
        this.timeStamp = timeStamp;
        this.url = url;

    }

}
