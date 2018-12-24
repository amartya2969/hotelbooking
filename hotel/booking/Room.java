package com.hotel.booking;

import java.util.ArrayList;

public class Room {

	private int id;
	private String name;
	private double price;
	private ArrayList<Amenity> amenities;
        private int availableCount;
	private int numBookings;

	public int getNumBookings() {
		return numBookings;
	}

	public void setNumBookings(int numBookings) {
		this.numBookings = numBookings;
	}
        
        public int getAvailableCount() {
		return availableCount;
	}

	public void setAvailableCount(int avail) {
		this.availableCount = avail;
	}

	Room() {
		amenities = new ArrayList<>();
	}

	public ArrayList<Amenity> getAmenities() {
		return amenities;
	}

	public void setAmenities(ArrayList<Amenity> amenities) {
		this.amenities = amenities;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
