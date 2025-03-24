package info.prog.agario.model.entity;

public class Player extends GameEntity {
    private double mass;

    public Player(double x, double y, double mass) {
        super(x, y, Math.sqrt(mass) * 10);
        this.mass = mass;
    }

    public void absorb(GameEntity entity) {
        this.mass += entity.getShape().getRadius();
        this.radius.set(Math.sqrt(mass) * 10);
    }
}
