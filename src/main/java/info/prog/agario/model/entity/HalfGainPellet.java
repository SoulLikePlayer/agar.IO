package info.prog.agario.model.entity;

import info.prog.agario.model.entity.player.Cell;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class HalfGainPellet extends SpecialPellet{
    public HalfGainPellet(double x, double y) {
        super(x, y);
    }

    @Override
    public void PlayEffect(Cell cell) {
        System.out.println(cell.getMultiplicatorGain());
        cell.setMultiplicatorGain(0.5);
        System.out.println(cell.getMultiplicatorGain());
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(20), e -> {
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
