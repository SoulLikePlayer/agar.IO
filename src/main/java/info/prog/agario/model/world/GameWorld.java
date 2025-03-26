package info.prog.agario.model.world;

import info.prog.agario.model.entity.EntityFactory;
import info.prog.agario.model.entity.GameEntity;
import info.prog.agario.model.entity.player.Player;
import java.util.List;
import java.util.ArrayList;

public class GameWorld {
    private QuadTree quadTree;
    private List<GameEntity> entities;
    private Player player;
    private boolean isOnline;

    public GameWorld(String pseudo) {
        this(pseudo, false);
    }

    public GameWorld(String pseudo, boolean isOnline) {
        this.isOnline = isOnline;
        entities = new ArrayList<>();
        quadTree = new QuadTree(new Boundary(0, 0, 2000, 2000));
        player = new Player(300, 400, 10, pseudo);

        if (!isOnline) {
            System.out.println("Joueur créé avec " + player.getPlayerGroup().getCells().size() + " cellule(s)");
            generatePellets(200);
        }
    }

    private void generatePellets(int count) {
        for (int i = 0; i < count; i++) {
            GameEntity pellet = EntityFactory.createEntity("pellet", Math.random() * 2000, Math.random() * 2000, 0);
            entities.add(pellet);
            quadTree.insert(pellet);
        }
    }

    public List<GameEntity> getEntities() {
        return entities;
    }

    public void clearEntities() {
        entities.clear();
    }

    public void addEntity(GameEntity entity) {
        entities.add(entity);
        quadTree.insert(entity);
    }

    public Player getPlayer() {
        return player;
    }

    public QuadTree getQuadTree() {
        return quadTree;
    }
}

