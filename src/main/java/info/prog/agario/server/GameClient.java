package info.prog.agario.server;

import info.prog.agario.model.world.GameWorld;

import java.io.*;
import java.net.*;

public class GameClient {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 12345;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private GameWorld gameWorld;

    public static void main(String[] args) {
        new GameClient().connect();
    }

    public void connect() {
        try {
            if (!isServerAvailable()) {
                System.out.println("Aucun serveur trouvé, lancement d'un nouveau serveur.");
                new Thread(() -> new GameServer().start()).start();
                Thread.sleep(1000);  // Attendre un peu pour que le serveur démarre
            }
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connecté au serveur !");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            gameWorld = new GameWorld("Player1");

            new Thread(this::listenForMessages).start();
            out.println("READY");


        } catch (IOException | InterruptedException e) {
            System.out.println("Impossible de se connecter au serveur.");
            e.printStackTrace();
        }
    }

    private boolean isServerAvailable() {
        try (Socket socketTest = new Socket()) {
            socketTest.connect(new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT), 1000);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void listenForMessages() {
        String message;
        try {
            while ((message = in.readLine()) != null) {
                if ("UPDATE_GAME_STATE".equals(message)) {
                    System.out.println("Mise à jour du jeu...");
                    // Traitement de l'état du jeu
                    String gameState = in.readLine();  // Récupère l'état complet du jeu
                    System.out.println(gameState);  // Affichage de l'état du jeu dans la console
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
