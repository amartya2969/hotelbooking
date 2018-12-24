/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hotel.booking;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author sohailrajdev
 */
public class WaitingsModel extends DatabaseModel {

    private HotelsModel hotelsModel;
    private RoomsModel roomsModel;

    public WaitingsModel() throws SQLException {
        super("waiting_list", "id");
        hotelsModel = new HotelsModel();
        roomsModel = new RoomsModel();
    }

    public Booking getById(int id) throws SQLException {
        ResultSet result = this.getByPrimaryKey(id);
        Booking booking = new Booking();

        PreparedStatement stmt = this.connection.prepareStatement(
                "SELECT * FROM waiting_rooms INNER JOIN rooms ON rooms.id = waiting_rooms.room WHERE waiting_list_id  = ? LIMIT 1");
        stmt.setInt(1, id);
        ResultSet hotelSearchSet = stmt.executeQuery();
        hotelSearchSet.next();

        booking.setId(id);
        booking.setHotel(hotelsModel.getHotelById(hotelSearchSet.getInt("hotel")));
        booking.setRooms(this.getRooms(id));
        booking.setStartDate(result.getDate("startDate"));
        booking.setEndDate(result.getDate("endDate"));

        return booking;
    }

    public ArrayList<Room> getRooms(int bookingId) throws SQLException {

        ArrayList<Room> rooms = new ArrayList<>();

        PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM waiting_rooms WHERE waiting_list_id = ?");
        stmt.setInt(1, bookingId);
        ResultSet roomsSet = stmt.executeQuery();

        while (roomsSet.next()) {
            Room room = roomsModel.getRoom(roomsSet.getInt("room"));
            room.setNumBookings(roomsSet.getInt("num"));
            rooms.add(room);
        }

        return rooms;
    }

    public ArrayList<Booking> getByUser(String username) throws SQLException {

        ArrayList<Integer> visitedBookings = new ArrayList<>();
        ArrayList<Booking> bookings = new ArrayList<>();

        PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM waiting_list WHERE user = ?");
        stmt.setString(1, username);
        ResultSet bookingsSet = stmt.executeQuery();

        while (bookingsSet.next()) {

            if (visitedBookings.indexOf(bookingsSet.getInt("id")) >= 0) {
                continue;
            }

            visitedBookings.add(bookingsSet.getInt("id"));
            bookings.add(this.getById(bookingsSet.getInt("id")));

        }

        return bookings;

    }

    public void create(String username, Date start, Date end, ArrayList<ArrayList<Integer>> map) throws SQLException {

        PreparedStatement stmt = this.connection.prepareStatement(
                "INSERT INTO waiting_list(user, startDate, endDate) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, username);
        stmt.setDate(2, start);
        stmt.setDate(3, end);
        stmt.executeUpdate();

        ResultSet booking = stmt.getGeneratedKeys();

        while (booking.next()) {
            int i = 0;
            while (i < map.size()) {
                PreparedStatement stmt2 = this.connection
                        .prepareStatement("INSERT INTO waiting_rooms (waiting_list_id, room, num) VALUES (?, ?, ?)");
                stmt2.setInt(1, booking.getInt(1));
                stmt2.setInt(2, map.get(i).get(0));
                stmt2.setInt(3, map.get(i).get(1));
                stmt2.executeUpdate();
                i++;
            }
        }
    }

    public void clearWaiting() throws SQLException {

        BookingsModel bookingsModel = new BookingsModel();

        PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM waiting_list");
        ResultSet waitings = stmt.executeQuery();

        while (waitings.next()) {

            boolean cleared = true;

            ArrayList<Room> rooms = getRooms(waitings.getInt("id"));

            PreparedStatement bookingCheck = this.connection.prepareStatement("SELECT * FROM bookings WHERE endDate > ? AND startDate < ?");
            bookingCheck.setDate(1, waitings.getDate("startDate"));
            bookingCheck.setDate(2, waitings.getDate("endDate"));
            ArrayList< ArrayList<Integer>> map = new ArrayList<>();

            for (Room room : rooms) {

                int bookedCount = 0;
                ArrayList<Integer> bm = new ArrayList<>();
                bm.add(room.getId());
                bm.add(room.getNumBookings());
                map.add(bm);
                ResultSet bookingsResult = bookingCheck.executeQuery();

//                Find booked count of room in waiting
                while (bookingsResult.next()) {
                    Booking b = bookingsModel.getById(bookingsResult.getInt("id"));
                    for (Room bookedRoom : b.getRooms()) {
                        if (bookedRoom.getId() == room.getId()) {
                            bookedCount += bookedRoom.getNumBookings();
                        }
                    }
                }

                if (bookedCount + room.getNumBookings() > room.getAvailableCount()) {
                    cleared = false;
                }

            }

            if (cleared) {
                UsersModel um = new UsersModel();
                bookingsModel.create(waitings.getString("user"), waitings.getDate("startDate"), waitings.getDate("endDate"), map);
                User user = um.getUser(waitings.getString("user"));
                Mailer m = new Mailer(user.getEmail(), "Booking Confirmed !!", "Greetings, " + user.getName() + "\n\nYour waiting list booking has been confirmed. Please login to MakeYourTrip portal for further details.\n\nRegards,\nMake Your Trip Team");
                this.deleteByPrimaryKey(waitings.getInt("id"));
            }

        }

    }

}
