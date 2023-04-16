package com.mohaa.dokan.Controllers.activities_popup;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mohaa.dokan.Controllers.BaseActivity;
import com.mohaa.dokan.R;
import com.mohaa.dokan.Utils.ConstantUI;
import com.mohaa.dokan.Utils.TextViewBold;
import com.mohaa.dokan.Utils.TextViewMedium;


public class ProductQuickDetailActivity extends BaseActivity {


    TextViewMedium tvSubTitle;
    ImageView ivProduct;
    TextViewBold tvProductName;
    WebView wvDetail;

    public static String changedHeaderHtml(String htmlText) {

        String head = "<head><meta name=\"viewport\" content=\"width=device-width, user-scalable=yes\" /></head>";

        String closedTag = "</body></html>";
        String changeFontHtml = head + htmlText + closedTag;
        return changeFontHtml;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_quick_detail);

        tvSubTitle  = findViewById(R.id.tvSubTitle);
        ivProduct  = findViewById(R.id.ivProduct);
        tvProductName  = findViewById(R.id.tvProductName);
        wvDetail  = findViewById(R.id.wvDetail);


        String description = getIntent().getExtras().getString(ConstantUI.PRODUCT_DETAILS);

        String details = getIntent().getExtras().getString(ConstantUI.PRODUCT_DETAILS);


        if (description != "") {

            //details =  details.replaceAll("width=\"\\d+\"" , "width=\"380\"").replaceAll("height=\"\\d+\"" , "height=\"280\"");
            details =  details.replaceAll("width=\"\\d+\"" , "width=\"380\"").replaceAll("height=\"\\d+\"" , "height=\"280\"").replaceAll("<p>" , "<p style=\"color:white;\">");
            // details =  details.replaceAll("<p>" , "<p style=\"color:white;\">");

            String styleDetails = "<body style=\"background-color:black;\">" + details + "</body>";
            //details =  details.replaceAll("width=\"1956\"" , "width=\""+minwidth+"\"").replaceAll("height=\"724\"" , "height=\""+minheight+"\"");
            wvDetail.loadData(styleDetails, "text/html; charset=UTF-8", null);
            /*
            wvDetail.setInitialScale(1);

            wvDetail.getSettings().setLoadsImagesAutomatically(true);
            wvDetail.getSettings().setUseWideViewPort(true);
            wvDetail.loadData(changedHeaderHtml(description), "text/html", "UTF-8");
            wvDetail.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
            wvDetail.getSettings().setBuiltInZoomControls(true);

             */
        }

        String subTitle = getIntent().getExtras().getString(ConstantUI.PRODUCT_CATEGORY);
        String productImage = getIntent().getExtras().getString(ConstantUI.PRODUCT_IMAGE);
        String productName = getIntent().getExtras().getString(ConstantUI.PRODUCT_TITLE);

        tvSubTitle.setText(subTitle);
        tvProductName.setText(productName);
        Toast.makeText(this, "" + productName, Toast.LENGTH_SHORT).show();
        tvProductName.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        if (productImage.length() > 0) {
            ivProduct.setVisibility(View.VISIBLE);

            Glide.with(this)
                    .load(productImage) // image url
                    .apply(new RequestOptions()
                            .override(512, 512) // resizing
                            .centerCrop())
                    .into(ivProduct);  // imageview object
        } else {
            ivProduct.setVisibility(View.INVISIBLE);
        }
        showBackButton();
    }


}
