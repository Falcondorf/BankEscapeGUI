package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un labyrinth
 *
 * @author pikirby45
 */
public class Maze {

    private Square[][] maze;
    private Player player;
    private List<Enemy> enemyList;
    private String nextLevelName;

    public String getNextLevelName() {
        return nextLevelName;
    }

    /**
     * Constructeur de labyrinth
     *
     * @param sizeRow La largeur du labyrinth
     * @param sizeColumn La longueur du labyrinth
     */
    public Maze(int sizeRow, int sizeColumn) {
        this.enemyList = new ArrayList<>();
        maze = new Square[sizeRow][sizeColumn];
        for (int i = 0; i < sizeRow; i++) {
            for (int j = 0; j < sizeColumn; j++) {
                Square s = new Square();
                maze[i][j] = s;
            }
        }
    }

    public Maze(String nameLevel) throws Exception {
        try {
            readLevel(nameLevel);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
    public Maze(Maze maze){
        //todo constructeur copie profonde
    }

    /**
     * Méthode dédiée au déplacement du joueur
     *
     * @param dir La direction vers laquelle se déplacerait le joueur
     */
    public synchronized void movePlayer(Direction dir) {
        if (moveAutorised(dir)) {
            this.removePlayer(player.getRow(), player.getColumn());
            displacePlayer(dir);
            pickSomething();
            notifyAll();
        }
    }

    public Square[][] getSquare() {
        return maze;
    }

    
    /**
     * Permet de savoir si un joueur est dans une sortie avec le butin
     *
     * @return Vrai dans le cas où il a le butin et est sur l'entrée ou la
     * sortie
     */
    public boolean isEscaped() {
        return (this.maze[this.player.getRow()][this.player.getColumn()].getType().equals("entry") && player.hasMoney())
                || (this.maze[this.player.getRow()][this.player.getColumn()].getType().equals("exit") && player.hasMoney());
    }

    /**
     * Déplace automatiquement tous les ennemis du labyrinth après analyse des
     * cases autour
     */
    public synchronized void autoMoveEnemy() {
        for (Enemy e : enemyList) {
            removeEnemy(e.getRow(), e.getColumn());
            try {
                Direction tmpMov = movementAnalysis(e);
                e.move(tmpMov);
                this.putEnemy(e.getRow(), e.getColumn());
                e.setDirection(tmpMov);
                watch(e);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        notifyAll();
    }

    public boolean isCaught() {
        return player.isCaught();
    }

    /**
     * Méthode implémentée pour afficher le labyrinth sous forme de texte
     *
     * @return La chaîne de caractères représentant le labyrinth
     */
    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                switch (maze[i][j].getType()) {
                    case "wall":
                        if (maze[i][j].hasDrill()) {
                            str += "D";
                        } else if (maze[i][j].hasEnemy()) {
                            str += "E";
                        } else if (maze[i][j].hasKey()) {
                            str += "K";
                        } else if (maze[i][j].hasPlayer()) {
                            str += "P";
                        } else {
                            str += "W";
                        }

                        break;
                    case "exit":
                        str += "S";
                        break;
                    case "floor":
                        if (maze[i][j].hasDrill()) {
                            str += "D";
                        } else if (maze[i][j].hasEnemy()) {
                            str += "E";
                        } else if (maze[i][j].hasKey()) {
                            str += "K";
                        } else if (maze[i][j].hasPlayer()) {
                            str += "P";
                        } else {
                            str += "°";
                        }
                        break;
                    case "entry":
                        if (maze[i][j].hasPlayer()) {
                            str += "P";
                        } else {
                            str += "I";
                        }
                        break;
                    case "vault":
                        str += "V";
                        break;
                    default:
                        System.out.println("Error : invalid element read ");
                }

            }
            str += "\n";
        }
        return str;
    }

    private void pickSomething() {
        if (maze[player.getRow()][player.getColumn()].hasKey()) {
            player.setHasKey(true);
            maze[player.getRow()][player.getColumn()].removeHasKey();
        }
        if (maze[player.getRow()][player.getColumn()].hasDrill()) {
            player.setHasDrill(true);
            maze[player.getRow()][player.getColumn()].removeDrill();
        }
        if (this.maze[this.player.getRow()][this.player.getColumn()].getType().equals("vault")) {
            player.setHasMoney(true);
        }
    }

    private void watch(Enemy e) {  //champ de vision 3
        if (lookingForPlayer(e)){
            player.setIsCaught();
        }
    }

    private boolean lookingForPlayer(Enemy e) {
        boolean caught = false;
        for (int i = 1; i <= 3; i++) {
            if (maze[e.getRow() + e.getDirection().getDecalX() * i][e.getColumn() + e.getDirection().getDecalY() * i].isReachable(false, false)) {
                if (maze[e.getRow() + e.getDirection().getDecalX() * i][e.getColumn() + e.getDirection().getDecalY() * i].hasPlayer()) {
                    caught = true;
                    break;
                }
            } else {
                break;
            }
        }
        return caught;
    }

    private void displacePlayer(Direction dir) {
        player.move(dir);     //deplace objet joueur
        this.putPlayer(player.getRow(), player.getColumn());  //place booléen joueur
    }

    private boolean moveAutorised(Direction dir) { //condition de sortie + est ce qu il y a un mur ?

        return isOutOfMaze(dir)
                && maze[player.getRow() + dir.getDecalX()][player.getColumn() + dir.getDecalY()].isReachable(player.hasKey(), player.hasDrill());
    }

    private boolean isOutOfMaze(Direction dir) {
        switch (dir) {
            case UP:
                return player.getRow() != 0;
            case DOWN:
                return player.getRow() != maze.length - 1;
            case LEFT:
                return player.getColumn() != 0;
            case RIGHT:
                return player.getColumn() != maze[0].length - 1;
            default:
                return false;
        }
    }

    private void addWall(int row, int column) {
        maze[row][column].setWall();
    }

    private void addVault(int row, int column) {
        maze[row][column].setVault();
    }

    private void addEntry(int row, int column) {
        maze[row][column].setEntry();
    }

    private void addExit(int row, int column) {
        maze[row][column].setExit();
    }

    private void addPlayer(int row, int column) {   //1 seule fois a l'initialisation du jeu
        Player play = new Player(new Position(row, column));
        maze[row][column].setHasPlayer();
        player = play;
    }

    private void putPlayer(int row, int column) {  //a chaque fois qu on bouge
        maze[row][column].setHasPlayer();
    }

    private void removePlayer(int row, int column) {
        maze[row][column].removePlayer();
    }

    private void addEnemy(Direction dir, int row, int column) {   //x fois a l'initialisation du jeu
        maze[row][column].setHasEnemy();
        enemyList.add(new Enemy(dir, new Position(row, column)));
    }

    private void putEnemy(int row, int column) {
        maze[row][column].setHasEnemy();
    }

    private void putDrill(int row, int column) {
        maze[row][column].setHasDrill();
    }

    private void putKey(int row, int column) {
        maze[row][column].setHasKey();
    }

    private void removeEnemy(int row, int column) {
        maze[row][column].removeEnemy();
    }

    private boolean isValid() throws Exception {
        //check mur ou entrée ou sortie autour.
        if (!checkEdge()) {  //si contour invalide ...
            return false;
        }
        if (!atLeastCheck()) {
            return false;
        }
        //Vérif couloir
//        Position p1 = new Position(player.getRow(),player.getColumn());
//        Position p2 = new Position(8,3);
//        
//        if(!PathFinding.findPath(p1,p2,maze,false,true)){
//            System.out.println("ALAH OUAKBAR");
//            return false;
//        }
        System.out.println("oh yeah");

        //check Player chemin vers vault(vault considéré comme mur), drill, entrée    
        //check chemin entre clé et sortie secrète OPTIONELLE
        return true;
    }

    private boolean atLeastCheck() {
        //check au moins un joueur, une vault, entrée, drill
        boolean hasP = false;
        boolean hasD = false;
        boolean hasV = false;
        boolean hasI = false;
        int nbP = 0;
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (maze[i][j].hasPlayer()) {
                    nbP++;
                }
                if (!hasP) {
                    hasP = maze[i][j].hasPlayer();
                }
                if (!hasD) {
                    hasD = maze[i][j].hasDrill();
                }
                if (!hasV) {
                    hasV = maze[i][j].hasVault();
                }
                if (!hasI) {
                    hasI = maze[i][j].hasEntry();
                }
            }
        }
        if (!hasP || !hasD || !hasI || !hasV || nbP > 1) {
            return false;
        }
        return true;
    }

    private boolean checkEdge() {

        for (int i = 0; i < maze[0].length; i++) {//parcours HORI
            if ((!maze[0][i].getType().equals("wall")
                    && !maze[0][i].getType().equals("entry")
                    && !maze[0][i].getType().equals("exit"))) {
                return false;
            }
            if (!maze[maze.length - 1][i].getType().equals("wall")
                    && !maze[maze.length - 1][i].getType().equals("entry")
                    && !maze[maze.length - 1][i].getType().equals("exit")) {
                return false;
            }
        }
        for (int i = 1; i < maze.length - 1; i++) {//parcours VERTI
            if ((!maze[i][0].getType().equals("wall")
                    && !maze[i][0].getType().equals("entry")
                    && !maze[i][0].getType().equals("exit"))) {
                return false;
            }
            if (!maze[i][maze[0].length - 1].getType().equals("wall")
                    && !maze[i][maze[0].length - 1].getType().equals("entry")
                    && !maze[i][maze[0].length - 1].getType().equals("exit")) {
                return false;
            }
        }

        return true; //todo
    }

    private Direction movementAnalysis(Enemy e) throws Exception {
        //evalue si mur devant la direction suivie .V.
        e.resetPossibleDirection();
        for (Direction dir : Direction.values()) {
            if (!maze[e.getRow() + dir.getDecalX()][e.getColumn() + dir.getDecalY()].getType().equals("entry")
                    && maze[e.getRow() + dir.getDecalX()][e.getColumn() + dir.getDecalY()].isReachable(false, false)) {
                e.addPossibleDirection(dir);
            }
        }
        //si + de 2 direction possible random
        if (e.getNumberDirections() > 2) {
            return e.randDir();
        } else { //soit un mur soit couloir
            return movementDecision(e, e.getDirection(), e.getDirection().getDecalX(), e.getDirection().getDecalY());
        }

    }

    private Direction movementDecision(Enemy e, Direction dir, int decalRow, int decalCol) { //couloir ou coin
        if (maze[e.getRow() + decalRow][e.getColumn() + decalCol].getType().equals("entry")
                || !maze[e.getRow() + decalRow][e.getColumn() + decalCol].isReachable(false, false)) { // mur devant lui
            //choisir une direction possible au hasard
            return e.randDir();

        } else { //continuer dans meme direction   libre devant lui      
            return dir;

        }
    }

    private void readLevel(String nameLv) throws IOException, Exception {
        int row = 0, col = 0;
        int r;
        StringBuffer buf1 = new StringBuffer();
        StringBuffer buf2 = new StringBuffer();
        StringBuffer buf3 = new StringBuffer();
        FileReader in = new FileReader(new File(getClass().getResource("../levels/" + nameLv).getPath()));

        BufferedReader br = new BufferedReader(in);
        while ((r = br.read()) != '/') {
            buf1.append((char) r);
        }
        while ((r = br.read()) != '/') {
            buf2.append((char) r);
        }
        while ((r = br.read()) != '\n') {
            buf3.append((char) r);
        }

        this.enemyList = new ArrayList<>();
        maze = new Square[Integer.parseInt(buf1.toString())][Integer.parseInt(buf2.toString())];
        this.nextLevelName = buf3.toString();
        for (int i = 0; i < Integer.parseInt(buf1.toString()); i++) {
            for (int j = 0; j < Integer.parseInt(buf2.toString()); j++) {
                Square s = new Square();
                maze[i][j] = s;
            }
        }
        //r = br.read();
        while ((r = br.read()) != -1) {
            char ch = (char) r;

            switch (ch) {
                case ' ':
                    break;
                case 'P':
                    // new Player(new Position(row, col))
                    addPlayer(row, col);
                    break;
                case 'W':
                    addWall(row, col);
                    break;
                case 'I':
                    addEntry(row, col);
                    break;
                case 'E':
                    addEnemy(Direction.UP, row, col);
                    break;
                case 'V':
                    addVault(row, col);
                    break;
                case 'D':
                    putDrill(row, col);
                    break;
                case 'K':
                    putKey(row, col);
                    break;
                case 'S':
                    addExit(row, col);
                    break;
                case '\n':
                    row++;
                    col = -1;
                    break;
                case '\r':
                    break;
                default:
                    throw new IllegalArgumentException("invalid character");
            }

            ++col;
        }
        if (!isValid()) {
            throw new IllegalArgumentException("Unvalid maze architecture");
        }
    }

   

}
