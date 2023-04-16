package com.mohaa.dokan.Utils.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mohaa.dokan.models.User;
import com.mohaa.dokan.models.wp.Customer;


public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static Context mCtx;
    // Shared Preferences
    SharedPreferences pref;
    private static final String SHARED_PREF_NAME = "dokanusers";

    private static final String KEY_USER_ID = "keyuserid";
    private static final String KEY_USER_NAME = "keyusername";
    private static final String KEY_USER_EMAIL = "keyuseremail";
    private static final String KEY_USER_TOKEN = "keyusertoken";
    private static final String KEY_USER_NICE_NAME = "keyusernicename";
    private static final String KEY_CUSTOMER = "customerinfo";
    private static final String KEY_CUSTOMER_BILLING = "customerbilling";
    private static final String KEY_CUSTOMER_SHIPPING = "customershipping";
    private static final String KEY_LAST_PRODUCT = "lastproduct";
    private static final String KEY_NOTIFICATIONS = "notifications";

    public SharedPrefManager(Context context) {
        mCtx = context;
        pref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_ID, user.getID());
        editor.putString(KEY_USER_NAME, user.getDisplay_name());
        editor.putString(KEY_USER_EMAIL, user.getUser_email());
        editor.putString(KEY_USER_TOKEN, user.getToken());
        editor.putString(KEY_USER_NICE_NAME, user.getUser_nicename());
        editor.apply();
        return true;
    }

    public boolean CustomerLogin(Customer customer) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(customer);
        editor.putString(KEY_CUSTOMER, json);
        editor.apply();
        return true;
    }
    public boolean CustomerBilling(Customer.Billing customer) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(customer);
        editor.putString(KEY_CUSTOMER_BILLING, json);
        editor.apply();
        return true;
    }
    public boolean CustomerShipping(Customer.Shipping customer) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(customer);
        editor.putString(KEY_CUSTOMER_SHIPPING, json);
        editor.apply();
        return true;
    }
    public boolean userToken(String token) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_TOKEN, token);
        editor.apply();
        return true;
    }
    public boolean userViewed(int category_id) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_LAST_PRODUCT, category_id);
        editor.apply();
        return true;
    }
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_USER_EMAIL, null) != null)
            return true;
        return false;
    }

    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_USER_ID, 0),
                sharedPreferences.getString(KEY_USER_NICE_NAME, null),
                sharedPreferences.getString(KEY_USER_EMAIL, null),
                sharedPreferences.getString(KEY_USER_NAME, null),
                sharedPreferences.getString(KEY_USER_TOKEN, null)
        );
    }
    public int getLastViewed() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_LAST_PRODUCT, 0);
    }
    public Customer getCustomer() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_CUSTOMER, null );
        Customer customer = gson.fromJson(json, Customer.class);
        return customer;
    }
    public Customer.Billing getCustomerBilling() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_CUSTOMER_BILLING, null );
        Customer.Billing customer = gson.fromJson(json, Customer.Billing.class);
        return customer;
    }
    public Customer.Shipping getCustomerShipping() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_CUSTOMER_SHIPPING, null );
        Customer.Shipping customer = gson.fromJson(json, Customer.Shipping.class);
        return customer;
    }
    public void addNotification(String notification) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // get old notifications
        String oldNotifications = getNotifications();

        if (oldNotifications != null) {
            oldNotifications += "|" + notification;
        } else {
            oldNotifications = notification;
        }

        editor.putString(KEY_NOTIFICATIONS, oldNotifications);
        editor.commit();
    }

    public String getNotifications() {
        return pref.getString(KEY_NOTIFICATIONS, null);
    }
    public boolean logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }
}
