
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class CheckersClient extends Thread {
    private ClientGUI gui = null;

    private Socket clientSocket = null;

    private PrintWriter out = null;

    private BufferedReader in = null;

    private boolean listening = true;

    private static class Message {
        public String sender;

        public String content;

        public Message(String plainText) {
            this.sender = plainText.substring(plainText.indexOf("###sentfrom=") + 12, plainText.indexOf("###message="));
            this.content = plainText.substring(plainText.indexOf("###message=") + 11, plainText.length());
        }

        public String toString() {
            return "Sender: " + this.sender + " Message: " + this.content;
        }
    }

    public CheckersClient(ClientGUI gui) {
        this.gui = gui;
    }

    public int connect(String IPAddress, int port, String name) {
        try {
            this.clientSocket = new Socket(IPAddress, port);
            this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            sendMessage("server", "###name=" + name + "###");
            return 1;
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host");
            return 0;
        } catch (IOException e) {
            System.err.println("Couldn't connect to host");
            return 0;
        }
    }

    public int disconnect() {
        sendMessage("server", "###disconnecting###");
        this.listening = false;
        return 1;
    }

    public int sendMessage(String destination, String message) {
        this.out.println("###sendto=" + destination + "###message=" + message);
        return 1;
    }

    public void run() {
        try {
            String fromServer;
            while ((fromServer = this.in.readLine()) != null && this.listening) {
                Message message = new Message(fromServer);
                if (message.sender.equals("server")) {
                    if (message.content.equals("###disconnected###")) {
                        this.gui.disconnect("Server disconnected");
                        continue;
                    }
                    if (message.content.equals("###too_many_connections###")) {
                        this.gui.disconnect("Too many connections to server");
                        continue;
                    }
                    if (message.content.equals("###name_already_taken###")) {
                        this.gui.disconnect("That name is already taken");
                        continue;
                    }
                    if (message.content.contains("###name_list=")) {
                        String[] names = message.content.substring(13, message.content.length() - 3).split(",");
                        this.gui.setUserList(names);
                    }
                    continue;
                }
                if (message.content.equals("###potential_chat_disconnected###")) {
                    this.gui.connectionDied(message.sender);
                    continue;
                }
                if (message.content.contains("###game_already_exists")) {
                    String b = message.content.substring(message.content.indexOf("###board=") + 9, message.content.indexOf("###turn="));
                    String t = message.content.substring(message.content.indexOf("###turn=") + 8, message.content.length() - 3);
                    this.gui.updateGame(message.sender, b, t, "Opened an existing game.");
                    continue;
                }
                if (message.content.contains("###new_game_started")) {
                    String b = message.content.substring(message.content.indexOf("###board=") + 9, message.content.length() - 3);
                    this.gui.updateGame(message.sender, b, "You have started a new game.");
                    continue;
                }
                if (message.content.contains("###new_game_restarted")) {
                    String b = message.content.substring(message.content.indexOf("###board=") + 9, message.content.indexOf("###turn="));
                    String t = message.content.substring(message.content.indexOf("###turn=") + 8, message.content.length() - 3);
                    this.gui.updateGame(message.sender, b, t, "A new game has been started.");
                    continue;
                }
                if (message.content.contains("###checkers_move")) {
                    String b = message.content.substring(message.content.indexOf("###new_board=") + 13, message.content.length() - 3);
                    this.gui.updateGame(message.sender, b, "New move from " + message.sender + ".");
                    continue;
                }
                if (message.content.equals("###you_won###")) {
                    this.gui.notifyWin(message.sender);
                    continue;
                }
                this.gui.recievedMessage(message.sender, message.content);
            }
            this.in.close();
            this.out.close();
            this.clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error listning to server input");
            System.exit(1);
        }
    }
}
