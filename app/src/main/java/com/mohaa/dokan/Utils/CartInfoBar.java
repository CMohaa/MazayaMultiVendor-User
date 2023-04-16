package com.mohaa.dokan.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.mohaa.dokan.R;

import androidx.annotation.Nullable;



public class CartInfoBar extends RelativeLayout {
    private CartInfoBarListener listener;


    TextView cartInfo;
    RelativeLayout container;


    public CartInfoBar(Context context) {
        super(context);
        init(context, null);
    }

    public CartInfoBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void init(Context context, @Nullable AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_cart_info_bar, null);
        cartInfo = view.findViewById(R.id.cart_price);
        container = view.findViewById(R.id.container);
        container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onClick();
            }
        });

        addView(view);
    }

    public void setListener(CartInfoBarListener listener) {
        this.listener = listener;
    }



    public void setData(int itemCount, String price) {
        cartInfo.setText(getContext().getString(R.string.cart_info_bar_data, itemCount, price));
    }

    public interface CartInfoBarListener {
        void onClick();
    }
}