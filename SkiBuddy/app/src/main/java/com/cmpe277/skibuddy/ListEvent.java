package com.cmpe277.skibuddy;

/**
 * Created by goudamy on 10/3/2015.
 */
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class ListEvent extends Fragment {

    String userName;
    public ListEvent()
    {

    }
    @SuppressLint("ValidFragment")
     ListEvent(String user){
     this. userName = user;
}

    //To Do: use APIs to call all the details of the event and fill in the following array structure
    ListView listview;

    ArrayList<HashMap<String,String>> list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View search = inflater.inflate(R.layout.search_results, container, false);
        Resources res = getResources();
        listview = (ListView) search.findViewById(R.id.listView);



        String entire = "'user_id'" + ":'" + userName + "'";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        final Fragment frag = this;

            try {
                StringEntity entity = new StringEntity("{'data': [{"+ entire+"}]}");

                client.post(getContext(), "http://52.90.230.67:8000/getEvent/", entity, "application/json",
                        new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            try {

                                JSONObject arr = new JSONObject(new String(responseBody));
                                JSONObject jsonObject = arr.getJSONObject("data");

                                JSONArray jArray = jsonObject.getJSONArray("eventId");
                                String[] eventId = jArray.toString().replace("},{", " ,").replace("[","").replace("]","").split(",");


                                jArray = jsonObject.getJSONArray("join");
                                String[] join = jArray.toString().replace("},{", " ,").replace("[","").replace("]","").replace("]","").split(",");

                                jArray = jsonObject.getJSONArray("end_time");
                                String[] end_time = jArray.toString().replace("},{", " ,").replace("[","").replace("]","").split(",");


                                jArray = jsonObject.getJSONArray("start_time");
                                String[] start_time = jArray.toString().replace("},{", " ,").replace("[","").replace("]","").split(",");

                                jArray = jsonObject.getJSONArray("venue");
                                String[] venue = jArray.toString().replace("},{", " ,").replace("[","").replace("]","").split(",");

                                jArray = jsonObject.getJSONArray("title");
                                String[] title = jArray.toString().replace("},{", " ,").replace("[","").replace("]","").split(",");

                                jArray = jsonObject.getJSONArray("description");
                                String[] name = jArray.toString().replace("},{", " ,").replace("[","").replace("]","").split(",");

                                list = new ArrayList<HashMap<String,String>>();

                                for(int i=0;i<eventId.length;i++) {
                                    HashMap<String,String> temp = new HashMap<String,String>();
                                    temp.put("name", name[i]);
                                    temp.put("title", title[i]);
                                    temp.put("venue", venue[i]);
                                    temp.put("startTime", start_time[i]);
                                    temp.put("endTime", end_time[i]);
                                    temp.put("eventId",eventId[i]);
                                    temp.put("join", join[i]);
                                    list.add(temp);
                                }
                                eventDetails adapter = new eventDetails(frag,list,userName);
                                listview.setAdapter(adapter);
                                }



                             catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            Log.d("Failed", "On failure called");
                        }
                    });
        } catch (UnsupportedEncodingException e) {

        }

        return search;

    }

}

class eventDetails extends BaseAdapter {
    public ArrayList<HashMap<String,String>> list;
    Fragment fragment;
    String userName;
    Context context;

    public eventDetails( Fragment fragment,ArrayList<HashMap<String,String>> list,String userName) {
        super();
this.fragment = fragment;
        this.list = list;
        this.userName = userName;
    }


    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int position) {

        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) fragment.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.rowtemplate, parent, false);
        TextView text = (TextView) row.findViewById(R.id.name);
        TextView venue = (TextView) row.findViewById(R.id.venue);
        TextView time = (TextView) row.findViewById(R.id.time);
        TextView title1 = (TextView) row.findViewById(R.id.title);
        TextView date = (TextView) row.findViewById(R.id.date);
        final CheckBox joined = (CheckBox) row.findViewById(R.id.checkBox);
        TextView id = (TextView) row.findViewById(R.id.eventid);
        Button btn = (Button) row.findViewById(R.id.friendButton);
        View button1 = row.findViewById(R.id.friendButton);

        final HashMap<String,String> map = list.get(position);


        text.setText( map.get("name").replace("\"", ""));

        venue.setText(map.get("venue").replace("\"", ""));
        String[] start = map.get("startTime").replace("\"", "").split("T");
        String[] end = map.get("endTime").replace("\"", "").split("T");
        date.setText(start[0]+"to"+ end[0]);
        if(start.length > 1 && end.length > 1) {
            time.setText(start[1] + "to" + end[1]);
        }
        id.setText(map.get("eventId").replace("\"", ""));
        title1.setText(map.get("title").replace("\"", ""));
        button1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myClickHandler(map.get("eventId").replace("\"", ""));
                    }
                }
        );

        joined.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (((CheckBox) joined).isChecked()) {
                            String entire = "'user_id'" + ":'" + userName + "',"+"'event_id'" + ":" + map.get("eventId") ;

                            AsyncHttpClient client = new AsyncHttpClient();
                            RequestParams params = new RequestParams();
                            try {
                                StringEntity entity = new StringEntity("{'data': [{"+ entire+"}]}");

                                client.post(context, "http://52.90.230.67:8000/joinEvent/", entity, "application/json",
                                        new AsyncHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                Log.d("Success", "On success called");

                                            }

                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                                Log.d("Failed", "On failure called");
                                            }
                                        });
                            }

                            catch(UnsupportedEncodingException e) {

                            }


                        } else {

                            String entire = "'user_id'" + ":'" + userName + "',"+"'event_id'" + ":" + map.get("eventId") ;

                            AsyncHttpClient client = new AsyncHttpClient();
                            RequestParams params = new RequestParams();
                            try {
                                StringEntity entity = new StringEntity("{'data': [{"+ entire+"}]}");

                                client.post(context, "http://52.90.230.67:8000/unjoinEvent/", entity, "application/json",
                                        new AsyncHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                Log.d("Success", "On success called");
                                            }

                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                                Log.d("Failed", "On failure called");
                                            }
                                        });
                            }

                            catch(UnsupportedEncodingException e) {

                            }


                        }

                    }
                }
        );

        if (map.get("join").equals("true") ){
           joined.setChecked(true);
        } else {
            joined.setChecked(false);
        }

        return row;

    }
    public void myClickHandler(String eventId) {


        Intent intent = new Intent(fragment.getContext(), ListFriends.class);
        intent.putExtra("Event_ID", eventId);
        fragment.getContext().startActivity(intent);

    }

}

