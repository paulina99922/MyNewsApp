package com.example.android.mynewsapp;

import org.json.JSONException;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }


    public static List<News> fetchData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<News> news = extractFeatureFromJson(jsonResponse);

        return news;

    }

    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem occured while retrieving the news.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset
                    .forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<News> extractFeatureFromJson(String newsJSON) {
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        List<News> news = new ArrayList<>();
        try {
            JSONObject JResponse;
            JSONObject Jresults;
            JSONArray newsArrayList;
            JSONObject currentNews;
            String title = "";
            String section = "";
            String publicationDate = "";
            String link = "";

            JResponse = new JSONObject(newsJSON);
            Jresults = JResponse.getJSONObject("response");
            if (Jresults.has("results")) {
                newsArrayList = Jresults.getJSONArray("results");
                for (int i = 0; i < newsArrayList.length(); i++) {
                    currentNews = newsArrayList.getJSONObject(i);

                    if (currentNews.has("webTitle")) {
                        title = currentNews.getString("webTitle");
                    }

                    if (currentNews.has("sectionName")) {
                        section = currentNews.getString("sectionName");
                    }

                    if (currentNews.has("webPublicationDate")) {
                        publicationDate = currentNews.getString("webPublicationDate");
                    }

                    if (currentNews.has("webUrl")) {
                        link = currentNews.getString("webUrl");
                    }

                    News newsitem = new News(title, section, publicationDate, link);
                    news.add(newsitem);
                }
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the news results", e);
        }
        return news;
    }
}

