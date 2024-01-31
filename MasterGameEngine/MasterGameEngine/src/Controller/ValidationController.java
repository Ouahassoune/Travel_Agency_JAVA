package Controller;
import javafx.concurrent.Worker;
import javafx.embed.swing.SwingNode;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import util.DBCon;
import util.GeneratePdf_Modified;
import com.codingerror.model.AddressDetails;
import com.codingerror.model.Product;


import javafx.fxml.FXML;
import Module.Billet;
import javafx.scene.control.Label;
import util.Mail;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.layout.HBox;

import static util.DBCon.connect;

public class ValidationController {

    @FXML
    private Label closebtn;
    // Contrôleur de la page panier.fxml
    @FXML
    private Label hotelo;
    @FXML
    private Label price;
    @FXML
    private Label villex;
    @FXML
    private Label hotellx;
    @FXML
    private Label hotelnbretoilex;
    @FXML
    private Label pricex;
    @FXML
    private Label  typex;

    @FXML
    private Label type;
    @FXML
    private Label ville;
    private WebView webView;


    @FXML
        private Label dateLabel;

        @FXML
        private Label villedLabel;

        @FXML
        private Label villeaLabel;

        @FXML
        private Label prixLabel;

        @FXML
        private Label nbrv;
    @FXML
    private Button retour;

        private static Billet selectedBillet;








    @FXML
    private Label usernameLabel;
    private  hotel selectedHotel;
    private String NN;
    private static List<chambre> selectedChambres;

    public static  String username;
    private String password;
    public static int hotel_choisi;

    @FXML
    private HBox contentPane;
    @FXML
    private Pane contentPanex;
   // private WebView webView;
    private boolean paymentProcessed = false;
        // Méthode statique pour définir le billet sélectionné depuis le contrôleur de la page choose.fxml
        public static void setSelectedBillet(Billet billet) {
            selectedBillet = billet;
        }
    public void setText(String n) {
       this.NN = n;
    }
//    public static void setSelectedchambre(List<chambre> ch) {
//  cho = new ArrayList<>(ch);
//
//    }
public String DateDepart;
    public int nbre_voyageurr;
    @FXML
    private void initializeWebViewy() {
        webView = initializeWebView("Controller/html/indexds.html");

        // Ensure contentPane is not null
        if (webView != null) {
            // Clear existing children and add WebView
            contentPanex.getChildren().clear();
            contentPanex.getChildren().add(webView);

            // Bind WebView dimensions to contentPane dimensions
            webView.prefWidthProperty().bind(contentPanex.widthProperty());
            webView.prefHeightProperty().bind(contentPanex.heightProperty());
        } else {
            System.out.println("webview is null");
        }
    }

    @FXML
    private HBox cardhotel2;
    @FXML
    private ImageView i1;

    @FXML
    private ImageView i2;

    @FXML
    private ImageView i3;

    @FXML
    private ImageView i4;

    @FXML
    private ImageView i5;

    @FXML
    private ImageView i6;

public void setData(hotel data,List<chambre> selectedChambreso) {
    if (data == null ) {
        cardhotel2.setVisible(false);
        i1.setVisible(false);
        i2.setVisible(false);
        i3.setVisible(false);
        i4.setVisible(false);
        i5.setVisible(false);}
    else{

    // process the received data
    villex.setText(" Ville: " +data.getVille());
    ville_hotel=data.getVille();
    nom_hotel=data.getNom();
    nbre_etoile=data.getNbre_etoile();

    hotellx.setText(" Hotel: " +data.getNom());
    hotelnbretoilex.setText(data.getNbre_etoile()+" "+"Etoiles");
    for (chambre c:selectedChambreso){
        pricex.setText(String.valueOf(c.getPrice()));
        price_hotel= (float) c.getPrice();
        typex.setText("Type de chambre: " +c.getTyp());
        typs=c.getTyp();
     //   price_billet= (float) c.getPrice();
    }}
//    hotelo.setText(data.getNom());
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

        System.out.println("valisationcontroller user is "+username);

        controller.HistoriqueFromDB(username);
        stage.setScene(new Scene(root));}



