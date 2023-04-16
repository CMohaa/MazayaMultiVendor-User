package com.mohaa.dokan.Auth;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.mohaa.dokan.Controllers.BaseActivity;
import com.mohaa.dokan.HomeActivity;
import com.mohaa.dokan.R;
import com.mohaa.dokan.Utils.EditTextRegular;
import com.mohaa.dokan.Utils.Toasty.Toasty;
import com.mohaa.dokan.Utils.helper.SharedPrefManager;

import com.mohaa.dokan.manager.ApiServices.UserAPIService;
import com.mohaa.dokan.manager.Response.UserResponse;
import com.mohaa.dokan.manager.RetrofitApi;
import com.mohaa.dokan.models.User;
import com.mohaa.dokan.models.wp.Customer;
import com.mohaa.dokan.models.wp.UserWp;
import com.mohaa.dokan.networksync.CheckInternetConnection;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "Login" ;


    //Layout_Views
    private EditTextRegular mEmailField;
    private EditTextRegular mPasswordField;
    //
    private ImageView appLogo;
    //ProgressDialog
    private ProgressDialog mLoginProgress;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

       // Typeface typeface = ResourcesCompat.getFont(this, R.font.jomhuriaregular);

        appLogo= findViewById(R.id.logo);
        YoYo.with(Techniques.Bounce)
                .duration(7000)
                .playOn(findViewById(R.id.logo));

        YoYo.with(Techniques.FadeInUp)
                .duration(5000)
                .playOn(findViewById(R.id.logo));
       // Typeface typeface = ResourcesCompat.getFont(this, R.font.blacklist);
      //  appname = findViewById(R.id.appname);
       // appname.setTypeface(typeface);

        // Views
        mEmailField = findViewById(R.id.email);
        mPasswordField = findViewById(R.id.password);
        TextView mSignInButton = findViewById(R.id.normal_login_button);
        TextView mRegButton = findViewById(R.id.register_now);//forward to Reg_Activity
        mRegButton.setOnClickListener( new View.OnClickListener()
           {
               @Override
               public void onClick(View view) {
                   senttoreg();//Intent
               }
           }
        );
        mLoginProgress = new ProgressDialog(this);
        // printKeyHash();

        // Click listeners
        mSignInButton.setOnClickListener(this);

    }

    private void senttoreg() {
        Intent mainIntent = new Intent( LoginActivity.this , RegisterActivity.class );
        startActivity(mainIntent);
        //finish();//unable to return
    }
    @Override
    public void onStart() {
        super.onStart();


    }


    private void checkFBLogin(String id, String email, String name, String profilePicUrl) {
        Toast.makeText(LoginActivity.this, "Welcome : " + name , Toast.LENGTH_SHORT).show();

    }



    public ProgressDialog mProgressDialog;
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading....");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    private void printKeyHash() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature  : packageInfo.signatures)
            {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.e("KEYHASH", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void signIn_Auth() {
        Log.d(TAG, "signIn");//Log Details
        String email = mEmailField.getText().toString().trim();
        String password = mPasswordField.getText().toString().trim();
        if (validateUsername(email) && validatePassword(password)) { //Username and Password Validation {
            showProgressDialog();


            UserAPIService service = RetrofitApi.retrofitWriteWP3AUTH().create(UserAPIService.class);


            Call<UserWp> call = service.userLoginWP(email, password);

            call.enqueue(new Callback<UserWp>() {
                @Override
                public void onResponse(Call<UserWp> call, Response<UserWp> response) {

                    if(response.body() != null) {
                        if (response.body().getUserEmail() != null) {
                            Log.d(TAG, "onResponse: " + response.body().getUserNicename());
                            //finish();
                            User user = new User();

                            user.setDisplay_name(response.body().getUserDisplayName());
                            user.setUser_email(response.body().getUserEmail());
                            user.setUser_nicename(response.body().getUserEmail());
                            user.setUser_email(response.body().getUserNicename());
                            user.setToken(response.body().getToken());

                            UserAPIService service = RetrofitApi.retrofitRead().create(UserAPIService.class);
                            Call<UserResponse> calle = service.usergetID(response.body().getUserEmail());

                            calle.enqueue(new Callback<UserResponse>() {
                                @Override
                                public void onResponse(Call<UserResponse> calle, Response<UserResponse> response) {

                                    if (!response.body().getError()) {

                                        user.setID(response.body().getUser().getID());

                                        Log.d(TAG, "onResponse: ID " + response.body().getUser().getID());

                                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                        hideProgressDialog();
                                        Toasty.success(LoginActivity.this, getResources().getString(R.string.login_successful), Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                        finish();
                                        /*
                                        UserAPIService service = RetrofitApi.retrofitReadWP3Product("customers/" +response.body().getUser().getID()).create(UserAPIService.class);
                                        Call<Customer> call = service.getWP3SingleCustomer(response.body().getUser().getID());
                                        // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
                                        call.enqueue(new Callback<Customer>() {
                                            @Override
                                            public void onResponse(Call<Customer> call, Response<Customer> response) {
                                                if(response.body() != null) {


                                                    hideProgressDialog();
                                                    Log.d("Customer", "onResponse: " + response.body());
                                                    SharedPrefManager.getInstance(getApplicationContext()).CustomerLogin(response.body());
                                                    SharedPrefManager.getInstance(getApplicationContext()).CustomerBilling(response.body().getBilling());
                                                    SharedPrefManager.getInstance(getApplicationContext()).CustomerShipping(response.body().getShipping());
                                                    Toasty.success(LoginActivity.this, getResources().getString(R.string.login_successful), Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                                    finish();

                                                }

                                            }

                                            @Override
                                            public void onFailure(Call<Customer> call, Throwable t) {
                                                hideProgressDialog();
                                                Log.d("Accessory", "onFailure: " + t.getLocalizedMessage());
                                                Toasty.error(LoginActivity.this,getResources().getString(R.string.string_some_thing_wrong), Toast.LENGTH_SHORT,true).show();

                                            }
                                        });

                                         */



                                    } else {
                                        hideProgressDialog();
                                        Toasty.error(LoginActivity.this,getResources().getString(R.string.string_some_thing_wrong), Toast.LENGTH_SHORT,true).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<UserResponse> calle, Throwable t) {
                                    Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                                    hideProgressDialog();
                                    Toasty.error(LoginActivity.this,getResources().getString(R.string.string_some_thing_wrong), Toast.LENGTH_SHORT,true).show();
                                }
                            });


                        } else {
                            hideProgressDialog();
                            Toasty.error(LoginActivity.this,getResources().getString(R.string.invalid_email_and_password), Toast.LENGTH_SHORT,true).show();

                        }
                    }
                    else
                    {
                        hideProgressDialog();
                        Toasty.error(LoginActivity.this,getResources().getString(R.string.string_internet_connection_not_available), Toast.LENGTH_SHORT,true).show();
                    }
                }

                @Override
                public void onFailure(Call<UserWp> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                    hideProgressDialog();
                    Toasty.error(LoginActivity.this,getResources().getString(R.string.string_some_thing_wrong), Toast.LENGTH_SHORT,true).show();
                   // Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }


    }



    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mEmailField.getText().toString())) {
            mEmailField.setError(getString(R.string.required));
            result = false;
        } else {
            mEmailField.setError(null);
        }

        if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
            mPasswordField.setError(getString(R.string.required));
            result = false;
        } else {
            mPasswordField.setError(null);
        }

        return result;
    }

    private boolean validatePassword(String pass) {


        if (pass.length() < 4 || pass.length() > 20) {
            mPasswordField.setError("Password Must consist of 4 to 20 characters");
            return false;
        }
        return true;
    }

    private boolean validateUsername(String email) {

        if (email.length() < 4 || email.length() > 30) {
            mEmailField.setError("Email Must consist of 4 to 30 characters");
            return false;
        } else if (!email.matches("^[A-za-z0-9.@]+")) {
            mEmailField.setError("Only . and @ characters allowed");
            return false;
        } else if (!email.contains("@") || !email.contains(".")) {
            mEmailField.setError("Email must contain @ and .");
            return false;
        }
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Login CheckPoint","LoginActivity resumed");
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

    }

    @Override
    protected void onStop () {
        super.onStop();
        Log.e("Login CheckPoint","LoginActivity stopped");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*
        Intent loginIntent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(loginIntent);
        finish();

         */
    }
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.normal_login_button) {
            signIn_Auth();
        }
    }
}
