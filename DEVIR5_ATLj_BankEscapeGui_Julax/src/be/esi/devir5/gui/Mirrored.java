/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.esi.devir5.gui;

import be.esi.devir5.model.Game;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author g39293
 */
public class Mirrored extends Application implements Initializable, Observer{
    
    private StackPane spView;
    private Pane paneStatic;
    private Pane paneDynamic;
    private Scene scene;
    private Stage stage;
    private Game mirroredGame;
    
    public Mirrored (Game g){
        mirroredGame = new Game(g);
        paneStatic = new Pane();
        paneDynamic = new Pane();
        spView = new StackPane();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        
        stage = primaryStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void update(Observable o, Object arg) {
        
    }
}
