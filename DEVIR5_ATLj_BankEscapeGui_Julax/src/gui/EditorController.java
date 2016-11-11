package gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author jackd
 */
public class EditorController extends Application {

    private AnchorPane anchor;
    private GridPane gridPane;
    private Stage stage;
    private HBox root;
    private VBox info;
    private ImageView imgRb;

    public EditorController() {
        this.anchor = new AnchorPane();
        this.gridPane = new GridPane();
        this.stage = new Stage();
        this.root = new HBox();
        this.info = new VBox();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        gridPane.setHgap(3);
        gridPane.setVgap(3);

        ImageView img;

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                img = new ImageView("file:src/images/floor.png");
                setStaticImage(img, i, j);
            }
        }

        ToggleGroup tg = new ToggleGroup();

        RadioButton rbWall = new RadioButton();
        rbWall.setToggleGroup(tg);
        RadioButton rbFloor = new RadioButton();
        rbFloor.setToggleGroup(tg);
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
                    if (n instanceof ImageView) {
                        if (n.getBoundsInParent().contains(event.getSceneX(), event.getSceneY())) {
                            // gridPane.clearConstraints(GridPane.getRowIndex(n) ,  GridPane.getColumnIndex(n);
                            
                            ImageView img = new ImageView();
                            img=imgRb;
                            img.setFitHeight(70);
                            img.setFitWidth(70);
                            gridPane.getChildren().remove(event.getPickResult().getIntersectedNode());
                            gridPane.add(img, GridPane.getColumnIndex(n), GridPane.getRowIndex(n));

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
            }

        });
        rbEnemy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imgRb = new ImageView("file:src/images/guardN.gif");
            }

        });
        rbEntry.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imgRb = new ImageView("file:src/images/doorEntry.png");
            }

        });
        rbExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imgRb = new ImageView("file:src/images/exit.png");
            }

        });
        rbFloor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imgRb = new ImageView("file:src/images/floor.png");
            }

        });
        rbKey.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imgRb = new ImageView("file:src/images/key.png");
            }

        });
        rbPlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imgRb = new ImageView("file:src/images/PlayerMovNHD.gif");
            }

        });
        rbVault.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imgRb = new ImageView("file:src/images/vault.png");
            }

        });
        rbWall.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imgRb = new ImageView("file:src/images/wall2.png");
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

}
