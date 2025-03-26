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
        for (GameEntity entity : world.getEntities()) {
            if(entity instanceof Pellet) {
                root.getChildren().add(entity.getShape());
            }
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
        boolean absorbedSomething = false;
        List<GameEntity> entitiesToRemove = new ArrayList<>();
        List<Enemy> enemiesToRemove = new ArrayList<>();

        for (int i = 0; i < cells.size(); i++) {
            playerGroup.merge(cells.get(i));
        }

        for (GameEntity entity : world.getEntities()) {
            for (Cell playerCell : playerGroup.getCells()) {
                if (playerCell.getShape().getBoundsInParent().intersects(entity.getShape().getBoundsInParent())) {
                    System.out.println("Player -> qqch");
                    if (entity instanceof Pellet || entity instanceof SpecialPellet) {
                        if (entity instanceof ExplosionPellet){
                            System.out.println("Player -> Explosion Pellet");
                            playerCell.contactExplosion(entity, root);
                        }else{
                            System.out.println("Player -> Absorbable Pellet");
                            playerCell.absorbPellet(entity);
                            entitiesToRemove.add(entity);
                            absorbedSomething = true;
                        }
                        break;
                    } else if (entity instanceof Cell) {
                        System.out.println("Player -> Enemy or Enemy Spawn Point");
                    } else if (entity instanceof Enemy) {
                        System.out.println("Player -> Enemy Cell or Enemy Cell Spawn Point");
                        entitiesToRemove.add(entity);
                        //enemiesToRemove.add(entity);
                    }
                }
            }
        }

        for (Enemy enemy : world.getEnemies()) {
            for (Cell enemyCell : enemy.getEnemyGroup().getCells()) {
                for (Cell playerCell : playerGroup.getCells()) {
                    if (enemyCell.getShape().getBoundsInParent().intersects(playerCell.getShape().getBoundsInParent())) {
                        System.out.println("Enemy -> touche Joueur");
                        System.out.println("Overlap : " + intersectionPercentage(playerCell, enemyCell));
                        if(intersectionPercentage(playerCell, enemyCell) > 33) {
                            if (playerCell.getMass() >= enemyCell.getMass() * 1.33) {
                                System.out.println("Player -> Enemy");
                                playerCell.absorbCell(enemyCell);
                                entitiesToRemove.add(enemyCell);
                                enemiesToRemove.add(enemy);
                                absorbedSomething = true;
                                break;
                            } else if (enemyCell.getMass() >= playerCell.getMass() * 1.33) {
                                System.out.println("Enemy -> Player");
                                enemyCell.absorbCell(playerCell);
                                entitiesToRemove.add(playerCell);
                                absorbedSomething = true;
                                break;
                            } else {
                                System.out.println("on se respecte");
                            }
                        } else {
                            System.out.println("j'ai pas toucheooooooooooooooo");
                        }
                    }
                }
                if(absorbedSomething){
                    break;
                }
                for (GameEntity entity : world.getEntities()) {
                    if (enemyCell.getShape().getBoundsInParent().intersects(entity.getShape().getBoundsInParent())) {
                        if (entity instanceof Pellet) {
                            System.out.println("Enemy -> Pellet");
                            enemyCell.absorbPellet(entity);
                            entitiesToRemove.add(entity);
                            absorbedSomething = true;
                            break;
                        }
                    }
                }
            }
        }

        for (GameEntity entityToRemove : entitiesToRemove) {
            if(entityToRemove instanceof Cell){
                playerGroup.removeComponent((Cell)entityToRemove);
                for (Enemy e : world.getEnemies()) {
                e.getEnemyGroup().removeComponent((Cell) entityToRemove);
                }
            }
            world.getEntities().remove(entityToRemove);
            //System.out.println("Toutes les entités : " + world.getEntities().size());
            root.getChildren().remove(entityToRemove.getShape());
        }

        for(Enemy enemyToRemove : enemiesToRemove){
            world.getEnemies().remove(enemyToRemove);
            root.getChildren().remove(enemyToRemove.getShape());
        }
        smallestInFront();

        if (absorbedSomething) {
            System.out.println("Absorption détectée ! Mise à jour du zoom.");
            camera.update();
        }

        //System.out.println("Masse : " + player.getPlayerGroup().getCells().get(0).getMass());
    }


    private void smallestInFront(){
        List<Cell> cells = new ArrayList<>(world.getPlayer().getPlayerGroup().getCells());
        cells.sort(Comparator.comparing(Cell::getMass).reversed());
        for (Cell cell : cells) {
            cell.getShape().toFront();
        }
    }

    public static double intersectionPercentage(Cell c1, Cell c2) {
        double d = distance(c1.getX(), c1.getY(), c2.getX(), c2.getY());

        if (d > c1.getRadius() + c2.getRadius()) {
            return 0.0;
        }

        double r1 = c1.getRadius();
        double r2 = c2.getRadius();
        double r1_sq = r1 * r1;
        double r2_sq = r2 * r2;
        double d_sq = d * d;

        double part1 = r1_sq * Math.acos((d_sq + r1_sq - r2_sq) / (2 * d * r1));
        double part2 = r2_sq * Math.acos((d_sq + r2_sq - r1_sq) / (2 * d * r2));
        double part3 = 0.5 * Math.sqrt((-d + r1 + r2) * (d + r1 - r2) * (d - r1 + r2) * (d + r1 + r2));

        double area1 = Math.PI * Math.pow(c1.getRadius(), 2);
        double area2 = Math.PI * Math.pow(c2.getRadius(), 2);

        double intersectionArea = part1 + part2 - part3;
        double maxArea = c1.getRadius() < c2.getRadius() ? area1 : area2;

        return 100.0 * intersectionArea / maxArea;
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

}
