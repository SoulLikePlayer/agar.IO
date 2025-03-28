package info.prog.agario.model.entity;

import info.prog.agario.model.world.Boundary;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.Circle;
import info.prog.agario.model.world.Boundary;

import java.util.UUID;

public abstract class GameEntity {
    private UUID ID;
    protected DoubleProperty x, y, radius;
    protected Circle shape;
    private double mass;

    public GameEntity(double x, double y, double radius, UUID ID) {
        this.ID = ID;
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

    public void move(double dX, double dY){
        this.x.set(this.x.get() + dX);
        this.y.set(this.y.get() + dY);
        this.shape.setCenterX(this.x.get());
        this.shape.setCenterY(this.y.get());
    }

    public void setPosition(double newX, double newY){
        this.x.setValue(newX);
        this.y.setValue(newY);
        this.shape.setCenterX(newX);
        this.shape.setCenterY(newY);
    }

    public double getRadius() {
        return radius.get();
    }

    public Boundary getBounds() {
        return new Boundary(x.get() - radius.get(), y.get() - radius.get(),radius.get() * 2, radius.get() * 2);
    }

    public UUID getID(){
        return ID;
    }
}
