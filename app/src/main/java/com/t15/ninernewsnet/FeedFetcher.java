package com.t15.ninernewsnet;

import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

    private final String searchTags = "Charlotte+AND+(NC+OR+Carolina)+NOT+(Royals+OR+Princess)";
    private final String excludeDomains = "bbc.co.uk,in.reuters.com,ca.reuters.com,nytimes.com,people.com,ca.nbc.com,thenews.com.pk,huffpost.com,thesimpledollar.com,design-milk.com";

    public ArrayList<CardModel> getFeedItems(int page, int elements) {
        String url = "https://newsapi.org/v2/everything?q=" + searchTags + "&excludeDomains=" + excludeDomains + "&sources?language=en&apiKey=cb29beb081534634abaffb9c35d483ce&page=" + page + "&pageSize=" + elements;
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

}
