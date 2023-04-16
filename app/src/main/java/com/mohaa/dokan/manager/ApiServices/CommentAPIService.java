package com.mohaa.dokan.manager.ApiServices;




import com.mohaa.dokan.manager.Call.Comments;
import com.mohaa.dokan.manager.Response.CommentResponse;
import com.mohaa.dokan.models.wp.Order;
import com.mohaa.dokan.models.wp.Productreview;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommentAPIService {



    @GET("comments")
    Call<Comments> getComments();

    //creating new comment
    @FormUrlEncoded
    @POST("createcommentwp")
    Call<CommentResponse> createComment(
            @Field("usr_id") int usr_id,
            @Field("product_id") int product_id,
            @Field("review") String review,
            @Field("reviewer") String reviewer,
            @Field("reviewer_email") String reviewer_email,
            @Field("rating") int rating
    );
    @POST("products/reviews")
    Call<Productreview> createCommentDirectWPBody(
            @Body RequestBody params
    );
    //getting Comments
    @GET("comments/{id}{page_number}")
    Call<Comments> getComments(@Path("id") int id,
                               @Path("page_number") int page_number);

    //getting Comments
    @GET("allcomments/{id}{page_number}")
    Call<Comments> getAllComments(@Path("id") int id,
                                  @Path("page_number") int page_number);

    @GET("products/reviews")
    Call<ArrayList<Productreview>> getWP3Reviews(

    );

    //creating new Comment
    @FormUrlEncoded
    @POST("products/reviews")
    Call<CommentResponse> createComment(
            @Field("product_id") String product_id,
            @Field("review") String review,
            @Field("reviewer") String reviewer,
            @Field("reviewer_email") String reviewer_email,
            @Field("rating") String rating
    );
    //creating new Comment
    @POST("products/reviews")
    Call<CommentResponse> createComment(

    );


}