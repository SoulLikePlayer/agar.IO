package info.prog.agario.model.entity;



import info.prog.agario.model.entity.player.Cell;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class HalfMassPellet extends SpecialPellet{
    public HalfMassPellet(double x, double y) {
        super(x, y);
    }

    @Override
    public void PlayEffect(Cell cell) {


        if(cell.getMass()>= 10){
            cell.setMass((cell.getMass()-10)/2);
        }



    }

    @Override
    public void ExplosionEffect(Cell cell, Pane root) {

    }

}
