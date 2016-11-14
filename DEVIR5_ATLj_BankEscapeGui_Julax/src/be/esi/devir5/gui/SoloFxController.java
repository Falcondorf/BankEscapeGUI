package be.esi.devir5.gui;

import be.esi.devir5.model.BankEscapeException;
import be.esi.devir5.model.Game;
import be.esi.devir5.model.Direction;
import be.esi.devir5.controller.ThreadPlayer;
import be.esi.devir5.controller.ThreadEnemy;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author jackd
 */
public class SoloFxController extends Application implements Initializable, Observer {

    private AnchorPane anchor;
    private Pane paneStatic;
    private Pane paneDynamic;
    private StackPane stackPane;
    private Game g;
    private Stage stage;
    private String nameLevel;

    public SoloFxController(String nameLevel) {
        this.anchor = new AnchorPane();
        this.paneStatic = new Pane();
        this.paneDynamic = new Pane();
        this.stackPane = new StackPane();
        this.nameLevel = nameLevel;
    }

    @Override
    public void start(Stage primaryStage) throws BankEscapeException, IOException {
        g = new Game(nameLevel);
        g.getMaze().addObserver(SoloFxController.this);
        stage = primaryStage;
        insertImages();
        stackPane.getChildren().add(paneStatic);
        stackPane.getChildren().add(paneDynamic);
        anchor.getChildren().add(stackPane);
        Parent root = anchor;
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        ThreadPlayer tp = new ThreadPlayer(g);
        ThreadEnemy te = new ThreadEnemy(g);
        te.start();
        tp.start();

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:
                        g.getMaze().movePlayer(Direction.UP);
                        break;
                    case DOWN:
                        g.getMaze().movePlayer(Direction.DOWN);
                        break;
                    case LEFT:
                        g.getMaze().movePlayer(Direction.LEFT);
                        break;
                    case RIGHT:
                        g.getMaze().movePlayer(Direction.RIGHT);
                        break;
                    default:
                }
            }
        });
