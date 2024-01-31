package Admin.Model.HotelsClasse;

public class HotelIcon {
    private int hotelId;
    private int iconId;

    public HotelIcon() {}

    public HotelIcon(int hotelId, int iconId) {
        this.hotelId = hotelId;
        this.iconId = iconId;
    }
    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
}