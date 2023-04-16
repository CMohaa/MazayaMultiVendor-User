package com.mohaa.dokan.views;


import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.mohaa.dokan.R;
import com.mohaa.dokan.Utils.TextViewBold;
import com.mohaa.dokan.Utils.Toasty.Toasty;
import com.mohaa.dokan.models.PendingProduct;

import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import static com.mohaa.dokan.Controllers.fragments_home.HomeFragment.TAG;


public class CartProductsAdapter extends RecyclerView.Adapter<CartProductsAdapter.MyViewHolder> {

    private Context context;
    private List<PendingProduct> sellProducts;
    private CartProductsAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView price ,price1;
        private ImageView src;
        private ImageView btnRemove;
        int selectedItemQuantity;
        private TextViewBold quantity;//itemCardSeller
        private ImageView minus, plus;
        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.tvName);
            price = view.findViewById(R.id.tvPrice);
            price1 = view.findViewById(R.id.tvPrice1);
            src = view.findViewById(R.id.ivImage);
            btnRemove = view.findViewById(R.id.ivDelete);

            quantity = view.findViewById(R.id.tvQuantity);
            minus =  view.findViewById(R.id.tvDecrement);
            plus =  view.findViewById(R.id.tvIncrement);
        }
    }


    public CartProductsAdapter(Context context, List<PendingProduct> sellProducts, CartProductsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.sellProducts = sellProducts;
    }
    public CartProductsAdapter(Context context, List<PendingProduct> sellProducts) {
        this.context = context;
        this.listener = listener;

    }
    public void setData(List<PendingProduct> sellProducts) {
        if (sellProducts == null) {
            this.sellProducts = Collections.emptyList();
        }

        this.sellProducts = sellProducts;

        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int i) {
        final PendingProduct products = sellProducts.get(i);
        String name = products.getProduct_name();
        String img = products.getThumb_image();
        final double price = products.getPrice();
        double quantity = products.getQuantity();
        Log.d(TAG, "onBindViewHolder: " + quantity);
        double total_cost = products.getTotal_price();
        holder.name.setText(name);//product.getName()
        holder.price.setText(holder.name.getContext().getString(R.string.lbl_item_price_quantity, context.getString(R.string.price_with_currency, products.getPrice()), (int)quantity));//product.getPrice()
       // Log.d(TAG, "onBindViewHolder: " +quantity);
        holder.selectedItemQuantity  = (int) Math.round(quantity);
        holder.quantity.setText(String.valueOf(quantity));
        holder.quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!holder.quantity.getText().toString().equals("")) {



                    try {
                        int quantity = Integer.parseInt(holder.quantity.getText().toString());
                        Log.d(TAG, "onBindViewHolder: " + quantity);
                        String total_price_T = String.valueOf((price * quantity));
                        sellProducts.get(i).setTotal_price((price * quantity));
                        sellProducts.get(i).setQuantity(quantity);
                        holder.price.setText(holder.name.getContext().getString(R.string.lbl_item_price_quantity, context.getString(R.string.price_with_currency, products.getPrice()), (int) quantity));//product.getPrice()
                    }
                    catch (Exception e)
                    {
                        Log.d(TAG, "onTextChanged: " + e.toString());
                       // Toast.makeText(context, "" + e.toString(), Toast.LENGTH_SHORT).show();
                    }



                    //Toast.makeText(context, "" + sellProducts.get(i).getQuantity() , Toast.LENGTH_SHORT).show();
                    //onCartClickListener.onProductClicked(products, i);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.selectedItemQuantity != 1) {
                    holder.selectedItemQuantity--;
                    holder.quantity.setText(String.valueOf(holder.selectedItemQuantity));
                    listener.onQuantityChnaged(i);
                }
            }
        });

        // Increment Listener
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(products.getStock_quantity() > holder.selectedItemQuantity) {
                    holder.selectedItemQuantity++;
                    holder.quantity.setText(String.valueOf(holder.selectedItemQuantity));
                    listener.onQuantityChnaged(i);
                }
                else
                {
                    Toasty.error(context, context.getResources().getString(R.string.out_of_stock), Toast.LENGTH_SHORT, true).show();
                }
            }
        });

        Glide.with(context)
                .load(img) // image url
                .apply(new RequestOptions()

                        .override(512, 512) // resizing
                        .centerCrop())
                .into(holder.src);  // imageview object
        if (listener != null)
            holder.btnRemove.setOnClickListener(view -> listener.onCartItemRemoved(i, products));
    }

    @Override
    public int getItemCount() {
        return sellProducts.size();
    }

    public interface CartProductsAdapterListener {
        void onCartItemRemoved(int index, PendingProduct cartItem);
        void onCartItemAdded(int index);
        void onQuantityChnaged(int index);
    }


}

