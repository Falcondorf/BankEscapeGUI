package be.esi.devir5.gui;

import be.esi.devir5.model.BankEscapeException;
import be.esi.devir5.model.Game;
import be.esi.devir5.model.Direction;
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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author jackd
 */
public class SoloFxController extends Application implements Initializable, Observer {

    private AnchorPane anchor;
    private Scene scene;
    private Pane paneStatic;
    private Pane paneDynamic;
    private StackPane stackPane;
    private Game g;
    private Stage stage;
    private String nameLevel;
    private ThreadEnemy te;

    public SoloFxController(String nameLevel, boolean mirror) {
        this.anchor = new AnchorPane();
        this.paneStatic = new Pane();
        this.paneDynamic = new Pane();
        this.stackPane = new StackPane();
        this.nameLevel = nameLevel;
    }

    @Override
    public void start(Stage primaryStage) throws BankEscapeException, IOException {
        initWindow(primaryStage);
    }

    private void initWindow(Stage primaryStage) throws IOException, BankEscapeException {
        VBox menuWindoEtc = new VBox();
        MenuBar mbMenu = new MenuBar();
        Menu menuContoller = new Menu("Controller");
        Menu menuCheats = new Menu("Cheats");
        MenuItem mirror = new MenuItem("Mirror");
        MenuItem cheat1 = new MenuItem("Unlock all items");
        MenuItem cheat2 = new MenuItem("Ghost mode");
        MenuItem cheat3 = new MenuItem("Kill all enemies");
        mirror.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Mirrored m = new Mirrored(g);
                    m.start(new Stage());
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        });
        cheat1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                g.getMaze().giveAllToPlayer();
            }
        });
        cheat2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                g.getMaze().goGhost();
            }
        });
        cheat3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                g.getMaze().killAllEnemies();
            }
        });
        menuContoller.getItems().add(mirror);
        menuCheats.getItems().addAll(cheat1, cheat2, cheat3);
        mbMenu.getMenus().addAll(menuContoller, menuCheats);

        g = new Game(nameLevel);
        g.getMaze().addObserver(SoloFxController.this);

        stage = primaryStage;

        insertImages();

        stackPane.getChildren().add(paneStatic);
        stackPane.getChildren().add(paneDynamic);
        anchor.getChildren().add(mbMenu);
        anchor.getChildren().add(stackPane);
        menuWindoEtc.getChildren().addAll(mbMenu, stackPane);

        scene = new Scene(menuWindoEtc);
        stage.setScene(scene);
        stage.show();

        te = new ThreadEnemy(g);
        te.start();

        setKeysMovement();
    }

    private void setKeysMovement() {
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
    }

    private void insertImages() throws IOException {
        for (int i = 0; i < g.getMaze().getSquares().length; i++) {
            for (int j = 0; j < g.getMaze().getSquares()[0].length; j++) {
                ImageView img;
                switch (g.getMaze().getSquares()[i][j].getType()) {
                    case "wall":
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

            }
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
                        refreshPane();

                        if (g.getMaze().getNextLevelName().equals("END")) {
                            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                            alert2.setTitle("Vous avez terminé le Jeu");
                            alert2.setHeaderText("Dernier niveau achevé");
                            alert2.setContentText("Bravo, vous êtes un voleur ... Content ?");
                            alert2.showAndWait();
                            System.exit(0);
                        }
                        anchor.getChildren().clear();
                        stackPane.getChildren().clear();
                        anchor = new AnchorPane();
                        paneStatic = new Pane();
                        paneDynamic = new Pane();
                        stackPane = new StackPane();
                        nameLevel = g.getMaze().getNextLevelName();

                        try {
                            initWindow(stage);
                        } catch (IOException ex) {
                            System.out.println(ex);
                        }

                    } else if (!g.isLost()) {
                        refreshPane();
                    } else if (g.isLost()) {
                        refreshPane();
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
                    case "exit":
                        img = new ImageView("file:src/images/exit.png");
                        setStaticImage(img, j, i);
                        break;
                    case "floor":
                        if (g.getMaze().getSquares()[i][j].isLighted()) {
                            img = new ImageView("file:src/images/floorLight.png");
                            setDynamicImage(img, j, i);
                        }
                        if (g.getMaze().getSquares()[i][j].hasDrill()) {
                            img = new ImageView("file:src/images/drill.png");
                            setDynamicImage(img, j, i);
                        }
                        if (g.getMaze().getSquares()[i][j].hasEnemy()) {
                            switch (g.getMaze().getEnemyDir(i, j)) {
                                case UP:
                                    img = new ImageView("file:src/images/guardN.gif");
                                    setDynamicImage(img, j, i);
                                    break;
                                case DOWN:
                                    img = new ImageView("file:src/images/guardS.gif");
                                    setDynamicImage(img, j, i);
                                    break;
                                case LEFT:
                                    img = new ImageView("file:src/images/guardO.gif");
                                    setDynamicImage(img, j, i);
                                    break;
                                case RIGHT:
                                    img = new ImageView("file:src/images/guardE.gif");
                                    setDynamicImage(img, j, i);
                                    break;
                                default:
                            }

                        }
                        if (g.getMaze().getSquares()[i][j].hasKey()) {
                            img = new ImageView("file:src/images/key.png");
                            setDynamicImage(img, j, i);
                        }
                        if (g.getMaze().getSquares()[i][j].hasPlayer()) {
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
            }
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}
