package info.prog.agario.model.world;

import info.prog.agario.model.entity.EntityFactory;
import info.prog.agario.model.entity.GameEntity;
import info.prog.agario.model.entity.player.Player;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

import info.prog.agario.model.world.Boundary;
import java.util.List;
import java.util.ArrayList;

public class GameWorld {

    private QuadTree quadTree;
    private List<GameEntity> entities;
    private Player player;

    public GameWorld(String pseudo) {
        entities = new ArrayList<>();
        quadTree = new QuadTree(new Boundary(0, 0, 2000, 2000));
        player = new Player(300, 400, 10, pseudo);
        System.out.println("Joueur créé avec " + player.getPlayerGroup().getCells().size() + " cellule(s)");
        generatePellets(200);
    }


    private final NavigableMap<Integer, String> pelletProbabilities = new TreeMap<>();

    private void initializePelletProbabilities() {
        pelletProbabilities.put(5, "InvisiblePellet");
        pelletProbabilities.put(10, "DoubleSpeedPellet");
        pelletProbabilities.put(12, "HalfSpeedPellet");
        pelletProbabilities.put(13, "DoubleMassPellet");
        pelletProbabilities.put(14, "HalfMassPellet");
        pelletProbabilities.put(17, "DoubleGainPellet");
        pelletProbabilities.put(19, "HalfGainPellet");
        pelletProbabilities.put(25, "ExplosionPellet");
        pelletProbabilities.put(100, "Pellet");
    }

    private void generatePellets(int count) {
        Random random = new Random();
        if (pelletProbabilities.isEmpty()) {
            initializePelletProbabilities();
        }

        for (int i = 0; i < count; i++) {
            int rank = random.nextInt(100);
            String type = pelletProbabilities.ceilingEntry(rank).getValue();
            GameEntity pellet = EntityFactory.createEntity(type, random.nextDouble() * 2000, random.nextDouble() * 2000, 0);
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

    public QuadTree getQuadTree() {
        return quadTree;
    }
}
