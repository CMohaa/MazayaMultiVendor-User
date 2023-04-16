package com.mohaa.dokan.views;

import android.view.View;
import android.widget.TextView;


import com.mohaa.dokan.R;
import com.mohaa.dokan.Utils.TimelineView;

import androidx.recyclerview.widget.RecyclerView;


public class TimeLineViewHolder extends RecyclerView.ViewHolder {

     View mView;
     TextView mDate;
     TextView mMessage;
     TimelineView mTimelineView;

    public TimeLineViewHolder(View itemView, int viewType) {
        super(itemView);
        mView = itemView;
        mDate = mView.findViewById(R.id.text_timeline_date);
        mMessage = mView.findViewById(R.id.text_timeline_title);
        mTimelineView = mView.findViewById(R.id.time_marker);
        mTimelineView.initLine(viewType);
    }
}