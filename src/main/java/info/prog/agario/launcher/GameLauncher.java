package info.prog.agario.launcher;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import info.prog.agario.view.GameView;

public class GameLauncher extends Application {
    @Override
    public void start(Stage primaryStage) {
        Button localButton = new Button("Jouer en Local");
        Button onlineButton = new Button("Jouer en Ligne");

        localButton.setOnAction(e -> launchGame(primaryStage));
        onlineButton.setOnAction(e -> launchGame(primaryStage));

        VBox layout = new VBox(10, localButton, onlineButton);
        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Agar.io JavaFX");
        primaryStage.show();
    }

    private void launchGame(Stage stage) {
        GameView gameView = new GameView();
        stage.setScene(gameView.getScene());
        stage.setTitle("Agar.io");
    }
}
