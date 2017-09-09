package com.example.android.mynewsapp;

public class News {

    private String mTitle;
    private String mSection;
    private String mPublicationDate;
    private String mLink;

    public News(String title, String section, String publicationDate, String link) {
        mTitle = title;
        mSection = section;
        mPublicationDate = publicationDate;
        mLink = link;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getPublicationDate() {
        return mPublicationDate;
    }

    public String getLink() {
        return mLink;
    }

}
