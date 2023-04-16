package com.mohaa.dokan.Controllers.activities_products;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mohaa.dokan.Auth.LoginActivity;
import com.mohaa.dokan.Controllers.BaseActivity;

import com.mohaa.dokan.Controllers.activities_popup.CartReadyActivity;
import com.mohaa.dokan.Controllers.activities_popup.CommentsActivity;
import com.mohaa.dokan.Controllers.activities_popup.ProductQuickDetailActivity;
import com.mohaa.dokan.Controllers.activities_traders.TraderActivity;
import com.mohaa.dokan.Controllers.fragments_products.ProductBottomSheetFragment;
import com.mohaa.dokan.R;
import com.mohaa.dokan.Utils.Constant;
import com.mohaa.dokan.Utils.ConstantUI;
import com.mohaa.dokan.Utils.GridSpacingItemDecoration;
import com.mohaa.dokan.Utils.ProductsUI;
import com.mohaa.dokan.Utils.TextViewBold;
import com.mohaa.dokan.Utils.TextViewLight;
import com.mohaa.dokan.Utils.TextViewRegular;
import com.mohaa.dokan.Utils.Toasty.Toasty;
import com.mohaa.dokan.Utils.helper.SharedPrefManager;
import com.mohaa.dokan.Utils.multisnaprecyclerview.MultiSnapRecyclerView;
import com.mohaa.dokan.Utils.progressbar.RoundCornerProgressBar;
import com.mohaa.dokan.interfaces.OnProductClickListener;
import com.mohaa.dokan.interfaces.ProductResultReceiver;
import com.mohaa.dokan.interfaces.VariantionResultReceiver;
import com.mohaa.dokan.manager.ApiServices.CommentAPIService;
import com.mohaa.dokan.manager.ApiServices.ProductsAPIService;
import com.mohaa.dokan.manager.OrdersBase;
import com.mohaa.dokan.manager.RetrofitApi;
import com.mohaa.dokan.models.wp.Product;
import com.mohaa.dokan.models.wp.Productreview;
import com.mohaa.dokan.models.wp.VariationProduct;
import com.mohaa.dokan.networksync.CheckInternetConnection;
import com.mohaa.dokan.views.CommentsRecyclerAdapter;
import com.mohaa.dokan.views.ProductImageViewPagerAdapter;
import com.mohaa.dokan.views.ProductsSnapWPAdapter;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IndividualProductActivity extends BaseActivity implements OnProductClickListener , View.OnClickListener , ProductResultReceiver , VariantionResultReceiver {
    private static final String TAG = "IndividualProductActivity";
    //=========== Toolbar =============
    Toolbar toolbar;
    //=========== Buttons =============
    Button btn_buy;
    Button btn_add_to_cart;
    //=========== TextViews =============
    private TextViewBold tvproductbrand;
    private TextViewBold tvProductName;
    private TextViewRegular tvPrice;
    private TextViewRegular tvPrice1;
    private TextViewRegular tvAverageRatting;
    private TextViewRegular tvAvailibility;
    private TextViewLight tvSellerName;
    private TextViewRegular tvViewStore;
    private TextView desc_panel , spec_panel;
    private TextView see_all_reccommened , see_all_alike;
    private TextViewRegular tvDiscount;
    private TextViewLight tvMoreDetail;
    //=========== ImageViews =============
    private ImageView ivDiscount;
    //=========== ExtraIntent =============
    private Product products;
    private String products_id;
    //=========== AlikeProducts =============
    private ArrayList<com.mohaa.dokan.models.wp.Product> sponsored_list;
    private ArrayList<com.mohaa.dokan.models.wp.Product> suggest_list;
    private ArrayList<com.mohaa.dokan.models.wp.Product> viewed_list;

    private ProductsSnapWPAdapter sponsoredAdapter;
    private ProductsSnapWPAdapter suggestAdapter;
    private ProductsSnapWPAdapter viewedAdapter;
    //=========== Comments =============
    private RecyclerView commentsRecyclerView;
    private CommentsRecyclerAdapter commentsRecyclerAdapter;
    private List<Productreview> commentsList;
    private LinearLayout newCommentContainer;
    private EditText commentEditText;
    private TextView see_all_comments;
    //=========== Rating =============
    private TextViewRegular tvRatting;
    TextViewRegular tvFive;
    TextViewRegular tvFour;
    TextViewRegular tvThree;
    TextViewRegular tvTwo;
    TextViewRegular tvOne;
    RoundCornerProgressBar rattingFive;
    RoundCornerProgressBar rattingFour;
    RoundCornerProgressBar rattingThree;
    RoundCornerProgressBar rattingTwo;
    RoundCornerProgressBar rattingOne;
    private int  fiveRate, fourRate, threeRate, twoRate, oneRate;
    //=========== Cart =============
    int cartCount = 0;
    private TextView count;
    private ImageView cart;
    //=========== Panels =============
    private FrameLayout flDiscount;
    private LinearLayout sponosored_panel , related_panel , upsale_panel;
    private LinearLayout llIsSeller;
    private LinearLayout desc , spec;
    //=========== ImageAlbums =============
    private ViewPager vpProductImages;
    private LinearLayout layoutDots;
    private ProductImageViewPagerAdapter productImageViewPagerAdapter;
    private TextView[] dots;
    private int currentPosition;
    List<com.mohaa.dokan.models.wp.Product.Image> imageList = new ArrayList<>();
    //=========== ProgressDialog =============
    private ProgressDialog mLoginProgress;
    //=========== Others =============
    private WebView products_specifications , products_details;
    private double discount;
    //=========== Variation =============
    ProductBottomSheetFragment fragment = new ProductBottomSheetFragment();

    List<VariationProduct> variantProducts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_product);

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

        initViews();
        initExtras();
        initSetViews();
        init();
        setToolbarIconsClickListeners();
        initListeners();
        initLoadData();
        initVariantion();
        initClickListeners();

        init_bar();
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            SharedPrefManager.getInstance(getApplicationContext()).userViewed(products.getCategories().get(0).getId());
        }



    }

    private void initVariantion() {
        getVariation();
    }


    private void initClickListeners() {
        desc_panel.setOnClickListener(this);
        spec_panel.setOnClickListener(this);
        tvMoreDetail.setOnClickListener(this);
        tvViewStore.setOnClickListener(this);
        btn_add_to_cart.setOnClickListener(this);
        btn_buy.setOnClickListener(this);
        commentEditText.setOnClickListener(this);
        see_all_comments.setOnClickListener(this);
        see_all_alike.setOnClickListener(this);
        see_all_reccommened.setOnClickListener(this);

    }

    private void initViews() {
        mLoginProgress = new ProgressDialog(this);

        count = findViewById(R.id.count);
        related_panel = findViewById(R.id.related_panel);
        upsale_panel = findViewById(R.id.upsale_panel);
        flDiscount = findViewById(R.id.flDiscount);
        ivDiscount = findViewById(R.id.ivDiscount);
        tvDiscount = findViewById(R.id.tvDiscount);
        vpProductImages = findViewById(R.id.vpProductImages);
        layoutDots = findViewById(R.id.layoutDots);
        tvProductName = findViewById(R.id.tvProductName);
        tvPrice = findViewById(R.id.tvPrice);
        tvPrice1 = findViewById(R.id.tvPrice1);

        tvOne = findViewById(R.id.tvOne);
        tvTwo = findViewById(R.id.tvTwo);
        tvThree = findViewById(R.id.tvThree);
        tvFour = findViewById(R.id.tvFour);
        tvFive = findViewById(R.id.tvFive);

        tvAverageRatting = findViewById(R.id.tvAverageRatting);
        rattingOne = findViewById(R.id.rattingOne);
        rattingTwo = findViewById(R.id.rattingTwo);
        rattingThree = findViewById(R.id.rattingThree);
        rattingFour = findViewById(R.id.rattingFour);
        rattingFive = findViewById(R.id.rattingFive);


        tvFive.setTextColor(getResources().getColor(R.color.colorAccent));
        tvFour.setTextColor(getResources().getColor(R.color.colorAccent));
        tvThree.setTextColor(getResources().getColor(R.color.colorAccent));
        tvTwo.setTextColor(getResources().getColor(R.color.colorAccent));
        tvOne.setTextColor(getResources().getColor(R.color.colorAccent));

        tvPrice = findViewById(R.id.tvPrice);
        tvAvailibility = findViewById(R.id.tvAvailibility);
        tvRatting = findViewById(R.id.tvRatting);
        llIsSeller = findViewById(R.id.llIsSeller);
        tvSellerName = findViewById(R.id.tvSellerName);


        tvViewStore = findViewById(R.id.tvViewStore);


        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        newCommentContainer = findViewById(R.id.newCommentContainer);
        commentEditText = findViewById(R.id.commentEditText);
        see_all_comments = findViewById(R.id.see_all_comments);

        tvproductbrand = findViewById(R.id.productbrand);

        products_details = findViewById(R.id.products_details);
        products_specifications = findViewById(R.id.products_specifications);
        see_all_alike = findViewById(R.id.see_all_alike);
        see_all_reccommened = findViewById(R.id.see_all_reccommened);

        desc_panel = findViewById(R.id.desc_panel);
        spec_panel = findViewById(R.id.spec_panel);
        desc = findViewById(R.id.desc);
        spec = findViewById(R.id.spec);
        sponosored_panel = findViewById(R.id.sponosored_panel);
        tvMoreDetail = findViewById(R.id.tvMoreDetail);
        btn_buy = findViewById(R.id.buy_now);
        btn_add_to_cart = findViewById(R.id.add_to_cart);
    }
    private void initExtras() {
        products = (com.mohaa.dokan.models.wp.Product) getIntent().getExtras().getSerializable(ProductsUI.BUNDLE_PRODUCTS_LIST);
        products_id = getIntent().getStringExtra(ProductsUI.BUNDLE_PRODUCTS_ID);
    }


    private void initSetViews() {

        tvProductName.setText(products.getName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvPrice.setText(Html.fromHtml(String.valueOf(products.getPriceHtml()), Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvPrice.setText(Html.fromHtml(String.valueOf(products.getPriceHtml())));
        }
        if(products.getSalePrice()!=null && !products.getSalePrice().equals("")) {
            discount = (Double.valueOf(products.getRegularPrice()) - Double.valueOf(products.getSalePrice())) / Double.valueOf(products.getRegularPrice()) * 100;
            tvPrice1.setVisibility(View.VISIBLE);
            tvDiscount.setText(String.valueOf(new DecimalFormat("##").format(discount))+ "%" + getResources().getString(R.string.off));
            tvPrice1.setText( getResources().getString(R.string.save) + " " +  String.valueOf(Double.valueOf(products.getRegularPrice())  - Double.valueOf(products.getSalePrice())+ " LE"));
            tvPrice1.setPaintFlags( tvPrice1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        tvSellerName.setText(products.getStore().getShopName());
    }


    private void init() {
        variantProducts = new ArrayList<>();
        if(products.getType().equals("variable"))
        {
            tvAvailibility.setText(getResources().getString(R.string.in_stock));
            tvAvailibility.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        else {
            if(products.getStockQuantity() !=null) {

                if (products.getStockQuantity() > 0 || !products.getStockStatus().equals("outofstock")) {
                    tvAvailibility.setText(products.getStockQuantity() + "  " + getResources().getString(R.string.in_stock));
                    tvAvailibility.setTextColor(getResources().getColor(R.color.colorAccent));
                } else {
                    tvAvailibility.setText(R.string.out_of_stock);

                    tvAvailibility.setTextColor(getResources().getColor(R.color.red_700));
                    btn_buy.setAlpha((float) 0.6);
                    btn_buy.setClickable(false);
                    btn_add_to_cart.setAlpha((float) 0.6);
                    btn_add_to_cart.setClickable(false);
                }
            }
        }



        if(products.getShortDescription() != null)
        {
           // "width=\"600\""
            String details =products.getShortDescription();
            details =  details.replaceAll("width=\"\\d+\"" , "width=\"380\"").replaceAll("height=\"\\d+\"" , "height=\"280\"").replaceAll("<p>" , "<p style=\"color:white;\">");
           // details =  details.replaceAll("<p>" , "<p style=\"color:white;\">");

            String styleDetails = "<body style=\"background-color:black;\">" + details + "</body>";
            //details =  details.replaceAll("width=\"1956\"" , "width=\""+minwidth+"\"").replaceAll("height=\"724\"" , "height=\""+minheight+"\"");
            products_details.loadData(styleDetails, "text/html; charset=UTF-8", null);
        }

        if(products.getRatingCount() != 0) {
            tvRatting.setText(products.getAverageRating());

        }


    }
    /**
     * method to initialize the listeners
     */
    private void initListeners() {


        setVpProductImages();
        updateAlikeProductsRecycleView();
        updateCommentsRecycleView();


    }
    private void initLoadData() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if(products.getRelatedIds().size() !=0 && products.getRelatedIds() != null) {
                String RelatedIDS = String.join(",", products.getRelatedIds().toString());
                getSponsoredProducts("(" + RelatedIDS + ")");
            }
        }
        LoadDatsViaLoops();
        loadComments(1);
    }

    private void LoadDatsViaLoops() {
        for (int i = 0; i < products.getMetaData().size(); i++) {
            if(products.getMetaData().get(i).getKey().equals("_specifications"))
            {
                if(products.getMetaData().get(i).getValue() != "") {
                    Log.d("_specifications", "_specifications: ");
                    String spec = String.valueOf(products.getMetaData().get(i).getValue());
                    spec = spec.replaceAll("<p>", "<p style=\"color:white;\">");
                    // details =  details.replaceAll("<p>" , "<p style=\"color:white;\">");

                    String styleSpec = "<body style=\"background-color:black;\">" + spec + "</body>";
                    //Toast.makeText(this, "" + ads_spec, Toast.LENGTH_SHORT).show();
                    products_specifications.loadData(styleSpec, "text/html; charset=UTF-8", null);

                    spec_panel.setVisibility(View.VISIBLE);
                    // fillSpecifications( ads_spec ,spec_message);
                }
                break;
            }

        }
        for (int i = 0; i < products.getMetaData().size(); i++) {
            if(products.getMetaData().get(i).getKey().equals("_accessory_ids"))
            {
                //String Accessory = String.valueOf(products.getMetaData().get(i).getValue());
                //Toast.makeText(this, "" +Accessory , Toast.LENGTH_SHORT).show();
                List<Double> acc_list = (List<Double>) products.getMetaData().get(i).getValue();
                Log.d("Accessory", "Accessory: " + acc_list.size());
                for (int j = 0; j <acc_list.size() ; j++) {
                    Log.d("Accessory", "Accessory: " + acc_list.get(j));
                    int id =  acc_list.get(j).intValue();
                    ProductsAPIService service = RetrofitApi.retrofitReadWP3Product("products/" +id).create(ProductsAPIService.class);
                    Call<com.mohaa.dokan.models.wp.Product> call = service.getWP3SingleProduct(id);
                    // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
                    call.enqueue(new Callback<com.mohaa.dokan.models.wp.Product>() {
                        @Override
                        public void onResponse(Call<com.mohaa.dokan.models.wp.Product> call, Response<com.mohaa.dokan.models.wp.Product> response) {
                            if(response.body() != null) {
                                sponosored_panel.setVisibility(View.VISIBLE);
                                Log.d("Accessory", "Accessory: " + response.body().getName());
                                sponsored_list.add(response.body());
                                //sponsored_list.addAll(response.body());
                                sponsoredAdapter.notifyDataSetChanged();
                            }




                        }

                        @Override
                        public void onFailure(Call<com.mohaa.dokan.models.wp.Product> call, Throwable t) {
                            Log.d("Accessory", "onFailure: " + t.getLocalizedMessage());


                        }
                    });
                }

                break;
            }

        }

        List<Integer> related_list = (List<Integer>) products.getRelatedIds();
        for (int j = 0; j <related_list.size() ; j++) {
            Log.d("related", "related: " + related_list.get(j));
            int id =  related_list.get(j);
            ProductsAPIService service = RetrofitApi.retrofitReadWP3Product("products/" +id).create(ProductsAPIService.class);
            Call<com.mohaa.dokan.models.wp.Product> call = service.getWP3SingleProduct(id);
            // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
            call.enqueue(new Callback<com.mohaa.dokan.models.wp.Product>() {
                @Override
                public void onResponse(Call<com.mohaa.dokan.models.wp.Product> call, Response<com.mohaa.dokan.models.wp.Product> response) {
                    if(response.body() != null) {
                        related_panel.setVisibility(View.VISIBLE);
                        Log.d("Accessory", "Accessory: " + response.body().getName());
                        suggest_list.add(response.body());
                        //sponsored_list.addAll(response.body());
                        suggestAdapter.notifyDataSetChanged();
                    }




                }

                @Override
                public void onFailure(Call<com.mohaa.dokan.models.wp.Product> call, Throwable t) {
                    Log.d("Accessory", "onFailure: " + t.getLocalizedMessage());


                }
            });
        }

        List<Integer> upsalelist = (List<Integer>) products.getUpsellIds();
        for (int j = 0; j <upsalelist.size() ; j++) {
            Log.d("upsale", "upsale: " + upsalelist.get(j));
            int id =  upsalelist.get(j);
            ProductsAPIService service = RetrofitApi.retrofitReadWP3Product("products/" +id).create(ProductsAPIService.class);
            Call<com.mohaa.dokan.models.wp.Product> call = service.getWP3SingleProduct(id);
            // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
            call.enqueue(new Callback<com.mohaa.dokan.models.wp.Product>() {
                @Override
                public void onResponse(Call<com.mohaa.dokan.models.wp.Product> call, Response<com.mohaa.dokan.models.wp.Product> response) {
                    if(response.body() != null) {
                        upsale_panel.setVisibility(View.VISIBLE);
                        Log.d("Accessory", "Accessory: " + response.body().getName());
                        viewed_list.add(response.body());
                        //sponsored_list.addAll(response.body());
                        viewedAdapter.notifyDataSetChanged();

                    }


                }

                @Override
                public void onFailure(Call<com.mohaa.dokan.models.wp.Product> call, Throwable t) {
                    Log.d("Accessory", "onFailure: " + t.getLocalizedMessage());


                }
            });
        }

        for (int i = 0; i < products.getAttributes().size(); i++) {
            if(products.getAttributes().get(i).getName().equals("brands"))
            {

                tvproductbrand.setText(String.valueOf(products.getAttributes().get(i).getOptions().get(0)));
                tvproductbrand.setVisibility(View.VISIBLE);
                //Toast.makeText(this, "" + String.valueOf(products.getAttributes().get(i).getOptions()), Toast.LENGTH_SHORT).show();
            }
            else{
                tvproductbrand.setVisibility(View.GONE);
            }
        }
    }

    private void setRate(int totalReview , int fiveRate , int fourRate , int threeRate , int twoRate , int oneRate ) {
        tvAverageRatting.setText(Constant.setDecimalTwo(Double.valueOf(products.getAverageRating())) + "/5");
        rattingFive.setProgress((fiveRate / totalReview) * 100);
        rattingFour.setProgress((fourRate / totalReview) * 100);
        rattingThree.setProgress((threeRate / totalReview) * 100);
        rattingTwo.setProgress((twoRate / totalReview) * 100);
        rattingOne.setProgress((oneRate / totalReview) * 100);
    }
    private void setVpProductImages() {
        imageList = products.getImages();
        addBottomDots(0, imageList.size());
        if (productImageViewPagerAdapter == null) {
            productImageViewPagerAdapter = new ProductImageViewPagerAdapter(this, products.getId());
            vpProductImages.setAdapter(productImageViewPagerAdapter);
            productImageViewPagerAdapter.addAll(imageList);
            vpProductImages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    addBottomDots(position, imageList.size());
                    currentPosition = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else {
            vpProductImages.setCurrentItem(0);
            productImageViewPagerAdapter.addAll(imageList);
        }


    }
    private void addBottomDots(int currentPage, int length) {
        layoutDots.removeAllViews();
        dots = new TextView[length];

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.green_600));
            layoutDots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(getResources().getColor(R.color.colorAccent));
    }
    private void getMostViewedProducts() {

        ProductsAPIService service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(products.getCategories().get(0).getId()), "rating" , "1").create(ProductsAPIService.class);
        Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call = service.getWP3Products();
        // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
        call.enqueue(new Callback<ArrayList<com.mohaa.dokan.models.wp.Product>>() {
            @Override
            public void onResponse(Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call, Response<ArrayList<com.mohaa.dokan.models.wp.Product>> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);

                // Toast.makeText(ProductsActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                if(response.body() != null) {
                    viewed_list.addAll(response.body());
                    viewedAdapter.notifyDataSetChanged();
                }
                // Log.d("onFailure:",  "ProductName:"+response.body().getProducts().get(1).getTitle());






            }

            @Override
            public void onFailure(Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call, Throwable t) {



            }
        });

    }
    private void getSuggestProducts() {
        ProductsAPIService service = RetrofitApi.retrofitReadWP3ProductsByCategory("products" , String.valueOf(products.getCategories().get(0).getId()), "popularity" , "1").create(ProductsAPIService.class);
        Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call = service.getWP3Products();
        // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
        call.enqueue(new Callback<ArrayList<com.mohaa.dokan.models.wp.Product>>() {
            @Override
            public void onResponse(Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call, Response<ArrayList<com.mohaa.dokan.models.wp.Product>> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);

                // Toast.makeText(ProductsActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                if(response.body() != null) {
                    suggest_list.addAll(response.body());
                    suggestAdapter.notifyDataSetChanged();
                }
                // Log.d("onFailure:",  "ProductName:"+response.body().getProducts().get(1).getTitle());






            }

            @Override
            public void onFailure(Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call, Throwable t) {



            }
        });
    }
    private void getSponsoredProducts(String includes) {
        ProductsAPIService service = RetrofitApi.retrofitReadWP3ProductsInclude("products" , includes , "1").create(ProductsAPIService.class);
        Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call = service.getWP3Products();
        // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
        call.enqueue(new Callback<ArrayList<com.mohaa.dokan.models.wp.Product>>() {
            @Override
            public void onResponse(Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call, Response<ArrayList<com.mohaa.dokan.models.wp.Product>> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);

                // Toast.makeText(ProductsActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();

                //sponsored_list.addAll(response.body());
               // sponsoredAdapter.notifyDataSetChanged();

                // Log.d("onFailure:",  "ProductName:"+response.body().getProducts().get(1).getTitle());
                if(response.body() != null)
                {
                   // Log.d(TAG, "onResponse: Succ product ");
                    sponosored_panel.setVisibility(View.VISIBLE);
                }
                else {
                    //Log.d(TAG, "onResponse: failed product ");
                    sponosored_panel.setVisibility(View.GONE);
                }





            }

            @Override
            public void onFailure(Call<ArrayList<com.mohaa.dokan.models.wp.Product>> call, Throwable t) {
                Log.d("IndProduct", "onFailure: " + t.getLocalizedMessage());


            }
        });

    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, dp, r.getDisplayMetrics()));
    }

    /*
    private void fillSpecifications(String spec , ExpandableSpecTextView commentTextView) {

        Spannable contentString = new SpannableStringBuilder(getResources().getString(R.string.specifications) + "   " + spec);
        contentString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(IndividualProductActivity.this, R.color.highlight_text)),
                0, products_specifications.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            commentTextView.setText(Html.fromHtml(String.valueOf(contentString), Html.FROM_HTML_MODE_COMPACT));
        } else {
            commentTextView.setText(Html.fromHtml(String.valueOf(contentString)));
        }


        commentTextView.setText(contentString);




    }


     */

    protected void onResume() {
        super.onResume();

        new CheckInternetConnection(this).checkConnection();
        // Update Cart Count
        cartCount = OrdersBase.getInstance().getmOrders().size();

        if (cartCount > 0) {
            count.setVisibility(View.VISIBLE);
            count.setText(String.valueOf(cartCount));
        } else {
            count.setVisibility(View.GONE);
        }

    }



    @Override
    public void onProductClicked(com.mohaa.dokan.models.wp.Product contact, int position) {
        Intent loginIntent = new Intent(IndividualProductActivity.this, IndividualProductActivity.class);
        loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST, (Serializable) contact);
        //loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_IMAGE, contact.getFeaturedSrc());
        loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_ID, contact.getId());
        loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_TYPE, contact.getType());
        startActivity(loginIntent);
    }
    // Set Toolbar Icons Click Listeners
    public void setToolbarIconsClickListeners() {
        ImageView cart = findViewById(R.id.cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cartCount > 0) {
                    startActivity(new Intent(getApplicationContext(), CartReadyActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), R.string.cart_is_empty, Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void updateAlikeProductsRecycleView() {
        sponsored_list = new ArrayList<>();
        suggest_list = new ArrayList<>();
        viewed_list = new ArrayList<>();

        sponsoredAdapter = new ProductsSnapWPAdapter(sponsored_list , this);
        suggestAdapter = new ProductsSnapWPAdapter(suggest_list , this);
        viewedAdapter = new ProductsSnapWPAdapter(viewed_list , this);


        MultiSnapRecyclerView firstRecyclerView = (MultiSnapRecyclerView) findViewById(R.id.first_recycler_view);
        LinearLayoutManager firstManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        firstRecyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        firstRecyclerView.setItemAnimator(new DefaultItemAnimator());
        firstRecyclerView.setLayoutManager(firstManager);
        firstRecyclerView.setAdapter(sponsoredAdapter);


        MultiSnapRecyclerView secondRecyclerView =(MultiSnapRecyclerView)findViewById(R.id.second_recycler_view);
        LinearLayoutManager secondManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        secondRecyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        secondRecyclerView.setItemAnimator(new DefaultItemAnimator());
        secondRecyclerView.setLayoutManager(secondManager);
        secondRecyclerView.setAdapter(suggestAdapter);

        MultiSnapRecyclerView thirdRecyclerView =(MultiSnapRecyclerView)findViewById(R.id.third_recycler_view);
        LinearLayoutManager thirdManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        thirdRecyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        thirdRecyclerView.setItemAnimator(new DefaultItemAnimator());
        thirdRecyclerView.setLayoutManager(thirdManager);
        thirdRecyclerView.setAdapter(viewedAdapter);
    }

    private void updateCommentsRecycleView() {
        commentsList = new ArrayList<>();
        commentsRecyclerAdapter = new CommentsRecyclerAdapter(commentsList);
        commentsRecyclerView.setHasFixedSize(true);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        commentsRecyclerView.setAdapter(commentsRecyclerAdapter);

    }
    private void loadComments(int pageno)
    {
        CommentAPIService service = RetrofitApi.retrofitReadWP3ProductsReviews("products/reviews" , String.valueOf(products.getId()), "1").create(CommentAPIService.class);
        Call<ArrayList<Productreview>> call = service.getWP3Reviews();
        // String[] sortByArray = {"Most Recent", "Most Order", "Most Shares", "Most Viewed"};
        call.enqueue(new Callback<ArrayList<Productreview>>() {
            @Override
            public void onResponse(Call<ArrayList<Productreview>> call, Response<ArrayList<Productreview>> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);

                // Toast.makeText(ProductsActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();

                commentsList.addAll(response.body());
                commentsRecyclerAdapter.notifyDataSetChanged();

                if(commentsList.size() > 0) {
                    for (int i = 0; i < commentsList.size(); i++) {
                        if (commentsList.get(i).getRating() == 5) {
                            fiveRate = fiveRate + 1;
                        } else if (commentsList.get(i).getRating() == 4) {
                            fourRate = fourRate + 1;
                        } else if (commentsList.get(i).getRating() == 3) {
                            threeRate = threeRate + 1;
                        } else if (commentsList.get(i).getRating() == 2) {
                            twoRate = twoRate + 1;
                        } else if (commentsList.get(i).getRating() == 1) {
                            oneRate = oneRate + 1;
                        }
                    }
                    setRate(commentsList.size(), fiveRate, fourRate, threeRate, twoRate, oneRate);
                }
                //shimmerFrameLayout.setVisibility(View.GONE);
                // shimmerFrameLayout.stopShimmer();
                // Log.d("onFailure:",  "ProductName:"+response.body().getProducts().get(1).getTitle());





            }

            @Override
            public void onFailure(Call<ArrayList<Productreview>> call, Throwable t) {



            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.desc_panel:
            {
                if(spec_panel.getVisibility() != View.GONE) {
                    desc.setVisibility(View.VISIBLE);
                    spec.setVisibility(View.GONE);

                    desc.setBackground(getResources().getDrawable(R.drawable.border_blue));
                    spec.setBackground(getResources().getDrawable(R.drawable.border_white));
                }
                break;
            }
            case R.id.spec_panel:
            {
                desc.setVisibility(View.GONE);
                spec.setVisibility(View.VISIBLE);

                desc.setBackground(getResources().getDrawable(R.drawable.border_white));
                spec.setBackground(getResources().getDrawable(R.drawable.border_blue));
                break;
            }
            case R.id.tvMoreDetail:
            {
                Intent intent = new Intent(IndividualProductActivity.this, ProductQuickDetailActivity.class);
                intent.putExtra(ConstantUI.PRODUCT_DETAILS, products.getDescription());
                intent.putExtra(ConstantUI.PRODUCT_TITLE, products.getName());
                intent.putExtra(ConstantUI.PRODUCT_CATEGORY, products.getCategories().get(0).getName());
                if (products.getImages().size() > 0) {
                    intent.putExtra(ConstantUI.PRODUCT_IMAGE, products.getImages().get(0).getSrc());
                } else {
                    intent.putExtra(ConstantUI.PRODUCT_IMAGE, "");
                }
                startActivity(intent);
                break;
            }
            case R.id.tvViewStore:
            {
                Intent loginIntent = new Intent(IndividualProductActivity.this, TraderActivity.class);
                loginIntent.putExtra(ProductsUI.BUNDLE_TRADERS_ID, products.getStore().getId());

                startActivity(loginIntent);
                break;
            }
            case R.id.buy_now:
            case R.id.add_to_cart:
            {
                if (SharedPrefManager.getInstance(this).isLoggedIn()) {
                    if(products.getType().equals("variable"))
                    {
                        showCart();
                    }
                    else
                    {
                        if (products.getStockQuantity() != null) {
                            if (products.getStockQuantity() > 0) {
                                showCart();


                            } else {
                                Toasty.error(IndividualProductActivity.this, getResources().getString(R.string.out_of_stock), Toast.LENGTH_SHORT, true).show();
                            }
                        }
                    }

                }
                else
                {
                    startActivity(new Intent(IndividualProductActivity.this, LoginActivity.class));
                }

                break;
            }

            case R.id.see_all_comments:
            case R.id.commentEditText:
            {
                Intent loginIntent = new Intent(IndividualProductActivity.this, CommentsActivity.class);
                loginIntent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST, (Serializable) products);

                startActivity(loginIntent);
                break;
            }

            case R.id.see_all_reccommened:
            case R.id.see_all_alike:
            {
                Intent intent = new Intent(IndividualProductActivity.this, ProductsActivity.class);
                intent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST_CATEGORY, products.getCategories().get(0).getId());
                intent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST_TYPE, "like");
                startActivity(intent);
                break;
            }

            //handle multiple view click events
        }
    }

    public void getVariation() {

        ProductsAPIService service = RetrofitApi.retrofitReadWP3Product("products/" + products.getId() + "/variations").create(ProductsAPIService.class);

        Call<List<VariationProduct>> call = service.getWP3SingleProductVarintaion(products.getId());
        call.enqueue(new Callback<List<VariationProduct>>() {
            @Override
            public void onResponse(Call<List<VariationProduct>> call, Response<List<VariationProduct>> response) {
                if(response.body() != null) {

                    variantProducts.addAll(response.body());
                    Log.d("Variation", "Variation: " + response.body().size());
                }


            }

            @Override
            public void onFailure(Call<List<VariationProduct>> call, Throwable t) {
                Log.d("Variation", "onFailure: " + t.getLocalizedMessage());


            }
        });
    }
    void showCart() {

        fragment.show(getSupportFragmentManager(), fragment.getTag());
    }

    private void init_bar() {

        //showCart()
        setToolbarIconsClickListeners();
        //changeStatusBarColor();
    }


    @Override
    public Product getResult() {
        return products;
    }

    @Override
    public List<VariationProduct> getVariant() {

        return variantProducts;
    }
}
