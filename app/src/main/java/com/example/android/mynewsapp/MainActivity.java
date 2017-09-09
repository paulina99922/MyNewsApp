package com.example.android.mynewsapp;

import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager
        .LoaderCallbacks<List<News>> {

    private static final int NEWS_LOADER = 1;
    private static final String NEWS_TECHNOLOGY = "https://content.guardianapis" +
            ".com/technology?api-key=b8da5902-84ba-4b9e-9e95-911871e09c57&q=Europe&order-by" +
            "=relevance";
    private NewsAdapter mAdapter;
    private static LoaderManager mLoaderManager;
    TextView emptyMessageTextView;
    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.main_title));
        emptyMessageTextView = (TextView) findViewById(R.id.message_text_view);
        progressBar = (ProgressBar) findViewById(R.id.loading);
        initializeLoaderAndAdapter();
        RecyclerView newsView = (RecyclerView) findViewById(R.id.news_recycler_view);


        if (isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(0, null, this);
        } else {
            emptyMessageTextView.setText(R.string.no_connection);
        }

        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        newsView.setAdapter(mAdapter);

    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String searchQuery = sharedPreferences.getString(getString(R.string
                .search_query_key), getString(R.string.search_query_default));

        String orderBy = sharedPreferences.getString(getString(R.string.order_list_key),
                getString(R.string.order_list_default));

        Uri baseIri = Uri.parse(NEWS_TECHNOLOGY);
        Uri.Builder uriBuilder = baseIri.buildUpon();
        uriBuilder.appendQueryParameter("q", searchQuery);
        uriBuilder.appendQueryParameter("order-by", "relevance");
        Log.v("MainActivity", "Uri: " + uriBuilder);
        return new NewsLoader(this, uriBuilder.toString());
    }


    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        View loadingIndicator = findViewById(R.id.loading);
        loadingIndicator.setVisibility(View.GONE);
        mAdapter.clearAll();
        if (isConnected()) {
            mAdapter.addAll(news);
        } else {
            emptyMessageTextView.setText(getString(R.string.no_articles));
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clearAll();
    }

    public void initializeLoaderAndAdapter() {
        mLoaderManager = getLoaderManager();
        mLoaderManager.initLoader(NEWS_LOADER, null, this);
        RecyclerView newsRecycler = (RecyclerView) findViewById(R.id.news_recycler_view);
        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        newsRecycler.setAdapter(mAdapter);
        newsRecycler.setLayoutManager(new LinearLayoutManager(this));

    }
}