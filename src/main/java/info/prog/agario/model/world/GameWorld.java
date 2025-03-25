package info.prog.agario.model.world;

import info.prog.agario.model.entity.EntityFactory;
import info.prog.agario.model.entity.GameEntity;
import info.prog.agario.model.entity.ai.Enemy;
import info.prog.agario.model.entity.player.Cell;
import info.prog.agario.model.entity.player.Player;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.ArrayList;

public class GameWorld {
    private List<GameEntity> entities;
    private Player player;
    private ArrayList<Enemy> enemies = new ArrayList<>();

    public GameWorld(String pseudo) {
        entities = new ArrayList<>();
        player = new Player(300, 400, 10, pseudo);
        System.out.println("Joueur créé avec " + player.getPlayerGroup().getCells().size() + " cellule(s)");
        Enemy enemy = new Enemy(500, 600, 10);
        enemies.add(enemy);
        entities.add(enemy);
        //entities.add(EntityFactory.createEntity("cell",100,100,10));
        //Cell enemy = new Cell(100, 100, 10, Color.BLACK);
        System.out.println("Nombre d'ennemies crées : " + this.getEnemies().size());
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

    public ArrayList<Enemy> getEnemies(){ return enemies; }
}
