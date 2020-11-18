package com.t15.ninernewsnet;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SettingsHandler extends ContextWrapper {

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
        Log.i("SettingsHandler", username + " selected");
    }

    public String getCurrentUser() {
        return globalPref.getString("username", null);
    }


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
        Log.i("SettingsHandler", getCurrentUser() + ": Feeds updated");
    }

    //Bookmarks
    public ArrayList<String> getBookmarks() {
        SharedPreferences userPrefs = getSharedPreferences(getCurrentUser(), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = userPrefs.getString("bookmarks", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public void setBookmarks(ArrayList<String> bookmarks) {
        SharedPreferences userPrefs = getSharedPreferences(getCurrentUser(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  userPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(bookmarks);
        editor.putString("bookmarks", json);
        editor.apply();
        Log.i("SettingsHandler", getCurrentUser() + ": Bookmarks updated");
    }

    //Filters
    public String getFilters() {
        SharedPreferences userPrefs = getSharedPreferences(getCurrentUser(), Context.MODE_PRIVATE);
        return userPrefs.getString("filters",null);
    }
    public void setFilters(String filters) {
        SharedPreferences userPrefs = getSharedPreferences(getCurrentUser(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  userPrefs.edit();

        editor.putString("filters",filters);

        editor.apply();
        Log.i("SettingsHandler", getCurrentUser() + ": Filters updated");
    }

    //Notifications
    public int getNotifications() {
        SharedPreferences userPrefs = getSharedPreferences(getCurrentUser(), Context.MODE_PRIVATE);
        return userPrefs.getInt("notifications",0);
    }
    public void setNotifications(int notificationOpt) {
        SharedPreferences userPrefs = getSharedPreferences(getCurrentUser(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  userPrefs.edit();

        editor.putInt("notifications",notificationOpt);

        editor.apply();
        Log.i("SettingsHandler", getCurrentUser() + ": notification setting updated");
    }

    //Autoupdate
    public int getAutoupdate() {
        SharedPreferences userPrefs = getSharedPreferences(getCurrentUser(), Context.MODE_PRIVATE);
        return userPrefs.getInt("autoupdate",0);
    }
    public void setAutoupdate(int updateOpt) {
        SharedPreferences userPrefs = getSharedPreferences(getCurrentUser(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  userPrefs.edit();

        editor.putInt("autoupdate",updateOpt);

        editor.apply();
        Log.i("SettingsHandler", getCurrentUser() + ": autoupdate setting updated");
    }


    //



    /*


    public void saveAllSettings() {
        //may not be necessary
        SharedPreferences userPrefs = getSharedPreferences(getCurrentUser(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  userPrefs.edit();

        editor.putString("feeds","feed[s]");
        editor.putString("bookmarks","bookmark[s]");


        editor.putString("filters","filter[s]");


        editor.putInt("notifications",0);

        editor.putInt("autoupdate",1);

        editor.apply();
        Log.i("NOTE",getCurrentUser() + ".xml saved");

    }

    public void saveSetting(String element, String data) {
        SharedPreferences userPrefs = getSharedPreferences(getCurrentUser(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  userPrefs.edit();

        editor.putString(element,data);

        editor.apply();
        Log.i("NOTE",getCurrentUser() + ".xml saved");

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
                Log.i("NOTE",username + " found");
                this.getCurrentUser() = username;
                return true;
            }
        }
        return false;
    }
    */

}
