package info.prog.agario.model.entity;

import info.prog.agario.model.entity.player.Cell;
import javafx.scene.paint.Color;

public class EntityFactory {
    public static GameEntity createEntity(String type, double x, double y, double param) {
        switch (type.toLowerCase()) {
            case "pellet":
                return new Pellet(x, y);
            case "cell":
                return new Cell(x, y, param, Color.hsb(Math.random() * 360, 0.8, 0.9));
            default:
                throw new IllegalArgumentException("Type d'entité inconnu: " + type);
        }
    }
}