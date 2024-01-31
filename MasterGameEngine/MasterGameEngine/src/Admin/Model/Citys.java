package Admin.Model;


public class Citys {
    private int CityId;
    private String CityName;

    public Citys() {
    }

    public Citys(int CityId, String CityName) {
        this.CityId = CityId;
        this.CityName = CityName;
    }

    public int getCityId() {
        return CityId;
    }
    public void setCityId(int cityId) {
        CityId = cityId;
    }
    public String getCityName() {
        return CityName;
    }
    public void setCityName(String cityName) {
        CityName = cityName;
    }
}