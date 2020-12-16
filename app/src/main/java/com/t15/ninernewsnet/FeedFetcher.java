package com.t15.ninernewsnet;

import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

public class FeedFetcher {
    private static final String TAG = "FeedFetcher";

    public ArrayList<CardModel> getFeedItems(int page, int elements) {
        String searchTags = "Charlotte+AND+(NC+OR+Carolina)+NOT+(Royals+OR+Princess)";
        String excludeDomains = "bbc.co.uk,in.reuters.com,ca.reuters.com,nytimes.com,people.com,ca.nbc.com,thenews.com.pk,huffpost.com,thesimpledollar.com,design-milk.com";

        String url = "https://newsapi.org/v2/everything?q=" + searchTags + "&excludeDomains=" + excludeDomains + "&sources?language=en&apiKey=4d6fb178378f44d6aedf55ac7af74130&page=" + page + "&pageSize=" + elements;
        ArrayList<CardModel> cardData = new ArrayList<CardModel>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Log.d(TAG,"Attempting to fetch " + url);

        String urlData = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            urlData = EntityUtils.toString(httpEntity);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage());
        } catch (ClientProtocolException e) {
            Log.e(TAG, e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        try {
            JSONObject newsData = new JSONObject(urlData.toString());
            JSONArray newsArray = newsData.getJSONArray("articles");
            for (int i=0;i<newsArray.length();i++) {
                JSONObject sourceObj = newsArray.getJSONObject(i);

                String title = sourceObj.getString("title");
                String description = sourceObj.getString("content");
                String imageURL = sourceObj.getString("urlToImage");
                String link = sourceObj.getString("url");

                //fallback to showing the title if there's no description
                if (description == "null")
                    description = title;

                CardModel card = new CardModel(description, imageURL, link, null);
                Log.d(TAG, "Card fetched and parsed: " + link);
                cardData.add(card);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return cardData;

    }

    //Fetches articles from inside uncc's site for testing
    public ArrayList<CardModel> getFeedItemsUNCC(int page) {
        String url = "https://inside.uncc.edu/news-features?page=";
        ArrayList<CardModel> cardData = new ArrayList<CardModel>();
        String feedPage = url + page;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Log.d(TAG, "Attempting to fetch " + feedPage);
        try {
            Document doc = Jsoup.connect(feedPage).get();
            Elements elements = doc.select("div.article");

            for (int i = 0; i < elements.size(); i++) {
                Element element = elements.get(i);

                String description = element.getElementsByClass("article-teaser").text();

                String imageURL;
                //Don't fail on no image
                try {
                    imageURL = element.getElementsByClass("img-responsive").first().absUrl("src");
                } catch (NullPointerException e) {
                    imageURL = "null";
                }

                String link = element.getElementsByClass("article-title").first().getElementsByIndexEquals(1).attr("abs:href");

                CardModel card = new CardModel(description, imageURL, link, null);
                Log.d(TAG, "Card fetched and parsed: " + link);
                cardData.add(card);
            }
        } catch (HttpStatusException e) {
            Log.e(TAG, e.getMessage());
        } catch (SocketTimeoutException e) {
            Log.e(TAG, e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        return cardData;

    }

}
