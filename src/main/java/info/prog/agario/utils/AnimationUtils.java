package info.prog.agario.utils;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class AnimationUtils {
    public static void playGrowAnimation(Node node) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), node);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(2);
        scaleTransition.play();
    }

    public static void playPelletAbsorptionAnimation(Node pellet, Node player) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), pellet);
        transition.setToX(player.getTranslateX());
        transition.setToY(player.getTranslateY());
        transition.setOnFinished(e -> pellet.setVisible(false));
        transition.play();
    }

    public static void playReboundAnimation(Node node) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(300), node);
        scaleTransition.setToX(1.2);
        scaleTransition.setToY(1.2);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(2);
        scaleTransition.play();
    }

    public static void playHeartbeatAnimation(Node node) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(1000), node);
        scaleTransition.setToX(1.05);
        scaleTransition.setToY(1.05);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(ScaleTransition.INDEFINITE);
        scaleTransition.play();
    }
}
