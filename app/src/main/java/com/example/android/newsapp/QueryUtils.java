package com.example.android.newsapp;

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

/**
 *  This class represents tools for handling a http request and response
 */
public class QueryUtils {

    private QueryUtils(){}
    private static final String LOG_TAG = QueryUtils.class.getName();

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
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
            urlConnection.setReadTimeout(10000 );
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem with news JSON results.", e);
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
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    public static List<NewsItem> fetchNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        List<NewsItem> news = extractNews(jsonResponse);

        return news;
    }

    private static ArrayList<NewsItem> extractNews(String newsJSON) {

        ArrayList<NewsItem> news = new ArrayList<>();
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(newsJSON);
            JSONObject nestedJsonObject = jsonObject.getJSONObject("response");
            JSONArray jsonArray = nestedJsonObject.getJSONArray("results");
            for(int i = 0; i< jsonArray.length(); i++){
                JSONObject singleNews = jsonArray.getJSONObject(i);
                String sectionName = singleNews.getString("sectionName");
                String title = singleNews.getString("webTitle");
                String url = singleNews.getString("webUrl");
                String publicationDate = singleNews.getString("webPublicationDate");

                NewsItem newsItem = new NewsItem(title, sectionName, publicationDate, url);
                news.add(newsItem);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return news;
    }
}
