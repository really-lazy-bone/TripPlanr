package com.lazybone.trips.sqlite;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseAccessObject {

	private SQLiteDatabase mDB = null;
	private DBOpenHelper mDbHelper;

	private Integer NTRIP_TIME = 1000;

	private String NLOCATION_TYPE = "school";
	private String NLOCATION_NOTES = "test notes";

	public DatabaseAccessObject(Context activity) {
		// Create a new DatabaseHelper
		mDbHelper = new DBOpenHelper(activity);

		// Get the underlying database for writing
		mDB = mDbHelper.getWritableDatabase();
	}

	public long insertTrips(String tripName, String tripMethod,
			List<Integer> locationIds) {
		ContentValues values = new ContentValues();

		values.put(DBOpenHelper.TRIP_NAME, tripName);
		values.put(DBOpenHelper.TRIP_TIME, NTRIP_TIME);

		values.put(DBOpenHelper.TRIP_METHOD, tripMethod);

		long tripId = mDB.insert(DBOpenHelper.TABLE_TRIPS, null, values);

		// insert relationship trip to location one by one
		for (Integer locationId : locationIds) {
			insertTripLocation(tripId, locationId);
		}

		// insert route one by one
		for (int i = 0; i < locationIds.size(); i++) {
			if (i != locationIds.size() - 1) {
				insertRoute(tripId, locationIds.get(i), locationIds.get(i + 1),
						tripMethod);
			}
		}

		return tripId;
	}

	public void insertLocations(String location, String name, double lat,
			double lon) {
		ContentValues values = new ContentValues();

		values.put(DBOpenHelper.LOCATION_ADDRESS, location);
		values.put(DBOpenHelper.LOCATION_NAME, name);
		values.put(DBOpenHelper.LOCATION_TYPE, NLOCATION_TYPE);
		values.put(DBOpenHelper.LOCATION_NOTES, NLOCATION_NOTES);

		mDB.insert(DBOpenHelper.TABLE_LOCATIONS, null, values);
	}

	public void deleteLocation(int id) {
		mDB.delete(DBOpenHelper.TABLE_LOCATIONS, "_id=?", new String[] { ""
				+ id });
	}

	private void insertRoute(long tripId, int fromLocationId, int toLocationId,
			String travelMethod) {
		ContentValues values = new ContentValues();

		values.put(DBOpenHelper.TRIP_ID, tripId);
		values.put(DBOpenHelper.ROUTE_FROM, fromLocationId);
		values.put(DBOpenHelper.ROUTE_TO, toLocationId);
		values.put(DBOpenHelper.ROUTE_METHOD, travelMethod);

		mDB.insert(DBOpenHelper.TABLE_ROUTES, null, values);
	}

	private void insertTripLocation(long tripId, int locationId) {
		ContentValues values = new ContentValues();

		values.put(DBOpenHelper.TRIP_ID, tripId);
		values.put(DBOpenHelper.LOCATION_ID, locationId);

		mDB.insert(DBOpenHelper.TABLE_MANY_TO_MANY, null, values);
	}

	// Returns all artist records in the database
	public Cursor readAddress() {

		// public Cursor query (String table, String[] columns, String
		// selection, String[] selectionArgs, String groupBy, String having,
		// String orderBy)

		return mDB.query(DBOpenHelper.TABLE_LOCATIONS,
				DBOpenHelper.location_columns, null, new String[] {}, null,
				null, null);
	}

	public Cursor readTrip(long tripId) {

		return mDB.query(DBOpenHelper.TABLE_TRIPS, new String[] {
				DBOpenHelper._ID, DBOpenHelper.TRIP_NAME }, "_id=?",
				new String[] { "" + tripId }, null, null, null);
	}

	public Cursor readTrips() {

		return mDB.query(DBOpenHelper.TABLE_TRIPS, new String[] {
				DBOpenHelper._ID, DBOpenHelper.TRIP_NAME }, null,
				new String[] {}, null, null, null);
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
