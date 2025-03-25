package info.prog.agario.controller;

import info.prog.agario.model.entity.*;
import info.prog.agario.model.entity.ai.Enemy;
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
        for (Enemy e : world.getEnemies()) {
            for(Cell cell : e.getEnemyGroup().getCells()) {
                root.getChildren().add(cell.getShape());
            }
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
                    double speed = Math.max(5.0, 5.0 / Math.sqrt(cell.getMass()));
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
            for (int j = i + 1; j < cells.size(); j++) {
                Cell cell1 = cells.get(i);
                Cell cell2 = cells.get(j);

                if (cell1.canMerge(cell2) && cell1.getShape().getBoundsInParent().intersects(cell2.getShape().getBoundsInParent())) {
                    cell1.merge(cell2);
                    break;
                }
            }
        }

        boolean absorbedSomething = false;
        List<GameEntity> entitiesToRemove = new ArrayList<>();
        List<Enemy> enemiesToRemove = new ArrayList<>();

        for (GameEntity entity : world.getEntities()) {
            for (Cell cell : playerGroup.getCells()) {
                if (cell.getShape().getBoundsInParent().intersects(entity.getShape().getBoundsInParent())) {
                    System.out.println("Player -> qqch");
                    if (entity instanceof Pellet) {
                        System.out.println("Player -> Pellet");
                        cell.absorb(entity);
                        entitiesToRemove.add(entity);
                        absorbedSomething = true;
                        break;
                    }
                }
            }
        }

        for (Enemy enemy : world.getEnemies()) {
            for (Cell enemyCell : enemy.getEnemyGroup().getCells()) {
                for (Cell playerCell : playerGroup.getCells()) {
                    if (enemyCell.getShape().getBoundsInParent().intersects(playerCell.getShape().getBoundsInParent())) {
                        System.out.println("Enemy -> touche Joueur");
                        if (playerCell.getMass() >= enemyCell.getMass() * 1.33) {
                            System.out.println("Player -> Enemy");
                            playerCell.absorb(enemyCell);
                            entitiesToRemove.add(enemyCell);
                            enemiesToRemove.add(enemy);
                            absorbedSomething = true;
                            break;
                        } else if (enemyCell.getMass() >= playerCell.getMass() * 1.33) {
                            System.out.println("Enemy -> Player");
                            enemyCell.absorb(playerCell);
                            entitiesToRemove.add(playerCell);
                            absorbedSomething = true;
                            break;
                        }
                        System.out.println("on se respecte");
                    }
                }
                for (GameEntity entity : world.getEntities()) {
                    if (enemyCell.getShape().getBoundsInParent().intersects(entity.getShape().getBoundsInParent())) {
                        System.out.println("Enemy -> touche");
                        if (entity instanceof Pellet) {
                            System.out.println("Enemy -> Pellet");
                            enemyCell.absorb(entity);
                            entitiesToRemove.add(entity);
                            absorbedSomething = true;
                            break;
                        } else if (entity instanceof Cell) {
                            System.out.println("Enemy -> Cell");
                            enemyCell.absorb(entity);
                            entitiesToRemove.add(entity);
                            absorbedSomething = true;
                            break;
                        }
                    }
                }
            }
        }

        for (GameEntity entity : entitiesToRemove) {
            world.getEntities().remove(entity);
            System.out.println("Toutes les entités : " + world.getEntities().size());
            root.getChildren().remove(entity.getShape());
        }

        for (Enemy enemy : enemiesToRemove) {
            world.getEnemies().remove(enemy);
        }

        if (absorbedSomething) {
            camera.update();
        }
    }


}
