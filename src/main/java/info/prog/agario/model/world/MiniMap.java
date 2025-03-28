package info.prog.agario.model.world;

import info.prog.agario.model.entity.GameEntity;
import info.prog.agario.model.entity.player.Player;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class MiniMap extends Canvas {
    private static final int MAP_SIZE = 4440;
    private static final int MINI_MAP_SIZE = 200;

    private List<GameEntity> entities;
    private Player player;

    /**
     * Constructor of the MiniMap
     * @param entities the entities to display on the map
     * @param player the player to display on the map
     */
    public MiniMap(List<GameEntity> entities, Player player) {
        super(MINI_MAP_SIZE, MINI_MAP_SIZE);
        this.entities = entities;
        this.player = player;
    }

    /**
     * Draw the entities and the player on the map
     */
    public void draw() {
        GraphicsContext gc = getGraphicsContext2D();

        gc.setFill(Color.GRAY);
        gc.fillRect(0, 0, getWidth(), getHeight());

        double scale = (double) MINI_MAP_SIZE / MAP_SIZE;

        gc.setFill(Color.YELLOW);
        for (GameEntity entity : entities) {
            int x = (int) (entity.getX() * scale);
            int y = (int) (entity.getY() * scale);
            gc.fillOval(x, y, 1  , 1);
        }

        gc.setFill(Color.BLUE);
        int playerX = (int) (player.getX() * scale);
        int playerY = (int) (player.getY() * scale);
        gc.fillOval(playerX, playerY, 5, 5);
    }

    /**
     * Update the entities and the player to display on the map
     * @param entities the entities to display on the map
     * @param player the player to display on the map
     */
    public void updateEntities(List<GameEntity> entities, Player player) {
        this.entities = entities;
        this.player = player;
        draw();
    }
}
