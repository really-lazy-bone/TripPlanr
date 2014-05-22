package com.lazybone.trips.ui;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import com.lazybone.trips.sqlite.DBOpenHelper;
import com.lazybone.trips.sqlite.DatabaseAccessObject;
import com.tripplanr.R;

public class New_Trip_Fragment extends Fragment {

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
		final View rootView = inflater.inflate(R.layout.new_trip, container,
				false);

		ListView listLocationView = (ListView) rootView
				.findViewById(R.id.list_location);

		mAdapter = new MyCursorAdapter(getActivity(),
				R.layout.location_row, c, DBOpenHelper.location_columns,
				new int[] { 0, R.id.location_id, R.id.location_label }, 0);

		listLocationView.setAdapter(mAdapter);

		Button addLocationButton = (Button) rootView
				.findViewById(R.id.add_location);

		Button addTripButton = (Button) rootView.findViewById(R.id.create_plan);

		listLocationView.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("deprecation")
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				c.moveToPosition(position);

				int location_id = c.getInt(0);

				dao.deleteLocation(location_id);

				mAdapter.getCursor().requery();
				mAdapter.notifyDataSetChanged();
			}
		});

		addLocationButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				New_Location_Fragment locationFrag = new New_Location_Fragment();
				FragmentTransaction transaction = getFragmentManager()
						.beginTransaction();
				// Replace whatever is in the fragment_container view with this
				// fragment,
				// and add the transaction to the back stack so the user can
				// navigate back
				transaction.replace(R.id.container, locationFrag);
				transaction.addToBackStack(null);

				// Commit the transaction
				transaction.commit();

			}
		});

		addTripButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				List<Integer> locationIds = new ArrayList<Integer>();

				for (int i = 0; i < c.getCount(); i++) {
					c.moveToPosition(i);
					locationIds.add(c.getInt(0));
				}

				EditText tripNameInput = (EditText) rootView
						.findViewById(R.id.name_of_trip);
				String tripName = tripNameInput.getText().toString();

				Spinner travelMethodInput = (Spinner) rootView
						.findViewById(R.id.travel_method_spinner);
				String tripMethod = travelMethodInput.getSelectedItem()
						.toString();

				long tripId = dao
						.insertTrips(tripName, tripMethod, locationIds);

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

	// extend the SimpleCursorAdapter to create a custom class where we
	// can override the getView to change the row colors
	@SuppressLint("ResourceAsColor")
	private class MyCursorAdapter extends SimpleCursorAdapter {

		public MyCursorAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to, int flags) {
			super(context, layout, c, from, to, flags);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			// get reference to the row
			View view = super.getView(position, convertView, parent);
			// check for odd or even to set alternate colors to the row
			// background
			if (position % 2 == 0) {
				view.setBackgroundColor(getResources().getColor(R.color.even_row));
			} else {
				view.setBackgroundColor(getResources().getColor(R.color.odd_row));
			}
			return view;
		}

	}
}
