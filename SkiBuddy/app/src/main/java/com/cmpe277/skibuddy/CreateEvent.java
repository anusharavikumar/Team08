package com.cmpe277.skibuddy;

/**
 * Created by goudamy on 10/3/2015.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateEvent extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View playlist = inflater.inflate(R.layout.eventcreate, container, false);
        //playlist.setBackgroundColor(Color.CYAN);

       View button = playlist.findViewById(R.id.button);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setDate(v);
                    }
                }
        );
       View button1 = playlist.findViewById(R.id.button1);
        button1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setTime(v);
                    }
                }
        );

       View button2 = playlist.findViewById(R.id.button3);
        button2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setEndDate(v);
                    }
                }
        );
  View button3 =playlist.findViewById(R.id.button4);
        button3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setEndTime(v);
                    }
                }
        );
        View button5 =playlist.findViewById(R.id.button5);
        button5.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText venue_text = (EditText) playlist.findViewById(R.id.editText);
                        String venue = venue_text.getText().toString();
                        EditText title_text = (EditText) playlist.findViewById(R.id.title);
                        String title = venue_text.getText().toString();
                        EditText desc_text = (EditText) playlist.findViewById(R.id.editText2);
                        String desc = desc_text.getText().toString();
                        TextView text = (TextView) playlist.findViewById(R.id.textView8);
                        String input = text.getText().toString();
                        TextView text1 = (TextView) playlist.findViewById(R.id.textView5);
                        String input1 = text1.getText().toString();
                        TextView text2 = (TextView) playlist.findViewById(R.id.textView6);
                        String input2 = text2.getText().toString();
                        TextView text3 = (TextView) playlist.findViewById(R.id.textView7);
                        String input3 = text3.getText().toString();
                        Toast.makeText(getContext(), input + "-" + input1 + "-" + input2 + "-" + input3+"-"+desc+"-"+venue+"-"+title, Toast.LENGTH_LONG).show();
                        // TO DO: add the values to the database
                    }
                }
        );
        return playlist;
    }





    public void setDate(View view){

        DateHandler pickerDialogs=new DateHandler();
        pickerDialogs.show(getActivity().getFragmentManager(), "date_picker");
    }
    public void setEndDate(View view){
        DateSetting pickerDialogs2 = new DateSetting();

        pickerDialogs2.show(getActivity().getFragmentManager(), "date_picker1");
    }
    // Context context;
    public void setTime(View view){

        TimeHandler pickerDialogs1=new TimeHandler();

        pickerDialogs1.show(getActivity().getFragmentManager(), "time_picker");


    }
    public void setEndTime(View view){

        TimeSetting pickerDialogs1=new TimeSetting();

        pickerDialogs1.show(getActivity().getFragmentManager(), "time_picker1");


    }




}
