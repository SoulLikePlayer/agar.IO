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

import java.util.ArrayList;
import java.util.Comparator;
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
        for (Cell cell : world.getPlayer().getPlayerGroup().getCells()) {
            root.getChildren().add(cell.getShape());
        }
        for (GameEntity entity : world.getQuadTree().retrieve(world.getPlayer().getPlayerGroup().getCells().get(0), world.getPlayer().getPlayerGroup().getCells().get(0).getRadius() * 2 )) {
            root.getChildren().add(entity.getShape());
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
            Player player = world.getPlayer();
            player.divide();

            List<Cell> updatedCells = player.getPlayerGroup().getCells();
            for (Cell cell : updatedCells) {
                if (!root.getChildren().contains(cell.getShape())) {
                    root.getChildren().add(cell.getShape());
                    cell.getShape().toFront();
                }
            }
        }
        smallestInFront();
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

        Player player = world.getPlayer();
        PlayerGroup playerGroup = player.getPlayerGroup();
        List<Cell> cells = playerGroup.getCells();

        for (int i = 0; i < cells.size(); i++) {
            playerGroup.merge(cells.get(i));
        }

        boolean absorbedSomething = false;

        for (Cell cell : cells) {
            double searchRadius = cell.getRadius();
            List<GameEntity> nearbyEntities = world.getQuadTree().retrieve(cell, searchRadius * 2);

            for (GameEntity entity : nearbyEntities) {
                if (entity instanceof Pellet && cell.getShape().getBoundsInParent().intersects(entity.getShape().getBoundsInParent())) {
                    cell.absorb(entity);
                    root.getChildren().remove(entity.getShape());
                    world.getQuadTree().remove(entity);
                    absorbedSomething = true;
                    break;
                }
            }
        }
        smallestInFront();

        if (absorbedSomething) {
            System.out.println("Absorption détectée ! Mise à jour du zoom.");
            camera.update();
        }
    }


    private void smallestInFront(){
        List<Cell> cells = new ArrayList<>(world.getPlayer().getPlayerGroup().getCells());
        cells.sort(Comparator.comparing(Cell::getMass).reversed());
        for (Cell cell : cells) {
            cell.getShape().toFront();
        }
    }
}
