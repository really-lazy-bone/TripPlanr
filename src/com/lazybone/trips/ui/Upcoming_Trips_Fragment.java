package com.lazybone.trips.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.lazybone.trips.sqlite.DBOpenHelper;
import com.lazybone.trips.sqlite.DatabaseAccessObject;
import com.tripplanr.R;

public class Upcoming_Trips_Fragment extends Fragment {

	private SimpleCursorAdapter mAdapter;
	private Cursor c;
	private DatabaseAccessObject dao;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dao = new DatabaseAccessObject(getActivity());

		c = dao.readTrips();

		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.upcoming_trips,
				container, false);

		ListView listTripView = (ListView) rootView
				.findViewById(R.id.list_trips);

		mAdapter = new SimpleCursorAdapter(getActivity(),
				R.layout.trip_row, c, new String[] { DBOpenHelper._ID,
						DBOpenHelper.TRIP_NAME }, new int[] { 0,
						R.id.trip_name }, 0);

		listTripView.setAdapter(mAdapter);
		
		listTripView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				c.moveToPosition(position);

				int tripId = c.getInt(0);

				Bundle bundle = new Bundle();
				bundle.putLong("tripId", tripId);
				
				Trip_Detail_Fragment tripDetailFrag = new Trip_Detail_Fragment();
				
				tripDetailFrag.setArguments(bundle);
				
				FragmentTransaction transaction = getFragmentManager()
						.beginTransaction();
				// Replace whatever is in the fragment_container view with this
				// fragment,
				// and add the transaction to the back stack so the user can
				// navigate back
				transaction.replace(R.id.container, tripDetailFrag);
				transaction.addToBackStack(null);

				// Commit the transaction
				transaction.commit();
			}
		});

		return rootView;
	}
}
