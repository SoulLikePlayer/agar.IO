package info.prog.agario.model.world;

import info.prog.agario.model.entity.EntityFactory;
import info.prog.agario.model.entity.GameEntity;
import info.prog.agario.model.entity.player.Player;

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

    private void generatePellets(int count) {
        for (int i = 0; i < count; i++) {
            entities.add(EntityFactory.createEntity("pellet", Math.random() * 2000, Math.random() * 2000, 0));
        }
    }

    public List<GameEntity> getEntities() {
        return entities;
    }

    public Player getPlayer() {
        return player;
    }
}
