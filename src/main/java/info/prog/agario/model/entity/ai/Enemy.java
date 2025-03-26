package info.prog.agario.model.entity.ai;

import info.prog.agario.model.entity.GameEntity;
import info.prog.agario.model.entity.player.Cell;
import info.prog.agario.model.entity.player.PlayerGroup;
import info.prog.agario.utils.AnimationUtils;
import javafx.scene.paint.Color;

public class Enemy extends GameEntity {
    private final Color color;
    private Strategy strat;
    private PlayerGroup enemyGroup;

    public Enemy(double x, double y, double mass) {
        super(x, y, 10 * Math.sqrt(mass));
        this.color = Color.hsb(Math.random() * 360, 0.8, 0.9);
        this.enemyGroup = new PlayerGroup();
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
        strat = new RandomMovement(this.enemyGroup);
        strat.movement();
    }

    public PlayerGroup getEnemyGroup() {
        return enemyGroup;
    }
}
