

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;

public class CheckersServer extends Thread {
    private ServerSocket serverSocket = null;
    private FindClients clientListener = null;
    private ServerGUI gui = null;
    private HashMap clients = null;
    private HashMap games = null;
    private int num_connected = 0;
    private int max_connections = 10;
    private boolean listening = false;

    public CheckersServer(ServerGUI gui) {
        this.gui = gui;
    }

    public int addClient(ServerThread client) {
        if (clients.containsKey(client.name)) {
            System.err.println("Names bust be unique");
            return 2;
        } else if (num_connected >= max_connections) {
            System.err.println("Error: too many connections");
            return 1;
        } else {
            clients.put(client.name, client);
            num_connected += 1;
            sendClientList();
            return 0;
        }
    }

    public void removeClient(ServerThread client) {
        if (clients.containsKey(client.name)) {
            clients.remove(client.name);
            num_connected -= 1;
            sendClientList();
        } else {
            System.err.println("Error removing client");
        }
    }

    private void sendClientList() {
        String[] clientIPList = new String[clients.size()];
        ServerThread[] clientsList = (ServerThread[]) clients.values().toArray(new ServerThread[0]);
        String names = "###name_list=";

        for (int n = 0; n < clientsList.length; n++) {
            clientIPList[n] = String.valueOf(clientsList[n].getSocket().getRemoteSocketAddress()) + " " + clientsList[n].name;
            names += clientsList[n].name + ",";
        }
        names = names.substring(0, names.length() - 1) + "###";
        gui.writeClientList(clientIPList);

        for (int n = 0; n < clientsList.length; n++) {
            if (clientsList[n] != null) {
                clientsList[n].sendMessage("server", names);
            }
        }
    }

    public int startListener(int port) {
        try {
            serverSocket = new ServerSocket(port);
            clients = new HashMap();
            games = new HashMap();
            clientListener = new FindClients(this, serverSocket);
            clientListener.start();
            listening = true;
            return 1;
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port);
            return 0;
        } catch (IllegalArgumentException e) {
            System.err.println("No port");
            return 0;
        }
    }

    public int stopListening() {
        try {
            if (listening) {
                for (ServerThread client : (ServerThread[]) clients.values().toArray(new ServerThread[0])) {
                    client.sendMessage("server", "###disconnected###");
                    client.kill();
                }
                clientListener.endSearch();
                clientListener = null;
                serverSocket.close();
                gui.writeClientList(new String[0]);
                clients.clear();
                games.clear();
                num_connected = 0;
                listening = false;
            }
            return 1;
        } catch (IOException e) {
            System.err.println("Could not close server.");
            return 0;
        }
    }

    public void sendMessage(String reciever, String sender, String message) {
        if (clients.containsKey(reciever)) {
            ServerThread threadReciever = (ServerThread) clients.get(reciever);
            threadReciever.sendMessage(sender, message);
        } else if (reciever.equals("all")) {
            ServerThread[] clientsList = (ServerThread[]) clients.values().toArray(new ServerThread[0]);
            for (ServerThread c : clientsList) {
                c.sendMessage(sender, message);
            }
        } else {
            System.err.println("Error sending message " + reciever);
        }
    }

    public void newGame(String client1, String client2) {
        String gameString = "";
        if (games.containsKey(client1 + ":" + client2)) {
            gameString = client1 + ":" + client2;
        } else if (games.containsKey(client2 + ":" + client1)) {
            gameString = client2 + ":" + client1;
        }

        if (!gameString.equals("")) {
            Checkers game = (Checkers) games.get(gameString);
            String board;
            if (gameString.substring(0, gameString.indexOf(":")).equals(client1)) {
                board = game.getBoard();
            } else {
                board = game.getRotatedBoard(game.getBoard());
            }
            sendMessage(client1, client2, "###game_already_exists###board=" + board + "###turn=" + game.getTurn() + "###");
        } else {
            Checkers game = new Checkers(client1, client2);
            games.put(client1 + ":" + client2, game);
            sendMessage(client1, client2, "###new_game_started###board=" + game.getBoard() + "###");
        }
    }

    public void endGame(String loser, String winner) {
        String gameString = "";
        if (games.containsKey(loser + ":" + winner)) {
            gameString = loser + ":" + winner;
        } else if (games.containsKey(winner + ":" + loser)) {
            gameString = winner + ":" + loser;
        } else {
            System.err.println("game does not existt");
        }

        sendMessage(winner, loser, "###you_won###");
        games.remove(gameString);
    }

    public void restartGame(String client1, String client2) {
        Checkers game = new Checkers(client1, client2);
        games.put(client1 + ":" + client2, game);
        sendMessage(client1, client2,
                "###new_game_restarted###board=" + game.getBoard() + "###turn=" + game.getTurn() + "###");
        sendMessage(client2, client1,
                "###new_game_restarted###board=" + game.getRotatedBoard(game.getBoard()) + "###turn=" + game.getTurn() + "###");
    }

    public void gameMove(String from, String to, String message) {
        String gameString = "";
        if (games.containsKey(from + ":" + to)) {
            gameString = from + ":" + to;
        } else if (games.containsKey(to + ":" + from)) {
            gameString = to + ":" + from;
        } else {
            System.err.println("Error game doesnt exist");
        }

        Checkers game = (Checkers) games.get(gameString);

        int[][] realBoard = new int[8][8];
        String res = message.substring(message.indexOf("###new_board=") + 14, message.length() - 4);
        String[] rows = res.split("\\],\\[");
        rows[0] = rows[0].substring(1, rows[0].length());
        rows[7] = rows[7].substring(0, rows[7].length() - 1);

        for (int y = 0; y < 8; y++) {
            String chars[] = rows[y].split(",");
            for (int x = 0; x < 8; x++) {
                realBoard[y][x] = Integer.parseInt(chars[x]);
            }
        }

        game.setBoard(realBoard);
        game.changeTurns();
        String newBoard;
        if (gameString.substring(0, gameString.indexOf(":")).equals(to)) {
            newBoard = game.getBoard();
        } else {
            newBoard = game.getRotatedBoard(game.getBoard());
        }
        sendMessage(to, from, "###checkers_move###new_board=" + newBoard + "###");
    }
}