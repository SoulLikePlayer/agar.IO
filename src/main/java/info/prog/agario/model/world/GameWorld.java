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

    public GameWorld(String pseudo) {
        entities = new ArrayList<>();
        quadTree = new QuadTree(new Boundary(0, 0, 2000, 2000));
        player = new Player(1000, 1000, 10, pseudo);
        System.out.println("Joueur créé avec " + player.getPlayerGroup().getCells().size() + " cellule(s)");
        generatePellets(200);
        entities.add(player);
        quadTree.insert(player);
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

    public Player getPlayer() {
        return player;
    }

    public QuadTree getQuadTree() {
        return quadTree;
    }

    public synchronized void updatePlayerPosition(double x, double y) {
        player.setPosition(x, y);
        quadTree.update(player);
    }

    public synchronized String getGameState() {
        StringBuilder gameState = new StringBuilder();
        gameState.append("Player: ").append(player.getPseudoText()).append("\n");
        gameState.append("Entities: ").append(entities.size()).append("\n");

        for (GameEntity entity : entities) {
            gameState.append(entity.getPosition()).append("\n");
        }
        return gameState.toString();
    }
}
