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
    private List<GameEntity> entities;
    private Player player;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private int nbEnnemies = 1;

    public GameWorld(String pseudo) {
        entities = new ArrayList<>();
        quadTree = new QuadTree(new Boundary(0, 0, 2000, 2000));
        player = new Player(1000, 1000, 10, pseudo);
        System.out.println("Joueur créé avec " + player.getPlayerGroup().getCells().size() + " cellule(s)");
        Random r = new Random();
        for(int i = 0; i < nbEnnemies; i++){
            Enemy enemy = new Enemy(r.nextInt(0,2001), r.nextInt(0,2001), 50);
            enemies.add(enemy);
            entities.add(enemy);
        }
        System.out.println("Nombre d'ennemies crées : " + this.getEnemies().size());
        generatePellets(500);
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
            GameEntity pellet = EntityFactory.createEntity("HalfSpeedPellet", random.nextDouble() * 2000, random.nextDouble() * 2000, 0);
            entities.add(pellet);
            quadTree.insert(pellet);
        }
    }

    public List<GameEntity> getEntities() {
        return entities;
    }

    public Player getPlayer() {
        return player;
    }


    public ArrayList<Enemy> getEnemies(){ return enemies; }

    public QuadTree getQuadTree() {
        return quadTree;
    }
}
