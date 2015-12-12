package com.cmpe277.skibuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
        String[] friends = new String[] {"Anusha", "Goudamy", "Sockalingam", "Dhanu",
                "Aastha", "Purvi", "Harsha", "Tilak"};
        ArrayList<String> friendList = new ArrayList<String>();
        friendList.addAll( Arrays.asList(friends) );


        listAdapter = new ArrayAdapter<String>(this, R.layout.friends,R.id.friendName, friendList);


        mainListView.setAdapter( listAdapter );

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    default:
                        Intent i = new Intent(getApplicationContext(), SkiDetailListActivity.class);
                        i.putExtra("user_id", "harsha");
                        startActivity(i);
                }

            }
        });
    }

    public void startMapView(View view) {
        Log.d("Hello", "Hello");
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);

    }
}
