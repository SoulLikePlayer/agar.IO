package info.prog.agario.model.entity.ai;

import info.prog.agario.model.entity.GameEntity;
import info.prog.agario.model.entity.player.Cell;
import info.prog.agario.model.entity.player.PlayerGroup;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.util.Random;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class RandomMovement implements Strategy{
    private PlayerGroup entity;
    private Random r = new Random();

    private int[] lastRandom = new int[2];
    public RandomMovement(PlayerGroup entity){
        this.entity = entity;
        this.lastRandom[0] = 0;
        this.lastRandom[1] = 0;
    }
    @Override
    public void movement() {
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(33), event -> {
            int deltaX = r.nextInt(-1, 2);
            int deltaY = r.nextInt(-1, 2);

            lastRandom[0] = Math.max(-10, Math.min(10, lastRandom[0] + deltaX));
            lastRandom[1] = Math.max(-10, Math.min(10, lastRandom[1] + deltaY));

            double speed = 2.0;
            double distance = Math.sqrt(lastRandom[0] * lastRandom[0] + lastRandom[1] * lastRandom[1]);
            double factor = distance > 0 ? speed / distance : 1;

            entity.move(lastRandom[0] * factor, lastRandom[1] * factor);
            //double newX = entity.getShape().getCenterX();
            //double newY = entity.getShape().getCenterY();
            //entity.setPosition(newX, newY);

            //System.out.println("Co X : " + newX + " Co Y : " + newY);
            //System.out.println("Co X : " + entity.getX() + " Co Y : " + entity.getY());
            //System.out.println("co du playergroup : " + entity.getCells().get(0).getX() + " " + entity.getCells().get(0).getY());
        }));

        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();
    }

}
