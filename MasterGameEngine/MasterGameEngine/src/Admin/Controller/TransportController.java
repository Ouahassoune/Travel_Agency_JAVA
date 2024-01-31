package Admin.Controller;

import Admin.Model.Citys;
import Admin.Model.Trips;
import static Admin.Resources.ConnectDatabase.getConnection;
import Admin.Resources.ConnectDatabase;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
public class TransportController {


    @FXML
    private AnchorPane root;

    @FXML
    private TextField searchTextField;

    @FXML
    private TableView<Trips> TransportTable;
    @FXML
    private TableColumn<Trips, Integer> IdColumn;
    @FXML
    private TableColumn<Trips, String> NameColumn;
    @FXML
    private TableColumn<Trips, String> DCColumn;
    @FXML
    private TableColumn<Trips, String> ACColumn;
    @FXML
    private TableColumn<Trips, String> PrColumn;
    @FXML
    private TableColumn<Trips, Void> deleteColumn;
    @FXML
    private TableColumn<Trips, Void> viewColumn;
    @FXML
    private TableColumn<Trips, Void> updateColumn;



    @FXML
    private AnchorPane pane1;
    @FXML
    private AnchorPane pane2;
    @FXML
    private AnchorPane updatePane;
    @FXML
    private AnchorPane addPane;
    @FXML
    private AnchorPane viewPane;
    @FXML
    private AnchorPane forupdate;
    @FXML
    private AnchorPane foradd;
    @FXML
    private AnchorPane forview;
    @FXML
    private Pane staticCityPane;
    @FXML
    private Pane addCityPane;
    @FXML
    private Pane foraddCity;


    @FXML
    private TextField TrNameTextField1;
    @FXML
    private ComboBox<String> departureCityComboBox1;
    @FXML
    private ComboBox<String> arrivalCityComboBox1;
    @FXML
    private TextField departureTimeTextField1;
    @FXML
    private TextField arrivalTimeTextField1;
    @FXML
    private TextField priceTextField1;
    @FXML
    private ComboBox<String> dayComboBox1;
    @FXML
    private CheckBox PlaneCheckBox1;
    @FXML
    private CheckBox CarCheckBox1;
    @FXML
    private CheckBox TrainCheckBox1;
    private String type1;



    @FXML
    private TextField TrNameTextField;
    @FXML
    private ComboBox<String> departureCityComboBox;
    @FXML
    private ComboBox<String> arrivalCityComboBox;
    @FXML
    private TextField departureTimeTextField;
    @FXML
    private TextField arrivalTimeTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private ComboBox<String> dayComboBox;
    @FXML
    private CheckBox PlaneCheckBox;
    @FXML
    private CheckBox CarCheckBox;
    @FXML
    private CheckBox TrainCheckBox;
    private String type;

    @FXML
    private TextField departureTimeTextFieldR;
    @FXML
    private TextField arrivalTimeTextFieldR;
    @FXML
    private TextField priceTextFieldR;
    @FXML
    private ComboBox<String> dayComboBoxR;


    @FXML
    private TextField updateTrNameTextField;
    @FXML
    private ComboBox<String> updatedepartureCityComboBox;
    @FXML
    private ComboBox<String> updatearrivalCityComboBox;
    @FXML
    private TextField updatedepartureTimeTextField;
    @FXML
    private TextField updatearrivalTimeTextField;
    @FXML
    private TextField updatepriceTextField;
    @FXML
    private ComboBox<String> updatedayComboBox;

    private Trips selectedTripForUpdate;


    @FXML
    private AnchorPane detailedInfoPane;
    @FXML
    private Label detailedNameLabel;
    @FXML
    private Label detailedDepCityLabel;
    @FXML
    private Label detailedArrCityLabel;
    @FXML
    private Label detailedDepTimeLabel;
    @FXML
    private Label detailedArrTimeLabel;
    @FXML
    private Label detailedPriceLabel;
    @FXML
    private Label detailedDayLabel;


    @FXML
    private TableView<Citys> CityTable;
    @FXML
    private TableColumn<Citys, Integer> IdCityColumn;
    @FXML
    private TableColumn<Citys, String> NameCityColumn;
    @FXML
    private TableColumn<Citys, Void> deleteCityColumn;
    @FXML
    private TableColumn<Citys, Void> viewCityColumn;
    @FXML
    private TextField CityNameTextField;
    @FXML
    private Label detailedCityNameLabel;
    @FXML
    private BarChart<String, Number> barChart;




