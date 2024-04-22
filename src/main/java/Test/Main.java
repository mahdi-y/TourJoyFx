package Test;

import Entities.Accomodation;
import Services.ServiceAccomodation;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Accomodation a1 = new Accomodation(3, "mi appartamento", "Apartment", "hhhh", 5002, 3);
        Accomodation a2 = new Accomodation(4, "dar jaddy", "farm house", "hhhh", 505, 3);
        Accomodation a3 = new Accomodation(5, "hhh", "guesthouse", "hhhh", 5023 , 3);
        Accomodation a4 = new Accomodation(6, "saif", "guefrachi", "hhhh5", 500000000, 3);


        // Create an instance of ServiceAccommodation
        ServiceAccomodation serviceAccomodation = new ServiceAccomodation();

        try {
            // Add accommodations
            serviceAccomodation.add(a1);
            serviceAccomodation.add(a2);
            serviceAccomodation.add(a3);
            serviceAccomodation.add(a4);


            //display all accommodations
            System.out.println("List of accommodations:");
            List<Accomodation> accomodationList = serviceAccomodation.Read();
            for (Accomodation accomodation : accomodationList) {
                System.out.println(accomodation);
            }

            // Update an acc
            a1.setName("United States");
            serviceAccomodation.update(a1);

            // Display all accommodations after update
            System.out.println("\nList of accommodations after update:");
            accomodationList = serviceAccomodation.Read();
            for (Accomodation accomodation : accomodationList) {
                System.out.println(accomodation);
            }

            // Delete an accommodation
            serviceAccomodation.delete(a2);

            // Display all accommodations after delete
            System.out.println("\nList of Accommodations after delete:");
            accomodationList = serviceAccomodation.Read();
            for (Accomodation accomodation : accomodationList) {
                System.out.println(accomodation);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
