package com.t15.ninernewsnet;

import android.os.StrictMode;
import android.util.Log;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;


public class FeedFetcherUNCC {
    private static final String TAG = "FeedFetcherUNCC";

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
