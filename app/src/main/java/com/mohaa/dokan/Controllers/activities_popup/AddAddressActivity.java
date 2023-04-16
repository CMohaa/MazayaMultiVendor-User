package com.mohaa.dokan.Controllers.activities_popup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.mohaa.dokan.Auth.LoginActivity;
import com.mohaa.dokan.Controllers.BaseActivity;
import com.mohaa.dokan.R;
import com.mohaa.dokan.Utils.EditTextRegular;
import com.mohaa.dokan.Utils.Toasty.Toasty;
import com.mohaa.dokan.Utils.helper.SharedPrefManager;
import com.mohaa.dokan.manager.ApiServices.AddressAPIService;
import com.mohaa.dokan.manager.ApiServices.StatesAPIService;
import com.mohaa.dokan.manager.Call.States;
import com.mohaa.dokan.manager.Response.AddressResponse;
import com.mohaa.dokan.manager.RetrofitApi;
import com.mohaa.dokan.networksync.CheckInternetConnection;

import java.sql.Timestamp;
import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddressActivity extends BaseActivity {

    private TextView add_to_product;
    private String user_id;
    private String check;
    private EditTextRegular ads_phone;
    private EditTextRegular ads_address;

    //ProgressDialog
    private ProgressDialog mLoginProgress;


    private AutoCompleteTextView government_ip;
    private Spinner spinner_government_ip;
    private ArrayAdapter<String> adapter_government;

    private ImageView done;


    Toolbar toolbar;
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        //Progress Dialog
        /*
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
                */

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

        mLoginProgress = new ProgressDialog(this);


        ads_address = findViewById(R.id.customer_address);
        ads_phone = findViewById(R.id.customer_phone);


        //
        government_ip = (AutoCompleteTextView) findViewById(R.id.goverment_ip);


        spinner_government_ip = (Spinner) findViewById(R.id.spinner_goverment_ip);


        //dapter_government = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.cities));



       // spinner_government_ip.setAdapter(adapter_government);

        getMainCategory(64);
        spinner_government_ip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                government_ip.setText(spinner_government_ip.getSelectedItem().toString());
                government_ip.dismissDropDown();


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                government_ip.setText(spinner_government_ip.getSelectedItem().toString());
                government_ip.dismissDropDown();
            }
        });

        //
        done = findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String address = ads_address.getText().toString();
                final String phone = ads_phone.getText().toString();

                if (validateAddress() && validateNumber()) {
                    if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(address)) {
                        mLoginProgress.setTitle(getResources().getString(R.string.loading));
                        mLoginProgress.setMessage(getResources().getString(R.string.please_wait));
                        mLoginProgress.setCanceledOnTouchOutside(false);
                        mLoginProgress.show();

                        UpdateInfo(address, phone, SharedPrefManager.getInstance(getApplicationContext()).getUser().getDisplay_name());

                    }
                    else {
                        Toasty.error(AddAddressActivity.this,getResources().getString(R.string.please_fill_empty_field), Toast.LENGTH_SHORT,true).show();
                    }
                }
            }
        });

        ads_phone.addTextChangedListener(numberWatcher);
        ads_address.addTextChangedListener(addressWatcher);

    }
    private void getMainCategory(int type) {
        ArrayList<String> Values_main = new ArrayList<>();

        StatesAPIService service = RetrofitApi.retrofitRead().create(StatesAPIService.class);


        Call<States> call = service.getStates(type);

        call.enqueue(new Callback<States>() {
            @Override
            public void onResponse(Call<States> call, Response<States> response) {

                //ArrayList<Address> jsonresponse  = new ArrayList<>(response.body().getAddress());
                for (int i = 0; i <response.body().getStates().size() ; i++) {
                    Values_main.add(response.body().getStates().get(i).getName());
                }
                ArrayAdapter<String> spinnerMainAdapter = new ArrayAdapter<String>(AddAddressActivity.this,  android.R.layout.simple_dropdown_item_1line, Values_main);
                spinnerMainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spinner_government_ip.setAdapter(spinnerMainAdapter);
            }

            @Override
            public void onFailure(Call<States> call, Throwable t) {
                Toast.makeText(AddAddressActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
    public void UpdateInfo(final String address, final String phone, final String owner_name) {


        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        String time = String.valueOf(timestamp.getTime());

        AddressAPIService service = RetrofitApi.retrofitWrite().create(AddressAPIService.class);

        Call<AddressResponse> call = service.createAdress(
                SharedPrefManager.getInstance(getApplicationContext()).getUser().getID(),
                owner_name,
                64,
                spinner_government_ip.getSelectedItem().toString(),
                address,
                phone,
                1

        );

        call.enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {

                if(response.body() != null) {
                    mLoginProgress.dismiss();
                    //Toast.makeText(SetupActivity.this, "The User Settings Are Updated", Toast.LENGTH_SHORT).show();
                    Intent mIntent = new Intent(AddAddressActivity.this, AddressActivity.class);
                    startActivity(mIntent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {
                mLoginProgress.dismiss();
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


    TextWatcher addressWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @Override
        public void afterTextChanged(Editable s) {

            check = s.toString();

            if (check.length() < 10 || check.length() > 64) {
               // ads_address.setError("Address Must consist of 10 to 64 characters");
            }
        }

    };


    //TextWatcher for Mobile -----------------------------------------------------

    TextWatcher numberWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @Override
        public void afterTextChanged(Editable s) {

            check = s.toString();

            if(check.startsWith("012") ||check.startsWith("011")||check.startsWith("010")|check.startsWith("015")) {

                if (check.length()>11) {
                   // ads_phone.setError("Number cannot be grated than 11 digits");
                }else if(check.length()<11){
                    //ads_phone.setError("Number should be 11 digits");
                }
            }
            else {
              //  ads_phone.setError("Please use Valid Number");
            }
        }

    };
    private boolean validateNumber() {

        check = ads_phone.getText().toString();
        Log.e("inside number",check.length()+" ");

        if(check.startsWith("012") ||check.startsWith("011")||check.startsWith("010")|check.startsWith("015")) {

            if (check.length()>11) {
                return false;
            }else if(check.length()<11){
                return false;
            }
            return true;
        }
        else {
            return false;
        }
    }



    private boolean validateAddress() {

        check = ads_address.getText().toString();

        return !(check.length() < 10 || check.length() > 64);

    }
}
