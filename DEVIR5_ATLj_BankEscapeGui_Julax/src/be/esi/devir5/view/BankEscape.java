package be.esi.devir5.view;

import be.esi.devir5.model.Game;
import java.io.IOException;
import be.esi.devir5.controller.ThreadEnemy;
import be.esi.devir5.controller.ThreadPlayer;

/**
 *
 * @author jackd
 */
public class BankEscape {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
//            Maze maze = new Maze(5,5);
//            Position p = new Position(0, 0);
//            maze.displace(p, Direction.UP);
//            System.out.println(p.getRow());
//            
           Game g = new Game("Niveau3.txt");
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

}
