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
    public PlayerComponent divide() {
        List<PlayerComponent> newCells = new ArrayList<>();
        for (PlayerComponent component : new ArrayList<>(components)) {
            PlayerComponent divided = component.divide();
            if (divided instanceof Cell) {
                ((Cell) divided).setParentGroup(this);
                newCells.add(divided);
            }
        }
        components.addAll(newCells);
        return null;
    }

    @Override
    public void merge(PlayerComponent other) {
        if (other instanceof PlayerGroup) {
            for (PlayerComponent component : ((PlayerGroup) other).components) {
                merge(component);
            }
        } else if (other instanceof Cell) {
            List<Cell> cells = getCells();

            for (Cell cell : cells) {
                if (cell != other && cell.canMerge((Cell)other)) {
                    cell.merge(other);
                    break;
                }
            }
        }
    }

    public List<PlayerComponent> getComponents() {
        return components;
    }

    public List<Cell> getCells() {
        List<Cell> cells = new ArrayList<>();
        for (PlayerComponent component : components) {
            cells.addAll(component.getCells());
        }
        return cells;
    }
}