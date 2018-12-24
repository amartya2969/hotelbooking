package com.hotel.booking;

import java.sql.Date;
import java.sql.SQLException;
import org.apache.commons.codec.digest.DigestUtils;

public class User {

	private String name;
	private String username;
	private String password;
	private String address;
	private String email;
	private String phone;
	private Date dob;
	private boolean loggedIn;
	private static UsersModel usersModel = null;

	public User() throws SQLException {

		loggedIn = false;

		if (usersModel == null)
			usersModel = new UsersModel();

	}

	public int register() throws SQLException {
		return usersModel.createUser(this);
	}

	public boolean login() throws SQLException {

		User fetchedUser = usersModel.getUser(this.username);

		if (fetchedUser != null && DigestUtils.md5Hex(this.password).compareTo(fetchedUser.getPassword()) == 0) {

			this.loggedIn = true;
			this.address = fetchedUser.getAddress();
			this.name = fetchedUser.getName();
			this.email = fetchedUser.getEmail();
			this.phone = fetchedUser.getPhone();
			this.dob = fetchedUser.getDob();

			return true;

		} else {
			return false;
		}
	}

	public String getName() {
		return name;
	}

	public boolean isLoggedIn() {
		return this.loggedIn;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = DigestUtils.md5Hex(password);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

}
