package com.lazybone.trips.google.places.autocomplete;

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

import android.util.Log;


public class AutoComplete {

	private static final String LOG_TAG = "TripPlnr";

	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String AMAZON_URL = "http://ec2-54-187-205-237.us-west-2.compute.amazonaws.com:8181/places?googlePlacesURL=";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String OUT_JSON = "/json";

	public static ArrayList<Place> autocomplete(String input) {
	    ArrayList<Place> resultList = null;

	    HttpURLConnection conn = null;
	    StringBuilder jsonResults = new StringBuilder();
	    try {
	        StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
	        sb.append("?sensor=false");
	        sb.append("&input=" + URLEncoder.encode(input.replace(' ', '.'), "utf8"));

	        URL url = new URL(AMAZON_URL+URLEncoder.encode(sb.toString(), "utf8"));
	        
	        conn = (HttpURLConnection) url.openConnection();
	        InputStreamReader in = new InputStreamReader(conn.getInputStream());

	        // Load the results into a StringBuilder
	        int read;
	        char[] buff = new char[1024];
	        while ((read = in.read(buff)) != -1) {
	            jsonResults.append(buff, 0, read);
	        }
	    } catch (MalformedURLException e) {
	        Log.e(LOG_TAG, "Error processing AWS Google proxy Service response", e);
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
	        // Create a JSON object hierarchy from the results
	        JSONObject jsonObj = new JSONObject(jsonResults.toString());
	        JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

	        // Extract the Place descriptions from the results
	        resultList = new ArrayList<Place>(predsJsonArray.length());
	        for (int i = 0; i < predsJsonArray.length(); i++) {
	        	Place placeToAdd = new Place(predsJsonArray.getJSONObject(i).getString("description"), predsJsonArray.getJSONObject(i).getString("reference"));
		        JSONArray termsArray = predsJsonArray.getJSONObject(i).getJSONArray("terms");
		        
		        for(int j=0; j< termsArray.length();j++)
		        {
		        	placeToAdd.getTerms().add(termsArray.getJSONObject(j).getString("value"));
		        	
		        }
JSONArray typesArray = predsJsonArray.getJSONObject(i).getJSONArray("types");
		        
		        for(int j=0; j< typesArray.length();j++)
		        {
		        	placeToAdd.getTypes().add(typesArray.getString(j));
		        	
		        }
	        	
	            resultList.add(placeToAdd);
	        }
	    } catch (JSONException e) {
	        Log.e(LOG_TAG, "Cannot process JSON results", e);
	    }

	    return resultList;
	}
}
