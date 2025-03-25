package info.prog.agario.model.entity.player;

import info.prog.agario.model.entity.GameEntity;
import javafx.scene.layout.Pane;
import info.prog.agario.model.entity.Pellet;
import info.prog.agario.model.entity.ai.Enemy;
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
    private static final int BOOST_DURATION = 1000;

    private static final int BOOST_MULTIPLIER = 3;
    private boolean isBoosted = false;
    private long boostStartTime;

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
        this.speedMultiplier = 3.0;
        this.shape.radiusProperty().bind(this.radius);
        System.out.println("Nouvelle cellule à x=" + x + ", y=" + y + ", radius=" + this.radius.get());
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
        this.radius.set(10 * Math.sqrt(mass));
        updateSpeed();
    }

    public void move(double dx, double dy) {
        long currentTime = System.currentTimeMillis();
        if (isBoosted && (currentTime - boostStartTime >= BOOST_DURATION)) {
            isBoosted = false;
            updateSpeed();
            System.out.println("Boost terminé, vitesse normale : " + speedMultiplier);
        }
        //System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");

        double newX = shape.getCenterX() + dx * speedMultiplier;
        double newY = shape.getCenterY() + dy * speedMultiplier;
        shape.setCenterX(newX);
        shape.setCenterY(newY);
        x.setValue(newX);
        y.setValue(newY);
    }

    public void absorb(GameEntity entity) {
        this.mass += 10;
        this.radius.set(10 * Math.sqrt(mass));
        updateSpeed();
        AnimationUtils.playGrowAnimation(this.shape);
        System.out.println("cccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc");
    }

    public void setSpeedMultiplier(double multiplier) {
        this.speedMultiplier = multiplier;
    }

    @Override
    public void divide() {
        if (mass < 20) return;

        double newMass = this.mass / 2;

        this.setMass(newMass);

        Cell newCell = new Cell(this.x.getValue(), this.y.getValue(), newMass, this.color);

        newCell.updateSpeed();

        newCell.isBoosted = true;
        newCell.boostStartTime = System.currentTimeMillis();
        newCell.speedMultiplier *= BOOST_MULTIPLIER;

        this.lastDivisionTime = System.currentTimeMillis();
        newCell.lastDivisionTime = this.lastDivisionTime;

        if (parentGroup != null) {
            parentGroup.addComponent(newCell);
        }
    }

    public void updateSpeed() {
        setSpeedMultiplier(Math.max(0.5, 10.0 / Math.sqrt(this.mass)));
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
        AnimationUtils.playGrowAnimation(this.shape);
        System.out.println("Fusion effectuée !");
    }




    public boolean canMerge(Cell other) {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - this.lastDivisionTime;

        double requiredTime = MERGE_TIME + this.mass / 100.0;

        return elapsedTime >= requiredTime;
    }

    @Override
    public List<Cell> getCells() {
        ArrayList<Cell> lst = new ArrayList();
        lst.add(this);
        return lst;
    }
}
