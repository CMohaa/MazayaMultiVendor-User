package com.mohaa.dokan.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;


import com.mohaa.dokan.R;
import com.mohaa.dokan.Utils.ExpandableTextView;
import com.mohaa.dokan.Utils.GetTimeAgo;
import com.mohaa.dokan.models.wp.Productreview;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Mohamed El Sayed
 */
public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder> {

    public List<Productreview> commentsList;

    public Context context;



    public CommentsRecyclerAdapter(List<Productreview> commentsList ){

        this.commentsList = commentsList;

    }

    @Override
    public CommentsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item, parent, false);
        context = parent.getContext();

        return new CommentsRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommentsRecyclerAdapter.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String data = prefs.getString("blog_post_id", ""); //no id: default value
        final int user_id = commentsList.get(position).getId();
        String userName = commentsList.get(position).getReviewer();

        String time = commentsList.get(position).getDateCreated();
       // holder.setTime(time);
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {

            // Date from = inputFormat.parse(string_from);
            Date to = inputFormat.parse(time);
            // long from_mil = from.getTime();
            long to_mil = to.getTime();
            holder.setTime(to_mil);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //final String blogPostId = comment_.getBlog_post_id();
        final String blogPostId = data;
        String commentMessage = commentsList.get(position).getReview();
        holder.setComment_message(commentMessage);

        Productreview c = commentsList.get(position);
        holder.fillComment(userName, c, holder.commentTextView, holder.comment_time_stamp);
        holder.ratingBar.setRating(commentsList.get(position).getRating());

    }


    @Override
    public int getItemCount() {

        if(commentsList != null) {

            return commentsList.size();

        } else {

            return 0;

        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView comment_message;

        private TextView blogUserName;
        //private CircleImageView blogUserImage;
        private TextView comment_time_stamp;
        private final ExpandableTextView commentTextView;
        private RatingBar ratingBar;

        // reply
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            commentTextView = (ExpandableTextView) itemView.findViewById(R.id.commentText);

            //blogUserImage = mView.findViewById(R.id.Comments_image);
            blogUserName = mView.findViewById(R.id.Comments_username);
            comment_message = mView.findViewById(R.id.Comments_message);
            comment_time_stamp = mView.findViewById(R.id.comment_time_stamp);
            ratingBar = mView.findViewById(R.id.ratingBar);

            //

        }
        private void fillComment(String userName, Productreview comment, ExpandableTextView commentTextView, TextView dateTextView) {
            Spannable contentString = new SpannableStringBuilder(userName + "   " + comment.getReview());
            contentString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.highlight_text)),
                    0, userName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                commentTextView.setText(Html.fromHtml(String.valueOf(contentString), Html.FROM_HTML_MODE_COMPACT));
            } else {
                commentTextView.setText(Html.fromHtml(String.valueOf(contentString)));
            }


            //CharSequence date = FormatterUtil.getRelativeTimeSpanString(context, comment.getDateCreated());
            //dateTextView.setText(comment.getDateCreated());
        }

        public void setComment_message(String message){


            comment_message.setText(message);

        }


        public void setTime(long time) {



            GetTimeAgo getTimeAgo = new GetTimeAgo();

            long lastTime = time;

            String lastSeenTime = GetTimeAgo.getTimeAgo(lastTime, context);

            comment_time_stamp.setText(lastSeenTime);

        }
    }
}
