package com.hotel.booking;

import java.sql.Date;
import java.util.ArrayList;

public class Booking {

    private int id;
    private Date startDate;
    private Date endDate;
    private Hotel hotel;
    private ArrayList<Room> rooms;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public double getBill() {
        double bill = 0;

        for (Room room : rooms) {
            bill += room.getNumBookings() * room.getPrice();
        }

        return bill * ((endDate.getTime() - startDate.getTime()) / 86400000);
    }

    public String getBookingString() {

        String bookingString = "<html>";

        for (Room room : rooms) {
            bookingString += room.getNumBookings() + " X " + room.getName() + "<br/>";
        }

        bookingString += "</html>";
        return bookingString;
    }

}
