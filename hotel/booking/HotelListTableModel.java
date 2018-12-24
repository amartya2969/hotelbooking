/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hotel.booking;

import javax.swing.table.DefaultTableModel;

public class HotelListTableModel extends DefaultTableModel {

    public static final String[] COLUMN_NAMES = {"", "Hotel Name", "Price Per Night", "Rating", ""};

    public HotelListTableModel() {
        super(COLUMN_NAMES, 0);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (getRowCount() > 0 && getValueAt(0, columnIndex) != null) {
            return getValueAt(0, columnIndex).getClass();
        }
        return super.getColumnClass(columnIndex);
    }

}
