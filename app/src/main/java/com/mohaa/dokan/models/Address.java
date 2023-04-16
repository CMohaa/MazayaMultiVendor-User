package com.mohaa.dokan.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Address implements Serializable {

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
    private Integer country;
    @SerializedName("government")
    @Expose
    private Integer government;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("state")
    @Expose
    private Integer state;

    public Address(String owner_email, String name, Integer country, Integer government, String address, String mobile, Integer state) {
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

    public Integer getCountry() {
        return country;
    }

    public void setCountry(Integer country) {
        this.country = country;
    }

    public Integer getGovernment() {
        return government;
    }

    public void setGovernment(Integer government) {
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
