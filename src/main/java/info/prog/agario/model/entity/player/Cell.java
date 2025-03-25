package info.prog.agario.model.entity.player;

import info.prog.agario.model.entity.GameEntity;
import javafx.scene.paint.Color;
import info.prog.agario.utils.AnimationUtils;

import java.util.ArrayList;
import java.util.List;

public class Cell extends GameEntity implements PlayerComponent {
    private double mass;
    private Color color;
    private double speedMultiplier;
    private double velocityX = 0;
    private double velocityY = 0;
    private PlayerGroup parentGroup;

    private static final int MERGE_TIME = 5000;

    private static final int BOOST_TIME = 1000;

    public void setParentGroup(PlayerGroup parentGroup) {
        this.parentGroup = parentGroup;
    }

    public PlayerGroup getParentGroup() {
        return parentGroup;
    }

    private long lastDivisionTime;


    public void setVelocity(double vx, double vy) {
        this.velocityX = vx;
        this.velocityY = vy;
    }

    public void move() {
        Double newX = shape.getCenterX() + velocityX;
        Double newY = shape.getCenterY() + velocityY;
        shape.setCenterX(newX);
        shape.setCenterY(newY);
        x.setValue(newX);
        y.setValue(newY);

        velocityX *= 0.95;
        velocityY *= 0.95;

        if (Math.abs(velocityX) < 0.1) velocityX = 0;
        if (Math.abs(velocityY) < 0.1) velocityY = 0;
    }

    public Cell(double x, double y, double mass, Color color) {
        super(x, y, 10 * Math.sqrt(mass));
        this.setMass(mass);
        this.color = color;
        this.shape.setFill(color);
        this.speedMultiplier = 3;
        this.shape.radiusProperty().bind(this.radius);
        System.out.println("Nouvelle cellule à x=" + x + ", y=" + y + ", radius=" + this.radius.get());
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

    public void absorb(GameEntity entity) {
        this.mass += 10;
        this.radius.set(10 * Math.sqrt(mass));
        AnimationUtils.playGrowAnimation(this.shape);
    }

    public void setSpeedMultiplier(double multiplier) {
        this.speedMultiplier = multiplier;
    }

    @Override
    public PlayerComponent divide() {
        if (mass < 20) return null;

        double newMass = this.mass / 2;
        this.setMass(newMass);

        Cell newCell = new Cell(this.x.getValue() + 20, this.y.getValue() + 20, newMass, this.color);

        this.updateSpeed();
        newCell.updateSpeed();

        // Met à jour le temps de dernière division (utile pour la fusion)
        this.lastDivisionTime = System.currentTimeMillis();
        newCell.lastDivisionTime = this.lastDivisionTime;

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

    public void updateSpeed() {
        this.speedMultiplier = Math.max(0.5, 5.0 / Math.sqrt(this.mass));
    }


    @Override
    public void merge(PlayerComponent other) {
        if (other instanceof Cell) {
            Cell otherCell = (Cell) other;

            if (!canMerge(otherCell)) return;

            this.mass += otherCell.getMass();
            this.radius.set(10 * Math.sqrt(mass));

            if (parentGroup != null) {
                parentGroup.removeComponent(otherCell);
            }

            System.out.println("Fusion de cellules !");
        }
    }

    public boolean canMerge(Cell other) {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastDivisionTime;


        double requiredTime = MERGE_TIME + 100 * Math.sqrt(this.mass);

        return elapsedTime >= requiredTime;
    }

    @Override
    public List<Cell> getCells() {
        ArrayList<Cell> lst = new ArrayList();
        lst.add(this);
        return lst;
    }
}
