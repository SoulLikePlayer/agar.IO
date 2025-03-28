package info.prog.agario.controller;

import info.prog.agario.model.entity.GameEntity;
import info.prog.agario.model.entity.Pellet;
import info.prog.agario.model.entity.player.Cell;
import info.prog.agario.model.entity.player.Player;
import info.prog.agario.model.world.GameWorld;
import info.prog.agario.network.GameClient;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;

public class GameNetworkController extends GameController{
    private GameWorld world;
    private GameClient client;
    private double mouseX, mouseY;
    private long lastSendTime = 0;
    private static final long SEND_INTERVAL = 50;
    private static final long UPDATE_INTERVAL = 16_000_000;
    private long lastUpdate = 0;


    public GameNetworkController(GameWorld gameWorld, Pane root, GameClient client){
        super(gameWorld, root,);
        this.world = gameWorld;
        this.client = client;
    }

    @Override
    public void initialize(){
        Pane root = super.root;
        root.setOnKeyPressed(this::handleKeyPress);
        root.setFocusTraversable(true);
        root.requestFocus();
        root.setOnMouseMoved(this::handleMouveMovement);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= UPDATE_INTERVAL) {
                    sendMovement();
                    lastUpdate = now;
                }
            }
        };
        timer.start();
    }

    private void handleMouveMovement(MouseEvent mouseEvent) {
        mouseX = mouseEvent.getX();
        mouseY = mouseEvent.getY();
        sendMovement();
    }

    private void sendMovement(){
        long now = System.currentTimeMillis();
        if(now - lastSendTime > SEND_INTERVAL) {
            Player player = world.getPlayer();
            if (player != null) {
                String message = "MOVE:" + mouseX + "," + mouseY + ";";
                client.sendMessage(message);
                lastSendTime = now;
            }
        }
    }

    private void handleKeyPress(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.SPACE){
            Player player = world.getPlayer();
            if (player != null){
                String message = "DIVIDE:"+player.getX()+","+player.getY()+";";
                client.sendMessage(message);
            }
        }
    }

    protected void update() {
        Player player = world.getPlayer();
        if (player == null) return;

        List<GameEntity> entitiesToRemove = new ArrayList<>();
        boolean absorbedSomething = false;

        for (GameEntity entity : world.getEntities()) {
            for (Cell playerCell : player.getPlayerGroup().getCells()) {
                if (playerCell.getShape().getBoundsInParent().intersects(entity.getShape().getBoundsInParent())) {
                    if (entity instanceof Pellet) {
                        String message = "PELLET_EATEN:" + entity.getID() + "," + playerCell.getId() + ";";
                        client.sendMessage(message);

                        playerCell.absorbPellet(entity);
                        entitiesToRemove.add(entity);
                        absorbedSomething = true;
                        break;
                    }
                }
            }
            if (absorbedSomething) break;
        }

        for (GameEntity entityToRemove : entitiesToRemove) {
            world.getEntities().remove(entityToRemove);
            root.getChildren().remove(entityToRemove.getShape());
        }

        if (absorbedSomething) {
            smallestInFront();
        }
    }
}
