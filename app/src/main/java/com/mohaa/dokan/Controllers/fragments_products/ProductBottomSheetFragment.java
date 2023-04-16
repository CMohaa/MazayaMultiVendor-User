package com.mohaa.dokan.Controllers.fragments_products;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mohaa.dokan.Auth.LoginActivity;
import com.mohaa.dokan.Controllers.activities_popup.CartReadyActivity;
import com.mohaa.dokan.R;
import com.mohaa.dokan.Utils.TextViewBold;
import com.mohaa.dokan.Utils.TextViewLight;
import com.mohaa.dokan.Utils.Toasty.Toasty;
import com.mohaa.dokan.Utils.helper.SharedPrefManager;
import com.mohaa.dokan.interfaces.OnCallbackReceived;
import com.mohaa.dokan.interfaces.ProductResultReceiver;
import com.mohaa.dokan.interfaces.VariantionResultReceiver;
import com.mohaa.dokan.manager.ApiServices.ProductsAPIService;
import com.mohaa.dokan.manager.OrdersBase;
import com.mohaa.dokan.manager.RetrofitApi;
import com.mohaa.dokan.models.PendingProduct;
import com.mohaa.dokan.models.wp.Product;
import com.mohaa.dokan.models.wp.VariationProduct;
import com.mohaa.dokan.networksync.CheckInternetConnection;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mohaa.dokan.Controllers.fragments_home.HomeFragment.TAG;


public class ProductBottomSheetFragment extends BottomSheetDialogFragment  {

    public ProductResultReceiver resultreceiver;
    public VariantionResultReceiver variantionResultReceiver;
    OnCallbackReceived mCallback;
    private Button btnCheckout;
    private ImageView brn_close;

    private String selectedSize = null;
    private String selectedColor = null;
    private String selectedItemPrice = null;
    private LinearLayout colorParentLay, sizeParentLay;
    private FlowLayout colorsLay, sizeLay;
    private Product productList;
    private List<VariationProduct> variationProduct;
    private VariationProduct selectedVariationProduct;


    private TextView name;
    private TextView price;
    private ImageView src;
    private double totalPrice;
    int selectedItemQuantity;
    private TextViewBold quantity ;//itemCardSeller
    private TextViewLight txtStockQuantity;//itemCardSellfer
    private ImageView minus, plus;
    List<String> sizeList = new ArrayList<>();
    public ProductBottomSheetFragment() {
        // Required empty public constructor
    }

