package info.prog.agario.model.entity.player;

import info.prog.agario.model.entity.ai.Enemy;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Player extends AliveEntity{
    private PlayerGroup playerGroup ;
    private UUID ID;
    private String pseudo;
    private Text pseudoText;
    private Color color;

    /**
     * Constructor of the player
     * @param x x position of the player
     * @param y y position of the player
     * @param mass mass of the player
     * @param pseudo pseudo of the player
     */
    public Player(double x, double y, double mass, String pseudo, UUID uuids, UUID cellId) {
        super(x, y, 30, uuids);
        this.pseudo = pseudo;
        this.color = Color.hsb(Math.random() * 360, 0.8, 0.9);
        this.playerGroup = new PlayerGroup();
        Cell firstCell = new Cell(x, y, mass, this.color, pseudo, cellId);
        firstCell.setParentGroup(this.playerGroup);
        playerGroup.addComponent(firstCell);
        this.pseudoText = new Text(pseudo);
        this.pseudoText.setFill(Color.WHITE);
        this.pseudoText.setStyle("-fx-font-weight: bold;");
    }

    /**
     * Method to devide the Player's PlayerGroup's cells
     */
    public void divide() {
        playerGroup.divide();
    }

    /**
     * Method to move the Player's PlayerGroup's cells
     * @param dx the x distance to move
     * @param dy the y distance to move
     */
    public void move(double dx, double dy) {
        playerGroup.move(dx, dy);
    }

    /**
     * Method to get the Player's PlayerGroup
     * @return PlayerGroup the Player's PlayerGroup
     */
    public PlayerGroup getPlayerGroup() {
        return playerGroup;
    }

    /**
     * Method to get the Player's pseudo
     * @return String the Player's pseudo
     */
    public String getPseudo() {
        return pseudoText.getText();
    }

    /**
     * Method to get the X position of the Player
     * @return double the X position of the Player
     */
    public double getX() {
        return playerGroup.getCells().get(0).getX();
    }

    /**
     * Method to get the Y position of the Player
     * @return double the Y position of the Player
     */
    public double getY() {
        return playerGroup.getCells().get(0).getY();
    }

    public List<Cell> getCells() {
        return  playerGroup.getCells();
    }

    public UUID getId() {
        return ID;
    }

    public void clearCell(){
        for(PlayerComponent component : playerGroup.getComponents()){
            playerGroup.removeComponent(component);
        }
    }
}