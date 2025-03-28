package info.prog.agario.controller;

import info.prog.agario.launcher.GameLauncher;
import info.prog.agario.model.entity.*;
import info.prog.agario.model.entity.ai.Enemy;
import info.prog.agario.model.entity.player.Cell;
import info.prog.agario.model.entity.player.Player;
import info.prog.agario.model.entity.player.PlayerComponent;
import info.prog.agario.model.entity.player.PlayerGroup;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Pos;

import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import info.prog.agario.model.world.GameWorld;
import info.prog.agario.view.Camera;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class GameController {
    private static final int TAUX_RESPAWN_ENEMY = 5; //Pourcentage de chance de respawn un ennemi à chaque update
    private static final int TAUX_RESPAWN_PELLET = 10; //Pourcentage de chance de respawn un pellet à chaque update
    private GameWorld world;
    private Pane root;
    private Camera camera;
    private long lastUpdate = 0;
    private static final long UPDATE_INTERVAL = 16_000_000;
    private double mouseX, mouseY;
    private AnchorPane mainRoot;
    private boolean gameOverAlertShown = false;

    /**
     * Constructor of the GameController class
     * @param world The game world
     * @param root The root pane
     * @param mainRoot The main root pane
     */
    public GameController(GameWorld world, Pane root, AnchorPane mainRoot) {
        this.mainRoot = mainRoot;
        this.world = world;
        this.root = root;
        this.camera = new Camera(root, world.getPlayer());

        root.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.widthProperty().addListener((obsWidth, oldWidth, newWidth) -> {
                    camera.smoothCenterOn();
                });
                newScene.heightProperty().addListener((obsHeight, oldHeight, newHeight) -> {
                    camera.smoothCenterOn();
                });
            }
        });
    }

    /**
     * Method to initialize the game
     */
    public void initialize() {
        for (GameEntity entity : world.getQuadTree().retrieve(world.getPlayer().getPlayerGroup().getCells().get(0), world.getPlayer().getPlayerGroup().getCells().get(0).getRadius() * 10 )) {
            if(entity instanceof Pellet) {
                root.getChildren().add(entity.getShape());
            }
        }
        for (Enemy e : world.getEnemies()) {
            for (Cell cell : e.getEnemyGroup().getCells()) {
                root.getChildren().add(cell.getShape());
                root.getChildren().add(cell.getPseudo());
            }
        }
        for (Cell cell : world.getPlayer().getPlayerGroup().getCells()) {
            root.getChildren().add(cell.getShape());
            root.getChildren().add(cell.getPseudo());
        }
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

    /**
     * Method to handle the key press
     * @param event The key pressed
     */
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.SPACE) {
            Player player = world.getPlayer();
            player.divide();

            List<Cell> updatedCells = player.getPlayerGroup().getCells();
            for (Cell cell : updatedCells) {
                if (!root.getChildren().contains(cell.getShape())) {
                    root.getChildren().add(cell.getShape());
                    root.getChildren().add(cell.getPseudo());
                    cell.getShape().toFront();
                    cell.getPseudo().toFront();
                }
            }
        }
        biggestInFront();
    }

    /**
     * Method to handle the mouse movement
     * @param event The mouse event
     */
    private void handleMouseMovement(MouseEvent event) {
        mouseX = event.getX();
        mouseY = event.getY();
    }

    /**
     * Method to update the player direction
     */
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

    /**
     * Method to update the game
     */
    private void update() {
        camera.update();
        Player player = world.getPlayer();
        PlayerGroup playerGroup = player.getPlayerGroup();
        List<Cell> cells = playerGroup.getCells();
        boolean absorbedSomething = false;

        List<GameEntity> entitiesToRemove = new ArrayList<>();
        List<Enemy> enemiesToRemove = new ArrayList<>();
        List<GameEntity> newEntities = new ArrayList<>();

        mergeCells(playerGroup, cells);
        processCellInteractions(cells, entitiesToRemove, newEntities);
        processEnemyInteractions(playerGroup, entitiesToRemove, enemiesToRemove);

        addNewEntitiesToRoot(newEntities);
        removeEntitiesFromWorld(entitiesToRemove, enemiesToRemove, playerGroup);

        if (absorbedSomething) {
            camera.update();
        }
        checkGameOver(playerGroup);
        respawnEntities();
    }

    /**
     * Method to merge the cells
     * @param playerGroup The player group
     * @param cells The cells to merge
     */
    private void mergeCells(PlayerGroup playerGroup, List<Cell> cells) {
        for (int i = 0; i < cells.size(); i++) {
            playerGroup.merge(cells.get(i));
        }
    }

    /**
     * Method to process the cell interactions
     * @param cells The cells
     * @param entitiesToRemove The entities to remove
     * @param newEntities The new entities
     */
    private void processCellInteractions(List<Cell> cells, List<GameEntity> entitiesToRemove, List<GameEntity> newEntities) {
        for (Cell cell : cells) {
            double searchRadius = cell.getRadius();
            List<GameEntity> nearbyEntities = world.getQuadTree().retrieve(cell, searchRadius * 2);
            newEntities.addAll(world.getQuadTree().retrieve(cell, searchRadius * 20));

            for (GameEntity entity : nearbyEntities) {
                if ((entity instanceof Pellet || entity instanceof ExplosionPellet) && cell.getShape().getBoundsInParent().intersects(entity.getShape().getBoundsInParent())) {
                    if (entity instanceof ExplosionPellet) {
                        cell.contactExplosion(entity, root);
                    } else {
                        cell.absorbPellet(entity);
                        entitiesToRemove.add(entity);
                    }
                    break;
                }
            }
        }
    }

    /**
     * Method to process the enemy interactions
     * @param playerGroup The player group
     * @param entitiesToRemove The entities to remove
     * @param enemiesToRemove The enemies to remove
     */
    private void processEnemyInteractions(PlayerGroup playerGroup, List<GameEntity> entitiesToRemove, List<Enemy> enemiesToRemove) {
        boolean absorbedSomething = false;

        for (Enemy enemy : world.getEnemies()) {
            for (Enemy enemy2 : world.getEnemies()) {
                if (enemy != enemy2) {
                    processEnemyCellInteractions(enemy, enemy2, entitiesToRemove, enemiesToRemove);
                }
            }
            processPlayerEnemyInteractions(playerGroup, enemy, entitiesToRemove, enemiesToRemove);
        }
    }

    /**
     * Method to process the enemy cell interactions
     * @param enemy The enemy
     * @param enemy2 The second enemy
     * @param entitiesToRemove The entities to remove
     * @param enemiesToRemove The enemies to remove
     */
    private void processEnemyCellInteractions(Enemy enemy, Enemy enemy2, List<GameEntity> entitiesToRemove, List<Enemy> enemiesToRemove) {
        for (Cell enemyCell1 : enemy.getEnemyGroup().getCells()) {
            for (Cell enemyCell2 : enemy2.getEnemyGroup().getCells()) {
                if (intersectionPercentage(enemyCell1, enemyCell2) > 33) {
                    if (enemyCell1.getMass() >= enemyCell2.getMass() * 1.33) {
                        enemyCell1.absorbCell(enemyCell2);
                        entitiesToRemove.add(enemyCell2);
                        enemiesToRemove.add(enemy2);
                    }
                }
            }
        }
    }

    /**
     * Method to process the player-enemy interactions
     * @param playerGroup The player group
     * @param enemy The enemy
     * @param entitiesToRemove The entities to remove
     * @param enemiesToRemove The enemies to remove
     */
    private void processPlayerEnemyInteractions(PlayerGroup playerGroup, Enemy enemy, List<GameEntity> entitiesToRemove, List<Enemy> enemiesToRemove) {
        boolean absorbedSomething = false;

        for (Cell enemyCell : enemy.getEnemyGroup().getCells()) {
            for (Cell playerCell : playerGroup.getCells()) {
                if (enemyCell.getShape().getBoundsInParent().intersects(playerCell.getShape().getBoundsInParent())) {
                    if (intersectionPercentage(playerCell, enemyCell) > 33) {
                        if (playerCell.getMass() >= enemyCell.getMass() * 1.33) {
                            playerCell.absorbCell(enemyCell);
                            entitiesToRemove.add(enemyCell);
                            enemiesToRemove.add(enemy);
                            absorbedSomething = true;
                            break;
                        } else if (enemyCell.getMass() >= playerCell.getMass() * 1.33) {
                            enemyCell.absorbCell(playerCell);
                            entitiesToRemove.add(playerCell);
                            absorbedSomething = true;
                            break;
                        }
                    }
                }
            }
            if (absorbedSomething) {
                break;
            }
            processEnemyEntityInteractions(enemyCell, entitiesToRemove);
        }
    }

    /**
     * Method to process the enemy entity interactions
     * @param enemyCell The enemy cell
     * @param entitiesToRemove The entities to remove
     */
    private void processEnemyEntityInteractions(Cell enemyCell, List<GameEntity> entitiesToRemove) {
        List<GameEntity> entities = world.getQuadTree().retrieve(enemyCell, enemyCell.getRadius() * 2);
        for (GameEntity entity : entities) {
            if ((entity instanceof Pellet || entity instanceof ExplosionPellet) && enemyCell.getShape().getBoundsInParent().intersects(entity.getShape().getBoundsInParent())) {
                if (entity instanceof ExplosionPellet) {
                    enemyCell.contactExplosion(entity, root);
                } else {
                    enemyCell.absorbPellet(entity);
                    entitiesToRemove.add(entity);
                }
                break;
            }
        }
    }

    /**
     * Method to add new entities to the root
     * @param newEntities The new entities
     */
    private void addNewEntitiesToRoot(List<GameEntity> newEntities) {
        for (GameEntity entity : newEntities) {
            if (!root.getChildren().contains(entity.getShape())) {
                root.getChildren().add(entity.getShape());
            }
        }
    }

    /**
     * Method to remove entities from the world
     * @param entitiesToRemove The entities to remove
     * @param enemiesToRemove The enemies to remove
     * @param playerGroup The player group
     */
    private void removeEntitiesFromWorld(List<GameEntity> entitiesToRemove, List<Enemy> enemiesToRemove, PlayerGroup playerGroup) {
        for (GameEntity entityToRemove : entitiesToRemove) {
            if (entityToRemove instanceof Cell) {
                root.getChildren().remove(((Cell) entityToRemove).getPseudo());
                playerGroup.removeComponent((Cell) entityToRemove);
                for (Enemy e : world.getEnemies()) {
                    e.getEnemyGroup().removeComponent((Cell) entityToRemove);
                }
            } else {
                world.getQuadTree().remove(entityToRemove);
                world.setNbEntities(world.getNbEntities() - 1);
            }
            root.getChildren().remove(entityToRemove.getShape());
        }

        for (Enemy enemyToRemove : enemiesToRemove) {
            world.getEnemies().remove(enemyToRemove);
            root.getChildren().remove(enemyToRemove.getShape());
        }
        biggestInFront();
    }

    /**
     * Method to check if the game is over
     * @param playerGroup The player group
     */
    private void checkGameOver(PlayerGroup playerGroup) {
        if (playerGroup.getCells().isEmpty() && !gameOverAlertShown) {
            gameOverAlertShown = true;
            Platform.runLater(() -> {
                Label gameOverLabel = new Label("Game Over! Vous avez perdu !");
                gameOverLabel.setFont(new Font("Arial", 30));
                gameOverLabel.setTextFill(Color.RED);
                gameOverLabel.setEffect(new DropShadow());

                Button btnPlayAgain = new Button("Rejouer");
                btnPlayAgain.setOnAction(e -> {
                    try {
                        Stage primaryStage = new Stage();
                        new GameLauncher().start(primaryStage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    Stage currentStage = (Stage) mainRoot.getScene().getWindow();
                    currentStage.close();
                });

                Button btnLeave = new Button("Quitter");
                btnLeave.setOnAction(e -> System.exit(0));

                VBox vbox = new VBox(10, gameOverLabel, btnPlayAgain, btnLeave);
                vbox.setAlignment(Pos.CENTER);
                vbox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
                vbox.setPrefSize(720,1080);

                StackPane stackPane = new StackPane(vbox);
                stackPane.setPrefSize(mainRoot.getWidth(), mainRoot.getHeight());

                mainRoot.getChildren().add(stackPane);
            });
        }
    }

    /**
     * Method to respawn the entities
     */
    public void respawnEntities(){
        if(world.getNbEnemies() < world.getEnemies().size()){
            if(new Random().nextInt(100) < TAUX_RESPAWN_ENEMY) {
                Random r = new Random();
                Enemy enemy = new Enemy(r.nextInt((int) (10 * Math.sqrt(world.getPlayer().getPlayerGroup().getCells().get(0).getMass())), world.getSize()), r.nextInt((int) (10 * Math.sqrt(world.getPlayer().getPlayerGroup().getCells().get(0).getMass())), world.getSize()), 10, world);
                world.getEnemies().add(enemy);
                for (Cell cell : enemy.getEnemyGroup().getCells()) {
                    root.getChildren().add(cell.getShape());
                    root.getChildren().add(cell.getPseudo());
                }
            }
        }
        if(world.getNbEntities() < world.getNbPellets()){
            if(new Random().nextInt(100) < TAUX_RESPAWN_PELLET){
                GameEntity pellet = EntityFactory.createEntity("pellet", Math.random() * world.getSize(), Math.random() * world.getSize(), 0);
                world.getQuadTree().insert(pellet);
                world.setNbEntities(world.getNbEntities() + 1);
            }
        }
    }

    /**
     * Method to put the biggest cell in front
     */
    private void biggestInFront(){
        List<Cell> cells = new ArrayList<>(world.getPlayer().getPlayerGroup().getCells());
        cells.sort(Comparator.comparing(Cell::getMass));
        for (Cell cell : cells) {
            cell.getShape().toFront();
            cell.getPseudo().toFront();
        }
    }

    /**
     * Method to calculate the intersection percentage
     * @param c1 The first cell
     * @param c2 The second cell
     * @return The intersection percentage
     */
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

    /**
     * Method to calculate the distance between two points
     * @param x1 The x position of the first point
     * @param y1 The y position of the first point
     * @param x2 The x position of the second point
     * @param y2 The y position of the second point
     * @return double The distance between the two points
     */
    private static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

}
