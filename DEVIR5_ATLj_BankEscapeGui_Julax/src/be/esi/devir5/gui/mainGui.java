
package be.esi.devir5.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author jackd
 */
public class mainGui extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
      //  Game game = new Game("levels/Niveau2.txt");
        Parent root = FXMLLoader.load(getClass().getResource("StartWindow.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
