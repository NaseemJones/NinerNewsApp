package com.t15.ninernewsnet.ui.bookmarks;


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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.t15.ninernewsnet.CustomAdapter;
import com.t15.ninernewsnet.R;

import java.util.ArrayList;

public class BookmarksFragment extends Fragment {

    private BookmarksViewModel bookmarksViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        bookmarksViewModel =
                ViewModelProviders.of(this).get(BookmarksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        //final CardView cardView = root.findViewById(R.id.itemCard);
        RecyclerView recyclerView = root.findViewById(R.id.recycleViewLayout2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ArrayList<String> textData = new ArrayList<>();
        textData.add("Megan Bird, a UNC Charlotte senior from Charlottesville, Virginia, is among finalists in contention for the Rhodes Scholarship, one of the world’s most prestigious graduate fellowships.");
        CustomAdapter myAdapter = new CustomAdapter(textData);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final TextView textView = root.findViewById(R.id.text_bookmarks);
        bookmarksViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}