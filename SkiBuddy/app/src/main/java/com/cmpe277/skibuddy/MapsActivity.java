package com.cmpe277.skibuddy;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

class OtherPeople{
    public LatLng location;
    public String name;
}
public class MapsActivity extends AppCompatActivity {

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
    List<Marker> currentMarkers=new ArrayList<Marker>();
    String eventId;
    String url="http://52.90.230.67:8000";
    Button startB, stopB;
    Boolean flagB;


    public void resetCurrentMarkers() {
        this.currentMarkers.clear();
    }

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
        //eventId = intent.getExtras("EventId");
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
           eventId= null;
        } else {
            eventId = extras.getString("EventId");
            flagB= extras.getBoolean("Valid");
        }

        System.out.println("Event id received: "+eventId);
        System.out.println("Flag received: "+flagB);
        startB=(Button)findViewById(R.id.buttonStart);
        stopB=(Button)findViewById(R.id.buttonStop);
        stopB.setEnabled(false);
        setUpMapIfNeeded();

        //Uncomment this when integrated with server
        updateServerWithCurrentLocation.run();
        getOtherUsersLocation.run();
    }

    public void onStart(View view)
    {

        if(!flag.equals("start")) {
            startB.setEnabled(false);
            stopB.setEnabled(true);

            flag = "start";
            pointList = new ArrayList<LatLng>();
            session = new SessionDetails();
            cal=Calendar.getInstance();
            session.start_time = sdf.format(cal.getTime());
        }
    }

    public void onStop(View view)
    {
        if(!flag.equals("stop")) {
            startB.setEnabled(true);
            stopB.setEnabled(false);
            flag = "stop";
            cal = Calendar.getInstance();
            session.Session_name=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
            if(eventId!=null)
            session.Event_id=eventId;
            session.User_id=MainActivity.getUserEmail();
            session.end_time = sdf.format(cal.getTime());
            session.Session_Data = pointList;
            session.distance=distCovered(pointList.get(0).latitude, pointList.get(0).longitude, pointList.get(pointList.size() - 1).latitude, pointList.get(pointList.size() - 1).longitude);
            if(flagB)
            sendInformationToServer(session);
            else
            {
                Toast.makeText(getApplicationContext(), "Information Not stored : Event is not active ", Toast.LENGTH_LONG).show();

            }

        }
    }

    public void sendInformationToServer(SessionDetails s)
    {
        Gson gson = new Gson();
        String json = gson.toJson(s);

        AsyncHttpClient client = new AsyncHttpClient();
        try {
            StringEntity entity = new StringEntity("{'data': ["+json+"]}");
            client.post(getApplicationContext(), url+"/endSession/", entity, "application/json",
                    new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d("Response received", "" + response);
                        }
                    });
        }
        catch(UnsupportedEncodingException e) {

        }
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


        //addPeopleOnMap(new LatLng(37.5771881,-122.0446438), "Dhanu");//37.3359136,"longitude":-121.8849874,
        //addPeopleOnMap(new LatLng(37.5393062,-122.2560391), "Anusha");
        /*addPeopleOnMap(new LatLng(37.3359136,-121.8839874), "Dhanu");
        addPeopleOnMap(new LatLng(37.337136,-121.8845874), "Purvi");
        addPeopleOnMap(new LatLng(37.3359136,-121.8879874), "Goudamy");
        addPeopleOnMap(new LatLng(37.3351136,-121.8899874), "Aastha"); */
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
            if(checkLocationIsOn()){

            if(locationSet)
            {
            //Inform server about current location
                AsyncHttpClient client = new AsyncHttpClient();
                try {
                    StringEntity entity = new StringEntity("{'data': [{'user_id':'"+MainActivity.getUserEmail()+"', 'CurrentLocation': "+new Gson().toJson(getCurrentLocation())+"}]}");
                    client.post(getApplicationContext(), url+"/updateLocation/", entity, "application/json",
                            new JsonHttpResponseHandler(){
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    Log.d("Response", "" + response);
                                }
                            });
                }
                catch(UnsupportedEncodingException e) {

                }
            }}
            Log.d("Thread", "Excecuted");
            handler.postDelayed(this, 20000); // Thread running after 20 sec
        }
    };

    private Runnable getOtherUsersLocation = new Runnable()
    {
        public void run()
        {
            if(checkLocationIsOn()){
            AsyncHttpClient client = new AsyncHttpClient();
            try {
                StringEntity entity = null;

                client.get(getApplicationContext(), url + "/getUsersInfo/", entity, "application/json",
                        new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                ArrayList<OtherPeople> others=new ArrayList<OtherPeople>();

                                try {
                                    if(response.has("Response")) {
                                        Log.d("Response", "" + response.getString("Response"));
                                        JSONArray array = response.getJSONArray("Response");
                                        for (int i = 0; i < array.length(); i++) {
                                            if (array.getJSONObject(i).has("user_location") && array.getJSONObject(i).has("user_name")) {
                                                OtherPeople obj = new OtherPeople();
                                                obj.location = new LatLng(array.getJSONObject(i).getJSONObject("user_location").getDouble("latitude"), array.getJSONObject(i).getJSONObject("user_location").getDouble("longitude"));
                                                obj.name = array.getJSONObject(i).getString("user_name");
                                                if(!obj.name.equals(MainActivity.getname()))
                                                others.add(obj);
                                                else
                                                System.out.println("Same user details not included");

                                                System.out.println(array.getJSONObject(i).getJSONObject("user_location").getDouble("latitude"));
                                            }
                                        }
                                        addOtherPeople(others);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
            catch(Exception e) {

            }}
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

    public void addOtherPeople(ArrayList<OtherPeople> others)
    {
        clearMarkers();
        resetCurrentMarkers();
        Log.d("CurrentMarker",""+currentMarkers);
        for(OtherPeople o:others){
            addPeopleOnMap(o.location,o.name);
        }

    }
    public void addPeopleOnMap(LatLng latLng, String title) {

        Marker mr = mMap.addMarker(new MarkerOptions().position(latLng).title(title).icon(BitmapDescriptorFactory.fromResource(R.drawable.m2))
                .anchor(0.0f, 1.0f)); // Anchors the marker on the bottom left);
        currentMarkers.add(mr);
        System.out.println("Marker"+mr);
        System.out.println("CurrentMarker2"+currentMarkers);
        if(currentMarkers==null)
        {
            System.out.println("Yes null");
        }
        //currentMarkers.add(mr);
    }
    public void clearMarkers()
    {
        System.out.println(" Clear markers called");

        if(currentMarkers!=null) {
            if (currentMarkers.size() > 0)
                System.out.println(" Removing markers ");
                for (Marker marks : currentMarkers) {
                    marks.remove();
                }
        }
    }
public boolean checkLocationIsOn(){


    LocationManager lm = (LocationManager)this.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
    boolean gps_enabled = false;
    boolean network_enabled = false;

    try {
        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    } catch(Exception ex) {}

    try {
        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    } catch(Exception ex) {}

    if(!gps_enabled && !network_enabled) {
        Toast.makeText(getApplicationContext(), "Please turn on the location services! ", Toast.LENGTH_LONG)
                .show();
        return false;

    }
    return true;
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

