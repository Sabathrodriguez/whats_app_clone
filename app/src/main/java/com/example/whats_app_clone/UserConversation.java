package com.example.whats_app_clone;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class UserConversation extends AppCompatActivity implements View.OnClickListener {
    private ListView chatListView;
    private ArrayList<String> chatsList;
    private ArrayAdapter adapter;
    private String selectedUser;
//    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_conversation);

//        swipeRefreshLayout = findViewById(R.id.swipeContainer2);

        selectedUser = getIntent().getStringExtra("username");
        setTitle("Conversations with: " + selectedUser);
//        setTitle("Conversation with " + Conversations_page.getOtherUser());

        findViewById(R.id.sendMessageButton).setOnClickListener(this);

        chatListView = findViewById(R.id.chatListView);
        chatsList = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, chatsList);
        chatListView.setAdapter(adapter);

        final ArrayList<ParseQuery<ParseObject>> allQueries = new ArrayList<>();
        final ParseQuery<ParseObject> firstUserChatQuery = ParseQuery.getQuery("Chat");
        ParseQuery<ParseObject> secondUserChatQuery = ParseQuery.getQuery("Chat");

        try {
            firstUserChatQuery.whereEqualTo("sender", ParseUser.getCurrentUser().getUsername());
            firstUserChatQuery.whereEqualTo("targetRecipient", selectedUser);

            secondUserChatQuery.whereEqualTo("sender", selectedUser);
            secondUserChatQuery.whereEqualTo("targetRecipient", ParseUser.getCurrentUser().getUsername());

            allQueries.add(firstUserChatQuery);
            allQueries.add(secondUserChatQuery);

            final ParseQuery<ParseObject> myQuery = ParseQuery.or(allQueries);
            myQuery.orderByAscending("createdAt");

            myQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if ((!objects.isEmpty()) && e == null) {
                        for (ParseObject el : objects) {
                            String message = (String) el.get("message");
                            if (el.get("sender").equals(ParseUser.getCurrentUser().getUsername())) {
                                message = ParseUser.getCurrentUser().getUsername() + ": " + message;
                            }
                            if (el.get("sender").equals(selectedUser)) {
                                message = selectedUser + ": " + message;
                            }

                            chatsList.add(message);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        } catch (Exception e ) {
            e.printStackTrace();
        }

//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                try {
//                    final ParseQuery<ParseObject> myQuery = ParseQuery.or(allQueries);
//                    myQuery.orderByAscending("createdAt");
//
//                    myQuery.findInBackground(new FindCallback<ParseObject>() {
//                        @Override
//                        public void done(List<ParseObject> objects, ParseException e) {
//                            if ((!objects.isEmpty()) && e == null) {
//                                for (ParseObject el : objects) {
//                                    String message = (String) el.get("message");
//                                    if (el.get("sender").equals(ParseUser.getCurrentUser().getUsername())) {
//                                        message = ParseUser.getCurrentUser().getUsername() + ": " + message;
//                                    }
//                                    if (el.get("sender").equals(selectedUser)) {
//                                        message = selectedUser + ": " + message;
//                                    }
//
//                                    chatsList.add(message);
//                                }
//                                adapter.notifyDataSetChanged();
//                            }
//                        }
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendMessageButton:
                final EditText edtMessage = findViewById(R.id.edtMessageBox);

                ParseObject chat = new ParseObject("Chat");
                chat.put("sender", ParseUser.getCurrentUser().getUsername());
                chat.put("targetRecipient", selectedUser);
                chat.put("message", edtMessage.getText().toString());
                chat.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            chatsList.add(ParseUser.getCurrentUser().getUsername().toString() + ": " + edtMessage.getText().toString());
                            adapter.notifyDataSetChanged();
                            edtMessage.setText("");
                        }
                    }
                });

                break;
        }
    }
}
