package com.cmpe277.skibuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by goudamy on 11/30/2015.
 */
public class ListFriends extends Activity {
    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findfriends);

        mainListView = (ListView) findViewById(R.id.friendListView);
        Intent intent = getIntent();
        String id = intent.getStringExtra("Event_ID");
        //Toast.makeText(getApplicationContext(), "Here we go " + id, Toast.LENGTH_LONG).show();
        //TO DO: Use API's to include friends list
        String[] friends = new String[] { "Mercury", "Venus", "Earth", "Mars",
                "Jupiter", "Saturn", "Uranus", "Neptune"};
        ArrayList<String> friendList = new ArrayList<String>();
        friendList.addAll( Arrays.asList(friends) );


        listAdapter = new ArrayAdapter<String>(this, R.layout.friends,R.id.friendName, friendList);


        mainListView.setAdapter( listAdapter );
    }
}
