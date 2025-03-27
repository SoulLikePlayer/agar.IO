package info.prog.agario.model.entity.player;

import info.prog.agario.model.entity.ai.Enemy;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Player extends AliveEntity{
    private PlayerGroup playerGroup ;
    private String pseudo;
    private Text pseudoText;
    private Color color;

    public Player(double x, double y, double mass, String pseudo) {
        super(x, y, 30);
        this.pseudo = pseudo;
        this.color = Color.hsb(Math.random() * 360, 0.8, 0.9);
        this.playerGroup = new PlayerGroup();
        Cell firstCell = new Cell(x, y, mass, this.color, pseudo);
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

    public String getPseudo() {
        return pseudoText.getText();
    }

    public double getX() {
        return playerGroup.getCells().get(0).getX();
    }

    public double getY() {
        return playerGroup.getCells().get(0).getY();
    }

}