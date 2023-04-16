package com.mohaa.dokan.manager.ApiServices;

import com.mohaa.dokan.manager.Call.Users;
import com.mohaa.dokan.manager.Response.UserResponse;
import com.mohaa.dokan.models.wp.Customer;
import com.mohaa.dokan.models.wp.UserWp;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserAPIService {

    @FormUrlEncoded
    @POST("register")
    Call<UserResponse> createUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("gender") String gender,
            @Field("gcmtoken") String gcmtoken

    );
    @FormUrlEncoded
    @POST("register")
    Call<UserResponse> createUserWP(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("register")
    Call<UserResponse> createUserWPBody(
            @Query("rest_route") String url,
            @Body RequestBody params
    );
    @FormUrlEncoded
    @POST("login")
    Call<UserResponse> userLogin(
            @Field("email") String email,
            @Field("password") String password
    );
    @FormUrlEncoded
    @POST("getid")
    Call<UserResponse> usergetID(
            @Field("email") String email
    );
    @FormUrlEncoded
    @POST("token")
    Call<UserWp> userLoginWP(
            @Field("username") String username,
            @Field("password") String password
    );
    @FormUrlEncoded
    @POST("login")
    Call<UserResponse> userLoginAuth(
            @Field("user_email") String user_email,
            @Field("user_pass") String user_pass
    );

    @GET("users")
    Call<Users> getUsers();

    //updating user
    @FormUrlEncoded
    @POST("update/{id}")
    Call<UserResponse> updateUser(
            @Path("id") int id,
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("gender") String gender
    );

    //updating user gcmToken
    @FormUrlEncoded
    @POST("storegcmtoken/{id}")
    Call<UserResponse> updateGcmToken(
            @Path("id") int id,
            @Field("gcmtoken") String gcmtoken
    );

    @GET("users")
    Call<List<com.mohaa.dokan.models.wp.Users>> getCustomers();


    @GET("customers/{id}")
    Call<Customer> getWP3SingleCustomer(
            @Path("id") int id
    );

}