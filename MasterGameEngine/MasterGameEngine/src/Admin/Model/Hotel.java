package Admin.Model;

import Admin.Model.HotelsClasse.HotelIcon;
import Admin.Model.HotelsClasse.Images;
import Admin.Model.HotelsClasse.Room;

import java.util.List;

public class Hotel {
    private int id;
    private String city;
    private String name;
    private int nb_room_s;
    private int nb_room_d;
    private int nb_stars;
    private int price;
    private int economy;
    private List<Images> images;
    private List<Room> rooms;
    private List<HotelIcon> icons;

    public Hotel() {
    }

    public Hotel(int id, String city, String name, int nb_room_s, int nb_room_d, int nb_stars, int economy, List<Images> images, List<Room> rooms, List<HotelIcon> icons) {
        this.id = id;
        this.city = city;
        this.name = name;
        this.nb_room_s = nb_room_s;
        this.nb_room_d = nb_room_d;
        this.nb_stars = nb_stars;
        this.economy = economy;
        this.images = images;
        this.rooms = rooms;
        this.icons = icons;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNb_room_s() {
        return nb_room_s;
    }

    public void setNb_room_s(int nb_room_s) {
        this.nb_room_s = nb_room_s;
    }

    public int getNb_room_d() {
        return nb_room_d;
    }

    public void setNb_room_d(int nb_room_d) {
        this.nb_room_d = nb_room_d;
    }

    public int getNb_stars() {
        return nb_stars;
    }

    public void setNb_stars(int nb_stars) {this.nb_stars = nb_stars;}

    public int getEconomy() {
        return economy;
    }

    public void setEconomy(int economy) {
        this.economy = economy;
    }

    public List<Images> getImages() {
        return images;
    }

    public void setImages(List<Images> images) {
        this.images = images;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<HotelIcon> getIcons() {
        return icons;
    }

    public void setIcons(List<HotelIcon> icons) {
        this.icons = icons;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}



