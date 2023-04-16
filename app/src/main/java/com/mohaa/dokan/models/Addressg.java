package com.mohaa.dokan.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Addressg implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("owner_email")
    @Expose
    private String owner_email;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("government")
    @Expose
    private String government;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("state")
    @Expose
    private Integer state;

    public Addressg(String owner_email, String name, String country, String government, String address, String mobile, Integer state) {
        this.owner_email = owner_email;
        this.name = name;
        this.country = country;
        this.government = government;
        this.address = address;
        this.mobile = mobile;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String  getUserId() {
        return owner_email;
    }

    public void setUserId(String userId) {
        this.owner_email = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGovernment() {
        return government;
    }

    public void setGovernment(String government) {
        this.government = government;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

}
