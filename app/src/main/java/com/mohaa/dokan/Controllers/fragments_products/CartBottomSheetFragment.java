package com.mohaa.dokan.Controllers.fragments_products;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import com.mohaa.dokan.Auth.LoginActivity;
import com.mohaa.dokan.Controllers.activities_popup.AddressActivity;
import com.mohaa.dokan.R;
import com.mohaa.dokan.Utils.helper.SharedPrefManager;
import com.mohaa.dokan.interfaces.OnCallbackReceived;
import com.mohaa.dokan.manager.OrdersBase;
import com.mohaa.dokan.models.PendingProduct;
import com.mohaa.dokan.networksync.CheckInternetConnection;
import com.mohaa.dokan.views.CartProductsAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



public class CartBottomSheetFragment extends BottomSheetDialogFragment implements CartProductsAdapter.CartProductsAdapterListener  {


    OnCallbackReceived mCallback;
    private CartProductsAdapter mAdapter;
    private List<PendingProduct> cartItems;
    private List<PendingProduct> products_list;
    RecyclerView recyclerView;
    Button btnCheckout;
    private ImageView brn_close;


    public CartBottomSheetFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Making bottom sheet expanding to full height by default
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(dialog1 -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog1;

            FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
        });
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart_bottom_sheet, container, false);
        recyclerView  = view.findViewById(R.id.recycler_view);
        btnCheckout = view.findViewById(R.id.btn_checkout);
        brn_close = view.findViewById(R.id.ic_close);
        FStore();
        brn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckoutClick();
            }
        });

        //mAdapter.setData(cartItems);
        //setTotalPrice();




        return view;
    }

    private void FStore() {

        products_list = new ArrayList<>();
        cartItems = OrdersBase.getInstance().getmOrders();
        init();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void init() {


        /*
        for (int i = 0 ; i <_products_list.size() ; i++)
        {
            cartItems.add(new SellProducts(String.valueOf(i) , _products_list.get(i),1));

        }

         */

        mAdapter = new CartProductsAdapter(getActivity(),cartItems, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        setTotalPrice();
    }

    private void setTotalPrice() {
        if (cartItems != null) {
            //float price = Utils.getCartPrice(cartItems);
            double total = 0;
            for (int i = 0; i < OrdersBase.getInstance().getmOrders().size(); i++) {
                total += OrdersBase.getInstance().getmOrders().get(i).getTotal_price();
            }
            if (total > 0) {
                btnCheckout.setText(getString(R.string.btn_Proceed, getString(R.string.price_with_currency, total)));
            } else {
                // if the price is zero, dismiss the dialog
                dismiss();
            }
        }
    }

    @Override
    public void onResume() {

        new CheckInternetConnection(getContext()).checkConnection();
        if (!SharedPrefManager.getInstance(getContext()).isLoggedIn()) {
            sendtoLogin();

        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cartItems != null) {
           // cartItems.removeChangeListener(cartItemRealmChangeListener);
        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        try {
            mCallback = (OnCallbackReceived) context;
        } catch (ClassCastException e) {

        }
    }


    void onCheckoutClick() {

        if (!SharedPrefManager.getInstance(getContext()).isLoggedIn()) {
            sendtoLogin();
            dismiss();

        }
        else {
            startActivity(new Intent(getActivity(), AddressActivity.class));
            dismiss();
        }

    }
    public void sendtoLogin()
    {
        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
        startActivity(loginIntent);
        getActivity().finish();



    }
    @Override
    public void onCartItemRemoved(int index, PendingProduct cartItem) {
        cartItems.remove(index);
        OrdersBase.getInstance().RemoveOrder(cartItem);
        mAdapter.notifyItemRemoved(index);
        mAdapter.notifyItemRangeChanged(index, cartItems.size());
        setTotalPrice();
        mCallback.Update();


    }

    @Override
    public void onCartItemAdded(int index) {

    }



    @Override
    public void onQuantityChnaged(int index) {
        setTotalPrice();
    }



}
