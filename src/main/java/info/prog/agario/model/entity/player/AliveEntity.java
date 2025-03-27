package info.prog.agario.model.entity.player;

import info.prog.agario.model.entity.GameEntity;

public class AliveEntity extends GameEntity {
    public AliveEntity(double x, double y, double radius) {
        super(x, y, radius);
    }

    public String getPseudo(){
        return "blablabla";
    }

}
