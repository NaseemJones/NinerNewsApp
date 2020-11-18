package com.t15.ninernewsnet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        Button loginButton = findViewById(R.id.loginButton);
        final EditText username = findViewById(R.id.username);

        final SettingsHandler settingsHandler = new SettingsHandler(LoginView.this);
        if (settingsHandler.getCurrentUser() != null) {
            //if a name exists, use it
            username.setText(settingsHandler.getCurrentUser());
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                //app_prefs is protected
                if(username.getText() != null && !name.equals("app_prefs") && !name.equals("")){
                    settingsHandler.setCurrentUser(name);

                    Intent myIntent = new Intent(v.getContext(), MainView.class);
                    // myIntent.putExtra("name", displayName);
                    startActivity(myIntent);

                }
                // else: notify wrong name
            }
        });
    }
}
