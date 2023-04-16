package com.mohaa.dokan.manager.Response;


import com.google.gson.annotations.SerializedName;
import com.mohaa.dokan.models.wp.Customer;

public class CustomerResponse {
    private boolean error;
    private String message;

    @SerializedName("customer")
    private Customer customer;

    public CustomerResponse() {

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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}