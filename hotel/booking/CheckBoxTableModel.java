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
public class CheckBoxTableModel extends DefaultTableModel {
    
    public static final String[] COLUMN_NAMES = {"", "Amenity"};

    public CheckBoxTableModel() {
        super(COLUMN_NAMES, 0);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        if(col == 0)
            return true;
        else
            return false;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if(columnIndex == 0)
            return Boolean.class;
        else
            return Object.class;
    }
    
}
