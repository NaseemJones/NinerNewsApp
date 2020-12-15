package com.t15.ninernewsnet;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class SettingsHandler extends ContextWrapper {
    private static final String TAG = "SettingsHandler";
    //context needs to be passed
    public SettingsHandler(Context base) {
        super(base);
    }


    //a global shared preference file contains the username of the active user
    private SharedPreferences globalPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
    private SharedPreferences.Editor globalEditor =  globalPref.edit();

    public void setCurrentUser(String username) {
        globalEditor.putString("username",username);
        globalEditor.apply();
        Log.d(TAG, username + " selected");
    }

    public String getCurrentUser() {
        return globalPref.getString("username", null);
    }

    //Bookmarks
    public ArrayList<CardModel> getBookmarks() {
        SharedPreferences userPrefs = getSharedPreferences(getCurrentUser(), Context.MODE_PRIVATE);

        Log.d(TAG, "Accessing bookmark data");

        Gson gson = new Gson();
        String json = userPrefs.getString("bookmarks", null);
        Type type = new TypeToken<ArrayList<CardModel>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void setBookmarks(ArrayList<CardModel> bookmarks) {
        SharedPreferences userPrefs = getSharedPreferences(getCurrentUser(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  userPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(bookmarks);
        editor.putString("bookmarks", json);
        editor.apply();
        Log.d(TAG, getCurrentUser() + ": Bookmarks updated");
    }


    //Notifications
    public boolean getNotifications() {
        SharedPreferences userPrefs = getSharedPreferences(getCurrentUser(), Context.MODE_PRIVATE);
        Log.d(TAG, "Accessing notification data");
        return userPrefs.getBoolean("notifications",false);
    }
    public void setNotifications(boolean notificationOpt) {
        SharedPreferences userPrefs = getSharedPreferences(getCurrentUser(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  userPrefs.edit();

        editor.putBoolean("notifications",notificationOpt);

        editor.apply();
        Log.d(TAG, getCurrentUser() + ": notification setting updated");
    }


    //Autoupdate
    public int getAutoupdate() {
        SharedPreferences userPrefs = getSharedPreferences(getCurrentUser(), Context.MODE_PRIVATE);
        Log.d(TAG, "Accessing autoupdate data");
        return userPrefs.getInt("autoupdate",0);
    }
    public void setAutoupdate(int updateOpt) {
        SharedPreferences userPrefs = getSharedPreferences(getCurrentUser(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  userPrefs.edit();

        editor.putInt("autoupdate",updateOpt);

        editor.apply();
        Log.d(TAG, getCurrentUser() + ": autoupdate setting updated");
    }


    /*

    // Unused functions for reference
    //Feeds
    public ArrayList<String> getFeeds() {
        SharedPreferences userPrefs = getSharedPreferences(getCurrentUser(), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = userPrefs.getString("feeds", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void setFeeds(ArrayList<String> feeds) {
        SharedPreferences userPrefs = getSharedPreferences(getCurrentUser(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  userPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(feeds);
        editor.putString("feeds", json);
        editor.apply();
        Log.d(TAG, getCurrentUser() + ": Feeds updated");
    }

    public void saveSetting(String element, String data) {
        SharedPreferences userPrefs = getSharedPreferences(getCurrentUser(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  userPrefs.edit();

        editor.putString(element,data);

        editor.apply();
        Log.d("NOTE",getCurrentUser() + ".xml saved");

    }

    public String getSetting(String element) {
        //get preference from file
        SharedPreferences userPrefs = getSharedPreferences(getCurrentUser(), Context.MODE_PRIVATE);
        return userPrefs.getString(element,null);
    }


     public boolean checkUser(String username) {
        //check in shared_prefs for an existing user
        File prefsdir = new File(getApplicationInfo().dataDir,"shared_prefs");
        for (final File prefs : prefsdir.listFiles()) {
            if (username.equals(prefs.getName().split("\\.")[0])) {
                Log.d("NOTE",username + " found");
                this.getCurrentUser() = username;
                return true;
            }
        }
        return false;
    }
    */
}
