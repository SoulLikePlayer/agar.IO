package info.prog.agario.model.entity;

import javafx.scene.paint.Color;

public class Pellet extends GameEntity {
    public Pellet(double x, double y) {
        super(x, y, 10);
        this.shape.setFill(Color.color(Math.random(), Math.random(), Math.random()));
    }
}