    public hotel getSelectedHotel() {
        return selectedHotel;
    }

    public void setSelectedHotel(hotel selectedHotel) {
        this.selectedHotel = selectedHotel;
    }

    public void setHotelAndChambres(hotel selectedHotelo, List<chambre> selectedChambreso) {
    this.setSelectedHotel(selectedHotelo);
}


    public void handleCloseBtnClick(MouseEvent event) {
        // Handle close button click event
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void handleRetour(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        System.out.println("back handle btn clicked");
        System.out.println("hotel1 est "+hotel_choisi);
        try {System.out.println("back btn clicked");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mastergameengine/View/hotels.fxml"));
            Parent root = loader.load();
            hotelsController hotelscontroller = loader.getController();
            System.out.println("hotesl est "+hotel_choisi);
            hotelscontroller.revenir(villedLabel.getText(),villeaLabel.getText(),hotel_choisi);
            hotelscontroller.setUser(username);



            // Set properties on the controller
            //  hotelscontroller.setSelectedBillett(selectedBillet);
            hotelscontroller.menuDisplayCard(selectedBillet.getVilleArrivee(),"autre valeur");
            String user = usernameLabel.getText();
            // chose.setvilled(depart);
            //chose.setarrive(arrive);
            // chose.initializeWithBillets(listebillet);
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }}
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
        generatePdfAndSendEmail();
        showhotels();

        // Instantiate the JavaBridge controller
        JavaBridge javaBridgeController = new JavaBridge();

        // Set the username in JavaBridge controller
        javaBridgeController.setUsername(username);

        webView = initializeWebView("Controller/html/thankyou.html");

        WebEngine webEngine = webView.getEngine();

        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                System.out.println("Page loaded successfully");

                // Get the JavaScript object
                JSObject jsObject = (JSObject) webEngine.executeScript("window");

                // Expose Java object to JavaScript
                jsObject.setMember("javafx", new JavaBridge(webView));
                System.out.println("Java: JavaScript object exposed to JavaBridge");

                // You can now call Java methods from JavaScript using 'javafx.methodName()'
            }
        });

        // Ensure contentPane is not null
        if (webView != null) {
            // Clear existing children and add WebView
            contentPanex.getChildren().clear();
            contentPanex.getChildren().add(webView);

            // Bind WebView dimensions to contentPane dimensions
            webView.prefWidthProperty().bind(contentPanex.widthProperty());
            webView.prefHeightProperty().bind(contentPanex.heightProperty());
        } else {
            System.out.println("webview is null");
        }
    }
    public String nom_hotel;
    public String ville_hotel;
    public int nbre_etoile;
    public float price_hotel;
    public float price_billet;
    public String typs;

    public void showhotels()  {
        String date_depart= selectedBillet.getVilleDepart();
        String ville_depart=villedLabel.getText();
        String ville_destinaton=villeaLabel.getText();
        String nbre_voyageur=nbrv.getText();
        String transport="Plane";
        //info hotel
        float prix_billet=price_billet;
        String hot_ville=nom_hotel;
        String hot_nom=nom_hotel;
        int etoile=nbre_etoile;
        String type_chambre=typs;
        float prix_hotel=price_hotel;
        float prix_total= prix_hotel+prix_billet;

        try {
            Connection con=connect();
            Statement stmn = con.createStatement();
            String sql1= "INSERT INTO `historique` (`username`, `date_depart`, `ville_depart`, " +
                    "`ville_destination`, `nbre_voyageur`, `transport`, `prix_billet`, " +
                    "`hotel_ville`, `hotel_nom`, `nbre_etoile`, `type_chambre`, `prix_chambre`, `prix_totale`) " +
                    "VALUES ('" +getUsername() +
                    "', '" + DateDepart + "', '" + ville_depart + "', '" +
                    ville_destinaton + "', '" + nbre_voyageurr + "', '" + transport + "', '" +
                    prix_billet + "', '" + hot_ville + "', '" + hot_nom + "', '" + etoile + "', '" +
                    type_chambre + "', '" + prix_hotel + "', '" + prix_total + "')";
            stmn.executeUpdate(sql1);

            System.out.println("Values inserted into all tables successfully.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }






    }
    // Méthode appelée lors de l'initialisation de la page panier.fxml
    public void initialize() {
        webView = initializeWebView("Controller/html/payment-master/index.html");
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
        closebtn.setOnMouseClicked(this::handleCloseBtnClick);
        // Afficher les informations du billet dans les libellés
        dateLabel.setText("Date de Depart : " + selectedBillet.getDateDepart().toString());
        villedLabel.setText(selectedBillet.getVilleDepart());
        villeaLabel.setText(selectedBillet.getVilleArrivee());
        nbre_voyageurr=selectedBillet.getNombreVoyageurs();
        price_billet= (float) selectedBillet.getPrix()*nbre_voyageurr;


        // Calculating the total price
        double totalPrix = selectedBillet.getNombreVoyageurs() * selectedBillet.getPrix();
        DateDepart = selectedBillet.getDateDepart().toString();

        float tt = (float) totalPrix;
        prixLabel.setText(String.format("Total : %.1f DH", totalPrix));
        nbrv.setText("Nombre Voyageur : " + Integer.toString(selectedBillet.getNombreVoyageurs()));
        System.out.println(NN);
        if (selectedHotel != null) {
            // Access properties of selectedHotel here
            hotelo.setText(selectedHotel.getNom());
            System.out.println("reussi");
            System.out.println(selectedHotel.getPrix());
            // Other initialization logic
        }

//

        String DateDepart = selectedBillet.getDateDepart().toString();
        String villed = selectedBillet.getVilleDepart();
        String villea = selectedBillet.getVilleArrivee();
        int Nbrvo = selectedBillet.getNombreVoyageurs();
        String Nom = usernameLabel.getText();








    }



    public void setPassword(String password) {
        this.password = password;


    }

    public void handlePayNowButton() {
        showAlert("Payment Successful");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
        updateUsernameLabel();
    }

    private void updateUsernameLabel() {
        // Update the usernameLabel with the username
        usernameLabel.setText(username);
    }
    private void generatePdfAndSendEmail() {

        try {

            double totalPrix = selectedBillet.getNombreVoyageurs() * selectedBillet.getPrix();
            float tt = (float) totalPrix;
            String hotelll= hotellx.getText();
            String hotelnbretoilexx= hotelnbretoilex.getText();


            String villed = selectedBillet.getVilleDepart();
            String villea = selectedBillet.getVilleArrivee();
            int Nbrvo = selectedBillet.getNombreVoyageurs();
            String pricexText = pricex.getText();
            float pricexx = Float.parseFloat(pricexText);
            // Move the PDF generation and email sending logic here
            String Nom = usernameLabel.getText();
            List<Product> productList = new ArrayList<>();
            productList.add(new Product(villed + " to " + villea, Nbrvo, tt));
            productList.add(new Product(hotelll + " Nombre d'etoile : " + hotelnbretoilexx,1,pricexx));
            GeneratePdf_Modified pdfGenerator = new GeneratePdf_Modified(Nom, productList);

            Mail mail = new Mail();
            String email = DBCon.getEmailByUsername(Nom);
            System.out.println("IS EMAIL BOM"+Nom);
            mail.sendMail(email);

            // Print a message indicating that the PDF and email have been generated
            System.out.println("PDF and email generated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void set_hotel_id(int hotelChois) {
        this.hotel_choisi=hotelChois;
        System.out.println("set_hotel_id"+ hotel_choisi + hotelChois);
    }


}
