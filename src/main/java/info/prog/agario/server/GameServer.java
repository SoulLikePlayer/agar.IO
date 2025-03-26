package info.prog.agario.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class GameServer {
    private static final int PORT = 12345;
    private final List<PrintWriter> clientWriters = new CopyOnWriteArrayList<>();
    private boolean running = true;

    public static void main(String[] args) {
        new GameServer().start();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur lancé sur le port " + PORT);
            ExecutorService clientPool = Executors.newCachedThreadPool();

            new Thread(this::gameLoop).start();

            while (running) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nouveau joueur connecté : " + clientSocket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clientPool.execute(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void gameLoop() {
        while (running) {
            sendGameStateToClients();
            try {
                Thread.sleep(33);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    synchronized void sendGameStateToClients() {
        for (PrintWriter out : clientWriters) {
            out.println("UPDATE_GAME_STATE");
        }
    }

    public synchronized void addClient(PrintWriter out) {
        clientWriters.add(out);
    }

    public synchronized void removeClient(PrintWriter out) {
        clientWriters.remove(out);
    }
}

class ClientHandler implements Runnable {
    private final Socket socket;
    private final GameServer server;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket, GameServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            server.addClient(out);

            String message;
            while ((message = in.readLine()) != null) {
                if ("READY".equals(message)) {
                    System.out.println("Client prêt !");
                } else {
                    server.sendGameStateToClients();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            server.removeClient(out);
        }
    }
}
