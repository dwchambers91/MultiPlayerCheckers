

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
    private CheckersServer server = null;
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private boolean listening = true;
    private boolean ready = false;

    public String name = "";

    private static class Message {
        public String recip;
        public String content;

        public Message(String plainText) {
            recip = plainText.substring(plainText.indexOf("###sendto")+10, plainText.indexOf("###message="));
            content = plainText.substring(plainText.indexOf("###message")+11, plainText.length());
        }

        @Override
        public String toString() {
            return "Recipiant: " + recip + " Message: " + content;
        }
    }

    public ServerThread(CheckersServer server, Socket socket) {
        super("CheckersServerThread");
        this.socket = socket;
        this.server = server;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("Cannot initial I/O for this thread.");
        }
    }

    public Socket getSocket() {
        return this.socket;
    }

    public void sendMessage(String sender, String incMessage) {
        out.println("###sentfrom=" + sender + "###message=" + incMessage);
    }

    public void kill() {
        listening = false;
    }

    @Override
    public void run() {
        try {
            String inputLine;

            while ((inputLine = in.readLine()) != null && listening) {
                Message message = new Message(inputLine);
                if (message.recip.equals("server")) {
                    if (message.content.equals("###disconnecting###")) {
                        server.sendMessage("all", name, "###potential_chat_disconnected###");
                        break;
                    }
                    else if (message.content.contains("###name=")) {
                        name = message.content.substring(message.content.indexOf("=")+1, message.content.length()-3);

                        int res = server.addClient(this);
                        if (res == 0) {
                            ready = true;
                        } else if (res == 1) {
                            sendMessage("server", "###too_many_connections###");
                            break;
                        } else if (res == 2) {
                            sendMessage("server", "###name_already_taken###");
                            break;
                        }
                    }
                } else {
                    if (ready) {
                        if (message.content.equals("###new_game_window###")) {
                            server.newGame(name, message.recip);
                        }
                        else if (message.content.contains("###new_move")) {
                            server.gameMove(name, message.recip, message.content);
                        }
                        else if (message.content.equals("###game_over###you_win###")) {
                            server.endGame(name, message.recip);
                        }
                        else if (message.content.equals("###new_game_restarted###")) {
                            server.restartGame(name, message.recip);
                        }

                        else {
                            server.sendMessage(message.recip, name, message.content);
                        }
                    }
                }
            }

            out.close();
            in.close();
            socket.close();
            if (ready) {
                server.removeClient(this);
            }

        } catch (IOException e) {}
    }
}
