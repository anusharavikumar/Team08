package com.cmpe277.skibuddy;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private PolylineOptions skiPath;
    List<LatLng> pointList=new ArrayList<LatLng>();
    String flag="";
    private Handler handler = new Handler();
    private LatLng currentLocation;
    private boolean locationSet=false;

    public void setCurrentLocation(LatLng location)
    {
        locationSet=true;
        currentLocation=location;
    }
    public String getCurrentLocation()
    {
        return currentLocation.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        //Uncomment this when integrated with server
        //updateServerWithCurrentLocation.run();
        //getOtherUsersLocation.run();

    }

    public void onStart(View view)
    {
        flag="start";
    }
    public void onStop(View view)
    {
        flag="stop";
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }


   /* @Override
    public void onMapReady(GoogleMap map) {
        LatLng sydney = new LatLng(-33.867, 151.206);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        map.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney));
    }*/

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        skiPath= new PolylineOptions().geodesic(true);
        //mMap.setMyLocationEnabled(true);
       /* LatLng sydney = new LatLng(-33.867, 151.206);

        //mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        mMap.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney)); */

      /*  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(-18.142, 178.431), 2));

        // Polylines are useful for marking paths and routes on the map.
        mMap.addPolyline(new PolylineOptions().geodesic(true)
                .add(new LatLng(-33.866, 151.195))  // Sydney
                .add(new LatLng(-18.142, 178.431))  // Fiji
                .add(new LatLng(21.291, -157.821))  // Hawaii
                .add(new LatLng(37.423, -122.091))  // Mountain View

        ); */

        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }*/
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(myLocationChangeListener);
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            setCurrentLocation(loc);

            Log.d("Current Loc string",getCurrentLocation());

            if(flag.equals("start"))
            {
                pointList.add(loc);
                Log.d("Location added", loc.toString());
            }
            else if (flag.equals("stop"))
            {
                // call server method
                Log.d("Stopped", "Stopped");
                showPolyLine();
            }
            // Marker mMarker = mMap.addMarker(new MarkerOptions().position(loc));
            if(mMap != null){
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            }
            //skiPath.add(loc);
            /*mMap.addPolyline(skiPath.add(new LatLng(-33.866, 151.195))  // Sydney
                   .add(new LatLng(-18.142, 178.431))  // Fiji
                    .add(new LatLng(21.291, -157.821))  // Hawaii
                           .add(new LatLng(37.423, -122.091)) ); // Mountain View */

           /*imp List<LatLng> points= skiPath.getPoints();
            points.add(loc);
            //skiPath.setPoints(points);

            mMap.addPolyline(skiPath.add(loc) ); */


            //mMap.addPolyline(skiPath.add(loc))

        }
    };

    private Runnable updateServerWithCurrentLocation = new Runnable()
    {
        public void run()
        {
            if(locationSet)
            {
                //Inform server about current location
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("CurrentLocation", getCurrentLocation());

                client.get("http://localhost:8080",params ,new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        // status has to be 200
                        Log.d("Response", ""+responseBody);

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d("Failed", "On failuer called");
                    }

                });
            }
            Log.d("Thread", "Excecuted");
            handler.postDelayed(this, 20000); // Thread running after 20 sec
        }
    };

    private Runnable getOtherUsersLocation = new Runnable()
    {
        public void run()
        {

            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("CurrentLocation", getCurrentLocation());

            client.get("http://localhost:8080",params ,new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    // status has to be 200
                    Log.d("Response", ""+responseBody);

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("Failed", "On failuer called");
                }
            });

            Log.d("Thread 2", "Excecuted");
            handler.postDelayed(this, 20000); // Thread running after 20 sec
        }
    };

    private void showPolyLine() {
        PolylineOptions skiPath2=new PolylineOptions().geodesic(true);
        for(LatLng point: pointList){
            skiPath2=skiPath2.add(point);
        }
        mMap.addPolyline(skiPath2);
        Log.d("Polyline","drawn");
    }


}

