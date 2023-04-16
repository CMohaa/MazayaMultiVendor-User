package com.mohaa.dokan.manager.Response;

import com.google.gson.annotations.SerializedName;
import com.mohaa.dokan.models.Orderproduct;


public class OrderProductResponse {
    @SerializedName("error")
    private Boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("orderProduct")
    private Orderproduct orderProduct;

    public OrderProductResponse(Boolean error, String message, Orderproduct orderProduct) {
        this.error = error;
        this.message = message;
        this.orderProduct = orderProduct;
    }

    public Boolean getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Orderproduct getOrderProduct() {
        return orderProduct;
    }
}