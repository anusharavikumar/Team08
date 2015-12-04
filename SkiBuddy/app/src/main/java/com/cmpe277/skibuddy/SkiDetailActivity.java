package com.cmpe277.skibuddy;


import android.content.Intent;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cmpe277.skibuddy.helpers.ServicesHelper;

public class SkiDetailActivity extends FragmentActivity {

    private TextView skiDistance;
    private TextView skiTime;
    private TextView detailTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ski_detail);

        skiDistance = (TextView) findViewById(R.id.skiDistance);
        skiTime = (TextView) findViewById(R.id.skiTime);
        detailTitle = (TextView) findViewById(R.id.detailTitle);
        populateDetails();
    }

    void populateDetails()
    {
        //TODO: Make service call and get details for the particular SKI record. Also draw map. Displaying image for now
        final Context context = this;
        new Thread() {
            @Override
            public void run() {
                Record record = ServicesHelper.shared().getSkiRecordDetailForUser("recordId",context);
                skiDistance.setText(record.distance);
                skiTime.setText(record.time);
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
