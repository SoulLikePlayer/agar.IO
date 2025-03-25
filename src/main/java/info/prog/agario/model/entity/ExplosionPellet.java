package info.prog.agario.model.entity;

import info.prog.agario.model.entity.player.Cell;
import info.prog.agario.model.entity.player.PlayerGroup;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;
import java.util.Random;

public class ExplosionPellet extends SpecialPellet {

    public ExplosionPellet(double x, double y) {
        super(x, y);
        this.radius = new SimpleDoubleProperty(50);
        this.setMass(0); // La pastille a une masse fixe pour la comparaison

        Circle mainCircle = new Circle(x, y, 50);
        mainCircle.setFill(Color.RED); // Couleur distincte
        mainCircle.setStroke(Color.DARKRED);
        mainCircle.setStrokeWidth(6);
        mainCircle.getStrokeDashArray().addAll(5d, 10d);
        this.shape = mainCircle;
    }

    @Override
    public void PlayEffect(Cell cell) {

    }

    public void ExplosionEffect(Cell cell, Pane root) {
        if(cell.getParentGroup()!= null){
            cell.getParentGroup().divide();
            List<Cell> updatedCells = cell.getParentGroup().getCells();
            for (Cell cell1 : updatedCells) {
                if (!root.getChildren().contains(cell1.getShape())) {
                    root.getChildren().add(cell1.getShape());
                    cell.getShape().toFront();
                }
            }
        }

    }
}