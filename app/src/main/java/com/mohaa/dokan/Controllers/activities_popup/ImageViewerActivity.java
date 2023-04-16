package com.mohaa.dokan.Controllers.activities_popup;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mohaa.dokan.Controllers.BaseActivity;
import com.mohaa.dokan.R;
import com.mohaa.dokan.Utils.ConstantUI;
import com.mohaa.dokan.Utils.TextViewMedium;
import com.mohaa.dokan.Utils.pinchtozoom.ImageMatrixTouchHandler;
import com.mohaa.dokan.models.wp.Product;
import com.mohaa.dokan.views.ProductImageViewPagerAdapter;


import java.util.ArrayList;
import java.util.List;


public class ImageViewerActivity extends BaseActivity {
    private int pos;
    private int cat_id;
    private List<Product.Image> imageList = ProductImageViewPagerAdapter.list;


    /**
     * Step 1: Download and set up v4 support library: http://developer.android.com/tools/support-library/setup.html
     * Step 2: Create ExtendedViewPager wrapper which calls TouchImageView.canScrollHorizontallyFroyo
     * Step 3: ExtendedViewPager is a custom view and must be referred to by its full package name in XML
     * Step 4: Write TouchImageAdapter, located below
     * Step 5. The ViewPager in the XML should be ExtendedViewPager
     */
    private TextViewMedium tvImageDone;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        tvImageDone = findViewById(R.id.tvImageDone);
        pos = getIntent().getIntExtra(ConstantUI.IMAGE_POSITION, 0);
        cat_id = getIntent().getIntExtra(ConstantUI.IMAGE_CATEGORYID, 0);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mViewPager.setAdapter(new TouchImageAdapter(this, getImageList()));
        mViewPager.setCurrentItem(pos);
        tvImageDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public List<Product.Image> getImageList() {
        List<Product.Image> list = new ArrayList<>();
        for(int i=0;i<imageList.size();i++) {
            list.add(imageList.get(i));
        }
        return list;


    }





    class TouchImageAdapter extends PagerAdapter {


        private Context context;
        private LayoutInflater layoutInflater;
        List<Product.Image> list = new ArrayList<>();

        public TouchImageAdapter(Context context, List<Product.Image> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.item_full_image, container, false);
            ImageView img = view.findViewById(R.id.ivImage);
            final ProgressBar progress_bar = view.findViewById(R.id.progress_bar);
//        imageView.setImageResource(R.drawable.banner);

            container.addView(view);
            Glide.with(context)
                    .asBitmap()
                    .load(list.get(position).getSrc())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            img.setImageBitmap(resource);
                            progress_bar.setVisibility(View.GONE);
                        }

                    });

            ImageMatrixTouchHandler imageMatrixTouchHandler = new ImageMatrixTouchHandler(context);
            img.setOnTouchListener(imageMatrixTouchHandler);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
