package com.mohaa.dokan.views;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.mohaa.dokan.Controllers.activities_popup.OnAdressClickListener;
import com.mohaa.dokan.R;
import com.mohaa.dokan.models.Addressg;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    private List<Addressg> addressList;

    public Context context;

    private OnAdressClickListener onAdressClickListener;
    public AddressAdapter(List<Addressg> addressList , OnAdressClickListener onAdressClickListener )
    {
        this.addressList = addressList;

        this.onAdressClickListener = onAdressClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_address_layout , viewGroup , false);
        context = viewGroup.getContext();

        return new ViewHolder(view);
    }
    int row_index;
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final Addressg products = addressList.get(i);
        String name = products.getName();
        String address = products.getAddress();
        // city = products.getCity();
        //String government = products.getGtd();
        String mobile = products.getMobile();


        viewHolder.phone.setText(mobile);
        viewHolder.address_info.setText(address);
        viewHolder.address_item_panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAdressClickListener.onAdressClicked(products, i);
                row_index = i ;
                notifyDataSetChanged();
            }
        });
        if(row_index==i){
            viewHolder.address_item_panel.setBackgroundResource(R.drawable.border_blue);
            viewHolder.selected_address.setVisibility(View.VISIBLE);
            //viewHolder.address.setTextColor(Color.parseColor("#d35400"));
        }
        else
        {
            viewHolder.selected_address.setVisibility(View.INVISIBLE);
            viewHolder.address_item_panel.setBackgroundColor(Color.parseColor("#000000"));
            //viewHolder.address.setTextColor(Color.parseColor("#000000"));
        }
        //viewHolder.price.setText(price);//cant cast to float

    }


    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        int selectedItemQuantity = 1;
        private CardView address_item_panel;
        private View mView;

        private TextView address_info;
        private TextView phone;//
        private TextView selected_address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;


            address_info = mView.findViewById(R.id.customer_address_info);
            phone = mView.findViewById(R.id.customer_phone);
            address_item_panel = mView.findViewById(R.id.address_item_panel);
            selected_address = mView.findViewById(R.id.selected_address);


        }
        private void FillAddress(String address  , String city , String goverment , TextView addressTextView) {
            Spannable contentString = new SpannableStringBuilder(address + "   " + city + "   " + goverment);
            contentString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.highlight_text)),
                    0, 64, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            addressTextView.setText(contentString);

        }

    }
}
