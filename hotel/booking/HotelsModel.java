package com.hotel.booking;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HotelsModel extends DatabaseModel {

    private AmenitiesModel amenitiesModel;
    private FeedbacksModel feedbacksModel;
    private RoomsModel roomsModel;
    private ArrayList<Hotel> availableList;
    private ArrayList<Hotel> waitingList;

    HotelsModel() throws SQLException {
        super("hotels", "id");
        amenitiesModel = new AmenitiesModel();
        feedbacksModel = new FeedbacksModel();
        roomsModel = new RoomsModel();
        availableList = new ArrayList<>();
        waitingList = new ArrayList<>();
    }

    public Hotel getHotelById(int id) throws SQLException {

        ResultSet result = this.getByPrimaryKey(id);

        Hotel hotel = new Hotel();
        hotel.setId(id);
        hotel.setName(result.getString("name"));
        hotel.setCity(result.getInt("city"));
        hotel.setAddress(result.getString("address"));
        hotel.setPhone(result.getString("phone"));
        hotel.setAmenities(amenitiesModel.getHotelAmenities(id));
        hotel.setFeedbacks(feedbacksModel.getHotelFeedbacks(id));

        return hotel;

    }

    public ArrayList<Hotel> getAvailableList() {
        return availableList;
    }

    public void setAvailableList(ArrayList<Hotel> availableList) {
        this.availableList = availableList;
    }

    public ArrayList<Hotel> getWaitingList() {
        return waitingList;
    }

    public void setWaitingList(ArrayList<Hotel> waitingList) {
        this.waitingList = waitingList;
    }

    public void generateLists(int city, int numSingle, int numDouble, int numFamily, Date start, Date end)
            throws SQLException {

        availableList.clear();
        waitingList.clear();

        String availableTable = "SELECT RoomsHotelTotal.*, COALESCE(bookingStatus.bookings_num, 0) AS bookings_num, COALESCE((totalCount - bookings_num), totalCount ) AS availableCount FROM (SELECT trueHotels.id AS hotelId, trueHotels.city AS cityID, trueHotels.name AS hotelName, trueHotels.phone, trueHotels.address, rooms.id AS roomID, rooms.name AS roomName, rooms.price, rooms.count AS totalCount, rooms.type FROM (SELECT * FROM hotels WHERE id IN (SELECT id FROM (SELECT id, COALESCE(singleCount, 0) AS singleCount, COALESCE(doubleCount, 0) AS doubleCount, COALESCE(familyCount, 0) AS familyCount FROM (SELECT DISTINCT hotels.id, (SELECT SUM(count) FROM rooms WHERE hotel = hotels.id AND type = 1) AS singleCount, (SELECT SUM(count) FROM rooms WHERE hotel = hotels.id AND type = 2) AS doubleCount, (SELECT SUM(count) FROM rooms WHERE hotel = hotels.id AND type = 3) AS familyCount FROM hotels, rooms) AS NullCheck WHERE singleCount >= ? AND doubleCount >= ? AND familyCount >= ?) AS validHotels)) AS trueHotels INNER JOIN rooms ON rooms.hotel = trueHotels.id) AS RoomsHotelTotal LEFT JOIN (SELECT room, SUM(num) AS bookings_num FROM booking_rooms INNER JOIN bookings ON bookings.id = booking_rooms.booking WHERE endDate > ? AND startDate < ? GROUP BY room) AS bookingStatus ON RoomsHotelTotal.roomId = bookingStatus.room WHERE cityId = ?";
        String roomQuery = "SELECT * FROM (SELECT RoomsHotelTotal.*, COALESCE(bookingStatus.bookings_num, 0) AS bookings_num, COALESCE((totalCount - bookings_num), totalCount ) AS availableCount FROM (SELECT trueHotels.id AS hotelId, trueHotels.city AS cityID, trueHotels.name AS hotelName, trueHotels.phone, trueHotels.address, rooms.id AS roomID, rooms.name AS roomName, rooms.price, rooms.count AS totalCount, rooms.type FROM (SELECT * FROM hotels WHERE id IN (SELECT id FROM (SELECT id, COALESCE(singleCount, 0) AS singleCount, COALESCE(doubleCount, 0) AS doubleCount, COALESCE(familyCount, 0) AS familyCount FROM (SELECT DISTINCT hotels.id, (SELECT SUM(count) FROM rooms WHERE hotel = hotels.id AND type = 1) AS singleCount, (SELECT SUM(count) FROM rooms WHERE hotel = hotels.id AND type = 2) AS doubleCount, (SELECT SUM(count) FROM rooms WHERE hotel = hotels.id AND type = 3) AS familyCount FROM hotels, rooms) AS NullCheck WHERE singleCount >= ? AND doubleCount >= ? AND familyCount >= ?) AS validHotels)) AS trueHotels INNER JOIN rooms ON rooms.hotel = trueHotels.id) AS RoomsHotelTotal LEFT JOIN (SELECT room, SUM(num) AS bookings_num FROM booking_rooms INNER JOIN bookings ON bookings.id = booking_rooms.booking WHERE endDate > ? AND startDate < ? GROUP BY room) AS bookingStatus ON RoomsHotelTotal.roomId = bookingStatus.room WHERE cityId = ?) AS roomFilter WHERE type = ? AND availableCount >= ? AND hotelId = ?";

        PreparedStatement availableStmt = this.connection.prepareStatement(availableTable);
        availableStmt.setInt(1, numSingle);
        availableStmt.setInt(2, numDouble);
        availableStmt.setInt(3, numFamily);
        availableStmt.setDate(4, start);
        availableStmt.setDate(5, end);
        availableStmt.setInt(6, city);
        ResultSet result = availableStmt.executeQuery();

        PreparedStatement roomStmt = this.connection.prepareStatement(roomQuery);
        roomStmt.setInt(1, numSingle);
        roomStmt.setInt(2, numDouble);
        roomStmt.setInt(3, numFamily);
        roomStmt.setDate(4, start);
        roomStmt.setDate(5, end);
        roomStmt.setInt(6, city);

        ArrayList<Integer> visitedHotels = new ArrayList<>();

        while (result.next()) {

            int hotelId = result.getInt("hotelId");
            boolean waiting = false;

            if (visitedHotels.indexOf(hotelId) >= 0) {
                continue;
            }

            visitedHotels.add(hotelId);
            Hotel hotel = this.getHotelById(hotelId);
            ArrayList<Room> singleRooms = new ArrayList<>();
            ArrayList<Room> doubleRooms = new ArrayList<>();
            ArrayList<Room> familyRooms = new ArrayList<>();
            roomStmt.setInt(9, hotelId);

            // Get Single Rooms
            roomStmt.setInt(8, numSingle);
            roomStmt.setInt(7, 1);
            ResultSet singleRoomsQuery = roomStmt.executeQuery();

            while (singleRoomsQuery.next()) {
                Room room = roomsModel.getRoom(singleRoomsQuery.getInt("roomId"));
                room.setAvailableCount(singleRoomsQuery.getInt("availableCount"));
                singleRooms.add(room);
            }

            if (numSingle > 0 && singleRooms.isEmpty()) {
                int count = 0;
                roomStmt.setInt(8, 0);
                singleRoomsQuery = roomStmt.executeQuery();
                while (singleRoomsQuery.next()) {
                    Room room = roomsModel.getRoom(singleRoomsQuery.getInt("roomId"));
                    room.setAvailableCount(singleRoomsQuery.getInt("availableCount"));
                    count += singleRoomsQuery.getInt("availableCount");
                    singleRooms.add(room);
                }
                waiting = numSingle > count;
            }

            // Get Double Rooms
            roomStmt.setInt(8, numDouble);
            roomStmt.setInt(7, 2);
            ResultSet douleRoomsQuery = roomStmt.executeQuery();

            while (douleRoomsQuery.next()) {
                Room room = roomsModel.getRoom(douleRoomsQuery.getInt("roomId"));
                room.setAvailableCount(douleRoomsQuery.getInt("availableCount"));
                doubleRooms.add(room);
            }

            if (numDouble > 0 && doubleRooms.isEmpty()) {
                int count = 0;
                roomStmt.setInt(8, 0);
                douleRoomsQuery = roomStmt.executeQuery();
                while (douleRoomsQuery.next()) {
                    Room room = roomsModel.getRoom(douleRoomsQuery.getInt("roomId"));
                    room.setAvailableCount(douleRoomsQuery.getInt("availableCount"));
                    doubleRooms.add(room);
                    count += douleRoomsQuery.getInt("availableCount");
                }
                waiting = numDouble > count;
            }

            // Get Family Rooms
            roomStmt.setInt(8, numFamily);
            roomStmt.setInt(7, 3);
            ResultSet familyRoomsQuery = roomStmt.executeQuery();

            while (familyRoomsQuery.next()) {
                Room room = roomsModel.getRoom(familyRoomsQuery.getInt("roomId"));
                room.setAvailableCount(familyRoomsQuery.getInt("availableCount"));
                familyRooms.add(room);
            }

            if (numFamily > 0 && familyRooms.isEmpty()) {
                int count = 0;
                roomStmt.setInt(8, 0);
                familyRoomsQuery = roomStmt.executeQuery();
                while (familyRoomsQuery.next()) {
                    Room room = roomsModel.getRoom(familyRoomsQuery.getInt("roomId"));
                    room.setAvailableCount(familyRoomsQuery.getInt("availableCount"));
                    familyRooms.add(room);
                    count += familyRoomsQuery.getInt("availableCount");
                }
                waiting = numFamily > count;
            }

            hotel.setSingleRoomList(singleRooms);
            hotel.setDoubleRoomList(doubleRooms);
            hotel.setFamilyRoomList(familyRooms);

            if (waiting) {
                this.waitingList.add(hotel);
            } else {
                this.availableList.add(hotel);
            }

        }
    }
}
