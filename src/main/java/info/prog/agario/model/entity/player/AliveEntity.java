package info.prog.agario.model.entity.player;

import info.prog.agario.model.entity.GameEntity;

public class AliveEntity extends GameEntity {

    /**
     * Constructor
     * @param x the x position of the entity
     * @param y the y position of the entity
     * @param radius the radius of the entity
     */
    public AliveEntity(double x, double y, double radius) {
        super(x, y, radius);
    }

    /**
     * Get the pseudo of the entity
     * @return String the pseudo of the entity
     */
    public String getPseudo(){
        return "blablabla";
    }

}
