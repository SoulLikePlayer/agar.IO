package info.prog.agario.model.entity.player;

import info.prog.agario.controller.GameController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlayerGroup implements PlayerComponent {
    public static final int MAX_DIVISIONS = 16;
    private List<PlayerComponent> components = new ArrayList<>();

    /**
     * Add a component to the PlayerGroup
     * @param component the component to add
     */
    public void addComponent(PlayerComponent component) {
        components.add(component);
        //System.out.println("Ajout d'un composant : " + component);
    }

    /**
     * Remove a component from the PlayerGroup
     * @param component the component to remove
     */
    public void removeComponent(PlayerComponent component) {
        components.remove(component);
    }

    /**
     * Meyhos to get the mass of the PlayerGroup
     * @return double the mass of the PlayerGroup
     */
    @Override
    public double getMass() {
        return components.stream().mapToDouble(PlayerComponent::getMass).sum();
    }

    /**
     * Method to move the PlayerGroup
     * @param dx the x distance to move
     * @param dy the y distance to move
     */
    @Override
    public void move(double dx, double dy) {
        for (PlayerComponent component : components) {
            component.move(dx, dy);
        }
    }

    /***
     * Method to divide the PlayerGroup
     * @return PlayerComponent the divided PlayerComponent
     */
    @Override
    public PlayerComponent divide() {
        List<PlayerComponent> newCells = new ArrayList<>();
        int currentCellCount = getCells().size();
        ArrayList<PlayerComponent> componentsCopy = new ArrayList<>(components);
        componentsCopy.sort(Comparator.comparing(PlayerComponent::getMass));

        for (PlayerComponent component : componentsCopy) {
            if (currentCellCount + newCells.size() < MAX_DIVISIONS) {
                PlayerComponent divided = component.divide();
                if (divided instanceof Cell) {
                    ((Cell) divided).setParentGroup(this);
                    newCells.add(divided);
                }
            } else {
                break;
            }
        }
        components.addAll(newCells);
        return null;
    }

    /**
     * Method to merge the PlayerGroup's cells
     * @param other the PlayerComponent to merge with
     */
    @Override
    public void merge(PlayerComponent other) {
        if (other instanceof PlayerGroup) {
            for (PlayerComponent component : ((PlayerGroup) other).components) {
                merge(component);
            }
        } else if (other instanceof Cell) {
            List<Cell> cells = getCells();
            Cell cell1 = null;
            Cell cell2 = null;
            double minDistance = Double.MAX_VALUE;

            for (Cell c1 : cells) {
                if (c1 != other) {
                    double dx = c1.getX() - ((Cell) other).getX();
                    double dy = c1.getY() - ((Cell) other).getY();
                    double distance = Math.sqrt(dx * dx + dy * dy);
                    if (distance < minDistance) {
                        minDistance = distance;
                        cell1 = c1;
                        cell2 = (Cell) other;
                    }
                }
            }

            if (cell1 != null && cell2 != null && minDistance < (cell1.getRadius() + cell2.getRadius()) && GameController.intersectionPercentage(cell1, cell2) > 33) {
                cell1.merge(cell2);
            }

            for (int i = 0; i < cells.size(); i++) {
                for (int j = i + 1; j < cells.size(); j++) {
                    if (cells.get(i) != cells.get(j)) {
                        repelCells(cells.get(i), cells.get(j));
                    }
                }
            }
        }
    }

    /**
     * Method to repel a Cell if it enters another Cell
     * @param c1 the first Cell
     * @param c2 the second Cell
     */
    private void repelCells(Cell c1, Cell c2) {
        if (c1.canMerge(c2)) {
            return;
        }

        double dx = c2.getX() - c1.getX();
        double dy = c2.getY() - c1.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        double minDistance = c1.getRadius() + c2.getRadius();

        if (distance < minDistance) {
            double overlap = minDistance - distance;
            double repelStrength = 0.5;

            double totalMass = c1.getMass() + c2.getMass();
            double influenceC1 = c2.getMass() / totalMass;
            double influenceC2 = c1.getMass() / totalMass;

            double factor = overlap * repelStrength / Math.max(distance, 1);
            double repelX = dx * factor;
            double repelY = dy * factor;

            c1.move(-repelX * influenceC1, -repelY * influenceC1);
            c2.move(repelX * influenceC2, repelY * influenceC2);
        }
    }

    /**
     * Method to get the components of the PlayerGroup
     * @return List<PlayerComponent> the components of the PlayerGroup
     */
    public List<PlayerComponent> getComponents() {
        return components;
    }

    /**
     * Method to get the cells of the PlayerGroup
     * @return List<Cell> the cells of the PlayerGroup
     */
    public List<Cell> getCells() {
        List<Cell> cells = new ArrayList<>();
        for (PlayerComponent component : components) {
            cells.addAll(component.getCells());
        }
        return cells;
    }
}