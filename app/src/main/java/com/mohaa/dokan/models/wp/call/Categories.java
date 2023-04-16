package com.mohaa.dokan.models.wp.call;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mohaa.dokan.models.wp.ProductCategory;

public class Categories {

    @SerializedName("product_categories")
    @Expose
    private List<ProductCategory> productCategories = null;

    public List<ProductCategory> getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(List<ProductCategory> productCategories) {
        this.productCategories = productCategories;
    }

}