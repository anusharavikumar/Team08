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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private PolylineOptions skiPath;
    List<LatLng> pointList;
    String flag="";
    private Handler handler = new Handler();
    private LatLng currentLocation;
    private boolean locationSet=false;
    private SessionDetails session;
    Calendar cal ;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    public void setCurrentLocation(LatLng location)
    {
        locationSet=true;
        currentLocation=location;
    }
    public LatLng getCurrentLocation()
    {
        return currentLocation;
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
        if(!flag.equals("start")) {
            flag = "start";
            pointList = new ArrayList<LatLng>();
            session = new SessionDetails();
            cal=Calendar.getInstance();
            session.Session_start = sdf.format(cal.getTime());
        }
    }
    public void onStop(View view)
    {
        if(!flag.equals("stop")) {
            flag = "stop";
            cal = Calendar.getInstance();
            session.Session_end = sdf.format(cal.getTime());
            session.Session_Data = pointList;
            session.distance=distCovered(pointList.get(0).latitude, pointList.get(0).longitude, pointList.get(pointList.size() - 1).latitude, pointList.get(pointList.size() - 1).longitude);
            sendInformationToServer(session);

        }
    }

    public void sendInformationToServer(SessionDetails s)
    {
        Gson gson = new Gson();
        String json = gson.toJson(s);

        // Implement code to send it to server
        System.out.println("Info sent to server: "+ json);

    }

    @Override
    protected void onResume() {
        super.onResume();
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
        skiPath= new PolylineOptions().geodesic(true);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(myLocationChangeListener);

        //addPeopleOnMap(new LatLng(37.560000, -122.044999), "Dhanu");37.3359136,"longitude":-121.8849874,
        addPeopleOnMap(new LatLng(37.3339136,-121.8819874), "Anusha");
        addPeopleOnMap(new LatLng(37.3359136,-121.8839874), "Dhanu");
        addPeopleOnMap(new LatLng(37.337136,-121.8845874), "Purvi");
        addPeopleOnMap(new LatLng(37.3359136,-121.8879874), "Goudamy");
        addPeopleOnMap(new LatLng(37.3351136,-121.8899874), "Aastha");
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            setCurrentLocation(loc);

            Log.d("Current Loc string",new Gson().toJson(loc));

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
        }
    };

    public static double distCovered(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75;  //this is in miles I believe
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        return dist;
    }

    private Runnable updateServerWithCurrentLocation = new Runnable()
    {
        public void run()
        {
            if(locationSet)
            {
                //Inform server about current location
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("CurrentLocation", new Gson().toJson(getCurrentLocation()));
                client.get("http://localhost:8080", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        // status has to be 200
                        Log.d("Response", "" + responseBody);

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
                   //Call addPeopleOnMap()
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

    public void addPeopleOnMap(LatLng latLng, String title){
        mMap.addMarker(new MarkerOptions().position(latLng).title(title).icon(BitmapDescriptorFactory.fromResource(R.drawable.m2))
                .anchor(0.0f, 1.0f)); // Anchors the marker on the bottom left);
    }

   /* @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putSerializable("PointList", (Serializable) pointList);
        outState.putString("Flag", flag);
        outState.putSerializable("Session", (Serializable) session);
        Log.d("Map","onSaveInstanceState called");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("Map", "onRestoreInstanceState called");
        pointList=(List<LatLng>)savedInstanceState.getSerializable("PointList");
        flag=savedInstanceState.getString("Flag");
        session=(SessionDetails)savedInstanceState.getSerializable("Session");
    } */
}

