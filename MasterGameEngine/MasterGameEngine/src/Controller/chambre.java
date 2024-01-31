package Controller;


import java.util.ArrayList;
import java.util.List;

public class chambre {
    private String description;
    private double price;
    private String in;
    private String out;
    private String typ;
    private List<String> amenties = new ArrayList<>();

    public List<String> getAmenties() {
        return amenties;
    }

    public void setAmenties(List<String> amenties) {
        this.amenties = amenties;
    }

    public chambre(String description, double price, String typ) {
        this.description = description;
        this.price = price;
        this.typ = typ;
    }

    public chambre(String description, double price) {
        this.description = description;
        this.price = price;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public chambre(String description, double price, String in, String out, String typ) {
        this.description = description;
        this.price = price;
        this.in = in;
        this.out = out;
        this.typ = typ;
    }

    public chambre(String description, double price, String in, String out) {
        this.description = description;
        this.price = price;
        this.in = in;
        this.out = out;
    }


    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public String getOut() {
        return out;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public chambre() {}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
