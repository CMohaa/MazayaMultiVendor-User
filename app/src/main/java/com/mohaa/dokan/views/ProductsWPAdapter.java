package com.mohaa.dokan.views;

import android.content.Context;
import android.graphics.Paint;
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
import com.mohaa.dokan.interfaces.OnCallbackReceived;
import com.mohaa.dokan.interfaces.OnProductClickListener;

import java.text.DecimalFormat;
import java.util.List;

public class ProductsWPAdapter extends RecyclerView.Adapter<ProductsWPAdapter.ViewHolder>  implements OnCallbackReceived {
    private List<com.mohaa.dokan.models.wp.Product> productList;
    public Context context;

    private CartProductsAdapter.CartProductsAdapterListener listener;
    private OnProductClickListener onProductClickListener;
    public ProductsWPAdapter(List<com.mohaa.dokan.models.wp.Product> _tradersList , OnProductClickListener onProductClickListener , CartProductsAdapter.CartProductsAdapterListener listener )
    {
        this.productList = _tradersList;
        this.listener = listener;
        this.onProductClickListener = onProductClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.products_card_layout , viewGroup , false);
        context = viewGroup.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final com.mohaa.dokan.models.wp.Product product = productList.get(i);
        String name = product.getName();
        String desc = product.getDescription();
        String shortdesc = product.getShortDescription();
        String price = product.getPrice();
        String regularprice = product.getRegularPrice();
        String saleprice = product.getSalePrice();



        Boolean onSale = product.getOnSale();
       // Boolean inStock = product.getInStock();
        String rate = product.getAverageRating();
        int ratecount = product.getRatingCount();
        //viewHolder.name.setText(name);
        //viewHolder.desc.setText(desc);

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
        //double new_price = price  - ((price *discount) / 100);
        viewHolder.name.setText(name);

        viewHolder.Discount.setText(context.getResources().getString(R.string.best_value));

        if(saleprice != null && !saleprice.equals(""))
        {

            double discount = ( Double.valueOf(regularprice)  - Double.valueOf(saleprice) ) / Double.valueOf(regularprice) * 100;
            viewHolder.old_price.setVisibility(View.VISIBLE);
            viewHolder.Discount.setText(String.valueOf(new DecimalFormat("##").format(discount))+ "%" + context.getResources().getString(R.string.off));
            viewHolder.old_price.setText( context.getResources().getString(R.string.save) + " " +  String.valueOf(Double.valueOf(regularprice)  - Double.valueOf(saleprice)+ " " + context.getResources().getString(R.string.egypt_currency)));
            viewHolder.old_price.setPaintFlags( viewHolder.old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }



        //viewHolder.sponsored.setVisibility(View.GONE);
        /*
        if(onSale)
        {
            viewHolder.Best_Seller.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.Best_Seller.setVisibility(View.INVISIBLE);
        }

         */





        viewHolder.price.setText(String.valueOf(price)+ " " + context.getResources().getString(R.string.egypt_currency));
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






    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public void Update() {


    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private View mView;
        private TextView name;
        private ImageView src;
        private TextView price;//discountedCardPrice
        private TextView old_price;//discountedCardPriceprivate TextV
        private TextView sponsored;//discountedCardPrice
        private RatingBar rating;
        private TextView Discount;
        private TextView  Best_Seller;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            name = mView.findViewById(R.id.ProductName);
            src = mView.findViewById(R.id.ProductPoster);
            price = mView.findViewById(R.id.ProductPrice);
            old_price = mView.findViewById(R.id.ProductOldPrice);
            sponsored = mView.findViewById(R.id.ProductSponsored);
            Discount = mView.findViewById(R.id.ProductDiscount);

            rating = mView.findViewById(R.id.ProductRating);

            Best_Seller = mView.findViewById(R.id.Best_Seller);



        }
        public void UpdateCart(com.mohaa.dokan.models.wp.Product products , double new_price , int quantity , double total_cost)
        {
            /*
            PendingProduct pendingproducts = new PendingProduct();
            pendingproducts.setId(OrdersBase.getInstance().getmOrders().size());
            pendingproducts.setProduct_id(products.getId());
            pendingproducts.setMerchant_id(products.getMerchant_id());
            pendingproducts.setMerchant_name(products.getMerchant_name());
            pendingproducts.setProduct_name(products.getProduct_name());
            pendingproducts.setProduct_shortname(products.getProduct_shortname());
            pendingproducts.setProduct_desc(products.getProduct_desc());
            pendingproducts.setCat_id(products.getCat_id());
            pendingproducts.setCat_parent_id(products.getCat_parent_id());
            pendingproducts.setCat_gparent_id(products.getCat_gparent_id());
            pendingproducts.setType(products.getType());
            pendingproducts.setPrice(new_price);
            pendingproducts.setQuantity(quantity);
            pendingproducts.setThumb_image(products.getThumb_image());
            pendingproducts.setDiscount(products.getDiscount());
            pendingproducts.setDepartment(products.getDepartment());
            pendingproducts.setStatus(products.getStatus());
            pendingproducts.setPack(products.getPack());
            pendingproducts.setCompany(products.getCompany());
            pendingproducts.setBarcode(products.getBarcode());
            pendingproducts.setTotal_price(total_cost);
            boolean inserted =  OrdersBase.getInstance().InsertOrder(pendingproducts);
            if(inserted)
            {
                Toast.makeText(context, context.getResources().getString(R.string.successfully_added_to_the_cart), Toast.LENGTH_SHORT).show();

            }
            else
            {
                //Toast.makeText(context, context.getResources().getString(R.string.already_added_to_the_cart), Toast.LENGTH_SHORT).show();
            }

             */

        }




    }

}
