package info.prog.agario.model.entity.player;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.UUID;

public class Player {
    private UUID ID;
    private PlayerGroup playerGroup;
    private String pseudo;
    private Text pseudoText;
    private Color color;

    private ArrayList<Cell> cells;

    public Player(double x, double y, double mass, String pseudo, UUID uuids, UUID cellId) {
        this.ID = uuids;
        this.pseudo = pseudo;
        this.color = Color.hsb(Math.random() * 360, 0.8, 0.9);
        this.playerGroup = new PlayerGroup();
        Cell firstCell = new Cell(x, y, mass, this.color, cellId);
        firstCell.setParentGroup(this.playerGroup);
        playerGroup.addComponent(firstCell);
        this.cells = (ArrayList<Cell>) playerGroup.getCells();
    }

    public void divide() {
        playerGroup.divide();
    }

    public void move(double dx, double dy) {
        System.out.println("Playergroup doit bouger");
        playerGroup.move(dx, dy);
    }

    public PlayerGroup getPlayerGroup() {
        return playerGroup;
    }

    public Text getPseudoText() {
        return pseudoText;
    }

    public double getX() {
        return playerGroup.getCells().get(0).getX();
    }

    public double getY() {
        return playerGroup.getCells().get(0).getY();
    }

    public ArrayList<Cell> getCells() {
        return  cells;
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