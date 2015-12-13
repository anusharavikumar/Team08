package com.cmpe277.skibuddy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cmpe277.skibuddy.adapters.CustomRecordsAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class SkiDetailListActivity extends AppCompatActivity {

    String userId;
    String playerId;
    JSONArray data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ski_detaillist);
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            userId= null;
            playerId = null;
        } else {
            userId= extras.getString("userID");
            playerId = extras.getString("playerID");
        }
        populateUsersList();
    }

    private void populateUsersList() {

       final Context c = this;
        new Thread() {
            @Override
            public void run() {
                // Construct the data source
                //final ArrayList<Record> arrayOfRecords = ServicesHelper.shared().getAllSkiRecordsForUser(userId,playerId,c);
                // Create the adapter to convert the array to views
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                try {
                    StringEntity entity = new StringEntity("{'data': [{'user_id':'rajini', 'player_id':'purvi'}]}");
                    client.post(getApplicationContext(), "http://52.90.230.67:8000/getSkirecords/", entity, "application/json",
                            new AsyncHttpResponseHandler(Looper.getMainLooper()) {
                                @Override
                              public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    // status has to be 200
                                    Log.d("Response", "" + responseBody);
                                    try {
                                        JSONObject arr = new JSONObject(new String(responseBody));
                                        Log.d("Response in json", "" + arr);
                                        data = arr.getJSONArray("data");
                                        final ArrayList<Record> arrayOfRecords = Record.getRecords(data);
                                        SkiDetailListActivity.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                CustomRecordsAdapter adapter = new CustomRecordsAdapter(c, arrayOfRecords);
                                                // Attach the adapter to a ListView
                                                ListView listView = (ListView) findViewById(R.id.lvUsers);
                                                listView.setAdapter(adapter);
                                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        try {
                                                            printDetailScreen(position,id);
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                            }
                                        });

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                                        error) {
                                    Log.d("Failed", "On failure called");
                                }
                            });
                }

                catch(UnsupportedEncodingException e) {

                }


            }
        }.start();

    }


    void printDetailScreen(int position, long id) throws JSONException {
        Intent intent = new Intent(this, SkiDetailActivity.class);
        intent.putExtra("SkiDetails", data.get(position).toString());
        Toast.makeText(this, "Button Clicked pos: " + position + "  id:  " + id, Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ski_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
