package info.prog.agario.model.entity.ai;

import info.prog.agario.launcher.GameLauncher;
import info.prog.agario.model.entity.GameEntity;
import info.prog.agario.model.entity.player.AliveEntity;
import info.prog.agario.model.entity.player.Cell;
import info.prog.agario.model.entity.player.PlayerGroup;
import info.prog.agario.model.world.GameWorld;
import info.prog.agario.utils.AnimationUtils;
import javafx.scene.paint.Color;
import java.util.UUID;
import java.util.Random;


public class Enemy extends AliveEntity {
    private final Color color;
    private Strategy strat;
    private PlayerGroup enemyGroup;

    private String pseudo;

    private GameWorld world;

    /**
     * Constructor of the Enemy class
     * @param x The x position of the enemy
     * @param y The y position of the enemy
     * @param mass The mass of the enemy
     * @param world The game world
     */
    public Enemy(double x, double y, double mass, GameWorld world){
        super(x, y, 10 * Math.sqrt(mass), UUID.randomUUID());
        this.color = Color.hsb(Math.random() * 360, 0.8, 0.9);
        this.enemyGroup = new PlayerGroup();
        this.world = world;
        this.pseudo = GameLauncher.RANDOM_PSEUDOS[new Random().nextInt(GameLauncher.RANDOM_PSEUDOS.length)];
        Cell firstCell = new Cell(x, y, mass, this.color, pseudo, UUID.randomUUID());
        firstCell.setParentGroup(this.enemyGroup);
        enemyGroup.addComponent(firstCell);
        Random r = new Random();
        int nStrat = r.nextInt(3);
        switch (nStrat){
            case 0 : strat = new CellEatingMovement(this.enemyGroup, this.world.getPlayer().getPlayerGroup()); break;
            case 1 : strat = new PelletMovement(this.enemyGroup, this.world.getQuadTree()); break;
            default : strat = new RandomMovement(this.enemyGroup); break;
        }
        try {
            this.move();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Method to get the pseudo of the enemy
     * @return String the pseudo of the enemy
     */
    public String getPseudo(){
        return pseudo;
    }

    /**
     * Method to move the enemy
     */
    public void move() throws InterruptedException {
        strat.movement();
    }

    /**
     * Method to get the group of cells of the enemy
     * @return PlayerGroup the group of cells of the enemy
     */
    public PlayerGroup getEnemyGroup() {
        return enemyGroup;
    }
}
