package utils;

import models.User;

public class SessionManager {
    private static User currentUser; // Static field to hold the currently logged-in user

    public static void setCurrentUser(User user) {
        currentUser = user; // Set the current user
    }

    public static User getCurrentUser() {
        return currentUser; // Retrieve the current user
    }


}
