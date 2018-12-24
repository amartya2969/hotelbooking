package com.hotel.booking;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class BookingsModel extends DatabaseModel {

    private HotelsModel hotelsModel;
    private RoomsModel roomsModel;

    public BookingsModel() throws SQLException {
        super("bookings", "id");
        hotelsModel = new HotelsModel();
        roomsModel = new RoomsModel();
    }

    public Booking getById(int id) throws SQLException {
        ResultSet result = this.getByPrimaryKey(id);
        Booking booking = new Booking();

        PreparedStatement stmt = this.connection.prepareStatement(
                "SELECT * FROM booking_rooms INNER JOIN rooms ON rooms.id = booking_rooms.room WHERE booking = ? LIMIT 1");
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

        PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM booking_rooms WHERE booking = ?");
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

        PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM bookings WHERE user = ?");
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
                "INSERT INTO bookings(user, startDate, endDate) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, username);
        stmt.setDate(2, start);
        stmt.setDate(3, end);
        stmt.executeUpdate();

        ResultSet booking = stmt.getGeneratedKeys();

        while (booking.next()) {
            int i = 0;
            while (i < map.size()) {
                PreparedStatement stmt2 = this.connection
                        .prepareStatement("INSERT INTO booking_rooms (booking, room, num) VALUES (?, ?, ?)");
                stmt2.setInt(1, booking.getInt(1));
                stmt2.setInt(2, map.get(i).get(0));
                stmt2.setInt(3, map.get(i).get(1));
                stmt2.executeUpdate();
                i++;
            }
        }
    }

    public boolean modifyBooking(int id, Date newStart, Date newEnd) throws SQLException {

        Booking oldBooking = getById(id);

        PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM bookings WHERE endDate > ? AND startDate < ? AND id != ?");
        stmt.setDate(1, newStart);
        stmt.setDate(2, newEnd);
        stmt.setInt(3, id);
        boolean possible = true;

        for (Room room : oldBooking.getRooms()) {

            int bookedCount = 0;

            ResultSet clashingBookings = stmt.executeQuery();

            while (clashingBookings.next()) {

                Booking clashingBooking = getById(clashingBookings.getInt("id"));

                for (Room clashedRoom : clashingBooking.getRooms()) {

                    if (clashedRoom.getId() == room.getId()) {
                        bookedCount += clashedRoom.getNumBookings();
                    }

                }

            }

            if (bookedCount + room.getNumBookings() > room.getAvailableCount()) {
                possible = false;
            }

        }

        if (possible) {
            stmt = this.connection.prepareStatement("UPDATE bookings SET startDate = ?, endDate = ? WHERE id = ?");
            stmt.setDate(1, newStart);
            stmt.setDate(2, newEnd);
            stmt.setInt(3, id);
            stmt.execute(); 
            new WaitingsModel().clearWaiting();
            return true;
        }

        return false;
    }
}
