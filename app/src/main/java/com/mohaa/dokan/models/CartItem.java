package com.mohaa.dokan.models;

import java.util.UUID;


public class CartItem {

    public String id = UUID.randomUUID().toString();
    public PendingProduct product;
    public int quantity = 0;

    public CartItem(String id, PendingProduct product, int quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PendingProduct getProduct() {
        return product;
    }

    public void setProduct(PendingProduct product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}