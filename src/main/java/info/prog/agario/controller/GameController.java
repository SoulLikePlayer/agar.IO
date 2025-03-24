package info.prog.agario.controller;

import javafx.animation.AnimationTimer;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import info.prog.agario.model.entity.GameEntity;
import info.prog.agario.model.entity.Player;
import info.prog.agario.model.world.GameWorld;
import info.prog.agario.view.Camera;
import info.prog.agario.utils.AnimationUtils;

import java.util.Iterator;

public class GameController {
    private GameWorld world;
    private Pane root;
    private Camera camera;
    private long lastUpdate = 0;
    private static final long UPDATE_INTERVAL = 16_000_000;

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
        root.getChildren().add(world.getPlayer().getPseudoText());
        root.setOnMouseMoved(this::handleMouseMovement);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= UPDATE_INTERVAL) {
                    update();
                    lastUpdate = now;
                }
            }
        };
        timer.start();
    }

    private void handleMouseMovement(MouseEvent event) {
        Player player = world.getPlayer();
        double dx = event.getX() - player.getShape().getCenterX();
        double dy = event.getY() - player.getShape().getCenterY();
        double speed = Math.max(0.5, 5.0 / Math.sqrt(player.getMass()));

        player.getShape().setCenterX(player.getShape().getCenterX() + dx * speed * 0.005);
        player.getShape().setCenterY(player.getShape().getCenterY() + dy * speed * 0.005);
        player.updatePseudoPosition();
    }

    private void update() {
        camera.update();

        Iterator<GameEntity> iterator = world.getEntities().iterator();
        while (iterator.hasNext()) {
            GameEntity entity = iterator.next();
            if (world.getPlayer().getShape().getBoundsInParent().intersects(entity.getShape().getBoundsInParent())) {
                world.getPlayer().absorb(entity);
                root.getChildren().remove(entity.getShape());
                iterator.remove();
            }
        }
    }
}
