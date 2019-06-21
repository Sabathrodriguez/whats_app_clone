package com.example.whats_app_clone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Conversations_page extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private ArrayList<String> users;
    private ArrayAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static String otherUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversations_page);

        users = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, users);
        swipeRefreshLayout = findViewById(R.id.swipeContainer);

//        ParseUser.logOut();
//        finish();

        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(Conversations_page.this);


        try {
            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();

            parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if ((! objects.isEmpty()) && e == null) {
                        for (ParseUser el : objects) {
                            users.add(el.getUsername());
                        }
                        listView.setAdapter(adapter);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();

                    parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
                    parseQuery.whereNotContainedIn("username", users);
                    parseQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                            if ((! objects.isEmpty()) && e == null) {
                                for (ParseUser el : objects) {
                                    users.add(el.getUsername());
                                }

                                adapter.notifyDataSetChanged();
                                if (swipeRefreshLayout.isRefreshing()) {
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            } else {
                                if (swipeRefreshLayout.isRefreshing()) {
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

//    public void getUserRefresh() {
//        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
//
//        parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
//        parseQuery.findInBackground(new FindCallback<ParseUser>() {
//            @Override
//            public void done(List<ParseUser> objects, ParseException e) {
//                if ((! objects.isEmpty()) && e == null) {
//                    for (ParseUser el : objects) {
//                        users.add(el.getUsername());
//                    }
//                    listView.setAdapter(adapter);
//                }
//            }
//        });
//    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.logoutUserItem) {
            ParseUser.getCurrentUser().logOut();
            finish();
            Intent intent = new Intent(Conversations_page.this, SignUp.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(Conversations_page.this, UserConversation.class);

        otherUser = users.get(position);

        intent.putExtra("username", otherUser);
        startActivity(intent);
    }

    public static String getOtherUser() {
        return otherUser;
    }


}
