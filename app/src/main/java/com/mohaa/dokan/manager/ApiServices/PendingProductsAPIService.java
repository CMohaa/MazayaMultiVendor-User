package com.mohaa.dokan.manager.ApiServices;




import com.mohaa.dokan.manager.Call.PendingProducts;
import com.mohaa.dokan.manager.Response.PendingProductResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PendingProductsAPIService {


    @GET("pendingproducts")
    Call<PendingProducts> getProducts();

    /*
        private int id;
        private int product_id;
        private int cat_id;
        private int cat_parent_id;
        private int cat_gparent_id;
        private int merchant_id;
        private String merchant_name;
        private String product_name;
        private String product_shortname;
        private String product_desc;
        private int quantity;
        private double price;
        private double total_price;
        private long barcode;
        private int type;
        private String thumb_image;
        private double discount;
        private int department;
        private int company;
        private int pack;
        private int status;
        private long created_at;
     */
    //creating new pendingproduct
    @FormUrlEncoded
    @POST("creatependingproduct")
    Call<PendingProductResponse> createPendingProduct(
            @Field("product_id") int product_id,
            @Field("cat_id") int cat_id,
            @Field("cat_parent_id") int cat_parent_id,
            @Field("cat_gparent_id") int cat_gparent_id,
            @Field("merchant_id") int merchant_id,
            @Field("merchant_name") String merchant_name,
            @Field("product_name") String product_name,
            @Field("product_shortname") String product_shortname,
            @Field("product_desc") String product_desc,
            @Field("quantity") int quantity,
            @Field("price") double price,
            @Field("total_price") double total_price,
            @Field("barcode") long barcode,
            @Field("type") int type,
            @Field("thumb_image") String thumb_image,
            @Field("discount") double discount,
            @Field("department") int department,
            @Field("company") int company,
            @Field("pack") int pack,
            @Field("status") int status,
            @Field("created_at") long created_at

    );



    //getting pendingproducts for trader
    @GET("pendingproductstrader/{merchant_id}{page_number}{sort_type}")
    Call<PendingProducts> getPendingProducts(
            @Path("merchant_id") int merchant_id,
            @Path("page_number") int page_number,
            @Path("sort_type") int sort_type
    );


}