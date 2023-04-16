package com.mohaa.dokan.views;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mohaa.dokan.R;
import com.mohaa.dokan.Utils.countdownview.CountdownView;
import com.mohaa.dokan.interfaces.OnProductClickListener;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProductsSnapWPAdapter extends RecyclerView.Adapter<ProductsSnapWPAdapter.ViewHolder>   {
    private List<com.mohaa.dokan.models.wp.Product> productsList;
    public Context context;


    private OnProductClickListener onProductClickListener;
    public ProductsSnapWPAdapter(List<com.mohaa.dokan.models.wp.Product> _productList , OnProductClickListener onProductClickListener )
    {
        this.productsList = _productList;

        this.onProductClickListener = onProductClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card_mini_layout , viewGroup , false);
        context = viewGroup.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final com.mohaa.dokan.models.wp.Product product = productsList.get(i);

        String name = product.getName();

        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String desc = product.getDescription();
        String shortdesc = product.getShortDescription();
        String price = product.getPrice();
        String regularprice = product.getRegularPrice();
        String saleprice = product.getSalePrice();

        Boolean onSale = product.getOnSale();
       // Boolean inStock = product.getInStock();
        String rate = product.getAverageRating();
        int ratecount = product.getRatingCount();


        viewHolder.name.setText(name);

        if(rate !=null  && ratecount != 0) {
            double rating = Double.valueOf(rate);
            viewHolder.rating.setRating((float) rating);
            //viewHolder.review_panel.setVisibility(View.VISIBLE);
            viewHolder.Best_Seller.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.rating.setRating((float) 0);
            //  viewHolder.review_panel.setVisibility(View.GONE);
            viewHolder.Best_Seller.setVisibility(View.GONE);
        }


        viewHolder.Discount.setText(context.getResources().getString(R.string.best_value));
        if(saleprice != null && !saleprice.equals(""))
        {

            double discount = ( Double.valueOf(regularprice)  - Double.valueOf(saleprice) ) / Double.valueOf(regularprice) * 100;
            viewHolder.Discount.setText(String.valueOf(new DecimalFormat("##").format(discount))+ "%"  + context.getResources().getString(R.string.off));

        }

    if(product.getDateOnSaleFromGmt() != null && !product.getDateOnSaleToGmt().equals("")) {
       // String string_from = product.getDateOnSaleFrom();
        String string_to = product.getDateOnSaleTo();
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {

           // Date from = inputFormat.parse(string_from);
            Date to = inputFormat.parse(string_to);
           // long from_mil = from.getTime();
            long to_mil = to.getTime();
            long diff = to_mil - timestamp.getTime();
            viewHolder.mCvCountdownView.setVisibility(View.VISIBLE);
            viewHolder.mCvCountdownView.start(diff); // Millisecond
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
    else
    {
        viewHolder.mCvCountdownView.setVisibility(View.GONE);
    }
        //viewHolder.sponsored.setVisibility(View.GONE);

        viewHolder.price.setText(String.valueOf(price)+ " "+ context.getResources().getString(R.string.egypt_currency));
        if(product.getImages().size() != 0)
        {
            String img = product.getImages().get(0).getSrc();
            Glide.with(context)
                    .load(img) // image url
                    .apply(new RequestOptions()
                            .override(512, 512) // resizing
                            .centerCrop())
                    .into(viewHolder.src);  // imageview object
        }
        viewHolder.src.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProductClickListener.onProductClicked(product, i);

            }
        });

        //viewHolder.price.setText(price);//cant cast to float

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private View mView;

        private TextView name;
        private ImageView src;
        private TextView price;//discountedCardPrice
        private TextView Discount;
        private RatingBar rating;
        private TextView Best_Seller;
        private CountdownView mCvCountdownView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            name = mView.findViewById(R.id.ProductName);
            src = mView.findViewById(R.id.ProductPoster);
            price = mView.findViewById(R.id.ProductPrice);
            rating = mView.findViewById(R.id.ProductRating);

            Best_Seller = mView.findViewById(R.id.Best_Seller);

            Discount = mView.findViewById(R.id.ProductDiscount);

            mCvCountdownView = (CountdownView) mView.findViewById(R.id.cv_countdownView);


        }
    }
}
