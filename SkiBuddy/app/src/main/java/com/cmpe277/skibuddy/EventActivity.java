package com.cmpe277.skibuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.astuetz.PagerSlidingTabStrip;

public class EventActivity extends ActionBarActivity {
    ViewPager pager;
    TabPage TabAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
         Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
      
        TabAdapter = new TabPage(getSupportFragmentManager(),userName);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(TabAdapter);
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);






    }
}
