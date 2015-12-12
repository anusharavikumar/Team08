package com.cmpe277.skibuddy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Record {
	public String record;
	public String event;
	public String distance;
	public String time;

	public Record(String record, String event) {
		this.record = record;
		this.event = event;
	}

	public static ArrayList<Record> getRecords(JSONArray data) throws JSONException {
		ArrayList<Record> records = new ArrayList<Record>();

		for(int i = 0; i < data.length(); i++) {

			JSONObject obj = (JSONObject) data.get(i);
            records.add(new Record(obj.get("session_id").toString(),obj.get("title").toString()));

		}
		//records.add(new Record("Record 1", "Lahoe Tahoe Ski Event"));
		//records.add(new Record("Record 5", "Mount Everest Sking"));
		//records.add(new Record("Record 10", "Ski Event - Yosemite"));
		return records;
	}
}
