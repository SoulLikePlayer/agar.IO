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


    public void divideParentGroup(PlayerGroup playerGroup, Pane root){
        playerGroup.divide();

        List<Cell> updatedCells = playerGroup.getCells();
        for (Cell cell : updatedCells) {
            if (!root.getChildren().contains(cell.getShape())) {
                root.getChildren().add(cell.getShape());
                root.getChildren().add(cell.getPseudo());
                cell.getShape().toFront();
                cell.getPseudo().toFront();
            }
        }
    }

    public void ExplosionEffect(Cell cell, Pane root) {
        if(cell.getParentGroup()!= null){
            divideParentGroup(cell.getParentGroup(), root);

        }
    }
}