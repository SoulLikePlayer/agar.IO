package info.prog.agario.model.entity.player;

import java.util.List;

public interface PlayerComponent {
    double getMass();
    void move(double dx, double dy);
    PlayerComponent divide();
    void merge(PlayerComponent other);

    List<Cell> getCells();
}
