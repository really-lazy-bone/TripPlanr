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
import android.widget.TextView;

import com.lazybone.trips.model.Location;
import com.lazybone.trips.model.Route;
import com.lazybone.trips.model.TripDetailItem;
import com.lazybone.trips.sqlite.DatabaseAccessObject;
import com.tripplanr.R;

public class Trip_Detail_Fragment extends Fragment {

	private TripDetailAdapter mAdapter;
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

		for (Location l : locations) {
			// add the location to the list
			list.add(l);

			// add the route which has from location equals to the location
			for (Route r : routes) {
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

		mAdapter = new TripDetailAdapter(getActivity(), list);

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
		private final List<TripDetailItem> values;

		public TripDetailAdapter(Context context, List<TripDetailItem> values) {
			super(context, R.layout.trip_detail_row, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.trip_detail_row, parent,
					false);

			ImageView icon = (ImageView) view.findViewById(R.id.icon);
			TextView title = (TextView) view
					.findViewById(R.id.trip_detail_title);
			TextView subTitle = (TextView) view
					.findViewById(R.id.trip_detail_subtitle);

			if (values.get(position).getItemType().equals("Location")) {
				Location location = (Location) values.get(position);

				if (location.getType().contains("establishment")) {
					icon.setImageResource(R.drawable.icon_business);
				} else {
					icon.setImageResource(R.drawable.icon_map_marker);
				}

				title.setText(location.getName());
				subTitle.setText(location.getAddress());
			} else {
				Route route = (Route) values.get(position);

				if (route.getTravelMethod().equals("Driving")) {
					icon.setImageResource(R.drawable.icon_car);
				} else if (route.getTravelMethod().equals("Public Transport")) {
					icon.setImageResource(R.drawable.icon_bus);
				} else if (route.getTravelMethod().equals("Biking")) {
					icon.setImageResource(R.drawable.icon_bike);
				} else {
					icon.setImageResource(R.drawable.icon_compas);
				}
				
				Location from = (Location) values.get(position-1);
				Location to = (Location) values.get(position+1);
				
				title.setText(route.getTravelMethod());
				subTitle.setText("From " + from.getName()+ " to " + to.getName());
			}

			return view;
		}

	}
}
