package com.mohaa.dokan.manager.Response;


import com.google.gson.annotations.SerializedName;
import com.mohaa.dokan.models.wp.Order;

public class OrderResponse {
    private boolean error;
    private String message;

    @SerializedName("order")
    private Order order;

    public OrderResponse() {

    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}