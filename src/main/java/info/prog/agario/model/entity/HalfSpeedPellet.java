package info.prog.agario.model.entity;

import info.prog.agario.model.entity.player.Cell;
import info.prog.agario.view.GameView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class HalfSpeedPellet extends SpecialPellet{
    public static boolean isOnEffect;

    public HalfSpeedPellet(double x, double y) {
        super(x, y);
        isOnEffect = false;
    }


    @Override
    public void PlayEffect(Cell cell) {
        System.out.println(cell.GetSpeedMultiplier());
        if(!isOnEffect){
            isOnEffect = true;
            double tmp = cell.GetSpeedMultiplier();
            cell.setSpeedMultiplier(cell.GetSpeedMultiplier()/2);

            System.out.println(cell.GetSpeedMultiplier());
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(3), e -> {
                        cell.setSpeedMultiplier(tmp);
                        isOnEffect = false;

                        System.out.println(cell.GetSpeedMultiplier());
                    })
            );
            timeline.play();


        }

    }

    @Override
    public void ExplosionEffect(Cell cell, Pane root) {

    }
}
