package com.mohaa.dokan.Controllers.activities_orders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mohaa.dokan.Auth.LoginActivity;
import com.mohaa.dokan.Controllers.BaseActivity;
import com.mohaa.dokan.R;
import com.mohaa.dokan.Utils.EditTextRegular;
import com.mohaa.dokan.Utils.MultiLineRadioGroup;
import com.mohaa.dokan.Utils.ProductsUI;
import com.mohaa.dokan.Utils.Toasty.Toasty;
import com.mohaa.dokan.Utils.helper.SharedPrefManager;
import com.mohaa.dokan.interfaces.OnCartClickListener;
import com.mohaa.dokan.manager.ApiServices.CouponAPIService;
import com.mohaa.dokan.manager.ApiServices.OrdersAPIService;
import com.mohaa.dokan.manager.OrdersBase;
import com.mohaa.dokan.manager.Response.OrderResponse;
import com.mohaa.dokan.manager.RetrofitApi;
import com.mohaa.dokan.models.Addressg;
import com.mohaa.dokan.models.PendingProduct;
import com.mohaa.dokan.models.wp.Coupon;

import com.mohaa.dokan.models.wp.Order;
import com.mohaa.dokan.models.wp.Product;
import com.mohaa.dokan.networksync.CheckInternetConnection;
import com.mohaa.dokan.views.SingleCartAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends BaseActivity implements OnCartClickListener {

    private static final String TAG = "OrderDetails";

    private List<PendingProduct> products_list;
    private TextView no_of_items;
    private TextView total_amount , shippingfee , codfee , total_cost , vat;


    private TextView Order_Send;
    private LinearLayout order_card;
    MultiLineRadioGroup main_activity_multi_line_radio_group;
    private ProgressDialog mLoginProgress;
    private TextView orderPnumber;
    private TextView orderLocation;
    Toolbar toolbar;
    private Addressg selected_address;


    private List<PendingProduct> orders_list;
    private StaggeredGridLayoutManager products_staggeredGridLayoutManager;
    private SingleCartAdapter orderAdapter;
    private RecyclerView recList;


    //
    private Boolean promo_codeState = false;
    private String promo_code ,promo_desc;
    private Double promo_disc;
    private EditTextRegular promo_name;

    private TextView promo_apply , discount_label;
    private LinearLayout vailed_code;

    private String id;

    double total;
    int total_count;


    double vat_fee ;
    double shipping_fee;
    double cod_fee;
   // double discount;
    double total_c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

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

        selected_address = (Addressg) getIntent().getExtras().getSerializable(ProductsUI.BUNDLE_ADDRESS_LIST);
        total_amount = findViewById(R.id.total_amount);
        no_of_items = findViewById(R.id.no_of_items);
        promo_name = findViewById(R.id.promo_name);
        promo_apply = findViewById(R.id.promo_apply);
        discount_label = findViewById(R.id.discount_label);

        vailed_code = findViewById(R.id.vailed_code);
        products_list = OrdersBase.getInstance().getmOrders();
        total = 0;
        total_count = 0;
        for (int i = 0; i < products_list.size(); i++) {
            total += products_list.get(i).getTotal_price();
            total_count+= products_list.get(i).getQuantity();
        }

        vat_fee = total * 0.14;
        shipping_fee  = 15;
        cod_fee = 10;

        total_c = total;
        mLoginProgress = new ProgressDialog(this);


        shippingfee = findViewById(R.id.total_shipping);
        codfee = findViewById(R.id.total_cod_fee);
        vat = findViewById(R.id.total_vat);

        total_cost = findViewById(R.id.total_cost);


        total_amount.setText( String.valueOf(total));
        no_of_items.setText(String.valueOf(total_count));

        shippingfee.setText(String.valueOf(shipping_fee));
        codfee.setText(String.valueOf(cod_fee));
        vat.setText(String.valueOf(vat_fee));
        total_cost.setText(String.valueOf(total_c));

        Order_Send = findViewById(R.id.Order_Send);

        orderPnumber = findViewById(R.id.customer_phone);
        orderLocation = findViewById(R.id.customer_address_info);



        orderLocation.setText(selected_address.getAddress());
        orderPnumber.setText(selected_address.getMobile());

       //total_cost.setText(String.valueOf(total_c));
        double finalTotal_count = total_count;
        promo_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        if(!promo_codeState)
                        {
                                getCoupon(promo_name.getText().toString());
                        }
                        else
                        {
                            total_cost.setText(String.valueOf(total_c));
                            vailed_code.setVisibility(View.GONE);
                            promo_code = "";
                            promo_desc = "";
                            promo_disc = 0.0;
                            promo_codeState = false;
                            promo_apply.setText(getResources().getString(R.string.apply));
                        }
                    }
                });
        Order_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderPnumber.length()== 11) {
                    String state;
                    if(selected_address.getGovernment() == null)
                    {
                         state  = "Cairo";
                    }
                    else
                    {
                        state  = selected_address.getGovernment();
                    }
                    if(!promo_codeState)
                    {
                        showProgressDialog();
                        JSONArray jsonArray = new JSONArray();
                        for (int i = 0; i < products_list.size(); i++) {

                            JSONObject product = new JSONObject();
                            try {
                                product.put("product_id", products_list.get(i).getProduct_id());
                                product.put("quantity",  products_list.get(i).getQuantity());
                                jsonArray.put(product);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }


                        JSONObject billing = new JSONObject();
                        try {
                            billing.put("address_1", selected_address.getAddress());
                            billing.put("country",  "EG");
                            billing.put("state",  state);
                            billing.put("phone",  selected_address.getMobile());
                            //jsonArray.put(billing);

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                        /*
                        Order.Billing billing = new Order.Billing();
                        billing.setAddress1(selected_address.getAddress());
                        billing.setCountry("EG");
                        billing.setState(state);
                        billing.setPhone(selected_address.getMobile());

                         */
                        int customerID = SharedPrefManager.getInstance(getApplicationContext()).getUser().getID();
                        OrdersAPIService service = RetrofitApi.retrofitWriteWP3Body("orders").create(OrdersAPIService.class);
                        Map<String, Object> jsonParams = new ArrayMap<>();

                        //put something inside the map, could be null
                        jsonParams.put("currency", "EGP");
                        jsonParams.put("customer_id", customerID);
                        jsonParams.put("payment_method", "cod");
                        jsonParams.put("payment_method_title", "Cash on delivery");
                        jsonParams.put("line_items", jsonArray);
                        jsonParams.put("billing", billing);
                       // jsonParams.put("address_1", selected_address.getAddress());
                       // jsonParams.put("state", state);
                        //jsonParams.put("phone", selected_address.getMobile());

                        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());
                        //defining the call
                        Log.d(TAG, "onClick:   " +customerID + " | " + jsonArray.toString() + " | "  + selected_address.getAddress() + state + selected_address.getMobile());
                        Call<Order> call = service.createOrderDirectWPBody(
                                body
                        );
                        call.enqueue(new Callback<Order>() {
                            @Override
                            public void onResponse(Call<Order> call, Response<Order> response) {
                                Log.d(TAG, "onClick: + Res ");
                                if(response.body() != null) {
                                    OrdersBase.getInstance().dispose();
                                    hideProgressDialog();
                                    Intent loginIntent = new Intent(OrderDetailsActivity.this, OrderSuccessActivity.class);
                                    //Order order = response.body().getOrder();
                                    loginIntent.putExtra(ProductsUI.BUNDLE_TOTAL_COST, total_cost.getText().toString());
                                    loginIntent.putExtra(ProductsUI.BUNDLE_ORDERS_LIST, (Serializable) response.body());
                                    loginIntent.putExtra(ProductsUI.BUNDLE_ORDER_NUMBER, String.valueOf(response.body().getNumber()));
                                    startActivity(loginIntent);
                                    finish();//Don't Return AnyMore TO the last page
                                }
                            }

                            @Override
                            public void onFailure(Call<Order> call, Throwable t) {
                                Log.d(TAG, "onFailure: "+ t.getLocalizedMessage());
                                hideProgressDialog();
                            }
                        });
                    }
                    else
                    {
                        showProgressDialog();
                        JSONArray jsonArray = new JSONArray();
                        for (int i = 0; i < products_list.size(); i++) {

                            JSONObject product = new JSONObject();
                            try {
                                product.put("product_id", products_list.get(i).getProduct_id());
                                product.put("quantity",  products_list.get(i).getQuantity());
                                jsonArray.put(product);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        JSONObject billing = new JSONObject();
                        try {
                            billing.put("address_1", selected_address.getAddress());
                            billing.put("country",  "EG");
                            billing.put("state",  state);
                            billing.put("phone",  selected_address.getMobile());
                            //jsonArray.put(billing);

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        JSONArray couponArray = new JSONArray();
                        JSONObject coupon = new JSONObject();
                        try {
                            coupon.put("code", promo_code);
                            couponArray.put(coupon);

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        int customerID = SharedPrefManager.getInstance(getApplicationContext()).getUser().getID();
                        OrdersAPIService service = RetrofitApi.retrofitWriteWP3Body("orders").create(OrdersAPIService.class);
                        Map<String, Object> jsonParams = new ArrayMap<>();

                        //put something inside the map, could be null
                        jsonParams.put("currency", "EGP");
                        jsonParams.put("customer_id", customerID);
                        jsonParams.put("payment_method", "cod");
                        jsonParams.put("payment_method_title", "Cash on delivery");
                        jsonParams.put("line_items", jsonArray);
                        jsonParams.put("billing", billing);
                        jsonParams.put("coupon_lines", couponArray);
                        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());
                        //defining the call
                        Log.d(TAG, "onClick:   " +customerID + " | " + jsonArray.toString() + " | "  + selected_address.getAddress() + state + selected_address.getMobile());
                        Call<Order> call = service.createOrderDirectWPBody(
                                body
                        );
                        call.enqueue(new Callback<Order>() {
                            @Override
                            public void onResponse(Call<Order> call, Response<Order> response) {
                                Log.d(TAG, "onClick: + Res ");
                                if(response.body() != null) {
                                    OrdersBase.getInstance().dispose();
                                    hideProgressDialog();
                                    Intent loginIntent = new Intent(OrderDetailsActivity.this, OrderSuccessActivity.class);
                                    //Order order = response.body().getOrder();
                                    loginIntent.putExtra(ProductsUI.BUNDLE_TOTAL_COST, total_cost.getText().toString());
                                    loginIntent.putExtra(ProductsUI.BUNDLE_ORDERS_LIST, (Serializable) response.body());
                                    loginIntent.putExtra(ProductsUI.BUNDLE_ORDER_NUMBER, String.valueOf(response.body().getNumber()));
                                    startActivity(loginIntent);
                                    finish();//Don't Return AnyMore TO the last page
                                }
                            }

                            @Override
                            public void onFailure(Call<Order> call, Throwable t) {
                                Log.d(TAG, "onFailure: "+ t.getLocalizedMessage());
                                hideProgressDialog();
                            }
                        });
                    }




                }
                else {
                    hideProgressDialog();
                    Toasty.error(OrderDetailsActivity.this,getResources().getString(R.string.please_check_phone_number), Toast.LENGTH_SHORT,true).show();
                }

        }
        });

        recList = (RecyclerView) findViewById(R.id.shoppingCartRecycleView);
        orders_list = OrdersBase.getInstance().getmOrders();
        orderAdapter = new SingleCartAdapter(orders_list , this);
        products_staggeredGridLayoutManager = new StaggeredGridLayoutManager(1 , LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(products_staggeredGridLayoutManager);
        recList.setAdapter(orderAdapter);
        orderAdapter.notifyDataSetChanged();


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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void onCartPendingClicked(PendingProduct contact, int position) {

    }

    @Override
    public void onCartClicked(Product contact, int position) {

    }

    public void getCoupon(String couponCode)
    {
        CouponAPIService service = RetrofitApi.retrofitReadWP3Coupons("coupons" , couponCode).create(CouponAPIService.class);


        Call<ArrayList<Coupon>> call = service.getWP3Coupons();
        // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
        call.enqueue(new Callback<ArrayList<Coupon>>() {
            @Override
            public void onResponse(Call<ArrayList<Coupon>> call, Response<ArrayList<Coupon>> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);

                // Toast.makeText(ProductsActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();


                // Log.d("onFailure:",  "ProductName:"+response.body().getProducts().get(1).getTitle());

                if(response.body() != null)
                {
                    //products_list.addAll(response.body());
                   // products_listAdapter.notifyDataSetChanged();
                   // shimmerFrameLayout.setVisibility(View.GONE);
                    //shimmerFrameLayout.stopShimmer();

                    for (int i = 0; i < products_list.size(); i++) {
                        //products_list.get(i).getProduct_id()
                        int ProductID;
                        if (products_list.get(i).getParentid() != 0) {
                            ProductID = products_list.get(i).getParentid();
                        } else {

                            ProductID = products_list.get(i).getProduct_id();
                            Log.d(TAG, "onResponse: " + ProductID);
                        }
                        if (response.body().size() > 0) {
                            if (response.body().get(0).getProductIds().contains(ProductID)) {
                                Log.d(TAG, "onResponse: 1");
                                if (response.body().get(0).getUsageLimit() != null && response.body().get(0).getUsageCount() != null) {
                                    Log.d(TAG, "onResponse: 2");
                                    if (response.body().get(0).getUsageLimit() > 0) {
                                        Log.d(TAG, "onResponse: 3");
                                        if (response.body().get(0).getUsageCount() < response.body().get(0).getUsageLimit()) {
                                            Log.d(TAG, "onResponse: 4");
                                            if (Double.parseDouble(response.body().get(0).getMinimumAmount()) <= total_c) {
                                                Log.d(TAG, "onResponse: 5");
                                                if (response.body().get(0).getDiscountType().equals("percent")) {
                                                    Log.d(TAG, "onResponse: 6");
                                                    String discount = response.body().get(0).getAmount();
                                                    promo_disc = products_list.get(i).getTotal_price() * (Double.parseDouble(response.body().get(0).getAmount()) / 100);
                                                    promo_code = couponCode;
                                                    promo_desc = response.body().get(0).getDescription();
                                                    // promo_disc = discount;
                                                    double total_c = Double.parseDouble(total_cost.getText().toString()) - (promo_disc);
                                                    total_cost.setText(String.valueOf(total_c));
                                                    vailed_code.setVisibility(View.VISIBLE);
                                                    promo_apply.setText(getResources().getString(R.string.clear_code));
                                                    discount_label.setText(discount + "% " + getResources().getString(R.string.off));
                                                    promo_codeState = true;
                                                    Log.d(TAG, "onResponse: " + promo_disc);
                                                    Log.d(TAG, "onResponse: " + promo_code);
                                                    Log.d(TAG, "onResponse: " + promo_desc);
                                                    Log.d(TAG, "onResponse: " + total_cost);
                                                }
                                            } else {

                                            }
                                        }
                                    } else {

                                    }
                                }

                            }

                        }
                        else
                        {
                            Toasty.error(OrderDetailsActivity.this, getResources().getString(R.string.invalid_code), Toast.LENGTH_SHORT, true).show();
                        }
                    }
                }






            }

            @Override
            public void onFailure(Call<ArrayList<Coupon>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());


            }
        });
    }

}



