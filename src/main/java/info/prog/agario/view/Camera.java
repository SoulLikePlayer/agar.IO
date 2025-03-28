package info.prog.agario.view;

import info.prog.agario.model.entity.player.Cell;
import info.prog.agario.model.entity.player.Player;
import info.prog.agario.model.entity.player.PlayerGroup;
import javafx.scene.layout.Pane;

public class Camera {
    private Pane root;
    private Player player;
    private PlayerGroup group;
    private static final double CAMERA_LERP_FACTOR = 0.1;
    private static final double BASE_ZOOM = 1.0;
    private static final double MIN_ZOOM = 0.5;

    public Camera(Pane root, Player player) {
        this.root = root;
        this.player = player;
        this.group = player.getPlayerGroup();
    }

    public void smoothCenterOn() {
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

        double currentTranslateX = root.getTranslateX();
        double currentTranslateY = root.getTranslateY();
        double targetTranslateX = (root.getWidth() / 2 - centerX) * targetZoom;
        double targetTranslateY = (root.getHeight() / 2 - centerY) * targetZoom;

        double lerpX = currentTranslateX + CAMERA_LERP_FACTOR * (targetTranslateX - currentTranslateX);
        double lerpY = currentTranslateY + CAMERA_LERP_FACTOR * (targetTranslateY - currentTranslateY);

        root.setTranslateX(lerpX);
        root.setTranslateY(lerpY);
    }


    public void update() {
        smoothCenterOn();
    }
}
