package com.mohaa.dokan.views;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.mohaa.dokan.R;
import com.mohaa.dokan.interfaces.OnCartClickListener;
import com.mohaa.dokan.models.PendingProduct;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SingleCartAdapter extends RecyclerView.Adapter<SingleCartAdapter.ViewHolder>  {
    private List<PendingProduct> productsList;
    public Context context;


    private OnCartClickListener onCartClickListener;
    public SingleCartAdapter(List<PendingProduct> _productList , OnCartClickListener onCartClickListener )
    {
        this.productsList = _productList;

        this.onCartClickListener = onCartClickListener;
    }
    public void setList(List<PendingProduct> list) {
        this.productsList = list;

    }

    @NonNull
    @Override
    public SingleCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_layout , viewGroup , false);
        context = viewGroup.getContext();

        return new SingleCartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleCartAdapter.ViewHolder viewHolder, final int i) {
        final PendingProduct products = productsList.get(i);
        String name = products.getProduct_name();
        String img = products.getThumb_image();
        String trader = products.getMerchant_name();
        viewHolder.name.setText(name);
        viewHolder.total_price.setText(viewHolder.name.getContext().getString(R.string.lbl_item_price_quantity, context.getString(R.string.price_with_currency, products.getPrice()), (int)products.getQuantity()));//product.getPrice()
        viewHolder.sold_by.setText(trader);
        viewHolder.src.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCartClickListener.onCartPendingClicked(products, i);
            }
        });

        Glide.with(context)
                .load(img) // image url
                .apply(new RequestOptions()

                        .override(512, 512) // resizing
                        .centerCrop())
                .into(viewHolder.src);  // imageview object

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
