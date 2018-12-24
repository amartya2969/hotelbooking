package com.hotel.booking;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseModel {

	String pkFieldName;
	String tableName;
	Connection connection;
	Statement s;

	DatabaseModel(String tableName, String pkFieldName) throws SQLException {
		this.tableName = tableName;
		this.pkFieldName = pkFieldName;
		this.connection = DatabaseConnection.getConnection();
		s = connection.createStatement();
	}

	ResultSet getAll() throws SQLException {
		String query = "SELECT * FROM " + this.tableName;
		ResultSet result = s.executeQuery(query);
                return result;		
	}

	ResultSet getByPrimaryKey(int val) throws SQLException {
		String query = "SELECT * FROM " + this.tableName + " WHERE " + this.pkFieldName + " = " + val;
		ResultSet result = s.executeQuery(query);

		if (result.next())
			return result;
		else
			return null;

	}

	ResultSet getByPrimaryKey(String val) throws SQLException {
		String query = "SELECT * FROM " + this.tableName + " WHERE " + this.pkFieldName + " = '" + val + "'";
		ResultSet result = s.executeQuery(query);

		if (result.next())
			return result;
		else
			return null;

	}

	void deleteByPrimaryKey(int val) throws SQLException {
		String query = "DELETE FROM " + this.tableName + " WHERE " + this.pkFieldName + " = " + val;
		s.executeUpdate(query);
	}

	void deleteByPrimaryKey(String val) throws SQLException {
		String query = "DELETE FROM " + this.tableName + " WHERE " + this.pkFieldName + " = '" + val + "'";
		s.executeUpdate(query);
	}

}
