
package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class hotels1Controller {

    @FXML
    private Label desc;

    @FXML
    private Label economieid;
    @FXML
    private ImageView idimage;
    @FXML
    private Label nb_chambre;
    @FXML
    private Label nom;
    @FXML
    private Label prix;



    @FXML
    void handleButtonAction(ActionEvent event){
        int id2=this.id;
        hotel h=new hotel();
        h=hot;
        this.getGridController();

        gridController.mettreJour(h);/*
        Parent root =FXMLLoader.load(this.getClass().getResource("afficherDetails.fxml"));
        Stage stage;
       int id=hot.getId();
       afficherDetailsController detail=new afficherDetailsController();
        detail.setId_hotel(id);
        System.out.println(detail.getId_hotel());

        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
         Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();*/
    }
    private int id;
    private Image imag;
    private String nome;
    private int nbre_etoile;
    private String villee;
    private int nbre_chambre;
    private int economiee;
    private int prixe;
    //
    // private List<String> icons = new ArrayList<>();
    // private List<String> libelleIcons = new ArrayList<>();
    private hotel hot;
    int size;

    private hotelsController gridController; // Référence vers le GridController

    public hotelsController getGridController() {
        return gridController;
    }
    public void setGridController(hotelsController gridController) {
        this.gridController = gridController;
    }

    public void setData(hotel hot) {
        this.setGridController(hot.getGrid());
        this.hot = hot;
        this.id = hot.getId();
        this.villee = hot.getVille();
        this.nome = hot.getNom();
        this.nbre_chambre = hot.getNbre_chambre1();
        this.economiee = hot.getEconomie();
        this.nbre_etoile = hot.getNbre_etoile();
        this.prixe = hot.getPrix();
        System.out.println(hot.getPrix());
        //M this.icons = hot.getIcons();
        //M this.libelleIcons = hot.getLibelleIcons();
        /*if (this.nbre_etoile != 0) {
            ImageView[] etoiles = {etoile1, etoile2, etoile3, etoile4, etoile5}; // Tableau des ImageView

            for (int i = 0; i < 5; i++) {
                if (i < this.nbre_etoile) {
                    String path = "file:MasterGameEngine/src/icons/star.png";
                    Image etoile = new Image(path);
                    etoiles[i].setImage(etoile); // Affichage de l'étoile dans l'ImageView correspondant
                } else {
                    // Si i est supérieur ou égal à nbre_etoile, masquer l'ImageView restant
                    etoiles[i].setImage(null);
                }
            }
        }*/

    /*   if (!icons.isEmpty()) {
            int numero=icons.size();
            this.size=numero;
                    ImageView[] iconv={icon1,icon2,icon3};
            Label[] labels={caract1,caract2,caract3};
            for(int i=0;i<3;i++){
                if(i<numero){
                String pathe = "File:" + icons.get(i);
               Image iconImage = new Image(pathe);
                iconv[i].setImage(iconImage);
                    labels[i].setText(libelleIcons.get(i));
                }
                else {
                    iconv[i].setImage(null); // Masquer ou désactiver l'ImageView si moins d'icônes sont disponibles
                }

            }}*/
        this.nb_chambre.setText(String.valueOf(nbre_chambre));
        this.desc.setText("L'hôtel XYZ est une destination de premier choix pour les voyageurs en quête de luxe et de confort. Niché au cœur d'une oasis urbaine, cet établissement cinq étoiles offre une expérience incomparable"+nome+nbre_etoile+villee+economiee+prixe);
        String path = "File:" + hot.getUrl();
        this.imag = new Image(path);
        //this.ville.setText(hot.getVille());
        this.nom.setText("Welcom To "+hot.getNom()+"\n"+" Find your room Here");
//        this.idimage.setImage(imag);
        this.economieid.setText(hot.getEconomie()+"%");
        this.prix.setText(hot.getPrix()+"Dh");
    }


}