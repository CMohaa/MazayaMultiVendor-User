package com.mohaa.dokan.interfaces;


import com.mohaa.dokan.models.PendingProduct;

import java.io.Serializable;

public interface OnCartChangeListener extends Serializable {
    void onCartClicked(PendingProduct contact, int position);
}
