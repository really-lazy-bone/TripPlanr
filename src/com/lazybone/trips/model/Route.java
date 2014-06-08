package com.lazybone.trips.model;

public class Route implements TripDetailItem {
	
	private Trip trip;	// many to one
	private Location fromLocation;
	private Location toLocation;
	private String travelMethod;
	
	// transient
	private long fromLocationId;
	private long toLocationId;
	
	public Route(long fromLocationId, long toLocationId, String travelMethod) {
		super();
		this.setFromLocationId(fromLocationId);
		this.setToLocationId(toLocationId);
		this.travelMethod = travelMethod;
	}

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

	public long getFromLocationId() {
		return fromLocationId;
	}

	public void setFromLocationId(long fromLocationId) {
		this.fromLocationId = fromLocationId;
	}

	public long getToLocationId() {
		return toLocationId;
	}

	public void setToLocationId(long toLocationId) {
		this.toLocationId = toLocationId;
	}
	
	@Override
	public String getItemType() {
		return "Route";
	}
}
