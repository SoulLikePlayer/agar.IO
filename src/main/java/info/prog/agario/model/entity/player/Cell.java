package info.prog.agario.model.entity.player;

import info.prog.agario.model.entity.ExplosionPellet;
import info.prog.agario.model.entity.GameEntity;

import info.prog.agario.model.entity.SpecialPellet;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import info.prog.agario.utils.AnimationUtils;

import java.util.ArrayList;
import java.util.List;



public class Cell extends GameEntity implements PlayerComponent {


    private static final int MERGE_TIME = 5000;

    private static final int BOOST_TIME = 1000;
    private double multiplicatorGain;
    private double mass;
    private Color color;
    private double speedMultiplier;
    private double velocityX = 0;
    private double velocityY = 0;
    private PlayerGroup parentGroup;

    private long lastDivisionTime;

    public void setParentGroup(PlayerGroup parentGroup) {
        this.parentGroup = parentGroup;
    }

    public PlayerGroup getParentGroup() {
        return parentGroup;
    }

    public Color getColor() {
        return color;
    }

    public void setVelocity(double vx, double vy) {
        this.velocityX = vx;
        this.velocityY = vy;
    }

    public double getMultiplicatorGain() {
        return multiplicatorGain;
    }

    public void setMultiplicatorGain(double multiplicator) {
        multiplicatorGain = multiplicator;
    }



    public Cell(double x, double y, double mass, Color color) {
        super(x, y, 10 * Math.sqrt(mass));
        this.mass = mass;
        this.color = color;
        this.shape.setFill(color);
        this.speedMultiplier = 3.0;
        this.shape.radiusProperty().bind(this.radius);
        multiplicatorGain = 1;
        System.out.println("Nouvelle cellule à x=" + x + ", y=" + y + ", radius=" + this.radius.get());
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
        this.radius.set(10 * Math.sqrt(mass));
    }

    public void move(double dx, double dy) {
        Double newX = shape.getCenterX() + dx * speedMultiplier;
        Double newY = shape.getCenterY() + dy * speedMultiplier;
        shape.setCenterX(newX);
        shape.setCenterY(newY);
        x.setValue(newX);
        y.setValue(newY);

    }


    public void contactExplosion(GameEntity entity, Pane root){
        if (entity instanceof SpecialPellet) {
            if(entity instanceof ExplosionPellet){
                ((SpecialPellet) entity).ExplosionEffect(this, root);
            }
            ((SpecialPellet) entity).PlayEffect(this);
        }
    }

    public void absorb(GameEntity entity) {
        if (entity instanceof SpecialPellet) {

            ((SpecialPellet) entity).PlayEffect(this);
        }
        this.mass += entity.getMass() * multiplicatorGain;
        this.radius.set(10 * Math.sqrt(mass));
        AnimationUtils.playGrowAnimation(this.shape);
    }

    public void setSpeedMultiplier(double multiplier) {
        this.speedMultiplier = multiplier;
    }

    public double GetSpeedMultiplier() {
        return speedMultiplier;
    }
    public void updateSpeed() {
        this.speedMultiplier = Math.max(0.5, 10.0 / Math.sqrt(this.mass));
    }

    @Override
    public PlayerComponent divide() {
        if (mass < 20) return null;

        double newMass = this.mass / 2;
        this.setMass(newMass);

        Cell newCell = new Cell(this.x.getValue() + 20, this.y.getValue() + 20, newMass, this.color);

        newCell.updateSpeed();
        this.lastDivisionTime = System.currentTimeMillis();
        newCell.lastDivisionTime = System.currentTimeMillis();

        if (parentGroup != null) {
            parentGroup.addComponent(newCell);
        }

        System.out.println("Division effectuée :");
        System.out.println(" - Ancienne cellule - Masse : " + this.mass + ", Vitesse : " + this.speedMultiplier);
        System.out.println(" - Nouvelle cellule - Masse : " + newCell.getMass() + ", Vitesse : " + newCell.speedMultiplier);
        System.out.println(" - Ancienne cellule - Radius : " + this.radius);
        System.out.println(" - Nouvelle cellule - Radius : " + newCell.radius);

        return this;
    }


    @Override
    public void merge(PlayerComponent other) {
        if (!(other instanceof Cell)) return;

        Cell otherCell = (Cell) other;

        if (!canMerge(otherCell)) return;

        this.mass += otherCell.getMass();
        this.radius.set(10 * Math.sqrt(mass));
        this.updateSpeed();

        if (parentGroup != null) {
            parentGroup.removeComponent(otherCell);
        }

        if (otherCell.getShape().getParent() != null) {
            ((Pane) otherCell.getShape().getParent()).getChildren().remove(otherCell.getShape());
        }

        System.out.println("Fusion effectuée !");
    }

    public boolean canMerge(Cell other) {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - this.lastDivisionTime;

        double requiredTime = MERGE_TIME + this.mass / 100.0; //

        return elapsedTime >= requiredTime;
    }

    @Override
    public List<Cell> getCells() {
        ArrayList<Cell> lst = new ArrayList();
        lst.add(this);
        return lst;
    }


}
