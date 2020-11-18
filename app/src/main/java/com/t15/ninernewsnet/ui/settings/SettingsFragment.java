package com.t15.ninernewsnet.ui.settings;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.t15.ninernewsnet.R;
import com.t15.ninernewsnet.SettingsHandler;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        //final CardView cardView = root.findViewById(R.id.itemCard);


        SettingsHandler settingsHandler = new SettingsHandler(getContext());

        ArrayList<String> testData = new ArrayList<String>();
        testData.add("a");
        testData.add("b");
        testData.add("c");
        testData.add("d");

        settingsHandler.setFeeds(testData);
        settingsHandler.setBookmarks(testData);
        settingsHandler.setFilters("Filters");
        settingsHandler.setNotifications(1);
        settingsHandler.setAutoupdate(1);


        final TextView textView = root.findViewById(R.id.text_settings);
        settingsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}