package com.cmpe277.skibuddy;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by goudamy on 11/21/2015.
 */
public class DateHandler extends DialogFragment  implements DatePickerDialog.OnDateSetListener {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
       // DateSetting dateSetting=new DateSetting(getActivity());
        Calendar calendar= Calendar.getInstance();
        int year= calendar.get(calendar.YEAR);
        int month=calendar.get(calendar.MONTH);
        int day=calendar.get(calendar.DAY_OF_MONTH);

        DatePickerDialog dialog;
        dialog=new DatePickerDialog(getActivity(),this,year,month,day);
        return dialog;
    }
    @Override
    public void onDateSet(DatePicker view, int year, int dateSetting, int dayOfMonth) {
        Context context;
        String val = dateSetting+":"+(dayOfMonth+1)+":"+year;
        TextView tv = (TextView) getActivity().findViewById(R.id.textView6);
        tv.setText(val);


    }
}
