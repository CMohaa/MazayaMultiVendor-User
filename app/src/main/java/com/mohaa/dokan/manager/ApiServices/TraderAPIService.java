package com.mohaa.dokan.manager.ApiServices;




import com.mohaa.dokan.manager.Call.Traders;
import com.mohaa.dokan.manager.Response.TraderResponse;
import com.mohaa.dokan.models.wp.Vendor;
import com.mohaa.dokan.models.wp.VendorComment;
import com.mohaa.dokan.models.wp.VendorEmpty;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TraderAPIService {


    @GET("traders")
    Call<Traders> getTraders();

    //creating new trader
    @FormUrlEncoded
    @POST("createtrader")
    Call<TraderResponse> createTrader(
            @Field("admin_id") int admin_id,
            @Field("admin_name") String admin_name,
            @Field("merchant_name") String merchant_name,
            @Field("merchant_desc") String merchant_desc,
            @Field("thumb_image") String thumb_image,
            @Field("location") String location,
            @Field("type") String type,
            @Field("rate") int rate,
            @Field("ratecount") int ratecount,
            @Field("merchant_credit") double merchant_credit,
            @Field("country_code") int country_code,
            @Field("count") int count

    );
    //getting a trader
    @GET("traders/{id}")
    Call<Traders> getTraders(@Path("id") int id);

    //getting a trader
    @GET("stores/{id}")
    Call<Vendor> getStore(@Path("id") int id);

    //getting a trader
    @GET("stores/{id}")
    Call<VendorEmpty> getStoreEmpty(@Path("id") int id);

    //getting a trader reviews
    @GET("stores/{id}/reviews")
    Call<List<VendorComment>> getStoreReviews(@Path("id") int id);

    //getting a trader products
    @GET("stores/{id}/products")
    Call<List<com.mohaa.dokan.models.wp.Product>> getStoreProducts(@Path("id") int id,
                                                                   @Query("page") String page);








}