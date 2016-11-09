package be.esi.devir5.controller;

import be.esi.devir5.model.Game;

/**
 * Classe qui gère le thread généralisant les actions effectuée par les ennemis
 * dans un labyrinthe
 *
 * @author jackd
 */
public class ThreadEnemy extends Thread {

    private Game g;

    /**
     * Constructeur du tread ennemi.
     *
     * @param niveau
     */
    public ThreadEnemy(Game niveau) {
        this.g = niveau;

    }

    /**
     * Gere la succession des deplacements ennemi
     */
    @Override
    public void run() {
        while (!g.isLost()) {
            try {
                g.getMaze().autoMoveEnemy();
                System.out.println(g.getMaze());
                sleep(1000);
            } catch (InterruptedException ex) {
                System.out.println("error2");
            }

        }
    }

}
