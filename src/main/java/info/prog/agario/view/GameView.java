package info.prog.agario.view;

import info.prog.agario.controller.GameController;
import info.prog.agario.network.GameClient;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import info.prog.agario.model.world.GameWorld;
import info.prog.agario.model.entity.GameEntity;
import info.prog.agario.model.entity.EntityFactory;

public class GameView {
    private Scene scene;
    private Pane root;
    private GameWorld world;
    private GameController controller;
    private GameClient client;
    private boolean isOnline;

    public GameView(String pseudo) {
        this(pseudo, null);
    }

    public GameView(String pseudo, GameClient client) {
        this.client = client;
        this.isOnline = (client != null);
        root = new Pane();
        world = new GameWorld(pseudo);
        controller = new GameController(world, root);
        scene = new Scene(root, 800, 600);
        controller.initialize();

        if (isOnline) {
            startListening();
        }
    }

    private void startListening() {
        new Thread(() -> {
            try {
                while (true) {
                    String update = client.receiveUpdate();

                    System.out.println(update);

                    if (update.startsWith("INIT:")) {
                        Platform.runLater(() -> initializeWorld(update));
                    } else if (update.startsWith("UPDATE:")) {
                        Platform.runLater(() -> updateWorld(update));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void initializeWorld(String initData) {
        world.clearEntities();
        root.getChildren().clear();

        String[] entities = initData.substring(5).split(";");
        for (String entityData : entities) {
            String[] parts = entityData.split(",");
            if (parts.length == 5) {
                String type = parts[0];
                double x = Double.parseDouble(parts[2]);
                double y = Double.parseDouble(parts[3]);
                double radius = Double.parseDouble(parts[4]);

                GameEntity entity = EntityFactory.createEntity(type, x, y, radius);
                world.addEntity(entity);
                root.getChildren().add(entity.getShape());
            }
        }
    }



    private void updateWorld(String update) {
        world.clearEntities();
        root.getChildren().clear();

        String[] entities = update.substring(7).split(";");
        for (String entityData : entities) {
            String[] parts = entityData.split(",");
            if (parts.length == 4) {
                String type = parts[0];
                double x = Double.parseDouble(parts[1]);
                double y = Double.parseDouble(parts[2]);
                double radius = Double.parseDouble(parts[3]);

                GameEntity entity = EntityFactory.createEntity(type, x, y, radius);
                world.addEntity(entity);
                root.getChildren().add(entity.getShape());
            }
        }
    }

    public Scene getScene() {
        return scene;
    }
}

