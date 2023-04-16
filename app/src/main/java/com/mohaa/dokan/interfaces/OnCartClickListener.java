package com.mohaa.dokan.interfaces;


import com.mohaa.dokan.models.PendingProduct;
import com.mohaa.dokan.models.wp.Product;

import java.io.Serializable;

public interface OnCartClickListener extends Serializable {
    void onCartPendingClicked(PendingProduct contact, int position);
    void onCartClicked(Product contact, int position);
}
