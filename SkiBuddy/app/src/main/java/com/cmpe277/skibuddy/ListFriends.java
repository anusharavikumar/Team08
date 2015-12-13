package com.cmpe277.skibuddy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by goudamy on 11/30/2015.
 */
public class ListFriends extends AppCompatActivity {
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
        Toast.makeText(getApplicationContext(), "Here we go " + id, Toast.LENGTH_LONG)
                .show();
        //TO DO: Use API's to include friends list

        String entire = "'event_id'" + ":'" + id +"'";
        String[] frnds;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        try {
            StringEntity entity = new StringEntity("{'data': [{"+ entire+"}]}");

            client.post(getApplicationContext(), "http://52.90.230.67:8000/getEventMembers/", entity, "application/json",
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            try {
                                JSONObject arr = new JSONObject(new String(responseBody));
                                JSONObject jsonObject = arr.getJSONObject("data");
                                JSONArray jArray = jsonObject.getJSONArray("events");
                                String[] friends = jArray.toString().replace("},{", " ,").replace("[", "").replace("]", "").replace("\"", "").split(",");
                                ArrayList<String> friendList = new ArrayList<String>();
                                friendList.addAll( Arrays.asList(friends) );


                                listAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.friends,R.id.friendName, friendList);
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

                            } catch (JSONException e
                                    )

                            {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {
                            Log.d("Failed", "On failure called");
                        }
                    });
        }

        catch(UnsupportedEncodingException e) {

        }


    }
    
    public void startMapView(View view) {
        Log.d("Hello", "Hello");
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("EventId",eventId);
        startActivity(intent);

    }
}
