package info.prog.agario.model.entity;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.Circle;

public abstract class GameEntity {
    protected DoubleProperty x, y, radius;
    protected Circle shape;

    public GameEntity(double x, double y, double radius) {
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
        this.radius = new SimpleDoubleProperty(radius);
        this.shape = new Circle(x, y, radius);
    }

    public Circle getShape() {
        return shape;
    }
}
