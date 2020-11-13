package com.t15.ninernewsnet.ui.filters;


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

public class FiltersFragment extends Fragment {

    private FiltersViewModel filtersViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        filtersViewModel =
                ViewModelProviders.of(this).get(FiltersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_filters, container, false);
        //final CardView cardView = root.findViewById(R.id.itemCard);

        final TextView textView = root.findViewById(R.id.text_filters);
        filtersViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}