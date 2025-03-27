package info.prog.agario.model.entity.ai;

import info.prog.agario.launcher.GameLauncher;
import info.prog.agario.model.entity.GameEntity;
import info.prog.agario.model.entity.player.AliveEntity;
import info.prog.agario.model.entity.player.Cell;
import info.prog.agario.model.entity.player.PlayerGroup;
import info.prog.agario.model.world.GameWorld;
import info.prog.agario.utils.AnimationUtils;
import javafx.scene.paint.Color;

import java.util.Random;

public class Enemy extends AliveEntity {
    private final Color color;
    private Strategy strat;
    private PlayerGroup enemyGroup;

    private String pseudo;

    private GameWorld world;

    public Enemy(double x, double y, double mass, GameWorld world) {
        super(x, y, 10 * Math.sqrt(mass));
        this.color = Color.hsb(Math.random() * 360, 0.8, 0.9);
        this.enemyGroup = new PlayerGroup();
        this.world = world;
        Cell firstCell = new Cell(x, y, mass, this.color, "[Bot] " + GameLauncher.RANDOM_PSEUDOS[new Random().nextInt(GameLauncher.RANDOM_PSEUDOS.length)]);
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

    public String getPseudo(){
        return pseudo;
    }
    public void move() throws InterruptedException {
        strat.movement();
    }

    public PlayerGroup getEnemyGroup() {
        return enemyGroup;
    }
}
