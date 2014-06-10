package com.lazybone.trips.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lazybone.trips.google.places.autocomplete.Place;
import com.lazybone.trips.model.Location;
import com.lazybone.trips.model.Route;

public class DatabaseAccessObject {

	private SQLiteDatabase mDB = null;
	private DBOpenHelper mDbHelper;

	private Integer NTRIP_TIME = 1000;

	private String NLOCATION_NOTES = "test notes";

	public DatabaseAccessObject() {

	}

	public DatabaseAccessObject(Context activity) {
		// Create a new DatabaseHelper
		mDbHelper = new DBOpenHelper(activity);

		// Get the underlying database for writing
		mDB = mDbHelper.getWritableDatabase();
	}

	public long addLocation(Place placeToInsert) {

		return insertLocations(placeToInsert.getFormattedAddress(),
				placeToInsert.getName(), placeToInsert.getTypeString(),
				placeToInsert.getLat(), placeToInsert.getLng());

	}

	public long insertTrips(String tripName, String tripMethod,
			List<Place> placesToInsert) {
		ContentValues values = new ContentValues();

		ArrayList<Integer> locationIds = new ArrayList<Integer>();

		for (Place place : placesToInsert) {
			int locationId = -1;
			if (!place.isDB()) {
				locationId = (int) addLocation(place);
				locationIds.add(locationId);
			} else {
				locationId = (int) place.getId();
				locationIds.add(locationId);

			}

		}
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

	public long insertLocations(String location, String name, String type,
			double lat, double lon) {
		ContentValues values = new ContentValues();
		Log.d("dbInsertLat", lat+"");
		Log.d("dbInsertLon", lon+"");

		values.put(DBOpenHelper.LOCATION_ADDRESS, location);
		values.put(DBOpenHelper.LOCATION_NAME, name);
		values.put(DBOpenHelper.LOCATION_LAT, lat);
		values.put(DBOpenHelper.LOCATION_LON, lon);
		values.put(DBOpenHelper.LOCATION_TYPE, type);
		values.put(DBOpenHelper.LOCATION_NOTES, NLOCATION_NOTES);

		return mDB.insert(DBOpenHelper.TABLE_LOCATIONS, null, values);
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

	// Overload readAdderss(long tripId), search certain trip locations address
	public List<Location> readAddress(long tripId) {

		Cursor c = mDB.query(DBOpenHelper.TABLE_MANY_TO_MANY, new String[] {
				DBOpenHelper._ID, DBOpenHelper.LOCATION_ID },
				DBOpenHelper.TRIP_ID + "=?", new String[] { tripId + "" },
				null, null, null);

		StringBuilder location_ids = new StringBuilder();
		location_ids.append("(");
		if (c.moveToFirst()) {
			do {
				location_ids.append(c.getString(1) + ",");
			} while (c.moveToNext());
		}
		location_ids.deleteCharAt(location_ids.length() - 1);
		location_ids.append(")");

		Cursor locationCursor = mDB.query(DBOpenHelper.TABLE_LOCATIONS,
				DBOpenHelper.all_location_columns, DBOpenHelper._ID + " in "
						+ location_ids, new String[] {}, null, null, null);

		List<Location> locations = new ArrayList<Location>();

		for (locationCursor.moveToFirst(); !locationCursor.isAfterLast(); locationCursor
				.moveToNext()) {
			locations.add(new Location(locationCursor.getLong(0),
					locationCursor.getString(1), locationCursor.getString(2),
					locationCursor.getString(3), locationCursor.getString(4),
					locationCursor.getDouble(5), locationCursor.getDouble(6)));
		}

		return locations;

	}

	// Overload readAdderss(long tripId), search certain trip locations address
	public List<Route> readRoutes(long tripId) {

		Cursor routeCursor = mDB.query(DBOpenHelper.TABLE_ROUTES,
				DBOpenHelper.route_columns, DBOpenHelper.TRIP_ID + "=?",
				new String[] { "" + tripId }, null, null, null);

		List<Route> routes = new ArrayList<Route>();

		for (routeCursor.moveToFirst(); !routeCursor.isAfterLast(); routeCursor
				.moveToNext()) {
			routes.add(new Route(routeCursor.getLong(1),
					routeCursor.getLong(2), routeCursor.getString(3)));
		}
		return routes;

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

	public Cursor matchLocation(String letter) {
		/*
		 * return mDB.query(DBOpenHelper.TABLE_LOCATIONS, new String[] {
		 * DBOpenHelper._ID, DBOpenHelper.LOCATION_ADDRESS,
		 * DBOpenHelper.LOCATION_NAME,DBOpenHelper.LOCATION_TYPE,
		 * DBOpenHelper.LOCATION_NOTES, DBOpenHelper.LOCATION_LAT,
		 * DBOpenHelper.LOCATION_LON}, "name LIKE '?%'", new String[] {letter},
		 * null, null, null);
		 */
		return mDB.rawQuery("select _id, name, address, type, notes, lat, lon "
				+ "from Locations where name LIKE ?", new String[] { "%"
				+ letter + "%" });
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
