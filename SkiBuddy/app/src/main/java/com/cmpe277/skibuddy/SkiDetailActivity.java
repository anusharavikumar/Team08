package com.cmpe277.skibuddy;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cmpe277.skibuddy.helpers.ServicesHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SkiDetailActivity extends AppCompatActivity {

    private TextView skiDistance;
    private TextView skiTime;
    private TextView detailTitle;
    JSONObject skiDetails ;
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
        //TODO: Make service call and get details for the particular SKI record. Also draw map. Displaying image for now
        final Context context = this;
        new Thread() {
            @Override
            public void run() {
                //Record record = ServicesHelper.shared().getSkiRecordDetailForUser("recordId",context);
                try {
                    skiDistance.setText(skiDetails.get("distance").toString());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
                    try {
                        Date starttime = dateFormat.parse(skiDetails.get("start_time").toString());
                        Date endtime = dateFormat.parse(skiDetails.get("end_time").toString());
                        skiTime.setText(String.valueOf(endtime.getTime() - starttime.getTime()));
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block

                    }
                    //skiTime.setText(skiDetails.get("");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.start();

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
        Intent intent = new Intent(this, SessionTrace.class);
        startActivity(intent);

    }
}
