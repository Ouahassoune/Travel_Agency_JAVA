package Controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import util.DBCon;
import Module.Billet;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class hotelsController implements Initializable{
    private String username;
    private ValidationController validationController;
    @FXML
    private Label usernameLabel;

    @FXML
    private Pane contentPane;
    @FXML
    private Label closebtn;


    private static Billet selectedBillet;
    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        usernameLabel.setText(user);
        this.user = user;
    }

    public void setValidationController(ValidationController validationController) {
        this.validationController = validationController;
    }
    public ValidationController valid;

    public ValidationController getValidationController() {
        return validationController;
    }

    public void setValid(ValidationController valid) {
        this.valid = valid;
    }


    public static void setSelectedBillett(Billet billet){
        selectedBillet=billet;
    }

    @FXML
    private JFXButton btnback2;

    @FXML
    private JFXButton btnback;
    @FXML
    private ImageView amentie1;

    @FXML
    private ImageView amentie2;

    @FXML
    private ImageView amentie3;


    @FXML
    private Label chm1;

    @FXML
    private Label chm2;

    @FXML
    private Label chm3;


    @FXML
    private ChoiceBox<String> typex;

    @FXML
    private TableColumn<chambre, String> desc;

    @FXML
    private GridPane grid;

    @FXML
    private DatePicker in;

    @FXML
    private DatePicker out;
    private WebView webView;

    //@FXML
    //  private DatChooser dchoose;

    @FXML
    private TableColumn<chambre,Integer> price;
    @FXML
    private TableColumn<chambre, String> typech;

    @FXML
    private AnchorPane scroll2;

    @FXML
    private TableView<chambre> table1;
    private String textg="ghizlane";


    @FXML
    private ChoiceBox<String> choicetri;
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
            controller.setUsername(user);
            stage.setScene(new Scene(root));
        }
    }

    @FXML
    private void backActionn2(ActionEvent event) {
        grid.setVisible(true);
        scroll2.setVisible(false);
        choicetri.setVisible(true);
        btnback.setVisible(true);
        btnback2.setVisible(false);
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
    private void backActionn(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        System.out.println("back btn clicked");


        try {

            System.out.println("back btn clicked");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mastergameengine/View/Choosexx.fxml"));
            Parent root = loader.load();
            ChooseController chose = loader.getController();
            chose.setvilled(depart);
            chose.setarrive(arrive);
            chose.setUsername(user);
            chose.initializeWithBillets(listebillet);
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /*
        try {
            // Chargement du fichier FXML pour la nouvelle fenêtre
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../mastergameengine/View/hotels.fxml"));
            Parent root = loader.load();

            // Création de la scène
            Scene scene = new Scene(root);

            // Obtention de la scène actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Changement de la scène pour afficher la nouvelle fenêtre
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
    @FXML
    private void gohistory(ActionEvent event) throws IOException {
        System.out.println("clicker username is"+ usernameLabel.getText());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mastergameengine/View/showHotels.fxml"));
        Parent root = loader.load();
        showHotelsController controller = loader.getController();
        controller.setUsername(user);

        controller.HistoriqueFromDB(user);
        stage.setScene(new Scene(root));}
    public  hotel hott;
    public List<chambre> chambresSelectionnees = new ArrayList<>();

    @FXML
    private void selectroomAction(ActionEvent event) throws IOException {

        LocalDate inn = in.getValue();
        LocalDate  outt=out.getValue();
        if (inn == null || outt == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une date.");

            alert.showAndWait(); // Afficher l'alerte et attendre la réponse de l'utilisateur
        }else{

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // ou un autre format
            String dateStr = inn.format(formatter);

            String dateStr2 = outt.format(formatter);

            ObservableList<chambre> selectedItems = table1.getSelectionModel().getSelectedItems();
            if (!selectedItems.isEmpty()) {
                chambresSelectionnees.clear();
                for (chambre selectedChambre : selectedItems) {
                    // Récupération des informations de chaque chambre sélectionnée
                    String description = selectedChambre.getDescription();
                    double price = selectedChambre.getPrice();
                    String type=selectedChambre.getTyp();
                    chambre ch=new chambre(description,price,dateStr,dateStr2);
                    ch.setTyp(type);
                    chambresSelectionnees.add(ch);
                }
                // Load the FXML file for the validation page
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/mastergameengine/View/validationx.fxml"));
                Parent root =(Parent) loader.load();
                ValidationController validationController = loader.getController();
                validationController.setUsername(user);
                validationController.setData(hott,chambresSelectionnees);

                // Get the ValidationController
//        ValidationController vac = loader.getController();
                validationController.setHotelAndChambres(hott, chambresSelectionnees);
                validationController.setText(textg);
                System.out.println(hott.getNom());
                // Set the selected billet and chambres
                validationController.setSelectedBillet(selectedBillet);
//   validationController.setSelectedchambre(chambresSelectionnees);


                // Get the current stage and set a new scene with the validation page
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));

            } else {
                // Gestion si aucune chambre n'est sélectionnée
                System.out.println("Aucune chambre sélectionnée.");
            }
            this.getValidationController();
//    valid.setthotel(hott);
            afficherAll();
        }}

    public  hotel getHott() {


        return hott;
    }
    //    private List<chambre> chambresSelectionnees;

    private void ajouterChambreSelectionnee(chambre ch) {
        chambresSelectionnees.add(ch);
    }

    private ObservableList<hotel> cardlistData = FXCollections.observableArrayList();


    public int hotel_choisi;
    public void mettreJour(hotel id){
        hott=id;
        hotel_choisi=hott.getId();
        grid.setVisible(false);
        scroll2.setVisible(true);
        choicetri.setVisible(false);
        btnback.setVisible(false);
        btnback2.setVisible(true);
        amenities();
        chambre_db(null);
        ValidationController val=new ValidationController();
        val.set_hotel_id(hotel_choisi);
        System.out.println("hotel: "+hotel_choisi);
    }
    @FXML
    void skipAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mastergameengine/View/validationx.fxml"));
        Parent root =(Parent) loader.load();
        ValidationController validationController = loader.getController();
        validationController.setUsername(user);
        validationController.setData(null,null);
        validationController.setHotelAndChambres(null, null);
        validationController.setSelectedBillet(selectedBillet);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }
    public hotelsController() {}
    private String villes;
    public String getVilles() {
        return villes;
    }

    public void setVilles(String villes) {
        this.villes = villes;
    }

    public ObservableList<hotel> getAllHotelDataFromDB(String vill) {

// Connexion à la base de données (exemple avec MySQL)
        ObservableList<hotel> hotelDataList = FXCollections.observableArrayList();
        System.out.println("ville de ill selectionne est "+vill);
        setVilles(vill);
        System.out.println("vile selec"+this.villes);
        try {Connection connection = DBCon.connect();
            // String villes=selectedBillet.getVilleArrivee();
            String sql;

            if(villes!="nulle") {
                sql = "SELECT hotel.id AS hotel_id, hotel.city AS ville , hotel.name AS nom, hotel.nb_room_s AS hotel_nbre_chambre1,  hotel.nb_room_d AS hotel_nbre_chambre2, " +
                        " hotel.stars AS hotel_nbre_etoile, hotel.economy AS hotel_economie, " +
                        " hotel.price AS hotel_prix FROM hotel  WHERE `ville`= \"" + villes + "\"";
                //"LEFT JOIN RelationTable ON hotels.id = RelationTable.id_hotel " +
                //"LEFT JOIN icons ON RelationTable.id_icon = icons.id";
            }else{   sql = "SELECT hotel.id AS hotel_id, hotel.city AS ville , hotel.name AS nom, hotel.nb_room_s AS hotel_nbre_chambre1,  hotel.nb_room_d AS hotel_nbre_chambre2, " +
                    " hotel.stars AS hotel_nbre_etoile, hotel.economy AS hotel_economie, " +
                    " hotel.price AS hotel_prix FROM hotel  ";}
            //"LEFT JOIN RelationTable ON hotels.id = RelationTable.id_hotel " +
            //"LEFT JOIN icons ON RelationTable.id_icon = icons.id";}
            // Requête SQL pour récupérer toutes les données des hôtels
            PreparedStatement statement2 = connection.prepareStatement(sql);
            ResultSet result = statement2.executeQuery();
            hotel hot;
            List<hotel> hotelMap = new LinkedList<hotel>();
            while (result.next()) {
                System.out.println( "nom hotel est "+result.getString("nom"));
                int hid = result.getInt("hotel_id");
                String hnom = result.getString("nom");
                // String url = result.getString("hotel_url");
                int hetoile = result.getInt("hotel_nbre_etoile");
                String ville = result.getString("ville");
                int chambre_s = result.getInt("hotel_nbre_chambre1");
                int chambre_d = result.getInt("hotel_nbre_chambre2");
                int economie = result.getInt("hotel_economie");
                int prix = result.getInt("hotel_prix");

                hotel hotel1;
                hotel1 = new hotel();
                hotel1.setId(hid);
                hotel1.setNom(hnom);
                hotel1.setVille(ville);
                hotel1.setNbre_chambre(chambre_s);
                hotel1.setNbre_chambre2(chambre_d);

                hotel1.setEconomie(economie);
                hotel1.setNbre_etoile(hetoile);
                hotel1.setPrix(prix);
                hotel1.setGrid(this);
                hotelDataList.add(hotel1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }return hotelDataList;

    }

    public void amenities(){{

        try {Connection connection = DBCon.connect();
            String sql;
            sql = "select icon.charact, icon.image from icon, hotel_icon where icon.id=hotel_icon.icon_id and hotel_icon.hotel_id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            System.out.println("amenties i hotels choisi is "+hotel_choisi);
            statement.setInt(1, hotel_choisi);
            ResultSet result = statement.executeQuery();
            List<String> listeAmenties = new ArrayList<>();
            List<String> icon = new ArrayList<>();
            while (result.next()) {
                System.out.println("amenties i hotels choisi is "+hotel_choisi);
                System.out.println("amenties is "+result.getString("charact"));
                listeAmenties.add(result.getString("charact"));
                icon.add(result.getString("image"));}
            if (!listeAmenties.isEmpty()) {
                int numero=listeAmenties.size();
                int sizee=numero;
                Label[] amenti={chm1,chm2,chm3};
                ImageView[] image={amentie1,amentie2,amentie3};
                for(int i=0;i<3;i++){
                    if(i<numero){
                        String pathe = "file:"+icon.get(i);
                        Image iconImage = new Image(pathe);
                        image[i].setImage(iconImage);
                        amenti[i].setText(listeAmenties.get(i));
                    }
                    else {
                        image[i].setImage(null); // Masquer ou désactiver l'ImageView si moins d'icônes sont disponibles
                    }}}
        }catch (SQLException e) {
            e.printStackTrace();}}
    }

    public void chambre_db(String type){

        try {Connection connection = DBCon.connect();
            String sql;
            sql = "SELECT room.description as description " +
                    ",room.type as type,room_price.price as prix FROM room," +
                    "room_price,hotel where room.id=room_price.room_id and hotel.id= "+ hotel_choisi+" and room_price.hotel_id= "+hotel_choisi+";";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            data = FXCollections.observableArrayList();
            table1.getItems().clear();
            while (result.next()) {
                if (result.getString("description") != null && result.getDouble("prix") != 0  && result.getString("type") != null) {
                    String description = result.getString("description");
                    double price = result.getDouble("prix");
                    type=result.getString("type");
                    System.out.println(type);
                    chambre chambree = new chambre(description,price,type);
                    chambree.setDescription(description);
                    chambree.setPrice(price);
                    chambree.setTyp(type);
                    System.out.println("type de cette ch"+chambree.getTyp());
                    data.add(chambree);

                }
                desc.setCellValueFactory(new PropertyValueFactory<>("description"));
                price.setCellValueFactory(new PropertyValueFactory<>("price"));
                typech.setCellValueFactory(new PropertyValueFactory<>("typ"));


                System.out.println(""+type);
                table1.setItems(data);
                for(chambre ch:data){
                    System.out.println("typech is "+ch.getTyp());
                }
                //  table1.getColumns().addAll(desc, price, typech);

                table1.refresh(); // Rafraîchit la table après avoir ajouté les données
            }this.amenities();

        } catch (SQLException e) {
            e.printStackTrace();}
    }

    public ObservableList<chambre> data;

    public void trie_chambre(String typeSpecifique) {
        // Filtrer les chambres par type spécifique
        ObservableList<chambre> chambresFiltrees = FXCollections.observableArrayList();
        for (chambre ch : data) {
            if (ch.getTyp().equals(typeSpecifique)) {
                chambresFiltrees.add(ch);
            }
        }

        // Mettre à jour la TableView avec les chambres filtrées
        table1.setItems(chambresFiltrees);
    }


    public void menuDisplayCard(String vill,String typeTrie) {

// Connexion à la base de données (exemple avec MySQL)
        ObservableList<hotel> hotelDataList = FXCollections.observableArrayList();
        System.out.println("ville de ill selectionne est "+vill);
        setVilles(vill);
        System.out.println("vile selec"+this.villes);
        try {Connection connection = DBCon.connect();
            // String villes=selectedBillet.getVilleArrivee();
            String sql;
            if(villes!="nulle") {
                sql = "SELECT hotel.id AS hotel_id, hotel.city AS ville, hotel.name AS nom, " +
                        "hotel.nb_room_s AS hotel_nbre_chambre1, hotel.nb_room_d AS hotel_nbre_chambre2, " +
                        "hotel.nb_stars AS hotel_nbre_etoile, hotel.economy AS hotel_economie, " +
                        "hotel.price AS hotel_prix, images.image AS image " +
                        "FROM hotel " +
                        "LEFT JOIN images ON hotel.id = images.hotel_id " +
                        "WHERE hotel.city = '"+villes+"'";
            }else{  sql = "SELECT hotel.id AS hotel_id, hotel.city AS ville , hotel.name AS nom, hotel.nb_room_s AS hotel_nbre_chambre1,  hotel.nb_room_d AS hotel_nbre_chambre2, " +
                    " hotel.stars AS hotel_nbre_etoile, hotel.economy AS hotel_economie, " +
                    " hotel.price AS hotel_prix FROM hotel  ";}
            //"LEFT JOIN RelationTable ON hotels.id = RelationTable.id_hotel " +
            //"LEFT JOIN icons ON RelationTable.id_icon = icons.id";}


            // Requête SQL pour récupérer toutes les données des hôtels
            PreparedStatement statement2 = connection.prepareStatement(sql);
            ResultSet result = statement2.executeQuery();
            hotel hot;
            List<hotel> hotelMap = new LinkedList<hotel>();


            while (result.next()) {
                System.out.println( "nom hotel est "+result.getString("nom"));
                int hid = result.getInt("hotel_id");
                String hnom = result.getString("nom");
                // String url = result.getString("hotel_url");
                int hetoile = result.getInt("hotel_nbre_etoile");
                String ville = result.getString("ville");
                int chambre_s = result.getInt("hotel_nbre_chambre1");
                int chambre_d = result.getInt("hotel_nbre_chambre2");
                int economie = result.getInt("hotel_economie");
                int prix = result.getInt("hotel_prix");
                Blob imageBlob = result.getBlob("image");

                hotel hotel1;
                hotel1 = new hotel();
                hotel1.setId(hid);
                hotel1.setNom(hnom);
                hotel1.setVille(ville);
                hotel1.setNbre_chambre(chambre_s);
                hotel1.setNbre_chambre2(chambre_d);

                hotel1.setEconomie(economie);
                hotel1.setNbre_etoile(hetoile);
                hotel1.setPrix(prix);
                hotel1.setImage(imageBlob);
                hotel1.setGrid(this);
                hotelDataList.add(hotel1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }//return hotelDataList;
        cardlistData.clear();
        cardlistData.addAll(hotelDataList);
        switch(typeTrie){
            case "trie par prix":
                cardlistData.sort(Comparator.comparingInt(hotel::getPrix));
                break;
            case "trie par etoile":
                cardlistData.sort(Comparator.comparingInt(hotel::getNbre_etoile));
                break;
            /*case "trie par diver":
                Collections.sort(cardlistData, Comparator.comparingInt(hotel -> hotel.getSizee()+1));
                Collections.reverse(cardlistData);
                break;*/
            case "trie par economie":
                cardlistData.sort(Comparator.comparingInt(hotel::getEconomie));
                Collections.reverse(cardlistData);
                break;

            default:break;
        }
        int row = 0;
        int column = 0;
        grid.getChildren().clear();
        grid.getRowConstraints().clear();
        grid.getColumnConstraints().clear();
        for (int q = 0; q < cardlistData.size(); q++) {
            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("/mastergameengine/View/items.fxml"));
                AnchorPane pane = load.load();
                // ImageView image = new ImageView((Element) new Image("file:"+cardlistData.get(q).getUrl()));
                //  setNode(pane);
                hotels1Controller cardC = load.getController();
                cardC.setData(cardlistData.get(q));
                if (column == 100) {
                    column = 0;
                    row += 1;}
                grid.add(pane, column++, row);
                GridPane.setMargin(pane, new Insets(10));
            } catch (Exception e) {
                e.printStackTrace();}}
    }
    public void trierParPrix() {
        String type="trie par prix";
        menuDisplayCard(this.getVilles(),type);
    }
    public void trierParEtoiles() {
        String type="trie par etoile";
        menuDisplayCard(this.getVilles(),type);}
    public void triParEconomie() {
        String type="trie par economie";
        menuDisplayCard(this.getVilles(),type);}


    private void setupSelectionListener() {
        table1.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                handleSelectedChambre(newSelection);
            }
        });
    }

    private void handleSelectedChambre(chambre selectedChambre) {
        String description = selectedChambre.getDescription();
        double price = selectedChambre.getPrice();
        String type = selectedChambre.getTyp();


        chambre ch=new chambre(description,price);
        ch.setTyp("type choisi est"+type);
        System.out.println("type choisi est"+type);
        ajouterChambreSelectionnee(ch);
        // Faites ce que vous voulez avec les informations récupérées ici
        // Par exemple, stockez-les dans des variables globales ou un objet
        // ...
    }
    @FXML
    void selectroom(ActionEvent event) {
        setupSelectionListener();
    }
    public void trier_hotel(){
        ObservableList<String> options = FXCollections.observableArrayList("Tri plus reservé","Tri par étoiles","Tri par prix","trie par economie","trie par plus de divertissement");
        choicetri.setItems(options);
        // Ajouter un gestionnaire d'événements pour la ChoiceBox pour effectuer le tri
        choicetri.setOnAction((event) -> {
            String selectedOption = choicetri.getValue();

            if (selectedOption.equals("Tri par étoiles")) {
                trierParEtoiles();
            } else if (selectedOption.equals("Tri par prix")) {
                trierParPrix();

            } else if (selectedOption.equals("trie par economie")) {
                triParEconomie();}
        });
    }
    public void trier_chambre(){
        ObservableList<String> options = FXCollections.observableArrayList("all","double","simple","triple","groupe de personne");
        typex.setItems(options);
        // Ajouter un gestionnaire d'événements pour la ChoiceBox pour effectuer le tri
        typex.setOnAction((event) -> {
            String selectedOption = typex.getValue();
            if (selectedOption.equals("all")) {
                chambre_db(null);            }
            if (selectedOption.equals("simple")) {
                trie_chambre("simple");
            } else if (selectedOption.equals("double")) {
                trie_chambre("double");
            } else if (selectedOption.equals("triple")) {
                trie_chambre("triple");
            } else if (selectedOption.equals("groupe de personne")) {
                trie_chambre("groupe de personne");}
        });
    }

