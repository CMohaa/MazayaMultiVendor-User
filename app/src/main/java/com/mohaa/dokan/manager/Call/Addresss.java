package com.mohaa.dokan.manager.Call;




import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mohaa.dokan.models.Address;

import java.util.List;


public class Addresss {

    @SerializedName("address")
    @Expose
    private List<Address> address = null;

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }


}