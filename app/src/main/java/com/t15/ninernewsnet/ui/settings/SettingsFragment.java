 package com.t15.ninernewsnet.ui.settings;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.t15.ninernewsnet.R;
import com.t15.ninernewsnet.SettingsHandler;

import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.File.*;

public class SettingsFragment extends Fragment  implements AdapterView.OnItemSelectedListener {

    private SettingsViewModel settingsViewModel;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_settings, container, false);
        //final CardView cardView = root.findViewById(R.id.itemCard);


        final SettingsHandler settingsHandler = new SettingsHandler(getContext());

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
        final Switch switch1 = (Switch) root.findViewById(R.id.settings_notif_on_alerts_switch);
        final Switch switch2 = (Switch) root.findViewById(R.id.settings_notif_on_new_articles_switch);
        final Switch switch3 = (Switch) root.findViewById(R.id.settings_popup_on_alerts_switch);
        final Switch switch4 = (Switch) root.findViewById(R.id.settings_popup_on_new_articles_switch);
        final TextView notifs_label = (TextView) root.findViewById(R.id.settings_notifs_on_label);
        final TextView popup_label = (TextView) root.findViewById(R.id.settings_popup_on_label);
        final TextView notifs_on_alerts_label = (TextView) root.findViewById(R.id.settings_notif_on_alerts_label);
        final TextView notifs_on_articles_label = (TextView) root.findViewById(R.id.settings_notif_on_new_articles_label);
        final TextView popup_on_alerts_label = (TextView) root.findViewById(R.id.settings_popup_on_alerts_label);
        final TextView popup_on_articles_label = (TextView) root.findViewById(R.id.settings_popup_on_new_article_label);

        /* Implement Spinner on Settings page for interval options */
        List<String> interval_spinner_options = new ArrayList<String>();
        interval_spinner_options.add("minutes");
        interval_spinner_options.add("hours");
        interval_spinner_options.add("days");

        time_units_spinner.setOnItemSelectedListener(this); //set listener for interval spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, interval_spinner_options); // Create adapter for spinner
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Drop down layout style - list view with radio button
        time_units_spinner.setAdapter(dataAdapter); // Attach data adapter to spinner

        /* Implement NumberPicker on Settings Page for interval options */
        interval_number_picker.setMinValue(0);
        interval_number_picker.setMaxValue(10);
        interval_number_picker.setWrapSelectorWheel(true);
        interval_number_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //TODO: Set update interval (autoupdate()?)

                String text = "Changed from " + oldVal + " to " + newVal;
                Toast.makeText(getParentFragment().getContext(), text, Toast.LENGTH_SHORT).show();
            }
        });

//*****************************************************************************************************************
        profiles_toggle_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    profiles_toggle_button.setBackgroundColor(getResources().getColor(R.color.colorAccent)); //change color of profiles button

                    //set visibility of profiles menu to visible
                    profile_menu.setVisibility(View.VISIBLE);

                    //set other buttons to unchecked
                    intervals_toggle_button.setChecked(false);
                    notifications_toggle_button.setChecked(false);

                    intervals_toggle_button.setTranslationY((float)600.0); // move intervals button to new position
                    notifications_toggle_button.setTranslationY((float)600.0); // move intervals button to new position
                } else {
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
                if (isChecked) {
                    intervals_toggle_button.setBackgroundColor(getResources().getColor(R.color.colorAccent)); //change color of intervals button

                    //set visibility of intervals items to visible
                    interval_text.setVisibility(View.VISIBLE);
                    interval_number_picker.setVisibility(View.VISIBLE);
                    time_units_spinner.setVisibility(View.VISIBLE);

                    //set other buttons to unchecked
                    profiles_toggle_button.setChecked(false);
                    notifications_toggle_button.setChecked(false);

                    notifications_toggle_button.setTranslationY((float)150.0); // move intervals button to new position
                } else {
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
                if (isChecked) {
                    notifications_toggle_button.setBackgroundColor(getResources().getColor(R.color.colorAccent)); //change color of notifications button

                    //set visibility of notifications items to visible
                    switch1.setVisibility(View.VISIBLE);
                    switch2.setVisibility(View.VISIBLE);
                    switch3.setVisibility(View.VISIBLE);
                    switch4.setVisibility(View.VISIBLE);
                    notifs_label.setVisibility(View.VISIBLE);
                    popup_label.setVisibility(View.VISIBLE);
                    notifs_on_alerts_label.setVisibility(View.VISIBLE);
                    notifs_on_articles_label.setVisibility(View.VISIBLE);
                    popup_on_alerts_label.setVisibility(View.VISIBLE);
                    popup_on_articles_label.setVisibility(View.VISIBLE);

                    //set other buttons to unchecked
                    profiles_toggle_button.setChecked(false);
                    intervals_toggle_button.setChecked(false);
                } else {
                    notifications_toggle_button.setBackgroundColor(grayColorValue); //set color of notifications button

                    //set visibility of intervals items to gone
                    switch1.setVisibility(View.GONE);
                    switch2.setVisibility(View.GONE);
                    switch3.setVisibility(View.GONE);
                    switch4.setVisibility(View.GONE);
                    notifs_label.setVisibility(View.GONE);
                    popup_label.setVisibility(View.GONE);
                    notifs_on_alerts_label.setVisibility(View.GONE);
                    notifs_on_articles_label.setVisibility(View.GONE);
                    popup_on_alerts_label.setVisibility(View.GONE);
                    popup_on_articles_label.setVisibility(View.GONE);
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
        for(int i = 0; i < files.length; i++) {
            if(!files[i].getName().equalsIgnoreCase("app_prefs.xml")) { //skip this file
                filename = files[i].getName();
                filename = filename.substring(0, filename.length() - 4); //remove the .xml part of the file name
                //profiles.add(filename);

                //create a table row and a button in that row to make up the profile menu
                TableRow row = new TableRow(getContext());
                row.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                Button profile_button = new Button(getContext());
                profile_button.setWidth((int)button_width);
                profile_button.setHeight((int)button_height);

                profile_button.setText((CharSequence)filename);
                profile_button.setTextColor((int)Color.parseColor("#000000"));
                profile_button.setBackgroundColor(Color.parseColor("#FFFFFF"));
                profile_button.setAllCaps(false);

                setProfileClickListener(settingsHandler, profile_button, filename, profiles_toggle_button);
                row.addView(profile_button);
                table.addView(row);
            }
        }

        return root;
    }

    //Selection handlers for Interval spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString(); // On selecting a spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();// Show selection in a Toast popup

        // Add actual code to change the update interval
        //TODO
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
        // Do i need code here???
    }

    private void setProfileClickListener(final SettingsHandler sh, Button b, final String text, final ToggleButton profile_menu){
        try {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sh.setCurrentUser(text);
                    Toast.makeText(getContext(), "Profile Selected: " + text, Toast.LENGTH_LONG).show();// Show selection in a Toast popup
                    //close profile_toggle_button
                    profile_menu.setChecked(false);
                }
            });
        }catch(NullPointerException npe){
            System.err.println("NullPointerException: Error setting click listener for profile button. ");
        }
    }

}