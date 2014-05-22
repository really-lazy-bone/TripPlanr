package com.lazybone.trips.ui;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.lazybone.trips.sqlite.DBOpenHelper;
import com.lazybone.trips.sqlite.DatabaseAccessObject;
import com.tripplanr.R;

public class Trip_Detail_Fragment extends Fragment {

	private SimpleCursorAdapter mAdapter;
	private Cursor c;
	private DatabaseAccessObject dao;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dao = new DatabaseAccessObject(getActivity());

		c = dao.readAddress();

		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.trip_detail, container,
				false);
		
		long tripId = this.getArguments().getLong("tripId");
		
		Cursor tripCursor = dao.readTrip(tripId);
		
		tripCursor.moveToFirst();
		String tripName = tripCursor.getString(1);
		
		TextView tripNameView = (TextView) rootView.findViewById(R.id.name_of_trip);
		tripNameView.setText(tripName);

		ListView listLocationView = (ListView) rootView
				.findViewById(R.id.list_point);

		mAdapter = new SimpleCursorAdapter(getActivity(),
				R.layout.location_row, c, DBOpenHelper.location_columns,
				new int[] { 0, R.id.location_id, R.id.location_label }, 0);

		listLocationView.setAdapter(mAdapter);

		listLocationView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});

		return rootView;
	}
}
