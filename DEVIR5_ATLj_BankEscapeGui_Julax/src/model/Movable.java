package model;

import javafx.beans.Observable;

/**
 * Classe mère représentant toute entité déplacable
 *
 * @author pikirby45
 */
public class Movable{

    private Position pos;

    /**
     * Constructeur de la classe. Lui donne une position géographique
     *
     * @param position La position du déplacable
     */
    public Movable(Position position){
        this.pos = position;
    }

    /**
     * Modifie la position du déplacable en fonction d'une direction
     *
     * @param direction La direction du mouvement
     */
    public void move(Direction direction) {
        this.pos.setRow(this.pos.getRow() + direction.getDecalX());
        this.pos.setColumn(this.pos.getColumn() + direction.getDecalY());
    }

    /**
     * Accède à la ligne où se trouve le déplaçable
     *
     * @return La ligne
     */
    public int getRow() {
        return pos.getRow();
    }

    /**
     * Accède à la colonne où se trouve le déplaçable
     *
     * @return La colonne
     */
    public int getColumn() {
        return pos.getColumn();
    }

}
