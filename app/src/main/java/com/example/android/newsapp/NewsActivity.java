package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>>{

    public static final String LOG_TAG = NewsActivity.class.getName();
    // URL for article data from the Guardian News API
    private static final String GUARDIAN_API_URL = "https://content.guardianapis.com/search?format=json&show-fields=all&q=art%26design";
    private static final String API_TEST_KEY = "&api-key=test";
    // Adapter for the list of articles
    private ArticleAdapter mAdapter;
    // Constant value for the book loader ID.
    private static final int ARTICLE_LOADER_ID = 11;

    private ImageView mNewsImage;
    private TextView mEmptyTextView;
    private ProgressBar mProgressView;
    private ArrayList<Article> articleArrayList = new ArrayList<>();
    ConnectivityManager cm;
    NetworkInfo activeNetwork;
    boolean isOnline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // Find a reference to the {@link ListView} in the layout
        ListView articleListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of articles as input
        mAdapter = new ArticleAdapter(this, articleArrayList);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        articleListView.setAdapter(mAdapter);

        mNewsImage = (ImageView) findViewById(R.id.news_image);

        // Set a custom message when there are no list items
        mEmptyTextView = (TextView) findViewById(R.id.empty_view_text);
        View emptyLayoutView = findViewById(R.id.empty_layout_view);
        articleListView.setEmptyView(emptyLayoutView);

        mProgressView = (ProgressBar) findViewById(R.id.progress);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website where a user can view the article.
        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current article that was clicked on
                Article currentArticle = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri articleUri = Uri.parse(currentArticle.getUrl());

                // Create a new intent to view the article URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();

        cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        activeNetwork = cm.getActiveNetworkInfo();

        isOnline = activeNetwork != null && activeNetwork.isConnected();
        if (isOnline && articleArrayList.isEmpty()) {

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
            Log.i(LOG_TAG, "Loader on init");
        } else {
            mProgressView.setVisibility(View.GONE);
            mNewsImage.setVisibility(View.GONE);
            mEmptyTextView.setText(R.string.no_internet);
        }

    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        // Create a new loader for the given URL
        String fullQueryURL = "https://content.guardianapis.com/search?q=artanddesign&format=json&show-fields=all&page-size=20&api-key=test";
        Log.i(LOG_TAG, "Loader on create");
        return new ArticleLoader(this, fullQueryURL);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {

        // Set empty state text to display "No articles found."
        mProgressView.setVisibility(View.GONE);
        mEmptyTextView.setText(R.string.no_articles);

        // Clear the adapter of previous article data
        mAdapter.clear();
        Log.i(LOG_TAG, "Loader on finished");
        // If there is a valid list of {@link Articles}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        // Loader reset, so we can clear out our existing data.
        Log.i(LOG_TAG, "Loader on reset");
        mAdapter.clear();
    }
}
