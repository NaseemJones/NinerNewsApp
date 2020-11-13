package com.t15.ninernewsnet;

import android.app.Application;

public class NinerNewsNet extends Application {

    //global class for user variables

    private String username;

    public String getName() {
        return username;
    }

    public void setName(String uname) {
        username = uname;
    }
}
