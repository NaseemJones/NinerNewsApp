 package com.t15.ninernewsnet.ui.settings;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.util.TypedValue.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.t15.ninernewsnet.R;
import com.t15.ninernewsnet.SettingsHandler;

import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.File.*;

public class SettingsFragment extends Fragment { // implements AdapterView.OnItemSelectedListener
    private static final String TAG = "SettingsFragment";
    //private SettingsViewModel settingsViewModel;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_settings, container, false);
        //final CardView cardView = root.findViewById(R.id.itemCard);

        final SettingsHandler settingsHandler = new SettingsHandler(getContext());

        //NOTE: removed the part where temporary settings were set because they interfered with
        // retrieving stored user data upon reopening the app

        /* Implement buttons for Settings page - clicking the buttons opens more options for each category */
        final ToggleButton intervals_toggle_button = (ToggleButton) root.findViewById(R.id.settings_interval_toggle_button);
        final ToggleButton profiles_toggle_button = (ToggleButton) root.findViewById(R.id.settings_profiles_toggle_button);
        final ToggleButton notifications_toggle_button = (ToggleButton) root.findViewById(R.id.settings_notification_toggle_button);

        final int grayColorValue = Color.parseColor("#AAAAAA"); //create a color value for the gray buttons

        // locate profile menu item
        final ScrollView profile_menu = (ScrollView) root.findViewById(R.id.profiles_menu_scrollable);

        // locate interval items
        final TextView interval_text = (TextView) root.findViewById(R.id.update_interval_option_text);
        final NumberPicker interval_number_picker = (NumberPicker) root.findViewById(R.id.interval_number_picker);
        final Spinner time_units_spinner = (Spinner) root.findViewById(R.id.time_units_spinner);

        // locate notifications items
        final Switch notifs_switch = (Switch) root.findViewById(R.id.settings_notif_on_new_articles_switch);
        final TextView notifs_label = (TextView) root.findViewById(R.id.settings_notifs_on_label);
        final TextView notifs_on_articles_label = (TextView) root.findViewById(R.id.settings_notif_on_new_articles_label);

