package com.cmpe277.skibuddy.helpers;

import android.content.Context;
import android.os.Looper;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.cmpe277.skibuddy.Record;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

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

    /*public ArrayList<Record> getAllSkiRecordsForUser( String userId, String playerId, Context context ) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        try {
            StringEntity entity = new StringEntity("{'data': [{'user_id':'rajini', 'player_id':'purvi'}]}");

            client.post(context, "http://52.91.8.130:8000/getSkirecords/", entity, "application/json",
                    new AsyncHttpResponseHandler(Looper.getMainLooper()) {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            // status has to be 200
                            Log.d("Response", "" + responseBody);
                            try {
                                JSONObject arr = new JSONObject(new String(responseBody));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                                error) {
                            Log.d("Failed", "On failure called");
                        }
                    });
        }

        catch(UnsupportedEncodingException e) {

        }
        ArrayList<Record> arrayOfRecords = Record.getRecords();
        return arrayOfRecords;
    }*/

    public Record getSkiRecordDetailForUser( String recordID ,Context context) {
        /*AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        try {
            StringEntity entity = new StringEntity("{'data': [{'user_id':'shm', 'CurrentLocation': {'latitude':37.5771021,'longitude':-122.0445751,'mVersionCode':'1'}}]}");

            client.post(context, "http://52.91.8.130:8000/updateLocation/", entity, "application/json",
                    new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }


                    });
        }

        catch(UnsupportedEncodingException e) {

        }*/

        Record r = new Record("Record 1", "Lahoe Tahoe Ski Event");
        r.time = "5 minutes";
        r.distance = "0.5 miles";
        return r;

    }
}
