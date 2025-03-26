package info.prog.agario.model.entity;

import info.prog.agario.model.entity.player.Cell;
import info.prog.agario.view.GameView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class DoubleGainPellet extends SpecialPellet{
    public DoubleGainPellet(double x, double y) {
        super(x, y);
    }

    @Override
    public void PlayEffect(Cell cell) {
        System.out.println(cell.getMultiplicatorGain());
        cell.setMultiplicatorGain(2);
        System.out.println(cell.getMultiplicatorGain());
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(5), e -> {
                    cell.setMultiplicatorGain(1);
                    System.out.println(cell.getMultiplicatorGain());
                })
        );
        timeline.play();
    }

    @Override
    public void ExplosionEffect(Cell cell, Pane root) {

    }

}
