package info.prog.agario.controller;

import info.prog.agario.model.entity.player.Player;
import info.prog.agario.model.world.GameWorld;
import info.prog.agario.network.GameClient;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyEvent;

public class GameNetworkController extends GameController{
    private GameWorld world;
    private GameClient client;
    private double mouseX, mouseY;
    private long lastSendTime = 0;
    private static final long SEND_INTERVAL = 50;

    public GameNetworkController(GameWorld gameWorld, Pane root, GameClient client){
        super(gameWorld, root);
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

}
