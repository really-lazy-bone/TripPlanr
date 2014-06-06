package com.lazybone.trips.ui;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lazybone.trips.google.places.autocomplete.Place;

import com.lazybone.trips.sqlite.DatabaseAccessObject;
import com.tripplanr.R;

public class New_Trip_Fragment extends Fragment {

	// private SimpleCursorAdapter mAdapter;
	// private Cursor c;
	private PlaceArrayAdapter placeAdapter;
	private DatabaseAccessObject dao;
	private EditText tripNameInput;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dao = new DatabaseAccessObject(getActivity());
		//
		// c = dao.readAddress();

		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.new_trip, container,
				false);

		String[] travelMethods = getResources().getStringArray(
				R.array.travel_arrays);

		ListView listLocationView = (ListView) rootView
				.findViewById(R.id.list_location);

		MainActivity main = (MainActivity) getActivity();
		placeAdapter = new PlaceArrayAdapter(getActivity(), main.locationsToAdd);
		listLocationView.setAdapter(placeAdapter);

		Button addLocationButton = (Button) rootView
				.findViewById(R.id.add_location);

		Button addTripButton = (Button) rootView.findViewById(R.id.create_plan);
		Spinner mySpinner = (Spinner) rootView
				.findViewById(R.id.travel_method_spinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.spinner_travel_method, R.id.travel_method_hint,
				travelMethods);

		mySpinner.setAdapter(adapter);

		// listLocationView.setOnItemClickListener(new OnItemClickListener() {
		// @SuppressWarnings("deprecation")
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		//
		// c.moveToPosition(position);
		//
		// int location_id = c.getInt(0);
		//
		// dao.deleteLocation(location_id);
		//
		// mAdapter.getCursor().requery();
		// mAdapter.notifyDataSetChanged();
		// }
		// });

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
				transaction.replace(R.id.container, locationFrag,
						"newTripFragment");

				transaction.addToBackStack("newTripFragment");

				// Commit the transaction
				transaction.commit();

			}
		});

		addTripButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				MainActivity main = (MainActivity) getActivity();
				tripNameInput = (EditText) rootView
						.findViewById(R.id.name_of_trip);
				String tripName = tripNameInput.getText().toString();
				if (tripName.equals("")) {

					Toast toast = Toast.makeText(getActivity(),
							"You must provide a name for the trip!!", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 0, 0);
					toast.show();
					return;
				}
				if (main.locationsToAdd.size() == 0) {
					Toast toast = Toast.makeText(getActivity(),
							"You must have at least 1 location!!!", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 0, 0);
					toast.show();
					return;

				}

				Spinner travelMethodInput = (Spinner) rootView
						.findViewById(R.id.travel_method_spinner);
				String tripMethod = travelMethodInput.getSelectedItem()
						.toString();

				long tripId = dao.insertTrips(tripName, tripMethod,
						main.locationsToAdd);

				main.locationsToAdd.clear();
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

	private class PlaceArrayAdapter extends ArrayAdapter<Place> {

		private final Context context;
		private final ArrayList<Place> values;

		public PlaceArrayAdapter(Context context, ArrayList<Place> values) {
			super(context, R.layout.row, values);
			this.context = context;
			this.values = values;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.location_row, parent, false);
			TextView name = (TextView) view.findViewById(R.id.location_id);
			TextView address = (TextView) view
					.findViewById(R.id.location_label);
			ImageView imageView = (ImageView) view
					.findViewById(R.id.location_icon);
			Place place = values.get(position);
			if (place.getTypes().contains("establishment")) {
				imageView.setImageResource(R.drawable.icon_business);

			} else {
				imageView.setImageResource(R.drawable.icon_map_marker);
			}

			name.setText(values.get(position).getName());
			address.setText(values.get(position).getFormattedAddress());

			// get reference to the row
			// check for odd or even to set alternate colors to the row
			// background
			if (position % 2 == 0) {
				view.setBackgroundColor(getResources().getColor(
						R.color.even_row));
			} else {
				view.setBackgroundColor(getResources()
						.getColor(R.color.odd_row));
			}
			return view;
		}

	}

	// // extend the SimpleCursorAdapter to create a custom class where we
	// // can override the getView to change the row colors
	// @SuppressLint("ResourceAsColor")
	// private class MyCursorAdapter extends SimpleCursorAdapter {
	//
	// public MyCursorAdapter(Context context, int layout, Cursor c,
	// String[] from, int[] to, int flags) {
	// super(context, layout, c, from, to, flags);
	// }
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	//
	// // get reference to the row
	// View view = super.getView(position, convertView, parent);
	// // check for odd or even to set alternate colors to the row
	// // background
	// if (position % 2 == 0) {
	// view.setBackgroundColor(getResources().getColor(R.color.even_row));
	// } else {
	// view.setBackgroundColor(getResources().getColor(R.color.odd_row));
	// }
	// return view;
	// }
	//
	// }
}
