package com.lazybone.trips.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseAccessObject {

	private SQLiteDatabase mDB = null;
	private DBOpenHelper mDbHelper;
	
	private String NTRIP_NAME = "test01";
	private Integer NTRIP_TIME = 1000;
	private String NTRIP_METHOD = "bike";
	
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
	}
	
	public void insertTrips(String tripName, String tripMethod) {
		ContentValues values = new ContentValues();

		values.put(DBOpenHelper.TRIP_NAME, NTRIP_NAME);
		values.put(DBOpenHelper.TRIP_TIME, NTRIP_TIME);
		values.put(DBOpenHelper.TRIP_METHOD,NTRIP_METHOD);
		mDB.insert(DBOpenHelper.TABLE_TRIPS, null, values);
	}
	
	public void insertLocations(String location, String name, double lat, double lon) {
		ContentValues values = new ContentValues();
		
		values.put(DBOpenHelper.LOCATION_ADDRESS, location);
		values.put(DBOpenHelper.LOCATION_NAME, name);
		values.put(DBOpenHelper.LOCATION_TYPE, NLOCATION_TYPE);
		values.put(DBOpenHelper.LOCATION_NOTES, NLOCATION_NOTES);
		
		mDB.insert(DBOpenHelper.TABLE_LOCATIONS, null, values);
	}
	
	public void deleteLocation(int id) {
		mDB.delete(DBOpenHelper.TABLE_LOCATIONS, "_id=?", new String[] {"" + id});
	}
	
	@SuppressWarnings("unused")
	private void insertRoutes(){
		ContentValues values = new ContentValues();

		values.put(DBOpenHelper.TRIP_ID, NTRIP_ID);
		values.put(DBOpenHelper.ROUTE_FROM, NFROM_LOCATION_ID);
		values.put(DBOpenHelper.ROUTE_TO, NTO_LOCATION_ID);
		values.put(DBOpenHelper.ROUTE_METHOD, NROUTE_METHOD);
		
		mDB.insert(DBOpenHelper.TABLE_ROUTES, null, values);
	}
	
	@SuppressWarnings("unused")
	private void insertManyToMany() {
		ContentValues values = new ContentValues();
		
		values.put(DBOpenHelper.TRIP_ID, NTRIP_ID);
		values.put(DBOpenHelper.LOCATION_ID, NLOCATION_ID);
		
		mDB.insert(DBOpenHelper.TABLE_MANY_TO_MANY, null, values);
	}

	// Returns all artist records in the database
	public Cursor readAddress() {
		
		// public Cursor query (String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) 
		
		return mDB.query(DBOpenHelper.TABLE_LOCATIONS,
				DBOpenHelper.location_columns, null, new String[] {}, null, null,
				null);
	}

	// Delete all records
	public void clearAll() {
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

