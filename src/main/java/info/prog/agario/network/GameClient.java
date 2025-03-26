package info.prog.agario.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private int playerId;

    public GameClient(String serverAddress, int serverPort) throws IOException{
        socket = new Socket(serverAddress, serverPort);

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String response = in.readLine();
        if (response.startsWith("ID: ")){
            System.out.println("Connecté au serveur : ID "+playerId);
        }else{
            throw new IOException("Réponse inatendue du serveur : "+ response);
        }
    }

    public void sendMessage(String message){
        out.println(message);
    }

    public String receiveUpdate() throws IOException{
        String message = in.readLine();
        return message.trim();
    }

    public int getPlayerId(){
        return playerId;
    }

    public void close() throws IOException{
        socket.close();
    }
}
