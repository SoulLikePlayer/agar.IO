package info.prog.agario.model.entity.ai;

import info.prog.agario.model.entity.GameEntity;
import info.prog.agario.model.entity.Pellet;
import info.prog.agario.model.entity.player.Cell;
import info.prog.agario.model.entity.player.PlayerGroup;
import info.prog.agario.model.world.QuadTree;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.List;

public class PelletMovement implements Strategy{
    private PlayerGroup group;

    private QuadTree quadTree;
    public PelletMovement(PlayerGroup group, QuadTree quadTree){
        this.group = group;
        this.quadTree = quadTree;
    }

    @Override
    public void movement() {
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(33), event -> {
            for (Cell cell : group.getCells()) {
                GameEntity nearestPellet = findNearestPellet(cell);

                if (nearestPellet != null) {
                    double dx = nearestPellet.getX() - cell.getX();
                    double dy = nearestPellet.getY() - cell.getY();
                    double distance = Math.sqrt(dx * dx + dy * dy);

                    if (distance > 1) {
                        double speed = Math.max(0.5, 5.0 / Math.sqrt(cell.getMass()));
                        cell.setVelocity(dx / distance * speed, dy / distance * speed);
                        cell.move(dx / distance * speed, dy / distance * speed);
                    }
                }
            }
        }));

        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();
    }

    private GameEntity findNearestPellet(Cell cell) {
        List<GameEntity> nearbyEntities = quadTree.retrieve(cell, cell.getRadius() * 5);
        GameEntity nearestPellet = null;
        double minDistance = Double.MAX_VALUE;

        for (GameEntity entity : nearbyEntities) {
            if (entity instanceof Pellet) {
                double distance = Math.sqrt(Math.pow(entity.getX() - cell.getX(), 2) + Math.pow(entity.getY() - cell.getY(), 2));
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestPellet = entity;
                }
            }
        }

        return nearestPellet;
    }

}
