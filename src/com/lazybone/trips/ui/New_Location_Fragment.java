package com.lazybone.trips.ui;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.lazybone.trips.google.places.autocomplete.AutoComplete;
import com.lazybone.trips.google.places.autocomplete.Place;
import com.lazybone.trips.sqlite.DatabaseAccessObject;
import com.lazybone.trips.util.NetworkUtil;
import com.tripplanr.R;

public class New_Location_Fragment extends Fragment {
	private static final String LOG_TAG = "TripPlnr";

	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String AMAZON_URL = "http://ec2-54-200-230-148.us-west-2.compute.amazonaws.com:8181/places?googlePlacesURL=";
	private static final String TYPE_DETAIL = "/details";
	private static final String OUT_JSON = "/json";

	private DatabaseAccessObject dao;
	private AutoCompleteTextView autoCompView;
	private Button confirmAddLocation;
	private Place selectedPlace;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dao = new DatabaseAccessObject(getActivity());

		dao.readAddress();

		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.new_location,
				container, false);

		getActivity().getActionBar().setTitle("Add New Location");

		confirmAddLocation = (Button) rootView
				.findViewById(R.id.confirm_add_location);
		confirmAddLocation.setOnClickListener(new OnClickListener() {

			@SuppressLint("ShowToast")
			@Override
			public void onClick(View v) {

				if (selectedPlace == null) {

					Toast toast = Toast.makeText(getActivity(),
							"Select an item from the dropdown", 5);
					toast.setGravity(Gravity.TOP, 0, 0);
					toast.show();
					return;

				}

				if (!selectedPlace.isDB()) {
					// not connected
					if (NetworkUtil.getConnectivityStatus(getActivity()) == 0) {
						CharSequence text = "You're not connected to the internet!!";

						Toast toast = Toast.makeText(getActivity(), text, 5);
						toast.setGravity(Gravity.TOP, 0, 0);
						toast.show();
						return;
					}
				}
				if (!selectedPlace.isDB()) {
					StringBuilder sb = new StringBuilder(PLACES_API_BASE
							+ TYPE_DETAIL + OUT_JSON);
					sb.append("?sensor=false");

					sb.append("&reference=" + selectedPlace.getReference());

					try {
						URL url = new URL(AMAZON_URL
								+ URLEncoder.encode(sb.toString(), "utf8"));
						Log.d("details", url.toString());

						// not connected

						new PlacesDetailTask().execute(url);

					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				} else {
					AddLocationToTrip();
				}
			}
		});

		autoCompView = (AutoCompleteTextView) rootView
				.findViewById(R.id.new_location_name);
		autoCompView.setAdapter(new PlacesAutoCompleteAdapter(this
				.getActivity(), R.layout.row));
		autoCompView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View arg1,
					int position, long id) {
				Place placeClicked = (Place) adapterView
						.getItemAtPosition(position);
				selectedPlace = placeClicked;
				autoCompView.setText(placeClicked.getDescription());

				InputMethodManager inputManager = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);

				inputManager.hideSoftInputFromWindow(getActivity()
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);

			}
		});

		return rootView;
	}

	public void AddLocationToTrip() {

		if (selectedPlace.isDB()) {

			MainActivity main = (MainActivity) getActivity();

			main.locationsToAdd.add(selectedPlace);
			selectedPlace = null;

			getActivity().getActionBar().setTitle("Create New Trip");

			getFragmentManager().popBackStackImmediate();
		}

		else if (selectedPlace.getTypes().contains("establishment")) {
			// addLocation(selectedPlace.getTerms().get(0), address,
			// lat,
			// lon);
			if (selectedPlace.getTerms().size() != 0) {
				selectedPlace.setName(selectedPlace.getTerms().get(0));
			} else {
				selectedPlace.setName(selectedPlace.getDescription());
			}
			MainActivity main = (MainActivity) getActivity();

			main.locationsToAdd.add(selectedPlace);
			selectedPlace = null;


			getActivity().getActionBar().setTitle("Create New Trip");

			getFragmentManager().popBackStackImmediate();

		} else {
			// launch dialog to ask user for name of location
			LayoutInflater li = LayoutInflater.from(getActivity());
			View promptsView = li.inflate(R.layout.custom__prompt, null);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					getActivity());
			alertDialogBuilder.setView(promptsView);

			final EditText userInput = (EditText) promptsView
					.findViewById(R.id.editTextDialogUserInput);
			// set dialog message
			alertDialogBuilder
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// get user input and set it to
									// result
									selectedPlace.setName(userInput.getText()
											.toString());
									// addLocation(
									// selectedPlace.getName(),
									// selectedPlace
									// .getFormattedAddress(),
									// selectedPlace.getLat(),
									// selectedPlace.getLng());

									MainActivity main = (MainActivity) getActivity();

									main.locationsToAdd.add(selectedPlace);
									selectedPlace = null;


									getActivity().getActionBar().setTitle(
											"Create New Trip");

									getFragmentManager()
											.popBackStackImmediate();
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
		}
	}

	private class PlacesDetailTask extends AsyncTask<URL, Integer, JSONObject> {
		protected JSONObject doInBackground(URL... urls) {
			HttpURLConnection urlConnection = null;
			try {
				urlConnection = (HttpURLConnection) urls[0].openConnection();

				InputStream in = new BufferedInputStream(
						urlConnection.getInputStream());
				return new JSONObject(getResponseText(in));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {
				urlConnection.disconnect();
			}

			return null;
		}

		protected void onPostExecute(JSONObject result) {

			try {
				Log.d("resultDetail", result.toString());

				String address = result.getJSONObject("result").getString(
						"formatted_address");
				double lat = result.getJSONObject("result")
						.getJSONObject("geometry").getJSONObject("location")
						.getDouble("lat");
				double lon = result.getJSONObject("result")
						.getJSONObject("geometry").getJSONObject("location")
						.getDouble("lng");

				Log.d("address", address);
				selectedPlace.setFormattedAddress(address);
				selectedPlace.setLat(lat);
				selectedPlace.setLng(lon);

				AddLocationToTrip();

			} catch (JSONException e) {
				Log.e(LOG_TAG, "Cannot process JSON results", e);
			}

		}
	}

	@SuppressWarnings("resource")
	private static String getResponseText(InputStream inStream) {
		// very nice trick from
		// http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
		return new Scanner(inStream).useDelimiter("\\A").next();
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

			if (place.isDB()) {
				imageView.setImageResource(R.drawable.icon_userlocation);

			} else if (place.getTypes().contains("establishment")) {
				imageView.setImageResource(R.drawable.icon_business);

			} else {
				imageView.setImageResource(R.drawable.icon_map_marker);
			}
			if (place.isDB()) {
				firstLine.setVisibility(View.GONE);
				secondLine.setText(place.getName());

			} else if (place.getTypes().contains("establishment")) {
				firstLine.setText(place.getTerms().get(0));
				StringBuilder address = new StringBuilder();
				ArrayList<String> terms = place.getTerms();
				for (int i = 1; i < terms.size(); i++) {
					address.append(terms.get(i) + ", ");
				}
				secondLine.setText(address.toString());
			} else {
				firstLine.setVisibility(View.GONE);
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
						resultList = AutoComplete.autocomplete(
								constraint.toString(), getActivity());
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
