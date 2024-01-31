package Admin.Controller;


import Admin.Model.User;
import Admin.Resources.ConnectDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Admin.Resources.ConnectDatabase.getConnection;


public class CustomerController {

    @FXML
    private TextField searchTextField;

    @FXML
    private TableView<User> UserTable;
    @FXML
    private TableColumn<User, Integer> IdColumn;
    @FXML
    private TableColumn<User, String> UsernameColumn;
    @FXML
    private TableColumn<User, String> PasswordColumn;
    @FXML
    private TableColumn<User, String> EmailColumn;
    @FXML
    private TableColumn<User, Void> deleteColumn;
    @FXML
    private TableColumn<User, Void> viewColumn;
    @FXML
    private TableColumn<User, Void> updateColumn;

    @FXML
    private AnchorPane detailedInfoPane;
    @FXML
    private Label detailedNameLabel;
    @FXML
    private Label detailedPasswordLabel;
    @FXML
    private Label detailedEmailLabel;

    private User selectedUserForUpdate;
    @FXML
    private TextField updateUserNameTextField;
    @FXML
    private TextField updatePasswordTextField;
    @FXML
    private TextField updateEmailTextField;

    @FXML
    private AnchorPane updatePane;
    @FXML
    private AnchorPane addPane;
    @FXML
    private AnchorPane viewPane;
    @FXML
    private AnchorPane forupdate;
    @FXML
    private AnchorPane forview;

    @FXML
    private PieChart pieChart;
    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private AnchorPane root;



    @FXML
    public void initialize() {
        setupCellValueFactories();
        setupViewColumn();
        setupDeleteColumn();
        setupUpdateColumn();

        List<User> usersList = loadUsers();
        setupSearchFunctionality(usersList);
        loadUsers();

        updatePieChart();
        updateBarChart();
    }


