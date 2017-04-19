package com.example.android.news_app;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PriteshJ on 4/17/17.
 * <p>
 * An {@link NewsAdapter} knows how to create a list item layout for each news article
 * in the data source (a list of {@link News} objects).
 * <p>
 * These list item layouts will be provided to an adapter view like RecyclerView
 * to be displayed to the user.
 */

class NewsAdapter extends RecyclerView.Adapter<NewsHolder> {
    private List<News> mNewsList = new ArrayList<>();
    private final Context mContext;


    /**
     * Constructs a new {@link NewsAdapter}.
     *
     * @param context    of the app
     * @param newsList   is the list of news articles, which is the data source of the adapter
     * @param parentView is the root view
     */
    public NewsAdapter(Context context, List<News> newsList, View parentView) {
        this.mContext = context;
        this.mNewsList = newsList;
    }

    /**
     * Called when RecyclerView needs a new {@link NewsHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(NewsHolder, int)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.news_list_item, parent, false);
        return new NewsHolder(itemView);
    }


    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link NewsHolder} to reflect the item at the given
     * position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */

    @Override
    public void onBindViewHolder(NewsHolder holder, int position) {
        News currentNews = mNewsList.get(position);
        currentNews.setmTrailText(currentNews.getmTrailText());
        currentNews.setmWebPublicationDate(formatDateTime(currentNews.getmWebPublicationDate()));
        holder.bindNewsInformation(currentNews, formatWordCount(currentNews.getmWordCount()), currentNews.getmByLine(), trimTrailText(currentNews.getmTrailText().trim()), mContext, thumbnailValidator(currentNews.getmThumbnailUrl()), currentNews.getmWebPublicationDate());
    }

    /**
     * @return the size of news list
     */
    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    /**
     * Implemented a custom method which retrieves correct news article
     *
     * @param position denotes order of news article within news list
     * @return chosen news article
     */
    public News getItem(int position) {
        return mNewsList.get(position);
    }

    /**
     * Implemented a custom method that deletes all news articles from news list
     */
    public void clear() {
        mNewsList.clear();
    }

    /**
     * Implemented a custom method that populates a list of news articles
     *
     * @param newsData is list of news articles to be added
     */
    public void addAll(List<News> newsData) {
        if (newsData != null) {
            clear();
            mNewsList.addAll(newsData);
            notifyDataSetChanged();
        }
    }


    /**
     * @param wordCount denotes total number of words in news article
     * @return a string denoting time it takes to read news article
     */
    private String formatWordCount(int wordCount) {
        int readDuration = (int) Math.floor(wordCount / 130.0);
        if (readDuration == 0) {
            return "";
        }
        return readDuration + " " + mContext.getString(R.string.minute_read);
    }


    /**
     * @param trailText denotes news summary
     * @return valid text with HTML content removed
     */
    @SuppressWarnings("deprecation")
    private String trimTrailText(String trailText) {
        StringBuilder result = new StringBuilder();
        if (Build.VERSION.SDK_INT >= 24) {
            result.append(Html.fromHtml(trailText, Html.FROM_HTML_MODE_LEGACY));
        } else {
            result.append(Html.fromHtml(trailText));
        }
        return result.toString();
    }

    /**
     * Uses Joda Time library for Android
     *
     * @param date denotes date news article was published on the web
     * @return formatted date and time
     */
    private String formatDateTime(String date) {
        StringBuilder result = new StringBuilder();
        if (!isValid(date)) {
            return date;
        }
        DateTime dt = ISODateTimeFormat.dateTimeParser().parseDateTime(date);
        result.append(net.danlew.android.joda.DateUtils.getRelativeTimeSpanString(mContext, dt));
        return result.toString();
    }


    /**
     * Reference: http://stackoverflow.com/questions/21044833/how-to-check-if-a-string-is-a-valid-joda-datetime
     *
     * @param test
     * @return true if date is valid
     */
    public boolean isValid(String test) {
        try {
            DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            dtf.parseDateTime(test);
            return true;
        } catch (IllegalArgumentException iae) {
            return false;
        }
    }

    /**
     * @param thumbnail denotes path of image on network
     * @return returns true if thumbnail url of image didn't exist on network
     */
    private boolean thumbnailValidator(String thumbnail) {
        return TextUtils.isDigitsOnly(thumbnail);
    }

}
