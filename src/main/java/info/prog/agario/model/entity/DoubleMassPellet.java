package info.prog.agario.model.entity;

import info.prog.agario.model.entity.player.Cell;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class DoubleMassPellet extends SpecialPellet{
    public DoubleMassPellet(double x, double y) {
        super(x, y);
    }

    @Override
    public void PlayEffect(Cell cell) {
            cell.setMass(cell.getMass()*2);


        }

    @Override
    public void ExplosionEffect(Cell cell, Pane root) {

    }

}
