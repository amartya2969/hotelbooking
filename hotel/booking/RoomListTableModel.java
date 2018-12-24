/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hotel.booking;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author sohailrajdev
 */
public class RoomListTableModel extends DefaultTableModel {

    public static final String[] COLUMN_NAMES = {"Room Type", "Price Per Night", "Amenities", "Available Rooms", "Booking Count"};

    public RoomListTableModel() {
        super(COLUMN_NAMES, 0);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        
        if(col == 4)
            return true;
        
        return false;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return Double.class;
            case 2:
                return String.class;
            case 3:
                return Integer.class;
            case 4:
                return Integer.class;
        }
        return Object.class;
    }

}
