package com.mohaa.dokan.interfaces;

import com.mohaa.dokan.models.Ad;

import java.io.Serializable;

public interface OnAdsClickListener extends Serializable {
    void onAdsClicked(Ad contact, int position);
}
