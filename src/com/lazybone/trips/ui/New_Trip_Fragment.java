package com.lazybone.trips.ui;

import java.util.ArrayList;
import java.util.List;

import com.tripplanr.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class New_Trip_Fragment extends Fragment {

	List<String> locations = new ArrayList<String>();
	ArrayAdapter<String> adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.new_trip, container,
				false);

		ListView listLocationView = (ListView) rootView
				.findViewById(R.id.list_location);

		// dummy list of location
		locations.add("Hello");
		locations.add("World");

		adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.location_row, R.id.location_label,
				locations);

		listLocationView.setAdapter(adapter);

		Button addLocationButton = (Button) rootView
				.findViewById(R.id.add_location);

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

		adapter.add(locationName);
	}
}
