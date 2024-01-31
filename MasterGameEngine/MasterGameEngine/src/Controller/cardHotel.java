package Controller;


import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class cardHotel { @javafx.fxml.FXML
@FXML
private Label dateLabel;

    @javafx.fxml.FXML
    @FXML
    private Label hotellx;
    @javafx.fxml.FXML
    @FXML
    private ImageView avion;
    @javafx.fxml.FXML
    @FXML
    private Label hotelnbretoilex;

    @javafx.fxml.FXML
    @FXML
    private Label nbrv;

    @javafx.fxml.FXML
    @FXML
    private Label pricex;

    @javafx.fxml.FXML
    @FXML
    private Label prixLabel;

    @javafx.fxml.FXML
    @FXML
    private Label typex;

    @javafx.fxml.FXML
    @FXML
    private Label villeaLabel;

    @javafx.fxml.FXML
    @FXML
    private Label villedLabel;

    @javafx.fxml.FXML
    @FXML
    private Label villex;

    @javafx.fxml.FXML
    @FXML
    private Label transport;
    @javafx.fxml.FXML
    @FXML
    private ImageView image3;

    public  void setData(card donnees){
        Image imge=new Image("file:MasterGameEngine\\src\\Controller\\images\\Animation - 1702613121394.gif");
        avion.setImage(imge);
        Image imge3=new Image("file:MasterGameEngine\\src\\Controller\\images\\icons8-history-50.png");
        image3.setImage(imge3);
        System.out.println("hiiii remplir card");

        dateLabel.setText(donnees.getDateString());
        villedLabel.setText("ville de depart "+donnees.getVilledString());
        villeaLabel.setText("ville d'arrivé "+donnees.getVilleaString());
        prixLabel.setText(donnees.getPrixString());
        nbrv.setText(donnees.getNbrv());
typex.setText(donnees.getTypex());

         villex.setText(donnees.getVillex());
         hotellx.setText(donnees.getHotellx());

       /* String pathe = "file:"+donnees.getUrl();
        Image iconImage = new Image(pathe);
        photo2.setImage(iconImage);

        star.setImage(new Image("file:C:\\Users\\Pc\\Desktop\\imagee\\stars.png"));
        String pathe2 = "file:"+donnees.getStars();
        Image iconImage2 = new Image(pathe2);
        star.setImage(iconImage2);

        nom.setText(donnees.getHotel());
        places.setText(donnees.getPlace());
        price.setText(donnees.getPrice());*/
    }
}
