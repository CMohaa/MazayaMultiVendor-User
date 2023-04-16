package com.mohaa.dokan.manager.ApiServices;




import com.mohaa.dokan.manager.Call.Variants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface VariantsAPIService {


    @GET("variants")
    Call<Variants> getVariants();

    //getting Variants
    @GET("variants/{id}")
    Call<Variants> getVariants(@Path("id") int id);



}