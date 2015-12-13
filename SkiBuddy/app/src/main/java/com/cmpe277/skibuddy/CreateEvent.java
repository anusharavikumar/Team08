package com.cmpe277.skibuddy;

/**
 * Created by goudamy on 10/3/2015.
 */

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class CreateEvent extends Fragment{


    String userName;
    public CreateEvent()
    {

    }
    @SuppressLint("ValidFragment")
    CreateEvent(String user){
        this. userName = user;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //final View playlist = inflater.inflate(R.layout.playlist, container, false);
        final View playlist = inflater.inflate(R.layout.eventcreate, container, false);

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
                        String desc = venue_text.getText().toString();
                        EditText title_text = (EditText) playlist.findViewById(R.id.title);
                        String title = venue_text.getText().toString();
                        EditText desc_text = (EditText) playlist.findViewById(R.id.editText2);
                        String venue = desc_text.getText().toString();
                        TextView text = (TextView) playlist.findViewById(R.id.textView8);
                        String input = text.getText().toString();
                        TextView text1 = (TextView) playlist.findViewById(R.id.textView5);
                        String input1 = text1.getText().toString();
                        TextView text2 = (TextView) playlist.findViewById(R.id.textView6);
                        String input2 = text2.getText().toString();
                        TextView text3 = (TextView) playlist.findViewById(R.id.textView7);
                        String input3 = text3.getText().toString();
                        //Toast.makeText(getContext(), input + "-" + input1 + "-" + input2 + "-" + input3+"-"+desc+"-"+venue+"-"+title, Toast.LENGTH_LONG).show();
                        // TO DO: add the values to the database

if(!input2.equals("DD/MM/YYYY") && !input.equals("HH:MM") && !input3.equals("DD/MM/YYYY") && !input1.equals("HH:MM") ) {

    String start = input2 + "T" + input + ":00.000000Z";
    String end = input3 + "T" + input1 + ":00.000000Z";
    String entire = "'user_id'" + ":'" +  userName + "',"  + "'title'" + ":'" + title + "'," + "'start_time'" + ":'" + start + "'," + "'end_time'" + ":'" + end + "'," + "'description'" + ":'" + desc + "'," + "'venue'" + ":'" + venue + "'";

    AsyncHttpClient client = new AsyncHttpClient();
    RequestParams params = new RequestParams();
    try {
        StringEntity entity = new StringEntity("{'data': [{" + entire + "}]}");

        client.post(getContext(), "http://52.90.230.67:8000/createEvent/", entity, "application/json",
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                       Toast.makeText(getContext(),"Event is Successfull Created", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d("Failed", "On failure called");
                    }
                });
    } catch (UnsupportedEncodingException e) {

    }

}else{
    Toast.makeText(getContext(), "Date and Time cannot be Empty", Toast.LENGTH_LONG)
            .show();
}


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
