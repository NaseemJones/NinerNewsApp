package com.t15.ninernewsnet.ui.filters;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FiltersViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FiltersViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Filters go here");
    }

    public LiveData<String> getText() {
        return mText;
    }
}