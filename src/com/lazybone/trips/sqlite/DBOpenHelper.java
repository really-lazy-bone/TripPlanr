package com.lazybone.trips.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

	final static String TABLE_TRIPS = "Trips";
	public final static String _ID = "_id";
	public final static String TRIP_NAME = "name";

	final static String TRIP_TIME = "startTime";
	// final static String TRIP_LOCATIONS = "locations";
	final static String TRIP_METHOD = "defaultTravelMethod";

	final static String[] trip_columns = { _ID, TRIP_NAME, TRIP_METHOD };

	final static String TABLE_MANY_TO_MANY = "tl";
	final static String TRIP_ID = "trip_id";
	final static String LOCATION_ID = "location_id";

	final static String TABLE_LOCATIONS = "Locations";
	final static String LOCATION_ADDRESS = "address";
	final static String LOCATION_NAME = "name";
	final static String LOCATION_TYPE = "type";
	final static String LOCATION_NOTES = "notes";
	final static String LOCATION_LAT = "lat";
	final static String LOCATION_LON = "lon";
	public final static String[] location_columns = { _ID, LOCATION_NAME,
			LOCATION_ADDRESS };
	public final static String[] all_location_columns = { _ID,
			LOCATION_ADDRESS, LOCATION_NAME, LOCATION_TYPE, LOCATION_NOTES,
			LOCATION_LAT, LOCATION_LON };

	final static String TABLE_ROUTES = "Routes";
	final static String ROUTE_FROM = "fromLocation";
	final static String ROUTE_TO = "toLocation";
	final static String ROUTE_METHOD = "travelMethod";
	public final static String[] route_columns = { _ID, ROUTE_FROM, ROUTE_TO,
			ROUTE_METHOD };

	final private static String CREATE_TRIPS =

	"CREATE TABLE " + TABLE_TRIPS + "(" + _ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + TRIP_NAME
			+ " TEXT NOT NULL, " + TRIP_TIME + " INTEGER, "

			+ TRIP_METHOD + " TEXT" + ")";

	final private static String CREATE_TL =

	"CREATE TABLE " + TABLE_MANY_TO_MANY + "(" + _ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + TRIP_ID
			+ " INTEGER NOT NULL, " + LOCATION_ID + " INTEGER NOT NULL" + ")";

	final private static String CREATE_LOCATIONS =

	"CREATE TABLE " + TABLE_LOCATIONS + "(" + _ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + LOCATION_ADDRESS
			+ " TEXT NOT NULL, " + LOCATION_NAME + " TEXT, " + LOCATION_TYPE
			+ " TEXT, " + LOCATION_NOTES + " TEXT, " + LOCATION_LAT + " REAL,"
			+ LOCATION_LON + " REAL"

			+ ")";

	final private static String CREATE_ROUTES =

	"CREATE TABLE " + TABLE_ROUTES + "(" + _ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + TRIP_ID
			+ " INTEGER NOT NULL, " + ROUTE_FROM + " INTEGER NOT NULL, "
			+ ROUTE_TO + " INTEGER NOT NULL, " + ROUTE_METHOD + " TEXT"

			+ ")";

	final private static String NAME = "trip_db";
	final private static Integer VERSION = 1;
	final private Context mContext;

	public DBOpenHelper(Context context) {
		super(context, NAME, null, VERSION);
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TRIPS);
		db.execSQL(CREATE_LOCATIONS);
		db.execSQL(CREATE_ROUTES);
		db.execSQL(CREATE_TL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// N/A
	}

	void deleteDatabase() {
		mContext.deleteDatabase(NAME);
	}
}
