package com.mohaa.dokan.models.wp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserWp implements Serializable {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("user_nicename")
    @Expose
    private String userNicename;
    @SerializedName("user_display_name")
    @Expose
    private String userDisplayName;

    public UserWp(String token, String userEmail, String userNicename, String userDisplayName) {
        this.token = token;
        this.userEmail = userEmail;
        this.userNicename = userNicename;
        this.userDisplayName = userDisplayName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserNicename() {
        return userNicename;
    }

    public void setUserNicename(String userNicename) {
        this.userNicename = userNicename;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

}