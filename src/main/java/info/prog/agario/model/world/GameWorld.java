package info.prog.agario.model.world;

import info.prog.agario.model.entity.GameEntity;
import info.prog.agario.model.entity.Player;
import info.prog.agario.model.entity.Pellet;
import java.util.List;
import java.util.ArrayList;

public class GameWorld {
    private List<GameEntity> entities;
    private Player player;

    public GameWorld() {
        entities = new ArrayList<>();
        player = new Player(500, 500, 100);
        generatePellets(50);
    }

    private void generatePellets(int count) {
        for (int i = 0; i < count; i++) {
            entities.add(new Pellet(Math.random() * 1000, Math.random() * 1000));
        }
    }

    public List<GameEntity> getEntities() {
        return entities;
    }

    public Player getPlayer() {
        return player;
    }
}
