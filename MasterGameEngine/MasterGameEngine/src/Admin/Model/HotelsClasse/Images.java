package Admin.Model.HotelsClasse;

public class Images {
    private int hotelId;
    private String imageUrl;

    public Images() {
    }

    public Images(int hotelId, String imageUrl) {
        this.hotelId = hotelId;
        this.imageUrl = imageUrl;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}


