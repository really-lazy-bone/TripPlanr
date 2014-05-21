package com.lazybone.trips.model;

import java.util.ArrayList;
import java.util.Date;

public class Trip {
	private String name;
	private Date startTime;
	private ArrayList<Route> routes;	// one to many
	private ArrayList<Location> locations; // many to many
	private String defaultTravelMethod;
	
	
}