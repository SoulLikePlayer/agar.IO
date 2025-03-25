package info.prog.agario.model.entity.player;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Player {
    private PlayerGroup playerGroup;
    private String pseudo;
    private Text pseudoText;
    private Color color;

    public Player(double x, double y, double mass, String pseudo) {
        this.pseudo = pseudo;
        this.color = Color.hsb(Math.random() * 360, 0.8, 0.9);
        this.playerGroup = new PlayerGroup();
        Cell firstCell = new Cell(x, y, mass, this.color);
        firstCell.setParentGroup(this.playerGroup);
        playerGroup.addComponent(firstCell);
        this.pseudoText = new Text(pseudo);
        this.pseudoText.setFill(Color.WHITE);
        this.pseudoText.setStyle("-fx-font-weight: bold;");
    }

    public void divide() {
        playerGroup.divide();
    }

    public void move(double dx, double dy) {
        playerGroup.move(dx, dy);
    }

    public PlayerGroup getPlayerGroup() {
        return playerGroup;
    }

    public Text getPseudoText() {
        return pseudoText;
    }
}