package info.prog.agario.model.entity;

import info.prog.agario.model.entity.player.Cell;
import javafx.scene.paint.Color;

import java.util.UUID;

public class EntityFactory {
    public static GameEntity createEntity(String type, double x, double y, double param, UUID id, Color... color) {
        switch (type.toLowerCase()) {
            case "pellet":
                return new Pellet(x, y, id);
            case "invisiblepellet":
                return new InvisiblePellet(x, y);
            case "doublespeedpellet":
                return new DoubleSpeedPellet(x, y);
            case "halfspeedpellet":
                return new HalfSpeedPellet(x, y);
            case "doublegainpellet":
                return new DoubleGainPellet(x, y);
            case "halfgainpellet":
                return new HalfGainPellet(x, y);
            case "doublemasspellet":
                return new DoubleMassPellet(x, y);
            case "halfmasspellet":
                return new HalfMassPellet(x, y);
            case "explosionpellet" :
                return new ExplosionPellet(x,y);
            case "cell":
                Color cellColor = (color != null && color.length > 0) ? color[0] : Color.hsb(Math.random() * 360, 0.8, 0.9);
                return new Cell(x, y, param, cellColor, UUID.randomUUID());
            default:
                throw new IllegalArgumentException("Type d'entité inconnu: " + type);
        }
    }
}