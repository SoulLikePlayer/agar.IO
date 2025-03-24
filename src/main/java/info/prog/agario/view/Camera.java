package info.prog.agario.view;

import info.prog.agario.model.entity.Player;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Camera {
    private Pane root;
    private Player player;
    private static final double MAX_ZOOM_OUT_SIZE = 300;
    private static final double CAMERA_LERP_FACTOR = 0.1;
    private static final double ZOOM_ANIMATION_DURATION = 0.3;

    public Camera(Pane root, Player player) {
        this.root = root;
        this.player = player;
    }

    public void update() {
        double targetZoom = 1.0 / Math.sqrt(player.getMass() / 100);
        if (player.getShape().getRadius() >= MAX_ZOOM_OUT_SIZE) {
            targetZoom = 1.0;
        }

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(ZOOM_ANIMATION_DURATION), root);
        scaleTransition.setToX(targetZoom);
        scaleTransition.setToY(targetZoom);
        scaleTransition.play();

        double centeredX = (400 - player.getShape().getCenterX()) * targetZoom;
        double centeredY = (300 - player.getShape().getCenterY()) * targetZoom;

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(ZOOM_ANIMATION_DURATION), root);
        translateTransition.setToX(centeredX);
        translateTransition.setToY(centeredY);
        translateTransition.play();
    }
}
