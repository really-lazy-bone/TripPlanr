package com.lazybone.trips.ui;

import com.lazybone.trips.sqlite.DBOpenHelper;
import com.lazybone.trips.sqlite.DatabaseAccessObject;
import com.tripplanr.R;

import android.app.Fragment;
import android.database.Cursor;
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
import android.widget.Toast;

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
		
		mAdapter = new SimpleCursorAdapter(getActivity(),
				R.layout.location_row, c, DBOpenHelper.location_columns,
				new int[] { R.id.location_id, R.id.location_label }, 0);

		listLocationView.setAdapter(mAdapter);

		Button addLocationButton = (Button) rootView
				.findViewById(R.id.add_location);
		
		listLocationView.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
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
				EditText newLocationName = (EditText) rootView
						.findViewById(R.id.new_location_name);

				String newLocation = newLocationName.getText().toString();

				addLocation(v, newLocation);
			}
		});

		return rootView;
	}

	public void addLocation(View v, String locationName) {
		Toast.makeText(getActivity(), "Adding New Location " + locationName,
				Toast.LENGTH_LONG).show();

		dao.insertLocations(locationName);
		
		mAdapter.getCursor().requery();
		mAdapter.notifyDataSetChanged();
	}
}
