package com.mohaa.dokan.manager.Call;




import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mohaa.dokan.models.Orderproduct;

import java.util.List;


public class orderproducts {


    @SerializedName("orderproducts")
    @Expose
    private List<Orderproduct> orderproducts = null;

    public List<Orderproduct> getOrderproducts() {
        return orderproducts;
    }

    public void setOrderproducts(List<Orderproduct> orderproducts) {
        this.orderproducts = orderproducts;
    }

}