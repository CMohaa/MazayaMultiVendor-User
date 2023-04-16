package com.mohaa.dokan.Controllers.activities_orders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.mohaa.dokan.Auth.LoginActivity;
import com.mohaa.dokan.Controllers.BaseActivity;
import com.mohaa.dokan.HomeActivity;
import com.mohaa.dokan.R;
import com.mohaa.dokan.Utils.ProductsUI;
import com.mohaa.dokan.Utils.helper.SharedPrefManager;
import com.mohaa.dokan.models.wp.Order;
import com.mohaa.dokan.networksync.CheckInternetConnection;

import java.io.Serializable;

public class OrderSuccessActivity extends BaseActivity {

    private TextView orderNumberValue , continueShipping;
    private Button viewOrderButton ;
    private String order_number;
    private String total_cost;
    private com.mohaa.dokan.models.wp.Order order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
        order = (Order) getIntent().getExtras().getSerializable(ProductsUI.BUNDLE_ORDERS_LIST);
        order_number = getIntent().getStringExtra(ProductsUI.BUNDLE_ORDER_NUMBER);
        total_cost = getIntent().getStringExtra(ProductsUI.BUNDLE_TOTAL_COST);
        orderNumberValue = findViewById(R.id.orderNumberValue);
        orderNumberValue.setText(order_number);
        viewOrderButton = findViewById(R.id.viewOrderButton);
        continueShipping = findViewById(R.id.continueShipping);
        viewOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent loginIntent = new Intent(OrderSuccessActivity.this, TrackActivity.class);
                loginIntent.putExtra(ProductsUI.BUNDLE_ORDERS_LIST, (Serializable) order);
                startActivity(loginIntent);
                finish();//Don't Return AnyMore TO the last page


            }
        });
        continueShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(OrderSuccessActivity.this, HomeActivity.class);
                startActivity(loginIntent);
                finish();//Don't Return AnyMore TO the last page
            }
        });

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
}
