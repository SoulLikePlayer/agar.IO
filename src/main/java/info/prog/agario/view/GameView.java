package info.prog.agario.view;

import info.prog.agario.controller.GameController;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import info.prog.agario.model.world.MiniMap;
import info.prog.agario.model.world.GameWorld;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GameView {
    private Scene scene;

    private Pane mainRoot;
    private Pane root;
    private GameWorld world;
    private GameController controller;
    private MiniMap miniMap;

    public GameView(String pseudo) {
        mainRoot = new Pane();
        root = new Pane();
        world = new GameWorld(pseudo);
        controller = new GameController(world, root);
        scene = new Scene(mainRoot, 800, 600);
        controller.initialize();

        miniMap = new MiniMap(world.getEntities(), world.getPlayer());
        miniMap.updateEntities(world.getEntities(),world.getPlayer());
        mainRoot.getChildren().add(root);
        mainRoot.getChildren().add(miniMap);
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                miniMap.updateEntities(world.getEntities(), world.getPlayer());
            }
        }.start();
    }

    public Scene getScene() {
        return scene;
    }
}
