package gui;

import controller.*;
import java.io.IOException;
import model.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
public class FXMLDocumentController implements Initializable {

    @FXML
    private Label label;
    private GridPane gridPaneStatic;

    @FXML
    private void handleButtonSolo(ActionEvent event) {
        Stage rootSolo = new Stage();
        gridPaneStatic = new GridPane();
        try {

            Game g = new Game("levels/Niveau2.txt");

            for (int i = 0; i < g.getMaze().getSquare().length; i++) {
                for (int j = 0; j < g.getMaze().getSquare()[0].length; j++) {
                    ImageView img = new ImageView();
                    switch (g.getMaze().getSquare()[i][j].getType()) {
                        case "wall":
                           
                               // img = new ImageView(new Image(FXMLDocumentController.class.getResourceAsStream(".\\src\\images\\sis.jpg")));
                                img = new ImageView("file:src/images/sis.jpg");
                            

                            break;
                        case "exit":
                          img = new ImageView("file:src/images/sis.jpg");
                            break;
                        case "floor":
                            if (g.getMaze().getSquare()[i][j].hasDrill()) {
                               img = new ImageView("file:src/images/sis.jpg");
                            } else if (g.getMaze().getSquare()[i][j].hasEnemy()) {
                               img = new ImageView("file:src/images/sis.jpg");
                            } else if (g.getMaze().getSquare()[i][j].hasKey()) {
                               img = new ImageView("file:src/images/sis.jpg");
                            } else if (g.getMaze().getSquare()[i][j].hasPlayer()) {
                                img = new ImageView("file:src/images/sis.jpg");
                            } else {
                               img = new ImageView("file:src/images/sis.jpg");
                            }
                            break;
                        case "entry":
                            if (g.getMaze().getSquare()[i][j].hasPlayer()) {
                               img = new ImageView("file:src/images/sis.jpg");
                            } else {
                               img = new ImageView("file:src/images/sis.jpg");
                            }
                            break;
                        case "vault":
                            img = new ImageView("file:src/images/sis.jpg");
                            break;
                        default:
                            System.out.println("Error : invalid element read ");
                    }
                    img.setFitHeight(80);
                    img.setFitWidth(80);
                    img.setX(j * 80);
                    img.setY(i * 80);
                    this.gridPaneStatic.add(img,j,i);
                }
              //  str += "\n";
            }
            
            Scene myScene =new Scene(gridPaneStatic);
            rootSolo.setScene(myScene);
            rootSolo.show();

//            ThreadPlayer tp = new ThreadPlayer(g);
//            ThreadEnemy te = new ThreadEnemy(g);
//            te.start();
//            tp.start();
//            while (!g.isLost()) {
//                if (g.endLevel()) {
//                    te.start();
//                    tp.start();
//                }
//            }

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
