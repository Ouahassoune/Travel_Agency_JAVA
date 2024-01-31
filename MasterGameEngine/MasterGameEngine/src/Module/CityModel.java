package Module;

import util.DBCon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CityModel {

    // Retrieve the list of cities from the database
    public static List<String> getCities() {
        List<String> cities = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Establish the database connection
            connection = DBCon.connect();

            // SQL query to retrieve cities
            String query = "SELECT city_name FROM cities";
            statement = connection.prepareStatement(query);

            // Execute the query
            resultSet = statement.executeQuery();

            // Process the results
            while (resultSet.next()) {
                String cityName = resultSet.getString("city_name");
                cities.add(cityName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately in your application
        } finally {
            // Close the database resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                DBCon.disconnect(connection);
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the exception appropriately in your application
            }
        }

        return cities;
    }
}
