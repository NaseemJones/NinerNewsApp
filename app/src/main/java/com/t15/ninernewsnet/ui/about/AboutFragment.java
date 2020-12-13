package com.t15.ninernewsnet.ui.about;


import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.t15.ninernewsnet.R;

public class AboutFragment extends Fragment {
    private static final String TAG = "AboutFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_about, container, false);

        final TextView textView = root.findViewById(R.id.textViewGroupName);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,getString(R.string.app_hello));
                Toast.makeText(v.getContext(), getString(R.string.app_hello), Toast.LENGTH_LONG).show();
            }

        });

        return root;
    }
}