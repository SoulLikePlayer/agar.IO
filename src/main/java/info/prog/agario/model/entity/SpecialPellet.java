package info.prog.agario.model.entity;

import info.prog.agario.model.entity.player.Cell;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import javafx.scene.shape.Circle;

import java.util.UUID;

public abstract class SpecialPellet extends GameEntity {

    public SpecialPellet(double x, double y) {

        super(x, y, 10, UUID.randomUUID());
        ScaleTransition st = new ScaleTransition(Duration.seconds(0.5), this.shape);
        st.setByX(0.5); // Augmente la taille
        st.setByY(0.5);
        st.setAutoReverse(true); // Revient à la taille normale
        st.setCycleCount(ScaleTransition.INDEFINITE); // Animation infinie
        st.play();
        this.shape.setFill(Color.BLACK);
        this.setMass(1);
    }

    public abstract void PlayEffect(Cell cell);
    public abstract void ExplosionEffect(Cell cell, Pane root);
}
