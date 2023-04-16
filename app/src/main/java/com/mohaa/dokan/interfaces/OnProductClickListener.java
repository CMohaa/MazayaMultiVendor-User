package com.mohaa.dokan.interfaces;





import com.mohaa.dokan.models.wp.Product;

import java.io.Serializable;

public interface OnProductClickListener extends Serializable {
    void onProductClicked(Product contact, int position);

}
