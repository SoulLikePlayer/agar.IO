package info.prog.agario.model.entity;

import info.prog.agario.model.entity.player.Cell;
import info.prog.agario.view.GameView;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import javafx.scene.shape.Circle;

public abstract class SpecialPellet extends Pellet {

    public SpecialPellet(double x, double y) {
        super(x, y, 10);
        ScaleTransition st = new ScaleTransition(Duration.seconds(0.5), this.shape);
        st.setByX(0.5);
        st.setByY(0.5);
        st.setAutoReverse(true);
        st.setCycleCount(ScaleTransition.INDEFINITE);
        st.play();
        this.shape.setFill(Color.BLACK);
        this.setMass(1);
    }

    public abstract void PlayEffect(Cell cell);

    public abstract void ExplosionEffect(Cell cell, Pane root);
}
