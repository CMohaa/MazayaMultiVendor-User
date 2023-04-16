package com.mohaa.dokan.interfaces;




import com.mohaa.dokan.models.Trader;


import java.io.Serializable;

public interface OnTraderClickListener extends Serializable {
    void onTraderClicked(Trader contact, int position);
}
