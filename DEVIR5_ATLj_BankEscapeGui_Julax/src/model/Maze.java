package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Classe représentant un labyrinth
 *
 * @author pikirby45
 */
public class Maze extends Observable {

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

    public Maze(String nameLevel) throws BankEscapeException {
        try {
            readLevel(nameLevel);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public Direction getEnemyDir(int i, int j) throws BankEscapeException {
        for (Enemy e : enemyList) {
            if (e.getRow() == i && e.getColumn() == j) {
                return e.getDirection();
            }
        }
        throw new BankEscapeException("no enemy here");
    }

    public Direction getPlayerDir(int i, int j) throws BankEscapeException {
        if (player.getRow() == i && player.getColumn() == j) {
            return player.getDirection();
        }
        throw new BankEscapeException("no player here");
    }

    public int getWidth() {
        return maze.length;
    }

    public int getHeight() {
        return maze[0].length;
    }

    public Maze(Maze maze, int modifWidth, int modifHeight) throws BankEscapeException {
        //todo constructeur copie profonde avec modification de dimension
        this.enemyList = new ArrayList<>();
        this.maze = new Square[maze.getHeight() + modifHeight][maze.getWidth() + modifWidth];
        for (int i = 0; i < maze.getWidth() + modifWidth; i++) {
            if (maze.getSquares()[0].length < i) {
                for (int j = 0; j < maze.getHeight() + modifHeight; j++) {
                    Square s = new Square();
                    // Maze otherMaze = new Maze(maze.getHeight()+modifHeight, maze.getWidth()+modifWidth);

                    this.maze[i][j] = s;
                    if (maze.getSquares().length < j) {
                        switch (maze.getSquares()[i][j].getType()) {
                            case "wall":
                                this.maze[i][j].setWall();
                                break;
                            case "entry":
                                this.maze[i][j].setEntry();
                                break;
                            case "exit":
                                this.maze[i][j].setExit();
                                break;
                            case "vault":
                                this.maze[i][j].setVault();
                                break;
                            case "floor":
                                if (this.maze[i][j].hasDrill()) {
                                    this.maze[i][j].setHasDrill();
                                } else if (this.maze[i][j].hasEnemy()) {
                                    addEnemy(Direction.UP, i, j);
                                } else if (this.maze[i][j].hasKey()) {
                                    this.maze[i][j].setHasKey();
                                } else if (this.maze[i][j].hasPlayer()) {
                                    addPlayer(i, j);
                                }
                                break;
                            default:
                                throw new BankEscapeException("Type lu incorrect");
                        }
                    }

                }
            }

        }
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
            setChanged();
            notifyObservers();
            notifyAll();
        }
    }

    public Square[][] getSquares() {
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
                setChanged();
                notifyObservers();
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
            setChanged();
            notifyObservers();
        }
        if (maze[player.getRow()][player.getColumn()].hasDrill()) {
            player.setHasDrill(true);
            maze[player.getRow()][player.getColumn()].removeDrill();
            setChanged();
            notifyObservers();
        }
        if (this.maze[this.player.getRow()][this.player.getColumn()].getType().equals("vault")) {
            player.setHasMoney(true);
            setChanged();
            notifyObservers();
        }
    }

