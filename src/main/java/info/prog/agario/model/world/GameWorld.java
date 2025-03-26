package info.prog.agario.model.world;

import info.prog.agario.model.entity.EntityFactory;
import info.prog.agario.model.entity.GameEntity;
import info.prog.agario.model.entity.player.Player;

import java.util.List;
import java.util.ArrayList;

public class GameWorld {
    private QuadTree quadTree;
    private Player player;

    private static final int SIZE = 200_000;

    private static final int NB_PELLETS = 2_000_000;

    public GameWorld(String pseudo) {
        quadTree = new QuadTree(new Boundary(0, 0, SIZE, SIZE));
        player = new Player(300, 400, 10, pseudo);
        System.out.println("Joueur créé avec " + player.getPlayerGroup().getCells().size() + " cellule(s)");
        generatePellets(NB_PELLETS);
    }

    private void generatePellets(int count) {
        for (int i = 0; i < count; i++) {
            GameEntity pellet = EntityFactory.createEntity("pellet", Math.random() * SIZE, Math.random() * SIZE, 0);
            quadTree.insert(pellet);
        }
    }


    public Player getPlayer() {
        return player;
    }

    public QuadTree getQuadTree() {
        return quadTree;
    }
}
