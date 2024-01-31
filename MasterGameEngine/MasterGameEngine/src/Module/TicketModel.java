package Module;
//
import util.DBCon;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.util.ArrayList;
import java.util.List;

//public class TicketModel {
//
//    public static List<Billet> rechercherBillets(String villeDepart, String villeArrivee, String dateDepart, String dateRetour, int nombreVoyageurs) {
//        List<Billet> billets = new ArrayList<>();
//        Connection connection = null;
//        PreparedStatement statement = null;
//        ResultSet resultSet = null;
//
//        try {
//            // Établir la connexion à la base de données
//            connection = DBCon.connect();
//
//            // Requête SQL pour rechercher les billets disponibles
//            String query = "SELECT * FROM billets WHERE ville_depart = ? AND ville_arrivee = ? AND date_depart = ? AND date_retour = ? AND places_disponibles >= ?";
//            statement = connection.prepareStatement(query);
//            statement.setString(1, villeDepart);
//            statement.setString(2, villeArrivee);
//            statement.setString(3, dateDepart);
//            statement.setString(4, dateRetour);
//            statement.setInt(5, nombreVoyageurs);
//
//            // Exécuter la requête
//            resultSet = statement.executeQuery();
//
//            // ...
//
//// Traiter les résultats
//            while (resultSet.next()) {
//                // Convertir les chaînes de date en objets LocalDate
//                LocalDate dateDepartValue = LocalDate.parse(resultSet.getString("date_depart"));
//                LocalDate dateRetourValue = LocalDate.parse(resultSet.getString("date_retour"));
//
//                // Créer un objet Billet à partir des résultats
//                Billet billet = new Billet(
//                        resultSet.getInt("id"),
//                        resultSet.getString("ville_depart"),
//                        resultSet.getString("ville_arrivee"),
//                        dateDepartValue,
//                        dateRetourValue,
//                        resultSet.getDouble("prix"),
//                        resultSet.getInt("places_disponibles"),
//                        resultSet.getString("Type")
//                );
//                billets.add(billet);
//            }
//
//// ...
//
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            // Gérer l'exception de manière appropriée dans votre application
//        } finally {
//            // Fermer les ressources de la base de données
//            try {
//                if (resultSet != null) resultSet.close();
//                if (statement != null) statement.close();
//                DBCon.disconnect(connection);
//            } catch (SQLException e) {
//                e.printStackTrace();
//                // Gérer l'exception de manière appropriée dans votre application
//            }
//        }
//
//        return billets;
//    }
//}
import util.DBCon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TicketModel {

    public static List<Billet> rechercherBillets(String villeDepart, String villeArrivee, String dateDepart, String dateRetour, int nombreVoyageurs) {
        List<Billet> billets = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBCon.connect();

            String dayOfWeekDepart = LocalDate.parse(dateDepart).getDayOfWeek().toString();
            String dayOfWeekRetour = LocalDate.parse(dateRetour).getDayOfWeek().toString();
            System.out.println(dayOfWeekDepart);
            System.out.println(dayOfWeekRetour);


            String query = "SELECT Tr_Id, Transport_Name, Departure_City, Arrival_City, Departure_Time, Arrival_Time, Price, Days, Type " +
                    "FROM trips " +
                    "WHERE Departure_City = ? AND Arrival_City = ? AND (Days LIKE ? OR Days LIKE ?)";


            statement = connection.prepareStatement(query);
            statement.setString(1, villeDepart);
            statement.setString(2, villeArrivee);
            statement.setString(3, "%" + dayOfWeekDepart + "%");
            statement.setString(4, "%" + dayOfWeekRetour + "%");

            resultSet = statement.executeQuery();
//            LocalDate dateDepartValue = dateDepart;
            LocalDate dateDepartValue = LocalDate.parse(dateDepart);

            LocalDate dateRetourValue = LocalDate.parse(dateRetour);

            while (resultSet.next()) {

                Billet billet = new Billet(
                        resultSet.getInt("Tr_Id"),
                        resultSet.getString("Departure_City"),
                        resultSet.getString("Arrival_City"),
//                resultSet.getTime("Departure_Time").toLocalTime(),
//                resultSet.getTime("Arrival_Time").toLocalTime(),
                        dateDepartValue,
                        dateRetourValue,
                        resultSet.getDouble("Price"),
                        nombreVoyageurs,
                        resultSet.getString("Type")
                );
                billets.add(billet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately in your application
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                DBCon.disconnect(connection);
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the exception appropriately in your application
            }
        }

        return billets;
    }

//    private static Billet convertResultSetToBillet(ResultSet resultSet) throws SQLException {
//        return new Billet(
//                resultSet.getInt("Tr_Id"),
//                resultSet.getString("Departure_City"),
//                resultSet.getString("Arrival_City"),
////                resultSet.getTime("Departure_Time").toLocalTime(),
////                resultSet.getTime("Arrival_Time").toLocalTime(),
//                dateDepartValue,
//                dateRetourValue,
//                resultSet.getDouble("Price"),
//                3,
//                resultSet.getString("Type")
//        );
//    }

}