//    public hotel envoyerHotel(){
//return hott;
//        for(chambre h:chambresSelectionnees){
//            System.out.println(h.getDescription()+" "+h.getPrice()+" "+h.getIn()+" "+h.getOut());
//        }


    public void afficherAll(){
        System.out.println("le id d'hotel: "+hott.getId()+hott.getNom()+hott.getPrix()+hott.getType());
        for(chambre h:chambresSelectionnees){
            System.out.println(h.getDescription()+" "+h.getPrice()+" "+h.getIn()+" "+h.getOut());
        }
//
    }


    public List<Billet> listebillet=new ArrayList<Billet>();
    public void setbillet(List<Billet> showBillet) {
        listebillet.addAll(showBillet);
    }

    public String depart;
    public String arrive;

    public void setdepart(String depar) {
        depart=depar;
    }

    public void setarrive(String arriv) {
        arrive=arriv;
    }

    public void revenir(String text, String text1, int hotel) {
        this.hotel_choisi=hotel;
        System.out.println("l'hotel que vous avez choisi "+hotel_choisi);

        setdepart(text);
        setarrive(text1);
        grid.setVisible(false);
        scroll2.setVisible(true);
        choicetri.setVisible(false);
        btnback.setVisible(false);
        btnback2.setVisible(true);
        chambre_db(null);
    }

    public void setuser(String username) {
        user=username;
    }

    private static class DatePickerDateCell extends DateCell {
        @Override
        public void updateItem(LocalDate item, boolean empty) {
            super.updateItem(item, empty);
            setDisable(empty || item.isBefore(LocalDate.now()));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        closebtn.setOnMouseClicked(this::handleCloseBtnClick);
        in.setValue(LocalDate.now());
        usernameLabel.setText(user);
        in.setDayCellFactory(picker -> new DatePickerDateCell());

        out.setDayCellFactory(picker -> new DatePickerDateCell());

        out.setValue(LocalDate.now());

        trier_hotel();
        trier_chambre();
        ChooseController ch=new ChooseController();
        System.out.println("waaaaaa"+ch.nom);
        //  menuDisplayCard("null");
    }
    public void handleCloseBtnClick(MouseEvent event) {
        // Handle close button click event
        Platform.exit();
        System.exit(0);
    }
    // ADDED
    public void setUsername(String username) {
        usernameLabel.setText(user);

        this.username = username;
        updateUsernameLabel();
    }

    private void updateUsernameLabel() {
        // Update the usernameLabel with the username
        usernameLabel.setText(username);
    }
}
