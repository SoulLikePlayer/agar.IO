package info.prog.agario.model.entity.player;

import java.util.ArrayList;
import java.util.List;

public class PlayerGroup implements PlayerComponent {
    private List<PlayerComponent> components = new ArrayList<>();

    public void addComponent(PlayerComponent component) {
        components.add(component);
        System.out.println("Ajout d'un composant : " + component);
    }

    public void removeComponent(PlayerComponent component) {
        components.remove(component);
    }

    @Override
    public double getMass() {
        return components.stream().mapToDouble(PlayerComponent::getMass).sum();
    }

    @Override
    public void move(double dx, double dy) {
        for (PlayerComponent component : components) {
            component.move(dx, dy);
        }
    }

    @Override
    public void divide() {
        List<PlayerComponent> componentsCopy = new ArrayList<>(components);
        for (PlayerComponent component : componentsCopy) {
            component.divide();
        }

    }

    @Override
    public void merge(PlayerComponent other) {
        if (other instanceof PlayerGroup) {
            components.addAll(((PlayerGroup) other).components);
        } else {
            components.add(other);
        }
    }

    public List<PlayerComponent> getComponents() {
        return components;
    }

    public List<Cell> getCells() {
        List<Cell> cells = new ArrayList<>();
        for (PlayerComponent component : components) {
            if (component instanceof Cell) {
                cells.add((Cell) component);
            }
        }
        return cells;
    }
}
