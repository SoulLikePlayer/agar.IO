package info.prog.agario.controller;

import javafx.animation.AnimationTimer;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import info.prog.agario.model.entity.GameEntity;
import info.prog.agario.model.entity.Player;
import info.prog.agario.model.world.GameWorld;
import info.prog.agario.view.Camera;

public class GameController {
    private GameWorld world;
    private Pane root;
    private Camera camera;

    public GameController(GameWorld world, Pane root) {
        this.world = world;
        this.root = root;
        this.camera = new Camera(root, world.getPlayer());
    }

    public void initialize() {
        for (GameEntity entity : world.getEntities()) {
            root.getChildren().add(entity.getShape());
        }
        root.getChildren().add(world.getPlayer().getShape());
        root.setOnMouseMoved(this::handleMouseMovement);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();
    }

    private void handleMouseMovement(MouseEvent event) {
        Player player = world.getPlayer();
        double dx = event.getX() - player.getShape().getCenterX();
        double dy = event.getY() - player.getShape().getCenterY();
        double speed = 2.0 / Math.sqrt(player.getShape().getRadius());

        player.getShape().setCenterX(player.getShape().getCenterX() + dx * speed);
        player.getShape().setCenterY(player.getShape().getCenterY() + dy * speed);
    }

    private void update() {
        camera.update();
    }
}
