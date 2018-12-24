package com.hotel.booking;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;;

public class UsersModel extends DatabaseModel {

	UsersModel() throws SQLException {
		super("users", "username");
	}

	User getUser(String username) throws SQLException {

		ResultSet result = this.getByPrimaryKey(username);

		if (result == null)
			return null;

		else {
			User user = new User();
			user.setName(result.getString("name"));
			user.setUsername(result.getString("username"));
			user.setPassword(result.getString("password"));
			user.setAddress(result.getString("address"));
			user.setEmail(result.getString("email"));
			user.setPhone(result.getString("phone"));
			user.setDob(result.getDate("dob"));
			return user;
		}

	}

	int createUser(User user) throws SQLException {

		/*
		 * Return Values:
		 * 
		 * 0 - User Created, 1 - Username Clash, 2 - Email Clash
		 * 
		 */

		PreparedStatement usernameStatement = this.connection
				.prepareStatement("SELECT * FROM users WHERE username = ?");
		usernameStatement.setString(1, user.getUsername());

		PreparedStatement emailStatement = this.connection.prepareStatement("SELECT * FROM users WHERE email = ?");
		emailStatement.setString(1, user.getEmail());

		ResultSet usernameResult = usernameStatement.executeQuery();
		ResultSet emailResult = emailStatement.executeQuery();

		if (usernameResult.next())
			return 1;

		if (emailResult.next())
			return 2;

		PreparedStatement insertStatement = this.connection.prepareStatement(
				"INSERT INTO users (username, password, name, address, email, phone, dob) VALUES (?, ?, ?, ?, ?, ?, ?)");

		insertStatement.setString(1, user.getUsername());
		insertStatement.setString(2, user.getPassword());
		insertStatement.setString(3, user.getName());
		insertStatement.setString(4, user.getAddress());
		insertStatement.setString(5, user.getEmail());
		insertStatement.setString(6, user.getPhone());
		insertStatement.setDate(7, user.getDob());
		insertStatement.executeUpdate();

		return 0;
	}
}
