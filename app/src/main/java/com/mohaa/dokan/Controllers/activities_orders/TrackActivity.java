package com.mohaa.dokan.Controllers.activities_orders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mohaa.dokan.Auth.LoginActivity;
import com.mohaa.dokan.Controllers.BaseActivity;
import com.mohaa.dokan.Controllers.activities_products.IndividualProductActivity;
import com.mohaa.dokan.HomeActivity;
import com.mohaa.dokan.R;
import com.mohaa.dokan.Utils.OrderStatus;
import com.mohaa.dokan.Utils.Orientation;
import com.mohaa.dokan.Utils.ProductsUI;
import com.mohaa.dokan.Utils.helper.SharedPrefManager;
import com.mohaa.dokan.interfaces.OnTrackerClickListener;
import com.mohaa.dokan.manager.ApiServices.OrdersAPIService;
import com.mohaa.dokan.manager.ApiServices.ProductsAPIService;
import com.mohaa.dokan.manager.Response.OrderResponse;
import com.mohaa.dokan.manager.RetrofitApi;
import com.mohaa.dokan.models.TimeLineModel;
import com.mohaa.dokan.models.wp.LineItem;
import com.mohaa.dokan.models.wp.Order;
import com.mohaa.dokan.models.wp.Product;
import com.mohaa.dokan.networksync.CheckInternetConnection;
import com.mohaa.dokan.views.SingleOrderWPAdapter;
import com.mohaa.dokan.views.TimeLineAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackActivity extends BaseActivity implements OnTrackerClickListener {

    private static final String TAG = "TrackActivity";
    private RecyclerView mRecyclerView;
    private TimeLineAdapter mTimeLineAdapter;
    private List<TimeLineModel> mDataList = new ArrayList<>();
    private Orientation mOrientation;
    private boolean mWithLinePadding;
   // private String order_id;
   // private String total_cost;
    Toolbar toolbar;
    private com.mohaa.dokan.models.wp.Order order;
    private TextView total_amount , total_Amont_label ,  id_txt , id_label;


    private List<Product> orders_list;
    private StaggeredGridLayoutManager products_staggeredGridLayoutManager;
    private SingleOrderWPAdapter orderAdapter;
    private RecyclerView recList;
    private TextView cancel_order;
    private int userPage = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        // Set Toolbar
        toolbar = findViewById(R.id.toolbar);
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
        order = (Order) getIntent().getExtras().getSerializable(ProductsUI.BUNDLE_ORDERS_LIST);
        mOrientation = (Orientation) getIntent().getSerializableExtra(HomeActivity.EXTRA_ORIENTATION);
        mWithLinePadding = getIntent().getBooleanExtra(HomeActivity.EXTRA_WITH_LINE_PADDING, false);
       // order_id = getIntent().getStringExtra("blog_post_id");
       // total_cost = getIntent().getStringExtra(ProductsUI.BUNDLE_TOTAL_COST);
        cancel_order = findViewById(R.id.cancel_button);
       // Toast.makeText(this, "" + total_cost, Toast.LENGTH_SHORT).show();
        total_amount = findViewById(R.id.total_amount);
        total_amount.setText(String.valueOf(order.getTotal()));
        total_Amont_label = findViewById(R.id.total_Amont_label);

        //order_state = getIntent().getStringExtra(ProductsUI.BUNDLE_ORDER_STATE);

        id_txt = findViewById(R.id.id_txt);
        id_txt.setText(String.valueOf(order.getNumber()));
        id_label = findViewById(R.id.id_label);
        if(order.getId() == 0)
        {
            id_txt.setVisibility(View.GONE);
            id_label.setVisibility(View.GONE);
        }
        if(order.getTotal().equals(""))
        {
            total_amount.setVisibility(View.GONE);
            total_Amont_label.setVisibility(View.GONE);
        }
        /*
        if(order.getStatus().equals("pending"))
        {
            cancel_order.setVisibility(View.VISIBLE);
        }

         */

        //setTitle(mOrientation == Orientation.HORIZONTAL ? getResources().getString(R.string.horizontal_timeline) : getResources().getString(R.string.vertical_timeline));

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(getLinearLayoutManager());
        mRecyclerView.setHasFixedSize(true);

        initView();

        recList = (RecyclerView) findViewById(R.id.shoppingCartRecycleView);
        orders_list = new ArrayList<>();
        //Orderproduct address = new Orderproduct(1,1,1,1,5623562,1,15.3,25.25,52,56565656);
        //orders_list.add(address);
        orderAdapter = new SingleOrderWPAdapter(orders_list , this);
        products_staggeredGridLayoutManager = new StaggeredGridLayoutManager(1 , LinearLayoutManager.HORIZONTAL);
        recList.setLayoutManager(products_staggeredGridLayoutManager);
        recList.setAdapter(orderAdapter);

        orderAdapter.notifyDataSetChanged();

        load_products();
        getData();

        cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(TrackActivity.this , R.style.AlertDialogStyle);
                alert.setTitle(getResources().getString(R.string.do_you_want_to_remove_order));
                //alert.setMessage("Your currently orders will remove");
                // alert.setMessage("Message");

                alert.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        showProgressDialog();
                        Map<String, Object> jsonParams = new ArrayMap<>();

                        //put something inside the map, could be null
                        jsonParams.put("status", "cancelled");
                        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());
                        OrdersAPIService service = RetrofitApi.retrofitWriteWP3Body("orders/" + order.getNumber() ).create(OrdersAPIService.class);
                        Call<Order> callp = service.CancelOrderDirectWPBody(
                                Integer.valueOf(order.getNumber()),
                                body

                        );
                        callp.enqueue(new Callback<Order>() {
                            @Override
                            public void onResponse(Call<Order> call, Response<Order> response) {
                                if(response.body() != null) {
                                    hideProgressDialog();
                                    Intent loginIntent = new Intent(TrackActivity.this, HomeActivity.class);
                                    startActivity(loginIntent);
                                    finish();

                                }
                            }

                            @Override
                            public void onFailure(Call<Order> call, Throwable t) {
                                Log.d(TAG, "onFailure: Product "+ t.getLocalizedMessage());
                            }
                        });

                    }
                });

                alert.setNegativeButton(getResources().getString(R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        });

                alert.show();
            }
        });

    }
    public void getData(){
        try {
            //swipeRefreshLayout.setRefreshing(true);
            getAllOrders();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAllOrders() {

        for (int j = 0; j < order.getLineItems().size() ; j++) {

            if(order.getLineItems().get(j).getVariationId() != null &&order.getLineItems().get(j).getVariationId() != 0 )
            {
                int id =  order.getLineItems().get(j).getVariationId();
                ProductsAPIService service = RetrofitApi.retrofitReadWP3Product("products/" +id).create(ProductsAPIService.class);
                Call<com.mohaa.dokan.models.wp.Product> call = service.getWP3SingleProduct(id);
                // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
                call.enqueue(new Callback<com.mohaa.dokan.models.wp.Product>() {
                    @Override
                    public void onResponse(Call<com.mohaa.dokan.models.wp.Product> call, Response<com.mohaa.dokan.models.wp.Product> response) {
                        if(response.body() != null) {


                            orders_list.add(response.body());
                            //sponsored_list.addAll(response.body());
                            orderAdapter.notifyDataSetChanged();
                        }




                    }

                    @Override
                    public void onFailure(Call<com.mohaa.dokan.models.wp.Product> call, Throwable t) {
                        Log.d("Accessory", "onFailure: " + t.getLocalizedMessage());


                    }
                });
            }
            else {
                int id =  order.getLineItems().get(j).getProductId();
                int quantity = order.getLineItems().get(j).getQuantity();
                ProductsAPIService service = RetrofitApi.retrofitReadWP3Product("products/" +id).create(ProductsAPIService.class);
                Call<com.mohaa.dokan.models.wp.Product> call = service.getWP3SingleProduct(id);
                // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
                call.enqueue(new Callback<com.mohaa.dokan.models.wp.Product>() {
                    @Override
                    public void onResponse(Call<com.mohaa.dokan.models.wp.Product> call, Response<com.mohaa.dokan.models.wp.Product> response) {
                        if(response.body() != null) {

                            Product product = response.body();
                            product.setOrderd_quantity(quantity);

                            orders_list.add(product);
                            //sponsored_list.addAll(response.body());
                            orderAdapter.notifyDataSetChanged();
                        }




                    }

                    @Override
                    public void onFailure(Call<com.mohaa.dokan.models.wp.Product> call, Throwable t) {
                        Log.d("Accessory", "onFailure: " + t.getLocalizedMessage());


                    }
                });
            }
            Log.d("OrderProduct", "OrderProduct: " + order.getLineItems().get(j).getProductId());

        }
    }


    public void getAllOrders_old()
    {
        /*
        OrderProductsAPIService service = RetrofitApi.retrofitRead().create(OrderProductsAPIService.class);
        Call<orderproducts> call = service.getUserOrderProducts(order.getOrder_number());
        call.enqueue(new Callback<orderproducts>() {
            @Override
            public void onResponse(Call<orderproducts> call, Response<orderproducts> response) {

                if(response.body() != null)
                {
                    for (int j = 0; j <response.body().getOrderproducts().size() ; j++) {
                        //Log.d("upsale", "upsale: " + response.body().getOrderproducts().get(j));
                        int id =  response.body().getOrderproducts().get(j).getProductId();
                        int quantity = response.body().getOrderproducts().get(j).getQuantity();
                        ProductsAPIService service = RetrofitApi.retrofitReadWP3Product("products/" +id).create(ProductsAPIService.class);
                        Call<com.mohaa.mazaya.models.wp.Product> callz = service.getWP3SingleProduct(id);
                        // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
                        callz.enqueue(new Callback<Product>() {
                            @Override
                            public void onResponse(Call<Product> call, Response<Product> response) {
                                if(response.body() != null) {

                                    Product product = response.body();
                                    product.setOrderd_quantity(quantity);
                                    orders_list.add(product);
                                    orderAdapter.notifyDataSetChanged();
                                }

                            }

                            @Override
                            public void onFailure(Call<Product> call, Throwable t) {

                            }
                        });
                    }
                }
                else {

                }






            }

            @Override
            public void onFailure(Call<orderproducts> call, Throwable t) {

                Log.d("TrackActivity", "onFailure: " + t.getLocalizedMessage());
                Toast.makeText(TrackActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

         */

    }
    //Responsible For Adding the 3 tabs : Camera  , Home , Messages
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void load_products() {
        String pendingMessege = getResources().getString(R.string.pending_1);
        String confirmedMessage = getResources().getString(R.string.confirmed_1) + " ( " + order.getShipping().getState() + " , " + order.getShipping().getAddress1() + " ) " + getResources().getString(R.string.confirmed_2) + " , " + getResources().getString(R.string.customer_1);
        String PackedMessage = getResources().getString(R.string.packed_1) + " " + order.getBilling().getPhone() + " " + getResources().getString(R.string.order_cost) + " : " + order.getTotal() + " " + getResources().getString(R.string.no_delivery_cost);
        String DeliverdMessege = getResources().getString(R.string.deliverd_1) + " " + order.getShipping().getState() + "," + order.getShipping().getAddress1() + " " + getResources().getString(R.string.deliverd_2);
        String CancelMessege = getResources().getString(R.string.you_cancled_this_order);
        switch (order.getStatus())
        {
            case "pending":
            {

                mDataList.add(new TimeLineModel(pendingMessege, "2017-02-10 14:00", OrderStatus.ACTIVE));

                break;
            }
            case "processing":
            {

                mDataList.add(new TimeLineModel(confirmedMessage, "2017-02-10 14:30", OrderStatus.ACTIVE));
                mDataList.add(new TimeLineModel(pendingMessege, "2017-02-10 14:00", OrderStatus.COMPLETED));

                break;
            }
            case "on-hold":
            {

                mDataList.add(new TimeLineModel(PackedMessage, "2017-02-10 15:00", OrderStatus.ACTIVE));
                mDataList.add(new TimeLineModel(confirmedMessage, "2017-02-10 14:30", OrderStatus.COMPLETED));
                mDataList.add(new TimeLineModel(pendingMessege, "2017-02-10 14:00", OrderStatus.COMPLETED));

                break;
            }
            case "completed":
            {
                mDataList.add(new TimeLineModel(DeliverdMessege, "2017-02-11 08:00", OrderStatus.ACTIVE));
                mDataList.add(new TimeLineModel(PackedMessage, "2017-02-10 15:00", OrderStatus.COMPLETED));
                mDataList.add(new TimeLineModel(confirmedMessage, "2017-02-10 14:30", OrderStatus.COMPLETED));
                mDataList.add(new TimeLineModel(pendingMessege, "2017-02-10 14:00", OrderStatus.COMPLETED));

                break;
            }
            /*
            case 4:
            {
                mDataList.add(new TimeLineModel(DeliverdMessege, "2017-02-11 08:00", OrderStatus.COMPLETED));
                mDataList.add(new TimeLineModel(PackedMessage, "2017-02-10 15:00", OrderStatus.COMPLETED));
                mDataList.add(new TimeLineModel(confirmedMessage, "2017-02-10 14:30", OrderStatus.COMPLETED));
                mDataList.add(new TimeLineModel(pendingMessege, "2017-02-10 14:00", OrderStatus.COMPLETED));

                break;
            }

             */
            case "cancelled":
            {

                mDataList.add(new TimeLineModel(CancelMessege, "2017-02-10 14:00", OrderStatus.COMPLETED));

                break;
            }
            default:
            {
                mDataList.add(new TimeLineModel(DeliverdMessege, "2017-02-11 08:00", OrderStatus.INACTIVE));
                mDataList.add(new TimeLineModel(PackedMessage, "2017-02-10 15:00", OrderStatus.INACTIVE));
                mDataList.add(new TimeLineModel(confirmedMessage, "2017-02-10 14:30", OrderStatus.INACTIVE));
                mDataList.add(new TimeLineModel(pendingMessege, "2017-02-10 14:00", OrderStatus.ACTIVE));

            }
        }



    }

    private LinearLayoutManager getLinearLayoutManager() {
        if (mOrientation == Orientation.HORIZONTAL) {
            return new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        } else {
            return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        }
    }

    private void initView() {

        setDataListItems();
        mTimeLineAdapter = new TimeLineAdapter(mDataList, mOrientation, mWithLinePadding);
        mRecyclerView.setAdapter(mTimeLineAdapter);
        load();
    }
    private void load() {

    }
    private void setDataListItems(){
/*
        mDataList.add(new TimeLineModel("Item successfully delivered", "", OrderStatus.INACTIVE));
        mDataList.add(new TimeLineModel("Courier is out to delivery your order", "2017-02-12 08:00", OrderStatus.ACTIVE));
        mDataList.add(new TimeLineModel("Item has reached courier facility at New Delhi", "2017-02-11 21:00", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Item has been given to the courier", "2017-02-11 18:00", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Item is packed and will dispatch soon", "2017-02-11 09:30", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Order is being readied for dispatch", "2017-02-11 08:00", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Order processing initiated", "2017-02-10 15:00", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Order confirmed by seller", "2017-02-10 14:30", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Order placed successfully", "2017-02-10 14:00", OrderStatus.COMPLETED));

 */


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Menu
        switch (item.getItemId()) {
            //When home is clicked
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        if(mOrientation!=null)
            savedInstanceState.putSerializable(HomeActivity.EXTRA_ORIENTATION, mOrientation);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(HomeActivity.EXTRA_ORIENTATION)) {
                mOrientation = (Orientation) savedInstanceState.getSerializable(HomeActivity.EXTRA_ORIENTATION);
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }


    @Override
    public void onTrackerClicked(Product contact, int position, int parentID) {
        if(parentID != 0) {
            showProgressDialog();
            ProductsAPIService service = RetrofitApi.retrofitReadWP3Product("products/" +parentID).create(ProductsAPIService.class);
            Call<com.mohaa.dokan.models.wp.Product> call = service.getWP3SingleProduct(parentID);
            // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
            call.enqueue(new Callback<com.mohaa.dokan.models.wp.Product>() {
                @Override
                public void onResponse(Call<com.mohaa.dokan.models.wp.Product> call, Response<com.mohaa.dokan.models.wp.Product> response) {
                    if(response.body() != null) {


                        Intent loginIntent = new Intent(TrackActivity.this, IndividualProductActivity.class);
                        loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST, (Serializable) response.body());
                        //loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_IMAGE, contact.getFeaturedSrc());
                        loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_ID, response.body().getId());
                        loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_TYPE, response.body().getType());
                        hideProgressDialog();
                        startActivity(loginIntent);
                    }




                }

                @Override
                public void onFailure(Call<com.mohaa.dokan.models.wp.Product> call, Throwable t) {
                    Log.d("Accessory", "onFailure: " + t.getLocalizedMessage());


                }
            });

        }
        else
        {
            Intent loginIntent = new Intent(TrackActivity.this, IndividualProductActivity.class);
            loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST, (Serializable) contact);
            //loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_IMAGE, contact.getFeaturedSrc());
            loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_ID, contact.getId());
            loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_TYPE, contact.getType());
            startActivity(loginIntent);
        }
    }

    @Override
    public void onTrackerClicked(LineItem contact, int position) {

    }
}