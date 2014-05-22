package com.lazybone.trips.ui;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.lazybone.trips.google.places.autocomplete.AutoComplete;
import com.lazybone.trips.google.places.autocomplete.Place;
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

		mAdapter = new SimpleCursorAdapter(getActivity(),
				R.layout.location_row, c, DBOpenHelper.location_columns,
				new int[] { 0, R.id.location_id, R.id.location_label }, 0);

		listLocationView.setAdapter(mAdapter);

		Button addLocationButton = (Button) rootView
				.findViewById(R.id.add_location);

		listLocationView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				c.moveToPosition(position);

				int location_id = c.getInt(0);

				dao.deleteLocation(location_id);

				mAdapter.getCursor().requery();
				mAdapter.notifyDataSetChanged();
			}
		});

		// TODO modify add
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

		return rootView;
	}

	private class PlacesAutoCompleteAdapter extends ArrayAdapter<Place>
			implements Filterable {
		private ArrayList<Place> resultList;

		public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater.inflate(R.layout.row, parent, false);
			TextView firstLine = (TextView) rowView
					.findViewById(R.id.firstLine);
			TextView secondLine = (TextView) rowView
					.findViewById(R.id.secondLine);
			ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
			Place place = resultList.get(position);

			if (place.getTypes().contains("establishment")) {
				imageView.setImageResource(R.drawable.icon_business);

			} else {
				imageView.setImageResource(R.drawable.icon_map_marker);
			}
			if (place.getTypes().contains("establishment")) {
				firstLine.setText(place.getTerms().get(0));
				StringBuilder address = new StringBuilder();
				ArrayList<String> terms = place.getTerms();
				for (int i = 1; i < terms.size(); i++) {
					address.append(terms.get(i) + ", ");
				}
				secondLine.setText(address.toString());
			} else {
				firstLine.setHeight(0);
				secondLine.setText(place.getDescription());
			}

			return rowView;
		}

		@Override
		public int getCount() {
			return resultList.size();
		}

		@Override
		public Place getItem(int index) {
			return resultList.get(index);
		}

		@Override
		public Filter getFilter() {
			Filter filter = new Filter() {
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults filterResults = new FilterResults();
					if (constraint != null) {
						// Retrieve the autocomplete results.
						resultList = AutoComplete.autocomplete(constraint
								.toString());
						// Assign the data to the FilterResults
						filterResults.values = resultList;
						filterResults.count = resultList.size();
					}
					return filterResults;
				}

				@Override
				protected void publishResults(CharSequence constraint,
						FilterResults results) {
					if (results != null && results.count > 0) {
						notifyDataSetChanged();
					} else {
						notifyDataSetInvalidated();
					}
				}
			};
			return filter;
		}
	}
}
