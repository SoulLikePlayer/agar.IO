package info.prog.agario.model.entity.ai;

import info.prog.agario.model.entity.GameEntity;
import info.prog.agario.model.entity.player.Cell;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.util.Random;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class RandomMovement implements Strategy{
    private GameEntity entity;
    private Random r = new Random();

    private int[] lastRandom = new int[2];
    public RandomMovement(GameEntity entity){
        this.entity = entity;
        this.lastRandom[0] = 0;
        this.lastRandom[1] = 0;
    }
    @Override
    public void movement() throws InterruptedException {
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(33), event -> {
            int moveX = r.nextInt(this.lastRandom[0] -1,this.lastRandom[0] +2);
            int moveY = r.nextInt(this.lastRandom[1] -1,this.lastRandom[1] +2);
            entity.move(Math.round(moveX*0.9),Math.round(moveY*0.9));
            this.lastRandom[0] = moveX;
            this.lastRandom[1] = moveY;
            System.out.println("Co X : " + entity.getShape().getCenterX() + " Co Y : " + entity.getShape().getCenterY());
        }));
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();
    }
}
