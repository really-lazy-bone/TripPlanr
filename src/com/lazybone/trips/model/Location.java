package com.lazybone.trips.model;

public class Location implements TripDetailItem {

	private long id;
	private String address;
	private String name;
	private String type;
	private String notes;
	private double lon;
	private double lat;
	
	public Location() {
		
	}
	
	public Location(long id, String address, String name, String type,
			String notes, double lon, double lat) {
		super();
		this.id = id;
		this.address = address;
		this.name = name;
		this.type = type;
		this.notes = notes;
		this.lon = lon;
		this.lat = lat;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	@Override
	public String getItemType() {
		return "Location";
	}

}