        /* Implement Spinner on Settings page for interval options */
        List<String> interval_spinner_options = new ArrayList<String>();
        interval_spinner_options.add("minutes");
        interval_spinner_options.add("hours");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, interval_spinner_options); // Create adapter for spinner
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Drop down layout style - list view with radio button
        time_units_spinner.setAdapter(dataAdapter); // Attach data adapter to spinner

        /* Implement NumberPicker on Settings Page for interval options */
        interval_number_picker.setMinValue(0);
        interval_number_picker.setMaxValue(59);
        //

        //get initial values for these objects based on what the user's current settings are
        if(settingsHandler.getAutoupdate() > interval_number_picker.getMaxValue()){
            interval_number_picker.setValue(settingsHandler.getAutoupdate() / 60); //set the value of the spinner to current value in user settings
            time_units_spinner.setSelection(1); //set the units to hours
        }else{
            interval_number_picker.setValue(settingsHandler.getAutoupdate());
            time_units_spinner.setSelection(0); //set the units to minutes
        }

        interval_number_picker.setWrapSelectorWheel(true);

        //create selection listeners for NumberPicker and Spinner for interval options
        createIntervalSelectionListeners(settingsHandler, interval_number_picker, time_units_spinner);

        //listeners for each toggle button on the page
        profiles_toggle_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {  //executes when the profiles button is clicked
                    profiles_toggle_button.setBackgroundColor(getResources().getColor(R.color.colorAccent)); //change color of profiles button

                    //set visibility of profiles menu to visible
                    profile_menu.setVisibility(View.VISIBLE);

                    //set other buttons to unchecked
                    intervals_toggle_button.setChecked(false);
                    notifications_toggle_button.setChecked(false);

                    //
                    intervals_toggle_button.setTranslationY((float)600.0); // move intervals button to new position
                    notifications_toggle_button.setTranslationY((float)600.0); // move intervals button to new position
                } else {    //executes when the profiles button is un-clicked
                    //set visibility of profiles menu to gone
                    profile_menu.setVisibility(View.GONE);

                    profiles_toggle_button.setBackgroundColor(grayColorValue); //set color of profiles button

                    intervals_toggle_button.setTranslationY((float)0.0); // move intervals button to original position
                    notifications_toggle_button.setTranslationY((float)0.0); // move notifications button to original position
                }
            }
        });

        intervals_toggle_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {  //executes when the intervals button is clicked
                    intervals_toggle_button.setBackgroundColor(getResources().getColor(R.color.colorAccent)); //change color of intervals button

                    //set visibility of intervals items to visible
                    interval_text.setVisibility(View.VISIBLE);
                    interval_number_picker.setVisibility(View.VISIBLE);
                    time_units_spinner.setVisibility(View.VISIBLE);

                    //set other buttons to unchecked
                    profiles_toggle_button.setChecked(false);
                    notifications_toggle_button.setChecked(false);

                    notifications_toggle_button.setTranslationY((float)150.0); // move intervals button to new position
                } else {  //executes when the intervals button is un-clicked
                    //set visibility of intervals items to gone
                    interval_text.setVisibility(View.GONE);
                    interval_number_picker.setVisibility(View.GONE);
                    time_units_spinner.setVisibility(View.GONE);

                    intervals_toggle_button.setBackgroundColor(grayColorValue); //set color of intervals button
                    notifications_toggle_button.setTranslationY((float)0.0); // move notifications button to original position
                }
            }
        });

        notifications_toggle_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { //executes when the notifications button is clicked
                    notifications_toggle_button.setBackgroundColor(getResources().getColor(R.color.colorAccent)); //change color of notifications button

                    //set visibility of notifications items to visible
                    notifs_switch.setVisibility(View.VISIBLE);
                    notifs_label.setVisibility(View.VISIBLE);
                    notifs_on_articles_label.setVisibility(View.VISIBLE);

                    //set other buttons to unchecked
                    profiles_toggle_button.setChecked(false);
                    intervals_toggle_button.setChecked(false);
                } else { //executes when the notifications button is un-clicked
                    notifications_toggle_button.setBackgroundColor(grayColorValue); //set color of notifications button

                    //set visibility of intervals items to gone
                    notifs_switch.setVisibility(View.GONE);
                    notifs_label.setVisibility(View.GONE);
                    notifs_on_articles_label.setVisibility(View.GONE);
                }
            }
        });

        /* Profile Menu - Load user profiles as buttons in a menu - doing this using a table layout of buttons */
        TableLayout table = (TableLayout) root.findViewById(R.id.profiles_menu_table);
        File directory = new File(settingsHandler.getApplicationInfo().dataDir,"shared_prefs");
        File[] files = directory.listFiles();

        //get the dimensions of each profile button in dp, rather than px (more scalable)
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float button_width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, dm);
        float button_height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, dm);

        String filename;

        TableRow row;
        Button profile_button;
        for(int i = 0; i < files.length; i++) {
            if(!files[i].getName().equalsIgnoreCase("app_prefs.xml")) { //skip this file
                filename = files[i].getName();
                filename = filename.substring(0, filename.length() - 4); //remove the .xml part of the file name
                //profiles.add(filename);

                //create a table row and a button in that row to make up the profile menu
                row = new TableRow(getContext());
                row.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                profile_button = new Button(getContext());
                profile_button.setWidth((int)button_width);
                profile_button.setHeight((int)button_height);

                profile_button.setText((CharSequence)filename);
                profile_button.setTextColor((int)Color.parseColor("#000000"));
                profile_button.setBackgroundColor(Color.parseColor("#FFFFFF"));
                profile_button.setAllCaps(false);

                setProfileClickListener(settingsHandler, profile_button, filename, profiles_toggle_button, interval_number_picker, time_units_spinner, notifs_switch);
                row.addView(profile_button);
                table.addView(row);
            }
        }

        /* Notification options - update based on switch value */
        notifs_switch.setChecked(settingsHandler.getNotifications()); //set value of switch to users current settings
        notifs_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settingsHandler.setNotifications(isChecked);
                String bool = (isChecked)? "on. " : "off. ";
                Toast.makeText(getContext(), "Notifications for new articles turned " + bool , Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    private void createIntervalSelectionListeners(final SettingsHandler settingsHandler, final NumberPicker numberPicker, final Spinner spinner){
        numberPicker.setOnScrollListener(new NumberPicker.OnScrollListener(){
            @Override
            public void onScrollStateChange(NumberPicker numberPicker, int scrollState) {
                if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                    int value = numberPicker.getValue();
                    String unit = (String)spinner.getSelectedItem();
                    if(unit.equalsIgnoreCase("hours")){
                        // if the user has selected hours, multiply the NumberPicker's value by 60 since the default unit is in minutes
                        settingsHandler.setAutoupdate(value * 60);
                    }else{
                        settingsHandler.setAutoupdate(value);
                    }
                    Toast.makeText(getContext(), "Selected: Automatically update feed after " + value + " " + unit , Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int value = numberPicker.getValue();
                String unit = parent.getItemAtPosition(position).toString(); // get the selected item
                Toast.makeText(parent.getContext(), "Selected: Automatically update feed after " + value + " " + unit , Toast.LENGTH_SHORT).show();
                if(unit.equalsIgnoreCase("hours")){
                    // if the user has selected hours, multiply the NumberPicker's value by 60 since the default unit is in minutes
                    settingsHandler.setAutoupdate(value * 60);
                }else{
                    settingsHandler.setAutoupdate(value);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setSelection(0);
            }
        });
    }

    //click listener for the buttons in the profiles menu
    private void setProfileClickListener(final SettingsHandler sh, Button b, final String text, final ToggleButton profile_menu,
                                         final NumberPicker numberPicker, final Spinner spinner, final Switch sw){
        try {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sh.setCurrentUser(text);

                    Toast.makeText(getContext(), "Profile Selected: " + text, Toast.LENGTH_SHORT).show();// Show selection in a Toast popup
                    //close profile_toggle_button
                    profile_menu.setChecked(false);
                    onChangeUser(sh, numberPicker, spinner, sw);
                }
            });
        }catch(NullPointerException npe){
            System.err.println("NullPointerException: Error setting click listener for profile button. ");
        }
    }

    //sets the states of the buttons to the values of the new user's settings
    private void onChangeUser(final SettingsHandler settingsHandler, final NumberPicker numberPicker, final Spinner spinner, final Switch sw){
        if(settingsHandler.getAutoupdate() > numberPicker.getMaxValue()){
            numberPicker.setValue(settingsHandler.getAutoupdate() / 60); //set the value of the spinner to current value in user settings
            spinner.setSelection(1); //set the units to hours
        }else{
            numberPicker.setValue(settingsHandler.getAutoupdate());
            spinner.setSelection(0); //set the units to minutes
        }

        sw.setChecked(settingsHandler.getNotifications());
    }
}