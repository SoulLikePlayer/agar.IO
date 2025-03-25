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

    public void setParentGroup(PlayerGroup parentGroup) {
        this.parentGroup = parentGroup;
    }

    public PlayerGroup getParentGroup() {
        return parentGroup;
    }

    private long lastDivisionTime;

    private static final int TEMPS_REFUSION = 5000;

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
    }

    public void move(double dx, double dy) {
        Double newX = shape.getCenterX() + dx * speedMultiplier;
        Double newY = shape.getCenterY() + dy * speedMultiplier;
        shape.setCenterX(newX);
        shape.setCenterY(newY);
        x.setValue(newX);
        y.setValue(newY);

        if (speedMultiplier > 1) {
            speedMultiplier *= 0.95;
        }
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
        if (mass < 20) return this;

        double newMass = this.mass / 2;
        this.mass = newMass;

        // Création des deux nouvelles cellules
        Cell newCell1 = new Cell(x.getValue() + 20, y.getValue() + 20, newMass, color);
        Cell newCell2 = new Cell(x.getValue() - 20, y.getValue() - 20, newMass, color);

        // Leur donner une impulsion
        newCell1.setVelocity(3 * velocityX + 1, 3 * velocityY + 1);
        newCell2.setVelocity(3 * velocityX - 1, 3 * velocityY - 1);

        // Ajouter les nouvelles cellules au groupe parent
        if (parentGroup != null) {
            parentGroup.addComponent(newCell1);
            parentGroup.addComponent(newCell2);
        }

        return this; // Retourne la cellule d'origine
    }

    @Override
    public void merge(PlayerComponent other) {
        if (other instanceof Cell) {
            Cell otherCell = (Cell) other;

            // Vérifier si le temps est écoulé avant fusion
            if (!canMerge(otherCell)) return;

            this.mass += otherCell.getMass();
            this.radius.set(10 * Math.sqrt(mass));

            // Supprimer l'autre cellule du groupe parent
            if (parentGroup != null) {
                parentGroup.removeComponent(otherCell);
            }

            System.out.println("Fusion de cellules !");
        }
    }

    public boolean canMerge(Cell other) {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastDivisionTime;

        // Formule du temps de fusion (exemple basé sur 6.2)
        double requiredTime = TEMPS_REFUSION + 100 * Math.sqrt(this.mass);

        return elapsedTime >= requiredTime;
    }

    @Override
    public List<Cell> getCells() {
        ArrayList<Cell> lst = new ArrayList();
        lst.add(this);
        return lst;
    }
}
