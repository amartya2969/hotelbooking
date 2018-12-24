package com.hotel.booking;

import java.util.ArrayList;

public class Hotel {

    private int id;
    private String name;
    private int city;
    private String address;
    private String phone;
    private ArrayList<Amenity> amenities;
    private ArrayList<Room> singleRoomList;
    private ArrayList<Room> doubleRoomList;
    private ArrayList<Room> familyRoomList;
    private ArrayList<Feedback> feedbacks;
    private ArrayList<Room> bookedRooms;

    public ArrayList<Room> getBookedRooms() {
        return bookedRooms;
    }

    public void setBookedRooms(ArrayList<Room> bookedRooms) {
        this.bookedRooms = bookedRooms;
    }

    public ArrayList<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(ArrayList<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public double getAverageRating() {

        double rating = 0;
        int count = 0;

        for (Feedback f : feedbacks) {
            rating += f.getRating();
            count++;
        }

        if (count != 0) {
            return rating / count;
        } else {
            return 0;
        }
    }

    public ArrayList<Room> getSingleRoomList() {
        return singleRoomList;
    }

    public void setSingleRoomList(ArrayList<Room> singleRoomList) {
        this.singleRoomList = singleRoomList;
    }

    public ArrayList<Room> getDoubleRoomList() {
        return doubleRoomList;
    }

    public void setDoubleRoomList(ArrayList<Room> doubleRoomList) {
        this.doubleRoomList = doubleRoomList;
    }

    public ArrayList<Room> getFamilyRoomList() {
        return familyRoomList;
    }

    public void setFamilyRoomList(ArrayList<Room> familyRoomList) {
        this.familyRoomList = familyRoomList;
    }

    public double getMinimumSinglePrice() {

        double price = singleRoomList.get(0).getPrice();

        for (Room room : singleRoomList) {
            if (room.getPrice() < price) {
                price = room.getPrice();
            }
        }

        return price;
    }

    public double getMinimumDoublePrice() {

        double price = doubleRoomList.get(0).getPrice();

        for (Room room : doubleRoomList) {
            if (room.getPrice() < price) {
                price = room.getPrice();
            }
        }

        return price;
    }

    public double getMinimumFamilyPrice() {

        double price = familyRoomList.get(0).getPrice();

        for (Room room : familyRoomList) {
            if (room.getPrice() < price) {
                price = room.getPrice();
            }
        }

        return price;
    }

    public boolean hasAmenities(ArrayList<Integer> amenities) {

        for (int amenityId : amenities) {

            int found = 0;

            for (Amenity amenity : this.amenities) {
                if (amenity.getId() == amenityId) {
                    found = 1;
                }
            }

            if (found == 0) {
                return false;
            }

        }

        return true;

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

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
