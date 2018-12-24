/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hotel.booking;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author sohailrajdev
 */
public class CitiesModel extends DatabaseModel{

    public CitiesModel() throws SQLException {
        super("cities", "id");
    }
    
    public ArrayList<City> getList() throws SQLException {
        
        ResultSet citySet = this.getAll();
        ArrayList<City> cities = new ArrayList<>();
        
        while(citySet.next()) {
            City city = new City();
            city.setId(citySet.getInt("id"));
            city.setName(citySet.getString("name"));
            cities.add(city);
        }
        
        return cities;
    }
    
}
