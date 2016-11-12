package view;

import model.*;
import java.io.IOException;
import controller.ThreadEnemy;
import controller.ThreadPlayer;

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
            Game g = new Game("levels/Niveau2.txt");
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

        } catch (BankEscapeException ex) {
            System.out.println(ex);
        }

    }

}
