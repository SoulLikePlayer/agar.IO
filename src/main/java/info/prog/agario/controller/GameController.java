package info.prog.agario.controller;

import info.prog.agario.model.entity.*;
import info.prog.agario.model.entity.player.Cell;
import info.prog.agario.model.entity.player.Player;
import info.prog.agario.model.entity.player.PlayerComponent;
import info.prog.agario.model.entity.player.PlayerGroup;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import info.prog.agario.model.world.GameWorld;
import info.prog.agario.view.Camera;
import java.util.Iterator;
import java.util.List;

public class GameController {
    private GameWorld world;
    private Pane root;
    private Camera camera;
    private long lastUpdate = 0;
    private static final long UPDATE_INTERVAL = 16_000_000;
    private double mouseX, mouseY;

    public GameController(GameWorld world, Pane root) {
        this.world = world;
        this.root = root;
        this.camera = new Camera(root, world.getPlayer());
    }

    public void initialize() {
        for (GameEntity entity : world.getEntities()) {
            root.getChildren().add(entity.getShape());
        }

        for (Cell cell : world.getPlayer().getPlayerGroup().getCells()) {
            root.getChildren().add(cell.getShape());
        }

        root.getChildren().add(world.getPlayer().getPseudoText());
        root.setOnKeyPressed(this::handleKeyPress);
        root.setFocusTraversable(true);
        root.requestFocus();
        root.setOnMouseMoved(this::handleMouseMovement);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= UPDATE_INTERVAL) {
                    updatePlayerDirection();
                    update();
                    lastUpdate = now;
                }
            }
        };
        timer.start();
    }

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.SPACE) {
            world.getPlayer().divide();
            for (Cell cell : world.getPlayer().getPlayerGroup().getCells()) {
                if (!root.getChildren().contains(cell.getShape())) {
                    root.getChildren().add(cell.getShape());
                    cell.getShape().toFront();
                }
            }
        }
    }

    private void handleMouseMovement(MouseEvent event) {
        mouseX = event.getX();
        mouseY = event.getY();
    }

    private void updatePlayerDirection() {
        camera.update();

        Player player = world.getPlayer();
        PlayerGroup playerGroup = player.getPlayerGroup();

        for (PlayerComponent component : playerGroup.getComponents()) {
            if (component instanceof Cell) {
                Cell cell = (Cell) component;
                double dx = mouseX - cell.getShape().getCenterX();
                double dy = mouseY - cell.getShape().getCenterY();
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance > 1) {
                    double speed = Math.max(0.5, 5.0 / Math.sqrt(cell.getMass()));
                    cell.setVelocity(dx / distance * speed, dy / distance * speed);
                    cell.move(dx / distance * speed, dy / distance * speed);
                }
            }
        }
    }

    private void update() {
        camera.update();
        Iterator<GameEntity> iterator = world.getEntities().iterator();
        while (iterator.hasNext()) {
            GameEntity entity = iterator.next();
            boolean absorbed = false;

            for (Cell cell : world.getPlayer().getPlayerGroup().getCells()) {
                if (cell.getShape().getBoundsInParent().intersects(entity.getShape().getBoundsInParent())) {
                    if (entity instanceof Cell || entity instanceof Pellet) {
                        cell.absorb(entity);
                        root.getChildren().remove(entity.getShape());
                        iterator.remove();
                        absorbed = true;
                        break;
                    }
                }
            }

            if (absorbed) break;
        }
    }


}
