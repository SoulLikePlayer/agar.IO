package info.prog.agario.model.entity;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.Circle;
import info.prog.agario.model.world.Boundary;

public abstract class GameEntity {
    protected DoubleProperty x, y, radius;
    protected Circle shape;
    private double mass;

    public GameEntity(double x, double y, double radius) {
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
        this.radius = new SimpleDoubleProperty(radius);
        this.shape = new Circle(x, y, radius);
        mass = 0;
    }

    public void setMass(double mass){
        this.mass = mass;
    }
    public double getMass(){
        return mass;
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
