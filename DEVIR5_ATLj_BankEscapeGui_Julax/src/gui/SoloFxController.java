package gui;

import controller.*;
import java.io.IOException;
import model.*;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author jackd
 */
public class SoloFxController extends Application implements Initializable, Observer {

    private AnchorPane anchor;
    private Pane gpStatic;
    private Pane gpDynamic;
    private StackPane stackPane;
    private Game g;
    private Stage stage;

    public SoloFxController() {
        this.anchor = new AnchorPane();
        this.gpStatic = new Pane();
        this.gpDynamic = new Pane();
        this.stackPane = new StackPane();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        g = new Game("levels/Niveau2.txt");
        g.getMaze().addObserver(SoloFxController.this);
        stage = primaryStage;
        insertImages();
        stackPane.getChildren().add(gpStatic);
        stackPane.getChildren().add(gpDynamic);
        anchor.getChildren().add(stackPane);
        Parent root = anchor;
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        ThreadPlayer tp = new ThreadPlayer(g);
        ThreadEnemy te = new ThreadEnemy(g);
        te.start();
        tp.start();
//        while (!g.isLost()) {
//            if (g.endLevel()) {
//                te.start();
//                tp.start();
//            }
//        }
    }

    private void insertImages() throws IOException {

        for (int i = 0; i < g.getMaze().getSquare().length; i++) {
            for (int j = 0; j < g.getMaze().getSquare()[0].length; j++) {
                ImageView img = new ImageView();
                switch (g.getMaze().getSquare()[i][j].getType()) {
                    case "wall":

                        // img = new ImageView(new Image(StartWindowController.class.getResourceAsStream(".\\src\\images\\sis.jpg")));
                        img = new ImageView("file:src/images/wall2.png");
                        setStaticImage(img, j, i);

                        break;
                    case "exit":
                        img = new ImageView("file:src/images/floor.png");
                        setStaticImage(img, j, i);
                        img = new ImageView("file:src/images/exit.png");
                        setStaticImage(img, j, i);
                        break;
                    case "floor":
                        if (g.getMaze().getSquare()[i][j].hasDrill()) {
                            img = new ImageView("file:src/images/floor.png");
                            setStaticImage(img, j, i);

                        } else if (g.getMaze().getSquare()[i][j].hasEnemy()) {
                            img = new ImageView("file:src/images/floor.png");
                            setStaticImage(img, j, i);
                            img = new ImageView("file:src/images/sis.jpg");
                        } else if (g.getMaze().getSquare()[i][j].hasKey()) {
                            img = new ImageView("file:src/images/floor.png");
                            setStaticImage(img, j, i);

                        } else if (g.getMaze().getSquare()[i][j].hasPlayer()) {
                            img = new ImageView("file:src/images/floor.png");
                            setStaticImage(img, j, i);

                        } else {
                            img = new ImageView("file:src/images/floor.png");
                            setStaticImage(img, j, i);
                        }
                        break;
                    case "entry":
                        if (g.getMaze().getSquare()[i][j].hasPlayer()) {

                            img = new ImageView("file:src/images/floor.png");
                            setStaticImage(img, j, i);
                        } else {

                            img = new ImageView("file:src/images/floor.png");
                            setStaticImage(img, j, i);
                            img = new ImageView("file:src/images/exit.png");
                            setStaticImage(img, j, i);
                        }
                        break;
                    case "vault":
                        img = new ImageView("file:src/images/vault.png");
                        setStaticImage(img, j, i);
                        break;
                    default:
                        System.out.println("Error : invalid element read ");
                }
                // setStaticImage(img, j, i);

            }
            //  str += "\n";
        }
    }

    private void setDynamicImage(ImageView img, int j, int i) {
        img.setFitHeight(80);
        img.setFitWidth(80);
        img.setX(j * 80);
        img.setY(i * 80);
        this.gpDynamic.getChildren().add(img);
    }

    private void setStaticImage(ImageView img, int j, int i) {
        img.setFitHeight(80);
        img.setFitWidth(80);
        img.setX(j * 80);
        img.setY(i * 80);
        this.gpStatic.getChildren().add(img);
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("observer");
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                refreshPane();
                stage.show();
            }

        });

    }

    private void refreshPane() {
        gpDynamic.getChildren().clear();
        for (int i = 0; i < g.getMaze().getSquare().length; i++) {
            for (int j = 0; j < g.getMaze().getSquare()[0].length; j++) {
                ImageView img = new ImageView();
                switch (g.getMaze().getSquare()[i][j].getType()) {
//                    case "wall":
//
//                        // img = new ImageView(new Image(StartWindowController.class.getResourceAsStream(".\\src\\images\\sis.jpg")));
//                        img = new ImageView("file:src/images/wall2.png");
//                        setStaticImage(img, j, i);
//
//                        break;
                    case "exit":
                        img = new ImageView("file:src/images/exit.png");
                        setStaticImage(img, j, i);
                        break;
                    case "floor":
                        if (g.getMaze().getSquare()[i][j].hasDrill()) {

                            img = new ImageView("file:src/images/drill.png");
                            setDynamicImage(img, j, i);
                        } else if (g.getMaze().getSquare()[i][j].hasEnemy()) {
                            switch (g.getMaze().getEnemyDir(i, j)) {
                                case UP:
                                    img = new ImageView("file:src/images/guardN.gif");
                                    break;
                                case DOWN:
                                    img = new ImageView("file:src/images/guardS.gif");
                                    break;
                                case LEFT:
                                    img = new ImageView("file:src/images/guardO.gif");
                                    break;
                                case RIGHT:
                                    img = new ImageView("file:src/images/guardE.gif");
                                    break;
                                default:
                            }

                            setDynamicImage(img, j, i);
                        } else if (g.getMaze().getSquare()[i][j].hasKey()) {
                            img = new ImageView("file:src/images/key.png");
                            setDynamicImage(img, j, i);
                        } else if (g.getMaze().getSquare()[i][j].hasPlayer()) {
                            img = new ImageView("file:src/images/PlayerMovNHD.gif");
                            setDynamicImage(img, j, i);
                        }
                        break;
                    case "entry":
                        if (g.getMaze().getSquare()[i][j].hasPlayer()) {
                            img = new ImageView("file:src/images/PlayerMovNHD.gif");
                            setDynamicImage(img, j, i);
                        } else {

                        }
                        break;
                    default:
                        break;
                }
                // setStaticImage(img, j, i);

            }
            //  str += "\n";
        }

    }

}
