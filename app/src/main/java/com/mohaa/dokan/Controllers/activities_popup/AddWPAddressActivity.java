package com.mohaa.dokan.Controllers.activities_popup;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.mohaa.dokan.Controllers.BaseActivity;
import com.mohaa.dokan.R;
import com.mohaa.dokan.Utils.ConstantUI;
import com.mohaa.dokan.Utils.EditTextRegular;
import com.mohaa.dokan.Utils.TextViewMedium;
import com.mohaa.dokan.Utils.TextViewRegular;
import com.mohaa.dokan.Utils.helper.SharedPrefManager;
import com.mohaa.dokan.manager.ApiServices.CustomerAPIService;
import com.mohaa.dokan.manager.Response.CustomerResponse;
import com.mohaa.dokan.manager.RetrofitApi;
import com.mohaa.dokan.models.wp.Customer;
import com.mohaa.dokan.models.wp.Customer.Billing;
import com.mohaa.dokan.networksync.CheckInternetConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddWPAddressActivity extends BaseActivity  {

    private static final String TAG = "AddWPAddressActivity";
    private int type;

    TextViewMedium tvActivityTitle;


    TextInputLayout tilPhone;


    EditTextRegular etFirstName;


    EditTextRegular etLastName;


    EditTextRegular etAddress1;


    EditTextRegular etAddress2;


    EditTextRegular etPincode;


    EditTextRegular etCity;


    EditTextRegular etCompany;


    EditTextRegular etPhoneNumber;


    TextViewRegular tvCancel;


    TextViewRegular tvSave;


    LinearLayout llButton;

   // private Customer customer;
   private Customer.Shipping shippingAddress;
    private Billing billingAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wp_address);
        initViews();
       // customer = SharedPrefManager.getInstance(getApplicationContext()).getCustomer();
        shippingAddress =  SharedPrefManager.getInstance(getApplicationContext()).getCustomerShipping();
        billingAddress =  SharedPrefManager.getInstance(getApplicationContext()).getCustomerBilling();

        type = getIntent().getIntExtra(ConstantUI.TYPE, 0);
        if (type == 0) {
            tvActivityTitle.setText(getResources().getText(R.string.add_billing_address));
        } else {
            tvActivityTitle.setText(getResources().getText(R.string.add_shipping_address));
            tilPhone.setVisibility(View.GONE);
        }
        setAddress();
        showBackButton();
        InitListeners();
    }

    private void InitListeners() {
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAddress();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }

    private void initViews() {
        tvActivityTitle = findViewById(R.id.tvActivityTitle);
        tilPhone = findViewById(R.id.tilPhone);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etLastName = findViewById(R.id.etLastName);
        etAddress1 = findViewById(R.id.etAddress1);
        etAddress2 = findViewById(R.id.etAddress2);
        etPincode = findViewById(R.id.etPincode);
        etCity = findViewById(R.id.etCity);
        etCompany = findViewById(R.id.etCompany);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        tvCancel = findViewById(R.id.tvCancel);
        tvSave = findViewById(R.id.tvSave);
        llButton = findViewById(R.id.llButton);

    }


    @Override
    protected void onResume() {
        super.onResume();



    }

    public void setAddress() {


            if (type == 0) {
                //Log.d("AddAddress", "setAddress: " + billingAddress.getFirstName());
                //billing Address
                if (billingAddress != null) {
                    etFirstName.setText(billingAddress.getFirstName() + "");
                    etLastName.setText(billingAddress.getLastName() + "");
                    etPincode.setText(billingAddress.getPostcode() + "");
                    etAddress1.setText(billingAddress.getAddress1() + "");
                    etAddress2.setText(billingAddress.getAddress2() + "");
                    etCity.setText(billingAddress.getCity() + "");
                    etPhoneNumber.setText(billingAddress.getPhone() + "");
                    etCompany.setText(billingAddress.getCompany() + "");


                }
            } else {
                //Shipping Address
                if (shippingAddress != null) {
                    etFirstName.setText(shippingAddress.getFirstName() + "");
                    etLastName.setText(shippingAddress.getLastName() + "");
                    etPincode.setText(shippingAddress.getPostcode() + "");
                    etAddress1.setText(shippingAddress.getAddress1() + "");
                    etAddress2.setText(shippingAddress.getAddress2() + "");
                    etCity.setText(shippingAddress.getCity() + "");
                    etCompany.setText(shippingAddress.getCompany() + "");
                }
            }

    }
    public void updateAddress() {
        if (CheckInternetConnection.isInternetConnected(this)) {
            try {
                if (type == 0) {
                    //Billing Address
                    Billing billingAddress = new Billing();
                    billingAddress.setFirstName(etFirstName.getText().toString());
                    billingAddress.setLastName(etLastName.getText().toString());
                    billingAddress.setPostcode(etPincode.getText().toString());
                    billingAddress.setAddress1(etAddress1.getText().toString());
                    billingAddress.setAddress2(etAddress2.getText().toString());
                    billingAddress.setCity(etCity.getText().toString());
                    billingAddress.setCompany(etCompany.getText().toString());
                    billingAddress.setPhone(etPhoneNumber.getText().toString());
                    CustomerAPIService service = RetrofitApi.retrofitWrite().create(CustomerAPIService.class);
                    Call<CustomerResponse> callp = service.UpdateBillingWP(
                            String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUser().getID()),
                            billingAddress.getFirstName(),
                            billingAddress.getLastName(),
                            billingAddress.getCompany(),
                            billingAddress.getAddress1(),
                            billingAddress.getAddress2(),
                            billingAddress.getCity(),
                            "CA",
                            billingAddress.getPostcode(),
                            "EG",
                            SharedPrefManager.getInstance(getApplicationContext()).getUser().getUser_email(),
                            billingAddress.getPhone()
                    );
                    callp.enqueue(new Callback<CustomerResponse>() {
                        @Override
                        public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                            if(response.body() != null) {

                                Customer customer = response.body().getCustomer();


                                Log.d(TAG, "onResponse: " + response.body().getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<CustomerResponse> call, Throwable t) {
                            Log.d(TAG, "onFailure: Product "+ t.getLocalizedMessage());
                        }
                    });
                } else {
                    //Shipping Address
                    shippingAddress.setFirstName(etFirstName.getText().toString());
                    shippingAddress.setLastName(etLastName.getText().toString());
                    shippingAddress.setPostcode(etPincode.getText().toString());
                    shippingAddress.setAddress1(etAddress1.getText().toString());
                    shippingAddress.setAddress2(etAddress2.getText().toString());
                    shippingAddress.setCity(etCity.getText().toString());
                    shippingAddress.setCompany(etCompany.getText().toString());
                }

            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        } else {
            Toast.makeText(this, R.string.internet_connection_failed, Toast.LENGTH_LONG).show();
        }


    }



}