    public void reload() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/CustomerMain.fxml"));
        try {
            AnchorPane newRoot = loader.load();

            root.getChildren().setAll(newRoot.getChildren());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void setupCellValueFactories() {
        UsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        PasswordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        EmailColumn.setCellValueFactory(new PropertyValueFactory<>("Email"));
    }
    private List<User> loadUsers() {
        List<User> usersList = new ArrayList<>();
        try (Connection connection = ConnectDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT id, username, password, Email FROM users");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setEmail(resultSet.getString("Email"));
                usersList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usersList;
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
                        User selectedUsers = getTableView().getItems().get(getIndex());
                        showViewPane();
                        showDetailedInformation(selectedUsers);
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

    private void showDetailedInformation(User user) {
        detailedNameLabel.setText(user.getUsername());
        detailedPasswordLabel.setText(user.getPassword());
        detailedEmailLabel.setText(user.getEmail());
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
                        User selecteduser = getTableView().getItems().get(getIndex());
                        boolean confirmed = showConfirmationDialog(selecteduser);
                        if (confirmed) {
                            deleteUser(selecteduser);
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

    private boolean showConfirmationDialog(User user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete " + user.getUsername() + "?");
        ButtonType okButton = new ButtonType("OK");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonType.CANCEL.getButtonData());
        alert.getButtonTypes().setAll(okButton, cancelButton);
        alert.showAndWait();
        return alert.getResult() == okButton;
    }

    private void deleteUser(User user) {
        try (Connection connection = ConnectDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM users WHERE id = ?")) {
            statement.setInt(1, user.getId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                loadUsers();
                reload();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* End delete */


    /* update */
    @FXML
    private void handleUpdateUser() {
        if (selectedUserForUpdate != null) {
            String newName = updateUserNameTextField.getText();
            String newPs = updatePasswordTextField.getText();
            String newEm = updateEmailTextField.getText();

            if (newName.isEmpty() || newEm.isEmpty() || newPs.isEmpty())  {
                return;
            }
            try (Connection connection = ConnectDatabase.getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                         "UPDATE users SET username = ?, password = ?, Email = ? WHERE id = ?")) {
                statement.setString(1, newName);
                statement.setString(2, newPs);
                statement.setString(3, newEm);
                statement.setInt(4, selectedUserForUpdate.getId());

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    loadUsers();
                    clearUpdateForm();
                    updatePane.setVisible(false);
                    UserTable.refresh();
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
    private void updateUserInformation(User user) {
        initializeUpdateUserForm(user);
        updatePane.setVisible(true);
    }

    private void clearUpdateForm() {
        updateUserNameTextField.clear();
        updateEmailTextField.clear();
        updatePasswordTextField.clear();
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
                        User selecteduser = getTableView().getItems().get(getIndex());
                        showUpdatePane();
                        selectedUserForUpdate = selecteduser;
                        initializeUpdateUserForm(selecteduser);
                        updateUserInformation(selecteduser);
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
    private void initializeUpdateUserForm(User user) {
        updateUserNameTextField.setText(user.getUsername());
        updatePasswordTextField.setText(user.getPassword());
        updateEmailTextField.setText(user.getEmail());
    }

    /* End update */

    /* Controle Pane  */

    private void showViewPane() {
        addPane.setVisible(false);
        updatePane.setVisible(false);
        viewPane.setVisible(true);
        forview.setStyle("-fx-background-color: #a19797;");
        forupdate.setStyle("-fx-background-color: #fff;");

    }
    private void showUpdatePane() {
        addPane.setVisible(false);
        updatePane.setVisible(true);
        viewPane.setVisible(false);
        forupdate.setStyle("-fx-background-color: #a19797;");
        forview.setStyle("-fx-background-color: #fff;");
    }

    /* End Controle Pane */


    /* search */
    private void setupSearchFunctionality(List<User> userList) {
        FilteredList<User> filteredData = new FilteredList<>(FXCollections.observableArrayList(userList), p -> true);
        SortedList<User> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(UserTable.comparatorProperty());
        UserTable.setItems(sortedData);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(data -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return data.getUsername().toLowerCase().contains(lowerCaseFilter)
                        || data.getPassword().toLowerCase().contains(lowerCaseFilter)
                        || data.getEmail().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }
    /* End search */

    /* Statistic */

    public void updatePieChart() {
        // Retrieve the counts of transport types from the database
        Map<String, Integer> transportCounts = calculateNumberOfTransportsByType();

        // Create an ObservableList to hold the PieChart data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Add data to the ObservableList based on the counts obtained
        for (Map.Entry<String, Integer> entry : transportCounts.entrySet()) {
            String type = entry.getKey();
            int count = entry.getValue();
            pieChartData.add(new PieChart.Data(type, count));
        }

        // Update the PieChart with the new data
        pieChart.setData(pieChartData);
        pieChart.setTitle("User / Type of Transport");

        // Set styles for each category
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
                // Add more cases if needed for other transport types
            }
        }
    }
    private Map<String, Integer> calculateNumberOfTransportsByType() {
        Map<String, Integer> transportCounts = new HashMap<>();
        try (Connection connection = ConnectDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT Type, COUNT(DISTINCT Transport_Name) AS NumberOfTransports FROM trips GROUP BY Type");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String type = resultSet.getString("Type");
                int numberOfTransports = resultSet.getInt("NumberOfTransports");
                transportCounts.put(type, numberOfTransports);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transportCounts;
    }

    public void updateBarChart() {

        Map<String, Integer> newUserCountMap = calculateNewUsersByMonth();
        BarChart.Series<String, Number> series = new BarChart.Series<>();
        series.setName("Number of Users");
        for (Map.Entry<String, Integer> entry : newUserCountMap.entrySet()) {
            series.getData().add(new BarChart.Data<>(entry.getKey(), entry.getValue()));
        }
        barChart.getData().clear();
        barChart.getData().add(series);

        barChart.setTitle("Number of Users in Month");

        for (XYChart.Data<String, Number> data : series.getData()) {
            Node node = data.getNode();
            if (node != null) {
                String style = "-fx-bar-fill: #3498db;";
                node.setStyle(style);
            }
        }
    }

    private static int getDaysDifference(Connection connection) throws SQLException {
        String query = "SELECT DATEDIFF(MAX(created_at), MIN(created_at)) AS dayDifference FROM users";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("dayDifference");
            }
        }
        return -1;
    }

    public static Map<String, Integer> calculateNewUsersByMonth() {
        Map<String, Integer> newUserCountMap = new HashMap<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT MONTHNAME(created_at) AS month_name, COUNT(*) AS user_count " +
                             "FROM users " +
                             "GROUP BY month_name"
             );
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String monthName = resultSet.getString("month_name");
                int userCount = resultSet.getInt("user_count");

                newUserCountMap.put(monthName, userCount);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return newUserCountMap;
    }



    /* End Statistic */
}