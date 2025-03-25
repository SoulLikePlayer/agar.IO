package info.prog.agario.model.entity;

import info.prog.agario.model.world.Boundary;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.Circle;

public abstract class GameEntity {
    protected double x, y, radius;
    protected Circle shape;

    public GameEntity(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.shape = new Circle(x, y, radius);
    }

    public Circle getShape() {
        return shape;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void move(double dX, double dY){
        this.x += dX;
        this.y += dY;
        this.shape.setCenterX(this.x);
        this.shape.setCenterY(this.y);
    }

    public double getRadius() {
        return this.radius;
    }

    public Boundary getBounds() {
        return new Boundary(this.x - this.radius, this.y - this.radius,this.radius * 2, this.radius * 2);
    }
}
