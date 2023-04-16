package com.mohaa.dokan.manager.ApiServices;




import com.mohaa.dokan.manager.Call.States;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface StatesAPIService {


    //getting Address
    @GET("states/{id}")
    Call<States> getStates(@Path("id") int id);



}