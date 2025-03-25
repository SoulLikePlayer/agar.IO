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
    private List<GameEntity> entities;
    private Player player;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private int nbEnnemies = 5;

    public GameWorld(String pseudo) {
        entities = new ArrayList<>();
        quadTree = new QuadTree(new Boundary(0, 0, 2000, 2000));
        player = new Player(1000, 1000, 10, pseudo);
        System.out.println("Joueur créé avec " + player.getPlayerGroup().getCells().size() + " cellule(s)");
        Random r = new Random();
        for(int i = 0; i < nbEnnemies; i++){
            Enemy enemy = new Enemy(r.nextInt(0,2001), r.nextInt(0,2001), 10);
            enemies.add(enemy);
        }
        //entities.add(enemy);
        //entities.add(EntityFactory.createEntity("cell",100,100,10));
        //Cell enemy = new Cell(100, 100, 10, Color.BLACK);
        System.out.println("Nombre d'ennemies crées : " + this.getEnemies().size());
        generatePellets(200);
    }

    private void generatePellets(int count) {
        for (int i = 0; i < count; i++) {
            GameEntity pellet = EntityFactory.createEntity("pellet", Math.random() * 2000, Math.random() * 2000, 0);
            entities.add(pellet);
            quadTree.insert(pellet);
        }
        GameEntity pellet = EntityFactory.createEntity("pellet", 500, 600, 10);
        entities.add(pellet);
        quadTree.insert(pellet);
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
