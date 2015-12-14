package com.cmpe277.skibuddy;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SkiDetailActivity extends AppCompatActivity {

    private TextView skiDistance;
    private TextView skiTime;
    private TextView detailTitle;
    JSONObject skiDetails ;
    JSONArray trace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ski_detail);

        skiDistance = (TextView) findViewById(R.id.skiDistance);
        skiTime = (TextView) findViewById(R.id.skiTime);
        detailTitle = (TextView) findViewById(R.id.detailTitle);
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            skiDetails= null;
        } else {
            try {
                skiDetails= new JSONObject(extras.getString("SkiDetails"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        populateDetails();


    }

    void populateDetails()
    {

                try {
                    skiDistance.setText(skiDetails.get("distance").toString());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
                    try {
                        Date starttime = dateFormat.parse(skiDetails.get("start_time").toString());
                        Date endtime = dateFormat.parse(skiDetails.get("end_time").toString());
                        String duration = String.valueOf(((endtime.getTime() - starttime.getTime()) / 1000));
                        trace = skiDetails.getJSONArray("location_trace");
                        skiTime.setText( duration + " Seconds");
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ski_detail, menu);
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
    public void viewTrace(View view)
    {
        Gson gson = new Gson();
        //String json = gson.toJson(pointList);
        Intent intent = new Intent(this, SessionTrace.class);
        intent.putExtra("SkiDetails", trace.toString());
        startActivity(intent);

    }
}
