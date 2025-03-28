package info.prog.agario.model.entity;

import javafx.scene.paint.Color;

import java.util.UUID;

public class Pellet extends GameEntity {

    public Pellet(double x, double y, UUID id) {
        super(x, y, 5, id);
        this.shape.setFill(Color.color(Math.random(), Math.random(), Math.random()));
        this.setMass(1);
    }
}
