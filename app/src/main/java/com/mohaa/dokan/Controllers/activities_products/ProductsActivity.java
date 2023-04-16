package com.mohaa.dokan.Controllers.activities_products;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mohaa.dokan.Controllers.BaseActivity;
import com.mohaa.dokan.Controllers.activities_popup.SearchActivity;
import com.mohaa.dokan.Controllers.fragments_products.CartBottomSheetFragment;
import com.mohaa.dokan.R;
import com.mohaa.dokan.Utils.CartInfoBar;
import com.mohaa.dokan.Utils.EndlessRecyclerViewScrollListener;
import com.mohaa.dokan.Utils.GridSpacingItemDecoration;
import com.mohaa.dokan.Utils.ProductsUI;

import com.mohaa.dokan.Utils.shimmer.ShimmerFrameLayout;
import com.mohaa.dokan.interfaces.OnCallbackReceived;
import com.mohaa.dokan.interfaces.OnCataClickListener;
import com.mohaa.dokan.interfaces.OnProductClickListener;
import com.mohaa.dokan.manager.ApiServices.CategoryAPIService;
import com.mohaa.dokan.manager.ApiServices.CustomerAPIService;
import com.mohaa.dokan.manager.ApiServices.ProductsAPIService;
import com.mohaa.dokan.manager.ApiServices.VariantsAPIService;
import com.mohaa.dokan.manager.Call.Variants;
import com.mohaa.dokan.manager.OrdersBase;
import com.mohaa.dokan.manager.RetrofitApi;
import com.mohaa.dokan.models.CartItem;
import com.mohaa.dokan.models.PendingProduct;
import com.mohaa.dokan.models.Variant;
import com.mohaa.dokan.models.wp.ProductCategory;
import com.mohaa.dokan.networksync.CheckInternetConnection;
import com.mohaa.dokan.views.CartProductsAdapter;
import com.mohaa.dokan.views.FilterItemListAdapter;
import com.mohaa.dokan.views.ProductsWPAdapter;
import com.mohaa.dokan.views.SortItemListAdapter;
import com.mohaa.dokan.views.SubCategoriesWPAdapter;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsActivity extends BaseActivity implements OnProductClickListener, OnCataClickListener  , OnCallbackReceived , CartProductsAdapter.CartProductsAdapterListener {



    private Menu menu;
    private static final String TAG = "ProductsActivity";
    private RecyclerView recList;
    private ArrayList<com.mohaa.dokan.models.wp.Product> products_list;
    private RecyclerView products_recyclerView;
    private StaggeredGridLayoutManager products_staggeredGridLayoutManager;
    private ProductsWPAdapter products_listAdapter;
    private ProductCategory category;

    private int userPage = 1;

    TableLayout sortFilter;
    RelativeLayout sort, filter;
    TextView sortByText;
    Map<Integer , String> sortBy = new HashMap<>();
    //String[] sortByArray = {"Most Recent", "Most Order", "Top Rated", "Most Viewed"};
    int sortById = 0;
    List<String> departmentFilter = new ArrayList<>();
    List<String> companyFilter = new ArrayList<>();
    List<String> packFilter = new ArrayList<>();
    List<String> statusFilter = new ArrayList<>();

    private String type;
    //

    private ArrayList<ProductCategory> categoeries_list;
    private SubCategoriesWPAdapter categoriesAdapte;
    private RecyclerView categories_recycleView;


    private List<CartItem> cartItems;
    private CartInfoBar cartInfoBar;

    private ShimmerFrameLayout shimmerFrameLayout , ShimmerLayoutcategory;
    OnCallbackReceived mCallback;
    private Integer category_id;
    //

    private int selectedKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);



        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
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
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setDisplayShowHomeEnabled(true);

        mCallback =  ProductsActivity.this;

        init();
    }


    private void init() {

        //String[] sortByArray = {"Most Recent", "Most Order", "Top Rated", "Most Viewed"};
        sortBy.put(0 , getResources().getString(R.string.most_recent));
        sortBy.put(1 , getResources().getString(R.string.most_order));
        sortBy.put(2 , getResources().getString(R.string.top_rated));
        sortBy.put(3 , getResources().getString(R.string.most_viewed));
        selectedKey = 0;
        shimmerFrameLayout = findViewById(R.id.parentShimmerLayout);
        ShimmerLayoutcategory = findViewById(R.id.parentShimmerLayoutcategory);

        ShimmerLayoutcategory.startShimmer();
        shimmerFrameLayout.startShimmer();
        category_id = getIntent().getIntExtra(ProductsUI.BUNDLE_PRODUCTS_LIST_CATEGORY , 0);
        type = getIntent().getStringExtra(ProductsUI.BUNDLE_PRODUCTS_LIST_TYPE);
        category = (ProductCategory) getIntent().getExtras().getSerializable(ProductsUI.BUNDLE_PRODUCTS_LIST);
        recList = (RecyclerView) findViewById(R.id.recyclerview);
        products_list = new ArrayList<>();

        sortFilter = findViewById(R.id.sortFilter);
        products_listAdapter = new ProductsWPAdapter(products_list , this , this);

        cartInfoBar = findViewById(R.id.cart_info_bar);
        init_bar();

        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recList.setLayoutManager(mLayoutManager);
        recList.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recList.setItemAnimator(new DefaultItemAnimator());
        recList.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                userPage++;
                if(category == null)
                {
                    getData( type  ,  userPage, sortBy.get(selectedKey));
                }
                else {
                    getData(type  , userPage , sortByText.getText().toString());
                }

            }
        });
        recList.setAdapter(products_listAdapter);

        sort = findViewById(R.id.sortLay);
        filter = findViewById(R.id.filterLay);
        sortByText = findViewById(R.id.sortBy);

        setSortListener();
        setFilterListener();
        sortByText.setText(sortBy.get(selectedKey));



        //
        categoeries_list = new ArrayList<>();
        categoriesAdapte = new SubCategoriesWPAdapter(categoeries_list ,this);
        categories_recycleView = findViewById(R.id.slider_cata);

        LinearLayoutManager cataManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        categories_recycleView.setItemAnimator(new DefaultItemAnimator());
        categories_recycleView.setLayoutManager(cataManager);
        categories_recycleView.setAdapter(categoriesAdapte);
        if(category == null)
        {
            sortFilter.setVisibility(View.GONE);
           // Toast.makeText(this, "EEE", Toast.LENGTH_SHORT).show();
            getData(type  ,1 , sortBy.get(selectedKey));
            getSubCategory(category_id);
        }
        else
        {
            getData(type  ,1 , sortByText.getText().toString());
            sortFilter.setVisibility(View.VISIBLE);

            getSubCategory(category.getId());
        }
       // getCustomers();
       // getProducts();
       // getCustomer();

    }

    private int dpToPx_(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, dp, r.getDisplayMetrics()));
    }
    //Responsible For Adding the 3 tabs : Camera  , Home , Messages
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    @Override
    protected void onResume() {
        super.onResume();
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
        //mShimmerViewContainer.startShimmer();
        // Update Cart Count
        init_bar();

    }
    public void getAllPosts(String type , int pagenumbr , String sortById)
    {

        ProductsAPIService service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "price", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
        // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
        switch (type) {
            case "Main":
            case "Group":
            case "Sub":


                switch (selectedKey) {
                    case 0:

                        service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "date" , String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                        break;

                    case 1:

                        service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "menu_order", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                        break;

                    case 2:

                        service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "rating", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                        break;
                    case 3:

                        service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "popularity", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                        break;


                    default:
                    {
                        service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "date", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                        break;
                    }
                }
                break;
            case "like":

                service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category_id), "popularity", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                break;
            case "recently":

                Toast.makeText(this, "TTT", Toast.LENGTH_SHORT).show();
                service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "date", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                break;

            case "sponsored":
                Toast.makeText(this, "TTT", Toast.LENGTH_SHORT).show();
                service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "title", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                break;


            case "suggest":
                Toast.makeText(this, "TTT", Toast.LENGTH_SHORT).show();
                service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "rating", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                break;

            case "viewed":
                Toast.makeText(this, "TTT", Toast.LENGTH_SHORT).show();
                service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "popularity", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                break;

            default:
            {
                service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "date", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                break;
            }
        }


        Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call = service.getWP3Products();
        // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
        call.enqueue(new Callback<ArrayList<com.mohaa.dokan.models.wp.Product>>() {
            @Override
            public void onResponse(Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call, Response<ArrayList<com.mohaa.dokan.models.wp.Product>> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);

                // Toast.makeText(ProductsActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();


                // Log.d("onFailure:",  "ProductName:"+response.body().getProducts().get(1).getTitle());

                if(response.body() != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            products_list.addAll(response.body());
                            products_listAdapter.notifyDataSetChanged();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            shimmerFrameLayout.stopShimmer();
                        }
                    }).run();
                }






            }

            @Override
            public void onFailure(Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());


            }
        });
    }
    public void getAllPostsAlike(String type , int pagenumbr , String sortById)
    {
        //Toast.makeText(this, "SSS", Toast.LENGTH_SHORT).show();

        ProductsAPIService service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category_id), "popularity", String.valueOf(pagenumbr)).create(ProductsAPIService.class);


        Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call = service.getWP3Products();
        // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
        call.enqueue(new Callback<ArrayList<com.mohaa.dokan.models.wp.Product>>() {
            @Override
            public void onResponse(Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call, Response<ArrayList<com.mohaa.dokan.models.wp.Product>> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);

                // Toast.makeText(ProductsActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();


                // Log.d("onFailure:",  "ProductName:"+response.body().getProducts().get(1).getTitle());

                if(response.body() != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            products_list.addAll(response.body());
                            products_listAdapter.notifyDataSetChanged();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            shimmerFrameLayout.stopShimmer();
                        }
                    }).run();
                }






            }

            @Override
            public void onFailure(Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());


            }
        });
    }
    public void getAllPosts_Filter(String name ,String value)
    {

    }

    public void getAllPostsHome(String type , int pagenumbr , String sortById)
    {

        ProductsAPIService service = RetrofitApi.retrofitReadWP3ProductsDefault("products" , "rating" , "1").create(ProductsAPIService.class);
        // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
        switch (type) {
            case "recently":

               // Toast.makeText(this, "TTT", Toast.LENGTH_SHORT).show();
                service = RetrofitApi.retrofitReadWP3ProductsDefault("products" , "date" , String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                //service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "date", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                break;

            case "sponsored":
               // Toast.makeText(this, "TTT", Toast.LENGTH_SHORT).show();
                service = RetrofitApi.retrofitReadWP3ProductsDefault("products" , "title" , String.valueOf(pagenumbr)).create(ProductsAPIService.class);
               // service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "title", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                break;


            case "suggest":
              //  Toast.makeText(this, "TTT", Toast.LENGTH_SHORT).show();
                service = RetrofitApi.retrofitReadWP3ProductsDefault("products" , "rating" , String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                //service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "rating", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                break;

            case "viewed":
              //  Toast.makeText(this, "TTT", Toast.LENGTH_SHORT).show();
                service = RetrofitApi.retrofitReadWP3ProductsDefault("products" , "popularity" , String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                //service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "popularity", String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                break;

            default:
            {
                //Toast.makeText(this, "DDD", Toast.LENGTH_SHORT).show();
                service = RetrofitApi.retrofitReadWP3ProductsDefault("products" , "rating" , String.valueOf(pagenumbr)).create(ProductsAPIService.class);
                break;
            }
        }


        Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call = service.getWP3Products();
        // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
        call.enqueue(new Callback<ArrayList<com.mohaa.dokan.models.wp.Product>>() {
            @Override
            public void onResponse(Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call, Response<ArrayList<com.mohaa.dokan.models.wp.Product>> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);

                // Toast.makeText(ProductsActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();


                // Log.d("onFailure:",  "ProductName:"+response.body().getProducts().get(1).getTitle());

                if(response.body() != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            products_list.addAll(response.body());
                            products_listAdapter.notifyDataSetChanged();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            shimmerFrameLayout.stopShimmer();
                        }
                    }).run();
                }






            }

            @Override
            public void onFailure(Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());


            }
        });
    }
    public void getData(String type , int userPage , String _sortById){
        try {
            //swipeRefreshLayout.setRefreshing(true);
            if(category == null && type.equals("like"))
            {

                getAllPostsAlike(type, userPage, _sortById);
            }
            if(category == null && !type.equals("like"))
            {
               // Toast.makeText(this, "" + type, Toast.LENGTH_SHORT).show();
                getAllPostsHome(type, userPage, _sortById);
            }
            else {
               // Toast.makeText(this, "ZZZ", Toast.LENGTH_SHORT).show();
                getAllPosts(type, userPage, _sortById);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }








    public void getProducts()
    {

        ProductsAPIService service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(category.getId()), "price" , "1").create(ProductsAPIService.class);
        Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call = service.getWP3Products();
        // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
        call.enqueue(new Callback<ArrayList<com.mohaa.dokan.models.wp.Product>>() {
            @Override
            public void onResponse(Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call, Response<ArrayList<com.mohaa.dokan.models.wp.Product>> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);

                // Toast.makeText(ProductsActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();


               // Log.d("onFailure:",  "ProductName:"+response.body().getProducts().get(1).getTitle());
                if(response.body() != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            products_list.addAll(response.body());
                            products_listAdapter.notifyDataSetChanged();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            shimmerFrameLayout.stopShimmer();
                        }
                    }).run();
                }
                else {
                    Log.d(TAG, "onResponse: failed product ");
                }





            }

            @Override
            public void onFailure(Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());


            }
        });



    }
    public void getCustomer()
    {
        String customerID = "1";
        CustomerAPIService service = RetrofitApi.retrofitReadWP3Z("customers/" + customerID).create(CustomerAPIService.class);
        Call<com.mohaa.dokan.models.wp.call.Customers> call = service.getWP3Customer( 1);
        // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
        call.enqueue(new Callback<com.mohaa.dokan.models.wp.call.Customers>() {
            @Override
            public void onResponse(Call<com.mohaa.dokan.models.wp.call.Customers> call, Response<com.mohaa.dokan.models.wp.call.Customers> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);

                // Toast.makeText(ProductsActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();


                 Log.d("onFailure:",  "Customer Email:"+response.body().getCustomer().getEmail());
                if(response.body() != null)
                {
                    Log.d(TAG, "onResponse: Succ ");

                }
                else {
                    Log.d(TAG, "onResponse: failed ");
                }





            }

            @Override
            public void onFailure(Call<com.mohaa.dokan.models.wp.call.Customers> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());


            }
        });



    }

    @Override
    public void onProductClicked(com.mohaa.dokan.models.wp.Product contact, int position) {
        //Toast.makeText(this, contact.getProduct_name(), Toast.LENGTH_SHORT).show();
        Intent loginIntent = new Intent(ProductsActivity.this, IndividualProductActivity.class);
        loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST, (Serializable) contact);
       // loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_IMAGE, contact.getFeaturedSrc());
        loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_ID, contact.getId());
        loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_TYPE, contact.getType());
        startActivity(loginIntent);
    }

    // Set Sort Listener
    private void setSortListener() {
        sort.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(View view) {
                // Create Dialog
                final Dialog dialog = new Dialog(ProductsActivity.this);
                dialog.setContentView(R.layout.sort_listview);

                ListView listView = dialog.findViewById(R.id.sort_listview);
                listView.setAdapter(new SortItemListAdapter(ProductsActivity.this, sortBy, sortById));
                listView.setDividerHeight(1);
                listView.setFocusable(true);
                listView.setClickable(true);
                listView.setFocusableInTouchMode(false);
                dialog.show();

                // ListView Click Listener
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        sortById = i;
                        selectedKey = sortById;
                        sortByText.setText(sortBy.get(sortById));

                        // Reload Products List
                        userPage = 1;
                        products_list.clear();
                        products_listAdapter.notifyDataSetChanged();
                        if(category == null)
                        {
                            // 1. First, clear the array of data
                            products_list.clear();
                            // 2. Notify the adapter of the update
                            products_listAdapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
                            shimmerFrameLayout.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.startShimmer();
                            getData( type  ,  1, sortBy.get(selectedKey));
                        }
                        else {
                            // 1. First, clear the array of data
                            products_list.clear();
                            // 2. Notify the adapter of the update
                            products_listAdapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
                            shimmerFrameLayout.setVisibility(View.VISIBLE);
                            shimmerFrameLayout.startShimmer();
                            getData( type   ,  1, sortByText.getText().toString());
                        }

                        dialog.dismiss();
                    }
                });
            }
        });
    }
    private void setFilterListener() {
        filter.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(View view) {
                // Create Dialog

                final Dialog dialog = new Dialog(ProductsActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.filterlayout);

                final List<String> department = getVarients(1);
                final List<String> company = getVarients(2);
                final List<String> pack = getVarients(3);
                final List<String> status= getVarients(4);

                // Add into hash map
                HashMap<String, List<String>> listHashMap = new HashMap<>();
                listHashMap.put("department", department);
                listHashMap.put("company", company);
                listHashMap.put("pack", pack);
                listHashMap.put("status", status);

                // Add Headers
                List<String> headers = new ArrayList<>();
                headers.add("department");
                headers.add("company");
                headers.add("pack");
                headers.add("status");

                final ExpandableListView listView = dialog.findViewById(R.id.expandableList);
                final FilterItemListAdapter filterItemListAdapter = new FilterItemListAdapter(ProductsActivity.this, headers, listHashMap, departmentFilter, companyFilter, packFilter, statusFilter);
                listView.setAdapter(filterItemListAdapter);
                listView.setDividerHeight(1);
                listView.setFocusable(true);
                listView.setClickable(true);
                listView.setFocusableInTouchMode(false);
                dialog.show();

                // ListView Click Listener
                listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                        switch (groupPosition) {
                            case 0: // department
                                if (!departmentFilter.contains(department.get(childPosition))) {
                                    companyFilter.clear();
                                    packFilter.clear();
                                    statusFilter.clear();
                                    departmentFilter.clear();
                                    departmentFilter.add(department.get(childPosition));
                                } else {
                                    departmentFilter.remove(department.get(childPosition));
                                }
                                break;

                            case 1: // occasion
                                if (!companyFilter.contains(company.get(childPosition))) {
                                    companyFilter.clear();
                                    packFilter.clear();
                                    statusFilter.clear();
                                    departmentFilter.clear();
                                    companyFilter.add(company.get(childPosition));
                                } else {
                                    companyFilter.remove(company.get(childPosition));
                                }
                                break;
                            case 2: // material
                                if (!packFilter.contains(pack.get(childPosition))) {
                                    companyFilter.clear();
                                    packFilter.clear();
                                    statusFilter.clear();
                                    departmentFilter.clear();
                                    packFilter.add(pack.get(childPosition));
                                } else {
                                    packFilter.remove(pack.get(childPosition));
                                }
                                break;
                            case 3: // occasion
                                if (!statusFilter.contains(status.get(childPosition))) {
                                    companyFilter.clear();
                                    packFilter.clear();
                                    statusFilter.clear();
                                    departmentFilter.clear();
                                    statusFilter.add(status.get(childPosition));
                                } else {
                                    statusFilter.remove(status.get(childPosition));
                                }
                                break;
                        }
                        filterItemListAdapter.notifyDataSetChanged();
                        return false;
                    }
                });

                // Filter Apply Button Click
                Button apply = dialog.findViewById(R.id.apply);
                apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (companyFilter.size() > 0) {
                            getAllPosts_Filter("company", companyFilter.get(0));
                        } else if (departmentFilter.size() > 0) {
                            getAllPosts_Filter("department", departmentFilter.get(0));
                        } else if (packFilter.size() > 0) {
                            getAllPosts_Filter("pack", packFilter.get(0));
                        } else if (statusFilter.size() > 0) {
                            getAllPosts_Filter("status", statusFilter.get(0));
                        } else {
                            if(category == null)
                            {
                                getData( type  ,  1, sortBy.get(selectedKey));
                            }
                            else {
                                getData( type   ,  1, sortByText.getText().toString());
                            }
                        }
                        // Reload Products List By Filter
                        // fillGridView();
                        dialog.dismiss();
                    }
                });

                // Clear All Button Click
                Button clear = dialog.findViewById(R.id.clear);
                clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        try {
                            departmentFilter.clear();
                        } catch (NullPointerException ignore) {

                        }

                        try {
                            companyFilter.clear();
                        } catch (NullPointerException ignore) {

                        }
                        try {
                            packFilter.clear();
                        } catch (NullPointerException ignore) {

                        }
                        try {
                            statusFilter.clear();
                        } catch (NullPointerException ignore) {

                        }
                        filterItemListAdapter.notifyDataSetChanged();
                    }
                });

                // Close Button
                final ImageView close = dialog.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    private List<String> getNames(List<Variant> type) {
       // Toast.makeText(this, type.size(), Toast.LENGTH_SHORT).show();
        List<String> type_values = new ArrayList<>();
        for (int i = 0; i < type.size(); i++) {
            type_values.add(type.get(i).getName());
            Toast.makeText(ProductsActivity.this, type.get(i).getName(), Toast.LENGTH_SHORT).show();
        }
        return type_values;
    }

    private List<String> getVarients(int type) {
        final List<String> vairent = new ArrayList<>();

        VariantsAPIService service = RetrofitApi.retrofitRead().create(VariantsAPIService.class);

        Call<Variants> call = service.getVariants(type);

        call.enqueue(new Callback<Variants>() {
            @Override
            public void onResponse(Call<Variants> call, Response<Variants> response) {
                if(response.body() != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < response.body().getVariants().size(); i++) {
                                vairent.add(response.body().getVariants().get(i).getName());
                                /*
                                if(response.body().getVariants().get(i).getType() == type)
                                {

                                    vairent.add(response.body().getVariants().get(i).getName());

                                }

                                 */

                            }
                        }
                    }).run();
                }


            }

            @Override
            public void onFailure(Call<Variants> call, Throwable t) {
                Toast.makeText(ProductsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    return vairent;

    }
    private List<Variant> getLastVarientList(Callback<Variants> callback , int type)
    {
        List<Variant> variants = new ArrayList<>();
        VariantsAPIService service = RetrofitApi.retrofitRead().create(VariantsAPIService.class);
        Call<Variants> call = service.getVariants(type);

        call.enqueue(callback);

        return variants;
    }
    private List<Variant> getVarientList(int type) {

        VariantsAPIService service = RetrofitApi.retrofitRead().create(VariantsAPIService.class);

        Call<Variants> call = service.getVariants(type);
        try {
            Variants variants = call.execute().body();
            List<Variant> variantList = variants.getVariants();
            return variantList;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    public void getSubCategory(int subCategory)
    {


            CategoryAPIService service = RetrofitApi.retrofitReadWP3Categories("products/categories", String.valueOf(subCategory), "count").create(CategoryAPIService.class);

            Call<List<ProductCategory>> call = service.getWP3Categories();


            call.enqueue(new Callback<List<ProductCategory>>() {
                @Override
                public void onResponse(Call<List<ProductCategory>> call, Response<List<ProductCategory>> response) {
                    if (response.body() != null) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                categoeries_list.addAll(response.body());
                                categoriesAdapte.notifyDataSetChanged();
                                ShimmerLayoutcategory.setVisibility(View.GONE);
                                ShimmerLayoutcategory.stopShimmer();
                                if (categoeries_list.size() > 0) {
                                    categories_recycleView.setVisibility(View.VISIBLE);
                                }
                            }
                        }).run();
                    }
                }

                @Override
                public void onFailure(Call<List<ProductCategory>> call, Throwable t) {

                }
            });

    }
    @Override
    protected void onPause() {

        super.onPause();
    }



    @Override
    public void onCategoryClicked(ProductCategory contact, int position) {
        Intent intent = new Intent(ProductsActivity.this, ProductsActivity.class);
        switch (type) {
            case "Main":


                intent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST, contact);
                intent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST_TYPE, "Sub");
                startActivity(intent);
                break;

            case "Sub":


                intent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST, contact);
                intent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST_TYPE, "Group");
                startActivity(intent);
                break;


            default:
            {

                intent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST, contact);
                intent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST_TYPE, "Main");
                startActivity(intent);
                break;
            }
        }
    }



    CartBottomSheetFragment fragment = new CartBottomSheetFragment();
    void showCart() {

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
        setToolbarIconsClickListeners();
        //changeStatusBarColor();
    }
    // Set Toolbar Icons Click Listeners
    public void setToolbarIconsClickListeners() {
        ImageView search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
            }
        });
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

    @Override
    public void Update() {
        init_bar();
    }

    @Override
    public void onCartItemRemoved(int index, PendingProduct cartItem) {

    }

    @Override
    public void onCartItemAdded(int index) {
        mCallback.Update();
    }

    @Override
    public void onQuantityChnaged(int index) {
        mCallback.Update();

        //init_bar();
        showCart();


    }


}
