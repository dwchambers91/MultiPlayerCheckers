

import java.io.IOException;
import java.net.ServerSocket;

public class FindClients extends Thread {
    private CheckersServer server = null;
    private ServerSocket serverSocket = null;
    private boolean keepSearching = true;

    public FindClients(CheckersServer server, ServerSocket serverSocket) {
        this.server = server;
        this.serverSocket = serverSocket;
    }

    public void endSearch() {
        keepSearching = false;
    }

    @Override
    public void run() {
        while (keepSearching) {
            try {
                ServerThread newThread = new ServerThread(server, serverSocket.accept());
                newThread.start();
            } catch(IOException e) {}
        }
    }
}
