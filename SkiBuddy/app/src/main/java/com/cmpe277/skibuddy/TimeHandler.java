package com.cmpe277.skibuddy;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by goudamy on 11/21/2015.
 */


/**
 * Created by goudamy on 11/21/2015.
 */
public class TimeHandler extends DialogFragment implements TimePickerDialog.OnTimeSetListener  {

String val;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //TimeSetting timeSetting=new TimeSetting(getActivity());
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        boolean twentyFour = true;
       // TimePickerDialog dialog;
       // dialog=new TimePickerDialog(getActivity(),timeSetting,hour,minute,twentyFour);


        //return dialog;
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));


    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int min) {
        Context context;

         val = hour +":"+ min;
        Toast.makeText(this.getContext(), "selected time" + hour + ":" + min, Toast.LENGTH_LONG).show();

        TextView tv = (TextView) getActivity().findViewById(R.id.textView8);
        //Set a message for user
        tv.setText(val);
    }




}


