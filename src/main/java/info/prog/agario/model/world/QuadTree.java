package info.prog.agario.model.world;

import info.prog.agario.model.entity.GameEntity;
import java.util.ArrayList;
import java.util.List;

public class QuadTree {
    private static final int CAPACITY = 8;
    private List<GameEntity> entities;
    private Boundary boundary;
    private boolean divided = false;

    private QuadTree northeast, northwest, southeast, southwest;

    public QuadTree(Boundary boundary) {
        this.boundary = boundary;
        this.entities = new ArrayList<>();
    }

    public void insert(GameEntity entity) {
        if (!boundary.contains(entity.getX(), entity.getY())) {
            return;
        }

        if (entities.size() < CAPACITY && !divided) {
            entities.add(entity);
            return;
        }

        if (!divided) {
            subdivide();
        }

        if (northeast.boundary.contains(entity.getX(), entity.getY())) {
            northeast.insert(entity);
        } else if (northwest.boundary.contains(entity.getX(), entity.getY())) {
            northwest.insert(entity);
        } else if (southeast.boundary.contains(entity.getX(), entity.getY())) {
            southeast.insert(entity);
        } else {
            southwest.insert(entity);
        }
    }


    private void subdivide() {
        double x = boundary.getX();
        double y = boundary.getY();
        double w = boundary.getWidth() / 2;
        double h = boundary.getHeight() / 2;

        northeast = new QuadTree(new Boundary(x + w, y, w, h));
        northwest = new QuadTree(new Boundary(x, y, w, h));
        southeast = new QuadTree(new Boundary(x + w, y + h, w, h));
        southwest = new QuadTree(new Boundary(x, y + h, w, h));
        divided = true;
    }

    public List<GameEntity> retrieve(GameEntity entity, double horizon) {
        List<GameEntity> found = new ArrayList<>();

        Boundary searchArea = new Boundary(
                entity.getX() - horizon, entity.getY() - horizon,
                horizon * 2, horizon * 2
        );

        if (!boundary.intersects(searchArea)) {
            return found;
        }

        found.addAll(entities);

        if (divided) {
            found.addAll(northeast.retrieve(entity, horizon));
            found.addAll(northwest.retrieve(entity, horizon));
            found.addAll(southeast.retrieve(entity, horizon));
            found.addAll(southwest.retrieve(entity, horizon));
        }

        return found;
    }

    public void remove(GameEntity entity) {
        if (!boundary.contains(entity.getX(), entity.getY())) {
            return;
        }

        entities.remove(entity);

        if (divided) {
            northeast.remove(entity);
            northwest.remove(entity);
            southeast.remove(entity);
            southwest.remove(entity);
        }
    }

}
