package gui;

import controller.*;
import java.io.IOException;
import model.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private void handleButtonSolo(ActionEvent event) throws IOException {

        try {

            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            SoloFxController sfc = new SoloFxController();
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
            EditorController ec = new EditorController();
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
