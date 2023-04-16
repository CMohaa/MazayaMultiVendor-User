package com.mohaa.dokan.views;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.mohaa.dokan.R;
import com.mohaa.dokan.interfaces.OnAdsClickListener;
import com.mohaa.dokan.models.Ad;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdsAdapter extends RecyclerView.Adapter<AdsAdapter.ViewHolder>  {
    private List<Ad> adList;
    public Context context;


    private OnAdsClickListener onAdsClickListener;
    public AdsAdapter(List<Ad> adList , OnAdsClickListener onAdsClickListener )
    {
        this.adList = adList;

        this.onAdsClickListener = onAdsClickListener;
    }


    @NonNull
    @Override
    public AdsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ads_layout , viewGroup , false);
        context = viewGroup.getContext();

        return new AdsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdsAdapter.ViewHolder viewHolder, final int i) {
        final Ad ads = adList.get(i);
        String img = ads.getThumb_image();
        Glide.with(context)
                .load(img) // image url
                .apply(new RequestOptions()

                        .override(1280, 720) // resizing
                        .centerCrop())
                .into(viewHolder.src);  // imageview object
        viewHolder.src.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAdsClickListener.onAdsClicked(ads, i);

            }
        });

        //viewHolder.price.setText(price);//cant cast to float

    }

    @Override
    public int getItemCount() {
        return adList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private View mView;


        private ImageView src;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            src = mView.findViewById(R.id.ProductPoster);



        }
    }
}
