package Controller;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import Module.*;
import Module.Billet;
import javafx.fxml.FXMLLoader;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.IOException;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.util.*;

import javafx.geometry.Insets;

import javafx.scene.image.ImageView;
import java.io.InputStream;
import java.util.List;

import javafx.scene.image.Image;



public class ChooseController {
    @FXML
    private Label closebtn;
    private String fxmlPath;

    @FXML
    private Pane contentPane;

    @FXML
    private BorderPane mainStackPane;

    private Billet selectedBillet;
    @FXML
    private Button retour;

    @FXML
    private Button continueButton;

    private ValidationController validationController;

    @FXML
    private Label usernameLabel;

    private String username;

    private WebView webView;


    public void setValidationController(ValidationController validationController) {
        this.validationController = validationController;
    }

    @FXML
    private GridPane billetsGrid;  // Your container to display tickets

    private TicketModel ticketModel = new TicketModel();
    private Pane lastSelectedBilletPane; // Keep track of the last selected billet's pane

    public void initialize() {
        // Other initialization code...

        // Set the onMouseClicked event for the closebtn
        closebtn.setOnMouseClicked(this::handleCloseBtnClick);
    }
    public List<Billet> showBillets=new ArrayList<Billet>();
    public void initializeWithBillets(List<Billet> billets) {
        showBillets.addAll(billets);
        int column = 0;
        int row = 0;
        double firstRowsVGap = 170.0;
        double restRowsVGap = 0;

        for (Billet billet : billets) {

            if(billet.getType().equals("car")){
                fxmlPath="/mastergameengine/View/itemc.fxml";
            } else if (billet.getType().equals("plane")) {
                fxmlPath="/mastergameengine/View/itemb.fxml";
            } else if (billet.getType().equals("train")) {
                fxmlPath="/mastergameengine/View/itema.fxml";
            }
            VBox billetPane = loadBilletsFromFXML(fxmlPath);
            fillBilletPane(billetPane, billet);

            billetPane.setOnMouseClicked(event -> handleBilletSelection(billetPane, billet));
//
                // Add the graphical element to the container (GridPane)
                billetsGrid.add(billetPane, column, row);
//            billetsGrid.setMargin(billetPane, new Insets( 1000));
            if (row == 0 || row == 1) {
                billetsGrid.setMargin(billetPane, new Insets(firstRowsVGap, 0, 0, 0));
            } else {
                billetsGrid.setMargin(billetPane, new Insets(restRowsVGap, 0, 0, 0));
            }

            // Increment column and row
            column++;
            if (column == 1) {
                column = 0;
                row++;
            }

        }
    }
    public WebView initializeWebView(String htmlFilePath) {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);

        // Capture console errors
        webEngine.setOnError(event -> {
            System.out.println("JavaScript Error: " + event.getMessage());
        });

        // Wait for the page to finish loading
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                // Page has finished loading
                System.out.println("Page loaded successfully");
            }
        });

        try {
            // Load the HTML content
            webEngine.load(getClass().getClassLoader().getResource(htmlFilePath).toExternalForm());
        } catch (Exception e) {
            e.printStackTrace();
            // Handle loading exception here
        }

        // Apply optimizations
        webView.setCache(true);

        return webView;
    }
    @FXML
    private void initializeWebViewx() {
        webView = initializeWebView("Controller/html/indexds.html");

        // Ensure contentPane is not null
        if (webView != null) {
            // Clear existing children and add WebView
            contentPane.getChildren().clear();
            contentPane.getChildren().add(webView);

            // Bind WebView dimensions to contentPane dimensions
            webView.prefWidthProperty().bind(contentPane.widthProperty());
            webView.prefHeightProperty().bind(contentPane.heightProperty());
        } else {
            System.out.println("webview is null");
        }
    }
    @FXML
    void goFirstPage(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Quitter la page actuelle");
        alert.setContentText("Êtes-vous sûr de vouloir quitter cette page ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mastergameengine/View/GameEngine.fxml"));
            Parent root = loader.load();
            GameEngineController controller = loader.getController();
            controller.setUsername(username);
            usernameLabel.setText(username);
            stage.setScene(new Scene(root));
        }
    }
    @FXML
    private void gohistory(ActionEvent event) throws IOException {
        System.out.println("clicker username is"+ usernameLabel.getText());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mastergameengine/View/showHotels.fxml"));
        Parent root = loader.load();
        showHotelsController controller = loader.getController();
        controller.setUsername(username);

        controller.HistoriqueFromDB(username);
        stage.setScene(new Scene(root));}

