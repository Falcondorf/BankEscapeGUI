package be.esi.devir5.model;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe d'un niveau de jeu.
 *
 * @author pikirby45
 */
public class Game {

    private Maze maze;

    /**
     * Constructeur de niveau à partir d'un numéro qui sera lu pour construire
     * le niveau
     *
     * @param nameFirstLevel Le nom du niveau
     * @throws be.esi.devir5.model.BankEscapeException
     * @throws IOException Si une lecture est erronée
     */
    public Game(String nameFirstLevel) throws BankEscapeException {
        // trouver moyen de construire le maze avant la lecture de fichier 
        try {
            maze = new Maze(nameFirstLevel);
        } catch (Exception e) {
            throw new BankEscapeException(e.getMessage());
        }

    }
    
    public Game(Game g){
        try {
            maze = new Maze(g.getMaze(), 0, 0);
        } catch (BankEscapeException ex) {
            System.out.println("Recopie échoué... "+ex);
        }
    }

    /**
     * Accède au labyrinth
     *
     * @return Le labyrinth
     */
    public Maze getMaze() {
        return maze;
    }

    /**
     * Vérifie qu'un niveau s'est terminé ou non
     *
     * @return Vrai lorsqu'un joueur s'est fait voir par un vigile ou s'il a
     * rapporté le butin
     */
    public boolean isLost() {
        return this.maze.isCaught();
    }

    public boolean endLevel() {
        if (this.maze.isEscaped()) {
            try {
                return true;
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex);
            }
        }
        return false;

    }
}