    private  int colorID , sizeID;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Making bottom sheet expanding to full height by default
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(dialog1 -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog1;

            FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
        });
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_bottom_sheet, container, false);
        productList = resultreceiver.getResult();
        variationProduct = variantionResultReceiver.getVariant();
        name = view.findViewById(R.id.tvName);
        price = view.findViewById(R.id.tvPrice);

        src = view.findViewById(R.id.ivImage);

        name.setText(productList.getName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            price.setText(Html.fromHtml(String.valueOf(productList.getPriceHtml()), Html.FROM_HTML_MODE_COMPACT));
        } else {
            price.setText(Html.fromHtml(String.valueOf(productList.getPriceHtml())));
        }
        if(productList.getImages() !=null) {
            if (productList.getImages().size() > 0) {
                Glide.with(getContext())
                        .load(productList.getImages().get(0).getSrc()) // image url
                        .apply(new RequestOptions()
                                .override(512, 512) // resizing
                                .centerCrop())
                        .into(src);  // imageview object
            }
        }
        quantity = view.findViewById(R.id.tvQuantity);
        txtStockQuantity = view.findViewById(R.id.txtStockQuantity);
        minus =  view.findViewById(R.id.tvDecrement);
        plus =  view.findViewById(R.id.tvIncrement);

        btnCheckout = view.findViewById(R.id.btn_checkout);
        brn_close = view.findViewById(R.id.ivDelete);

        Log.d(TAG, "onCreateView: " + variationProduct.size());
        colorsLay = view.findViewById(R.id.colorsLay);
        sizeLay = view.findViewById(R.id.sizesLay);
        colorParentLay = view.findViewById(R.id.colorParentLay);
        sizeParentLay = view.findViewById(R.id.sizeParentLay);

        init();
        setValues(productList);
        brn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckoutClick();
            }
        });

        selectedItemQuantity  = 1;
        quantity.setText(String.valueOf(selectedItemQuantity));
        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!quantity.getText().toString().equals("")) {
                    int selectedQuantity = Integer.parseInt(quantity.getText().toString());
                    try {
                        totalPrice = Double.parseDouble(selectedItemPrice) * selectedQuantity;
                        btnCheckout.setText(getString(R.string.btn_Proceed, getString(R.string.price_with_currency, totalPrice)));

                    }
                    catch (Exception e)
                    {
                        Log.d(TAG, "onTextChanged: " + e.getLocalizedMessage());
                    }



                    //Toast.makeText(context, "" + sellProducts.get(i).getQuantity() , Toast.LENGTH_SHORT).show();
                    //onCartClickListener.onProductClicked(products, i);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( selectedItemPrice !=null) {
                    if (selectedItemQuantity != 1) {
                        selectedItemQuantity--;
                        quantity.setText(String.valueOf(selectedItemQuantity));

                    }
                }
            }
        });

        // Increment Listener
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedItemPrice !=null) {
                    if(productList.getType().equals("variable"))
                    {
                        if(selectedVariationProduct.getStockQuantity() > selectedItemQuantity)
                        {
                            selectedItemQuantity++;
                            quantity.setText(String.valueOf(selectedItemQuantity));
                        }
                        else
                        {
                            Toasty.error(getActivity(), getResources().getString(R.string.out_of_stock), Toast.LENGTH_SHORT, true).show();
                        }
                    }
                    else
                    {
                        if(productList.getStockQuantity() > selectedItemQuantity)
                        {
                            selectedItemQuantity++;
                            quantity.setText(String.valueOf(selectedItemQuantity));
                        }
                        else
                        {
                            Toasty.error(getActivity(), getResources().getString(R.string.out_of_stock), Toast.LENGTH_SHORT, true).show();
                        }
                    }

                }
                else
                {
                    Toasty.error(getActivity(), getResources().getString(R.string.string_some_thing_wrong), Toast.LENGTH_SHORT, true).show();
                }

            }
        });



        return view;
    }


    private void setValues(Product _productsClothes) {


        selectedItemPrice = null;
        selectedItemQuantity = 1;
        totalPrice = 0;

        sizeList.clear();
        //List<String> colorList = new ArrayList<>();

        for (int i = 0; i < _productsClothes.getAttributes().size(); i++) {

            if(_productsClothes.getAttributes().get(i).getName().equals("size"))
            {
                sizeID  = i;
                sizeList.addAll(_productsClothes.getAttributes().get(i).getOptions());

                //Toast.makeText(this, "" + String.valueOf(products.getAttributes().get(i).getOptions()), Toast.LENGTH_SHORT).show();
            }
            if(_productsClothes.getAttributes().get(i).getName().equals("color"))
            {
                colorID  = i;
            }
        }

        if(productList.getType().equals("simple"))
        {
            if(productList.getStockQuantity() !=null) {

                if (productList.getStockQuantity() > 0 || !productList.getStockStatus().equals("outofstock")) {
                    txtStockQuantity.setText(productList.getStockQuantity() + "  " + getResources().getString(R.string.in_stock));
                    txtStockQuantity.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    txtStockQuantity.setText(R.string.out_of_stock);

                    txtStockQuantity.setTextColor(getResources().getColor(R.color.red_700));

                }
            }
        }

        // Size


        if(sizeList.size() > 0)
        {
            sizeParentLay.setVisibility(View.VISIBLE);
            colorParentLay.setVisibility(View.VISIBLE);
            btnCheckout.setText(getResources().getString(R.string.please_select_option));
            setSizeLayout(sizeList);


        }
        else
        {
            sizeParentLay.setVisibility(View.GONE);
            colorParentLay.setVisibility(View.GONE);
            selectedItemPrice = productList.getPrice();
            selectedItemQuantity = 1;
            totalPrice = Double.parseDouble(selectedItemPrice) * 1;
            btnCheckout.setText(getString(R.string.btn_Proceed, getString(R.string.price_with_currency, totalPrice)));
        }
        //Toast.makeText(this, "" + sizeList.size() +" |||||  " + ads_name , Toast.LENGTH_SHORT).show();

        // Color

       // setColorLayout(colorList);


    }
    private void setSizeLayout(final List<String> sizeList) {
        sizeLay.removeAllViews();
        try {
            if (sizeList.size() > 0) {
                Map<String,VariationProduct> colorMap = new HashMap<>();
                for (int i = 0; i < sizeList.size(); i++) {
                    final TextView size = new TextView(getContext());
                    size.setText(sizeList.get(i));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        size.setBackground(getResources().getDrawable(R.drawable.border_grey));
                    } else {
                        size.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_grey));
                    }

                    // Change Border To Blue If Selected
                    try {
                        if (selectedSize.equals(sizeList.get(i))) {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                size.setBackground(getResources().getDrawable(R.drawable.border_accent));
                            } else {
                                size.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_accent));
                            }
                        }
                    } catch (NullPointerException ignore) {
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        size.setTextColor(getResources().getColor(R.color.black, null));
                    } else {
                        size.setTextColor(getResources().getColor(R.color.black));
                    }
                    size.setFocusableInTouchMode(false);
                    size.setFocusable(true);
                    size.setClickable(true);
                    size.setTextSize(16);

                    int dpValue = 8; // margin in dips
                    float d = getResources().getDisplayMetrics().density;
                    int margin = (int) (dpValue * d); // margin in pixels
                    FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
                            FlowLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(margin, margin, 0, 0);
                    size.setLayoutParams(params);
                    sizeLay.addView(size);
                    //List<String> colorList = new ArrayList<>();


                    // Size Click Listener
                    size.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TextView textView = (TextView) view;
                            selectedSize = textView.getText().toString();
                            selectedColor = null;
                            selectedItemPrice = null;
                            setSizeLayout(sizeList); // refresh to set selected

                            // Get Color of Selected Size & Set Color Layout
                            //TODO
                            colorMap.clear();
                            for (int j = 0; j < variationProduct.size(); j++) {

                                Log.d(TAG, "onClick: Color " + colorID + " Size" + sizeID);
                                if(variationProduct.get(j).getAttributes().get(sizeID).getOption().equals(selectedSize))
                                {
                                    colorMap.put(variationProduct.get(j).getAttributes().get(colorID).getOption() , variationProduct.get(j));
                                    //colorList.add(variationProduct.get(j).getAttributes().get(1).getOption());
                                }

                            }
                            selectedColor = null;
                            selectedItemPrice = null;
                            selectedItemQuantity = 1;
                            quantity.setText(String.valueOf(selectedItemQuantity));
                            btnCheckout.setText(getResources().getString(R.string.please_select_option));
                            setColorLayout(colorMap);


                        }
                    });
                }
            } else {
                sizeParentLay.setVisibility(View.GONE);
                selectedSize = "-";
            }
        } catch (NullPointerException e) {
            sizeParentLay.setVisibility(View.GONE);
            selectedSize = "-";
        }
    }

    private void setColorLayout(final Map<String , VariationProduct> colorList) {
        colorsLay.removeAllViews();
        try {
            if (colorList.size() > 0) {
                for (Map.Entry<String, VariationProduct> entry : colorList.entrySet()) {
                    final TextView color = new TextView(getContext());
                    color.setText(entry.getKey());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        color.setBackground(getResources().getDrawable(R.drawable.border_grey));
                    } else {
                        color.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_grey));
                    }

                    // Change Border To Blue If Selected
                    try {
                        if (selectedColor.equals(entry.getKey())) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                color.setBackground(getResources().getDrawable(R.drawable.border_accent));
                            } else {
                                color.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_accent));
                            }
                        }
                    } catch (NullPointerException ignore) {
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        color.setTextColor(getResources().getColor(R.color.black, null));
                    } else {
                        color.setTextColor(getResources().getColor(R.color.black));
                    }
                    if(entry.getValue().getStockStatus().equals("outofstock"))
                    {
                        color.setEnabled(false);
                        color.setBackground(getResources().getDrawable(R.drawable.border_red));
                    }
                    else
                    {

                        color.setEnabled(true);
                    }
                    color.setFocusableInTouchMode(false);
                    color.setFocusable(true);
                    color.setClickable(true);
                    color.setTextSize(16);

                    int dpValue = 8; // margin in dips
                    float d = getResources().getDisplayMetrics().density;
                    int margin = (int) (dpValue * d); // margin in pixels
                    FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
                            FlowLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(margin, margin, 0, 0);
                    color.setLayoutParams(params);
                    colorsLay.addView(color);

                    // Size Click Listener
                    color.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onClick(View view) {

                            try {
                                // Get Selected Item Price
                                name.setText(productList.getName());
                                selectedItemPrice = entry.getValue().getPrice();
                                price.setText(selectedItemPrice + " "+ getResources().getString(R.string.egypt_currency));
                                selectedItemQuantity = 1;
                                quantity.setText(String.valueOf(selectedItemQuantity));
                                if(!quantity.getText().toString().equals("")) {
                                    int selectedQuantity = Integer.parseInt(quantity.getText().toString());
                                    try {
                                        totalPrice = Double.parseDouble(selectedItemPrice) * selectedQuantity;
                                        btnCheckout.setText(getString(R.string.btn_Proceed, getString(R.string.price_with_currency, totalPrice)));

                                    }
                                    catch (Exception e)
                                    {
                                        Log.d(TAG, "onTextChanged: " + e.getLocalizedMessage());
                                    }



                                    //Toast.makeText(context, "" + sellProducts.get(i).getQuantity() , Toast.LENGTH_SHORT).show();
                                    //onCartClickListener.onProductClicked(products, i);
                                }
                                selectedVariationProduct = entry.getValue();
                                Glide.with(getContext())
                                        .load(entry.getValue().getImage().getSrc()) // image url
                                        .apply(new RequestOptions()

                                                .override(512, 512) // resizing
                                                .centerCrop())
                                        .into(src);  // imageview object
                                if(productList.getType().equals("variable"))
                                {
                                    if (selectedVariationProduct.getStockQuantity() > 0 || !selectedVariationProduct.getStockStatus().equals("outofstock")) {
                                        txtStockQuantity.setText(selectedVariationProduct.getStockQuantity() + "  " + getResources().getString(R.string.in_stock));
                                        txtStockQuantity.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                    } else {
                                        txtStockQuantity.setText(R.string.out_of_stock);

                                        txtStockQuantity.setTextColor(getResources().getColor(R.color.red_700));

                                    }
                                }
                                if (selectedSize.equals("-") || selectedSize != null) {
                                    TextView textView = (TextView) view;
                                    selectedColor = textView.getText().toString();
                                    /* //TODO
                                    Variant variant;
                                    if (selectedSize.equals("-")) // no size for product
                                    {
                                        variant = db_handler.getProductVariant(product.getId(), null, selectedColor);
                                        selectedItemPrice = variant.getPrice();
                                    } else {
                                        variant = db_handler.getProductVariant(product.getId(), selectedSize, selectedColor);
                                        selectedItemPrice = variant.getPrice();
                                    }

                                    selectedItemVariantId = variant.getId();

                                     */
                                    //price.setText("Rs." + selectedItemPrice);
                                    setColorLayout(colorList); // reload to refresh background
                                } else {
                                    Toast.makeText(getContext(), R.string.size_select, Toast.LENGTH_LONG).show();
                                }
                            } catch (NullPointerException e) {
                                Toast.makeText(getContext(), R.string.size_select, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            } else {
                colorParentLay.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            colorParentLay.setVisibility(View.GONE);
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void init() {


    }

    @Override
    public void onResume() {

        new CheckInternetConnection(getContext()).checkConnection();
        if (!SharedPrefManager.getInstance(getContext()).isLoggedIn()) {
            sendtoLogin();

        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();



    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        resultreceiver = (ProductResultReceiver) context;
        variantionResultReceiver = (VariantionResultReceiver) context;
        try {
            mCallback = (OnCallbackReceived) context;
        } catch (ClassCastException e) {

        }
    }


    void onCheckoutClick() {

        if (SharedPrefManager.getInstance(getActivity()).isLoggedIn()) {
            if(selectedVariationProduct != null) {
                if (selectedVariationProduct.getStockQuantity() != null) {
                    if (selectedVariationProduct.getStockQuantity() > 0) {
                        if (selectedColor != null && selectedSize != null) {
                            PendingProduct pendingproducts = new PendingProduct();
                            pendingproducts.setId(OrdersBase.getInstance().getmOrders().size());
                            pendingproducts.setProduct_id(selectedVariationProduct.getId());
                            pendingproducts.setParentid(productList.getId());
                            pendingproducts.setMerchant_id(productList.getStore().getId());
                            pendingproducts.setMerchant_name(productList.getStore().getShopName());
                            pendingproducts.setProduct_name(productList.getName());
                            pendingproducts.setProduct_shortname(productList.getName());
                            pendingproducts.setProduct_desc(productList.getDescription());
                            pendingproducts.setCat_id(productList.getCategories().get(0).getId());
                            pendingproducts.setType(productList.getType());
                            pendingproducts.setPrice(Double.parseDouble(selectedVariationProduct.getPrice()));

                            if (selectedVariationProduct.getImage() != null) {
                                pendingproducts.setThumb_image(selectedVariationProduct.getImage().getSrc());
                            }
                            //double discount = (Double.valueOf(selectedVariationProduct.getRegularPrice()) - Double.valueOf(selectedVariationProduct.getSalePrice())) / Double.valueOf(selectedVariationProduct.getRegularPrice()) * 100;
                            pendingproducts.setDiscount(0);
                            pendingproducts.setQuantity(selectedItemQuantity);
                            pendingproducts.setStock_quantity(selectedVariationProduct.getStockQuantity());
                            pendingproducts.setBarcode(productList.getSku());
                            pendingproducts.setTotal_price(totalPrice);

                            boolean inserted = OrdersBase.getInstance().InsertOrder(pendingproducts);
                            if (inserted) {

                                selectedItemPrice = null;
                                selectedItemQuantity = 1;
                                totalPrice = 0;
                                Toasty.success(getActivity(), getResources().getString(R.string.successfully_added_to_the_cart), Toast.LENGTH_SHORT).show();
                                Intent loginIntent = new Intent(getActivity(), CartReadyActivity.class);
                                startActivity(loginIntent);

                            } else {
                                Toasty.warning(getActivity(), getResources().getString(R.string.already_added_to_the_cart), Toast.LENGTH_SHORT).show();
                                //Toast.makeText(IndividualProductActivity.this, getResources().getString(R.string.already_added_to_the_cart), Toast.LENGTH_SHORT).show();
                                Intent loginIntent = new Intent(getActivity(), CartReadyActivity.class);
                                startActivity(loginIntent);
                            }
                        }

                    } else {
                        Toasty.error(getActivity(), getResources().getString(R.string.out_of_stock), Toast.LENGTH_SHORT, true).show();
                    }
                }
            }
            else if(selectedItemPrice != null && totalPrice != 0) {
                if (productList.getStockQuantity() != null) {
                    if (productList.getStockQuantity() > 0) {
                        PendingProduct pendingproducts = new PendingProduct();
                        pendingproducts.setId(OrdersBase.getInstance().getmOrders().size());
                        pendingproducts.setProduct_id(productList.getId());
                        pendingproducts.setMerchant_id(productList.getStore().getId());
                        pendingproducts.setMerchant_name(productList.getStore().getShopName());
                        pendingproducts.setProduct_name(productList.getName());
                        pendingproducts.setProduct_shortname(productList.getName());
                        pendingproducts.setProduct_desc(productList.getDescription());
                        pendingproducts.setCat_id(productList.getCategories().get(0).getId());
                        pendingproducts.setType(productList.getType());
                        pendingproducts.setPrice(Double.parseDouble(productList.getPrice()));
                        pendingproducts.setQuantity(selectedItemQuantity);
                        pendingproducts.setStock_quantity(productList.getStockQuantity());
                        if (productList.getImages() != null && productList.getImages().size() > 0) {

                            if (productList.getImages().get(0) != null) {
                                pendingproducts.setThumb_image(productList.getImages().get(0).getSrc());
                            }
                        }
                        //double discount = (Double.valueOf(selectedVariationProduct.getRegularPrice()) - Double.valueOf(selectedVariationProduct.getSalePrice())) / Double.valueOf(selectedVariationProduct.getRegularPrice()) * 100;
                        pendingproducts.setDiscount(0);
                        pendingproducts.setBarcode(productList.getSku());
                        pendingproducts.setTotal_price(totalPrice);

                        boolean inserted = OrdersBase.getInstance().InsertOrder(pendingproducts);
                        if (inserted) {

                            selectedItemPrice = null;
                            selectedItemQuantity = 1;
                            totalPrice = 0;
                            Toasty.success(getActivity(), getResources().getString(R.string.successfully_added_to_the_cart), Toast.LENGTH_SHORT).show();
                            Intent loginIntent = new Intent(getActivity(), CartReadyActivity.class);
                            startActivity(loginIntent);
                        } else {
                            Toasty.warning(getActivity(), getResources().getString(R.string.already_added_to_the_cart), Toast.LENGTH_SHORT).show();
                            //Toast.makeText(IndividualProductActivity.this, getResources().getString(R.string.already_added_to_the_cart), Toast.LENGTH_SHORT).show();
                            Intent loginIntent = new Intent(getActivity(), CartReadyActivity.class);
                            startActivity(loginIntent);
                        }
                    }
                }
            }
        }
        else
        {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }

    }
    public void sendtoLogin()
    {
        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
        startActivity(loginIntent);
        getActivity().finish();



    }
    public void getVariation() {
        ProductsAPIService service = RetrofitApi.retrofitReadWP3Product("products/" + productList.getId() + "/variations").create(ProductsAPIService.class);

        Call<List<VariationProduct>> call = service.getWP3SingleProductVarintaion(productList.getId());
        call.enqueue(new Callback<List<VariationProduct>>() {
            @Override
            public void onResponse(Call<List<VariationProduct>> call, Response<List<VariationProduct>> response) {
                if(response.body() != null) {

                    Log.d("Variation", "Variation: " + response.body().size());
                }


            }

            @Override
            public void onFailure(Call<List<VariationProduct>> call, Throwable t) {
                Log.d("Variation", "onFailure: " + t.getLocalizedMessage());


            }
        });
    }


}
