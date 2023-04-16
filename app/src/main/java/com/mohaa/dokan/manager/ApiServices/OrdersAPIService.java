package com.mohaa.dokan.manager.ApiServices;




import com.mohaa.dokan.manager.Call.Orders;
import com.mohaa.dokan.manager.Response.OrderEmptyResponse;
import com.mohaa.dokan.manager.Response.OrderResponse;
import com.mohaa.dokan.models.wp.LineItem;
import com.mohaa.dokan.models.wp.Order;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface OrdersAPIService {


    @GET("orders")
    Call<Orders> getOrders();


    /*
     private long order_number;
    private int owner_id;
    private String owner_name;
    private String message;
    private int country;
    private int government;
    private String address;
    private String mobile;
    private int state;
    private long created_at;
    private int count;
    private double total_cost;
     */
    //creating new order
    @FormUrlEncoded
    @POST("createorder")
    Call<OrderResponse> createOrder(
            @Field("order_number") long order_number,
            @Field("owner_id") int owner_id,
            @Field("message") String message,
            @Field("country") int country,
            @Field("government") int government,
            @Field("address") String address,
            @Field("mobile") String mobile,
            @Field("state") int state,
            @Field("created_at") long created_at,
            @Field("count") int count,
            @Field("total_cost") double total_cost

    );
    @POST("createordert")
    Call<OrderResponse> createOrderWPBody(
            @Body RequestBody params
    );
    @POST("createordertc")
    Call<OrderResponse> createOrderCWPBody(
            @Body RequestBody params
    );
    @POST("orders")
    Call<Order> createOrderDirectWPBody(
            @Body RequestBody params
    );
    @FormUrlEncoded
    @POST("createorderwp")
    Call<OrderResponse> createOrderWP(
            @Field("owner_id") int owner_id



    );
    @FormUrlEncoded
    @POST("createorderwp")
    Call<OrderEmptyResponse> createOrderWPEmpty(
            @Field("owner_id") int owner_id


    );
    @FormUrlEncoded
    @POST("createorderwp")
    Call<OrderEmptyResponse> createOrderWPEmptyD(
            @Field("owner_id") int owner_id,
            @Body List<LineItem> orders


    );

    @FormUrlEncoded
    @POST("updateproductorderwp")
    Call<OrderResponse> UpdateOrderProductWP(
            @Field("order_id") String order_id,
            @Field("product_id") int product_id,
            @Field("quantity") int quantity


    );
    @FormUrlEncoded
    @POST("cancelorderwp")
    Call<OrderResponse> CancelOrderWP(
            @Field("order_id") String order_id



    );
    @PUT("orders/{id}")
    Call<Order> CancelOrderDirectWPBody(
            @Path("id") int id,
            @Body RequestBody params
    );
    //getting ordersuser for user
    @GET("ordersuser/{owner_id}{page_number}{sort_type}")
    Call<Orders> getUserOrders(
            @Path("owner_id") int owner_id,
            @Path("page_number") int page_number,
            @Path("sort_type") int sort_type
    );

    //getting orderstrader for trader
    @GET("orderstrader/{merchant_id}{page_number}{sort_type}")
    Call<Orders> getOrders(
            @Path("merchant_id") int merchant_id,
            @Path("page_number") int page_number,
            @Path("sort_type") int sort_type
    );

    @GET("orders")
    Call<ArrayList<Order>> getWP3Orders(

    );



}