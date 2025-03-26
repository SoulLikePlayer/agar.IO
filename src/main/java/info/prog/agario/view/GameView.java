package info.prog.agario.view;

import info.prog.agario.controller.GameController;
import info.prog.agario.model.entity.player.Player;
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
        world = new GameWorld(pseudo, isOnline);
        controller = new GameController(world, root);
        scene = new Scene(root, 2000, 2000);
        if(!isOnline){
            controller.initialize();
        }
        else{
            startListening();
        }


    }

    private void startListening() {
        new Thread(() -> {
            try {
                while (true) {
                    String update = client.receiveUpdate();

                    System.out.println("Reçu du serveur : " + update);

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
        //TODO : CREATION DU MONDE
    }



    private void updateWorld(String update) {
        //TODO : UPDATE
    }

    public Scene getScene() {
        return scene;
    }
}

