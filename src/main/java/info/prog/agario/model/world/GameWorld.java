package info.prog.agario.model.world;

import info.prog.agario.model.entity.EntityFactory;
import info.prog.agario.model.entity.GameEntity;
import info.prog.agario.model.entity.ai.Enemy;
import info.prog.agario.model.entity.player.Cell;
import info.prog.agario.model.entity.player.Player;

import java.util.*;

public class GameWorld {
    private QuadTree quadTree;
    private List<GameEntity> entities;
    private boolean isOnline;
    private Player player;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private int nbEnnemies = 5;

    public GameWorld(String pseudo) {
        this(pseudo, false);
    }

    public GameWorld(String pseudo, boolean isOnline) {
        this.isOnline = isOnline;
        entities = new ArrayList<>();
        quadTree = new QuadTree(new Boundary(0, 0, 2000, 2000));

        if (!isOnline) {
            player = new Player(300, 400, 10, pseudo, UUID.randomUUID(), UUID.randomUUID());
            System.out.println("Joueur créé avec " + player.getPlayerGroup().getCells().size() + " cellule(s)");
            generatePellets(200);

            Random r = new Random();
            for(int i = 0; i < nbEnnemies; i++){
                Enemy enemy = new Enemy(r.nextInt(0,2001), r.nextInt(0,2001), 10);
                enemies.add(enemy);
                entities.add(enemy);
            }
        }

    }

    public Player getPlayerById(UUID playerId){
        if(player != null && player.getId().equals(playerId)){
            return player;
        }
        return null;
    }


    private final NavigableMap<Integer, String> pelletProbabilities = new TreeMap<>();

    private void initializePelletProbabilities() {
        pelletProbabilities.put(5, "InvisiblePellet");
        pelletProbabilities.put(10, "DoubleSpeedPellet");
        pelletProbabilities.put(12, "HalfSpeedPellet");
        pelletProbabilities.put(13, "DoubleMassPellet");
        pelletProbabilities.put(14, "HalfMassPellet");
        pelletProbabilities.put(17, "DoubleGainPellet");
        pelletProbabilities.put(19, "HalfGainPellet");
        pelletProbabilities.put(25, "ExplosionPellet");
        pelletProbabilities.put(100, "Pellet");
    }

    private void generatePellets(int count) {
        Random random = new Random();
        if (pelletProbabilities.isEmpty()) {
            initializePelletProbabilities();
        }

        for (int i = 0; i < count; i++) {
            int rank = random.nextInt(100);
            String type = pelletProbabilities.ceilingEntry(rank).getValue();
            GameEntity pellet = EntityFactory.createEntity(type, random.nextDouble() * 2000, random.nextDouble() * 2000, 0, UUID.randomUUID());
            entities.add(pellet);
            quadTree.insert(pellet);
        }
    }

    public List<GameEntity> getEntities() {
        return entities;
    }

    public void clearEntities() {
        entities.clear();
    }

    public void clearEntityExceptPlaye(){
        entities.removeIf(e -> !(e instanceof Cell && getPlayer().getCells().contains(e)));
    }

    public void addEntity(GameEntity entity) {
        entities.add(entity);
        quadTree.insert(entity);
    }

    public void removeEntity(GameEntity entity){
        entities.remove(entity);
    }

    public Player getPlayer() {
        return player;
    }

    public synchronized void addPlayer(Player player) {
        this.player = player;

        for(Cell cell : player.getPlayerGroup().getCells()){
            entities.add(cell);
            quadTree.insert(cell);
        }
    }

    public ArrayList<Enemy> getEnemies(){ return enemies; }

    public QuadTree getQuadTree() {
        return quadTree;
    }
}

