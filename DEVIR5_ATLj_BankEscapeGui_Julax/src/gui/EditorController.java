package gui;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Maze;
import model.Square;

/**
 *
 * @author jackd
 */
public class EditorController extends Application implements Observer {

    private AnchorPane anchor;
    private GridPane gridPane;
    private Stage stage;
    private HBox root;
    private VBox info;
    private ImageView imgRb;
    private String type;
    private Maze maze;

    public EditorController() {
        maze = new Maze(10, 10);
        this.anchor = new AnchorPane();
        this.gridPane = new GridPane();
        this.stage = new Stage();
        this.root = new HBox();
        this.info = new VBox();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        for (int i = 0; i < maze.getWidth(); i++) {
            for (int j = 0; j < maze.getHeight(); j++) {
                maze.addObserver(EditorController.this);
            }

        }
        gridPane.setHgap(3);
        gridPane.setVgap(3);

        ImageView img;

        insertImages();
//        for (int i = 0; i < 10; i++) {
//            for (int j = 0; j < 10; j++) {
//                img = new ImageView("file:src/images/floor.png");
//                setStaticImage(img, i, j);
//            }
//        }
        ToggleGroup tg = new ToggleGroup();

        RadioButton rbWall = new RadioButton();
        rbWall.setToggleGroup(tg);
        RadioButton rbFloor = new RadioButton();
        rbFloor.setToggleGroup(tg);
        rbFloor.setSelected(true);
        RadioButton rbExit = new RadioButton();
        rbExit.setToggleGroup(tg);
        RadioButton rbDrill = new RadioButton();
        rbDrill.setToggleGroup(tg);
        RadioButton rbEntry = new RadioButton();
        rbEntry.setToggleGroup(tg);
        RadioButton rbPlayer = new RadioButton();
        rbPlayer.setToggleGroup(tg);
        RadioButton rbEnemy = new RadioButton();
        rbEnemy.setToggleGroup(tg);
        RadioButton rbVault = new RadioButton();
        rbVault.setToggleGroup(tg);
        RadioButton rbKey = new RadioButton();
        rbKey.setToggleGroup(tg);

        initRbImg(rbDrill, rbWall, rbPlayer, rbExit, rbEntry, rbFloor, rbEnemy, rbKey, rbVault);

        addToInfo(rbDrill, rbEnemy, rbEntry, rbExit, rbFloor, rbKey, rbPlayer, rbVault, rbWall);

        loadImgRb(rbDrill, rbEnemy, rbEntry, rbExit, rbFloor, rbKey, rbPlayer, rbVault, rbWall);

//        gridPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//               // ImageView i = (ImageView)event.getPickResult().getIntersectedNode();           
//              //  System.out.println(i);
//
//                System.out.println(event.getX()+" "+event.getY());
//            }
//        });
        gridPane.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                for (Node n : gridPane.getChildren()) {
                    System.out.println("HEY");
                    if (n instanceof ImageView) {
                        System.out.println("HEY2");
                        if (n.getBoundsInParent().contains(event.getSceneX(), event.getSceneY())) {
                            System.out.println("PD");
                            // gridPane.clearConstraints(GridPane.getRowIndex(n) ,  GridPane.getColumnIndex(n);

                            ImageView img = new ImageView();
                            img = imgRb;
                            img.setFitHeight(70);
                            img.setFitWidth(70);
                            //gridPane.getChildren().remove(event.getPickResult().getIntersectedNode());
                            //gridPane.add(img, GridPane.getColumnIndex(n), GridPane.getRowIndex(n));
                            
                            switch (type) {
                                case "wall":
                                    maze.addWall(GridPane.getRowIndex(n), GridPane.getColumnIndex(n));
                                    break;
                                case "floor":
                                    maze.addFloor(GridPane.getRowIndex(n), GridPane.getColumnIndex(n));
                                    break;
                                case "vault":
                                    maze.addVault(GridPane.getRowIndex(n), GridPane.getColumnIndex(n));
                                    break;
                                case "entry":
                                    maze.addEntry(GridPane.getRowIndex(n), GridPane.getColumnIndex(n));
                                    break;
                                case "exit":
                                    maze.addExit(GridPane.getRowIndex(n), GridPane.getColumnIndex(n));
                                    break;
                                case "player":
                                    maze.putPlayer(GridPane.getRowIndex(n), GridPane.getColumnIndex(n));
                                    break;
                                case "enemy":
                                    maze.putEnemy(GridPane.getRowIndex(n), GridPane.getColumnIndex(n));
                                    break;
                                case "drill":
                                    maze.putDrill(GridPane.getRowIndex(n), GridPane.getColumnIndex(n));
                                    break;
                                case "key":
                                    maze.putKey(GridPane.getRowIndex(n), GridPane.getColumnIndex(n));
                                    break;
                                default:

                            }

                        }
                    }
                }
            }
        });

        root.setSpacing(50);

        root.getChildren().add(gridPane);
        root.getChildren().add(info);

        anchor.getChildren().add(root);
        Parent rootP = anchor;
        Scene scene = new Scene(rootP);
        stage.setScene(scene);
        stage.show();
    }

    private void initRbImg(RadioButton rbDrill, RadioButton rbWall, RadioButton rbPlayer, RadioButton rbExit, RadioButton rbEntry, RadioButton rbFloor, RadioButton rbEnemy, RadioButton rbKey, RadioButton rbVault) {
        ImageView img;
        img = new ImageView("file:src/images/drill.png");
        img.setFitHeight(50);
        img.setFitWidth(50);
        rbDrill.setGraphic(img);
        img = new ImageView("file:src/images/wall2.png");
        img.setFitHeight(50);
        img.setFitWidth(50);
        rbWall.setGraphic(img);
        img = new ImageView("file:src/images/PlayerMovNHD.gif");
        img.setFitHeight(50);
        img.setFitWidth(50);
        rbPlayer.setGraphic(img);
        img = new ImageView("file:src/images/exit.png");
        img.setFitHeight(50);
        img.setFitWidth(50);
        rbExit.setGraphic(img);
        img = new ImageView("file:src/images/doorEntry.png");
        img.setFitHeight(50);
        img.setFitWidth(50);
        rbEntry.setGraphic(img);
        img = new ImageView("file:src/images/floor.png");
        img.setFitHeight(50);
        img.setFitWidth(50);
        rbFloor.setGraphic(img);
        img = new ImageView("file:src/images/guardN.gif");
        img.setFitHeight(50);
        img.setFitWidth(50);
        rbEnemy.setGraphic(img);
        img = new ImageView("file:src/images/key.png");
        img.setFitHeight(50);
        img.setFitWidth(50);
        rbKey.setGraphic(img);
        img = new ImageView("file:src/images/doorVault.png");
        img.setFitHeight(50);
        img.setFitWidth(50);
        rbVault.setGraphic(img);
    }

    private void addToInfo(RadioButton rbDrill, RadioButton rbEnemy, RadioButton rbEntry, RadioButton rbExit, RadioButton rbFloor, RadioButton rbKey, RadioButton rbPlayer, RadioButton rbVault, RadioButton rbWall) {
        info.getChildren().add(rbDrill);
        info.getChildren().add(rbEnemy);
        info.getChildren().add(rbEntry);
        info.getChildren().add(rbExit);
        info.getChildren().add(rbFloor);
        info.getChildren().add(rbKey);
        info.getChildren().add(rbPlayer);
        info.getChildren().add(rbVault);
        info.getChildren().add(rbWall);
    }

    private void loadImgRb(RadioButton rbDrill, RadioButton rbEnemy, RadioButton rbEntry, RadioButton rbExit, RadioButton rbFloor, RadioButton rbKey, RadioButton rbPlayer, RadioButton rbVault, RadioButton rbWall) {
        rbDrill.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imgRb = new ImageView("file:src/images/drill.png");
                type = "drill";
            }

        });
        rbEnemy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imgRb = new ImageView("file:src/images/guardN.gif");
                type = "enemy";
            }

        });
        rbEntry.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imgRb = new ImageView("file:src/images/doorEntry.png");
                type = "entry";
            }

        });
        rbExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imgRb = new ImageView("file:src/images/exit.png");
                type = "exit";
            }

        });
        rbFloor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imgRb = new ImageView("file:src/images/floor.png");
                type = "floor";
            }

        });
        rbKey.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imgRb = new ImageView("file:src/images/key.png");
                type = "key";
            }

        });
        rbPlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imgRb = new ImageView("file:src/images/PlayerMovNHD.gif");
                type = "player";
            }

        });
        rbVault.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imgRb = new ImageView("file:src/images/vault.png");
                type = "vault";
            }

        });
        rbWall.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imgRb = new ImageView("file:src/images/wall2.png");
                type = "wall";
            }

        });
    }

    private void initRadioButton() {

    }

    private void setStaticImage(ImageView img, int j, int i) {
        img.setFitHeight(70);
        img.setFitWidth(70);
        img.setX(j * 70);
        img.setY(i * 70);
        this.gridPane.add(img, i, j);
    }

    private void insertImages() {
        for (int i = 0; i < maze.getWidth(); i++) {
            for (int j = 0; j < maze.getHeight(); j++) {
                ImageView img = new ImageView();
                switch (maze.getSquares()[j][i].getType()) {
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
                        if (maze.getSquares()[i][j].hasDrill()) {
                            img = new ImageView("file:src/images/floor.png");
                            setStaticImage(img, j, i);
                            img = new ImageView("file:src/images/drill.png");
                            setStaticImage(img, j, i);
                        } else if (maze.getSquares()[i][j].hasEnemy()) {
                            img = new ImageView("file:src/images/floor.png");
                            setStaticImage(img, j, i);
                            img = new ImageView("file:src/images/guardN.gif");
                            setStaticImage(img, j, i);
                        } else if (maze.getSquares()[i][j].hasKey()) {
                            img = new ImageView("file:src/images/floor.png");
                            setStaticImage(img, j, i);
                            img = new ImageView("file:src/images/key.png");
                            setStaticImage(img, j, i);

                        } else if (maze.getSquares()[i][j].hasPlayer()) {
                            img = new ImageView("file:src/images/floor.png");
                            setStaticImage(img, j, i);
                            img = new ImageView("file:src/images/PlayerMovNHD.gif");
                            setStaticImage(img, j, i);
                        } else {
                            img = new ImageView("file:src/images/floor.png");
                            setStaticImage(img, j, i);
                        }
                        break;
                    case "entry":
                        if (maze.getSquares()[i][j].hasPlayer()) {

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

    @Override
    public void update(Observable o, Object arg) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("Observer");
                insertImages();
            }
        });

    }

}
