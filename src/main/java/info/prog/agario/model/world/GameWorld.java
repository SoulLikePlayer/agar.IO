package info.prog.agario.model.world;

import info.prog.agario.model.entity.EntityFactory;
import info.prog.agario.model.entity.GameEntity;
import info.prog.agario.model.entity.player.Player;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

import java.util.List;
import java.util.ArrayList;

public class GameWorld {
    private List<GameEntity> entities;

    private Player player;

    public GameWorld(String pseudo) {
        entities = new ArrayList<>();
        player = new Player(300, 400, 10, pseudo);
        System.out.println("Joueur créé avec " + player.getPlayerGroup().getCells().size() + " cellule(s)");
        generatePellets(200);

    }

    private final Random random = new Random();
    private final NavigableMap<Integer, String> pelletProbabilities = new TreeMap<>();

    private void initializePelletProbabilities() {
        pelletProbabilities.put(0, "InvisiblePellet");
        pelletProbabilities.put(0, "DoubleSpeedPellet");
        pelletProbabilities.put(0, "HalfSpeedPellet");
        pelletProbabilities.put(0, "DoubleMassPellet");
        pelletProbabilities.put(0, "HalfMassPellet");
        pelletProbabilities.put(0, "DoubleGainPellet");
        pelletProbabilities.put(0, "HalfGainPellet");
        pelletProbabilities.put(0, "ExplosionPellet");
        pelletProbabilities.put(100, "Pellet"); // Le reste correspond à "normal"
    }

    private void generatePellets(int count) {
        if (pelletProbabilities.isEmpty()) {
            initializePelletProbabilities();
        }

        for (int i = 0; i < count; i++) {
            int rank = random.nextInt(100);
            String type = pelletProbabilities.ceilingEntry(rank).getValue();

            entities.add(EntityFactory.createEntity(type, random.nextDouble() * 2000, random.nextDouble() * 2000, 0));
        }
    }

    public List<GameEntity> getEntities() {
        return entities;
    }

    public Player getPlayer() {
        return player;
    }
}
