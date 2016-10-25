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

}

