package com.mohaa.dokan.models.wp.call;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mohaa.dokan.models.wp.Customer;

import java.util.List;

public class Customers {

    @SerializedName("customer")
    @Expose
    private Customer customer;

    @SerializedName("customers")
    @Expose
    private List<Customer> customers = null;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
}