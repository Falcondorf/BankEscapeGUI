package gui;

import controller.*;
import java.io.IOException;
import model.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author jackd
 */
public class SoloFxController extends Application implements Initializable {

    private AnchorPane anchor;
    private GridPane gpStatic;
    private GridPane gpDynamic;
    private StackPane stackPane;

    public SoloFxController() {
        this.anchor = new AnchorPane();
        this.gpStatic = new GridPane();
        this.gpDynamic = new GridPane();
        this.stackPane = new StackPane();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        insertImages();
        stackPane.getChildren().add(gpStatic);        
        anchor.getChildren().add(stackPane);
        Parent root = anchor;
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    
    
    
    
    
    
    private void insertImages() throws IOException {
        Game g = new Game("levels/Niveau2.txt");
        
        for (int i = 0; i < g.getMaze().getSquare().length; i++) {
            for (int j = 0; j < g.getMaze().getSquare()[0].length; j++) {
                ImageView img = new ImageView();
                switch (g.getMaze().getSquare()[i][j].getType()) {
                    case "wall":

                        // img = new ImageView(new Image(StartWindowController.class.getResourceAsStream(".\\src\\images\\sis.jpg")));
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
                this.gpStatic.add(img, j, i);
            }
            //  str += "\n";
        }
    }

}
