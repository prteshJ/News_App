package com.example.android.news_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static android.view.View.GONE;

/**
 * Created by PriteshJ on 4/17/17.
 * <p>
 * Used Holder pattern for performance efficiency reasons
 */

class NewsHolder extends ViewHolder {

    private final TextView mSectionNameView;
    private final TextView mReadDurationView;
    private final TextView mByLineView;
    private final TextView mNewsTitleView;
    private final TextView mNewsTrailTextView;
    private final ImageView mNewsImage;
    private final TextView mTimeElapsed;

    /**
     * @param newsView is root view of siblings which denote
     *                 various GUI components which make
     *                 up a single news article
     */
    public NewsHolder(View newsView) {
        super(newsView);
        mSectionNameView = (TextView) newsView.findViewById(R.id.section_name_text_view);
        mReadDurationView = (TextView) newsView.findViewById(R.id.word_count_calculator_text_view);
        mByLineView = (TextView) newsView.findViewById(R.id.byLine_text_view);
        mNewsTitleView = (TextView) newsView.findViewById(R.id.news_title_text);
        mNewsTrailTextView = (TextView) newsView.findViewById(R.id.news_information_text);
        mNewsImage = (ImageView) newsView.findViewById(R.id.news_thumbnail_image);
        mTimeElapsed = (TextView) newsView.findViewById(R.id.date_time_text_view);
    }


    /**
     * @param newsArticle        the container of news information
     * @param formattedWordCount the word count formatted so that reader gets
     *                           an estimate of how long it takes to read the
     *                           news article
     * @param byLineDetails      formatted details of news author
     * @param trailText          news summary properly formatted
     * @param context            the application context required to access resources
     * @param thumbnailStatus    if true, image wasn't provided by server and we
     *                           need to set it from our resources locally
     * @param formattedDateTime  time formatted relative to local timezone of news reader
     */
    public void bindNewsInformation(News newsArticle, String formattedWordCount, String byLineDetails, String trailText, Context context, boolean thumbnailStatus, String formattedDateTime) {
        // Set the section name on the GUI
        mSectionNameView.setText(newsArticle.getmSectionName());
        // Set the article read duration on the GUI
        if (formattedWordCount.isEmpty()) {
            mReadDurationView.setVisibility(GONE);
        } else {
            mReadDurationView.setText(formattedWordCount);
        }
        // Set the author details on the GUI
        mByLineView.setText(context.getString(R.string.authored_by) + context.getString(R.string.space) + byLineDetails);
        // Set the news title on the GUI
        mNewsTitleView.setText(newsArticle.getmWebTitle());
        // Set the news summary on the GUI
        mNewsTrailTextView.setText(trailText);
        // Set the image on the GUI using Picasso library
        if (thumbnailStatus) {
            mNewsImage.setVisibility(GONE);
        } else {
            int width = (int)context.getResources().getDimension(R.dimen.image_dimension_width);
            int height = (int)context.getResources().getDimension(R.dimen.image_dimension_height);
            Picasso.with(context).load(newsArticle.getmThumbnailUrl()).noFade().resize(width, height).into(mNewsImage);
        }
        // Set the formatted date time on the GUI
        mTimeElapsed.setText(formattedDateTime);
    }
}
