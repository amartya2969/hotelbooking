package com.hotel.booking;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AmenitiesModel extends DatabaseModel {

	public AmenitiesModel() throws SQLException {
		super("amenities", "id");
	}
        
        public ArrayList<Amenity> getList() throws SQLException {
            
            ResultSet amenitySet = this.getAll();
            ArrayList<Amenity> amenities = new ArrayList<>();
            
            while(amenitySet.next()) {
                Amenity amenity = new Amenity();
                amenity.setId(amenitySet.getInt("id"));
                amenity.setName(amenitySet.getString("amenity"));
                amenities.add(amenity);
            }
            
            return amenities;
        }

	public ArrayList<Amenity> getHotelAmenities(int id) throws SQLException {

		PreparedStatement stmt = this.connection.prepareStatement(
				"SELECT amenities.id, amenities.amenity FROM hotel_amenities INNER JOIN amenities ON amenities.id = hotel_amenities.amenity WHERE hotel = ?");
		stmt.setInt(1, id);

		ResultSet amenitiesSet = stmt.executeQuery();
		ArrayList<Amenity> amenities = new ArrayList<>();

		while (amenitiesSet.next()) {

			Amenity amenity = new Amenity();
			amenity.setId(amenitiesSet.getInt("id"));
			amenity.setName(amenitiesSet.getString("amenity"));
			amenities.add(amenity);

		}

		return amenities;

	}

	public ArrayList<Amenity> getRoomAmenities(int id) throws SQLException {

		PreparedStatement stmt = this.connection.prepareStatement(
				"SELECT amenities.id, amenities.amenity FROM room_amenities INNER JOIN amenities ON amenities.id = room_amenities.amenity WHERE room = ?");
		stmt.setInt(1, id);

		ResultSet amenitiesSet = stmt.executeQuery();
		ArrayList<Amenity> amenities = new ArrayList<>();

		while (amenitiesSet.next()) {

			Amenity amenity = new Amenity();
			amenity.setId(amenitiesSet.getInt("id"));
			amenity.setName(amenitiesSet.getString("amenity"));
			amenities.add(amenity);

		}

		return amenities;

	}

}
