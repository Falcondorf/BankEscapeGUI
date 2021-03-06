package be.esi.devir5.gui;

import be.esi.devir5.model.BankEscapeException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author jackd
 */
public class StartWindowController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private GridPane gridPaneStatic;

    @FXML
    private TextField txtFieldLevel;
    @FXML
    private void handleButtonSolo(ActionEvent event) throws IOException {

        try {

            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            SoloFxController sfc = new SoloFxController(txtFieldLevel.getText(), false);
            sfc.start(stage);

        } catch (BankEscapeException | IOException e) {
            System.out.println(e);
        }

    }

    @FXML
    private void handleButtonEdit(ActionEvent event) {
        try {

            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Editor ec = new Editor();
            ec.start(stage);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    private void quitWindowEvent(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void addImageView(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
