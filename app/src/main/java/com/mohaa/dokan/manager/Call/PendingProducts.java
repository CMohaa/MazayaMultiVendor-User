package com.mohaa.dokan.manager.Call;




import com.mohaa.dokan.models.PendingProduct;

import java.util.ArrayList;


public class PendingProducts {

    private ArrayList<PendingProduct> pendingProducts;

    public PendingProducts() {

    }

    public ArrayList<PendingProduct> getPendingProducts() {
        return pendingProducts;
    }

    public void setPendingProducts(ArrayList<PendingProduct> pendingProducts) {
        this.pendingProducts = pendingProducts;
    }
}