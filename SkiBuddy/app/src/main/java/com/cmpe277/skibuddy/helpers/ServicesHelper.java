package com.cmpe277.skibuddy.helpers;

import android.util.Log;

import com.cmpe277.skibuddy.Record;

import java.util.ArrayList;

public class ServicesHelper {

    private static ServicesHelper shared = null;

    public static ServicesHelper shared() {
        if ( shared == null ) {
            synchronized ( ServicesHelper.class ) {
                if ( shared == null ) {
                    try {
                        shared = new ServicesHelper();
                    } catch ( Exception exception ) {
                        Log.e( "ServicesHelper", "Unable to initalize.", exception);
                    }
                }
            }
        }

        return shared;
    }

    public ArrayList<Record> getAllSkiRecordsForUser( String userId ) {
        //TODO : Change this code to get the records from the service
        ArrayList<Record> arrayOfRecords = Record.getRecords();
        return arrayOfRecords;
    }

    public Record getSkiRecordDetailForUser( String recordID ) {
        //TODO : Change this code to get the records from the service
        Record r = new Record("Record 1", "Lahoe Tahoe Ski Event");
        r.time = "5 minutes";
        r.distance = "0.5 miles";
        return r;

    }
}
