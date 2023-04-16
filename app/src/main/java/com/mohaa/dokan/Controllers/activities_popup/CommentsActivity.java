package com.mohaa.dokan.Controllers.activities_popup;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mohaa.dokan.Auth.LoginActivity;
import com.mohaa.dokan.Controllers.BaseActivity;
import com.mohaa.dokan.R;
import com.mohaa.dokan.Utils.ProductsUI;
import com.mohaa.dokan.Utils.helper.SharedPrefManager;
import com.mohaa.dokan.Utils.reviewratings.RatingReviews;
import com.mohaa.dokan.manager.ApiServices.CommentAPIService;
import com.mohaa.dokan.manager.Response.CommentResponse;
import com.mohaa.dokan.manager.RetrofitApi;
import com.mohaa.dokan.models.Comment;
import com.mohaa.dokan.models.wp.Productreview;
import com.mohaa.dokan.networksync.CheckInternetConnection;
import com.mohaa.dokan.views.CommentsRecyclerAdapter;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsActivity extends BaseActivity {

    //=========== Comments =============
    private RecyclerView commentsRecyclerView;
    private CommentsRecyclerAdapter commentsRecyclerAdapter;
    private List<Productreview> commentsList;
    private EditText commentEditText;
    private Button sendButton;
    private RatingBar ratingbar;
    private com.mohaa.dokan.models.wp.Product products;
    private int userPage = 1;
    private RatingReviews ratingReviews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Hide Title
        TextView titleToolbar = findViewById(R.id.appname);
        titleToolbar.setVisibility(View.GONE);

        // Back Button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
        products = (com.mohaa.dokan.models.wp.Product) getIntent().getExtras().getSerializable(ProductsUI.BUNDLE_PRODUCTS_LIST);
        init();
        initListeners();


    }




    private void init() {


        ratingbar=(RatingBar)findViewById(R.id.ratingBar);
        commentEditText = findViewById(R.id.commentEditText);
        sendButton = findViewById(R.id.sendButton);
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
    }
    /**
     * method to initialize the listeners
     */
    private void initListeners() {

        // ExpandableListView on child click listener
        ratingbar.setRating(5);
        updateRecycleView();
        loadComments(userPage);
        commentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sendButton.setEnabled(charSequence.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ratingbar.getRating()!= 0 && commentEditText.getText()!= null)
                {
                    sendComment();
                }

            }
        });

    }
    private void sendComment() {

        showProgressDialog();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        int owner_id = SharedPrefManager.getInstance(this).getUser().getID();
        String owner_email = SharedPrefManager.getInstance(this).getUser().getUser_email();
        String owner_name = SharedPrefManager.getInstance(this).getUser().getDisplay_name();

        Map<String, Object> jsonParams = new ArrayMap<>();

        //put something inside the map, could be null
        jsonParams.put("product_id", products.getId());
        jsonParams.put("review", commentEditText.getText().toString());
        jsonParams.put("reviewer", owner_name);
        jsonParams.put("reviewer_email", owner_email);
        jsonParams.put("rating", (int)ratingbar.getRating());
        // jsonParams.put("address_1", selected_address.getAddress());
        // jsonParams.put("state", state);
        //jsonParams.put("phone", selected_address.getMobile());

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());
        CommentAPIService service = RetrofitApi.retrofitWriteWP3Body("products/reviews").create(CommentAPIService.class);
        Log.d("Comments", "sendComment: beofre " + owner_name);
        Log.d("Comments", "sendComment: token " + SharedPrefManager.getInstance(this).getUser().getToken());
        Comment trader = new Comment(owner_id,products.getId(),commentEditText.getText().toString() ,owner_name , owner_email ,(int)ratingbar.getRating());

        // Log.d("helloo",encodedImage +"   >>"+imgname);
        //Log.d("imggggg","   >>"+imgname);

        //defining the call

        Call<Productreview> call = service.createCommentDirectWPBody(
               body


        );
        Log.d("Comments", "sendComment: id " + trader.getUsr_id());
        Log.d("Comments", "sendComment: pdt " + trader.getProduct_id());
        Log.d("Comments", "sendComment: review " +  trader.getReview());
        Log.d("Comments", "sendComment: reviewer " + trader.getReviewer());
        Log.d("Comments", "sendComment: email " + trader.getReviewer_email());
        Log.d("Comments", "sendComment: rate " + trader.getRate());
        call.enqueue(new Callback<Productreview>() {
            @Override
            public void onResponse(Call<Productreview> call, Response<Productreview> response) {
                //IncRate(products.getId() , (int)ratingbar.getRating());
                hideProgressDialog();
                if(response.body() != null)
                {
                    onBackPressed();
                }
                else {
                    Toast.makeText(CommentsActivity.this, "Failed To Create New Comment", Toast.LENGTH_SHORT).show();
                }

                //Toast.makeText(CommentsActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Productreview> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(CommentsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void sendCommentold() {



        showProgressDialog();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        int owner_id = SharedPrefManager.getInstance(this).getUser().getID();
        String owner_name = SharedPrefManager.getInstance(this).getUser().getDisplay_name();

        CommentAPIService service = RetrofitApi.retrofitWriteWP3Comments("products/reviews").create(CommentAPIService.class);

        //Defining the trader object as we need to pass it with the call
       // Productreview trader = new Productreview(products.getId() , "Mohamed El Sayed", "mohaa.coder@yahoo.com" ,commentEditText.getText().toString() , (int)ratingbar.getRating());

        // Log.d("helloo",encodedImage +"   >>"+imgname);
        //Log.d("imggggg","   >>"+imgname);

        //defining the call
        /*
        Call<CommentResponse> call = service.createComment(
                String.valueOf(trader.getProductId()),
                trader.getReview(),
                trader.getReviewer(),
                trader.getReviewerEmail(),
                String.valueOf(trader.getRating())


        );

         */
        Call<CommentResponse> call = service.createComment();
        call.enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                //IncRate(products.getId() , (int)ratingbar.getRating());

                hideProgressDialog();
               // Toast.makeText(CommentsActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                hideProgressDialog();
                Log.d("CommentsActivity", "onFailure: " + t.getLocalizedMessage());
               // Toast.makeText(CommentsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });




    }

    private void updateRecycleView() {
        //RecyclerView Firebase List
        commentsList = new ArrayList<>();

        commentsRecyclerAdapter = new CommentsRecyclerAdapter(commentsList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this , LinearLayoutManager.VERTICAL, false);
        commentsRecyclerView.setHasFixedSize(true);
        commentsRecyclerView.setLayoutManager(mLayoutManager);
        /*
        commentsRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                userPage++;
                loadComments(userPage);
            }
        });

         */
        commentsRecyclerView.setAdapter(commentsRecyclerAdapter);
        //mUserDatabase.keepSynced(true);
    }
    private void loadComments(int pageno)
    {
        CommentAPIService service = RetrofitApi.retrofitReadWP3ProductsReviews("products/reviews" , String.valueOf(products.getId()), String.valueOf(pageno)).create(CommentAPIService.class);
        Call<ArrayList<Productreview>> call = service.getWP3Reviews();
        // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
        call.enqueue(new Callback<ArrayList<Productreview>>() {
            @Override
            public void onResponse(Call<ArrayList<Productreview>> call, Response<ArrayList<Productreview>> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);

                // Toast.makeText(ProductsActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                if(response.body() != null) {
                    commentsList.addAll(response.body());
                    commentsRecyclerAdapter.notifyDataSetChanged();
                }
                //shimmerFrameLayout.setVisibility(View.GONE);
               // shimmerFrameLayout.stopShimmer();
                // Log.d("onFailure:",  "ProductName:"+response.body().getProducts().get(1).getTitle());





            }

            @Override
            public void onFailure(Call<ArrayList<Productreview>> call, Throwable t) {



            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new CheckInternetConnection(this).checkConnection();
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
           // finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    public void IncRate(int product_id , int rate)
    {
        /*
        ProductsAPIService service = RetrofitApi.retrofitRead().create(ProductsAPIService.class);

        Product user = new Product(product_id ,products.getMerchant_id() , rate , 1);

        Call<ProductResponse> call = service.updateRate(
                user.getId(),
                user.getMerchant_id(),
                user.getRate(),
                user.getRatecount()

        );

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                onBackPressed();
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {

            }
        });

         */
    }
}
