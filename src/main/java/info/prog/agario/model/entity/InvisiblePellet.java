package info.prog.agario.model.entity;

import info.prog.agario.model.entity.player.Cell;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class InvisiblePellet extends SpecialPellet {


    public InvisiblePellet(double x, double y) {
        super(x, y);
    }

    @Override
    public void PlayEffect(Cell cell) {
        cell.shape.setFill(Color.TRANSPARENT);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(3), e -> {
                    cell.shape.setFill(cell.getColor());
                })
        );
        timeline.play();
    }

    @Override
    public void ExplosionEffect(Cell cell, Pane root) {

    }
}
