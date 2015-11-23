package com.cmpe277.skibuddy;

import java.util.ArrayList;

public class Record {
	public String record;
	public String event;

	public Record(String record, String event) {
		this.record = record;
		this.event = event;
	}

	public static ArrayList<Record> getRecords() {
		ArrayList<Record> records = new ArrayList<Record>();
		records.add(new Record("Record 1", "Lahoe Tahoe Ski Event"));
		records.add(new Record("Record 5", "Mount Everest Sking"));
		records.add(new Record("Record 10", "Ski Event - Yosemite"));
		return records;
	}
}
