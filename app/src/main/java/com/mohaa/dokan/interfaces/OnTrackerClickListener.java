package com.mohaa.dokan.interfaces;


import com.mohaa.dokan.models.wp.LineItem;
import com.mohaa.dokan.models.wp.Product;

import java.io.Serializable;

public interface OnTrackerClickListener extends Serializable {
    void onTrackerClicked(Product contact, int position , int parentID);
    void onTrackerClicked(LineItem contact, int position);
}
