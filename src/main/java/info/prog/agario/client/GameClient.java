package info.prog.agario.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.util.Random;

public class GameClient extends Application {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 12345;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private String playerName;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox layout = new VBox(10);
        TextField pseudoField = new TextField();
        pseudoField.setPromptText("Entrez votre pseudo");
        Button connectButton = new Button("Se connecter");

        connectButton.setOnAction(e -> {
            playerName = pseudoField.getText().trim();
            if (playerName.isEmpty()) {
                playerName = "Joueur_" + new Random().nextInt(10000);
            }
            connectToServer(playerName);
        });

        layout.getChildren().addAll(pseudoField, connectButton);
        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Agar.io - Connexion");
        primaryStage.show();
    }

    private void connectToServer(String playerName) {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(playerName);
            new Thread(this::listenForMessages).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenForMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Message du serveur: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
