package com.tourjoy.ihebtransportt;

import utils.MyDB;

import java.sql.SQLException;

public class DatabaseConnectionTest {

    public static void main(String[] args) {
        MyDB myDB = MyDB.getInstance();
        try {
            System.out.println("Connected to database: " + myDB.getConnection().getCatalog());
        } catch (SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
    }
}