//        while (!g.isLost()) {
//            if (g.endLevel()) {
//                te.start();
//                tp.start();
//            }
//        }
    }

    private void insertImages() throws IOException {

        for (int i = 0; i < g.getMaze().getSquares().length; i++) {
            for (int j = 0; j < g.getMaze().getSquares()[0].length; j++) {
                ImageView img = new ImageView();
                switch (g.getMaze().getSquares()[i][j].getType()) {
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
                        if (g.getMaze().getSquares()[i][j].hasDrill()) {
                            img = new ImageView("file:src/images/floor.png");
                            setStaticImage(img, j, i);

                        } else if (g.getMaze().getSquares()[i][j].hasEnemy()) {
                            img = new ImageView("file:src/images/floor.png");
                            setStaticImage(img, j, i);
                        } else if (g.getMaze().getSquares()[i][j].hasKey()) {
                            img = new ImageView("file:src/images/floor.png");
                            setStaticImage(img, j, i);

                        } else if (g.getMaze().getSquares()[i][j].hasPlayer()) {
                            img = new ImageView("file:src/images/floor.png");
                            setStaticImage(img, j, i);

                        } else {
                            img = new ImageView("file:src/images/floor.png");
                            setStaticImage(img, j, i);
                        }
                        break;
                    case "entry":
                        if (g.getMaze().getSquares()[i][j].hasPlayer()) {

                            img = new ImageView("file:src/images/floor.png");
                            setStaticImage(img, j, i);
                        } else {

                            img = new ImageView("file:src/images/floor.png");
                            setStaticImage(img, j, i);
                            img = new ImageView("file:src/images/doorEntry.png");
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
        img.setFitHeight(70);
        img.setFitWidth(70);
        img.setX(j * 70);
        img.setY(i * 70);
        this.paneDynamic.getChildren().add(img);
    }

    private void setStaticImage(ImageView img, int j, int i) {
        img.setFitHeight(70);
        img.setFitWidth(70);
        img.setX(j * 70);
        img.setY(i * 70);
        this.paneStatic.getChildren().add(img);
    }

    @Override
    public void update(Observable o, Object arg) {

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                try {
                    if (g.endLevel()) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Niveau Terminé");
                        alert.setHeaderText("Vous avez réussi!!!");
                        alert.setContentText("Bravo, vous avez récupéré l'argent et vous êtes enfuis. Quel gredin...");

                        alert.showAndWait();
                        stackPane.getChildren().clear();
                        if (g.getMaze().getNextLevelName().equals("END")) {
                            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                            alert2.setTitle("Vous avez terminé le Jeu");
                            alert2.setHeaderText("Dernier niveau achevé");
                            alert2.setContentText("Bravo, vous êtes un voleur ... Content ?");
                            alert2.showAndWait();
                            System.exit(0);
                        }
                        g = new Game(g.getMaze().getNextLevelName());
                        g.getMaze().addObserver(SoloFxController.this);
                        try {
                            insertImages();
                        } catch (IOException ex) {
                            System.out.println("ololool");
                        }
                        stackPane.getChildren().add(paneStatic);
                        stackPane.getChildren().add(paneDynamic);
                        anchor.getChildren().add(stackPane);
                        Parent root = anchor;
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();

                        ThreadPlayer tp = new ThreadPlayer(g);
                        ThreadEnemy te = new ThreadEnemy(g);
                        te.start();
                        tp.start();

                    } else if (!g.isLost()) {
                        refreshPane();
                    } else if (g.isLost()) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Game Over");
                        alert.setHeaderText("GAME OVER !!!");
                        alert.setContentText("Vous avez perdu... Dommage.");

                        alert.showAndWait();
                        System.exit(0);
                    }
                } catch (BankEscapeException ex) {
                    System.out.println(ex);
                }
                stage.show();
            }

        });

    }

    private void refreshPane() throws BankEscapeException {
        paneDynamic.getChildren().clear();
        for (int i = 0; i < g.getMaze().getSquares().length; i++) {
            for (int j = 0; j < g.getMaze().getSquares()[0].length; j++) {
                ImageView img = new ImageView();
                switch (g.getMaze().getSquares()[i][j].getType()) {
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
                        if (g.getMaze().getSquares()[i][j].hasDrill()) {

                            img = new ImageView("file:src/images/drill.png");
                            setDynamicImage(img, j, i);
                        } else if (g.getMaze().getSquares()[i][j].hasEnemy()) {
                            switch (g.getMaze().getEnemyDir(i, j)) {
                                case UP:
                                    img = new ImageView("file:src/images/guardN.gif");
                                    setDynamicImage(img, j, i);
                                    if (!(g.getMaze().getSquares()[i - 1][j].getType().equals("wall"))) {
                                        img = new ImageView("file:src/images/floorLight.png");
                                        setDynamicImage(img, j, i - 1);
                                        if (!(g.getMaze().getSquares()[i - 2][j].getType().equals("wall"))) {
                                            img = new ImageView("file:src/images/floorLight.png");
                                            setDynamicImage(img, j, i - 2);
                                            if (!(g.getMaze().getSquares()[i - 3][j].getType().equals("wall"))) {
                                                img = new ImageView("file:src/images/floorLight.png");
                                                setDynamicImage(img, j, i - 3);
                                            }
                                        }
                                    }

//                                        
//                                        img = new ImageView("file:src/images/floorLight.png");
//                                        setDynamicImage(img, j, i - 1);
//                                        i--;
//                                        cpt++;
//                                    }
                                    break;
                                case DOWN:
                                    img = new ImageView("file:src/images/guardS.gif");
                                    setDynamicImage(img, j, i);
//                                    while (!g.getMaze().getSquare()[i + 1][j].getType().equals("wall")) {
//                                        img = new ImageView("file:src/images/floorLight.png");
//                                        setDynamicImage(img, j, i + 1);
//                                        i++;
//                                    }
                                    if (!(g.getMaze().getSquares()[i + 1][j].getType().equals("wall"))) {
                                        img = new ImageView("file:src/images/floorLight.png");
                                        setDynamicImage(img, j, i + 1);
                                        if (!(g.getMaze().getSquares()[i + 2][j].getType().equals("wall"))) {
                                            img = new ImageView("file:src/images/floorLight.png");
                                            setDynamicImage(img, j, i + 2);
                                            if (!(g.getMaze().getSquares()[i + 3][j].getType().equals("wall"))) {
                                                img = new ImageView("file:src/images/floorLight.png");
                                                setDynamicImage(img, j, i + 3);
                                            }
                                        }
                                    }
                                    break;
                                case LEFT:
                                    img = new ImageView("file:src/images/guardO.gif");
                                    setDynamicImage(img, j, i);
//                                    while (!g.getMaze().getSquare()[i][j - 1].getType().equals("wall")) {
//                                        img = new ImageView("file:src/images/floorLight.png");
//                                        setDynamicImage(img, j - 1, i);
//                                        j--;
//                                    }
                                    if (!(g.getMaze().getSquares()[i][j - 1].getType().equals("wall"))) {
                                        img = new ImageView("file:src/images/floorLight.png");
                                        setDynamicImage(img, j - 1, i);
                                        if (!(g.getMaze().getSquares()[i][j - 2].getType().equals("wall"))) {
                                            img = new ImageView("file:src/images/floorLight.png");
                                            setDynamicImage(img, j - 2, i);
                                            if (!(g.getMaze().getSquares()[i][j - 3].getType().equals("wall"))) {
                                                img = new ImageView("file:src/images/floorLight.png");
                                                setDynamicImage(img, j - 3, i);
                                            }
                                        }
                                    }
                                    break;
                                case RIGHT:
                                    img = new ImageView("file:src/images/guardE.gif");
                                    setDynamicImage(img, j, i);
//                                    while (!g.getMaze().getSquare()[i][j + 1].getType().equals("wall")) {
//                                        img = new ImageView("file:src/images/floorLight.png");
//                                        setDynamicImage(img, j + 1, i);
//                                        j++;
//                                    }
                                    if (!(g.getMaze().getSquares()[i][j + 1].getType().equals("wall"))) {
                                        img = new ImageView("file:src/images/floorLight.png");
                                        setDynamicImage(img, j + 1, i);
                                        if (!(g.getMaze().getSquares()[i][j + 2].getType().equals("wall"))) {
                                            img = new ImageView("file:src/images/floorLight.png");
                                            setDynamicImage(img, j + 2, i);
                                            if (!(g.getMaze().getSquares()[i][j + 3].getType().equals("wall"))) {
                                                img = new ImageView("file:src/images/floorLight.png");
                                                setDynamicImage(img, j + 3, i);
                                            }
                                        }
                                    }
                                    break;
                                default:
                            }

                        } else if (g.getMaze().getSquares()[i][j].hasKey()) {
                            img = new ImageView("file:src/images/key.png");
                            setDynamicImage(img, j, i);
                        } else if (g.getMaze().getSquares()[i][j].hasPlayer()) {
                            switch (g.getMaze().getPlayerDir(i, j)) {
                                case UP:
                                    img = new ImageView("file:src/images/PlayerMovNHD.gif");
                                    break;
                                case DOWN:
                                    img = new ImageView("file:src/images/PlayerMovSHD.gif");
                                    break;
                                case LEFT:
                                    img = new ImageView("file:src/images/PlayerMovWHD.gif");
                                    break;
                                case RIGHT:
                                    img = new ImageView("file:src/images/PlayerMovEHD.gif");
                                    break;
                                default:
                            }

                            setDynamicImage(img, j, i);
                        }
                        break;
                    case "entry":
                        if (g.getMaze().getSquares()[i][j].hasPlayer()) {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}
