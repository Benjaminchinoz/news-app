package com.example.android.newsapp;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {

    public static final String LOG_TAG = NewsActivity.class.getName();
    // URL for article data from the Guardian News API
    private static final String GUARDIAN_API_URL = "https://content.guardianapis.com/search?format=json&show-fields=all&q=art%26design";
    private static final String API_TEST_KEY = "&api-key=test";
    // Adapter for the list of articles
//    private BookAdapter mAdapter;
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
    }
}
