package com.lazybone.trips.model;

import java.util.ArrayList;
import java.util.Date;

public class Trip {
	private String name;
	private Date startTime;
	private ArrayList<Route> routes; // one to many
	private ArrayList<Location> locations; // many to many
	private String defaultTravelMethod;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public ArrayList<Route> getRoutes() {
		return routes;
	}

	public void setRoutes(ArrayList<Route> routes) {
		this.routes = routes;
	}

	public ArrayList<Location> getLocations() {
		return locations;
	}

	public void setLocations(ArrayList<Location> locations) {
		this.locations = locations;
	}

	public String getDefaultTravelMethod() {
		return defaultTravelMethod;
	}

	public void setDefaultTravelMethod(String defaultTravelMethod) {
		this.defaultTravelMethod = defaultTravelMethod;
	}

}
