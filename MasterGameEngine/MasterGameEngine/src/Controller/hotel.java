package Controller;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

public class hotel {
    private hotelsController grid;
    private int id;
    private String nom;
    private String url;
    private int nbre_etoile;
    private Blob image;
    private String ville;
    private int nbre_chambre1;
    private int nbre_chambre2;
    private int economie;
    private int prix;
    private List<String> icons = new ArrayList<>();
    private int size;
    private String type;
    private List<String> libelleIcons = new ArrayList<>();

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public hotelsController getGrid() {
        return grid;
    }

    public void setGrid(hotelsController grid) {
        this.grid = grid;
    }

    /*public void addIconLibelle(String icon, String libelleIcon) {
    icons.add(icon);
    libelleIcons.add(libelleIcon);
}*/
    public List<String> getIcons() {
        return icons;
    }
    public void setSizee(int size) {
        this.size = size;
    }
    public int getSizee() {
        return size;
    }

    public void setIcons(List<String> icons) {
        this.icons = icons;
    }

    public List<String> getLibelleIcons() {
        return libelleIcons;
    }

    public void setLibelleIcons(List<String> libelleIcons) {
        this.libelleIcons = libelleIcons;
    }
    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public hotel(){};

    public hotel(int id,String nom, String url, int nbre_etoile, String ville, int nbre_chambre, int economie, int prix) {
        this.id = id;
        this.url = url;
        this.nom = nom;
        this.nbre_etoile = nbre_etoile;
        this.ville = ville;
        this.nbre_chambre1 = nbre_chambre;

        this.economie = economie;
        this.prix = prix;
        this.size=0;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNbre_etoile() {
        return nbre_etoile;
    }

    public void setNbre_etoile(int nbre_etoile) {
        this.nbre_etoile = nbre_etoile;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }
    public int getNbre_chambre1() {
        return nbre_chambre1;
    }

    public void setNbre_chambre(int nbre_chambre1) {
        this.nbre_chambre1 = nbre_chambre1;
    }

    public int getNbre_chambre2() {
        return nbre_chambre2;
    }

    public void setNbre_chambre2(int nbre_chambre2) {
        this.nbre_chambre2 = nbre_chambre2;
    }

    public int getEconomie() {
        return economie;
    }

    public void setEconomie(int economie) {
        this.economie = economie;
    }
}
