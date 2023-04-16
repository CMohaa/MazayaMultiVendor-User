package com.mohaa.dokan.manager.Response;


import com.google.gson.annotations.SerializedName;
import com.mohaa.dokan.models.wp.OrderEmpty;

public class OrderEmptyResponse {
    private boolean error;
    private String message;

    @SerializedName("order")
    private OrderEmpty order;

    public OrderEmptyResponse() {

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

    public OrderEmpty getOrder() {
        return order;
    }

    public void setOrder(OrderEmpty order) {
        this.order = order;
    }
}