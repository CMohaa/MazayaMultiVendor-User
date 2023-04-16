package com.mohaa.dokan.interfaces;


import com.mohaa.dokan.models.Order;

import java.io.Serializable;

public interface OnOrderClickListener extends Serializable {
    void onOrderClicked(Order contact, int position);
    void onOrderClicked(com.mohaa.dokan.models.wp.Order contact, int position);
}
