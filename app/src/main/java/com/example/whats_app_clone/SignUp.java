package com.example.whats_app_clone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    private EditText username;
    private EditText email;
    private EditText password;
    private Button signup;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.signup_username_txt);
        email = findViewById(R.id.signup_email_txt);
        password = findViewById(R.id.signup_password_txt);

        password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onClick(signup);
                }
                return false;
            }
        });

        signup = findViewById(R.id.signup_page_sign_up_btn);
        login = findViewById(R.id.signup_page_login_btn);

        signup.setOnClickListener(SignUp.this);
        login.setOnClickListener(SignUp.this);

        if (ParseUser.getCurrentUser() != null) {
            transitionToConversations();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_page_login_btn:
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
                finish();
                break;
            case R.id.signup_page_sign_up_btn:
                if (email.getText().toString().isEmpty() || username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    FancyToast.makeText(SignUp.this, "Email, Username, Password is required!", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
                } else {
                    final ParseUser parseUser = new ParseUser();
                    parseUser.setEmail(email.getText().toString());
                    parseUser.setUsername(username.getText().toString());
                    parseUser.setPassword(password.getText().toString());

                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Signing Up " + username.getText().toString());
                    progressDialog.show();

                    parseUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                FancyToast.makeText(SignUp.this, parseUser.getUsername().toString() + " is signed up!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                            } else {
                                FancyToast.makeText(SignUp.this, "There was an error: " + e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
                }
                transitionToConversations();
                break;
        }
    }

    private void transitionToConversations() {
        Intent intent = new Intent(SignUp.this, Conversations_page.class);
        startActivity(intent);
        finish();
    }
}
