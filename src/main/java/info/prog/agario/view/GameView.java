package info.prog.agario.view;

import info.prog.agario.controller.GameController;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import info.prog.agario.model.world.GameWorld;

public class GameView {
    private Scene scene;
    private Pane root;
    private GameWorld world;
    private GameController controller;

    public GameView(String pseudo) {
        root = new Pane();
        world = new GameWorld(pseudo);
        controller = new GameController(world, root);
        scene = new Scene(root, 1080, 720);
        controller.initialize();
    }

    public Scene getScene() {
        return scene;
    }
}
