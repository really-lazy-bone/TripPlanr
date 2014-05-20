package com.lazybone.trips.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseAccessObject {

	private SQLiteDatabase mDB = null;
	private DBOpenHelper mDbHelper;
	
	private String NTRIP_NAME = "test01";
	private Integer NTRIP_TIME = 1000;
	private String NTRIP_METHOD = "bike";
	
	private String NLOCATION_ADDRESS = "5151 State University Dr, Los Angeles, CA 90032";
	private String NLOCATION_NAME = "Cal State LA";
	private String NLOCATION_TYPE = "school";
	private String NLOCATION_NOTES = "test notes";
	
	private Integer NTRIP_ID = 1;
	private Integer NLOCATION_ID = 1;
	private Integer NFROM_LOCATION_ID = 1;
	private Integer NTO_LOCATION_ID = 1;
	private String NROUTE_METHOD = "driving";
	

	public DatabaseAccessObject(Context activity) {
		// Create a new DatabaseHelper
		mDbHelper = new DBOpenHelper(activity);

		// Get the underlying database for writing
		mDB = mDbHelper.getWritableDatabase();
		
		insertTrips();
		insertLocations();
		insertRoutes();
		insertManyToMany();
	}
	
	// Insert several artist records
	private void insertTrips() {

		ContentValues values = new ContentValues();

		values.put(DBOpenHelper.TRIP_NAME, NTRIP_NAME);
		values.put(DBOpenHelper.TRIP_TIME, NTRIP_TIME);
		values.put(DBOpenHelper.TRIP_METHOD,NTRIP_METHOD);
		mDB.insert(DBOpenHelper.TABLE_TRIPS, null, values);

	}
	
	private void insertLocations() {

		ContentValues values = new ContentValues();
		
		values.put(DBOpenHelper.LOCATION_ADDRESS, NLOCATION_ADDRESS);
		values.put(DBOpenHelper.LOCATION_NAME, NLOCATION_NAME);
		values.put(DBOpenHelper.LOCATION_TYPE, NLOCATION_TYPE);
		values.put(DBOpenHelper.LOCATION_NOTES, NLOCATION_NOTES);
		mDB.insert(DBOpenHelper.TABLE_LOCATIONS, null, values);

	}
	
	private void insertRoutes(){
		ContentValues values = new ContentValues();

		values.put(DBOpenHelper.TRIP_ID, NTRIP_ID);
		values.put(DBOpenHelper.ROUTE_FROM, NFROM_LOCATION_ID);
		values.put(DBOpenHelper.ROUTE_TO, NTO_LOCATION_ID);
		values.put(DBOpenHelper.ROUTE_METHOD, NROUTE_METHOD);
		
		mDB.insert(DBOpenHelper.TABLE_ROUTES, null, values);

	}
	
	private void insertManyToMany() {
		ContentValues values = new ContentValues();
		
		values.put(DBOpenHelper.TRIP_ID, NTRIP_ID);
		values.put(DBOpenHelper.LOCATION_ID, NLOCATION_ID);
		
		mDB.insert(DBOpenHelper.TABLE_MANY_TO_MANY, null, values);
		
	}

	// Returns all artist records in the database
	public Cursor readAddress() {
		
		// public Cursor query (String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) 
		
		Log.i("TEST", 	DBOpenHelper.location_columns[0]);
		Log.i("TEST", 	DBOpenHelper.location_columns[1]);
		
		return mDB.query(DBOpenHelper.TABLE_LOCATIONS,
				DBOpenHelper.location_columns, null, new String[] {}, null, null,
				null);
	}

	// Modify the contents of the database
	/*private void fix() {

		// Sorry Lady Gaga :-(
		mDB.delete(DatabaseOpenHelper.TABLE_NAME,
				DatabaseOpenHelper.ARTIST_NAME + "=?",
				new String[] { "Lady Gaga" });

		// fix the Man in Black
		ContentValues values = new ContentValues();
		values.put(DatabaseOpenHelper.ARTIST_NAME, "Johnny Cash");

		mDB.update(DatabaseOpenHelper.TABLE_NAME, values,
				DatabaseOpenHelper.ARTIST_NAME + "=?",
				new String[] { "Jawny Cash" });

	}*/

	// Delete all records
	private void clearAll() {

		mDB.delete(DBOpenHelper.TABLE_TRIPS, null, null);
		mDB.delete(DBOpenHelper.TABLE_LOCATIONS, null, null);
		mDB.delete(DBOpenHelper.TABLE_ROUTES, null, null);
		mDB.delete(DBOpenHelper.TABLE_MANY_TO_MANY, null, null);

	}

	protected void onDestroy() {

		mDB.close();
		mDbHelper.deleteDatabase();

	}
}

