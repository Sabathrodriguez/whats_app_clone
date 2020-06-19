package com.example.whats_app_clone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Login extends AppCompatActivity implements View.OnClickListener{
    private EditText username;
    private EditText password;
    private Button loginButton;
    private Button signpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.login_username_txt);
        password = findViewById(R.id.login_password_txt);

        password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onClick(loginButton);
                }
                return false;
            }
        });

        loginButton = findViewById(R.id.login_page_login_btn);
        signpButton = findViewById(R.id.login_page_sign_up_btn);

        loginButton.setOnClickListener(Login.this);
        signpButton.setOnClickListener(Login.this);

        if (ParseUser.getCurrentUser() != null) {
            transitionToConversations();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_page_login_btn:
                if (username.getText().toString().isEmpty() || username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    FancyToast.makeText(Login.this, "////Email, Username, Password is required!", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
                } else {
                    ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(),
                            new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {
                                    if (e == null && user != null) {
                                        FancyToast.makeText(Login.this,
                                                user.getUsername().toString() + " is successfully logged in.",
                                                FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                                        transitionToConversations();
                                    } else {
                                        FancyToast.makeText(Login.this,
                                                "Error message: " + e.getMessage(),
                                                FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                                    }
                                }
                            });
                }
                break;
            case R.id.login_page_sign_up_btn:
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FancyToast.makeText(this, ParseUser.getCurrentUser().getUsername() + " is logged out!", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
        finish();
    }

    private void transitionToConversations() {
        Intent intent = new Intent(Login.this, Conversations_page.class);
        startActivity(intent);
        finish();
    }
}
