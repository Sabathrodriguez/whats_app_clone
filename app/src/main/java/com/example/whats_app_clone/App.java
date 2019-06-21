package com.example.whats_app_clone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("M54rDsCHVnAz5qRwAP3OewFkRvNleCCNDOhHwVOx")
                // if defined
                .clientKey("kYkhspnJ98l1YUbNOWqyR0yDajrCNt5cKRveSaMz")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
