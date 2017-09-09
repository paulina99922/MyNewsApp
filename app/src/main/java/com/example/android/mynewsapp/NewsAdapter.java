package com.example.android.mynewsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import android.support.v7.widget.RecyclerView;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<News> mNews;
    private Context mContext;
    private String mLink;

    public static final String LOG_TAG = NewsAdapter.class.getSimpleName();

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView newsTitle;
        public TextView newsSection;
        public TextView newsPublicationDate;
        private Context context;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            itemView.setOnClickListener(this);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsSection = itemView.findViewById(R.id.news_section);
            newsPublicationDate = itemView.findViewById(R.id.news_publication_date);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            News newsItem = mNews.get(position);
            mLink = newsItem.getLink();
            Uri newsURI = Uri.parse(mLink);
            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsURI);
            context.startActivity(websiteIntent);
        }
    }

    public NewsAdapter(Context context, List<News> newsItems) {
        mContext = context;
        mNews = newsItems;
    }

    private Context getContext() {
        return mContext;
    }

    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View listView = inflater.inflate(R.layout.item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(mContext, listView);
        return viewHolder;
    }

    public void onBindViewHolder(NewsAdapter.ViewHolder viewHolder, int position) {
        News newsItem = mNews.get(position);
        TextView newsTitleTextView = viewHolder.newsTitle;
        TextView newsSectionTextView = viewHolder.newsSection;
        TextView newsPublicationDateTextView = viewHolder.newsPublicationDate;
        newsTitleTextView.setText(newsItem.getTitle());
        newsSectionTextView.setText(newsItem.getSection());
        newsPublicationDateTextView.setText(newsItem.getPublicationDate());
    }

    @Override
    public int getItemCount() {
        return mNews.size();
    }

    public void addAll(List<News> newsList) {
        mNews.clear();
        mNews.addAll(newsList);
        notifyDataSetChanged();
    }

    public void clearAll() {
        mNews.clear();
    }

}