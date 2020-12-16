package com.t15.ninernewsnet.ui.home;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.appcompat.widget.SearchView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.t15.ninernewsnet.CardModel;
import com.t15.ninernewsnet.CardRecyclerAdapter;

import com.t15.ninernewsnet.FeedFetcher;
import com.t15.ninernewsnet.MainView;
import com.t15.ninernewsnet.R;
import com.t15.ninernewsnet.SettingsHandler;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private static RecyclerView recyclerView;
    private SwipeRefreshLayout swipeLayout;
    private CardRecyclerAdapter cardRecyclerAdapter;
    private SearchView searchView;
    private SettingsHandler settingsHandler;

    ArrayList<CardModel> cardData;
    private FeedFetcher feedData = new FeedFetcher();
    private int viewCounter = 1;
    private int cardsPerPage = 12;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.recycleViewLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        settingsHandler = new SettingsHandler(getContext());

        //Populate card data with test data
        //cardData = new ArrayList<CardModel>(new TestData().getData());

        //Populate card data from feed fetcher
        cardData = new ArrayList<CardModel>();

        if (settingsHandler.getLocalFeed()) {
            cardData.addAll(feedData.getFeedItems(viewCounter, cardsPerPage));
        } else {
            cardData.addAll(feedData.getFeedItemsUNCC(viewCounter));
        }

        cardRecyclerAdapter = new CardRecyclerAdapter(cardData);

        recyclerView.setAdapter(cardRecyclerAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        //what happens when a user reaches the bottom
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)) {
                    Log.d(TAG, "Bottom reached, loading new data");

                    viewCounter += 1;
                    if (settingsHandler.getLocalFeed()) {
                        for(CardModel card : feedData.getFeedItems(viewCounter,cardsPerPage)) {
                            cardData.add(card);
                        }
                    } else
                    {
                        for(CardModel card : feedData.getFeedItemsUNCC(viewCounter)) {
                            cardData.add(card);
                        }
                    }

                    cardRecyclerAdapter.notifyDataSetChanged();

                }
            }
        });

        //what happens when a user swipes down from top
        swipeLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipeContainer);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh () {
                updateCards();
                swipeLayout.setRefreshing(false);
            }
        });


        //Timer for autoupdater/notifier
        if (settingsHandler.getAutoupdate() != 0) {
            final Handler timerHandler = new Handler();
            Runnable timerRunnable = new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "Autoupdating cards then sleeping for " + String.valueOf(settingsHandler.getAutoupdate()) + " minute(s)");
                    //if new articles exist, notifications are enabled, and the activity is active, update the cards and notify the user
                    if (updateCards() && settingsHandler.getNotifications() && (getActivity() != null))
                        showNotification(getContext().getApplicationContext(), getString(R.string.articles_updated), getString(R.string.new_articles));
                    timerHandler.postDelayed(this, settingsHandler.getAutoupdate() * 60 * 1000);
                }
            };
            timerHandler.postDelayed(timerRunnable, settingsHandler.getAutoupdate() * 60 * 1000);
        }

        //needed to enable search icon
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //connect search button with search menu
        inflater.inflate(R.menu.menu_search_options, menu);

        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);




        //If text is changed or submitted, apply the filter
        // Do not apply if there are no cards, and clear the filter if the query is empty
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return setFilter(query);
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return setFilter(query);
            }

            boolean setFilter(String query) {
                //call filter
                if(cardData != null) {
                    if (cardData.size() != 0) {
                        if (!query.isEmpty()) {
                            cardRecyclerAdapter.setFilter(query);
                            return false;
                        } else {
                            cardRecyclerAdapter.clearFilter();
                        }
                    }
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) { return true; }
            if(id == R.id.action_uncc_feed) {
                settingsHandler.setLocalFeed(false);
                updateCards();
            }
            if(id == R.id.action_local_feed) {
                settingsHandler.setLocalFeed(true);
                updateCards();
            }


        return super.onOptionsItemSelected(item);
    }

    //Returns true if there is new data, false otherwise
    public boolean updateCards() {
        Log.d(TAG, "Refresh data");

        ArrayList<CardModel> oldData = new ArrayList<CardModel>(cardData.subList(0, cardsPerPage));

        //reload data
        cardData.clear();

         /*
         for(CardModel card : new TestData().getData()) {
             cardData.add(card);
         }
         */

        viewCounter = 1;
        if (settingsHandler.getLocalFeed()) {
            cardData.addAll(feedData.getFeedItems(viewCounter,cardsPerPage));
        } else {
            cardData.addAll(feedData.getFeedItemsUNCC(viewCounter));
        }

        cardRecyclerAdapter.notifyDataSetChanged();

        if (oldData.equals(cardData)) {
            Log.d(TAG, "Card data has not changed");
            return false;
        } else {
            Log.d(TAG,"New card data");
            return true;
        }
    }

    public void showNotification(Context context, String title, String body) {
        //When tapped, the notification should lead to the main view
        Intent intent = new Intent(context.getApplicationContext(), MainView.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String channelID = getString(R.string.app_name);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelID, channelID, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.ic_user_logo)
                .setContentTitle(title)
                .setContentText(body);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context).addNextIntent(intent);
        notificationBuilder.setContentIntent(stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT));
        notificationManager.notify(1, notificationBuilder.build());
    }
}