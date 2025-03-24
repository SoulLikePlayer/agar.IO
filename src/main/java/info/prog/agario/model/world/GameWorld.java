package info.prog.agario.model.world;

import info.prog.agario.model.entity.GameEntity;
import info.prog.agario.model.entity.Player;
import info.prog.agario.model.entity.Pellet;
import java.util.List;
import java.util.ArrayList;

public class GameWorld {
    private List<GameEntity> entities;
    private Player player;

    public GameWorld(String pseudo) {
        entities = new ArrayList<>();
        player = new Player(1000, 1000, 100, pseudo);
        generatePellets(200);
    }

    private void generatePellets(int count) {
        for (int i = 0; i < count; i++) {
            entities.add(new Pellet(Math.random() * 2000, Math.random() * 2000));
        }
    }

    public List<GameEntity> getEntities() {
        return entities;
    }

    public Player getPlayer() {
        return player;
    }
}
