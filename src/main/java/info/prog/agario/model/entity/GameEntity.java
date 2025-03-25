package info.prog.agario.model.entity;

import info.prog.agario.model.world.Boundary;
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

    public double getX() {
        return x.get();
    }

    public double getY() {
        return y.get();
    }

    public double getRadius() {
        return radius.get();
    }

    public void setPosition(double newX, double newY) {
        x.set(newX);
        y.set(newY);
        shape.setCenterX(newX);
        shape.setCenterY(newY);
    }

    public Boundary getBounds() {
        return new Boundary(x.get() - radius.get(), y.get() - radius.get(),
                radius.get() * 2, radius.get() * 2);
    }
}
