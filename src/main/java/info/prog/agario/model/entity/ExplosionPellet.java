package info.prog.agario.model.entity;

import info.prog.agario.model.entity.player.Cell;
import info.prog.agario.model.entity.player.Player;
import info.prog.agario.model.entity.player.PlayerComponent;
import info.prog.agario.model.entity.player.PlayerGroup;
import info.prog.agario.view.GameView;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExplosionPellet extends GameEntity {

    public ExplosionPellet(double x, double y) {
        super(x, y, 50);
        this.setMass(0);
        Circle mainCircle = new Circle(x, y, 50);
        mainCircle.setFill(Color.RED);
        mainCircle.setStroke(Color.DARKRED);
        mainCircle.setStrokeWidth(6);
        mainCircle.getStrokeDashArray().addAll(5d, 10d);
        this.shape = mainCircle;
    }


    public void divideParentGroup(PlayerGroup playerGroup){
        List<PlayerComponent> newCells = new ArrayList<>();
        for (PlayerComponent component : new ArrayList<>(playerGroup.getComponents())) {
            PlayerComponent divided = component.divide();
            if (divided instanceof Cell) {
                ((Cell) divided).setParentGroup(playerGroup);
                newCells.add(divided);
            }
        }
        playerGroup.getComponents().addAll(newCells);
    }

    public void ExplosionEffect(Cell cell, Pane root) {
        if(cell.getParentGroup()!= null){
            //cell.getParentGroup().divide();
            divideParentGroup(cell.getParentGroup());
            List<Cell> updatedCells = cell.getParentGroup().getCells();
            for (Cell cell1 : updatedCells) {
                if (!root.getChildren().contains(cell1.getShape())) {
                    root.getChildren().add(cell1.getShape());
                    root.getChildren().add(cell1.getPseudo());
                    cell1.getShape().toFront();
                }
            }
        }

    }
}