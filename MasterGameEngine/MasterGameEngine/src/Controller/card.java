package Controller;


public class card {
    private String transport;
    private String dateString;
    private String hotellx;
    private String hotelnbretoilex;
    private String nbrv;
    private String pricex;
    private String prixString;

    private String typex;

    private String villeaString;
    private String villedString;

    private String villex;
    public card() {
    }

    public card(String dateString, String hotellx, String hotelnbretoilex, String nbrv, String pricex, String prixString, String typex, String villeaString, String villedString, String villex) {
        this.dateString = dateString;
        this.hotellx = hotellx;
        this.hotelnbretoilex = hotelnbretoilex;
        this.nbrv = nbrv;
        this.pricex = pricex;
        this.prixString = prixString;
        this.typex = typex;
        this.villeaString = villeaString;
        this.villedString = villedString;
        this.villex = villex;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getHotellx() {
        return hotellx;
    }

    public void setHotellx(String hotellx) {
        this.hotellx = hotellx;
    }

    public String getHotelnbretoilex() {
        return hotelnbretoilex;
    }

    public void setHotelnbretoilex(String hotelnbretoilex) {
        this.hotelnbretoilex = hotelnbretoilex;
    }

    public String getNbrv() {
        return nbrv;
    }

    public void setNbrv(String nbrv) {
        this.nbrv = nbrv;
    }

    public String getPricex() {
        return pricex;
    }

    public void setPricex(String pricex) {
        this.pricex = pricex;
    }

    public String getPrixString() {
        return prixString;
    }

    public void setPrixString(String prixString) {
        this.prixString = prixString;
    }

    public String getTypex() {
        return typex;
    }

    public void setTypex(String typex) {
        this.typex = typex;
    }

    public String getVilleaString() {
        return villeaString;
    }

    public void setVilleaString(String villeaString) {
        this.villeaString = villeaString;
    }

    public String getVilledString() {
        return villedString;
    }

    public void setVilledString(String villedString) {
        this.villedString = villedString;
    }

    public String getVillex() {
        return villex;
    }

    public void setVillex(String villex) {
        this.villex = villex;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }
}
