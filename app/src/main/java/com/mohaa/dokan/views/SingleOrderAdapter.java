package com.mohaa.dokan.views;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mohaa.dokan.R;
import com.mohaa.dokan.interfaces.OnTrackerClickListener;
import com.mohaa.dokan.models.wp.LineItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SingleOrderAdapter extends RecyclerView.Adapter<SingleOrderAdapter.ViewHolder> {
    private List<LineItem> productsList;
    public Context context;


    private OnTrackerClickListener onTrackerClickListener;
    public SingleOrderAdapter(List<LineItem> _productList , OnTrackerClickListener onTrackerClickListener )
    {
        this.productsList = _productList;

        this.onTrackerClickListener = onTrackerClickListener;
    }


    @NonNull
    @Override
    public SingleOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_layout , viewGroup , false);
        context = viewGroup.getContext();

        return new SingleOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleOrderAdapter.ViewHolder viewHolder, final int i) {
        final LineItem products = productsList.get(i);
        String name = products.getName();
       // String img = products.getImages().get(0).getSrc();
        //String trader = products.getStore().getShopName();
        viewHolder.name.setText(name);
        viewHolder.total_price.setText(viewHolder.name.getContext().getString(R.string.lbl_item_price_quantity, context.getString(R.string.price_with_currency, Double.parseDouble(products.getTotal())), products.getQuantity()));//product.getPrice()
      //  viewHolder.sold_by.setText(trader);
        viewHolder.src.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTrackerClickListener.onTrackerClicked(products, i);
            }
        });


        /*
        Glide.with(context)
                .load(img) // image url
                .apply(new RequestOptions()

                        .override(512, 512) // resizing
                        .centerCrop())
                .into(viewHolder.src);  // imageview object

         */



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
