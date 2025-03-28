package info.prog.agario.model.entity.ai;

import info.prog.agario.model.entity.player.PlayerGroup;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.Random;

public class RandomMovement implements Strategy{
    private PlayerGroup group;
    private Random r = new Random();

    private int[] lastRandom = new int[2];

    /**
     * Constructor for RandomMovement
     * @param group the PlayerGroup to move
     */
    public RandomMovement(PlayerGroup group){
        this.group = group;
        this.lastRandom[0] = 0;
        this.lastRandom[1] = 0;
    }

    /**
     * Moves the PlayerGroup in a random direction
     */
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

            group.move(lastRandom[0] * factor, lastRandom[1] * factor);
        }));

        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();
    }

}
