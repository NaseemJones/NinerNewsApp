package com.t15.ninernewsnet.ui.settings;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.t15.ninernewsnet.R;
import com.t15.ninernewsnet.SettingsHandler;

public class SettingsFragment extends Fragment {
    private static final String TAG = "SettingsFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        SettingsHandler settingsHandler = new SettingsHandler(getContext());

        //temporary settings
        settingsHandler.setNotifications(true); //notify on new articles when autobg is on
        settingsHandler.setAutoupdate(10); //minutes
        Toast.makeText(getActivity(), "Notif=" + settingsHandler.getNotifications() + " au=" + settingsHandler.getAutoupdate(), Toast.LENGTH_LONG).show();
        //

        return root;
    }
}