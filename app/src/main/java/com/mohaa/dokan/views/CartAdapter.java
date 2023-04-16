package com.mohaa.dokan.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<PendingProduct> productsList;

    public Context context;


    private OnCartClickListener onCartClickListener;
    public CartAdapter(List<PendingProduct> _productList , OnCartClickListener onCartClickListener )
    {
        this.productsList = _productList;

        this.onCartClickListener = onCartClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cart_layout , viewGroup , false);
        context = viewGroup.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final PendingProduct products = productsList.get(i);
        String name = products.getProduct_name();
        final double price = products.getPrice();
        int quantity = products.getQuantity();
        double total_cost = products.getTotal_price();
        viewHolder.selectedItemQuantity  = (int) Math.round(quantity);
        viewHolder.name.setText(name);
        //viewHolder.price.setText(String.valueOf(price));
        String img = products.getThumb_image();
        viewHolder.total_price.setText(String.valueOf(total_cost));
        viewHolder.quantity.setText(String.valueOf(quantity));
        viewHolder.quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!viewHolder.quantity.getText().toString().equals("")) {
                    String quantity = viewHolder.quantity.getText().toString();
                    String total_price_T = String.valueOf((price * Integer.parseInt(quantity)));
                    viewHolder.total_price.setText(total_price_T);
                    productsList.get(i).setTotal_price((price * Integer.parseInt(quantity)));
                    productsList.get(i).setQuantity(Integer.parseInt(quantity));
                    //Toast.makeText(context, "" + productsList.get(i).getQuantity() , Toast.LENGTH_SHORT).show();
                    //onCartClickListener.onProductClicked(products, i);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        viewHolder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.selectedItemQuantity != 1) {
                    viewHolder.selectedItemQuantity--;
                    viewHolder.quantity.setText(String.valueOf(viewHolder.selectedItemQuantity));
                }
            }
        });

        // Increment Listener
        viewHolder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(products.getStock_quantity() > viewHolder.selectedItemQuantity) {
                    viewHolder.selectedItemQuantity++;
                    viewHolder.quantity.setText(String.valueOf(viewHolder.selectedItemQuantity));
                }
            }
        });
        viewHolder.src.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCartClickListener.onCartPendingClicked(products, i);
            }
        });
        //viewHolder.price.setText(price);//cant cast to float
        Glide.with(context)
                .load(img) // image url
                .apply(new RequestOptions()

                        .override(512, 512) // resizing
                        .centerCrop())
                .into(viewHolder.src);  // imageview object
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        int selectedItemQuantity;
        private View mView;
        private CardView cardView;
        private TextView name;
        private ImageView src;
        //private TextView price;//
        private TextView total_price;//
        private TextView quantity;//itemCardSeller
        private ImageView minus, plus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            cardView = mView.findViewById(R.id.cart_item_panel);
            name = mView.findViewById(R.id.titleText);
            src = mView.findViewById(R.id.productImage);
           // price = mView.findViewById(R.id.eachPrice);
            total_price = mView.findViewById(R.id.totalPrice);
            quantity = mView.findViewById(R.id.quantityEditText);
            minus =  mView.findViewById(R.id.minus);
            plus =  mView.findViewById(R.id.plus);
            //selectedItemQuantity = Integer.parseInt(quantity.getText().toString());


        }

    }
}
