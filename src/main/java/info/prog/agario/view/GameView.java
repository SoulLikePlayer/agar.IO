package info.prog.agario.view;

import info.prog.agario.controller.GameController;
import info.prog.agario.controller.GameNetworkController;
import info.prog.agario.model.entity.EntityFactory;
import info.prog.agario.model.entity.GameEntity;
import info.prog.agario.model.entity.player.Cell;
import info.prog.agario.model.entity.player.Player;
import info.prog.agario.model.world.GameWorld;
import info.prog.agario.model.world.MiniMap;
import info.prog.agario.network.GameClient;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.Objects;
import java.util.UUID;

public class GameView {
    private Scene scene;

    private Pane mainRoot;
    private Pane root;
    private GameWorld world;
    private GameController controller;
    private GameClient client;
    private boolean isOnline;
    private MiniMap miniMap;

    private GameNetworkController networkController;
    private volatile boolean isWorldInitialized = false;

    public GameView(String pseudo) {
        this(pseudo, null);
    }

    public GameView(String pseudo, GameClient client) {
        this.client = client;
        this.isOnline = (client != null);
        root = new Pane();
        world = new GameWorld(pseudo, isOnline);
        scene = new Scene(root, 2000, 2000);
        if(!isOnline){
            controller = new GameController(world, root);
            controller.initialize();
        }
        else{
            networkController = new GameNetworkController(world, root, client);
            networkController.initialize();
            startListening();
        }
    }


    private void startListening() {
        new Thread(() -> {
            try {
                while (true) {
                    String update = client.receiveUpdate();
                    System.out.println("Donnée reçue : "+update);
                    if (update.startsWith("INIT:")) {
                        Platform.runLater(() -> {
                            initializeWorld(update);
                            isWorldInitialized = true;
                            System.out.println("Monde initialisé avec succès");
                        });
                    } else if (update.startsWith("UPDATE:")) {
                        Platform.runLater(() -> {
                            if(isWorldInitialized){
                                updateWorld(update);
                            }else{
                                System.out.println("UPDATE reçu mais monde pas initialisée");
                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }



    private void initializeWorld(String initData) {
        System.out.println("Initialisation");
        world.clearEntities();
        String[] entities = initData.substring(5).split(";");
        for (String entityData : entities) {
            String[] parts = entityData.split(",");
            System.out.println(parts.length);
            if (parts.length >= 5) {
                String type = parts[0];
                double x = Double.parseDouble(parts[2]);
                double y = Double.parseDouble(parts[3]);
                if(Objects.equals(parts[0], "cell")){
                    double mass = Double.parseDouble(parts[4]);
                    System.out.println("mass : "+mass);
                    System.out.println("Création du player");
                    Player player = new Player(x, y, mass, "test", UUID.fromString(parts[6]), UUID.fromString(parts[1]));
                    System.out.println("Player : "+player);
                    world.addPlayer(player);
                    System.out.println(world.getPlayerById(player.getId()));
                    for(Cell cell : player.getPlayerGroup().getCells()){
                        root.getChildren().add(cell.getShape());
                    }
                }else{
                    double radius = Double.parseDouble(parts[4]);
                    GameEntity entity = EntityFactory.createEntity(type, x, y, radius);
                    world.addEntity(entity);
                    root.getChildren().add(entity.getShape());
                }

            }
        }
    }

    private void updateWorld(String update) {
        world.clearEntities();
        if(world.getPlayer() == null){
            System.err.println("ERREUR CRITIQUE : Player est null ");
            return;
        }
        String[] entities = update.substring(7).split(";");

        for (String entityData : entities) {
            String[] parts = entityData.split(",");
            if (parts.length == 7) {
                UUID playerId = UUID.fromString(parts[6]);
                double x = Double.parseDouble(parts[2]);
                double y = Double.parseDouble(parts[3]);
                double mass = Double.parseDouble(parts[4]);
                Color color = Color.valueOf(parts[5]);
                UUID cellId = UUID.fromString(parts[1]);


                Player existingPlayer = world.getPlayer();
                System.out.println("Joueur trouvée : "+world.getPlayer());

                if (existingPlayer != null) {
                    boolean cellFound = false;
                    for (Cell cell : existingPlayer.getPlayerGroup().getCells()) {
                        System.out.println("ID de la cell : "+cell.getId().toString());
                        System.out.println("ID de la cible : "+cellId);
                        if (cell.getId().toString().equals(cellId.toString())) {
                            cellFound = true;
                            System.out.println("mise a jour");
                            cell.getShape().setCenterX(x);
                            cell.getShape().setCenterY(y);
                            cell.setMass(mass);
                            break;
                        }
                    }
                    if (!cellFound){
                        Cell newCell = new Cell(x, y, mass, color, cellId);
                        root.getChildren().add(newCell.getShape());
                    }
                }
            }
        }
    }



    public Scene getScene() {
        return scene;
    }
}

