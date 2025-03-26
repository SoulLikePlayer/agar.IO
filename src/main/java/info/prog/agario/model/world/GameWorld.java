package info.prog.agario.model.world;

import info.prog.agario.model.entity.EntityFactory;
import info.prog.agario.model.entity.GameEntity;
import info.prog.agario.model.entity.ai.Enemy;
import info.prog.agario.model.entity.player.Cell;
import info.prog.agario.model.entity.player.Player;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class GameWorld {
    private QuadTree quadTree;
    private Player player;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private static final int NB_ENEMIES = 5;

    private static final int SIZE = 200_000;

    private static final int NB_PELLETS = 2_000_000;

    public GameWorld(String pseudo) {
        quadTree = new QuadTree(new Boundary(0, 0, SIZE, SIZE));
        player = new Player(300, 400, 10, pseudo);
        System.out.println("Joueur créé avec " + player.getPlayerGroup().getCells().size() + " cellule(s)");
        generatePellets(NB_PELLETS);
        Random r = new Random();
        for(int i = 0; i < NB_ENEMIES; i++){
            Enemy enemy = new Enemy(r.nextInt(0,2001), r.nextInt(0,2001), 10);
            enemies.add(enemy);
        }
        //entities.add(EntityFactory.createEntity("cell",100,100,10));
        //Cell enemy = new Cell(100, 100, 10, Color.BLACK);
        System.out.println("Nombre d'ennemies crées : " + this.getEnemies().size());
        generatePellets(200);
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


    public ArrayList<Enemy> getEnemies(){ return enemies; }

    public QuadTree getQuadTree() {
        return quadTree;
    }
}
