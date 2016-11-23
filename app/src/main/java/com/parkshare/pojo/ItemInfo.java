package com.parkshare.pojo;

import java.io.Serializable;

/**
 * Created by Shopon on 8/26/16.
 */
public class ItemInfo implements Serializable{
    private String name = "";
    private int id = -1;
    private int addressId = -1;
    private String specialInstruction = "";
    private Integer image = -1;
    private String itemType = "";
    private float latitude = 0;
    private float longitude = 0;
    private double distance = 0;

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }

    public void setAddressId(int addressId){
        this.addressId = addressId;
    }
    public int getAddressId(){
        return addressId;
    }

    public void setSpecialInstruction(String specialInstruction){
        this.specialInstruction = specialInstruction;
    }
    public String getSpecialInstruction(){
        return specialInstruction;
    }

    public void setImage(Integer image){
        this.image = image;
    }
    public Integer getImage(){
        return this.image;
    }

    public void setItemType(String itemType){
        this.itemType = itemType;
    }
    public String getItemType(){
        return this.itemType;
    }

    public void setLatitude(float latitude){
        this.latitude = latitude;
    }
    public float getLatitude(){
        return this.latitude;
    }

    public void setLongitude(float longitude){
        this.longitude = longitude;
    }
    public float getLongitude(){
        return this.longitude;
    }

    public void setDistance(double distance){
        this.distance = distance;
    }
    public double getDistance(){
        return this.distance;
    }


}

