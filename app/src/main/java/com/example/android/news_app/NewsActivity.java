package com.example.android.news_app;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PriteshJ
 *         <p>
 *         Displays updated list of latest news articles
 */
public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    @SuppressWarnings("unused")
    private static final String LOG_TAG = NewsActivity.class.getName();

    /**
     * URL for news data from The Guardian Open Platform dataset
     */
    private static final String NEWS_REQUEST_URL =
            "https://content.guardianapis.com/";

    /**
     * Adapter for the list of news articles
     */
    private NewsAdapter mAdapter;

    /**
     * Constant value for the news loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWS_LOADER_ID = 1;


    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news);

        // Initialize {@link JodaTimeAndroid} library for Android
        JodaTimeAndroid.init(getApplicationContext());

        // Find a reference to the {@link RecyclerView} in the layout
        RecyclerView newsListView = (RecyclerView) findViewById(R.id.news_list);

        mEmptyStateTextView = (TextView) findViewById(R.id.news_empty_view);

        // Create a new adapter that takes an empty list of news as input
        mAdapter = new NewsAdapter(this, new ArrayList<News>(), newsListView);

        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);

        // Sets layout manager for recycler view
        newsListView.setLayoutManager(LayoutManager);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected news article.
        newsListView.addOnItemTouchListener(new CustomRVItemTouchListener(getApplicationContext(), newsListView, new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                // Find the current news article that was clicked on
                News currentNews = mAdapter.getItem(position);

                if (currentNews != null) {
                    // Convert the String URL into a URI object (to pass into the Intent constructor)
                    Uri newsUri = Uri.parse(currentNews.getmShortUrl());

                    // Create a new intent to view the news URI
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                    // Send the intent to launch a new activity
                    startActivity(websiteIntent);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.news_loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String country_endpoint = sharedPrefs.getString(getString(R.string.settings_country_key),
                getString(R.string.settings_country_default));

        String page_size = sharedPrefs.getString(getString(R.string.settings_number_articles_key),
                getString(R.string.settings_number_articles_default));

        // Sets the title based on selected country using helper class
        if (!TextUtils.isEmpty(NewsSettingsActivity.country_label)) {
            setTitle(NewsSettingsActivity.country_label + getString(R.string.space) + getString(R.string.news_text));
        }

        // Making respective url's based on country endpoints
        Uri baseUri = Uri.parse(NEWS_REQUEST_URL + country_endpoint);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("show-fields", "all");
        uriBuilder.appendQueryParameter("show-elements", "image");
        uriBuilder.appendQueryParameter("page-size", page_size);
        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("api-key", "test");
        uriBuilder.appendQueryParameter("orderBy", "newest");


        // Create a new loader for the given URL
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsList) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.news_loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.no_news);

        // Clear the adapter of previous news data
        mAdapter.clear();

        // If there is a valid list of {@link News}, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (newsList != null && !newsList.isEmpty()) {
            mAdapter.addAll(newsList);
            mEmptyStateTextView.setVisibility(View.GONE);
        }

        // Handle behavior when no data is retrieved from server
        if (mAdapter.getItemCount() == 0) {
            mEmptyStateTextView.setText(R.string.no_news);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, NewsSettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
