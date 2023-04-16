package com.mohaa.dokan.manager.ApiServices;

import com.mohaa.dokan.manager.Response.CustomerResponse;
import com.mohaa.dokan.models.wp.Customer;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CustomerAPIService {

    @GET("customers/{id}")
    Call<com.mohaa.dokan.models.wp.call.Customers> getWP3Customer(
            @Path("id") int id
    );

    @FormUrlEncoded
    @POST("updatecustomerbillingwp")
    Call<CustomerResponse> UpdateBillingWP(
            @Field("customer_id") String customer_id,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("company") String company,
            @Field("address_1") String address_1,
            @Field("address_2") String address_2,
            @Field("city") String city,
            @Field("state") String state,
            @Field("postcode") String postcode,
            @Field("country") String country,
            @Field("email") String email,
            @Field("phone") String phone


    );
    @FormUrlEncoded
    @POST("updatecustomerbillingwp")
    Call<CustomerResponse> UpdateBillingWP(
            @Field("customer_id") String customer_id,
            @Body Customer.Billing billing


    );


}