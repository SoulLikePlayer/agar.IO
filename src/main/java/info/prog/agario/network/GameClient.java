package info.prog.agario.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class GameClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private int playerId;

    public GameClient(String serverAddress, int serverPort) throws IOException{
        socket = new Socket()
    }
}
