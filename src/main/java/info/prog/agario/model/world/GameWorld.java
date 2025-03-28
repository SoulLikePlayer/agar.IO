package info.prog.agario.model.world;

import info.prog.agario.model.entity.EntityFactory;
import info.prog.agario.model.entity.GameEntity;
import info.prog.agario.model.entity.ai.Enemy;
import info.prog.agario.model.entity.player.Cell;
import info.prog.agario.model.entity.player.Player;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;
import javafx.scene.paint.Color;

import info.prog.agario.model.world.Boundary;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class GameWorld {
    private QuadTree quadTree;
    private Player player;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private final NavigableMap<Double, String> pelletProbabilities = new TreeMap<>();
    private static final int NB_ENEMIES = 5;
    private static final int SIZE = 4440;
    private static final int NB_PELLETS = 200;
    private int nbEntities = NB_PELLETS;
    private static final int MAP_SIZE = 8000;

    /**
     * Constructor of the GameWorld class
     * @param pseudo the pseudo of the player
     */
    public GameWorld(String pseudo) {
        quadTree = new QuadTree(new Boundary(0, 0, SIZE, SIZE));
        player = new Player(300, 400, 10, pseudo);
        System.out.println("Joueur créé avec " + player.getPlayerGroup().getCells().size() + " cellule(s)");
        Random r = new Random();
        for(int i = 0; i < NB_ENEMIES; i++){
            Enemy enemy = new Enemy(r.nextInt(SIZE), r.nextInt(SIZE), 10, this);
            enemies.add(enemy);
        }

        System.out.println("Nombre d'ennemies crées : " + this.getEnemies().size());
        generatePellets((int)(MAP_SIZE*0.8));
    }

    /**
     * Initialize the probabilities of the pellets
     */
    private void initializePelletProbabilities() {
        pelletProbabilities.put(0.5, "InvisiblePellet");
        pelletProbabilities.put(1.0, "DoubleSpeedPellet");
        pelletProbabilities.put(1.5, "HalfSpeedPellet");
        pelletProbabilities.put(2.0, "DoubleMassPellet");
        pelletProbabilities.put(2.25, "HalfMassPellet");
        pelletProbabilities.put(2.5, "DoubleGainPellet");
        pelletProbabilities.put(3.0, "HalfGainPellet");
        pelletProbabilities.put(4.5, "ExplosionPellet");
        pelletProbabilities.put(100., "Pellet");
    }

    /**
     * Generate the pellets
     * @param count the number of pellets to generate
     */
    private void generatePellets(int count) {
        Random random = new Random();
        if (pelletProbabilities.isEmpty()) {
            initializePelletProbabilities();
        }

        for (int i = 0; i < count; i++) {
            double rank = random.nextDouble(100);
            String type = pelletProbabilities.ceilingEntry(rank).getValue();
            GameEntity pellet = EntityFactory.createEntity(type, random.nextDouble() * MAP_SIZE, random.nextDouble() * MAP_SIZE, 0);
            quadTree.insert(pellet);
        }
    }

    /**
     * Get the player
     * @return Player the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the enemies
     * @return ArrayList<Enemy> the enemies
     */
    public ArrayList<Enemy> getEnemies(){
        return enemies;
    }

    /**
     * Get the quadtree
     * @return QuadTree the quadtree of the world
     */
    public QuadTree getQuadTree() {
        return quadTree;
    }

    /**
     * Get the number of enemies
     * @return int the number of enemies at the beginning
     */
    public int getNbEnemies(){
        return NB_ENEMIES;
    }

    /**
     * Get the number of pellets
     * @return int the number of pellets at the beginning
     */
    public int getNbPellets(){
        return NB_PELLETS;
    }

    /**
     * Get the size of the world
     * @return int the size of the world
     */
    public int getSize() {
        return SIZE;
    }

    /**
     * Get the number of entities
     * @return int the number of entities currently in the world
     */
    public int getNbEntities() {
        return nbEntities;
    }

    /**
     * Set the number of entities
     * @param nbEntities the number of entities to set
     */
    public void setNbEntities(int nbEntities) {
        this.nbEntities = nbEntities;
    }
}
