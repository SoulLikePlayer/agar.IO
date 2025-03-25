package info.prog.agario.model.entity.player;

import info.prog.agario.model.entity.GameEntity;
import javafx.scene.paint.Color;
import info.prog.agario.utils.AnimationUtils;

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

    public void setVelocity(double vx, double vy) {
        this.velocityX = vx;
        this.velocityY = vy;
    }

    public void move() {
        shape.setCenterX(shape.getCenterX() + velocityX);
        shape.setCenterY(shape.getCenterY() + velocityY);

        velocityX *= 0.95;
        velocityY *= 0.95;

        if (Math.abs(velocityX) < 0.1) velocityX = 0;
        if (Math.abs(velocityY) < 0.1) velocityY = 0;
    }

    public Cell(double x, double y, double mass, Color color) {
        super(x, y, 10 * Math.sqrt(mass));
        this.mass = mass;
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
        shape.setCenterX(shape.getCenterX() + dx * speedMultiplier);
        shape.setCenterY(shape.getCenterY() + dy * speedMultiplier);

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
    public void divide() {
        /*TODO : DIVISION CELLULAIRE :

        Votre joueur doit pouvoir se séparer en deux entités de même taille. Au moment de la séparation, une des deux
        entités est projetée à trois fois la vitesse maximale avant de ralentir jusqu’à la vitesse normale du joueur. (Rappelons
        que quand le joueur est constitué de plusieurs cellules, chacune a une vitesse maximale propre en fonction de sa
        taille).
        Une fois séparée, les cellules peuvent refusionner au bout d’un temps t, une formule de calcul possible est :
        t = C + m/100
        Avec C une constante de temps (par exemple 10 secondes) et m la masse de la cellule divisée.*/
    }

    @Override
    public void merge(PlayerComponent other) {
        /*TODO : REFUSION*/
    }
}
