package info.prog.agario.launcher;

import info.prog.agario.network.GameClient;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import info.prog.agario.view.GameView;

import java.io.IOException;
import java.util.Random;

public class GameLauncher extends Application {
    private static final String[] RANDOM_PSEUDOS = {
            "Xx_DarkSasukeDu89_xX", "SeigneurDu98", "Pikachu_Sombre",
            "Naruto_LeBg", "JeanMichel_Gamer", "ProGamer666",
            "BigChungus2000", "MangeurDeQuiches", "MasterChief_PasContent"
    };

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
            pseudo = RANDOM_PSEUDOS[new Random().nextInt(RANDOM_PSEUDOS.length)];
        }
        if (online) {
            try{
                System.out.println("Connexion au serveur...");
                GameClient gameClient = new GameClient("10.42.17.102", 12345);

                System.out.println("Chargement du jeu en ligne");
                GameView gameView = new GameView(pseudo, gameClient);
                stage.setScene(gameView.getScene());
                stage.setTitle("Agar.io - "+pseudo);
            }catch (IOException e){
                System.out.println("Echec de connexion");
                e.printStackTrace();
            }
            return;
        }
        GameView gameView = new GameView(pseudo);
        stage.setScene(gameView.getScene());
        stage.setTitle("Agar.io - " + pseudo);
    }
}
