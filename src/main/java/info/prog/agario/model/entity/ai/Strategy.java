package info.prog.agario.model.entity.ai;

import info.prog.agario.model.entity.GameEntity;
import info.prog.agario.model.entity.player.Cell;

public interface Strategy {

    public void movement() throws InterruptedException;

}
