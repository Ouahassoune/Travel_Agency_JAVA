package Admin.Controller;



import Admin.Model.Trips;
import Admin.Resources.ConnectDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static Admin.Resources.ConnectDatabase.getConnection;

public class DashboardController implements Initializable {

    @FXML
    private VBox pnl_scroll;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private PieChart pieChart1;
    @FXML
    private PieChart pieChart2;
    @FXML
    private PieChart pieChart3;

    @FXML
    private Label RevPlane;
    @FXML
    private Label RevTrain;
    @FXML
    private Label RevCar;
    @FXML
    private Label PorsTt;
    @FXML
    private Label RevTt;
    @FXML
    private Label PorsHt;
    @FXML
    private Label RevHt;
    @FXML
    private Label NumUs;
    @FXML
    private Label NumTr;
    @FXML
    private Label NumHt;
    @FXML
    private Label TotRev;
    @FXML
    private Label TotUsr;
    @FXML
    private Label TotTrs;
    @FXML
    private Label TotHot;




    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupItem();
        setupBarChart();
        remplirDiagrammeCirculaire1();
        remplirDiagrammeCirculaire2();
        updatePieChart3();
        getUniqueUserCount();
        getUniqueHotelCount();
        getUniqueTransportCount();
        getUniqueRevenueCount();
    }

    private void setupItem() {
        List<Trips> tripsList = loadTrips();
        Node[] nodes = new Node[tripsList.size()];

        for (int i = 0; i < tripsList.size(); i++) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/Item.fxml"));
                nodes[i] = loader.load();
                pnl_scroll.getChildren().add(nodes[i]);
                ItemController controller = loader.getController();
                Trips trip = tripsList.get(i);
                controller.setName(trip.getTransportName());
                controller.setNumber(trip.getDay());

            } catch (IOException ex) {
                Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void setupBarChart() {
        barChart.getData().clear();
        Map<String, Double> totalPricesByCity = calculateTotalPricesByCity();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Double> entry : totalPricesByCity.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }



        barChart.setTitle("Total Prices by City");
        Node titleNode = barChart.lookup(".chart-title");
        if (titleNode instanceof Label) {
            ((Label) titleNode).setTextFill(Color.WHITE);
        } else if (titleNode instanceof Text) {
            ((Text) titleNode).setFill(Color.WHITE);
        }

        barChart.getData().addListener((ListChangeListener.Change<? extends XYChart.Series<String, Number>> change) -> {
            for (XYChart.Series<String, Number> s : barChart.getData()) {
                for (XYChart.Data<String, Number> data : s.getData()) {
                    Node node = data.getNode();
                    if (node != null) {
                        String style = "-fx-bar-fill: rgba(255,255,255,0.5);";
                        node.setStyle(style);
                    }
                }
            }
        });

        barChart.getData().add(series);
    }
    public static Map<String, Double> calculateTotalPricesByCity() {
        Map<String, Double> totalPricesByCity = new HashMap<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT ville_depart, SUM(prix_totale) AS total_prix FROM historique GROUP BY ville_depart ORDER BY total_prix DESC LIMIT 9");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String city = resultSet.getString("ville_depart");
                Double total = resultSet.getDouble("total_prix");
                totalPricesByCity.put(city, total);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalPricesByCity;
    }




    public void remplirDiagrammeCirculaire1() {
        double totalRevenue = 0.0;
        double totalPrixTotale = 0.0;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT SUM(prix_billet) AS total FROM historique");
             ResultSet resultSet1 = preparedStatement1.executeQuery();
             PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT SUM(prix_totale) AS total FROM historique");
             ResultSet resultSet2 = preparedStatement2.executeQuery()) {

            if (resultSet1.next()) {
                totalRevenue = resultSet1.getDouble("total");
            }

            if (resultSet2.next()) {
                totalPrixTotale = resultSet2.getDouble("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        double premiereCategorieValeur = (totalRevenue / totalPrixTotale) * 100.0;
        double deuxiemeCategorieValeur = 100.0 - premiereCategorieValeur;

        PorsTt.setText(String.format("%.0f%%", premiereCategorieValeur));
        RevTt.setText(String.format("%.0f", totalRevenue));

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Deuxième Catégorie", deuxiemeCategorieValeur),
                new PieChart.Data("Première Catégorie", premiereCategorieValeur)
        );

        pieChart1.setData(pieChartData);

        for (PieChart.Data data : pieChartData) {
            String category = data.getName();
            switch (category) {
                case "Première Catégorie":
                    data.getNode().setStyle("-fx-pie-color: blue;");
                    break;
                case "Deuxième Catégorie":
                    data.getNode().setStyle("-fx-pie-color: rgba(255,255,255,0);");
                    break;
            }
        }
    }

    public void remplirDiagrammeCirculaire2() {
        double totalPrixHotel = 0.0;
        double totalPrixTotale = 0.0;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT SUM(prix_chambre) AS total FROM historique");
             ResultSet resultSet1 = preparedStatement1.executeQuery();
             PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT SUM(prix_totale) AS total FROM historique");
             ResultSet resultSet2 = preparedStatement2.executeQuery()) {

            if (resultSet1.next()) {
                totalPrixHotel = resultSet1.getDouble("total");
            }

            if (resultSet2.next()) {
                totalPrixTotale = resultSet2.getDouble("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        double premiereCategorieValeur = (totalPrixHotel / totalPrixTotale) * 100.0;
        double deuxiemeCategorieValeur = 100.0 - premiereCategorieValeur;

        PorsHt.setText(String.format("%.0f %%", premiereCategorieValeur));
        RevHt.setText(String.format("%.0f", totalPrixHotel));

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Deuxième Catégorie", deuxiemeCategorieValeur),
                new PieChart.Data("Première Catégorie", premiereCategorieValeur)
        );

        pieChart2.getData().clear();
        pieChart2.getData().addAll(pieChartData);

        for (PieChart.Data data : pieChartData) {
            String category = data.getName();
            switch (category) {
                case "Première Catégorie":
                    data.getNode().setStyle("-fx-pie-color: red;");
                    break;
                case "Deuxième Catégorie":
                    data.getNode().setStyle("-fx-pie-color: rgba(255,255,255,0);");
                    break;
            }
        }
    }

    public void updatePieChart3() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT transport, SUM(prix_billet) AS total FROM historique GROUP BY transport");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String transport = resultSet.getString("transport");
                double totalRevenue = resultSet.getDouble("total");

                pieChartData.add(new PieChart.Data(transport, totalRevenue));

                switch (transport) {
                    case "Car":
                        RevCar.setText(String.format("%.0f$", totalRevenue));
                        break;
                    case "Plane":
                        RevPlane.setText(String.format("%.0f$", totalRevenue));
                        break;
                    case "Train":
                        RevTrain.setText(String.format("%.0f$", totalRevenue));
                        break;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        pieChart3.setData(pieChartData);

        for (PieChart.Data data : pieChartData) {
            String category = data.getName();
            switch (category) {
                case "Car":
                    data.getNode().setStyle("-fx-pie-color: #e74c3c;");
                    break;
                case "Plane":
                    data.getNode().setStyle("-fx-pie-color: #3498db;");
                    break;
                case "Train":
                    data.getNode().setStyle("-fx-pie-color: #2ecc71;");
                    break;
            }
        }
    }

    public void getUniqueUserCount() {
        int NumUser = 0;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(DISTINCT username) AS NumberOfUser FROM historique");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                NumUser = resultSet.getInt("NumberOfUser");
                NumUs.setText(String.format("%d", NumUser));
                TotUsr.setText(String.format("%d", NumUser));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void getUniqueHotelCount() {
        int NumHotel = 0;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(DISTINCT id) AS NumberOfHotels FROM hotel");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                NumHotel  = resultSet.getInt("NumberOfHotels");
                NumHt.setText(String.format("%d", NumHotel ));
                TotHot.setText(String.format("%d", NumHotel ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void getUniqueTransportCount() {
        int NumTrs = 0;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(DISTINCT Transport_Name) AS NumberOfTrs FROM trips");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                NumTrs = resultSet.getInt("NumberOfTrs");
                NumTr.setText(String.format("%d", NumTrs));
                TotTrs.setText(String.format("%d", NumTrs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void getUniqueRevenueCount() {
        Double TotalRevenue = 0.00;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT SUM(prix_totale) AS total FROM historique");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                TotalRevenue  = resultSet.getDouble("total");
                TotRev.setText(String.format("%.0f $", TotalRevenue ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Trips> loadTrips() {
        List<Trips> tripsList = new ArrayList<>();
        try (Connection connection = ConnectDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT Transport_Name, Days FROM trips");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Trips trip = new Trips();
                trip.setTransportName(resultSet.getString("Transport_Name"));
                trip.setDay(resultSet.getString("Days"));
                tripsList.add(trip);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tripsList;
    }

}
