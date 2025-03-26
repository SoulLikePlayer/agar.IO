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
                if (cell != other) {
                    if (cell.canMerge((Cell) other)) {
                        cell.merge(other);
                        break;
                    } else {
                        repelCells(cell, (Cell) other);
                    }
                }
            }
        }
    }

    private void repelCells(Cell c1, Cell c2) {
        double dx = c2.getX() - c1.getX();
        double dy = c2.getY() - c1.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < (c1.getRadius() + c2.getRadius())) {
            double repelStrength = 5.0;

            double totalMass = c1.getMass() + c2.getMass();
            double influence = c2.getMass() / totalMass;

            double factor = repelStrength / Math.max(distance, 1);
            double repelX = dx * factor;
            double repelY = dy * factor;

            c1.move(-repelX * influence, -repelY * influence);
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
