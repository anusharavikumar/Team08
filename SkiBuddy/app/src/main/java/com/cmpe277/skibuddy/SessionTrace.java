package com.cmpe277.skibuddy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SessionTrace extends AppCompatActivity {//FragmentActivity {

    private GoogleMap mMap;
    JSONArray trace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_trace);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            trace= new JSONArray();
        } else {
            try {
                trace = new JSONArray(extras.getString("SkiDetails"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        mMap.setMyLocationEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.3339136, -121.8819874), 16.0f));
        PolylineOptions skiPath2=new PolylineOptions().geodesic(true);
        try {
            {for(int i = 0; i < trace.length(); i++) {

                JSONObject obj = (JSONObject) trace.get(i);
                skiPath2.add(new LatLng((Double)obj.get("latitude"),(Double)obj.get("longitude")));

            }
        }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        /*skiPath2=skiPath2.add(new LatLng(37.3339136,-121.8819874));
        skiPath2=skiPath2.add(new LatLng(37.3349136,-121.8829874));
        skiPath2=skiPath2.add(new LatLng(37.3359136,-121.8839874));*/

        mMap.addPolyline(skiPath2);

    }

}
