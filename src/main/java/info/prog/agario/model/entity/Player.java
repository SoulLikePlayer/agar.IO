package info.prog.agario.model.entity;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Player extends GameEntity {
    private double mass;
    private String pseudo;
    private Text pseudoText;
    private Color color;

    public Player(double x, double y, double mass, String pseudo) {
        super(x, y, 10 * Math.sqrt(mass));
        this.mass = mass;
        this.pseudo = pseudo;
        this.color = Color.hsb(Math.random() * 360, 0.8, 0.9);
        this.shape.setFill(this.color);
        this.pseudoText = new Text(pseudo);
    }

    public void absorb(GameEntity entity) {
        this.mass += 10;
        this.radius.set(10 * Math.sqrt(mass));
    }

    public Text getPseudoText() {
        return pseudoText;
    }

    public double getMass() {
        return mass;
    }
}
