package Admin.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ItemController {

    @FXML
    private Label nameLabel;

    @FXML
    private Label numberLabel;

    public void setName(String name) {
        nameLabel.setText(name);
    }

    public void setNumber(String number) {
        numberLabel.setText(number);
    }
}
