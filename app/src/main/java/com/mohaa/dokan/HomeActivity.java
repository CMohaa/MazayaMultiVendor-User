package com.mohaa.dokan;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mohaa.dokan.Auth.LoginActivity;
import com.mohaa.dokan.Controllers.BaseActivity;
import com.mohaa.dokan.Controllers.activities_popup.CartReadyActivity;
import com.mohaa.dokan.Controllers.activities_products.ProductsActivity;
import com.mohaa.dokan.Utils.CircleImageView;
import com.mohaa.dokan.Utils.PermUtil;
import com.mohaa.dokan.Utils.ProductsUI;
import com.mohaa.dokan.Utils.helper.SharedPrefManager;
import com.mohaa.dokan.manager.ApiServices.CategoryAPIService;
import com.mohaa.dokan.manager.ApiServices.UserAPIService;
import com.mohaa.dokan.manager.OrdersBase;
import com.mohaa.dokan.manager.RetrofitApi;
import com.mohaa.dokan.models.wp.ProductCategory;
import com.mohaa.dokan.networksync.CheckInternetConnection;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "HomeActivity";

    private TextView count;
    private ImageView cart;
    int cartCount = 0;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    public NavController navController;
    public NavigationView navigationView;

    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
    public static final String REGISTRATION_ERROR = "RegistrationError";

    public final static String EXTRA_ORIENTATION = "EXTRA_ORIENTATION";
    public final static String EXTRA_WITH_LINE_PADDING = "EXTRA_WITH_LINE_PADDING";

    private TextView profile_name , profile_credit;
    private CircleImageView profile_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
        init();
    }
    /*
    @Override
    public void onPostCreate(@Nullable @Nullable Bundle savedInstanceState, @Nullable @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        actionBarDrawerToggle.syncState();
    }
*/



    private void init() {



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.my_drawer);

        navigationView = findViewById(R.id.navigationView);
        //
        count = findViewById(R.id.count);
        cart = findViewById(R.id.cart);

        profile_name = navigationView.getHeaderView(0).findViewById(R.id.header_name);
        profile_image = navigationView.getHeaderView(0).findViewById(R.id.header_img);
        profile_credit = navigationView.getHeaderView(0).findViewById(R.id.header_credit);

        //LoadMenus(navigationView);
        LoadMenusWP(navigationView);


        //
        mToggle = new ActionBarDrawerToggle(this , drawerLayout , R.string.open , R.string.close);
        drawerLayout.addDrawerListener(mToggle);

        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);

        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cartCount > 0 ) {
                    Intent loginIntent = new Intent(HomeActivity.this, CartReadyActivity.class);
                    startActivity(loginIntent);
                }
                else {
                    Toast.makeText(HomeActivity.this, getResources().getString(R.string.cart_is_empty), Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            //registerGCM();
            sendRegistrationTokenToServer(FirebaseInstanceId.getInstance().getToken());
            //Toast.makeText(this, String.valueOf( SharedPrefManager.getInstance(getApplicationContext()).getLastViewed()) + " " , Toast.LENGTH_SHORT).show();
        }






    }
    private void LoadMenusWP(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        final Menu submenu = menu.addSubMenu(getResources().getString(R.string.main_category));


        CategoryAPIService service = RetrofitApi.retrofitReadWP3Categories("products/categories" , "0" , "count").create(CategoryAPIService.class);

        Call<List<ProductCategory>> call = service.getWP3Categories();

        call.enqueue(new Callback<List<ProductCategory>>() {
            @Override
            public void onResponse(Call<List<ProductCategory>> call, final Response<List<ProductCategory>> response) {
                try {
                    if(response.body() != null) {
                        for (int i = 0; i < response.body().size(); i++) {
                            final int finalI = i;
                            submenu.add(Html.fromHtml(response.body().get(finalI).getName())).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    Intent intent = new Intent(HomeActivity.this, ProductsActivity.class);
                                    intent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST, response.body().get(finalI));
                                    intent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST_TYPE, "Main");
                                    startActivity(intent);
                                    return false;
                                }
                            });
                        }
                    }
                }catch (Exception ex)
                {
                    ex.getLocalizedMessage();
                }

            }

            @Override
            public void onFailure(Call<List<ProductCategory>> call, Throwable t) {

            }
        });
        navigationView.invalidate();
    }



    //Responsible For Adding the 3 tabs : Camera  , Home , Messages
    @Override
    protected void onResume() {
        super.onResume();
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
        // Update Cart Count
        cartCount = OrdersBase.getInstance().getmOrders().size();

        if (cartCount > 0) {
            count.setVisibility(View.VISIBLE);
            count.setText(String.valueOf(cartCount));
        } else {
            count.setVisibility(View.GONE);
        }
        /*
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

         */


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
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            attemptToExitIfRoot(null);
        }


    }

    // convert the list of contact to a map of members




    public void sendtoLogin()
    {
        Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        // getActivity().finish();



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        menuItem.setChecked(true);

        drawerLayout.closeDrawers();

        int id = menuItem.getItemId();

        switch (id) {

            case R.id.nav_orders:

                if (!SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {
                    sendtoLogin();

                }
                else {
                    navController.navigate(R.id.orders_fragment);
                }

                break;




            case R.id.nav_settings:
                if (!SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {
                    sendtoLogin();

                }
                else {
                    navController.navigate(R.id.settings_fragment);
                }
                break;

        }
        return true;

    }
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }
    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(drawerLayout, Navigation.findNavController(this, R.id.nav_host_fragment));
    }

    private void registerGCM() {
        Intent registrationComplete = null;
        String token = null;
        try {

            token = FirebaseInstanceId.getInstance().getToken();
            Log.w("GCMRegIntentService", "token:" + token);

            sendRegistrationTokenToServer(token);
            registrationComplete = new Intent(REGISTRATION_SUCCESS);
            registrationComplete.putExtra("token", token);
        } catch (Exception e) {
            Log.w("GCMRegIntentService", "Registration error");
            registrationComplete = new Intent(REGISTRATION_ERROR);
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationTokenToServer(final String token) {
        //Getting the user id from shared preferences
        //We are storing gcm token for the user in our mysql database
        Log.d(TAG, "sendRegistrationTokenToServer: " + token);
        UserAPIService service = RetrofitApi.retrofitWrite().create(UserAPIService.class);
        /*
        UserWp user = new UserWp(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId(), token);
        SharedPrefManager.getInstance(getApplicationContext()).userToken(token);

        Call<UserResponse> call = service.updateGcmToken(
                user.getId(),
                user.getGcmtoken()

        );

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                if(response.body() != null)
                {

                }
               // Toast.makeText(HomeActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                //Log.d("gcmKey", "onResponse: " + response.body().getMessage());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

         */
    }
}
