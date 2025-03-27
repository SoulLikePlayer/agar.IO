package info.prog.agario.model.entity.player;

import java.util.List;

public interface PlayerComponent {
    PlayerComponent divide();
    List<Cell> getCells();

    double getMass();

    void move(double dx, double dy);
    void merge(PlayerComponent other);
}