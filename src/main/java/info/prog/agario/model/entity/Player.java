package info.prog.agario.model.entity;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import info.prog.agario.utils.AnimationUtils;

public class Player extends GameEntity {
    private double mass;
    private String pseudo;
    private Text pseudoText;
    private Color color;
    private DoubleProperty targetX, targetY;
    private static final double MOVEMENT_LERP_FACTOR = 0.15;

    public Player(double x, double y, double mass, String pseudo) {
        super(x, y, 10 * Math.sqrt(mass));
        this.mass = mass;
        this.pseudo = pseudo;
        this.color = Color.hsb(Math.random() * 360, 0.8, 0.9);
        this.shape.setFill(this.color);
        this.pseudoText = new Text(pseudo);
        this.pseudoText.setFill(Color.WHITE);
        this.pseudoText.setStyle("-fx-font-weight: bold;");
        updatePseudoPosition();
        AnimationUtils.playHeartbeatAnimation(this.shape);

        this.targetX = new SimpleDoubleProperty(x);
        this.targetY = new SimpleDoubleProperty(y);
    }

    public void setTargetPosition(double x, double y) {
        this.targetX.set(x);
        this.targetY.set(y);
    }

    public void updatePosition() {
        double newX = shape.getCenterX() + (targetX.get() - shape.getCenterX()) * MOVEMENT_LERP_FACTOR;
        double newY = shape.getCenterY() + (targetY.get() - shape.getCenterY()) * MOVEMENT_LERP_FACTOR;

        shape.setCenterX(newX);
        shape.setCenterY(newY);
        updatePseudoPosition();
    }

    public void absorb(GameEntity entity) {
        AnimationUtils.playPelletAbsorptionAnimation(entity.getShape(), this.shape);
        this.mass += 10;
        this.radius.set(10 * Math.sqrt(mass));
        updatePseudoPosition();
        AnimationUtils.playGrowAnimation(this.shape);
    }

    public void updatePseudoPosition() {
        double baseFontSize = radius.get() * 0.4;
        pseudoText.setFont(new Font(baseFontSize));

        double maxWidth = radius.get() * 1.5;
        double scaleFactor = Math.min(1.0, maxWidth / pseudoText.getLayoutBounds().getWidth());

        pseudoText.setFont(new Font(baseFontSize * scaleFactor));

        pseudoText.setX(shape.getCenterX() - pseudoText.getLayoutBounds().getWidth() / 2);
        pseudoText.setY(shape.getCenterY() + pseudoText.getLayoutBounds().getHeight() / 4);
    }


    public Text getPseudoText() {
        return pseudoText;
    }

    public double getMass() {
        return mass;
    }
}
