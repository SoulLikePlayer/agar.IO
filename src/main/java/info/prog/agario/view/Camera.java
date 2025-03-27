package info.prog.agario.view;

import info.prog.agario.model.entity.player.Cell;
import info.prog.agario.model.entity.player.Player;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Camera {
    private Pane root;
    private Player player;
    private static final double CAMERA_LERP_FACTOR = 0.1;
    private static final double ZOOM_ANIMATION_DURATION = 0.3;
    private static final double BASE_ZOOM = 1.0;
    private static final double MIN_ZOOM = 0.5;

    public Camera(Pane root, Player player) {
        this.root = root;
        this.player = player;
    }

    public void update() {
        double totalX = 0, totalY = 0, totalMass = 0;
        for (Cell cell : player.getPlayerGroup().getCells()) {
            totalX += cell.getShape().getCenterX() * cell.getMass();
            totalY += cell.getShape().getCenterY() * cell.getMass();
            totalMass += cell.getMass();
        }

        double centerX = totalX / totalMass;
        double centerY = totalY / totalMass;

        double targetZoom = BASE_ZOOM / (Math.sqrt(totalMass / 100));
        targetZoom = Math.max(MIN_ZOOM, Math.min(BASE_ZOOM, targetZoom));

        root.setScaleX(targetZoom);
        root.setScaleY(targetZoom);

        double averageRadius = Math.sqrt(totalMass) * 10;
        double centeredX = (400 - centerX) * targetZoom;
        double centeredY = (300 - centerY) * targetZoom;

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(ZOOM_ANIMATION_DURATION), root);
        translateTransition.setToX(centeredX);
        translateTransition.setToY(centeredY);
        translateTransition.play();
    }

}