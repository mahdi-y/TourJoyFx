package Test;

import Entities.Guide;
import Services.GuideServices;
import utils.MyDB;

import java.sql.SQLException;
import java.util.List;
import java.time.LocalDate;


public class Main {
    public static void main(String[] args) throws SQLException {
        // Obtain an instance of MyDB
        MyDB db = MyDB.getInstance();

        // Create an instance of GuideServices
        GuideServices guideServices = new GuideServices();

        // Add a new guide to the database
        Guide newGuide = new Guide(123456799, "John", "Doe", "john@example.com", "1234567890", "Male", "English", "1990-01-01",100, "",1);
        guideServices.add(newGuide);

        // Display all guides from the database
        List<Guide> guideList = guideServices.Read();

        // Display the retrieved guides
        System.out.println("************************** Displaying Guides ***********************************");
        for (Guide retrievedGuide : guideList) {
            System.out.println(retrievedGuide.toString());
        }



    }
}
