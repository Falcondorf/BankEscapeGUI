package controller;

import model.Direction;
import model.Game;
import java.util.Scanner;

/**
 * Classe qui gère le thread généralisant les actions effectuée par le joueur
 * dans un labyrinthe
 *
 * @author pikirby45
 */
public class ThreadPlayer extends Thread {

    private Game g;
    private Scanner clavier = new Scanner(System.in);

    /**
     * Constructeur du thread du joueur
     *
     * @param niveau
     */
    public ThreadPlayer(Game niveau) {
        this.g = niveau;
    }

    /**
     * Gere la succession des deplacements du joueur.
     */
    @Override
    public void run() {
        while (!g.isLost()) {
            System.out.println(g.getMaze());
            try {
                String str = clavier.next();
                switch (str) {
                    case "z":
                        g.getMaze().movePlayer(Direction.UP);
                        break;
                    case "s":
                        g.getMaze().movePlayer(Direction.DOWN);
                        break;
                    case "d":
                        g.getMaze().movePlayer(Direction.RIGHT);
                        break;
                    case "q":
                        g.getMaze().movePlayer(Direction.LEFT);
                        break;
                    default:
                }
                if (!g.getMaze().isCaught()) {
                    sleep(200);
                }
            } catch (InterruptedException ex) {
                System.out.println("ERROR");
            }
        }
    }
}
