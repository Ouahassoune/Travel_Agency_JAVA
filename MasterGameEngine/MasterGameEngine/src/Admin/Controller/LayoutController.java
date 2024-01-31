package Admin.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class LayoutController {
    @FXML
    private Label closebtn;

    @FXML
    private JFXButton dashboardBtn;
    @FXML
    private JFXButton customerBtn;
    @FXML
    private JFXButton transportBtn;
    @FXML
    private JFXButton hotelBtn;
    @FXML
    private VBox contentVBox;




    @FXML
    public void initialize() {
        closebtn.setOnMouseClicked(this::handleCloseBtnClick);
        goPage();
    }

    @FXML
    public void handleCloseBtnClick(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }

    public void goPage() {
        dashboardBtn.setOnAction(event -> loadFXML("../View/DashboardMain.fxml"));
        customerBtn.setOnAction(event -> loadFXML("../View/CustomerMain.fxml"));
        transportBtn.setOnAction(event -> loadFXML("../View/TransportMain.fxml"));
        hotelBtn.setOnAction(event -> loadFXML("../View/HotelMain.fxml"));
        loadFXML("../View/DashboardMain.fxml");
    }

    private void loadFXML(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Node node = loader.load();
            contentVBox.getChildren().setAll(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
