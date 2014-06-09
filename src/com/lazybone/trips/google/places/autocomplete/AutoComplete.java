package com.lazybone.trips.google.places.autocomplete;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.lazybone.trips.sqlite.DatabaseAccessObject;


public class AutoComplete {

	private static final String LOG_TAG = "TripPlnr";

	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String AMAZON_URL = "http://ec2-54-200-230-148.us-west-2.compute.amazonaws.com:8181/places?googlePlacesURL=";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String OUT_JSON = "/json";
	private static DatabaseAccessObject dao;
	
	public static ArrayList<Place> autocomplete(String input, Context activity) {
		ArrayList<Place> resultList = new ArrayList<Place>();
		
		// pattern match in local database
		dao = new DatabaseAccessObject(activity);
		
			Cursor c = dao.matchLocation(input);
		
		if (c.moveToFirst()) {

            while (c.isAfterLast() == false) {
            	long id = Long.parseLong(c.getString(c.getColumnIndex("_id")));
                String name = c.getString(c.getColumnIndex("name"));
                String address = c.getString(c.getColumnIndex("address"));
                String type = c.getString(c.getColumnIndex("type"));
                double lat = c.getColumnIndex("lat");
                double lon = c.getColumnIndex("lon");
                
                Place placeToAdd = new Place(id, name, address, lat, lon, type, true);
                resultList.add(placeToAdd);
                
                Log.d("test02","db :" + name);
                c.moveToNext();
            }
        
		}
		
		HttpURLConnection conn = null;
		StringBuilder response = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE
					+ TYPE_AUTOCOMPLETE + OUT_JSON);
			sb.append("?sensor=false");
			sb.append("&input="
					+ URLEncoder.encode(input.replace(' ', '.'), "utf8"));

			URL url = new URL(AMAZON_URL
					+ URLEncoder.encode(sb.toString(), "utf8"));

			conn = (HttpURLConnection) url.openConnection();
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(conn.getInputStream(), "ISO-8859-15"));
			String inputLine;
 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch (MalformedURLException e) {
			Log.e(LOG_TAG,
					"Error processing AWS Google proxy Service response", e);
			return resultList;
		} catch (IOException e) {
			Log.e(LOG_TAG, "Error connecting to AWS Google proxy service", e);
			return resultList;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		try {
			Log.d("response", response.toString());
			// Create a JSON object hierarchy from the results
			JSONObject jsonObj = new JSONObject(response.toString());
			JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

			// Extract the Place descriptions from the results
			//resultList = new ArrayList<Place>();
			for (int i = 0; i < predsJsonArray.length(); i++) {
				
				
				Place placeToAdd = new Place(predsJsonArray.getJSONObject(i)
						.getString("description"), predsJsonArray
						.getJSONObject(i).getString("reference"));
				JSONArray termsArray = predsJsonArray.getJSONObject(i)
						.getJSONArray("terms");

				for (int j = 0; j < termsArray.length(); j++) {
					placeToAdd.getTerms().add(
							termsArray.getJSONObject(j).getString("value"));

				}
				JSONArray typesArray = predsJsonArray.getJSONObject(i)
						.getJSONArray("types");

				for (int j = 0; j < typesArray.length(); j++) {
					placeToAdd.getTypes().add(typesArray.getString(j));

				}

				if(placeToAdd.getTypes().contains("postal_code"))
				{
					//Don't show postal addresses
					continue;
				}

				resultList.add(placeToAdd);
			}
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Cannot process JSON results", e);
		}

		return resultList;
	}
	
}
