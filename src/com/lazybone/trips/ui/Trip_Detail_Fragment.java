package com.lazybone.trips.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.lazybone.trips.google.places.autocomplete.Place;
import com.lazybone.trips.model.Location;
import com.lazybone.trips.model.Route;
import com.lazybone.trips.model.TripDetailItem;
import com.lazybone.trips.sqlite.DatabaseAccessObject;
import com.tripplanr.R;

public class Trip_Detail_Fragment extends Fragment {

	private SimpleCursorAdapter mAdapter;
	private DatabaseAccessObject dao;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dao = new DatabaseAccessObject(getActivity());

		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.trip_detail, container,
				false);

		long tripId = this.getArguments().getLong("tripId");

		// get cursors
		List<Location> locations = dao.readAddress(tripId);
		List<Route> routes = dao.readRoutes(tripId);
		
		List<TripDetailItem> list = new ArrayList<TripDetailItem>();
		
		for (Location l: locations) {
			// add the location to the list
			list.add(l);
			
			// add the route which has from location equals to the location
			for (Route r: routes) {
				if (r.getFromLocationId() == l.getId()) {
					list.add(r);
				}
			}
		}

		Cursor tripCursor = dao.readTrip(tripId);

		tripCursor.moveToFirst();
		String tripName = tripCursor.getString(1);

		TextView tripNameView = (TextView) rootView
				.findViewById(R.id.name_of_trip);
		tripNameView.setText(tripName);

		ListView listLocationView = (ListView) rootView
				.findViewById(R.id.list_point);

//		mAdapter = new SimpleCursorAdapter(getActivity(),
//				R.layout.location_row, c, DBOpenHelper.location_columns,
//				new int[] { 0, R.id.location_id, R.id.location_label }, 0);

		listLocationView.setAdapter(mAdapter);

		listLocationView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});

		return rootView;
	}
	
	private class TripDetailAdapter extends ArrayAdapter<TripDetailItem> {

		private final Context context;
		private final ArrayList<TripDetailItem> values;

		public TripDetailAdapter(Context context, ArrayList<TripDetailItem> values) {
			super(context, R.layout.row, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.location_row, parent, false);
			
			return view;
		}

	}
}
