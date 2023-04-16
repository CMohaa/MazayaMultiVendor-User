package com.mohaa.dokan.Controllers.fragments_traders;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;


import com.mohaa.dokan.R;
import com.mohaa.dokan.Utils.EndlessRecyclerViewScrollListener;
import com.mohaa.dokan.Utils.reviewratings.RatingReviews;
import com.mohaa.dokan.interfaces.MyResultReceiver;
import com.mohaa.dokan.manager.ApiServices.TraderAPIService;
import com.mohaa.dokan.manager.RetrofitApi;
import com.mohaa.dokan.models.wp.Vendor;
import com.mohaa.dokan.models.wp.VendorComment;
import com.mohaa.dokan.models.wp.VendorEmpty;
import com.mohaa.dokan.views.CommentsVendorRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RatingFragment extends Fragment {


    private static final String TAG = "TraderActivity";

    public RatingFragment() {
        // Required empty public constructor
    }

    public MyResultReceiver resultreceiver;
    private RecyclerView commentsRecyclerView;
    private CommentsVendorRecyclerAdapter commentsRecyclerAdapter;
    private List<VendorComment> commentsList;
    private RatingBar ratingbar;
    private int userPage = 1;
    private RatingReviews ratingReviews;
    private int traderID;
    private RatingBar ratingBar;
    private TextView rating_count;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        resultreceiver = (MyResultReceiver)context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rating , container , false);
        traderID  = resultreceiver.getResult();
        ratingbar=(RatingBar) view.findViewById(R.id.ratingBar);
        commentsRecyclerView = view.findViewById(R.id.commentsRecyclerView);
        ratingBar = view.findViewById(R.id.ratingBar);
        rating_count = view.findViewById(R.id.rating_count);
        updateRecycleView();
        loadComments(userPage);
        getTradersDetails(traderID);
        return view;
    }

    private void getTradersDetails(int traderID) {
        TraderAPIService service = RetrofitApi.retrofitReadWD().create(TraderAPIService.class);


        Call<Vendor> call = service.getStore(traderID);

        call.enqueue(new Callback<Vendor>() {
            @Override
            public void onResponse(Call<Vendor> call, Response<Vendor> response) {
                if(response.body() != null)
                {
                    if(response.body().getRating().getCount() != 0) {
                        ratingBar.setRating(Float.parseFloat(response.body().getRating().getRating()));

                    }
                    rating_count.setText(String.valueOf(response.body().getRating().getCount()) + "  Reviews");

                }
                else {
                    Log.d(TAG, "onResponse: failed Vendor ");
                }



            }

            @Override
            public void onFailure(Call<Vendor> call, Throwable t) {
                //Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                TraderAPIService service = RetrofitApi.retrofitReadWD().create(TraderAPIService.class);


                Call<VendorEmpty> callz = service.getStoreEmpty(traderID);
                callz.enqueue(new Callback<VendorEmpty>() {
                    @Override
                    public void onResponse(Call<VendorEmpty> callz, Response<VendorEmpty> response) {
                        if(response.body() != null)
                        {
                            if(response.body().getRating().getCount() != 0) {
                                ratingBar.setRating(Float.parseFloat(response.body().getRating().getRating()));

                            }
                            rating_count.setText(String.valueOf(response.body().getRating().getCount()) + "  Reviews");
                        }
                        else {
                            Log.d(TAG, "onResponse: failed Vendor ");
                        }


                    }

                    @Override
                    public void onFailure(Call<VendorEmpty> callz, Throwable t) {
                       // Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                    }
                });
            }
        });

    }

    private void updateRecycleView() {
        //RecyclerView Firebase List
        commentsList = new ArrayList<>();

        commentsRecyclerAdapter = new CommentsVendorRecyclerAdapter(commentsList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity() , LinearLayoutManager.VERTICAL, false);
        commentsRecyclerView.setHasFixedSize(true);
        commentsRecyclerView.setLayoutManager(mLayoutManager);
        commentsRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                userPage++;
                loadComments(userPage);
            }
        });
        commentsRecyclerView.setAdapter(commentsRecyclerAdapter);
        //mUserDatabase.keepSynced(true);
    }
    private void loadComments(int pageno)
    {
        TraderAPIService service = RetrofitApi.retrofitReadWD().create(TraderAPIService.class);


        Call<List<VendorComment>> call = service.getStoreReviews(traderID);

        call.enqueue(new Callback<List<VendorComment>>() {
            @Override
            public void onResponse(Call<List<VendorComment>> call, Response<List<VendorComment>> response) {
                if(response.body() != null)
                {
                    Log.d(TAG, "onResponse: Succ VendorComment ");
                    commentsList.addAll(response.body());
                    commentsRecyclerAdapter.notifyDataSetChanged();
                }
                else {
                    Log.d(TAG, "onResponse: failed VendorComment ");
                }



            }

            @Override
            public void onFailure(Call<List<VendorComment>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

}
