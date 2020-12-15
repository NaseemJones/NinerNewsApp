package com.t15.ninernewsnet.ui.bookmarks;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.t15.ninernewsnet.BuildConfig;
import com.t15.ninernewsnet.CardModel;
import com.t15.ninernewsnet.CardRecyclerAdapter;
import com.t15.ninernewsnet.R;
import com.t15.ninernewsnet.SettingsHandler;

import java.util.ArrayList;

public class BookmarksFragment extends Fragment {
    private static final String TAG = "BookmarksFragment";

    private static RecyclerView recyclerView;
    private SwipeRefreshLayout swipeLayout;
    private CardRecyclerAdapter cardRecyclerAdapter;
    private SearchView searchView;
    private SettingsHandler settingsHandler;

    ArrayList<CardModel> cardData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_bookmarks, container, false);


        recyclerView = root.findViewById(R.id.recycleViewLayoutBookmarks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //fill card data with bookmarks from storage
        settingsHandler = new SettingsHandler(getContext());

        cardData = settingsHandler.getBookmarks() != null ? settingsHandler.getBookmarks() : new ArrayList<CardModel>();

        cardRecyclerAdapter = new CardRecyclerAdapter(cardData);
        recyclerView.setAdapter(cardRecyclerAdapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //setup swipe from top
        swipeLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipeContainer);

        //what happens when a user swipes down from top
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh () {
                Log.d(TAG, "Refresh data");

                //reload data
                if(settingsHandler.getBookmarks() != null) {
                    cardData.clear();
                    cardData.addAll(settingsHandler.getBookmarks());
                    cardRecyclerAdapter.notifyDataSetChanged();
                }

                swipeLayout.setRefreshing(false);
            }
        });

        //needed to enable search icon
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //connect search button with search menu
        inflater.inflate(R.menu.menu_search, menu);

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

        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}