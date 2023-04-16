package com.mohaa.dokan.Auth;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
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


import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends BaseActivity {

    private EditTextRegular edtname, edtemail, edtpass , edtcnfpass;
    private String check,name,email,password;
    public static final String TAG = "RegisterActivity";
    private ImageView appLogo;
    private ProgressDialog mLoginProgress;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
        appLogo= findViewById(R.id.logo);
        YoYo.with(Techniques.Bounce)
                .duration(7000)
                .playOn(findViewById(R.id.logo));

        YoYo.with(Techniques.FadeInUp)
                .duration(5000)
                .playOn(findViewById(R.id.logo));

        //Progress Dialog
        mLoginProgress = new ProgressDialog(this);



        edtname = findViewById(R.id.etUsername);
        edtemail = findViewById(R.id.etEmail);
        edtpass = findViewById(R.id.etPass);
        edtcnfpass = findViewById(R.id.etConfirmPass);

        edtname.addTextChangedListener(nameWatcher);
        edtemail.addTextChangedListener(emailWatcher);
        edtpass.addTextChangedListener(passWatcher);



        //validate user details and register user

        TextView button =findViewById(R.id.register);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO AFTER VALDATION
                if ( validateName() && validateEmail() && validatePass()){

                    name=edtname.getText().toString().trim();
                    email=edtemail.getText().toString().trim();
                    password=edtpass.getText().toString().trim();


                    if (!TextUtils.isEmpty(name)) {
                        signUp(email  , password , "Male");
                    }
                    //Validation Success
                    //convertBitmapToString(profilePicture);

                }
            }
        });

        //Take already registered user to login page

        final TextView loginuser=findViewById(R.id.login_now);
        loginuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
               // finish();
            }
        });

        //take user to reset password




    }

    private void sendRegistrationEmail(final String name, final String emails) {



    }
    @Override
    public void onStart() {
        super.onStart();

        // Check auth on Activity start

    }







    private boolean validatePass() {


        check = edtpass.getText().toString();

        if(edtpass.getText().toString().equals(edtcnfpass.getText().toString())) {
            if (check.length() < 4 || check.length() > 20) {
                return false;
            } else if (!check.matches("^[A-za-z0-9@]+")) {
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean validateEmail() {

        check = edtemail.getText().toString();

        if (check.length() < 4 || check.length() > 40) {
            return false;
        } else if (!check.matches("^[A-za-z0-9.@]+")) {
            return false;
        } else if (!check.contains("@") || !check.contains(".")) {
            return false;
        }

        return true;
    }

    private boolean validateName() {

        check = edtname.getText().toString();

        return !(check.length() < 4 || check.length() > 20);

    }

    //TextWatcher for Name -----------------------------------------------------

    TextWatcher nameWatcher = new TextWatcher() {
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

            if (check.length() < 4 || check.length() > 20) {
               // edtname.setError("Name Must consist of 4 to 20 characters");
            }
        }

    };

    //TextWatcher for Email -----------------------------------------------------

    TextWatcher emailWatcher = new TextWatcher() {
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

            if (check.length() < 4 || check.length() > 40) {
                //edtemail.setError("Email Must consist of 4 to 20 characters");
            } else if (!check.matches("^[A-za-z0-9.@]+")) {
                //edtemail.setError("Only . and @ characters allowed");

            } else if (!check.contains("@") || !check.contains(".")) {
                //edtemail.setError("Enter Valid Email");
            }

        }

    };

    //TextWatcher for pass -----------------------------------------------------

    TextWatcher passWatcher = new TextWatcher() {
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

            if (check.length() < 4 || check.length() > 20) {
                //edtpass.setError("Password Must consist of 4 to 20 characters");
            } else if (!check.matches("^[A-za-z0-9@]+")) {
                //edtemail.setError("Only @ special character allowed");
            }
        }

    };



    @Override
    protected void onResume() {
        super.onResume();
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
    }

    @Override
    protected void onStop () {
        super.onStop();
    }







    private void signUp(final String email , final String password , final String gender) {
        Log.d(TAG, "signUp");


        showProgressDialog();



        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {

                UserAPIService service = RetrofitApi.retrofitWriteRegister().create(UserAPIService.class);
                //Defining the user object as we need to pass it with the call
                // User user = new User(name, password , email);
                Map<String, Object> jsonParams = new ArrayMap<>();
                //put something inside the map, could be null
                jsonParams.put("username", name);
                jsonParams.put("email", email);
                jsonParams.put("password", password);

                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());
                //defining the call
                Call<UserResponse> call = service.createUserWPBody(
                        "/wp/v2/users/register",
                        body
                );

                //calling the api
                call.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {


                        if(response.body() != null) {

                            Log.d(TAG, "onResponse: not null" );
                            if(response.body().getMessage().contains("Registration was Successful"))
                            {

                                //Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                                UserAPIService service = RetrofitApi.retrofitWriteWP3AUTH().create(UserAPIService.class);


                                Call<UserWp> callz = service.userLoginWP(email, password);

                                callz.enqueue(new Callback<UserWp>() {
                                    @Override
                                    public void onResponse(Call<UserWp> call, Response<UserWp> response) {

                                        if(response.body() != null) {
                                            if (response.body().getUserEmail() != null) {
                                                Log.d(TAG, "onResponse: " + response.body().getUserNicename());
                                                User user = new User();

                                                user.setDisplay_name(response.body().getUserDisplayName());
                                                user.setUser_email(response.body().getUserEmail());
                                                user.setUser_nicename(response.body().getUserEmail());
                                                user.setUser_email(response.body().getUserNicename());
                                                user.setToken(response.body().getToken());
                                                UserAPIService service = RetrofitApi.retrofitRead().create(UserAPIService.class);


                                                Call<UserResponse> calle = service.usergetID(email);

                                                calle.enqueue(new Callback<UserResponse>() {
                                                    @Override
                                                    public void onResponse(Call<UserResponse> calle, Response<UserResponse> response) {
                                                        hideProgressDialog();
                                                        if (!response.body().getError()) {

                                                            user.setID(response.body().getUser().getID());
                                                            hideProgressDialog();
                                                            Log.d(TAG, "onResponse: ID " + response.body().getUser().getID());
                                                            Toasty.success(RegisterActivity.this, getResources().getString(R.string.registration_was_successful), Toast.LENGTH_SHORT).show();
                                                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                                            hideProgressDialog();
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
                                                                        Toasty.success(RegisterActivity.this, getResources().getString(R.string.login_successful), Toast.LENGTH_SHORT).show();
                                                                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                                                        finish();

                                                                    }

                                                                }

                                                                @Override
                                                                public void onFailure(Call<Customer> call, Throwable t) {
                                                                    hideProgressDialog();
                                                                    Log.d("Accessory", "onFailure: " + t.getLocalizedMessage());
                                                                    Toasty.error(RegisterActivity.this,getResources().getString(R.string.string_some_thing_wrong), Toast.LENGTH_SHORT,true).show();

                                                                }
                                                            });

                                                            //hiding progress dialog

                                                             */


                                                        } else {
                                                            hideProgressDialog();
                                                            Toasty.error(RegisterActivity.this,getResources().getString(R.string.string_some_thing_wrong), Toast.LENGTH_SHORT,true).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<UserResponse> calle, Throwable t) {
                                                        Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                                                        hideProgressDialog();
                                                        Toasty.error(RegisterActivity.this,getResources().getString(R.string.string_some_thing_wrong), Toast.LENGTH_SHORT,true).show();
                                                    }
                                                });


                                            } else {
                                                hideProgressDialog();
                                                Toasty.error(RegisterActivity.this,getResources().getString(R.string.invalid_email_and_password), Toast.LENGTH_SHORT,true).show();

                                            }
                                        }
                                        else
                                        {
                                            hideProgressDialog();
                                            Toasty.error(RegisterActivity.this,getResources().getString(R.string.string_internet_connection_not_available), Toast.LENGTH_SHORT,true).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<UserWp> call, Throwable t) {
                                        Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                                        hideProgressDialog();
                                        Toasty.error(RegisterActivity.this,getResources().getString(R.string.string_some_thing_wrong), Toast.LENGTH_SHORT,true).show();
                                    }
                                });

                            }
                            else
                            {
                                //hiding progress dialog
                                hideProgressDialog();
                            }


                            //TODO
                            /*
                            finish();
                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body().getUser());
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                             */
                        }
                        else
                        {
                            Log.d(TAG, "onResponse:  null : " + response.code() + "|" + response.errorBody() );
                            hideProgressDialog();
                        }

                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        hideProgressDialog();
                        Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });




            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
            }
        });



    }



    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(edtemail.getText().toString())) {
            edtemail.setError(getString(R.string.required));
            result = false;
        } else {
            edtemail.setError(null);
        }

        if (TextUtils.isEmpty(edtpass.getText().toString())) {
            edtpass.setError(getString(R.string.required));
            result = false;
        } else {
            edtpass.setError(null);
        }


        return result;
    }



}


