package Controller;

import Module.CityModel;
import Module.TicketModel;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.util.List;
import javafx.embed.swing.SwingNode;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.Node;
import Module.Billet;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import com.jfoenix.controls.JFXButton;
import netscape.javascript.JSObject;
//import test.Main;

import javax.swing.*;

public class GameEngineController implements Initializable {

    @FXML
    private Pane contentPane;
    @FXML
    private JFXButton webViewButton;

    private WebView webView;
//
//    // Constructor or initialization method
//    public GameEngineController() {
//        loginApp2 = new LoginApp2();
//    }
//    ouahassoune
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
    void goFirstPage(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Quitter la page actuelle");
        alert.setContentText("Êtes-vous sûr de vouloir quitter cette page ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/java/mastergameengine/View/GameEngine.fxml"));
            Parent root = loader.load();
            GameEngineController controller = loader.getController();
            controller.setUsername(username);
            controller.usernameLabel.setText(username);
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



//    @FXML
//    private void initializeWebViewx() {
//        SwingNode swingNode = new SwingNode();
//
//        // Assuming your MainSwingClass extends JFrame or JPanel
//        Main mainSwingClass = new Main();
//        mainSwingClass.initializeSwingNode(swingNode);
//
//        // Ensure contentPane is not null
//        if (contentPane != null) {
//            // Clear existing children and add SwingNode
//            contentPane.getChildren().clear();
//            contentPane.getChildren().add(swingNode);
//
//            // Bind SwingNode dimensions to contentPane dimensions if needed
////            swingNode.prefWidthProperty().bind(contentPane.widthProperty());
////            swingNode.prefHeightProperty().bind(contentPane.heightProperty());
//        } else {
//            System.out.println("Content pane is null");
//        }
//    }




    @FXML
    private Label closebtn;

    @FXML
    private HBox mpHbox;

    @FXML
    private Label resize;

    @FXML
    private Label reduce;

    @FXML
    private Button switchme;

    @FXML
    public ChoiceBox<String> choiceboxdepart;

    @FXML
    public ChoiceBox<String> choiceboxarrivee;

    @FXML
    private DatePicker datepickerdepart;

    @FXML
    private DatePicker datepickerretour;

    @FXML
    private Button searchButton;

    @FXML
    private Spinner<Integer> spinnerNombreVoyageurs;

    @FXML
    private  Label notext;
    private ValidationController validationController;

    @FXML
    public Label usernameLabel;

    private String username;


    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {

//

        // Set event handlers for the labels
        closebtn.setOnMouseClicked(this::handleCloseBtnClick);

        // Initialize and populate choice boxes
        populateChoiceBoxes();

        datepickerdepart.setValue(LocalDate.now());
        // In initialize method
        datepickerretour.setValue(LocalDate.now());  // Set an initial value
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        spinnerNombreVoyageurs.setValueFactory(valueFactory);

        datepickerdepart.setDayCellFactory(picker -> new DatePickerDateCell());
        datepickerretour.setDayCellFactory(picker -> new DatePickerDateCell());

        searchButton.setOnAction(this::handleRecherche);
        switchme.setOnAction(this::switchchoiceboxvalue);
//        webViewButton.setOnAction(this::openWebView);
    }
    private static class DatePickerDateCell extends DateCell {
        @Override
        public void updateItem(LocalDate item, boolean empty) {
            super.updateItem(item, empty);
            setDisable(empty || item.isBefore(LocalDate.now()));
        }
    }

    public void handleCloseBtnClick(MouseEvent event) {
        // Handle close button click event
        Platform.exit();
        System.exit(0);
    }

    private void populateChoiceBoxes() {
        // Use the model class to fetch data
        List<String> cities = CityModel.getCities();

        // Populate choice boxes
        choiceboxdepart.getItems().addAll(cities);
        choiceboxarrivee.getItems().addAll(cities);
    }
    public void setValidationController(ValidationController validationController) {
        this.validationController = validationController;
    }

    @FXML
    private void handleRecherche(ActionEvent event) {
        // Récupération des informations d'entrée de l'utilisateur
        System.out.println("1");
        String villeDepart = choiceboxdepart.getValue();
        String villeArrivee = choiceboxarrivee.getValue();
        String dateDepart = datepickerdepart.getValue().toString();
        String dateRetour = datepickerretour.getValue().toString();
        int nombreVoyageurs = spinnerNombreVoyageurs.getValue();  // Use getValue() for Spinner

        // Appel à la fonction de recherche dans le modèle (CityModel et TicketModel)
        // Utilisez les données récupérées pour afficher les billets disponibles dans la nouvelle page
        List<Billet> billets = TicketModel.rechercherBillets(villeDepart, villeArrivee, dateDepart, dateRetour, nombreVoyageurs);
        System.out.println("2");
        if (billets.isEmpty()) {
            // Aucun billet n'est disponible, afficher le message sur la première page
            showNoBilletsMessage();
        } else {
            // Charger la nouvelle page pour afficher les résultats
            loadResultsPage(billets);
        }
    }
    private void loadResultsPage(List<Billet> billets) {
        try {
            // Charger la nouvelle page pour afficher les résultats
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mastergameengine/View/choosexx.fxml"));
            Parent root = loader.load();

            // Passer les données des billets au contrôleur de la nouvelle page
            ChooseController resultatsController = loader.getController();
            // Assuming you have a ValidationController instance (validationController) available
            resultatsController.setValidationController(validationController);
            resultatsController.initializeWithBillets(billets);
            String user= usernameLabel.getText();
            resultatsController.setUsername(user);
            resultatsController.setvilled(choiceboxdepart.getValue());
            resultatsController.setarrive(choiceboxarrivee.getValue());


            // Remplacer le contenu de la scène actuelle avec la nouvelle page
            Stage stage = (Stage) searchButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'exception de chargement de la nouvelle page
        }
    }
    private void showNoBilletsMessage() {
        // Afficher un message sur la première page
        // Vous pouvez utiliser une boîte de dialogue, une étiquette, ou tout autre composant pour afficher le message
        notext.setText("Aucun billet n'est disponible");
        // Ajoutez ici le code pour afficher le message sur la première page
    }
    private void switchchoiceboxvalue(ActionEvent event){
        String villeDepart = choiceboxdepart.getValue();
        String villeArrivee = choiceboxarrivee.getValue();
        String aide= villeDepart;
        choiceboxdepart.setValue(villeArrivee);
        choiceboxarrivee.setValue(aide);
    }
    public void setUsername(String username) {
        usernameLabel.setText(username);
        this.username = username;
        updateUsernameLabel();
    }

    private void updateUsernameLabel() {
        // Update the usernameLabel with the username
        usernameLabel.setText(username);
    }
}
