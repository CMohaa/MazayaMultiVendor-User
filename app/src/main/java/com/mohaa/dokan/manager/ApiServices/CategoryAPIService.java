package com.mohaa.dokan.manager.ApiServices;




import com.mohaa.dokan.models.wp.ProductCategory;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryAPIService {



    @GET("products/categories")
    Call<List<ProductCategory>> getWP3Categories(

    );


}