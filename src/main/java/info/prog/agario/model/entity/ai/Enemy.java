package info.prog.agario.model.entity.ai;

import info.prog.agario.model.entity.GameEntity;
import info.prog.agario.model.entity.player.Cell;
import javafx.scene.paint.Color;

public class Enemy extends GameEntity {
    private double x;
    private double y;
    private final Color color;
    private Strategy strat;

    public Enemy(double x, double y, double mass) {
        super(x, y, 10 * Math.sqrt(mass));
        this.x = x;
        this.y = y;
        this.color = Color.hsb(Math.random() * 360, 0.8, 0.9);
        Cell firstCell = new Cell(x, y, mass, this.color);
        try {
            this.move();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }

    public void setX(double newX){
        this.x = newX;
    }

    public void setY(double newY){
        this.y = newY;
    }

    public void move() throws InterruptedException {
        strat = new RandomMovement(this);
        strat.movement();
        System.out.println("Final X : " + this.getShape().getCenterX() + " Final Y : " + this.getShape().getCenterY());
    }
}
