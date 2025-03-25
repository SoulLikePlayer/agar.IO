package info.prog.agario.model.entity.player;

public interface PlayerComponent {
    double getMass();
    void move(double dx, double dy);
    void divide();
    void merge(PlayerComponent other);
}
