package com.mohaa.dokan.Controllers.activities_popup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.mohaa.dokan.Controllers.BaseActivity;
import com.mohaa.dokan.R;
import com.mohaa.dokan.Utils.ConstantUI;
import com.mohaa.dokan.Utils.TextViewBold;
import com.mohaa.dokan.Utils.TextViewLight;
import com.mohaa.dokan.Utils.TextViewRegular;
import com.mohaa.dokan.Utils.helper.SharedPrefManager;
import com.mohaa.dokan.models.wp.Customer;


public class MyAddressActivity extends BaseActivity {


    LinearLayout llBillingAddress;


    LinearLayout llShippingAddress;


    LinearLayout llNoBillingAddress;


    LinearLayout llNoShippingAddress;


    TextViewRegular tvNoBillingAddressAdd;


    TextViewRegular tvNoShippingAddressAdd;


    TextViewBold tvBillingName;


    TextViewBold tvShippingName;


    TextViewLight tvBillingAddress;


    TextViewLight tvShippingAddress;


    TextViewLight tvBillingPhoneNumber;


    TextViewBold tvShippingEdit;


    TextViewBold tvBillingEdit;

    //private Customer customer;
    private Customer.Shipping shippingAddress;
    private Customer.Billing billingAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        //customer =  SharedPrefManager.getInstance(getApplicationContext()).getCustomer();
        shippingAddress =  SharedPrefManager.getInstance(getApplicationContext()).getCustomerShipping();
        billingAddress =  SharedPrefManager.getInstance(getApplicationContext()).getCustomerBilling();
        initViews();

        setAddress();
        showBackButton();
        InitListeners();
    }


    private void initViews() {

        llBillingAddress  = findViewById(R.id.llBillingAddress);

        llShippingAddress  = findViewById(R.id.llShippingAddress);
        llNoBillingAddress  = findViewById(R.id.llNoBillingAddress);
        llNoShippingAddress  = findViewById(R.id.llNoShippingAddress);
        tvNoBillingAddressAdd  = findViewById(R.id.tvNoBillingAddressAdd);
        tvNoShippingAddressAdd  = findViewById(R.id.tvNoShippingAddressAdd);
        tvBillingName  = findViewById(R.id.tvBillingName);
        tvShippingName  = findViewById(R.id.tvShippingName);
        tvBillingAddress  = findViewById(R.id.tvBillingAddress);
        tvShippingAddress  = findViewById(R.id.tvShippingAddress);
        tvBillingPhoneNumber  = findViewById(R.id.tvBillingPhoneNumber);

        tvShippingEdit  = findViewById(R.id.tvShippingEdit);
        tvBillingEdit  = findViewById(R.id.tvBillingEdit);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("here", "in on resume");


    }

    public void setAddress() {
        //set Address data



            if (billingAddress != null) {
                if (billingAddress.getPhone().equals("") && billingAddress.getFirstName().equals("") && billingAddress.getLastName().equals("") && billingAddress.getAddress1().equals("") && billingAddress.getAddress2().equals("") && billingAddress.getCompany().equals("") && billingAddress.getCity().equals("") && billingAddress.getState().equals("") && billingAddress.getPostcode().equals("")) {
                    llBillingAddress.setVisibility(View.GONE);
                    llNoBillingAddress.setVisibility(View.VISIBLE);
                } else {
                    llBillingAddress.setVisibility(View.VISIBLE);
                    llNoBillingAddress.setVisibility(View.GONE);

                    tvBillingPhoneNumber.setText(billingAddress.getPhone() + "");

                    String address1empty = billingAddress.getAddress1().equals("") ? "" : ", ";
                    String address2empty = billingAddress.getAddress2().equals("") ? "" : ", ";
                    String cityempty = billingAddress.getCity().equals("") ? "" : ", ";
                    String stateempty = billingAddress.getState().equals("") ? "" : ", ";
                    String countryempty = billingAddress.getCountry().equals("") ? "" : ", ";

                    String address = billingAddress.getAddress1() + address1empty + billingAddress.getAddress2() + address2empty + billingAddress.getCity() + cityempty + billingAddress.getState() + stateempty + billingAddress.getCountry() + countryempty + billingAddress.getPostcode();
                    tvBillingAddress.setText(address);

                    tvBillingName.setText(billingAddress.getFirstName() + " " + billingAddress.getLastName());
                }
            }
            else
            {
                Toast.makeText(this, getResources().getString(R.string.string_some_thing_wrong), Toast.LENGTH_SHORT).show();
            }

            if (shippingAddress != null) {
                if (shippingAddress.getFirstName().equals("") && shippingAddress.getLastName().equals("") && shippingAddress.getAddress1().equals("") && shippingAddress.getAddress2().equals("") && shippingAddress.getCompany().equals("") && shippingAddress.getCity().equals("") && shippingAddress.getState().equals("") && shippingAddress.getPostcode().equals("")) {
                    llShippingAddress.setVisibility(View.GONE);
                    llNoShippingAddress.setVisibility(View.VISIBLE);
                } else {
                    llShippingAddress.setVisibility(View.VISIBLE);
                    llNoShippingAddress.setVisibility(View.GONE);

                    String address1empty = shippingAddress.getAddress1().equals("") ? "" : ", ";
                    String address2empty = shippingAddress.getAddress2().equals("") ? "" : ", ";
                    String cityempty = shippingAddress.getCity().equals("") ? "" : ", ";
                    String stateempty = shippingAddress.getState().equals("") ? "" : ", ";
                    String countryempty = shippingAddress.getCountry().equals("") ? "" : ", ";

                    String address = shippingAddress.getAddress1() + address1empty + shippingAddress.getAddress2() + address2empty + shippingAddress.getCity() + cityempty + shippingAddress.getState() + stateempty + shippingAddress.getCountry() + countryempty + shippingAddress.getPostcode();
                    tvShippingAddress.setText(address);

                    tvShippingName.setText(shippingAddress.getFirstName() + " " + shippingAddress.getLastName());
                }
            }
            else
            {
                Toast.makeText(this, getResources().getString(R.string.string_some_thing_wrong), Toast.LENGTH_SHORT).show();
            }

    }

    public void addBilling() {
        Intent intent = new Intent(MyAddressActivity.this, AddWPAddressActivity.class);
        intent.putExtra(ConstantUI.TYPE, 0);
        startActivity(intent);
    }

    public void addShipping() {
        Intent intent = new Intent(MyAddressActivity.this, AddWPAddressActivity.class);
        intent.putExtra(ConstantUI.TYPE, 1);
        startActivity(intent);
    }

    private void InitListeners() {
        tvBillingEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBilling();
            }
        });
        tvShippingEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addShipping();
            }
        });
        tvNoBillingAddressAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBilling();
            }
        });
        tvNoShippingAddressAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addShipping();
            }
        });
    }



}
