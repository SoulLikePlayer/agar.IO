package info.prog.agario.model.entity;

import info.prog.agario.model.entity.player.Cell;
import info.prog.agario.view.GameView;
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
        cell.shape.setStroke(Color.TRANSPARENT);
        cell.getPseudo().setFill(Color.TRANSPARENT);
        cell.getPseudo().setStroke(Color.TRANSPARENT);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(5), e -> {
                    cell.shape.setFill(cell.getColor());
                    cell.shape.setStroke(cell.getColor().darker());
                    cell.getPseudo().setFill(Color.WHITE);
                    cell.getPseudo().setStroke(Color.BLACK);
                })
        );
        timeline.play();
    }



    @Override
    public void ExplosionEffect(Cell cell, Pane root) {

    }
}
