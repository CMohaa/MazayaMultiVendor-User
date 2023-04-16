package com.mohaa.dokan.Controllers.activities_popup;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mohaa.dokan.Auth.LoginActivity;
import com.mohaa.dokan.Controllers.BaseActivity;
import com.mohaa.dokan.Controllers.activities_orders.OrderDetailsActivity;
import com.mohaa.dokan.R;
import com.mohaa.dokan.Utils.PermUtil;
import com.mohaa.dokan.Utils.ProductsUI;
import com.mohaa.dokan.Utils.Toasty.Toasty;
import com.mohaa.dokan.Utils.helper.SharedPrefManager;
import com.mohaa.dokan.manager.ApiServices.AddressAPIService;
import com.mohaa.dokan.manager.Call.Addresssg;
import com.mohaa.dokan.manager.OrdersBase;
import com.mohaa.dokan.manager.RetrofitApi;
import com.mohaa.dokan.models.Address;
import com.mohaa.dokan.models.Addressg;
import com.mohaa.dokan.networksync.CheckInternetConnection;
import com.mohaa.dokan.views.AddressAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddressActivity extends BaseActivity implements OnAdressClickListener {


    private RecyclerView recList;

    private Addressg selected_address;
    private TextView btn_add_address;
    private List<Addressg> addressList;
    private AddressAdapter addressAdapter;
    Toolbar toolbar;
    TextView btn_continue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

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
        init();





    }

    private void init() {

        //
        addressList = new ArrayList<>();
        btn_add_address = findViewById(R.id.btn_add_address);
        recList = (RecyclerView) findViewById(R.id.shoppingCartRecycleView);

       // Address address = new Address(1,"Mohaa" , 64 , 1056 , "Toukh City " , "01277637646" ,1);
       // addressList.add(address);
        //addressList = OrdersBase.getInstance().getmOrders();
        addressAdapter = new AddressAdapter(addressList , this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this , LinearLayoutManager.VERTICAL, false);

        recList.setLayoutManager(mLayoutManager);
        recList.setAdapter(addressAdapter);
        addressAdapter.notifyDataSetChanged();
        load();

        btn_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddressActivity.this, AddAddressActivity.class);
                //intent.putExtra("subTotal",subTotal.getText());

                startActivity(intent);
                finish();//Don't Return AnyMore TO the last page
            }
        });
        btn_continue = findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OrdersBase.getInstance().getmOrders().size() != 0) {
                    if(addressList.size() > 0  && selected_address == null)
                    {
                        selected_address = addressList.get(0);
                        Toasty.success(AddressActivity.this, selected_address.getAddress(), Toast.LENGTH_SHORT).show();
                    }
                    if(selected_address != null) {
                        Intent intent = new Intent(AddressActivity.this, OrderDetailsActivity.class);
                        intent.putExtra(ProductsUI.BUNDLE_ADDRESS_LIST, (Serializable) selected_address);
                        //intent.putExtra("subTotal",subTotal.getText());

                        startActivity(intent);
                        finish();//Don't Return AnyMore TO the last page
                    }
                    else
                    {
                        Toasty.error(AddressActivity.this,getResources().getString(R.string.please_select_address), Toast.LENGTH_SHORT,true).show();


                    }
                }
            }
        });
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    //Toast.makeText(MultiEditorActivity.this, "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onAdressClicked(Address contact, int position) {
        //selected_address = contact;
       // Toasty.success(this, selected_address.getAddress()  , Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAdressClicked(Addressg contact, int position) {
        selected_address = contact;
        Toasty.success(this, selected_address.getAddress()  , Toast.LENGTH_SHORT).show();
    }

    private void load() {
        showProgressDialog();
        AddressAPIService service = RetrofitApi.retrofitRead().create(AddressAPIService.class);
        Call<Addresssg> call = service.getAddress(SharedPrefManager.getInstance(getApplicationContext()).getUser().getID());
        call.enqueue(new Callback<Addresssg>() {
            @Override
            public void onResponse(Call<Addresssg> call, Response<Addresssg> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);
               // Toast.makeText(AddressActivity.this, response.body().getAddresses().get(0).getCountry(), Toast.LENGTH_SHORT).show();

                addressList.addAll(response.body().getAddress());
                addressAdapter.notifyDataSetChanged();
                hideProgressDialog();


            }

            @Override
            public void onFailure(Call<Addresssg> call, Throwable t) {
                hideProgressDialog();
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
