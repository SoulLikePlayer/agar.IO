package info.prog.agario.model.entity;

import javafx.scene.paint.Color;

public class Pellet extends GameEntity {

    public Pellet(double x, double y, double radius) {
        super(x, y, radius);
        this.shape.setFill(Color.color(Math.random(), Math.random(), Math.random()));
        this.setMass(1);
    }
}
