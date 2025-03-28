package info.prog.agario.model.entity.ai;

import info.prog.agario.model.entity.GameEntity;
import info.prog.agario.model.entity.player.Cell;
import info.prog.agario.model.entity.player.PlayerGroup;
import info.prog.agario.model.world.QuadTree;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.List;

public class CellEatingMovement implements Strategy {

    private PlayerGroup enemyGroup;
    private PlayerGroup playerGroup;

    /**
     * Constructor of the CellEatingMovement class
     * @param enemyGroup The group of enemy cells
     * @param playerGroup The group of player cells
     */
    public CellEatingMovement(PlayerGroup enemyGroup, PlayerGroup playerGroup) {
        this.enemyGroup = enemyGroup;
        this.playerGroup = playerGroup;
    }

    /**
     * Method to move the enemy cells
     */
    @Override
    public void movement() {
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(33), event -> {
            for (Cell enemyCell : enemyGroup.getCells()) {
                Cell nearestPlayerCell = findNearestPlayerCell(enemyCell);

                if (nearestPlayerCell != null) {
                    double dx = nearestPlayerCell.getX() - enemyCell.getX();
                    double dy = nearestPlayerCell.getY() - enemyCell.getY();
                    double distance = Math.sqrt(dx * dx + dy * dy);

                    if (distance > 1) {
                        double speed = Math.max(0.5, 5.0 / Math.sqrt(enemyCell.getMass()));
                        enemyCell.setVelocity(dx / distance * speed, dy / distance * speed);
                        enemyCell.move(dx / distance * speed, dy / distance * speed);
                    }
                }
            }
        }));

        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();
    }

    /**
     * Method to find the nearest player cell
     * @param enemyCell The enemy cell
     * @return The nearest player cell
     */
    private Cell findNearestPlayerCell(Cell enemyCell) {
        Cell nearestCell = null;
        double minDistance = Double.MAX_VALUE;
        for (Cell playerCell : playerGroup.getCells()) {
            double distance = Math.sqrt(Math.pow(playerCell.getX() - enemyCell.getX(), 2) + Math.pow(playerCell.getY() - enemyCell.getY(), 2));
            if (distance < minDistance) {
                minDistance = distance;
                nearestCell = playerCell;
            }
        }
        return nearestCell;
    }
}
