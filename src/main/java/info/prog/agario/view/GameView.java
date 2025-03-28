package info.prog.agario.view;

import info.prog.agario.controller.GameController;
import info.prog.agario.model.world.MiniMap;
import info.prog.agario.model.world.ScoreBoard;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import info.prog.agario.model.world.GameWorld;

public class GameView {
    private Scene scene;
    private AnchorPane mainRoot;
    private Pane root;
    private GameWorld world;
    private GameController controller;
    private MiniMap miniMap;
    private ScoreBoard scoreBoard;
    private Canvas gridCanvas;

    public GameView(String pseudo) {
        mainRoot = new AnchorPane();
        root = new Pane();
        root.setPrefSize(1080, 720); // Adjusting the size of the root Pane to match the scene
        mainRoot.setStyle("-fx-background-color: #000000;");
        world = new GameWorld(pseudo);
        controller = new GameController(world, root, mainRoot);

        // Create a canvas for the grid
        gridCanvas = new Canvas(4440, 4440); // Adjusting the size of the canvas to match the scene
        drawGrid(gridCanvas);
        root.getChildren().add(gridCanvas);

        scene = new Scene(mainRoot, 1080, 720);
        controller.initialize();

        miniMap = new MiniMap(world.getQuadTree().retrieve(world.getPlayer().getPlayerGroup().getCells().get(0), world.getPlayer().getPlayerGroup().getCells().get(0).getRadius() * 10), world.getPlayer());
        miniMap.updateEntities(world.getQuadTree().retrieve(world.getPlayer().getPlayerGroup().getCells().get(0), world.getPlayer().getPlayerGroup().getCells().get(0).getRadius() * 10), world.getPlayer());

        mainRoot.getChildren().add(root);
        AnchorPane.setBottomAnchor(miniMap, 10.0);
        AnchorPane.setRightAnchor(miniMap, 10.0);
        mainRoot.getChildren().add(miniMap);

        scoreBoard = new ScoreBoard(world);
        AnchorPane.setTopAnchor(scoreBoard, 10.0);
        AnchorPane.setRightAnchor(scoreBoard, 10.0);
        mainRoot.getChildren().add(scoreBoard);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!world.getPlayer().getPlayerGroup().getCells().isEmpty()) {
                    miniMap.updateEntities(world.getQuadTree().retrieve(world.getPlayer().getPlayerGroup().getCells().get(0), world.getPlayer().getPlayerGroup().getCells().get(0).getRadius() * 10), world.getPlayer());
                    scoreBoard.updateScores();  // Mise à jour du scoreboard
                }
            }
        }.start();
    }

    private void drawGrid(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(1);

        double width = canvas.getWidth();
        double height = canvas.getHeight();
        double gridSize = 50; // Size of the grid squares

        // Draw vertical grid lines
        for (double x = 0; x <= width; x += gridSize) {
            gc.strokeLine(x, 0, x, height);
        }

        // Draw horizontal grid lines
        for (double y = 0; y <= height; y += gridSize) {
            gc.strokeLine(0, y, width, y);
        }
    }

    public Scene getScene() {
        return scene;
    }
}