    private void watch(Enemy e) {  //champ de vision 3
        if (lookingForPlayer(e)) {
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
        player.setDirection(dir);
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

    public void addWall(int row, int column) {
        maze[row][column].setWall();
        setChanged();
        notifyObservers();
    }

    public void addFloor(int row, int col) {
        maze[row][col].setFloor();
        setChanged();
        notifyObservers();
    }

    public void addVault(int row, int column) {
        maze[row][column].setVault();
        setChanged();
        notifyObservers();
    }

    public void addEntry(int row, int column) {
        maze[row][column].setEntry();
        setChanged();
        notifyObservers();
    }

    public void addExit(int row, int column) {
        maze[row][column].setExit();
        setChanged();
        notifyObservers();
    }

    public void addPlayer(int row, int column) {   //1 seule fois a l'initialisation du jeu
        Player play = new Player(new Position(row, column), Direction.UP);
        maze[row][column].setHasPlayer();
        player = play;
    }

    public void putPlayer(int row, int column) {  //a chaque fois qu on bouge
        maze[row][column].setHasPlayer();
        setChanged();
        notifyObservers();
    }

    public void removePlayer(int row, int column) {
        maze[row][column].removePlayer();
    }

    public void addEnemy(Direction dir, int row, int column) {   //x fois a l'initialisation du jeu
        maze[row][column].setHasEnemy();
        enemyList.add(new Enemy(dir, new Position(row, column)));
    }

    public void putEnemy(int row, int column) {
        maze[row][column].setHasEnemy();
        setChanged();
        notifyObservers();
    }

    public void putDrill(int row, int column) {
        maze[row][column].setHasDrill();
        setChanged();
        notifyObservers();
    }

    public void putKey(int row, int column) {
        maze[row][column].setHasKey();
        setChanged();
        notifyObservers();
    }

    private void removeEnemy(int row, int column) {
        maze[row][column].removeEnemy();
    }

    public boolean isValid() throws BankEscapeException {
        //check mur ou entrée ou sortie autour.
        if (!checkEdge()) {  //si contour invalide ...
            return false;
        }
        if (!atLeastCheck()) {
            return false;
        }
        //Vérif couloir
        Position playerPos = new Position(player.getRow(), player.getColumn());
        if (!PathFinding.findPath(playerPos, whereIsDrill(), maze, false, false)) {
            return false;
        }
        if (!PathFinding.findPath(playerPos, whereIsVault(), maze, true, false)) {
            return false;
        }
        if (!PathFinding.findPath(playerPos, whereIsEntry(), maze, true, true)) {
            return false;
        }

        System.out.println("oh yeah");

        //check Player chemin vers vault(vault considéré comme mur), drill, entrée    
        //check chemin entre clé et sortie secrète OPTIONELLE
        return true;
    }

    private Position whereIsEntry() throws BankEscapeException {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (maze[i][j].getType().equals("entry")) {
                    return new Position(i, j);
                }
            }
        }
        throw new BankEscapeException("L'entréé n'a pas été trouvée.");
    }

    private Position whereIsVault() throws BankEscapeException {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (maze[i][j].getType().equals("vault")) {
                    return new Position(i, j);
                }
            }
        }
        throw new BankEscapeException("Le coffre-fort n'a pas été trouvée.");
    }

    private Position whereIsDrill() throws BankEscapeException {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (maze[i][j].hasDrill()) {
                    return new Position(i, j);
                }
            }
        }
        throw new BankEscapeException("La perçeuse n'a pas été trouvée.");
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
            if (!maze[0][i].getType().equals("wall")) {
                return false;
            }
            if (!maze[maze.length - 1][i].getType().equals("wall")) {
                return false;
            }
        }
        for (int i = 1; i < maze.length - 1; i++) {//parcours VERTI
            if (!maze[i][0].getType().equals("wall")) {
                return false;
            }
            if (!maze[i][maze[0].length - 1].getType().equals("wall")) {
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

    private void readLevel(String nameLv) throws IOException, BankEscapeException {
        int row = 0, col = 0;
        int r;
        StringBuffer buf1 = new StringBuffer();
        StringBuffer buf2 = new StringBuffer();
        StringBuffer buf3 = new StringBuffer();

        BufferedReader br = new BufferedReader(new FileReader("levels/" + nameLv));
        while ((r = br.read()) != '/') {
            buf1.append((char) r);
        }
        while ((r = br.read()) != '/') {
            buf2.append((char) r);
        }
        while ((r = br.read()) != '\r') {
            buf3.append((char) r);
        }
        r = br.read();
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
                    throw new BankEscapeException("invalid character");
            }

            ++col;
        }
        if (!isValid()) {
            throw new BankEscapeException("Unvalid maze architecture");
        }
    }

}
