package info.prog.agario.model.world;

import info.prog.agario.model.entity.GameEntity;
import java.util.List;
import java.util.ArrayList;

public class QuadTree {
    private List<GameEntity> entities;

    public QuadTree() {
        entities = new ArrayList<>();
    }

    public void insert(GameEntity entity) {
        entities.add(entity);
    }

    public List<GameEntity> retrieve(GameEntity entity) {
        return entities;
    }
}
