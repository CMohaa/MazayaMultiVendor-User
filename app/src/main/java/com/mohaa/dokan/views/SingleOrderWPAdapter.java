package com.mohaa.dokan.views;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mohaa.dokan.R;
import com.mohaa.dokan.interfaces.OnTrackerClickListener;
import com.mohaa.dokan.models.wp.Product;

import java.util.List;

public class SingleOrderWPAdapter extends RecyclerView.Adapter<SingleOrderWPAdapter.ViewHolder> {
    private List<Product> productsList;
    public Context context;


    private OnTrackerClickListener onTrackerClickListener;
    public SingleOrderWPAdapter(List<Product> _productList , OnTrackerClickListener onTrackerClickListener )
    {
        this.productsList = _productList;

        this.onTrackerClickListener = onTrackerClickListener;
    }


    @NonNull
    @Override
    public SingleOrderWPAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_layout , viewGroup , false);
        context = viewGroup.getContext();

        return new SingleOrderWPAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleOrderWPAdapter.ViewHolder viewHolder, final int i) {
        final Product products = productsList.get(i);
        String name = products.getName();
        String type = products.getType();

        if (products.getImages().size() > 0) {
            String img = products.getImages().get(0).getSrc();
            Glide.with(context)
                    .load(img) // image url
                    .apply(new RequestOptions()

                            .override(512, 512) // resizing
                            .centerCrop())
                    .into(viewHolder.src);  // imageview object
        }
        String trader = products.getStore().getShopName();
        viewHolder.name.setText(name);
        viewHolder.total_price.setText(viewHolder.name.getContext().getString(R.string.lbl_item_price_quantity, context.getString(R.string.price_with_currency, Double.parseDouble(products.getPrice())), products.getOrderd_quantity()));//product.getPrice()
        viewHolder.sold_by.setText(trader);
        if(type.equals("variation")) {
            int parent_id = products.getParentId();
            viewHolder.src.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTrackerClickListener.onTrackerClicked(products, i , parent_id);
                }
            });

        }
        else
        {

            viewHolder.src.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTrackerClickListener.onTrackerClicked(products, i , 0);
                }
            });
        }






        //viewHolder.price.setText(price);//cant cast to float

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private View mView;
        //private CardView cardView;
        private TextView name;
        private ImageView src;
        private TextView sold_by;
        private TextView total_price;//


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            //cardView = mView.findViewById(R.id.cardview1);
            name = mView.findViewById(R.id.titleText);
            src = mView.findViewById(R.id.productImage);
            sold_by = mView.findViewById(R.id.sold_by);
            total_price = mView.findViewById(R.id.totalPrice);




        }

    }
}
