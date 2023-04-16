package com.mohaa.dokan.views;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.mohaa.dokan.R;
import com.mohaa.dokan.Utils.GetTimeAgo;
import com.mohaa.dokan.interfaces.OnOrderClickListener;
import com.mohaa.dokan.models.Order;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder> {
    private List<Order> ordersList;
    public Context context;


    private OnOrderClickListener onOrderClickListener;
    public MyOrdersAdapter(List<Order> _ordersList , OnOrderClickListener onOrderClickListener )
    {
        this.ordersList = _ordersList;

        this.onOrderClickListener = onOrderClickListener;
    }


    @NonNull
    @Override
    public MyOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_card_layout , viewGroup , false);
        context = viewGroup.getContext();

        return new MyOrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrdersAdapter.ViewHolder viewHolder, final int i) {
        final Order orders = ordersList.get(i);
        String message = orders.getMessage();
        viewHolder.name.setText(message);
        long lastTime = orders.getCreated_at();

        String lastSeenTime = GetTimeAgo.getTimeAgo(lastTime, context);

        viewHolder.mDate.setText(lastSeenTime);
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




    }

}
