package com.mohaa.dokan.Controllers.activities_popup;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.mohaa.dokan.Auth.LoginActivity;
import com.mohaa.dokan.Controllers.BaseActivity;
import com.mohaa.dokan.Controllers.fragments_products.CartBottomSheetFragment;
import com.mohaa.dokan.HomeActivity;
import com.mohaa.dokan.R;
import com.mohaa.dokan.Utils.CartInfoBar;
import com.mohaa.dokan.Utils.helper.SharedPrefManager;
import com.mohaa.dokan.interfaces.OnCallbackReceived;
import com.mohaa.dokan.manager.OrdersBase;
import com.mohaa.dokan.models.CartItem;
import com.mohaa.dokan.models.PendingProduct;
import com.mohaa.dokan.networksync.CheckInternetConnection;
import com.mohaa.dokan.views.CartProductsAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;

public class CartReadyActivity extends BaseActivity implements CartProductsAdapter.CartProductsAdapterListener , OnCallbackReceived {

    private String mLanguageCode = "ar";
    Toolbar toolbar;
    private List<CartItem> cartItems;
    private CartInfoBar cartInfoBar;
    private List<PendingProduct> products_list;
    private TextView no_of_items;
    private TextView continueShipping;
    private Button viewOrderButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_ready);

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

    @Override
    protected void onStart() {

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            sendtoLogin();

        }
        super.onStart();
    }
    public void sendtoLogin()
    {
        Intent loginIntent = new Intent(CartReadyActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        //finish();//Don't Return AnyMore TO the last page



    }
    @Override
    protected void onResume() {
        super.onResume();
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();


        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            //finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        else
        {
            showCart();
        }
    }

    void showCart() {
        CartBottomSheetFragment fragment = new CartBottomSheetFragment();
        fragment.show(getSupportFragmentManager(), fragment.getTag());
    }
    private void toggleCartBar(boolean show) {
        if (show)
            cartInfoBar.setVisibility(View.VISIBLE);
        else
            cartInfoBar.setVisibility(View.GONE);
    }
    private void init_bar() {
        cartItems = new ArrayList<>();
        cartInfoBar.setListener(() -> showCart());
        for (int i = 0; i < OrdersBase.getInstance().getmOrders().size() ; i++)
        {
            cartItems.add(new CartItem(String.valueOf(i) , OrdersBase.getInstance().getmOrders().get(i),1));

        }
        if (cartItems != null && cartItems.size() > 0) {
            setCartInfoBar(cartItems);
            toggleCartBar(true);
        } else {
            toggleCartBar(false);
        }
        //changeStatusBarColor();
    }

    private void setCartInfoBar(List<CartItem> cartItems) {
        int itemCount = 0;
        for (CartItem cartItem : cartItems) {
            itemCount += cartItem.quantity;
        }
        double total = 0;
        for (int i = 0; i < OrdersBase.getInstance().getmOrders().size(); i++) {
            total += OrdersBase.getInstance().getmOrders().get(i).getTotal_price();
        }
        cartInfoBar.setData(itemCount, String.valueOf(total));
        //cartInfoBar.setData(itemCount,"5000");
    }

    public void changeStatusBarColor(int color) {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(color);
        }
    }



    @Override
    public void Update() {
        init();
    }

    private void init() {



        no_of_items = findViewById(R.id.no_of_items);

        products_list = OrdersBase.getInstance().getmOrders();

        double total_count = 0;
        for (int i = 0; i < products_list.size(); i++) {
            total_count+= products_list.get(i).getQuantity();
        }


        no_of_items.setText(String.valueOf(total_count));
        cartInfoBar = findViewById(R.id.cart_info_bar);
        init_bar();
        viewOrderButton = findViewById(R.id.viewOrderButton);
        continueShipping = findViewById(R.id.continueShipping);
        viewOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(OrdersBase.getInstance().getmOrders().size() > 0)
                {
                    Intent loginIntent = new Intent(CartReadyActivity.this, AddressActivity.class);
                    startActivity(loginIntent);
                    finish();//Don't Return AnyMore TO the last page
                }
                else {
                    onBackPressed();
                }

            }
        });
        continueShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LocaleHelper.setLocale(CartReadyActivity.this, mLanguageCode);
                //recreate();
                Intent loginIntent = new Intent(CartReadyActivity.this, HomeActivity.class);
                startActivity(loginIntent);
                finish();//Don't Return AnyMore TO the last page
            }
        });
    }

    @Override
    public void onCartItemRemoved(int index, PendingProduct cartItem) {

    }

    @Override
    public void onCartItemAdded(int index) {

    }



    @Override
    public void onQuantityChnaged(int index) {

    }
}
