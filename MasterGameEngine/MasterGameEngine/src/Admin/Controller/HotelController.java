package Admin.Controller;

import Admin.Model.Hotel;
import Admin.Model.HotelsClasse.Images;
import static Admin.Resources.ConnectDatabase.getConnection;
import Admin.Resources.ConnectDatabase;

import Controller.ValidationController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HotelController {

    @FXML
    private TextField searchTextField;

    @FXML
    private TableView<Hotel> HotelTable;
    @FXML
    private TableColumn<Hotel, Integer> IdColumn;
    @FXML
    private TableColumn<Hotel, String> NameColumn;
    @FXML
    private TableColumn<Hotel, String> CityColumn;
    @FXML
    private TableColumn<Hotel, Integer> StarsColumn;
    @FXML
    private TableColumn<Hotel, Integer> PriceColumn;
    @FXML
    private TableColumn<Hotel, Void> viewColumn;
    @FXML
    private TableColumn<Hotel, Void> deleteColumn;

    @FXML
    private TextField AddHotelName;
    @FXML
    private ComboBox<String> AddHotelCity;
    @FXML
    private TextField AddHotelStars;
    @FXML
    private TextField AddHotelPrice;
    @FXML
    private TextField AddHotelNRS;
    @FXML
    private TextField AddHotelPSR;
    @FXML
    private TextField AddHotelNRD;
    @FXML
    private TextField AddHotelPDR;
    @FXML
    private CheckBox RSimpleCheckBox;
    @FXML
    private CheckBox RDoubleCheckBox;
    @FXML
    private CheckBox MRCheckBox;
    @FXML
    private CheckBox WifiCheckBox;
    @FXML
    private CheckBox SpaCheckBox;

    private List<String> selectedCheckBoxes = new ArrayList<>();

    @FXML
    private TableView<Images> ImageTable;
    @FXML
    private TableColumn<Images, String> pathImageColumn;
    @FXML
    private TableColumn<Images, Void> deleteImageColumn;
    @FXML
    private Button uploadButton;

    private ObservableList<Images> imagePathList = FXCollections.observableArrayList();
    private List<String> globalImagePaths = new ArrayList<>();


    @FXML
    private AnchorPane detailedInfoPane;
    @FXML
    private Label NameLabel;
    @FXML
    private Label CityLabel;
    @FXML
    private Label StarsLabel;
    @FXML
    private Label NRSLabel;
    @FXML
    private Label PRSLabel;
    @FXML
    private Label NRSBLabel;
    @FXML
    private Label NRDLabel;
    @FXML
    private Label PRDLabel;
    @FXML
    private Label NRDBLabel;
    @FXML
    private Pane MRPane;
    @FXML
    private Pane WifiPane;
    @FXML
    private Pane SpaPane;
    @FXML
    private ImageView imageView;

    @FXML
    private AnchorPane root;

    @FXML
    public void initialize() {
        populateCityComboBoxes();
        setupCellValueFactories();
        setupViewColumn();
        setupDeleteColumn();

        List<Hotel> hotelsList = loadHotel();
        setupSearchFunctionality(hotelsList);
        loadHotel();

        setupImages();

    }

    public void reload() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/HotelMain.fxml"));
        try {
            AnchorPane newRoot = loader.load();

            root.getChildren().setAll(newRoot.getChildren());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupCellValueFactories() {
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        CityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        StarsColumn.setCellValueFactory(new PropertyValueFactory<>("nb_stars"));
        PriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
    private List<Hotel> loadHotel() {
        List<Hotel> hotelslist = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT id, city, name, nb_stars, price FROM hotel");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Hotel hotel = new Hotel();
                hotel.setId(resultSet.getInt("id"));
                hotel.setCity(resultSet.getString("city"));
                hotel.setName(resultSet.getString("name"));
                hotel.setNb_stars(resultSet.getInt("nb_stars"));
                hotel.setPrice(resultSet.getInt("price"));
                hotelslist.add(hotel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotelslist;
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
    private void populateCityComboBoxes(){
        List<String> cityNames = getCities();
        AddHotelCity.getItems().addAll(cityNames);
    }

    /* Add Hotel */

    @FXML
    public void handleCheckBoxAction() {
        selectedCheckBoxes = getCheckedCheckBoxes(RSimpleCheckBox, RDoubleCheckBox, MRCheckBox, WifiCheckBox, SpaCheckBox);
        setDisable(selectedCheckBoxes);
    }

    public List<String> getCheckedCheckBoxes(CheckBox... checkBoxes) {
        List<String> checkedCheckBoxIds = new ArrayList<>();
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected()) {
                checkedCheckBoxIds.add(checkBox.getId());
            }
        }
        return checkedCheckBoxIds;
    }

    public void setDisable(List<String> TouchedBox) {
        if (TouchedBox.contains("RSimpleCheckBox")) {
            AddHotelNRS.setDisable(false);
            AddHotelPSR.setDisable(false);
        } else {
            AddHotelNRS.setDisable(true);
            AddHotelPSR.setDisable(true);
        }

        if (TouchedBox.contains("RDoubleCheckBox")) {
            AddHotelNRD.setDisable(false);
            AddHotelPDR.setDisable(false);
        } else {
            AddHotelNRD.setDisable(true);
            AddHotelPDR.setDisable(true);
        }
    }

    public void handleAddHotel() {
        String hotelName = AddHotelName.getText();
        String hotelCity = AddHotelCity.getValue();
        String hotelPrice = AddHotelPrice.getText();
        String hotelStars = AddHotelStars.getText();

        String hotelNRS = AddHotelNRS.getText();
        String hotelPSR = AddHotelPSR.getText();

        String hotelNRD = AddHotelNRD.getText();
        String hotelPDR = AddHotelPDR.getText();

        if (hotelCity == null || hotelName.isEmpty() || hotelPrice.isEmpty() || hotelStars.isEmpty()) {
            showErrorAlert("Missing Information", "Please fill in all required fields.");
            return;
        }

        boolean simpleTouched = selectedCheckBoxes.contains("RSimpleCheckBox");
        boolean doubleTouched = selectedCheckBoxes.contains("RDoubleCheckBox");

        if (!simpleTouched) {
            hotelNRS = null;
            hotelPSR = null;
        } else {
            if (hotelNRS.isEmpty() || hotelPSR.isEmpty()) {
                showErrorAlert("Missing Information", "Please fill in all required fields.");
                return;
            }
        }

        if (!doubleTouched) {
            hotelNRD = null;
            hotelPDR = null;
        } else {
            if (hotelNRD.isEmpty() || hotelPDR.isEmpty()) {
                showErrorAlert("Missing Information", "Please fill in all required fields.");
                return;
            }
        }

        boolean MRTouched = selectedCheckBoxes.contains("MRCheckBox");
        boolean WifiTouched = selectedCheckBoxes.contains("WifiCheckBox");
        boolean SpaTouched = selectedCheckBoxes.contains("SpaCheckBox");

        int HotelId = 1000;

        if (!simpleTouched && !doubleTouched){
            showErrorAlert("Missing Information", "Please choose rooms type.");
            return;
        }
        else if(simpleTouched && doubleTouched){
            HotelId = insertIntoHotel(hotelName, hotelCity, Integer.valueOf(hotelStars), Integer.valueOf(hotelPrice), Integer.valueOf(hotelNRS), Integer.valueOf(hotelNRD));
            insertIntoRoom_price(HotelId, 1, Integer.valueOf(hotelPSR));
            for (int i = 0; i < Integer.valueOf(hotelNRS); i++){
                insertIntoReservation(HotelId, 1);
            }
            insertIntoRoom_price(HotelId, 2, Integer.valueOf(hotelPDR));
            for (int i = 0; i < Integer.valueOf(hotelNRD); i++){
                insertIntoReservation(HotelId, 2);
            }
        }
        else if (simpleTouched && !doubleTouched){
            HotelId = insertIntoHotelRS(hotelName, hotelCity, Integer.valueOf(hotelStars), Integer.valueOf(hotelPrice), Integer.valueOf(hotelNRS));
            insertIntoRoom_price(HotelId, 1, Integer.valueOf(hotelPSR));
            for (int i = 0; i < Integer.valueOf(hotelNRS); i++){
                insertIntoReservation(HotelId, 1);
            }
        }
        else if (doubleTouched && !simpleTouched){
            HotelId = insertIntoHotelRD(hotelName, hotelCity, Integer.valueOf(hotelStars), Integer.valueOf(hotelPrice), Integer.valueOf(hotelNRD));
            insertIntoRoom_price(HotelId, 2, Integer.valueOf(hotelPDR));
            for (int i = 0; i < Integer.valueOf(hotelNRD); i++){
                insertIntoReservation(HotelId, 2);
            }
        }

        if (MRTouched){
            insertIntoHotel_icon(HotelId, 1);
        }
        if (SpaTouched){
            insertIntoHotel_icon(HotelId, 2);
        }
        if (WifiTouched){
            insertIntoHotel_icon(HotelId, 3);
        }

        insertIntoImage(HotelId, globalImagePaths);

        clearInputFields();
        reload();
    }

    private int insertIntoHotel(String HotelName, String HotelCity, Integer HotelStars, Integer HotelPrice, Integer HotelNRS, Integer HotelNRD) {
        String insertQuery = "INSERT INTO hotel (city, name, nb_room_s, nb_room_d, nb_stars, price) VALUES (?, ?, ?, ?, ?, ?)";
        int generatedId = -1;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, HotelCity);
            preparedStatement.setString(2, HotelName);
            preparedStatement.setInt(3, HotelNRS);
            preparedStatement.setInt(4, HotelNRD);
            preparedStatement.setInt(5, HotelStars);
            preparedStatement.setInt(6, HotelPrice);

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                generatedId = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedId;
    }

    private int insertIntoHotelRS(String HotelName, String HotelCity, Integer HotelStars, Integer HotelPrice, Integer HotelNRS) {
        String insertQuery = "INSERT INTO hotel (city, name, nb_room_s, nb_stars, price) VALUES (?, ?, ?, ?, ?)";
        int generatedId = -1;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, HotelCity);
            preparedStatement.setString(2, HotelName);
            preparedStatement.setInt(3, HotelNRS);
            preparedStatement.setInt(4, HotelStars);
            preparedStatement.setInt(5, HotelPrice);

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                generatedId = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedId;
    }

    private int insertIntoHotelRD(String HotelName, String HotelCity, Integer HotelStars, Integer HotelPrice, Integer HotelNRD) {
        String insertQuery = "INSERT INTO hotel (city, name, nb_room_d, nb_stars, price) VALUES (?, ?, ?, ?, ?)";
        int generatedId = -1;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, HotelCity);
            preparedStatement.setString(2, HotelName);
            preparedStatement.setInt(3, HotelNRD);
            preparedStatement.setInt(4, HotelStars);
            preparedStatement.setInt(5, HotelPrice);

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                generatedId = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedId;
    }

    private void insertIntoHotel_icon(Integer hotelId, Integer iconId) {
        String insertQuery = "INSERT INTO hotel_icon (hotel_id, icon_id) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, hotelId);
            preparedStatement.setInt(2, iconId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertIntoRoom_price(Integer hotelId,Integer roomId, Integer price){
        String insertQuery = "INSERT INTO room_price (hotel_id, room_id, price) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, hotelId);
            preparedStatement.setInt(2, roomId);
            preparedStatement.setInt(3, price);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void  insertIntoReservation(Integer hotelId,Integer roomId){
        String insertQuery = "INSERT INTO reservation (hotel_id, room_id) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, hotelId);
            preparedStatement.setInt(2, roomId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearInputFields() {
        AddHotelName.clear();
        AddHotelCity.getSelectionModel().clearSelection();
        AddHotelPrice.clear();
        AddHotelStars.clear();
        AddHotelNRS.clear();
        AddHotelPSR.clear();
        AddHotelNRD.clear();
        AddHotelPDR.clear();
        RSimpleCheckBox.setSelected(false);
        RDoubleCheckBox.setSelected(false);
        MRCheckBox.setSelected(false);
        WifiCheckBox.setSelected(false);
        SpaCheckBox.setSelected(false);
        ImageTable.getItems().clear();
    }

    /* Image Add part */
    private void setupImages(){
        pathImageColumn.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));
        ImageTable.setItems(imagePathList);
        setupDeleteImageColumn();
    }

    @FXML
    private void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image Files");
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(imageView.getScene().getWindow());

        if (selectedFiles != null && !selectedFiles.isEmpty()) {

            for (File file : selectedFiles) {
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);

                String imagePath = file.getAbsolutePath();
                imagePathList.add(new Images(1, imagePath));
                globalImagePaths.add(imagePath);
            }
        }
    }

    private void setupDeleteImageColumn() {
        deleteImageColumn.setCellFactory(param -> new TableCell<>() {
            private final ImageView deleteIconView;
            {
                Image deleteIcon = new Image(getClass().getResourceAsStream("../Resources/icons/delete.png"));
                deleteIconView = new ImageView(deleteIcon);
                deleteIconView.setFitWidth(20);
                deleteIconView.setFitHeight(20);
                setGraphic(deleteIconView);
                setOnMouseClicked(event -> {
                    if (event.getButton().equals(MouseButton.PRIMARY)) {
                        Images selectedImage = getTableView().getItems().get(getIndex());

                        imagePathList.remove(selectedImage);
                        globalImagePaths.remove(selectedImage.getImageUrl());

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

    private byte[] convertImageToByteArray(String imagePath) {
        try {
            File file = new File(imagePath);
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<byte[]> convertImagesToByteArrays(List<String> imagePaths) {
        List<byte[]> imageBytesList = new ArrayList<>();
        for (String imagePath : imagePaths) {
            byte[] imageBytes = convertImageToByteArray(imagePath);
            if (imageBytes != null) {
                imageBytesList.add(imageBytes);
            }
        }
        return imageBytesList;
    }

    private void insertIntoImage(Integer hotelId, List<String> imagePaths) {
        String insertQuery = "INSERT INTO images (hotel_id, image) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            List<byte[]> imageBytesList = convertImagesToByteArrays(imagePaths);

            for (byte[] imageBytes : imageBytesList) {
                preparedStatement.setInt(1, hotelId);
                preparedStatement.setBytes(2, imageBytes);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* End Image Add part */
    /* End Add Hotel */

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
                        Hotel selectedHotel = getTableView().getItems().get(getIndex());
                        showDetailedInformation(selectedHotel);
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

    private void showDetailedInformation(Hotel hotel) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement hotelStatement = connection.prepareStatement(
                    "SELECT name, city, nb_stars, nb_room_s, nb_room_d FROM hotel WHERE id = ?")) {
                hotelStatement.setInt(1, hotel.getId());
                ResultSet hotelResult = hotelStatement.executeQuery();
                if (hotelResult.next()) {
                    NameLabel.setText(hotelResult.getString("name"));
                    CityLabel.setText(hotelResult.getString("city"));
                    StarsLabel.setText(String.valueOf(hotelResult.getInt("nb_stars")));
                    NRSLabel.setText(String.valueOf(hotelResult.getInt("nb_room_s")));
                    NRDLabel.setText(String.valueOf(hotelResult.getInt("nb_room_d")));
                }
            }

            try (PreparedStatement roomPriceStatement = connection.prepareStatement(
                    "SELECT price FROM room_price WHERE hotel_id = ? AND room_id = ?")) {
                roomPriceStatement.setInt(1, hotel.getId());
                roomPriceStatement.setInt(2, 1);
                ResultSet simpleRoomPriceResult = roomPriceStatement.executeQuery();
                if (simpleRoomPriceResult.next()) {
                    PRSLabel.setText(String.valueOf(simpleRoomPriceResult.getInt("price")));
                }

                roomPriceStatement.setInt(2, 2);
                ResultSet doubleRoomPriceResult = roomPriceStatement.executeQuery();
                if (doubleRoomPriceResult.next()) {
                    PRDLabel.setText(String.valueOf(doubleRoomPriceResult.getInt("price")));
                }
            }

            try (PreparedStatement reservationStatement = connection.prepareStatement(
                    "SELECT COUNT(CASE WHEN room_id = ? THEN 1 END) AS nb_room_s_reserved, " +
                            "COUNT(CASE WHEN room_id = ? THEN 1 END) AS nb_room_d_reserved " +
                            "FROM reservation WHERE hotel_id = ? AND status = true")) {

                reservationStatement.setInt(1, 1);
                reservationStatement.setInt(2, 2);
                reservationStatement.setInt(3, hotel.getId());

                ResultSet reservationResult = reservationStatement.executeQuery();

                if (reservationResult.next()) {
                    NRSBLabel.setText(String.valueOf(reservationResult.getInt("nb_room_s_reserved")));
                    NRDBLabel.setText(String.valueOf(reservationResult.getInt("nb_room_d_reserved")));
                }
            }

            try (PreparedStatement iconStatement = connection.prepareStatement(
                    "SELECT icon_id FROM hotel_icon WHERE hotel_id = ?")) {
                iconStatement.setInt(1, hotel.getId());
                ResultSet iconResult = iconStatement.executeQuery();

                MRPane.setVisible(false);
                WifiPane.setVisible(false);
                SpaPane.setVisible(false);

                while (iconResult.next()) {
                    int iconId = iconResult.getInt("icon_id");
                    switch (iconId) {
                        case 1:
                            MRPane.setVisible(true);
                            break;
                        case 2:
                            WifiPane.setVisible(true);
                            break;
                        case 3:
                            SpaPane.setVisible(true);
                            break;
                        default:
                            break;
                    }
                }
            }

            try (PreparedStatement imageStatement = connection.prepareStatement(
                    "SELECT image FROM images WHERE hotel_id = ?")) {
                imageStatement.setInt(1, hotel.getId());
                ResultSet imageResult = imageStatement.executeQuery();

                List<Image> images = new ArrayList<>();
                while (imageResult.next()) {
                    Blob imageBlob = imageResult.getBlob("image");
                    if (imageBlob != null) {
                        byte[] imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
                        Image image = new Image(new ByteArrayInputStream(imageBytes));
                        images.add(image);
                    }
                }

                int numImages = images.size();
                if (numImages > 0) {
                    AtomicInteger imageIndex = new AtomicInteger(0);
                    Timeline timeline = new Timeline(new KeyFrame(
                            Duration.seconds(5),
                            event -> {
                                imageView.setImage(images.get(imageIndex.get()));
                                imageIndex.set((imageIndex.get() + 1) % numImages);
                            }
                    ));
                    timeline.setCycleCount(Timeline.INDEFINITE);
                    timeline.play();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    /* End View */



    /* Delete */

    private void deleteHotel(Hotel hotel) {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);

            try {

                deleteRecordsFromTable(connection, "DELETE FROM hotel_icon WHERE hotel_id = ?", hotel.getId());
                deleteRecordsFromTable(connection, "DELETE FROM images WHERE hotel_id = ?", hotel.getId());
                deleteRecordsFromTable(connection, "DELETE FROM reservation WHERE hotel_id = ?", hotel.getId());
                deleteRecordsFromTable(connection, "DELETE FROM room_price WHERE hotel_id = ?", hotel.getId());

                try (PreparedStatement statement = connection.prepareStatement("DELETE FROM hotel WHERE id = ?")) {
                    statement.setInt(1, hotel.getId());
                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected > 0) {
                        connection.commit();
                    } else {
                        connection.rollback();
                    }
                }
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteRecordsFromTable(Connection connection, String deleteQuery, int hotelId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            statement.setInt(1, hotelId);
            statement.executeUpdate();
        }
    }


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
                        Hotel selectedhotel = getTableView().getItems().get(getIndex());
                        boolean confirmed = showConfirmationDialog(selectedhotel);
                        if (confirmed) {
                            deleteHotel(selectedhotel);
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

    private boolean showConfirmationDialog(Hotel hotel) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete " + hotel.getName() + "?");
        ButtonType okButton = new ButtonType("OK");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonType.CANCEL.getButtonData());
        alert.getButtonTypes().setAll(okButton, cancelButton);
        alert.showAndWait();
        return alert.getResult() == okButton;
    }

    /* End delete */

    /* search */
    private void setupSearchFunctionality(List<Hotel> hotelList) {
        FilteredList<Hotel> filteredData = new FilteredList<>(FXCollections.observableArrayList(hotelList), p -> true);
        SortedList<Hotel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(HotelTable.comparatorProperty());
        HotelTable.setItems(sortedData);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(data -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return data.getName().toLowerCase().contains(lowerCaseFilter)
                        || data.getCity().toLowerCase().contains(lowerCaseFilter)
                        || String.valueOf(data.getNb_stars()).toLowerCase().contains(lowerCaseFilter)
                        || String.valueOf(data.getPrice()).toLowerCase().contains(lowerCaseFilter);
            });
        });
    }

    /* End search */
}