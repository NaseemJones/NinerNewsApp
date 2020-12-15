package com.t15.ninernewsnet;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//previously CustomAdapter
//implements tasks for individual cards in the RecyclerView
public class CardRecyclerAdapter extends RecyclerView.Adapter<CardRecyclerAdapter.ViewHolder> {
    private static final String TAG = "CardRecyclerAdapter";

    private ArrayList<CardModel> items;
    private ArrayList<CardModel> itemsFiltered;
    public boolean isFilterActive = false;

    //handles the data returned to each card
    @Override
    public void onBindViewHolder(@NonNull CardRecyclerAdapter.ViewHolder holder, int position) {
        CardModel item;

        item = isFilterActive ? itemsFiltered.get(position) : items.get(position);

        TextView contents = holder.contents;
        ImageView thumbnail = holder.thumbnail;
        TextView sourceURL = holder.sourceURL;
        ImageButton LocationMarker = holder.locationMarker;

        //set the card's data
        contents.setText(item.getContent());

        if (item.getImageURL() != "null") {
            Picasso.get().load(item.getImageURL()).into(thumbnail);
        } else {
            thumbnail.setImageResource(android.R.drawable.ic_menu_report_image);
        }
        //tag thumbnail element with URL so we can use it later
        thumbnail.setTag(item.getImageURL());

        sourceURL.setText(item.getSource());
        LocationMarker.setTag(item.getLocation());
    }

    @Override
    public int getItemCount() {
        ArrayList<CardModel> temp = isFilterActive ? itemsFiltered : items;
        //size should never be null, only zero
        return temp != null ? temp.size() : 0;
    }

    public void setFilter(String searchCriteria) {
        setFilteredItems(searchCriteria);
        notifyDataSetChanged();
    }

    public void clearFilter() {
        isFilterActive = false;
        notifyDataSetChanged();
    }

    private void setFilteredItems(String searchCriteria) {
        itemsFiltered = new ArrayList<CardModel>();
        if (searchCriteria.isEmpty()) {
            isFilterActive = false;
            return;
        } else {
            for (CardModel item : items) {
                //only look at card content when filtering and remove case
                if (item.getContent().toLowerCase().contains(searchCriteria.toLowerCase())) {
                    itemsFiltered.add(item);
                }
                //keep view empty, even if the query is not found
                isFilterActive = true;
            }
        }
    }

    //Handles all events for cards
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView contents;
        private ImageView thumbnail;
        private TextView sourceURL;
        private ImageButton locationMarker;
        private ImageButton saveBookmarkButton;

        public ViewHolder(final View view) {
            super(view);

            //define elements in the card for events
            this.contents = (TextView) view.findViewById(R.id.itemContents);
            this.thumbnail = (ImageView) view.findViewById(R.id.itemThumbnail);
            this.sourceURL = (TextView) view.findViewById(R.id.itemSourceURL);
            this.locationMarker = (ImageButton) view.findViewById(R.id.locationMarker);
            this.saveBookmarkButton = (ImageButton) view.findViewById(R.id.saveBookmarkButton);


            //what happens when an individual card is clicked on
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleView(v);
                }

                private void toggleView(View v) {
                    TextView content = (TextView) v.findViewById(R.id.itemContents);
                    RelativeLayout moreOptions = (RelativeLayout) v.findViewById((R.id.moreOptions));
                    //hide if shown
                    if (moreOptions.isShown()) {
                        moreOptions.setVisibility(View.GONE);
                        content.setMaxLines(2);

                    } else {
                        //show extra opts
                        moreOptions.setVisibility(View.VISIBLE);
                        content.setMaxLines(10);

                        //get data for setting the status of card elements
                        CardModel cardData = new CardModel(contents.getText().toString(), thumbnail.getTag().toString(), sourceURL.getText().toString(), (Location) locationMarker.getTag());

                        SettingsHandler settingsHandler = new SettingsHandler(view.getContext());
                        //if null is returned, use a blank set
                        ArrayList<CardModel> bookmarkedCards = settingsHandler.getBookmarks() != null ? settingsHandler.getBookmarks() : new ArrayList<CardModel>();

                        //set state of bookmark star
                        if (!bookmarkedCards.contains(cardData)) {
                            saveBookmarkButton.setImageResource(android.R.drawable.btn_star_big_off);
                        } else {
                            saveBookmarkButton.setImageResource(android.R.drawable.btn_star_big_on);
                        }

                        //if we have location data, show the marker
                        if(cardData.getLocation() != null) {
                            locationMarker.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });

            //click bookmark
            saveBookmarkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "handling bookmark request for " + sourceURL.getText());

                    CardModel newBookmark = new CardModel(contents.getText().toString(), thumbnail.getTag().toString(), sourceURL.getText().toString(), (Location) locationMarker.getTag());

                    SettingsHandler settingsHandler = new SettingsHandler(view.getContext());
                    //if null is returned, use a blank set
                    ArrayList<CardModel> bookmarkedCards = settingsHandler.getBookmarks() != null ? settingsHandler.getBookmarks() : new ArrayList<CardModel>();

                    if (!bookmarkedCards.contains(newBookmark)) {
                        Log.d(TAG, "bookmark does not exist, saving " + sourceURL.getText());

                        bookmarkedCards.add(newBookmark);
                        settingsHandler.setBookmarks(bookmarkedCards);
                        saveBookmarkButton.setImageResource(android.R.drawable.btn_star_big_on);
                    } else {
                        Log.d(TAG, "bookmark exists, removing " + sourceURL.getText());
                        bookmarkedCards.remove(newBookmark);
                        settingsHandler.setBookmarks(bookmarkedCards);
                        saveBookmarkButton.setImageResource(android.R.drawable.btn_star_big_off);
                    }
                }
            });

            //clicking source URL opens in browser
            sourceURL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "opening " + sourceURL.getText());

                    Context context = view.getContext();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse((String) sourceURL.getText()));
                    context.startActivity(i);
                }

            });

            //clicking location
            locationMarker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "opening " + locationMarker.getTag());
                        Toast.makeText(v.getContext(), "Location marker selected", Toast.LENGTH_LONG).show();
                    }
                    //what happens when a location is opened
                }

            });
        }
    }

    //CardModel.java
    public CardRecyclerAdapter(ArrayList<CardModel> cardData){
        items = cardData;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout, viewGroup, false);
        return new ViewHolder(view);
    }

}

