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
    private static final int NB_ENEMIES = 5;
    private static final int SIZE = 2000;
    private static final int NB_PELLETS = 200;
    private int nbEntities = NB_PELLETS;

    private static final int MAP_SIZE = 8000;

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


    private final NavigableMap<Double, String> pelletProbabilities = new TreeMap<>();

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

    public Player getPlayer() {
        return player;
    }
    public ArrayList<Enemy> getEnemies(){
        return enemies;
    }
    public QuadTree getQuadTree() {
        return quadTree;
    }
    public int getNbEnemies(){
        return NB_ENEMIES;
    }
    public int getNbPellets(){
        return NB_PELLETS;
    }
    public int getSize() {
        return SIZE;
    }
    public int getNbEntities() {
        return nbEntities;
    }
    public void setNbEntities(int nbEntities) {
        this.nbEntities = nbEntities;
    }
}
