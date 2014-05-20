package com.lazybone.trips.model;

public class Route {

	private Trip trip;	// many to one
	private Location fromLocation;
	private Location toLocation;
	private String travelMethod;
	
	public Trip getTrip() {
		return trip;
	}
	public void setTrip(Trip trip) {
		this.trip = trip;
	}
	public Location getFromLocation() {
		return fromLocation;
	}
	public void setFromLocation(Location fromLocation) {
		this.fromLocation = fromLocation;
	}
	public Location getToLocation() {
		return toLocation;
	}
	public void setToLocation(Location toLocation) {
		this.toLocation = toLocation;
	}
	public String getTravelMethod() {
		return travelMethod;
	}
	public void setTravelMethod(String travelMethod) {
		this.travelMethod = travelMethod;
	}
	
	
}
