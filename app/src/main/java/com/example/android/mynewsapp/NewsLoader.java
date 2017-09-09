package com.example.android.mynewsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private String mQueryUtils;

    public NewsLoader(Context context, String queryutils) {
        super(context);
        mQueryUtils = queryutils;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (mQueryUtils == null) {
            return null;
        }
        List<News> news = QueryUtils.fetchData(mQueryUtils);
        return news;
    }
}