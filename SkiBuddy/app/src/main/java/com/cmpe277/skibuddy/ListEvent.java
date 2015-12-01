package com.cmpe277.skibuddy;

/**
 * Created by goudamy on 10/3/2015.
 */
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListEvent extends Fragment {

    //To Do: use APIs to call all the details of the event and fill in the following array structure
    String[] name = {"ski", "happy sking", "skydiving", "skiers"};
    String[] start_date = {"10-20-2015", "10-21-2015", "10-22-2015", "10-23-2015"};
    String[] end_date = {"10-20-2015", "10-21-2015", "10-22-2015", "10-23-2015"};
    String[] start_time = {"10:20", "10:21", "10:22", "10:23"};
    String[] end_time = {"11:20", "11:21", "11:22", "12:23"};
    String[] eventId = {"1", "2", "3", "4"};
    String[] title = {"1", "2", "3", "4"};
    //Here join variable is used to find if the user has joined the event or not
    boolean[] join = {true, false, true, false};
    ListView listview;
    String[] venue = {"Fremont", "Livermore", "SanJose", "Berkley"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View search = inflater.inflate(R.layout.search_results, container, false);
        Resources res = getResources();
        listview = (ListView) search.findViewById(R.id.listView);
        eventDetails img = new eventDetails(getContext(), name, start_date, end_date, start_time, end_time, venue, join, eventId, title);
        listview.setAdapter(img);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getContext(), "Click ListItem Number " + position, Toast.LENGTH_LONG)
                        .show();
            }
        });

        return search;

    }


}

class eventDetails extends ArrayAdapter<String> {
    Context context;
    String[] event_venue;
    String[] event_name;
    String[] event_date;
    String[] event_endDate;
    String[] event_time;
    String[] event_endtime;
    String[] eventId;
    String[] title;
    boolean[] join;

    eventDetails(Context c, String[] name, String[] startDate, String[] endDate, String[] startTime, String[] endTime, String[] venue, boolean[] joined, String[] eventId, String[] title) {

        super(c, R.layout.rowtemplate, R.id.name, name);
        this.context = c;
        this.event_venue = venue;
        this.event_date = startDate;
        this.event_endDate = endDate;
        this.event_name = name;
        this.event_time = startTime;
        this.event_endtime = endTime;
        this.join = joined;
        this.title = title;
        this.eventId = eventId;

    }


    @Override

    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.rowtemplate, parent, false);
        TextView text = (TextView) row.findViewById(R.id.name);
        TextView venue = (TextView) row.findViewById(R.id.venue);
        TextView time = (TextView) row.findViewById(R.id.time);
        TextView title1 = (TextView) row.findViewById(R.id.title);
        TextView date = (TextView) row.findViewById(R.id.date);
        CheckBox joined = (CheckBox) row.findViewById(R.id.checkBox);
        TextView id = (TextView) row.findViewById(R.id.eventid);
        Button btn = (Button) row.findViewById(R.id.friendButton);
        text.setText(event_name[position]);
        venue.setText(event_venue[position]);
        date.setText(event_date[position] + "to" + event_endDate[position]);
        time.setText(event_time[position] + "to" + event_endtime[position]);
        id.setText(eventId[position]);
        title1.setText(title[position]);
        View button1 = row.findViewById(R.id.friendButton);
        button1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myClickHandler(eventId[position]);
                    }
                }
        );
        if (join[position] == true) {
            joined.setChecked(true);
        } else {
            joined.setChecked(false);
        }

        return row;

    }
    public void myClickHandler(String eventId) {

/*
        Intent intent = new Intent(getContext(), ListFriends.class);
        intent.putExtra("Event_ID", eventId);
        getContext().startActivity(intent);
*/
        }

    }



