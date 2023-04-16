package com.mohaa.dokan.manager.Call;




import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mohaa.dokan.models.Addressg;

import java.util.List;


public class Addresssg {

    @SerializedName("address")
    @Expose
    private List<Addressg> address = null;

    public List<Addressg> getAddress() {
        return address;
    }

    public void setAddress(List<Addressg> address) {
        this.address = address;
    }


}