package info.prog.agario.model.entity;

import info.prog.agario.model.entity.player.Cell;
import info.prog.agario.view.GameView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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

public abstract class SpecialPellet extends Pellet {

    public SpecialPellet(double x, double y) {

        super(x, y, 10, UUID.randomUUID());
        ScaleTransition st = new ScaleTransition(Duration.seconds(0.5), this.shape);
        st.setByX(0.5);
        st.setByY(0.5);
        st.setAutoReverse(true);
        st.setCycleCount(ScaleTransition.INDEFINITE);
        st.play();

        Timeline colorChangeTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.1), event -> this.shape.setFill(Color.RED)),
                new KeyFrame(Duration.seconds(0.2), event -> this.shape.setFill(Color.ORANGE)),
                new KeyFrame(Duration.seconds(0.3), event -> this.shape.setFill(Color.YELLOW)),
                new KeyFrame(Duration.seconds(0.4), event -> this.shape.setFill(Color.GREEN)),
                new KeyFrame(Duration.seconds(0.5), event -> this.shape.setFill(Color.BLUE)),
                new KeyFrame(Duration.seconds(0.6), event -> this.shape.setFill(Color.VIOLET))
        );
        colorChangeTimeline.setCycleCount(Timeline.INDEFINITE);
        colorChangeTimeline.play();

        this.setMass(1);
    }

    public abstract void PlayEffect(Cell cell);

    public abstract void ExplosionEffect(Cell cell, Pane root);
}
