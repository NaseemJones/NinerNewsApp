package com.t15.ninernewsnet;

import android.location.Location;

public class CardModel {
        private static final String TAG = "CardModel";

        //Model of data for a single article to have
        private String content;
        private String imageURL;
        private String sourceURL;
        private Location location;

        public CardModel(String content, String imageURL, String sourceURL, Location location) {
            this.content = content;
            this.imageURL = imageURL;
            this.sourceURL = sourceURL;
            this.location = location;
        }

    public CardModel(String content, String imageURL, String sourceURL) {
        this.content = content;
        this.imageURL = imageURL;
        this.sourceURL = sourceURL;
        this.location = null;
    }

        public String getContent() {
            return content;
        }

        public String getImageURL() {
            return imageURL;
        }

        public String getSource() {
            return sourceURL;
        }

        public Location getLocation() {
            return location;
        }

    //Used for handling the contains() method for CardModel to handle duplicates/removing cards
    //If two bookmarks have the same article content and source, they are equal
    @Override
    public boolean equals(Object fromObj) {
        //Compare content and URL of article only to determine if article is unique
        CardModel card = (CardModel)fromObj;
        if(content == null) return false;
        if(card.content.equals(content)||card.sourceURL.equals(sourceURL)) return true;
        return false;
    }
}
