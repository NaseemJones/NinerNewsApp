package com.t15.ninernewsnet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        Button loginButton = findViewById(R.id.loginButton);
        final EditText username = findViewById(R.id.username);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText() != null && !username.getText().toString().equals("")){
                    String displayName = username.getText().toString();
                    Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                    myIntent.putExtra("name", displayName);
                    startActivity(myIntent);

                }
            }
        });
    }
}
