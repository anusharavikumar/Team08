package com.cmpe277.skibuddy;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by goudamy on 10/3/2015.
 */
public class TabPage extends FragmentStatePagerAdapter {


    public TabPage(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment f = null;
        if(i == 0) {

          //  f = new CreateEvent();
        }
           else if(i ==1){
          //  f = new ListEvent();

        }
        return f;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 2; //No of Tabs
    }
@Override
    public  CharSequence getPageTitle(int position){
    if(position == 1){
        return "List Events";
    }else if(position == 0){
        return "Create Event";
    }
    return super.getPageTitle(position);
}

}
