package info.prog.agario.view;

import info.prog.agario.model.entity.Player;
import javafx.scene.layout.Pane;

public class Camera {
    private Pane root;
    private Player player;

    public Camera(Pane root, Player player) {
        this.root = root;
        this.player = player;
    }

    public void update() {
        root.setTranslateX(-player.getShape().getCenterX() + 400);
        root.setTranslateY(-player.getShape().getCenterY() + 300);
    }
}
