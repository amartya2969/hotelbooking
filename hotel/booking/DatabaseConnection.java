package com.hotel.booking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

	private static Connection connection = null;

	public static Connection getConnection() {
		if (connection == null) {
			try {
				connection = DriverManager
						.getConnection("jdbc:mysql://localhost/HotelBooking?" + "user=root&useSSL=false");

			} catch (SQLException ex) {
				connection = null;
				System.out.println("SQLException: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());
			}
		}
		return connection;
	}
	
}
