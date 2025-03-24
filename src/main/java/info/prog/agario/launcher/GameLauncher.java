package info.prog.agario.launcher;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import info.prog.agario.view.GameView;

public class GameLauncher extends Application {
    @Override
    public void start(Stage primaryStage) {
        VBox layout = new VBox(10);
        TextField pseudoField = new TextField();
        pseudoField.setPromptText("Entrez votre pseudo");
        Button localButton = new Button("Jouer en Local");
        Button onlineButton = new Button("Jouer en Ligne");

        localButton.setOnAction(e -> launchGame(primaryStage, pseudoField.getText().trim(), false));
        onlineButton.setOnAction(e -> launchGame(primaryStage, pseudoField.getText().trim(), true));

        layout.getChildren().addAll(pseudoField, localButton, onlineButton);
        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Agar.io JavaFX");
        primaryStage.show();
    }

    private void launchGame(Stage stage, String pseudo, boolean online) {
        if (pseudo.isEmpty()) {
            pseudo = "Joueur";
        }
        if (online) {
            System.out.println("Connexion au mode en ligne... (fonctionnalité non encore implémentée)");
            return;
        }
        GameView gameView = new GameView(pseudo);
        stage.setScene(gameView.getScene());
        stage.setTitle("Agar.io - " + pseudo);
    }
}
