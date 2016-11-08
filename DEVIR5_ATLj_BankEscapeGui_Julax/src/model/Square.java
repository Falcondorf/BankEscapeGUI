package model;

/**
 * Classe représentant une case du plateau de jeu.
 *
 * @author jackd
 */
public class Square {

    private String type;
    private boolean hasPlayer;
    private boolean hasEnemy;
    private boolean hasDrill;
    private boolean hasKey;

    /**
     * construit la case.
     */
    public Square() { //Floor, Wall, Vault, Entry, Exit. ==> Un vault 
        this.type = "floor";        //et une exit fermée ne sont pas reachable.
        this.hasPlayer = false;
        this.hasDrill = false;
        this.hasEnemy = false;
        this.hasKey = false;

    }

    /**
     * Indique si la case contient.
     *
     * @return true si la case contient un joueur.
     */
    public boolean hasPlayer() {
        return hasPlayer;
    }

    /**
     * Met un joueur dans la case
     */
    public void setHasPlayer() {
        this.hasPlayer = true;
    }

    /**
     * Enleve le joueur de la case.
     */
    public void removePlayer() {
        this.hasPlayer = false;
    }

    /**
     * Indique si la case possede un ennemi.
     *
     * @return true si c'est le cas.
     */
    public boolean hasEnemy() {
        return hasEnemy;
    }

    /**
     * Met un ennemi dans la case.
     */
    public void setHasEnemy() {
        this.hasEnemy = true;
    }

    /**
     * Enleve l'ennemi de la case
     */
    public void removeEnemy() {
        this.hasEnemy = false;
    }

    /**
     * Indique si la case possede une foreuse.
     *
     * @return true si c'est le cas.
     */
    public boolean hasDrill() {
        return hasDrill;
    }
    
    public boolean hasVault(){
        return type.equals("vault") ;
    }
    
    public boolean hasEntry(){
        return type.equals("entry");
    }

    /**
     * Met une foreuse dans la case.
     */
    public void setHasDrill() {
        this.hasDrill = true;
    }

    /**
     * Enleve la foreuse de la case.
     */
    public void removeDrill() {
        this.hasDrill = false;
    }

    /**
     * Indique si la case possede une clé.
     *
     * @return true si c'est le cas.
     */
    public boolean hasKey() {
        return hasKey;
    }

    /**
     * Met une clé dans la case.
     */
    public void setHasKey() {
        this.hasKey = true;
    }

    /**
     * Enleve la clé de la case.
     */
    public void removeHasKey() {
        this.hasKey = false;
    }

    /**
     * Indique si la case est atteignable.
     *
     * @param hasKey true si le joueur possede la clé de la sortie.
     * @param hasDrill true si le joueur possede la foreuse.
     * @return true si la case est atteignable.
     */
    public boolean isReachable(boolean hasKey, boolean hasDrill) { 
        return (hasDrill && type.equals("vault"))
                || (hasKey && type.equals("exit"))
                || (type.equals("floor"))
                || (type.equals("entry"));
    }

    /**
     * Renvoi le type de la case.
     *
     * @return la sorte de case dont il s'agit.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Met le type de la case à : mur.
     */
    public void setWall() {
        type = "wall";
    }

    /**
     * Met le type de la case à : voute.
     */
    public void setVault() {
        type = "vault";
    }

    /**
     * Met le type de la case à : sol.
     */
    public void setFloor() {
        type = "floor";
    }

    /**
     * Met le type de la case à : entrée.
     */
    public void setEntry() {
        type = "entry";
    }

    /**
     * Met le type de la case à : sortie.
     */
    public void setExit() {
        type = "exit";
    }

}
