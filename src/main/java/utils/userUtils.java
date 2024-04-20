package utils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class userUtils {

    /**
     * Extracts roles from a ResultSet and converts them into an array.
     * @param resultSet The ResultSet from which to extract the roles.
     * @return An array of roles.
     * @throws SQLException If there is a database access error.
     */
    public static String[] extractRoles(ResultSet resultSet) throws SQLException {
        String rolesString = resultSet.getString("roles"); // Get the roles as a single String
        return rolesString.split(","); // Split the string by comma to convert into an array
    }
}

