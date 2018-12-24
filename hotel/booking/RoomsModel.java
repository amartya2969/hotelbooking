package com.hotel.booking;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomsModel extends DatabaseModel {

	private AmenitiesModel amenitiesModel;

	public RoomsModel() throws SQLException {
		super("rooms", "id");
		amenitiesModel = new AmenitiesModel();
	}

	public Room getRoom(int id) throws SQLException {

		ResultSet result = this.getByPrimaryKey(id);
		Room room = new Room();
		room.setId(result.getInt("id"));
		room.setName(result.getString("name"));
		room.setPrice(result.getDouble("price"));
                room.setAvailableCount(result.getInt("count"));
		room.setAmenities(amenitiesModel.getRoomAmenities(room.getId()));
		return room;

	}

}
