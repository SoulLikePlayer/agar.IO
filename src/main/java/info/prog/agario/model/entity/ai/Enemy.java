package info.prog.agario.model.entity.ai;

import info.prog.agario.model.entity.GameEntity;
import info.prog.agario.model.entity.player.Cell;
import info.prog.agario.model.entity.player.PlayerGroup;
import info.prog.agario.model.world.GameWorld;
import info.prog.agario.utils.AnimationUtils;
import javafx.scene.paint.Color;

public class Enemy extends GameEntity {
    private final Color color;
    private Strategy strat;
    private PlayerGroup enemyGroup;

    private GameWorld world;

    public Enemy(double x, double y, double mass, GameWorld world) {
        super(x, y, 10 * Math.sqrt(mass));
        this.color = Color.hsb(Math.random() * 360, 0.8, 0.9);
        this.enemyGroup = new PlayerGroup();
        this.world = world;
        Cell firstCell = new Cell(x, y, mass, this.color);
        firstCell.setParentGroup(this.enemyGroup);
        enemyGroup.addComponent(firstCell);
        try {
            this.move();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void move() throws InterruptedException {
        strat = new PelletMovement(this.enemyGroup, world.getQuadTree());
        strat.movement();
    }

    public PlayerGroup getEnemyGroup() {
        return enemyGroup;
    }
}
