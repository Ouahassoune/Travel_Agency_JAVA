package Controller;

import java.time.LocalDate;

public class Billet {
    private String villeDepart;
    private String villeArrivee;
    private LocalDate dateDepart;
    private LocalDate dateRetour;
    private int nombreVoyageurs;
    private double prix;

    // Constructeur
    public Billet(int id,String villeDepart, String villeArrivee, LocalDate dateDepart, LocalDate dateRetour,double prix, int nombreVoyageurs) {
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.dateDepart = dateDepart;
        this.dateRetour = dateRetour;
        this.nombreVoyageurs = nombreVoyageurs;
        this.prix=prix;
    }
    public Billet(String villeDepart, String villeArrivee, LocalDate dateDepart,double prix, int nombreVoyageurs) {
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.dateDepart = dateDepart;
        this.dateRetour = dateRetour;
        this.nombreVoyageurs = nombreVoyageurs;
        this.prix=prix;
    }

    // Getters (et éventuellement des setters si nécessaire)
    public String getVilleDepart() {
        return villeDepart;
    }

    public String getVilleArrivee() {
        return villeArrivee;
    }

    public LocalDate getDateDepart() {
        return dateDepart;
    }

    public double getPrix() {
        return prix;
    }

    public LocalDate getDateRetour() {
        return dateRetour;
    }

    public int getNombreVoyageurs() {
        return nombreVoyageurs;
    }
}
