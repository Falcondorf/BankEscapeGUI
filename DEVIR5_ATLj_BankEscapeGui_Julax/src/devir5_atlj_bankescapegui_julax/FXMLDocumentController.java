package devir5_atlj_bankescapegui_julax;

import controller.*;
import java.io.IOException;
import model.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author jackd
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Label label;

    @FXML
    private void handleButtonSolo(ActionEvent event) {
        try {

            Game g = new Game("Niveau2.txt");

            for (int i = 0; i < g.getMaze().getSquare().length; i++) {
                for (int j = 0; j < g.getMaze().getSquare()[0].length; j++) {
                    ImageView img;
                    switch (g.getMaze().getSquare()[i][j].getType()) {
                        case "wall":
                            if (g.getMaze().getSquare()[i][j].hasDrill()) {
                                img = new ImageView(new Image(FXMLDocumentController.class.getResourceAsStream("")));
                            } else if (g.getMaze().getSquare()[i][j].hasEnemy()) {
                                str += "E";
                            } else if (g.getMaze().getSquare()[i][j].hasKey()) {
                                str += "K";
                            } else if (g.getMaze().getSquare()[i][j].hasPlayer()) {
                                str += "P";
                            } else {
                                str += "W";
                            }

                            break;
                        case "exit":
                            str += "S";
                            break;
                        case "floor":
                            if (g.getMaze().getSquare()[i][j].hasDrill()) {
                                str += "D";
                            } else if (g.getMaze().getSquare()[i][j].hasEnemy()) {
                                str += "E";
                            } else if (g.getMaze().getSquare()[i][j].hasKey()) {
                                str += "K";
                            } else if (g.getMaze().getSquare()[i][j].hasPlayer()) {
                                str += "P";
                            } else {
                                str += "°";
                            }
                            break;
                        case "entry":
                            if (g.getMaze().getSquare()[i][j].hasPlayer()) {
                                str += "P";
                            } else {
                                str += "I";
                            }
                            break;
                        case "vault":
                            str += "V";
                            break;
                        default:
                            System.out.println("Error : invalid element read ");
                    }

                }
                str += "\n";
            }

            ThreadPlayer tp = new ThreadPlayer(g);
            ThreadEnemy te = new ThreadEnemy(g);
            te.start();
            tp.start();
            while (!g.isLost()) {
                if (g.endLevel()) {
                    te.start();
                    tp.start();
                }
            }

        } catch (IOException | RuntimeException ex) {
            System.out.println(ex);
        }
    }

    @FXML
    private void handleButtonEdit(ActionEvent event) {

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
