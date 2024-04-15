package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private final String URL = "jdbc:mysql://localhost:3306/tourjoy-user";
    private final String USER = "root";
    private final String PWD = "";

    private Connection cnx;
    private static DBConnection instance;

    private DBConnection() {
        try {
            cnx = DriverManager.getConnection(URL, USER, PWD);
            System.out.println("Connexion établie !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'établissement de la connexion : " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return cnx;
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }
}