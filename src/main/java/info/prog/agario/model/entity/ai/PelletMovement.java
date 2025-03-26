package info.prog.agario.model.entity.ai;

import info.prog.agario.model.entity.GameEntity;
import info.prog.agario.model.entity.player.PlayerGroup;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class PelletMovement implements Strategy{
    private PlayerGroup entity;
    public PelletMovement(PlayerGroup entity){
        this.entity = entity;
    }

    @Override
    public void movement() {
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(33), event -> {

        }));

        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();
    }
}