    @FXML
    public void initialize() {


        populateCityComboBoxes();
        setupCellValueFactories();
        setupViewColumn();
        setupDeleteColumn();
        setupUpdateColumn();
        showPaneAdd();

        List<Trips> tripsList = loadTrips();
        setupSearchFunctionality(tripsList);

        loadTrips();


        setupCellValueFactoriesCity();
        setupDeleteColumnCity();
        setupViewColumnCity();
        List<Citys> citysList = loadCitys();
        CityTable.getItems().addAll(citysList);

    }

    public void reload() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/TransportMain.fxml"));
        try {
            AnchorPane newRoot = loader.load();

            root.getChildren().setAll(newRoot.getChildren());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleGoButton() {
        pane1.setVisible(true);
        pane2.setVisible(false);
    }

    @FXML
    private void handleRoundTripButton() {
        pane1.setVisible(false);
        pane2.setVisible(true);
    }

    private void setupCellValueFactories() {
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("TransportName"));
        DCColumn.setCellValueFactory(new PropertyValueFactory<>("DepartureCity"));
        ACColumn.setCellValueFactory(new PropertyValueFactory<>("ArrivalCity"));
        PrColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    private List<Trips> loadTrips() {
        List<Trips> tripsList = new ArrayList<>();
        try (Connection connection = ConnectDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT Tr_id, Transport_Name, Departure_City, Arrival_City, Price FROM trips");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Trips trip = new Trips();
                trip.setTrId(resultSet.getInt("Tr_id"));
                trip.setTransportName(resultSet.getString("Transport_Name"));
                trip.setDepartureCity(resultSet.getString("Departure_City"));
                trip.setArrivalCity(resultSet.getString("Arrival_City"));
                trip.setPrice(resultSet.getInt("Price"));
                tripsList.add(trip);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tripsList;
    }


    private List<String> getCities() {
        List<String> cityNames = new ArrayList<>();
        String query = "SELECT city_name FROM cities";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String cityName = resultSet.getString("city_name");
                cityNames.add(cityName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cityNames;
    }

    private void populateCityComboBoxes() {
        List<String> cityNames = getCities();
        departureCityComboBox.getItems().addAll(cityNames);
        arrivalCityComboBox.getItems().addAll(cityNames);
        departureCityComboBox1.getItems().addAll(cityNames);
        arrivalCityComboBox1.getItems().addAll(cityNames);
        updatedepartureCityComboBox.getItems().addAll(cityNames);
        updatearrivalCityComboBox.getItems().addAll(cityNames);

    }

    @FXML
    public void addTrip() {
        String transportName = TrNameTextField1.getText();
        String departureCity = departureCityComboBox1.getValue();
        String arrivalCity = arrivalCityComboBox1.getValue();

        LocalTime departureTime = parseTime(departureTimeTextField1.getText());
        if (departureTime == null) {
            showErrorAlert("Invalid Departure Time", "Please enter a valid time in HH:mm format for Departure Time.");
            return;
        }
        LocalTime arrivalTime = parseTime(arrivalTimeTextField1.getText());
        if (arrivalTime == null) {
            showErrorAlert("Invalid Arrival Time", "Please enter a valid time in HH:mm format for Arrival Time.");
            return;
        }
        String price = priceTextField1.getText();
        String day = dayComboBox1.getValue();

        if (departureCity == null || arrivalCity == null || price.isEmpty() || day == null || type1 == null) {
            showErrorAlert("Missing Information", "Please fill in all required fields.");
            return;
        }
        clearAllFields1();
        insertIntoTrips(transportName, departureCity, arrivalCity, departureTime, arrivalTime, price, day, type1);
    }

    @FXML
    public void addTrip_R() {
        String transportName = TrNameTextField.getText();
        String departureCity1 = departureCityComboBox.getValue();
        String arrivalCity1 = arrivalCityComboBox.getValue();
        LocalTime departureTime1 = parseTime(departureTimeTextField.getText());

        if (departureTime1 == null) {
            showErrorAlert("Invalid Departure Time", "Please enter a valid time in HH:mm format for Departure Time.");
            return;
        }
        LocalTime arrivalTime1 = parseTime(arrivalTimeTextField.getText());
        if (arrivalTime1 == null) {
            showErrorAlert("Invalid Arrival Time", "Please enter a valid time in HH:mm format for Arrival Time.");
            return;
        }
        String price1 = priceTextField.getText();
        String day1 = dayComboBox.getValue();

        String departureCity2 = arrivalCityComboBox.getValue();
        String arrivalCity2 = departureCityComboBox.getValue();

        LocalTime departureTime2 = parseTime(departureTimeTextFieldR.getText());
        if (departureTime2 == null) {
            showErrorAlert("Invalid Departure Time", "Please enter a valid time in HH:mm format for Departure Time.");
            return;
        }
        LocalTime arrivalTime2 = parseTime(arrivalTimeTextFieldR.getText());
        if (arrivalTime2 == null) {
            showErrorAlert("Invalid Arrival Time", "Please enter a valid time in HH:mm format for Arrival Time.");
            return;
        }
        String price2 = priceTextFieldR.getText();
        String day2 = dayComboBoxR.getValue();

        if (departureCity1 == null || arrivalCity1 == null || price1.isEmpty() || day1 == null || price2.isEmpty() || day2 == null || type == null) {
            showErrorAlert("Missing Information", "Please fill in all required fields.");
            return;
        }
        clearAllFields();
        insertIntoTrips(transportName, departureCity1, arrivalCity1, departureTime1, arrivalTime1, price1, day1, type);
        insertIntoTrips(transportName, departureCity2, arrivalCity2, departureTime2, arrivalTime2, price2, day2, type);
    }

    private void clearAllFields() {
        TrNameTextField.clear();
        departureCityComboBox.setValue(null);
        arrivalCityComboBox.setValue(null);
        departureTimeTextField.clear();
        arrivalTimeTextField.clear();
        priceTextField.clear();
        dayComboBox.setValue(null);
        PlaneCheckBox.setSelected(false);
        CarCheckBox.setSelected(false);
        TrainCheckBox.setSelected(false);
        departureTimeTextFieldR.clear();
        arrivalTimeTextFieldR.clear();
        priceTextFieldR.clear();
        dayComboBoxR.setValue(null);
    }
    private void clearAllFields1() {
        TrNameTextField1.clear();
        departureCityComboBox1.setValue(null);
        arrivalCityComboBox1.setValue(null);
        departureTimeTextField1.clear();
        arrivalTimeTextField1.clear();
        priceTextField1.clear();
        dayComboBox1.setValue(null);
        PlaneCheckBox1.setSelected(false);
        CarCheckBox1.setSelected(false);
        TrainCheckBox1.setSelected(false);
    }
    @FXML
    private void handleCheckBoxAction(ActionEvent event) {
        if (PlaneCheckBox.isSelected()) {
            CarCheckBox.setSelected(false);
            TrainCheckBox.setSelected(false);
            type = "Plane";
        } else if (CarCheckBox.isSelected()) {
            PlaneCheckBox.setSelected(false);
            TrainCheckBox.setSelected(false);
            type = "Car";
        } else if (TrainCheckBox.isSelected()) {
            PlaneCheckBox.setSelected(false);
            CarCheckBox.setSelected(false);
            type = "Train";
        } else {
            PlaneCheckBox.setSelected(false);
            CarCheckBox.setSelected(false);
            TrainCheckBox.setSelected(false);
            type = null;
        }
    }

    @FXML
    private void handleCheckBoxAction1(ActionEvent event) {
        if (PlaneCheckBox1.isSelected()) {
            CarCheckBox1.setSelected(false);
            TrainCheckBox1.setSelected(false);
            type1 = "Plane";
        } else if (CarCheckBox1.isSelected()) {
            PlaneCheckBox1.setSelected(false);
            TrainCheckBox1.setSelected(false);
            type1 = "Car";
        } else if (TrainCheckBox1.isSelected()) {
            PlaneCheckBox1.setSelected(false);
            CarCheckBox1.setSelected(false);
            type1 = "Train";
        } else {
            PlaneCheckBox1.setSelected(false);
            CarCheckBox1.setSelected(false);
            TrainCheckBox1.setSelected(false);
            type1 = null;
        }
    }


    private LocalTime parseTime(String timeText) {
        try {
            return LocalTime.parse(timeText, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void insertIntoTrips(String transportName, String departureCity, String arrivalCity, LocalTime departureTime, LocalTime arrivalTime, String price, String day, String type) {
        String insertQuery = "INSERT INTO Trips (Transport_Name, Departure_City, Arrival_City, Departure_Time, Arrival_Time, Price, Days, Type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, transportName);
            preparedStatement.setString(2, departureCity);
            preparedStatement.setString(3, arrivalCity);
            preparedStatement.setTime(4, Time.valueOf(departureTime));
            preparedStatement.setTime(5, Time.valueOf(arrivalTime));
            preparedStatement.setBigDecimal(6, new BigDecimal(price));
            preparedStatement.setString(7, day);
            preparedStatement.setString(8, type);

            preparedStatement.executeUpdate();
            reload();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* View */
    private void setupViewColumn() {
        viewColumn.setCellFactory(param -> new TableCell<>() {
            private ImageView imageView;
            {
                Image viewIcon = new Image(getClass().getResourceAsStream("../Resources/icons/eye.png"));
                imageView = new ImageView(viewIcon);
                imageView.setFitWidth(20);
                imageView.setFitHeight(20);
                setGraphic(imageView);
                setOnMouseClicked(event -> {
                    if (event.getButton().equals(MouseButton.PRIMARY)) {
                        Trips selectedTrips = getTableView().getItems().get(getIndex());
                        showViewPane();
                        showDetailedInformation(selectedTrips);
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                }
                else {
                    setGraphic(imageView);
                }
            }
        });
    }

    private void showDetailedInformation(Trips trips) {
        try (Connection connection = ConnectDatabase.getConnection()) {
            try (PreparedStatement tripsStatement = connection.prepareStatement(
                    "SELECT Transport_Name, Departure_City, Arrival_City, Departure_Time, Arrival_Time, Price , Days FROM trips WHERE Tr_id = ?")) {
                tripsStatement.setInt(1, trips.getTrId());
                ResultSet tripsResult = tripsStatement.executeQuery();
                if (tripsResult.next()) {
                    detailedNameLabel.setText(tripsResult.getString("Transport_Name"));
                    detailedDepCityLabel.setText(tripsResult.getString("Departure_City"));
                    detailedArrCityLabel.setText(String.valueOf(tripsResult.getString("Arrival_City")));
                    detailedDepTimeLabel.setText(String.valueOf(tripsResult.getString("Departure_Time")));
                    detailedArrTimeLabel.setText(String.valueOf(tripsResult.getString("Arrival_Time")));
                    detailedPriceLabel.setText(String.valueOf(tripsResult.getString("Price")));
                    detailedDayLabel.setText(String.valueOf(tripsResult.getString("Days")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /* End View */



    /* Delete */
    private void setupDeleteColumn() {
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final ImageView deleteIconView;

            {
                Image deleteIcon = new Image(getClass().getResourceAsStream("../Resources/icons/delete.png"));
                deleteIconView = new ImageView(deleteIcon);
                deleteIconView.setFitWidth(20);
                deleteIconView.setFitHeight(20);

                setGraphic(deleteIconView);

                setOnMouseClicked(event -> {
                    if (event.getButton().equals(MouseButton.PRIMARY)) {
                        Trips selectedtrip = getTableView().getItems().get(getIndex());
                        boolean confirmed = showConfirmationDialog(selectedtrip);
                        if (confirmed) {
                            deleteTrip(selectedtrip);
                            reload();
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteIconView);
                }
            }
        });
    }

    private boolean showConfirmationDialog(Trips trip) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete " + trip.getTransportName() + "?");
        ButtonType okButton = new ButtonType("OK");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonType.CANCEL.getButtonData());
        alert.getButtonTypes().setAll(okButton, cancelButton);
        alert.showAndWait();
        return alert.getResult() == okButton;
    }

    private void deleteTrip(Trips trip) {
        try (Connection connection = ConnectDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM trips WHERE Tr_Id = ?")) {
            statement.setInt(1, trip.getTrId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                loadTrips();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /* End delete */


    /* update */
    @FXML
    private void handleUpdateTrip() {
        if (selectedTripForUpdate != null) {
            String newName = updateTrNameTextField.getText();
            String newDC = updatedepartureCityComboBox.getValue();
            String newAC = updatearrivalCityComboBox.getValue();
            LocalTime newDT = parseTime(updatedepartureTimeTextField.getText());
            LocalTime newAT = parseTime(updatearrivalTimeTextField.getText());
            String newPr = updatepriceTextField.getText();
            String newDy = updatedayComboBox.getValue();

            if (newName.isEmpty() || newDC == null || newAC == null || newDT == null || newAT == null || newPr.isEmpty() || newDy == null)  {
                showErrorAlert("Missing Information", "Please fill in all required fields.");
                return;
            }


            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                         "UPDATE trips SET Transport_Name = ?, Departure_City = ?, Arrival_City = ?, Departure_Time = ?, Arrival_Time = ?, Price = ?, Days = ? WHERE Tr_Id = ?")) {
                statement.setString(1, newName);
                statement.setString(2, newDC);
                statement.setString(3, newAC);
                statement.setTime(4, Time.valueOf(newDT));
                statement.setTime(5, Time.valueOf(newAT));
                statement.setBigDecimal(6, new BigDecimal(newPr));
                statement.setString(7, newDy);
                statement.setInt(8, selectedTripForUpdate.getTrId());

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    loadTrips();
                    clearUpdateForm();
                    updatePane.setVisible(false);
                    TransportTable.refresh();
                } else {
                    System.out.println("No rows were updated.");
                }
                reload();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    @FXML
    private void updateTripInformation(Trips trips) {
        initializeUpdateTripForm(trips);
        updatePane.setVisible(true);
    }

    private void clearUpdateForm() {
        updateTrNameTextField.clear();
        updatedepartureCityComboBox.getSelectionModel().clearSelection();
        updatearrivalCityComboBox.getSelectionModel().clearSelection();
        updatedepartureTimeTextField.clear();
        updatearrivalTimeTextField.clear();
        updatepriceTextField.clear();
        updatedayComboBox.getSelectionModel().clearSelection();
        updatePane.setVisible(true);
    }

    private void setupUpdateColumn() {
        updateColumn.setCellFactory(param -> new TableCell<>() {
            private ImageView imageView;
            {
                Image updateIcon = new Image(getClass().getResourceAsStream("../Resources/icons/loading-arrow.png"));
                imageView = new ImageView(updateIcon);
                imageView.setFitWidth(20);
                imageView.setFitHeight(20);
                setGraphic(imageView);
                setOnMouseClicked(event -> {
                    if (event.getButton().equals(MouseButton.PRIMARY)) {
                        Trips selectedTrip = getTableView().getItems().get(getIndex());
                        showUpdatePane();
                        selectedTripForUpdate = selectedTrip;
                        initializeUpdateTripForm(selectedTrip);
                        updateTripInformation(selectedTrip);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                }
                else {
                    setGraphic(imageView);
                }
            }
        });
    }

    @FXML
    private void initializeUpdateTripForm(Trips trips) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        updateTrNameTextField.setText(trips.getTransportName());
        updatedepartureCityComboBox.setValue(trips.getDepartureCity());
        updatearrivalCityComboBox.setValue(trips.getArrivalCity());
        LocalTime departureTime = trips.getDepartureTime();
        if (departureTime != null) {
            updatedepartureTimeTextField.setText(departureTime.format(timeFormatter));
        } else {
            updatedepartureTimeTextField.setText("");
        }
        LocalTime arrivalTime = trips.getArrivalTime();
        if (arrivalTime != null) {
            updatearrivalTimeTextField.setText(arrivalTime.format(timeFormatter));
        } else {
            updatearrivalTimeTextField.setText("");
        }
        updatepriceTextField.setText(String.valueOf(trips.getPrice()));
        updatedayComboBox.setValue(trips.getDay());
    }

    /* End update */

    /* Controle Pane  */

    private void showPaneAdd(){
        foradd.setOnMouseClicked(event -> {
            showAddPane();
        });
    }
    private void showAddPane() {
        foradd.setStyle("-fx-background-color: #a19797;");
        addPane.setVisible(true);
        updatePane.setVisible(false);
        viewPane.setVisible(false);
        forview.setStyle("-fx-background-color: #fff;");
        forupdate.setStyle("-fx-background-color: #fff;");
    }
    private void showViewPane() {
        addPane.setVisible(false);
        updatePane.setVisible(false);
        viewPane.setVisible(true);
        forview.setStyle("-fx-background-color: #a19797;");
        foradd.setStyle("-fx-background-color: #fff;");
        forupdate.setStyle("-fx-background-color: #fff;");

    }
    private void showUpdatePane() {
        addPane.setVisible(false);
        updatePane.setVisible(true);
        viewPane.setVisible(false);
        forupdate.setStyle("-fx-background-color: #a19797;");
        foradd.setStyle("-fx-background-color: #fff;");
        forview.setStyle("-fx-background-color: #fff;");
    }

    /* End Controle Pane */


    /* search */
    private void setupSearchFunctionality(List<Trips> tripsList) {
        FilteredList<Trips> filteredData = new FilteredList<>(FXCollections.observableArrayList(tripsList), p -> true);
        SortedList<Trips> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(TransportTable.comparatorProperty());
        TransportTable.setItems(sortedData);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(data -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return data.getTransportName().toLowerCase().contains(lowerCaseFilter)
                        || data.getDepartureCity().toLowerCase().contains(lowerCaseFilter)
                        || data.getArrivalCity().toLowerCase().contains(lowerCaseFilter)
                        || String.valueOf(data.getPrice()).toLowerCase().contains(lowerCaseFilter);
            });
        });
    }

    /* End search */

    /* City */


    private void setupCellValueFactoriesCity() {
        NameCityColumn.setCellValueFactory(new PropertyValueFactory<>("CityName"));
    }

    private List<Citys> loadCitys() {
        List<Citys> citysList = new ArrayList<>();
        try (Connection connection = ConnectDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT id, city_name FROM cities");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Citys city = new Citys();
                city.setCityId(resultSet.getInt("id"));
                city.setCityName(resultSet.getString("city_name"));
                citysList.add(city);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return citysList;
    }

    public void addCity(ActionEvent actionEvent) {
        String cityName = CityNameTextField.getText();
        if (cityName.isEmpty()) {
            showErrorAlert("Missing Information", "Please fill in all required fields.");
            return;
        }
        CityNameTextField.clear();
        insertIntoCitys(cityName);
        reload();
    }
    private void insertIntoCitys(String cityName) {
        String insertQuery = "INSERT INTO cities (city_name) VALUES (?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, cityName);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void deleteCity(Citys city) {
        try (Connection connection = ConnectDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM cities WHERE id = ?")) {
            statement.setInt(1, city.getCityId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                loadTrips();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupDeleteColumnCity() {
        deleteCityColumn.setCellFactory(param -> new TableCell<>() {
            private final ImageView deleteCityIconView;
            {
                Image deleteIcon = new Image(getClass().getResourceAsStream("../Resources/icons/delete.png"));
                deleteCityIconView = new ImageView(deleteIcon);
                deleteCityIconView.setFitWidth(20);
                deleteCityIconView.setFitHeight(20);
                setGraphic(deleteCityIconView);

                setOnMouseClicked(event -> {
                    if (event.getButton().equals(MouseButton.PRIMARY)) {
                        Citys selectedcity = getTableView().getItems().get(getIndex());
                        boolean confirmed = showConfirmationDialogCity(selectedcity);
                        if (confirmed) {
                            deleteCity(selectedcity);
                            reload();
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteCityIconView);
                }
            }
        });
    }

    private boolean showConfirmationDialogCity(Citys city) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete " + city.getCityName() + "?");
        ButtonType okButton = new ButtonType("OK");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonType.CANCEL.getButtonData());
        alert.getButtonTypes().setAll(okButton, cancelButton);
        alert.showAndWait();
        return alert.getResult() == okButton;
    }

    private void setupViewColumnCity() {
        viewCityColumn.setCellFactory(param -> new TableCell<>() {
            {
                Image viewIcon = new Image(getClass().getResourceAsStream("../Resources/icons/eye.png"));
                ImageView imageView = new ImageView(viewIcon);
                imageView.setFitWidth(20);
                imageView.setFitHeight(20);
                setGraphic(imageView);
                setOnMouseClicked(event -> {
                    if (!isEmpty()) {
                        Citys selectedCity = getTableView().getItems().get(getIndex());
                        showStatisticPane();
                        showDetailedCityInformation(selectedCity);
                        updateBarChart(selectedCity);
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                }
            }
        });
    }

    private void showDetailedCityInformation(Citys citys) {
        detailedCityNameLabel.setText(citys.getCityName());
    }

    @FXML
    private void handleAddCityClick(MouseEvent event) {
        showAddCityPane();
        event.consume();
    }
    private void showAddCityPane() {
        addCityPane.setVisible(true);
        staticCityPane.setVisible(false);
    }
    private void showStatisticPane(){
        addCityPane.setVisible(false);
        staticCityPane.setVisible(true);
    }


    public void updateBarChart(Citys city) {
        BarChart.Series<String, Number> series = new BarChart.Series<>();
        series.setName("Number of Users");

        String query = "SELECT Type, COUNT(*) as Count FROM trips WHERE Arrival_City = ? OR Departure_City = ? GROUP BY Type";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, city.getCityName());
            preparedStatement.setString(2, city.getCityName());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String type = resultSet.getString("Type");
                    int count = resultSet.getInt("Count");
                    series.getData().add(new XYChart.Data<>(type, count));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        barChart.getData().clear();
        barChart.getData().add(series);
        barChart.setTitle("Transportation Usage Over the Last Week");
    }
    /* End City */


}