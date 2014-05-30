package com.lazybone.trips.google.places.autocomplete;

import java.util.ArrayList;

import android.util.Log;

public class Place {

	private ArrayList<String> types;

	private String description;
	private ArrayList<String> terms;
	private String reference;

	// only use when place is not an establishment
	private String name;

	// retriebed from places detail api call
	private String formattedAddress;

	private double lat;
	private double lng;

	public Place(String description, String reference) {
		this.description = description;
		this.reference = reference;
		this.types = new ArrayList<String>();
		this.terms = new ArrayList<String>();
	}

	public ArrayList<String> getTypes() {
		return types;
	}

	public void setTypes(ArrayList<String> types) {
		this.types = types;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<String> getTerms() {
		return terms;
	}

	public void setTerms(ArrayList<String> terms) {
		this.terms = terms;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFormattedAddress() {
		return formattedAddress;
	}

	public void setFormattedAddress(String formattedAddress) {
		this.formattedAddress = formattedAddress;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	//returns long 
	public String getTypeString() {
		
		StringBuilder s = new StringBuilder();
		for (String type : types) {
			s.append(type +",");
		}
		s.deleteCharAt(s.length()-1);
		return s.toString();
		
	}

}
