package com.mohaa.dokan.views;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mohaa.dokan.R;
import com.mohaa.dokan.Utils.GetTimeAgo;
import com.mohaa.dokan.interfaces.OnOrderClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyOrdersWPAdapter extends RecyclerView.Adapter<MyOrdersWPAdapter.ViewHolder> {
    private List<com.mohaa.dokan.models.wp.Order> ordersList;
    public Context context;


    private OnOrderClickListener onOrderClickListener;
    public MyOrdersWPAdapter(List<com.mohaa.dokan.models.wp.Order> _ordersList , OnOrderClickListener onOrderClickListener )
    {
        this.ordersList = _ordersList;

        this.onOrderClickListener = onOrderClickListener;
    }


    @NonNull
    @Override
    public MyOrdersWPAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_card_layout , viewGroup , false);
        context = viewGroup.getContext();

        return new MyOrdersWPAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrdersWPAdapter.ViewHolder viewHolder, final int i) {
        final com.mohaa.dokan.models.wp.Order orders = ordersList.get(i);
        String message = orders.getNumber();
        String status = orders.getStatus();
        viewHolder.name.setText(message  + " | " + status);
        String lastTime = orders.getDateModified();

        //String lastSeenTime = GetTimeAgo.getTimeAgo(lastTime, context);

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {

            // Date from = inputFormat.parse(string_from);
            Date to = inputFormat.parse(lastTime);
            // long from_mil = from.getTime();
            long to_mil = to.getTime();
            viewHolder.setTime(to_mil);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        viewHolder.order_panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOrderClickListener.onOrderClicked(orders, i);

            }
        });

        //viewHolder.price.setText(price);//cant cast to float

    }


    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private View mView;
        private RelativeLayout order_panel;
        private TextView name;
        private TextView mDate;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            order_panel = mView.findViewById(R.id.order_panel);
            name = mView.findViewById(R.id.OrderOwner);
            mDate = mView.findViewById(R.id.OrderTime);





        }


        public void setTime(long time) {

            GetTimeAgo getTimeAgo = new GetTimeAgo();

            long lastTime = time;

            String lastSeenTime = GetTimeAgo.getTimeAgo(lastTime, context);

            mDate.setText(lastSeenTime);

        }

    }


}
