package com.mohaa.dokan.Controllers.fragments_home;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.mohaa.dokan.Auth.LoginActivity;
import com.mohaa.dokan.Controllers.activities_orders.TrackActivity;
import com.mohaa.dokan.R;
import com.mohaa.dokan.Utils.EndlessRecyclerViewScrollListener;
import com.mohaa.dokan.Utils.GridSpacingItemDecoration;
import com.mohaa.dokan.Utils.ProductsUI;
import com.mohaa.dokan.Utils.helper.SharedPrefManager;
import com.mohaa.dokan.Utils.shimmer.ShimmerFrameLayout;
import com.mohaa.dokan.interfaces.OnOrderClickListener;
import com.mohaa.dokan.manager.ApiServices.OrdersAPIService;
import com.mohaa.dokan.manager.RetrofitApi;
import com.mohaa.dokan.models.Order;
import com.mohaa.dokan.networksync.CheckInternetConnection;
import com.mohaa.dokan.views.MyOrdersWPAdapter;

import java.io.Serializable;
import java.util.ArrayList;

import static com.mohaa.dokan.Controllers.fragments_home.HomeFragment.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends Fragment implements OnOrderClickListener {


    public OrdersFragment() {
        // Required empty public constructor
    }
    private int userPage = 1;
    private Menu menu;
    private ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView recList;
    private int products_num = 5;
    private ArrayList<com.mohaa.dokan.models.wp.Order> orders_list;
    private RecyclerView orders_recyclerView;
    private MyOrdersWPAdapter ordersAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders , container , false);

        recList = (RecyclerView) view.findViewById(R.id.recyclerview);
        shimmerFrameLayout = view.findViewById(R.id.parentShimmerLayout);
        shimmerFrameLayout.startShimmer();
        orders_list = new ArrayList<>();
        ordersAdapter = new MyOrdersWPAdapter(orders_list , this);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recList.setLayoutManager(mLayoutManager);
        recList.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recList.setItemAnimator(new DefaultItemAnimator());
        recList.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                userPage++;
                getData(userPage , "Most Recent");
            }
        });
        recList.setAdapter(ordersAdapter);
        if (SharedPrefManager.getInstance(getContext()).isLoggedIn()) {
            getData(1 , "Most Recent");

        }
        return view;
    }
    //Responsible For Adding the 3 tabs : Camera  , Home , Messages
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onStart() {
        super.onStart();
        new CheckInternetConnection(getContext()).checkConnection();
        if (!SharedPrefManager.getInstance(getContext()).isLoggedIn()) {
            sendtoLogin();

        }

    }
    public void sendtoLogin()
    {
        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
        startActivity(loginIntent);
        getActivity().finish();



    }

    @Override
    public void onResume() {
        //check Internet Connection

        new CheckInternetConnection(getContext()).checkConnection();
        if (!SharedPrefManager.getInstance(getContext()).isLoggedIn()) {
            sendtoLogin();

        }
        super.onResume();
    }
    public void getData(int userPage , String _sortById){
        try {
            //swipeRefreshLayout.setRefreshing(true);
            getAllOrders(userPage ,_sortById );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getAllOrders( int pagenumbr , String sortById)
    {
        //Toast.makeText(this, "SSS", Toast.LENGTH_SHORT).show();

        int userID = SharedPrefManager.getInstance(getActivity()).getUser().getID();
        OrdersAPIService service = RetrofitApi.retrofitReadWP3OrdersByCustomer("orders" , String.valueOf(userID), "date", String.valueOf(pagenumbr)).create(OrdersAPIService.class);


        Call<ArrayList<com.mohaa.dokan.models.wp.Order>> call = service.getWP3Orders();
        // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
        call.enqueue(new Callback<ArrayList<com.mohaa.dokan.models.wp.Order>>() {
            @Override
            public void onResponse(Call<ArrayList<com.mohaa.dokan.models.wp.Order>> call, Response<ArrayList<com.mohaa.dokan.models.wp.Order>> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);

                // Toast.makeText(ProductsActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();


                // Log.d("onFailure:",  "ProductName:"+response.body().getProducts().get(1).getTitle());

                if(response.body() != null)
                {
                    orders_list.addAll(response.body());
                    ordersAdapter.notifyDataSetChanged();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    shimmerFrameLayout.stopShimmer();

                }






            }

            @Override
            public void onFailure(Call<ArrayList<com.mohaa.dokan.models.wp.Order>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());


            }
        });
    }
    public void getAllOrders_old(int userPage , String sortById)
    {
        /*
        OrdersAPIService service = RetrofitApi.retrofitRead().create(OrdersAPIService.class);
        Call<Orders> call = service.getUserOrders(SharedPrefManager.getInstance(getContext()).getUser().getID() , userPage , 1 );
        call.enqueue(new Callback<Orders>() {
            @Override
            public void onResponse(Call<Orders> call, Response<Orders> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);
                if(response.body() != null) {

                    orders_list.addAll(response.body().getOrders());
                    ordersAdapter.notifyDataSetChanged();

                }



            }

            @Override
            public void onFailure(Call<Orders> call, Throwable t) {

            }
        });

         */

    }
    @Override
    public void onOrderClicked(Order contact, int position) {
        Intent loginIntent = new Intent(getContext(), TrackActivity.class);
        loginIntent.putExtra(ProductsUI.BUNDLE_ORDERS_LIST, (Serializable) contact);
        //loginIntent.putExtra(ProductsUI.BUNDLE_ORDER_ID,contact.getId());
       // loginIntent.putExtra(ProductsUI.BUNDLE_TOTAL_COST, String.valueOf(contact.getTotal_cost()));
       // loginIntent.putExtra(ProductsUI.BUNDLE_ORDER_STATE, contact.getState());
        startActivity(loginIntent);
    }

    @Override
    public void onOrderClicked(com.mohaa.dokan.models.wp.Order contact, int position) {
        Intent loginIntent = new Intent(getContext(), TrackActivity.class);
        loginIntent.putExtra(ProductsUI.BUNDLE_ORDERS_LIST, (Serializable) contact);
        //loginIntent.putExtra(ProductsUI.BUNDLE_ORDER_ID,contact.getId());
        // loginIntent.putExtra(ProductsUI.BUNDLE_TOTAL_COST, String.valueOf(contact.getTotal_cost()));
        // loginIntent.putExtra(ProductsUI.BUNDLE_ORDER_STATE, contact.getState());
        startActivity(loginIntent);
    }
}
