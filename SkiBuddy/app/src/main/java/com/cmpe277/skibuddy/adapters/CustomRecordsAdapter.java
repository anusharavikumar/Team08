package com.cmpe277.skibuddy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ListView;

import com.cmpe277.skibuddy.R;
import com.cmpe277.skibuddy.Record;

import java.util.ArrayList;

public class CustomRecordsAdapter extends ArrayAdapter<Record> {
    public CustomRecordsAdapter(Context context, ArrayList<Record> records) {
        super(context, 0, records);
     }

     @Override
     public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Record record = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
           convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_ski, parent, false);
        }
        // Lookup view for data population
        TextView tvRecordName = (TextView) convertView.findViewById(R.id.tvRecordName);
        TextView tvEvent = (TextView) convertView.findViewById(R.id.tvEvent);
        // Populate the data into the template view using the data object
        tvRecordName.setText(record.record);
        tvEvent.setText(record.event);
        // Return the completed view to render on screen
        return convertView;
    }


}
