package com.mohaa.dokan.manager.ApiServices;




import com.mohaa.dokan.manager.Call.orderproducts;
import com.mohaa.dokan.manager.Response.OrderProductResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrderProductsAPIService {


    @GET("orderproducts")
    Call<orderproducts> getOrderdProducts();


    //creating new pendingproduct
    @FormUrlEncoded
    @POST("createorderproduct")
    Call<OrderProductResponse> createPendingProduct(
            @Field("product_id") int product_id,
            @Field("merchant_id") int merchant_id,
            @Field("order_number") long order_number,
            @Field("owner_id") int owner_id,
            @Field("quantity") int quantity,
            @Field("price") double price,
            @Field("total_price") double total_price,
            @Field("barcode") String barcode,
            @Field("created_at") long created_at

    );



    //getting orderproducts for user
    @GET("orderproductstracking/{order_number}")
    Call<orderproducts> getUserOrderProducts(
            @Path("order_number") long order_number
    );



}