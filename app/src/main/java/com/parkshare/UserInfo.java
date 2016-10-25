package com.parkshare;

import java.io.Serializable;

/**
 * Created by Shopon on 8/26/16.
 */
public class UserInfo implements Serializable{
    private static UserInfo userInfo = new UserInfo();

    private String NAME;
    private String USER_EMAIL;
    private double SEARCH_DISTANCE = 3;
    private boolean IS_ALL = true;
    private String CURRENT_LOCATION;

    private UserInfo(){

    }

    public static UserInfo getInstance(){
        return userInfo;
    }

    public void setName(String name){
        NAME = name;
    }
    public void setUserEmail(String email){
        USER_EMAIL = email;
    }
    public void setIsAll(boolean isAll){
        IS_ALL = isAll;
    }
    public void setSearchDistance(double distance){
        SEARCH_DISTANCE = distance;
    }

    public String getName(){
        return NAME;
    }
    public String getUserEmail(){
        return USER_EMAIL;
    }
    public boolean getIsAll(){
        return IS_ALL;
    }
    public double getSearchDistance(){
        return SEARCH_DISTANCE;
    }
}
