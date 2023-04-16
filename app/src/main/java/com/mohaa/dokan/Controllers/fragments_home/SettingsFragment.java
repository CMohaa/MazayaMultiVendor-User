package com.mohaa.dokan.Controllers.fragments_home;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.mohaa.dokan.Auth.LoginActivity;
import com.mohaa.dokan.Controllers.activities_popup.CartReadyActivity;
import com.mohaa.dokan.Controllers.activities_popup.MyAddressActivity;
import com.mohaa.dokan.HomeActivity;
import com.mohaa.dokan.MainActivity;
import com.mohaa.dokan.R;
import com.mohaa.dokan.Utils.LocaleHelper;
import com.mohaa.dokan.Utils.helper.SharedPrefManager;
import com.mohaa.dokan.manager.ApiServices.UserAPIService;
import com.mohaa.dokan.manager.CustomerBase;
import com.mohaa.dokan.manager.OrdersBase;
import com.mohaa.dokan.manager.RetrofitApi;
import com.mohaa.dokan.models.wp.Customer;
import com.mohaa.dokan.networksync.CheckInternetConnection;

import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private LinearLayout profile_panel;
    private LinearLayout cart_panel;
    private LinearLayout wishList_panel;
    private LinearLayout language_panel;
    protected Button logoutButton;
    private ImageView whatsapp , facebook , instigram;
    private TextView profile_edit;
    int cartCount = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings , container , false);

        //check Internet Connection
        new CheckInternetConnection(getContext()).checkConnection();

        whatsapp = view.findViewById(R.id.whatsapp);
        facebook = view.findViewById(R.id.facebook);
        instigram = view.findViewById(R.id.instigram);
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnWhatsApp();
            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebookIntent = getFBIntent(getContext(), "ELDokan97");
                startActivity(facebookIntent);
            }
        });
        instigram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://instagram.com/mohamed.elareeg");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/mohamed.elareeg")));
                }
            }
        });

        profile_panel = view.findViewById(R.id.profile_panel);
        cart_panel = view.findViewById(R.id.cart_panel);
        wishList_panel = view.findViewById(R.id.wish_list_panel);
        language_panel = view.findViewById(R.id.language_panel);

        profile_edit = view.findViewById(R.id.edit_profile);
        profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        logoutButton = view.findViewById(R.id.btnLogout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(getActivity()).logout();
               getActivity().finish();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        init();
        getCustomers(SharedPrefManager.getInstance(getContext()).getUser().getID());
        return view;
    }
    public void getCustomers(int id)
    {


        UserAPIService service = RetrofitApi.retrofitReadWP3Product("customers/" +id).create(UserAPIService.class);
        Call<Customer> call = service.getWP3SingleCustomer(id);
        // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if(response.body() != null) {

                    boolean inserted = CustomerBase.getInstance().InsertOrder(response.body());
                    Log.d("Customer", "Customer: " + response.body().getEmail());

                }




            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Log.d("Accessory", "onFailure: " + t.getLocalizedMessage());


            }
        });

    }
    public Intent getFBIntent(Context context, String facebookId) {

        try {
            // Check if FB app is even installed
            context.getPackageManager().getPackageInfo("com.facebook.orca", 0);

            String facebookScheme = "http://m.me/" + facebookId;
            return new Intent(Intent.ACTION_VIEW, Uri.parse(facebookScheme));
        }
        catch(Exception e) {

            // Cache and Open a url in browser
            String facebookProfileUri = "http://m.me/" + facebookId;
            return new Intent(Intent.ACTION_VIEW, Uri.parse(facebookProfileUri));
        }

    }

    private void OnWhatsApp() {
        try {
            Uri uri = Uri.parse("smsto:" + "+201277637646");
            Intent i = new Intent(Intent.ACTION_SENDTO, uri);
            i.putExtra("sms_body", "");
            i.setPackage("com.whatsapp");
            startActivity(i);
        }
        catch (Exception e)
        {

        }

    }
    public void sendtoLogin()
    {
        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
        startActivity(loginIntent);
       // getActivity().finish();



    }
    @Override
    public void onResume() {
        super.onResume();
        new CheckInternetConnection(getContext()).checkConnection();
        if (!SharedPrefManager.getInstance(getContext()).isLoggedIn()) {
            sendtoLogin();

        }
        cartCount = OrdersBase.getInstance().getmOrders().size();


        super.onResume();
    }


    private void init() {


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String data = prefs.getString("language", ""); //no id: default value
        profile_panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent loginIntent = new Intent(getActivity(), MyAddressActivity.class);
                startActivity(loginIntent);
            }
        });
        cart_panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cartCount > 0 ) {
                    Intent loginIntent = new Intent(getContext(), CartReadyActivity.class);
                    startActivity(loginIntent);
                }
                else {
                    Toast.makeText(getContext(), getResources().getString(R.string.cart_is_empty), Toast.LENGTH_SHORT).show();
                }

            }
        });
        wishList_panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        language_panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(data.equals("en"))
                {
                    LocaleHelper.setLocale(getActivity(), "ar");
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    storeLanguageInPref("ar");
                    getActivity().finish();
                }
                else if(data.equals("ar"))
                {
                    LocaleHelper.setLocale(getActivity(), "en");
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    storeLanguageInPref("en");
                    getActivity().finish();
                }
                else {
                    LocaleHelper.setLocale(getActivity(), "ar");
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    storeLanguageInPref("ar");
                    getActivity().finish();
                }


            }
        });

    }
    //SharedPreferences  language = getActivity().getSharedPreferences("language",MODE_PRIVATE);
    private void storeLanguageInPref(String language) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("language", language);
        editor.apply();
    }

}
