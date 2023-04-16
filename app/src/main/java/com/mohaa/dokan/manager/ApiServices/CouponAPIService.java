package com.mohaa.dokan.manager.ApiServices;




import com.mohaa.dokan.models.wp.Coupon;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CouponAPIService {



    @GET("coupons")
    Call<ArrayList<Coupon>> getWP3Coupons(

    );


}