package com.mohaa.dokan.manager.ApiServices;




import com.mohaa.dokan.manager.Call.VariantsProduct;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface VariantsProductAPIService {


    //getting VariantsProduct
    @GET("variants_pdt/{id}")
    Call<VariantsProduct> getVariantsProduct(@Path("id") int id);



}