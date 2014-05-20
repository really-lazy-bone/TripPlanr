package com.lazybone.trips.sqlite;



import java.util.ArrayList;

import com.lazybone.trips.R;

import android.app.ListActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends ListActivity {

	private SQLiteDatabase mDB = null;
	private DBOpenHelper mDbHelper;
	private SimpleCursorAdapter mAdapter;
	
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
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		// Create a new DatabaseHelper
		mDbHelper = new DBOpenHelper(this);

		// Get the underlying database for writing
		mDB = mDbHelper.getWritableDatabase();

		// start with an empty database
		clearAll();

		// Insert records
		insertTrips();
		insertLocations();
		insertRoutes();
		insertManyToMany();

		// Create a cursor
		Cursor c = readAddress();
		mAdapter = new SimpleCursorAdapter(this, R.layout.list_layout, c,
				DBOpenHelper.location_columns, new int[] { R.id._id, R.id.name },
				0);

		setListAdapter(mAdapter);

		//Button fixButton = (Button) findViewById(R.id.fix_button);
		/*fixButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// execute database operations
				fix();

				// Redisplay data
				mAdapter.getCursor().requery();
				mAdapter.notifyDataSetChanged();
			}
		});*/
		
		clearAll();

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
	private Cursor readAddress() {
		
		// public Cursor query (String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) 
		
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

	// Close database
	@Override
	protected void onDestroy() {

		mDB.close();
		mDbHelper.deleteDatabase();

		super.onDestroy();

	}
}

