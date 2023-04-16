package com.mohaa.dokan.Controllers.fragments_home;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.mohaa.dokan.Controllers.activities_popup.SearchActivity;
import com.mohaa.dokan.Controllers.activities_products.IndividualProductActivity;
import com.mohaa.dokan.Controllers.activities_products.ProductsActivity;
import com.mohaa.dokan.R;
import com.mohaa.dokan.Utils.GridSpacingItemDecoration;
import com.mohaa.dokan.Utils.ProductsUI;
import com.mohaa.dokan.Utils.helper.SharedPrefManager;
import com.mohaa.dokan.Utils.multisnaprecyclerview.MultiSnapRecyclerView;
import com.mohaa.dokan.Utils.searchbar.MaterialSearchBar;
import com.mohaa.dokan.Utils.shimmer.ShimmerFrameLayout;
import com.mohaa.dokan.interfaces.OnAdsClickListener;
import com.mohaa.dokan.interfaces.OnCataClickListener;
import com.mohaa.dokan.interfaces.OnProductClickListener;

import com.mohaa.dokan.manager.ApiServices.AdAPIService;
import com.mohaa.dokan.manager.ApiServices.CategoryAPIService;
import com.mohaa.dokan.manager.ApiServices.ProductsAPIService;
import com.mohaa.dokan.manager.Call.Ads;
import com.mohaa.dokan.manager.Response.AdResponse;
import com.mohaa.dokan.manager.RetrofitApi;
import com.mohaa.dokan.models.Ad;
import com.mohaa.dokan.models.wp.ProductCategory;
import com.mohaa.dokan.views.AdsAdapter;
import com.mohaa.dokan.views.CategoriesWPAdapter;
import com.mohaa.dokan.views.ProductsSnapWPAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment  implements OnProductClickListener , OnCataClickListener  , OnAdsClickListener {

    public static final String TAG = "HomeActivity";
    private ArrayList<com.mohaa.dokan.models.wp.Product> sponsored_list;
    private ArrayList<com.mohaa.dokan.models.wp.Product> suggest_list;
    private ArrayList<com.mohaa.dokan.models.wp.Product> viewed_list;

    private ProductsSnapWPAdapter sponsoredAdapter;
    private ProductsSnapWPAdapter suggestAdapter;
    private ProductsSnapWPAdapter viewedAdapter;
    //

    private ArrayList<ProductCategory> categoeries_list;
    private CategoriesWPAdapter categoriesAdapter;
    private RecyclerView categories_recycleView;

    private LinearLayout recently_viewed_panel;
    private TextView btn_recently , btn_suggest , btn_viewed;

    private MaterialSearchBar materialSearchBar;

    private RecyclerView AdsrecList;
    private ArrayList<Ad> ads_list;
    private AdsAdapter adsAdapter;

    private ShimmerFrameLayout shimmerFrameLayout , shimmerFrameLayout2 , shimmerFrameLayout3 , shimmerFrameLayoutads , ShimmerLayoutcategory ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home , container , false);

        materialSearchBar = view.findViewById(R.id.searchBar);
        materialSearchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productIntent = new Intent(getContext(), SearchActivity.class);
                startActivity(productIntent);
            }
        });

        //
        shimmerFrameLayout = view.findViewById(R.id.parentShimmerLayout);
        shimmerFrameLayout2 = view.findViewById(R.id.parentShimmerLayout2);
        shimmerFrameLayout3 = view.findViewById(R.id.parentShimmerLayout3);
        shimmerFrameLayoutads = view.findViewById(R.id.parentShimmerLayoutads);
        ShimmerLayoutcategory = view.findViewById(R.id.parentShimmerLayoutcategory);


        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout2.startShimmer();
        shimmerFrameLayout3.startShimmer();
        shimmerFrameLayoutads.startShimmer();
        ShimmerLayoutcategory.startShimmer();


        categoeries_list = new ArrayList<>();
        categoriesAdapter = new CategoriesWPAdapter(categoeries_list ,this);
        categories_recycleView = view.findViewById(R.id.slider_cata);

        LinearLayoutManager cataManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        categories_recycleView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        categories_recycleView.setItemAnimator(new DefaultItemAnimator());
        categories_recycleView.setLayoutManager(cataManager);
        categories_recycleView.setAdapter(categoriesAdapter);
        loadCategoriesWP();

        //
        btn_recently = view.findViewById(R.id.btn_recentrly);
        btn_suggest = view.findViewById(R.id.btn_suggest);
        btn_viewed = view.findViewById(R.id.btn_viewed);

        recently_viewed_panel = view.findViewById(R.id.recently_viewed_panel);
        sponsored_list = new ArrayList<>();
        suggest_list = new ArrayList<>();
        viewed_list = new ArrayList<>();

        sponsoredAdapter = new ProductsSnapWPAdapter(sponsored_list , this);
        suggestAdapter = new ProductsSnapWPAdapter(suggest_list , this);
        viewedAdapter = new ProductsSnapWPAdapter(viewed_list , this);


        MultiSnapRecyclerView firstRecyclerView = (MultiSnapRecyclerView) view.findViewById(R.id.first_recycler_view);
        LinearLayoutManager firstManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        firstRecyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        firstRecyclerView.setItemAnimator(new DefaultItemAnimator());
        firstRecyclerView.setLayoutManager(firstManager);
        firstRecyclerView.setAdapter(sponsoredAdapter);


        MultiSnapRecyclerView secondRecyclerView =(MultiSnapRecyclerView)view.findViewById(R.id.second_recycler_view);
        LinearLayoutManager secondManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        secondRecyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        secondRecyclerView.setItemAnimator(new DefaultItemAnimator());
        secondRecyclerView.setLayoutManager(secondManager);
        secondRecyclerView.setAdapter(suggestAdapter);

        MultiSnapRecyclerView thirdRecyclerView =(MultiSnapRecyclerView)view.findViewById(R.id.third_recycler_view);
        LinearLayoutManager thirdManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        thirdRecyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        thirdRecyclerView.setItemAnimator(new DefaultItemAnimator());
        thirdRecyclerView.setLayoutManager(thirdManager);
        thirdRecyclerView.setAdapter(viewedAdapter);


        if( SharedPrefManager.getInstance(getContext()).getLastViewed() != 0)
        {
            getSponsoredProducts(SharedPrefManager.getInstance(getContext()).getLastViewed());
            btn_recently.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ProductsActivity.class);
                    intent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST_TYPE, "recently");

                    startActivity(intent);
                }
            });
        }
        else
        {

            getSponsoredProducts();
            btn_recently.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ProductsActivity.class);
                    intent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST_TYPE, "sponsored");
                    startActivity(intent);
                }
            });
        }

        getSuggestProducts();
        getMostViewedProducts();


        btn_suggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProductsActivity.class);
                intent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST_TYPE, "suggest");
                startActivity(intent);
            }
        });
        btn_viewed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProductsActivity.class);
                intent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST_TYPE, "viewed");
                startActivity(intent);
            }
        });


        AdsrecList = (RecyclerView) view.findViewById(R.id.slider);
        ads_list = new ArrayList<>();
        adsAdapter = new AdsAdapter(ads_list , this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()) {

            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                try {
                    LinearSmoothScroller smoothScroller = new LinearSmoothScroller(Objects.requireNonNull(getContext())) {
                        private static final float SPEED = 3500f;// Change this value (default=25f)

                        @Override
                        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                            return SPEED / displayMetrics.densityDpi;
                        }
                    };
                    smoothScroller.setTargetPosition(position);
                    startSmoothScroll(smoothScroller);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        //  LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        autoScrollAnother();
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        AdsrecList.setLayoutManager(layoutManager);//products_staggeredGridLayoutManager
        //
        AdsrecList.setHasFixedSize(true);
        AdsrecList.setItemViewCacheSize(1000);
        AdsrecList.setDrawingCacheEnabled(true);
        AdsrecList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        //
        AdsrecList.setAdapter(adsAdapter);
        loadAds();

        return view;
    }
    int scrollCount = 0;
    public void autoScrollAnother() {
        scrollCount = 0;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                AdsrecList.smoothScrollToPosition((scrollCount++));
                if (scrollCount == adsAdapter.getItemCount() - 4) {
                    ads_list.addAll(ads_list);
                    adsAdapter.notifyDataSetChanged();
                }
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);
    }
    private void loadAds() {
        AdAPIService service = RetrofitApi.retrofitRead().create(AdAPIService.class);


        Call<Ads> call = service.getAds(0);

        call.enqueue(new Callback<Ads>() {
            @Override
            public void onResponse(Call<Ads> call, final Response<Ads> response) {

                if(response.body() != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ads_list.addAll(response.body().getAds());
                            adsAdapter.notifyDataSetChanged();
                            shimmerFrameLayoutads.setVisibility(View.GONE);
                            shimmerFrameLayoutads.stopShimmer();
                        }
                    }).run();
                }


            }

            @Override
            public void onFailure(Call<Ads> call, Throwable t) {

            }
        });
    }
    private void loadCategoriesWP() {
        //

        CategoryAPIService service = RetrofitApi.retrofitReadWP3Categories("products/categories" , "0" , "count").create(CategoryAPIService.class);

        Call<List<ProductCategory>> call = service.getWP3Categories();



        call.enqueue(new Callback<List<ProductCategory>>() {
            @Override
            public void onResponse(Call<List<ProductCategory>> call, final Response<List<ProductCategory>> response) {
                if(response.body() != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            categoeries_list.addAll(response.body());
                            categoriesAdapter.notifyDataSetChanged();
                            ShimmerLayoutcategory.setVisibility(View.GONE);
                            ShimmerLayoutcategory.stopShimmer();
                        }
                    }).run();
                }

            }

            @Override
            public void onFailure(Call<List<ProductCategory>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }


    private void getMostViewedProducts() {

        ProductsAPIService service = RetrofitApi.retrofitReadWP3ProductsDefault("products" , "rating" , "1").create(ProductsAPIService.class);
        Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call = service.getWP3Products();
        // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
        call.enqueue(new Callback<ArrayList<com.mohaa.dokan.models.wp.Product>>() {
            @Override
            public void onResponse(Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call, Response<ArrayList<com.mohaa.dokan.models.wp.Product>> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);

                // Toast.makeText(ProductsActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                if(response.body() != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            viewed_list.addAll(response.body());
                            viewedAdapter.notifyDataSetChanged();
                            shimmerFrameLayout3.setVisibility(View.GONE);
                            shimmerFrameLayout3.stopShimmer();
                        }
                    }).run();
                }
                // Log.d("onFailure:",  "ProductName:"+response.body().getProducts().get(1).getTitle());






            }

            @Override
            public void onFailure(Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call, Throwable t) {

                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());

            }
        });

    }
    private void getSuggestProducts() {
        ProductsAPIService service = RetrofitApi.retrofitReadWP3ProductsDefault("products" , "rating" , "1").create(ProductsAPIService.class);
        Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call = service.getWP3Products();
        // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
        call.enqueue(new Callback<ArrayList<com.mohaa.dokan.models.wp.Product>>() {
            @Override
            public void onResponse(Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call, Response<ArrayList<com.mohaa.dokan.models.wp.Product>> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);

                // Toast.makeText(ProductsActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                if(response.body() != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            suggest_list.addAll(response.body());
                            suggestAdapter.notifyDataSetChanged();
                            shimmerFrameLayout2.setVisibility(View.GONE);
                            shimmerFrameLayout2.stopShimmer();
                        }
                    }).run();
                }
                // Log.d("onFailure:",  "ProductName:"+response.body().getProducts().get(1).getTitle());






            }

            @Override
            public void onFailure(Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call, Throwable t) {

                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());

            }
        });
    }
    private void getSponsoredProducts() {
        ProductsAPIService service = RetrofitApi.retrofitReadWP3ProductsDefault("products" , "rating" , "1").create(ProductsAPIService.class);
        Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call = service.getWP3Products();
        // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
        call.enqueue(new Callback<ArrayList<com.mohaa.dokan.models.wp.Product>>() {
            @Override
            public void onResponse(Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call, Response<ArrayList<com.mohaa.dokan.models.wp.Product>> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);

                // Toast.makeText(ProductsActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                if(response.body() != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sponsored_list.addAll(response.body());
                            sponsoredAdapter.notifyDataSetChanged();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            shimmerFrameLayout.stopShimmer();
                        }
                    }).run();
                }
                // Log.d("onFailure:",  "ProductName:"+response.body().getProducts().get(1).getTitle());





            }

            @Override
            public void onFailure(Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call, Throwable t) {

                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());

            }
        });

    }
    private void getSponsoredProducts(int cat_id) {
        ProductsAPIService service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(cat_id), "rating" , "1").create(ProductsAPIService.class);
        Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call = service.getWP3Products();
        // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
        call.enqueue(new Callback<ArrayList<com.mohaa.dokan.models.wp.Product>>() {
            @Override
            public void onResponse(Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call, Response<ArrayList<com.mohaa.dokan.models.wp.Product>> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);

                // Toast.makeText(ProductsActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                if(response.body() != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sponsored_list.addAll(response.body());
                            sponsoredAdapter.notifyDataSetChanged();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            shimmerFrameLayout.stopShimmer();
                        }
                    }).run();
                }
                // Log.d("onFailure:",  "ProductName:"+response.body().getProducts().get(1).getTitle());





            }

            @Override
            public void onFailure(Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call, Throwable t) {



            }
        });

    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, dp, r.getDisplayMetrics()));
    }




    @Override
    public void onProductClicked(com.mohaa.dokan.models.wp.Product contact, int position) {
        Intent loginIntent = new Intent(getContext(), IndividualProductActivity.class);
        loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST, (Serializable) contact);
        //loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_IMAGE, contact.getFeaturedSrc());
        loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_ID, contact.getId());
        loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_TYPE, contact.getType());
        startActivity(loginIntent);
    }

    @Override
    public void onCategoryClicked(ProductCategory contact, int position) {
        Intent intent = new Intent(getContext(), ProductsActivity.class);
        intent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST, contact);
        intent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST_TYPE, "Main");
        startActivity(intent);
    }



    @Override
    public void onAdsClicked(Ad contact, int position) {
            incViewedCount(contact.getId());
    }

    private void incViewedCount(int ads_id) {
        AdAPIService service = RetrofitApi.retrofitWrite().create(AdAPIService.class);

        Ad user = new Ad(ads_id, 1);

        Call<AdResponse> call = service.updateAdsView(
                user.getId(),
                user.getCount()

        );

        call.enqueue(new Callback<AdResponse>() {
            @Override
            public void onResponse(Call<AdResponse> call, Response<AdResponse> response) {

            }

            @Override
            public void onFailure(Call<AdResponse> call, Throwable t) {

            }
        });
    }

}
