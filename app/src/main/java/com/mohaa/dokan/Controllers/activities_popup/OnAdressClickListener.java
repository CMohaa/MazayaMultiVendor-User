package com.mohaa.dokan.Controllers.activities_popup;


import com.mohaa.dokan.models.Address;
import com.mohaa.dokan.models.Addressg;

import java.io.Serializable;

public interface OnAdressClickListener extends Serializable {
    void onAdressClicked(Address contact, int position);
    void onAdressClicked(Addressg contact, int position);
}
