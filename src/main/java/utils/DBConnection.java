package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Déclaration d'une classe publique appelée DBConnection, utilisée pour établir une connexion à une base de données MySQL.
public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    //////////     Second Step: Creer une instance static de meme type que la classe
    private static DBConnection instance;
    // private static :Cela signifie qu'il n'y aura qu'une seule copie partagée de cette variable pour toute la classe DBConnection
    private Connection cnx;

    //////////First Step: Rendre le constructeur privé
    private DBConnection() {     // Définition du constructeur de la classe DBConnection.
        try {
            cnx = DriverManager.getConnection(URL, USER, PASSWORD); // Établissement de la connexion à la base de données en utilisant DriverManager.
            // Le DriverManager est une classe fournie par Java pour gérer les pilotes de connexion aux bases de données via JDBC
            System.out.println("Connected To DATABASE !");
        } catch (SQLException e) {
            System.err.println("Error: "+e.getMessage());
        }
    }



    //////////   Thrid Step: Creer une methode static pour recuperer l'instance
// Définition d'une méthode statique getInstance() pour récupérer l'instance unique de DBConnection
    public static DBConnection getInstance(){
        if (instance == null) instance = new DBConnection(); // Création d'une nouvelle instance si aucune instance n'existe
        return instance; // Retour de l'instance existante.
    }

    // Définition d'une méthode getCnx() pour récupérer la connexion à la base de données.
    public Connection getCnx() {
        return cnx; // Retour de la connexion à la base de données.
    }

}

// le modèle de conception Singleton, qui garantit qu'une seule instance de
// la classe DBConnection est créée.
//il s'applique sur la classe et non sur la base de donées

//JDBC: Java DataBase Connectivity : c'est une API (Application Programming Interface) d’interaction avec un SGBD contenant :
//un ensemble de classes et d’interfaces
//Permet de:
//1*Établir une connexion avec un SGBD
//2*Envoyer des requêtes SQL
//3*Récupérer des résultats de requêtes