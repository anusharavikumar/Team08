package com.cmpe277.skibuddy;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.cmpe277.skibuddy.adapters.CustomRecordsAdapter;
import com.cmpe277.skibuddy.helpers.ServicesHelper;

import java.util.ArrayList;

public class SkiDetailListActivity extends FragmentActivity {

    String userId;
    String playerId;

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
                final ArrayList<Record> arrayOfRecords = ServicesHelper.shared().getAllSkiRecordsForUser(userId,playerId,c);
                // Create the adapter to convert the array to views
                SkiDetailListActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        CustomRecordsAdapter adapter = new CustomRecordsAdapter(c, arrayOfRecords);
                        // Attach the adapter to a ListView

                        ListView listView = (ListView) findViewById(R.id.lvUsers);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                printDetailScreen();
                            }
                        });
                    }
                });


            }
        }.start();

    }


    void printDetailScreen()
    {
        Intent intent = new Intent(this, SkiDetailActivity.class);
        Toast.makeText(this, "Button Clicked ", Toast.LENGTH_SHORT).show();
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