//    public void menuDisplayCard(String typeTrie) {
//        cardlistData.clear();
//        cardlistData.addAll(getAllHotelDataFromDB());
//        switch(typeTrie){
//            case "trie par prix":
//                Collections.sort(cardlistData, Comparator.comparingInt(hotel -> hotel.getPrix()));
//                break;
//            case "trie par etoile":
//                Collections.sort(cardlistData, Comparator.comparingInt(hotel -> hotel.getNbre_etoile()));
//                break;
//            case "trie par diver":
//                Collections.sort(cardlistData, Comparator.comparingInt(hotel -> hotel.getSizee()+1));
//                Collections.reverse(cardlistData);
//                break;
//            case "trie par economie":
//                Collections.sort(cardlistData, Comparator.comparingInt(hotel -> hotel.getEconomie()));
//                Collections.reverse(cardlistData);
//                break;
//            default:break;
//        }
//
//        int row = 0;
//        int column = 0;
//        grid.getChildren().clear();
//        grid.getRowConstraints().clear();
//        grid.getColumnConstraints().clear();
//        for (int q = 0; q < cardlistData.size(); q++) {
//            try {
//                FXMLLoader load = new FXMLLoader();
//                load.setLocation(getClass().getResource("/mastergameengine/View/hotel1.fxml"));
//                VBox pane = load.load();
//                // ImageView image = new ImageView((Element) new Image("file:"+cardlistData.get(q).getUrl()));
//                //  setNode(pane);
//                hotel1Controller cardC = load.getController();
//                cardC.setData(cardlistData.get(q));
//                if (column == 100) {
//                    column = 0;
//                    row += 1;}
//                grid.add(pane, column++, row);
//                GridPane.setMargin(pane, new Insets(10));
//            } catch (Exception e) {
//                e.printStackTrace();}}
//    }
    private VBox loadBilletsFromFXML(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return new VBox();  // Handle the appropriate exception in your application
        }
    }

    private void handleBilletSelection(Pane billetPane, Billet billet) {
        if (selectedBillet != null) {
            // If another billet is already selected, remove the selection
            removeSelection(lastSelectedBilletPane);
        }

        // Update the selected billet and its style
        selectedBillet = billet;
        updateBilletStyle(billetPane, true);

        // Save the reference to the last selected billet's pane
        lastSelectedBilletPane = billetPane;

        // Pass the information to the ValidationController
        validationController.setSelectedBillet(selectedBillet);
        continueButton.setDisable(false);
    }
    public void handleCloseBtnClick(MouseEvent event) {
        // Handle close button click event
        Platform.exit();
        System.exit(0);
    }

    private void removeSelection(Pane billetPane) {
        // Remove the selection style from the previously selected billet
        updateBilletStyle(billetPane, false);
    }

    private void updateBilletStyle(Pane billetPane, boolean isSelected) {
        if (isSelected) {
            billetPane.getStyleClass().add("selected-ticket");
        } else {
            billetPane.getStyleClass().remove("selected-ticket");
        }
    }

    @FXML
    private void handleRetour(ActionEvent event) {
        // Code to handle the retour button action

        // Get the current stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        try {
            // Load the FXML file for the previous page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mastergameengine/View/GameEngine.fxml"));
            Parent root = loader.load();
            GameEngineController cardC = loader.getController();
            cardC.choiceboxdepart.setValue(depart);
            cardC.choiceboxarrivee.setValue(arrive);
            cardC.setUsername(username);
            System.out.println(depart+arrive);

            // Set the content of the current stage to the previous page
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }

    @FXML
    private void handleContinueButtonClick(ActionEvent event) {
        try {
            if (selectedBillet != null) {
                System.out.println("Continue button clicked");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/mastergameengine/View/hotels.fxml"));

                Parent validationRoot = loader.load();
                hotelsController hotelscontroller = loader.getController();
                hotelscontroller.setUser(this.username);
                hotelscontroller.setdepart(depart);
                hotelscontroller.setarrive(arrive);
                hotelscontroller.setbillet((List<Billet>) showBillets);
                hotelscontroller.setSelectedBillett(selectedBillet);
                hotelscontroller.menuDisplayCard(selectedBillet.getVilleArrivee(),"autre valeur");
                String user = usernameLabel.getText();
                mainStackPane.getChildren().setAll(validationRoot);
            } else {
                System.out.println("Validation controller or selected billet is null");
                System.out.println("Selected Billet: " + selectedBillet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




//    private Pane loadBilletFromFXML() {
//        // Logic to load the graphical element of the billet from the FXML
//        // Use an FXMLLoader to load the graphical element from the dedicated FXML for the billet
//
//        // Example:
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mastergameengine/View/itemb.fxml"));
//            return loader.load();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new Pane();  // Handle the appropriate exception in your application
//        }
//    }

    private void fillBilletPane(Pane billetPane, Billet billet) {
        // Logic to fill the graphical element of the billet with the billet data
        // Use the appropriate graphical elements in billetPane and configure them based on the billet properties
        // Make sure you have the correct IDs in your FXML for each billet property

        // Example:
        Label Dated = (Label) billetPane.lookup("#Dated");
        Dated.setText(billet.getDateDepart().toString());
        Label Prixb = (Label) billetPane.lookup("#Prixb");
        Prixb.setText(String.valueOf(billet.getPrix()));

        Label nbrv = (Label) billetPane.lookup("#nbrv");
        nbrv.setText(String.valueOf(billet.getNombreVoyageurs()));

        Label departb = (Label) billetPane.lookup("#departb");
        departb.setText(billet.getVilleDepart());
        Label arriveb = (Label) billetPane.lookup("#arriveb");
        arriveb.setText(billet.getVilleArrivee());
        Label Typeb = (Label) billetPane.lookup("#Typeb");
        Typeb.setText(billet.getType());
        if (billet.getType().equals("plane")) {
            // Access the ImageView
            ImageView randomImageView = (ImageView) billetPane.lookup("#randomImageView");

            // Set a random image
            String randomImagePath = getRandomImagePath(); // Implement this method
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(randomImagePath);

            if (inputStream != null) {
                Image randomImage = new Image(inputStream);
                randomImageView.setImage(randomImage);
            } else {
                System.err.println("Image not found: " + randomImagePath);
            }
            // ... Repeat for each billet property
        }

    }
    private String getRandomImagePath() {
        // Provide a list of image paths and randomly select one
        List<String> imagePaths = Arrays.asList("icons/gratis-png-logo-air-france-beirut-rafic-hariri-aeropuerto-internacional-aerolinea-aviacion-qatar-airways-logo-blanco.png","icons/sa2892s363-saudi-arabian-airlines-logo-saudi-arabian-airlines-logo-evolution-history-and-meaning.png","icons/Logo_Royal_Air_Maroc.svg.png","icons/turkish.png","icons/download.png");
        Random random = new Random();
        return imagePaths.get(random.nextInt(imagePaths.size()));
    }
    public void setUsername(String username) {
        this.username = username;
        updateUsernameLabel();
    }

    private void updateUsernameLabel() {
        // Update the usernameLabel with the username
        usernameLabel.setText(username);
    }

    private String depart;
    public void setvilled(String value) {
        depart=value;
    }
    private String arrive;
    public void setarrive(String arriv) {
        arrive=arriv;
    }
    public String nom="nihal hmara";
}
