package com.mohaa.dokan.manager.ApiServices;




import com.mohaa.dokan.manager.Call.Products;
import com.mohaa.dokan.manager.Response.ProductResponse;
import com.mohaa.dokan.models.wp.Product;
import com.mohaa.dokan.models.wp.VariationProduct;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductsAPIService {


    @GET("products")
    Call<Products> getProducts();

    //creating new product
    @FormUrlEncoded
    @POST("createproduct")
    Call<ProductResponse> createProduct(
            @Field("cat_id") String cat_id,
            @Field("cat_parent_id") String cat_parent_id,
            @Field("cat_gparent_id") String cat_gparent_id,
            @Field("merchant_id") int merchant_id,
            @Field("merchant_name") String merchant_name,
            @Field("product_name") String product_name,
            @Field("product_shortname") String product_shortname,
            @Field("product_desc") String product_desc,
            @Field("quantity") int quantity,
            @Field("price") double price,
            @Field("barcode") long barcode,
            @Field("rate") int rate,
            @Field("ratecount") int ratecount,
            @Field("type") String type,
            @Field("thumb_image") String thumb_image,
            @Field("discount") double discount,
            @Field("department") String department,
            @Field("company") String company,
            @Field("pack") String pack,
            @Field("status") String status,
            @Field("created_at") long created_at,
            @Field("view_count") int view_count,
            @Field("order_count") int order_count,
            @Field("share_count") int share_count,
            @Field("wish_count") int wish_count,
            @Field("sponsored") int sponsored

    );

    //getting products for trader
    @GET("products/{merchant_id}")
    Call<Products> getProducts(
            @Path("merchant_id") int merchant_id
    );

    //updating productviewCount
    @FormUrlEncoded
    @POST("productviewcount/{id}")
    Call<ProductResponse> updateProductView(
            @Path("id") int id,
            @Field("view_count") int view_count

    );

    //updating productviewCount
    @FormUrlEncoded
    @POST("productwishcount/{id}")
    Call<ProductResponse> updateProductwish(
            @Path("id") int id,
            @Field("wish_count") int wish_count

    );
    //updating productviewCount
    @FormUrlEncoded
    @POST("productordercount/{id}")
    Call<ProductResponse> updateProductorder(
            @Path("id") int id,
            @Field("order_count") int order_count

    );
    //updating productviewCount
    @FormUrlEncoded
    @POST("productsharecount/{id}")
    Call<ProductResponse> updateProductshare(
            @Path("id") int id,
            @Field("share_count") int share_count

    );
    //updating productRate
    @FormUrlEncoded
    @POST("productrate/{id}")
    Call<ProductResponse> updateRate(
            @Path("id") int id,
            @Field("merchant_id") int merchant_id,
            @Field("rate") int rate,
            @Field("ratecount") int ratecount

    );

    //getting products for home
    @GET("productshome/{page_number}{sort_type}")
    Call<Products> getProducts(
            @Path("page_number") int page_number,
            @Path("sort_type") int sort_typess
    );

    //getting products for trader
    @GET("productstrader/{merchant_id}{page_number}{sort_type}")
    Call<Products> getProducts(
            @Path("merchant_id") int merchant_id,
            @Path("page_number") int page_number,
            @Path("sort_type") int sort_type
    );

    //getting products for trader
    @GET("productssearch/{product_name}{page_number}{sort_type}")
    Call<Products> getProducts(
            @Path("product_name") String product_name,
            @Path("page_number") int page_number,
            @Path("sort_type") int sort_type
    );
    //getting products alike
    @GET("productlike/{cat_parent_id}{page_number}{sort_type}")
    Call<Products> getProductsAlike(
            @Path("cat_parent_id") int cat_parent_id,
            @Path("page_number") int page_number,
            @Path("sort_type") int sort_type
    );
    //getting products alike
    @GET("productmain/{cat_gparent_id}{page_number}{sort_type}")
    Call<Products> getProductsMain(
            @Path("cat_gparent_id") int cat_gparent_id,
            @Path("page_number") int page_number,
            @Path("sort_type") int sort_type
    );

    //getting products alike
    @GET("productsub/{cat_parent_id}{page_number}{sort_type}")
    Call<Products> getProductsSub(
            @Path("cat_parent_id") int cat_parent_id,
            @Path("page_number") int page_number,
            @Path("sort_type") int sort_type
    );
    //getting products alike
    @GET("productgroup/{cat_id}{page_number}{sort_type}")
    Call<Products> getProductsGroup(
            @Path("cat_id") int cat_id,
            @Path("page_number") int page_number,
            @Path("sort_type") int sort_type
    );


    @GET("product")
    Call<List<com.mohaa.dokan.models.wp.Product>> getWPProducts();

    @GET("products")
    Call<com.mohaa.dokan.models.wp.call.Products> getWC3Products(

    );
    @GET("products")
    Call<ArrayList<Product>> getWP3Products(

    );

    @GET("products/{id}")
    Call<com.mohaa.dokan.models.wp.Product> getWP3SingleProduct(
            @Path("id") int id
    );
    @GET("products/{id}/variations")
    Call<List<VariationProduct>> getWP3SingleProductVarintaion(
            @Path("id") int id
    );
    @GET("products")
    Call<com.mohaa.dokan.models.wp.call.Products> getWP3Products(

            @Query("page") String page
    );
}