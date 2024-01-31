package Module;

import java.time.LocalDate;

public class Billet {
    private String villeDepart;
    private String villeArrivee;
    private LocalDate dateDepart;
    private LocalDate dateRetour;
    private int nombreVoyageurs;

    private String Type;

    private double prix;

    // Constructeur
    public Billet(int id,String villeDepart, String villeArrivee, LocalDate dateDepart, LocalDate dateRetour,double prix, int nombreVoyageurs,String Type) {
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.dateDepart = dateDepart;
        this.dateRetour = dateRetour;
        this.nombreVoyageurs = nombreVoyageurs;
        this.prix=prix;
        this.Type=Type;
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

    public String getType() {
        return Type;
    }
}
