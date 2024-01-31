package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static util.DBCon.connect;

public class showHotelsController implements Initializable {

    @FXML
    private Label Reduce;

    @FXML
    private GridPane cardlayout;


    @FXML
    private Label closebtn;
    @FXML
    private ImageView icon1;

    @FXML
    private ImageView icon2;

    @FXML
    private ImageView icon3;

    @FXML
    private ImageView icon4;

    private ObservableList<card> infoHistorique = FXCollections.observableArrayList();

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
    private ImageView imageview;

    public void HistoriqueFromDB(String user) {

// Connexion à la base de données (exemple avec MySQL)
        ObservableList<card> hotelDataList = FXCollections.observableArrayList();
        try {
            System.out.println("vous etes dans HistoriqueFromDB()"+user);
            Connection con=connect();
            String sql="select * from historique where username = ?";
            //Statement stmn=con.createStatement();
            PreparedStatement stmn= (PreparedStatement) con.prepareStatement(sql);
            stmn.setString(1,user );

            ResultSet rs=stmn.executeQuery();

            card objet;

            int row = 0;
            int column = 0;
            cardlayout.getChildren().clear();
            cardlayout.getRowConstraints().clear();
            cardlayout.getColumnConstraints().clear();
            while(rs.next()) {
                objet = new card();
                //  rs.getString("username");
                objet.setDateString(rs.getString("date_depart"));
                objet.setVilledString(rs.getString("ville_depart"));
                objet.setVilleaString(rs.getString("ville_destination"));
                objet.setNbrv(rs.getString("nbre_voyageur"));
                objet.setTransport(rs.getString("transport"));
                objet.setPrixString(rs.getString("prix_totale"));
                //objet.setVillex(rs.getString("hotel_ville"));
                objet.setHotellx("Nom Hotel: "+rs.getString("hotel_nom"));
                objet.setHotelnbretoilex(rs.getString("nbre_etoile"));
                objet.setTypex("Type Chambre: "+rs.getString("type_chambre"));
                objet.setPricex(rs.getString("prix_totale"));
                //String prix = rs.getString("prix_totale");
                FXMLLoader loadd=new FXMLLoader();
                loadd.setLocation(getClass().getResource("/mastergameengine/View/cardHotel.fxml"));
                AnchorPane cardBox=loadd.load();

                cardHotel cardC=loadd.getController();
                cardC.setData(objet);
                if (column == 1) {
                    column = 0;
                    row += 1;}
                cardlayout.add(cardBox, column++, row);
                cardlayout.setMargin(cardBox, new Insets(5));

                hotelDataList.add(objet);

          /*  while (result.next()) {
                int hid = result.getInt("hotel_id");
                String hnom = result.getString("hotel_nom");
                String url = result.getString("hotel_url");
                int hetoile = result.getInt("hotel_nbre_etoile");
                String ville = result.getString("hotel_ville");
                int chambre = result.getInt("hotel_nbre_chambre");
                int economie = result.getInt("hotel_economie");
                int prix = result.getInt("hotel_prix");

                hotel hotel1;
                hotel1 = new hotel();
                hotel1.setId(hid);
                hotel1.setNom(hnom);
                hotel1.setVille(ville);
                hotel1.setNbre_chambre(chambre);
                hotel1.setEconomie(economie);
                hotel1.setNbre_etoile(hetoile);
                hotel1.setUrl(url);
                hotel1.setPrix(prix);
                hotel1.setGrid(this);
                hotelDataList.add(hotel1);
            }*/
            }  /* try{
                int row = 0;
                int column = 0;
                cardlayout.getChildren().clear();
                cardlayout.getRowConstraints().clear();
                cardlayout.getColumnConstraints().clear();
                for(int i=0;i<hotelDataList.size();i++){
                    System.out.println(i);
                    System.out.println(hotelDataList.get(i).getHotellx());
                    System.out.println(hotelDataList.get(i).getVillex());
                    FXMLLoader loadd=new FXMLLoader();
                    loadd.setLocation(getClass().getResource("/main/java/mastergameengine/View/cardHotel.fxml"));
                    AnchorPane cardBox=loadd.load();

                    cardHotel cardC=loadd.getController();

                    cardC.setData(hotelDataList.get(i));

                    cardlayout.add(cardBox, column++, row);
                    cardlayout.setMargin(cardBox, new Insets(10));

                }

            }catch(IOException e){
                e.printStackTrace();
            }*/
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }









    private static List<card> liste;
    public ResultSet resultat;
    public void  setData(String nom) {
        System.out.println(nom);
        this.setUsername(nom);
        System.out.println("username is "+this.getUsername());}
        /*
            try {  Connection con=connect();
                String sql="select * from historique where username = ?";
                //Statement stmn=con.createStatement();
                PreparedStatement stmn= (PreparedStatement) con.prepareStatement(sql);
                stmn.setString(1,username );

                ResultSet rs=stmn.executeQuery();
                resultat=rs;
                List<card> ls=new ArrayList<>();
                card objet;
                while(rs.next()){
                    objet=new card();
                  //  rs.getString("username");
                    objet.setDateString(rs.getString("date_depart"));
                    objet.setVilledString(rs.getString("ville_depart"));
                    objet.setVilleaString(rs.getString("ville_destination"));
                    objet.setNbrv(rs.getString("nbre_voyageur"));
                    objet.setTransport(rs.getString("transport"));
                    objet.setVillex(rs.getString("hotel_ville"));
                    objet.setHotellx(rs.getString("hotel_nom"));
                    objet.setHotelnbretoilex(rs.getString("nbre_etoile"));
                   objet.setTypex(rs.getString("type_chambre"));
                    objet.setPricex(rs.getString("prix_chambre"));
                    //String prix = rs.getString("prix_totale");

                    ls.add(objet);
                }
                infoHistorique=new ArrayList<>(ls);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }*/


    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

   /* public void avoirData(){


            try {  Connection con=connect();
                String sql="select * from historique where username = ?";
                //Statement stmn=con.createStatement();
                PreparedStatement stmn= (PreparedStatement) con.prepareStatement(sql);
                stmn.setString(1,username );

                ResultSet rs=stmn.executeQuery();
                resultat=rs;
                List<card> ls=new ArrayList<>();
                card objet;
                while(rs.next()){
                    objet=new card();
                    //  rs.getString("username");
                    objet.setDateString(rs.getString("date_depart"));
                    objet.setVilledString(rs.getString("ville_depart"));
                    objet.setVilleaString(rs.getString("ville_destination"));
                    objet.setNbrv(rs.getString("nbre_voyageur"));
                    objet.setTransport(rs.getString("transport"));
                    objet.setVillex(rs.getString("hotel_ville"));
                    objet.setHotellx(rs.getString("hotel_nom"));
                    objet.setHotelnbretoilex(rs.getString("nbre_etoile"));
                    objet.setTypex(rs.getString("type_chambre"));
                    objet.setPricex(rs.getString("prix_chambre"));
                    //String prix = rs.getString("prix_totale");

                    ls.add(objet);
                }
               // infoHistorique=new ArrayList<>(ls);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

    }*/
   @FXML
   private ImageView petiticon;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int row = 0;
        int column = 0;

        System.out.println("vous etes dans showhotels" +getUsername());
        String pathe = "file:MasterGameEngine\\src\\Controller\\images\\travel3.jpg";
        Image iconImage = new Image(pathe);
        imageview.setImage(iconImage);
        Image imge=new Image("file:MasterGameEngine\\src\\Controller\\images\\animation.gif");
        petiticon.setImage(imge);
      // icon4.setImage(new Image("file:C:\\Users\\Pc\\Desktop\\imagee\\musik.png"));
        //  scroll.setStyle("-fx-background-color: transparent; -fx-padding: 0;");


        // infoHistorique.clear();
       // infoHistorique.addAll(HistoriqueFromDB());
    }
public String villedepart;
    public void setville(String value) {
        villedepart=value;
    }
}
