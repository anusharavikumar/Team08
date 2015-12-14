package com.cmpe277.skibuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by goudamy on 11/30/2015.
 */
public class ListFriends extends AppCompatActivity {
    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;

    Button startSki;

    String eventId;
    String start, end;
    Date startTime;
    Date endTime;
    ArrayList<String> friendId = new ArrayList<String>();
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findfriends);

        mainListView = (ListView) findViewById(R.id.friendListView);
        startSki= (Button) findViewById(R.id.buttonMap);

        Intent intent = getIntent();
        String id = intent.getStringExtra("Event_ID");
        start= intent.getStringExtra("Start");
        System.out.println ("Start Received"+ start);
        System.out.println ("End Received"+ start);
        end = intent.getStringExtra("End");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            startTime=sdf.parse(start);
            endTime= sdf.parse(end);
        } catch (ParseException e) {
            sdf=new SimpleDateFormat("dd:MM:yyyy HH:mm:ss");
            try {
                startTime=sdf.parse(start);
                endTime= sdf.parse(end);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }

        }

        setEventId(id);
        System.out.println("Event Id found"+id);
      //  Toast.makeText(getApplicationContext(), "Here we go " + id, Toast.LENGTH_LONG)
        Toast.makeText(getApplicationContext(), "Here we go ", Toast.LENGTH_LONG)
                .show();
        //TO DO: Use API's to include friends list

        String entire = "'event_id'" + ":'" + id +"'";
        //String entire = "'event_id'" + ":" + id +"";
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
                                //String[] friends = jArray.toString().replace("},{", " ,").replace("[", "").replace("]", "").replace("\"", "").split(",");
                                ArrayList<String> friendList = new ArrayList<String>();
                                {
                                    for(int i = 0; i < jArray.length(); i++) {

                                        JSONObject obj = (JSONObject) jArray.get(i);
                                        friendList.add(obj.get("user_name").toString());
                                        friendId.add(obj.get("user_id").toString());

                                    }
                                }


                                listAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.friends,R.id.friendName, friendList);
                                mainListView.setAdapter( listAdapter );
                                mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    default:
                        Intent i = new Intent(getApplicationContext(), SkiDetailListActivity.class);
                        i.putExtra("userID", MainActivity.getUserEmail());
                        i.putExtra("playerID",friendId.get(position));
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
        Boolean flag = false;
        Date d= new Date();
        System.out.println("Date"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d));
        System.out.println("Start"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime));
        System.out.println("End"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime));
        if(startTime.compareTo(d)<=0 && endTime.compareTo(d)>=0 ){
            flag=true;
        }
        System.out.println("Flag"+flag);
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("EventId",getEventId());
        intent.putExtra("Valid", flag);
        startActivity(intent);

    }
    public void setEventId(String eventId) {

        this.eventId = eventId;
    }
    public String getEventId(){
        return this.eventId;
    }
}
