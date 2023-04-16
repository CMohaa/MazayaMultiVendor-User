package com.mohaa.dokan.interfaces;




import com.mohaa.dokan.models.wp.ProductCategory;

import java.io.Serializable;


public interface OnCataClickListener extends Serializable {
    void onCategoryClicked(ProductCategory contact, int position);

}
